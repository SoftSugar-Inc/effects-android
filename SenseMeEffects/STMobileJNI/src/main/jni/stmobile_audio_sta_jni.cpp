#include <jni.h>
#include <android/log.h>
#include "utils.h"
#include <st_mobile_audio_sta.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include "prebuilt/include/st_mobile_common.h"
#include <fstream>
#define  LOG_TAG    "STMobileAudioStaNative"

jobject mObj = NULL;
JavaVM *global_jvm;

JNIEnv *get_env(int *attach) {
    if (global_jvm == NULL) return NULL;
    *attach = 0;
    JNIEnv *jni_env = NULL;
    int status = global_jvm->GetEnv((void **)&jni_env, JNI_VERSION_1_6);
    if (status == JNI_EDETACHED || jni_env == NULL) {
        status = global_jvm->AttachCurrentThread(&jni_env, NULL);
        if (status < 0) {
            jni_env = NULL;
        } else {
            *attach = 1;
        }
    }
    return jni_env;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    global_jvm = vm;

    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }

    return JNI_VERSION_1_4;
}

static inline jfieldID getAudioStaHandleField(JNIEnv *env, jobject obj) {
    jclass c = env->GetObjectClass(obj);
    // J is the type signature for long:
    return env->GetFieldID(c, "nativeAudioStaHandle", "J");
}

void setAudioStaHandle(JNIEnv *env, jobject obj, void *h) {
    jlong handle = reinterpret_cast<jlong>(h);
    env->SetLongField(obj, getAudioStaHandleField(env, obj), handle);
}

void *getAudioStaHandle(JNIEnv *env, jobject obj) {
    jlong handle = env->GetLongField(obj, getAudioStaHandleField(env, obj));
    return reinterpret_cast<void *>(handle);
}

void audio_sta_func(st_audio_callback_status_t status, st_phoneme_list_t* p_phoneme, void* user_data) {
    int isAttached = false;
    JNIEnv *env = get_env(&isAttached);
    if(!env) {
        return;
    }
    jclass clazz = env->GetObjectClass(mObj);
    jmethodID mid = env->GetMethodID(clazz, "audioStaFunc", "(Z)V");
    if (status == ST_AUDIO_CALLBACK_END) {
        env->CallVoidMethod(mObj, mid, true);
    }
    //LOGE("AudioStaNativeTest audio_sta_func callback");
    env->DeleteLocalRef(clazz);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileAudioStaNative_createInstance(JNIEnv *env, jobject thiz,
                                                                  jstring zip_path, jstring ppg_zip_path, jint language) {
    st_handle_t ha_handle = NULL;
    if (zip_path == NULL) {
        LOGE("model path is null, create handle form null");
        return ST_E_INVALIDARG;
    }
    const char *phonemePathChars = env->GetStringUTFChars(zip_path, 0);
    const char *phonemePathChars2 = env->GetStringUTFChars(ppg_zip_path, 0);
    LOGI("-->> modelpath=%s", phonemePathChars);
    int result = st_mobile_audio_sta_create(&ha_handle, phonemePathChars, phonemePathChars2, (st_language_t) language);
    if (result != 0) {
        LOGE("create handle for phoneme failed");
        env->ReleaseStringUTFChars(zip_path, phonemePathChars);
        return result;
    }
    mObj = env->NewGlobalRef(thiz);
    st_mobile_audio_sta_set_callback(ha_handle, audio_sta_func, NULL);
    setAudioStaHandle(env, thiz, ha_handle);
    env->ReleaseStringUTFChars(zip_path, phonemePathChars);
    return result;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileAudioStaNative_createInstanceFromAssetFile(JNIEnv *env,
                                                                               jobject obj,
                                                                               jstring zip_path,
                                                                               jstring model_zip_path,
                                                                               jint language,
                                                                               jobject assetManager) {
    st_handle_t handle = NULL;
    if(NULL == zip_path){
        LOGE("model_path is null");
        return ST_E_INVALIDARG;
    }

    if(NULL == assetManager){
        LOGE("assetManager is null");
        return ST_E_INVALIDARG;
    }

    const char* model_file_name_str = env->GetStringUTFChars(zip_path, 0);
    const char* model_file_name_str2 = env->GetStringUTFChars(model_zip_path, 0);
    if(NULL == model_file_name_str || NULL == model_file_name_str2){
        LOGE("change model_path to c_str failed");
        return ST_E_INVALIDARG;
    }
    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
    if(NULL == mgr){
        LOGE("native assetManager is null");
        return ST_E_INVALIDARG;
    }

    LOGE("asset %s",model_file_name_str);
    LOGE("model_zip_path %s",model_file_name_str2);
    AAsset* asset = AAssetManager_open(mgr, model_file_name_str, AASSET_MODE_UNKNOWN);
    AAsset* asset2 = AAssetManager_open(mgr, model_file_name_str2, AASSET_MODE_UNKNOWN);
    env->ReleaseStringUTFChars(zip_path, model_file_name_str);
    env->ReleaseStringUTFChars(model_zip_path, model_file_name_str2);
    unsigned char* buffer = NULL;
    unsigned char* buffer2 = NULL;
    int size = 0;
    int size2 = 0;
    if (NULL == asset || NULL == asset2){
        LOGE("open asset file failed");
        return ST_E_FILE_NOT_FOUND;
    }

    size = AAsset_getLength(asset);
    size2 = AAsset_getLength(asset2);
    buffer = new unsigned char[size];
    buffer2 = new unsigned char[size2];
    memset(buffer,'\0',size);
    memset(buffer2,'\0',size2);
    int readSize = AAsset_read(asset,buffer,size);
    int readSize2 = AAsset_read(asset2,buffer2,size2);

    if (readSize != size){
        AAsset_close(asset);
        if(buffer){
            delete[] buffer;
            buffer = NULL;
        }
        return ST_E_INVALID_FILE_FORMAT;
    }

    if (readSize2 != size2){
        AAsset_close(asset2);
        if(buffer2){
            delete[] buffer2;
            buffer2 = NULL;
        }
        return ST_E_INVALID_FILE_FORMAT;
    }

    AAsset_close(asset);
    AAsset_close(asset2);

    if (size < 1000){
        LOGE("Model file is too samll");
        if(buffer){
            delete[] buffer;
            buffer = NULL;
        }
        return ST_E_INVALID_FILE_FORMAT;
    }

    int result = st_mobile_audio_sta_create_from_buffer(&handle, buffer, size, buffer2, size2, (st_language_t) language);
    mObj = env->NewGlobalRef(obj);
    st_mobile_audio_sta_set_callback(handle, audio_sta_func, NULL);
    if(buffer){
        delete[] buffer;
        buffer = NULL;
    }
    if(buffer2){
        delete[] buffer2;
        buffer2 = NULL;
    }

    if(result != 0){
        LOGE("create handle failed, %d",result);
        return result;
    }

    setAudioStaHandle(env, obj, handle);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileAudioStaNative_destroy(JNIEnv *env, jobject thiz) {
    st_handle_t avatarhandle = getAudioStaHandle(env, thiz);
    int result = ST_OK;
    if (avatarhandle != NULL) {
        LOGI(" Avatar handle destory");
        setAudioStaHandle(env, thiz, NULL);
        st_mobile_audio_sta_destroy(avatarhandle);
    }
    return result;
}

bool convert2STAudio(JNIEnv *env, jobject inputObject, st_audio_t *audio_t) {
    jclass clazz = env->FindClass("com/softsugar/stmobile/params/STAudio");

    jfieldID fieldSentenceId = env->GetFieldID(clazz, "sentenceId", "I");
    audio_t->sentence_id = env->GetIntField(inputObject, fieldSentenceId);

    jfieldID fieldPcmData = env->GetFieldID(clazz, "pcmData", "[B");
    jbyteArray dataArray = (jbyteArray) env->GetObjectField(inputObject, fieldPcmData);
    audio_t->pcm_data = convert2JByteArrayToChars2(env, dataArray);

    jfieldID fieldPcmLength = env->GetFieldID(clazz, "pcmLength", "I");
    audio_t->pcm_length = env->GetIntField(inputObject, fieldPcmLength);

    env->DeleteLocalRef(clazz);
    return true;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileAudioStaNative_audioStaInput(JNIEnv *env, jobject thiz,
                                                                 jobject audio) {
    st_handle_t handle = getAudioStaHandle(env, thiz);
    if (handle == NULL) {
        LOGE("handle is null");
        return NULL;
    }
    st_audio_t *st_audio = new st_audio_t();
    convert2STAudio(env, audio, st_audio);

    st_audio->status = ST_AUDIO_ALONE;
    int result = st_mobile_audio_sta_input(handle, st_audio);

    if (st_audio->pcm_data) {
        delete[] st_audio->pcm_data;
        st_audio->pcm_data = NULL;
    }
    if (st_audio) {
        delete st_audio;
        st_audio = NULL;
    }

    return result;
}

jobject convert2BlendShapeFactory2(JNIEnv *env, const st_blend_shape_factor_t *blend_shape) {
    jclass clazz = env->FindClass("com/softsugar/stmobile/params/STBlendShapeFactory");
    jobject blendShapeObject = env->AllocObject(clazz);

    // count
    jfieldID fieldCount = env->GetFieldID(clazz, "count", "I");
    env->SetIntField(blendShapeObject, fieldCount, (int) blend_shape->count);

    // blendShapes
    jfieldID fieldBlendShapes = env->GetFieldID(clazz, "blendShapes", "[F");
    jfloatArray blendShapeArray = env->NewFloatArray(blend_shape->count);
    env->SetFloatArrayRegion(blendShapeArray, 0, blend_shape->count, blend_shape->p_blend_shapes);
    env->SetObjectField(blendShapeObject, fieldBlendShapes, blendShapeArray);
    env->DeleteLocalRef(blendShapeArray);

    // region
    jfieldID fieldRegion = env->GetFieldID(clazz, "region", "I");
    env->SetIntField(blendShapeObject, fieldRegion, (int) blend_shape->region);

    env->DeleteLocalRef(clazz);

    return blendShapeObject;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileAudioStaNative_getCurrentBs(JNIEnv *env, jobject thiz,
                                                                jint sentence_id, jfloat time,
                                                                jobject out_param) {
    st_handle_t handle = getAudioStaHandle(env, thiz);
    if (handle == NULL) {
        LOGE("handle is null");
        return NULL;
    }
    st_blend_shape_factor_t *blend_shape = new st_blend_shape_factor_t();
    memset(blend_shape, 0, sizeof(st_blend_shape_factor_t));
    int result = st_mobile_audio_get_current_bs_data(handle, sentence_id, time, blend_shape);
    convert2BlendShapeFactory2(env, blend_shape, out_param);
    delete blend_shape;
    blend_shape = NULL;
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileAudioStaNative_reset(JNIEnv *env, jobject thiz) {
    st_handle_t handle = getAudioStaHandle(env, thiz);
    if (handle == NULL) {
        LOGE("handle is null");
        return NULL;
    }
    int result = st_mobile_audio_sta_reset(handle);
    return result;
}
