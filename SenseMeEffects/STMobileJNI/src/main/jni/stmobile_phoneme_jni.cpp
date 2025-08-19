#include <jni.h>
#include <android/log.h>
#include "utils.h"
#include <st_mobile_phoneme.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include "prebuilt/include/st_mobile_common.h"

#define  LOG_TAG    "STMobilePhonemeNative"

static inline jfieldID getPhonemeHandleField(JNIEnv *env, jobject obj) {
    jclass c = env->GetObjectClass(obj);
    // J is the type signature for long:
    return env->GetFieldID(c, "nativePhonemeHandle", "J");
}

void setPhonemeHandle(JNIEnv *env, jobject obj, void *h) {
    jlong handle = reinterpret_cast<jlong>(h);
    env->SetLongField(obj, getPhonemeHandleField(env, obj), handle);
}

void *getPhonemeHandle(JNIEnv *env, jobject obj) {
    jlong handle = env->GetLongField(obj, getPhonemeHandleField(env, obj));
    return reinterpret_cast<void *>(handle);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobilePhonemeNative_createInstance(JNIEnv *env, jobject thiz,
                                                                 jstring phoneme_file_path, jint type) {
    st_handle_t ha_handle = NULL;
    if (phoneme_file_path == NULL) {
        LOGE("model path is null, create handle form null");
        return ST_E_INVALIDARG;
    }
    const char *phonemePathChars = env->GetStringUTFChars(phoneme_file_path, 0);
    LOGI("-->> modelpath=%s", phonemePathChars);
    int result = st_mobile_phoneme_create(phonemePathChars, static_cast<st_phoneme_type_t>(type), &ha_handle);
    if (result != 0) {
        LOGE("create handle for phoneme failed");
        env->ReleaseStringUTFChars(phoneme_file_path, phonemePathChars);
        return result;
    }
    setPhonemeHandle(env, thiz, ha_handle);
    env->ReleaseStringUTFChars(phoneme_file_path, phonemePathChars);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobilePhonemeNative_createInstanceFromAssetFile(JNIEnv *env,
                                                                              jobject obj,
                                                                              jstring model_path, jint type,
                                                                              jobject assetManager) {
    st_handle_t handle = NULL;
    if(NULL == model_path){
        LOGE("model_path is null");
        return ST_E_INVALIDARG;
    }

    if(NULL == assetManager){
        LOGE("assetManager is null");
        return ST_E_INVALIDARG;
    }

    const char* model_file_name_str = env->GetStringUTFChars(model_path, 0);
    if(NULL == model_file_name_str){
        LOGE("change model_path to c_str failed");
        return ST_E_INVALIDARG;
    }
    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
    if(NULL == mgr){
        LOGE("native assetManager is null");
        return ST_E_INVALIDARG;
    }

    LOGE("asset %s",model_file_name_str);
    AAsset* asset = AAssetManager_open(mgr, model_file_name_str, AASSET_MODE_UNKNOWN);
    env->ReleaseStringUTFChars(model_path, model_file_name_str);
    char* buffer = NULL;
    int size = 0;
    if (NULL == asset){
        LOGE("open asset file failed");
        return ST_E_FILE_NOT_FOUND;
    }

    size = AAsset_getLength(asset);
    buffer = new char[size];
    memset(buffer,'\0',size);
    int readSize = AAsset_read(asset,buffer,size);

    if (readSize != size){
        AAsset_close(asset);
        if(buffer){
            delete[] buffer;
            buffer = NULL;
        }
        return ST_E_INVALID_FILE_FORMAT;
    }

    AAsset_close(asset);

    if (size < 1000){
        LOGE("Model file is too samll");
        if(buffer){
            delete[] buffer;
            buffer = NULL;
        }
        return ST_E_INVALID_FILE_FORMAT;
    }

    int result = st_mobile_phoneme_create_from_buffer(buffer, size, static_cast<st_phoneme_type_t>(type), &handle);
    if(buffer){
        delete[] buffer;
        buffer = NULL;
    }

    if(result != 0){
        LOGE("create handle failed, %d",result);
        return result;
    }

    setPhonemeHandle(env, obj, handle);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobilePhonemeNative_destroy(JNIEnv *env, jobject thiz) {
    st_handle_t avatarhandle = getPhonemeHandle(env, thiz);
    if (avatarhandle != NULL) {
        LOGI(" Avatar handle destory");
        setPhonemeHandle(env, thiz, NULL);
        st_mobile_phoneme_destroy(avatarhandle);
    }
    return ST_OK;
}

jobject convert2BlendShapeFactory(JNIEnv *env, const st_blend_shape_factor_t *blend_shape) {
    jclass clazz = env->FindClass("com/softsugar/stmobile/params/STBlendShapeFactory");
    jobject blendShapeObject = env->AllocObject(clazz);

    // count
    jfieldID fieldCount = env->GetFieldID(clazz, "count", "I");
    env->SetIntField(blendShapeObject, fieldCount, blend_shape->count);

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

char *convert2JByteArrayToCharsNew(JNIEnv *env, jbyteArray byteArray) {
    if (byteArray == NULL)
        return NULL;
    char *chars = NULL;
    jbyte *bytes;
    jboolean isCopy = 0;
    bytes = env->GetByteArrayElements(byteArray, &isCopy);
    int chars_len = env->GetArrayLength(byteArray);
    chars = new char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;
    env->ReleaseByteArrayElements(byteArray, bytes, 0);
    return chars;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_softsugar_stmobile_STMobilePhonemeNative_getBlendShapeFactorByPhoneme(JNIEnv *env,
                                                                               jobject thiz,
                                                                               jstring phoneme) {
    st_handle_t handle = getPhonemeHandle(env, thiz);
    if (handle == NULL) {
        LOGE("handle is null");
        return NULL;
    }
    const char *p_phoneme = env->GetStringUTFChars(phoneme, 0);
    st_blend_shape_factor_t *blend_shape = new st_blend_shape_factor_t;
    memset(blend_shape, 0, sizeof(st_blend_shape_factor_t));
    int result = st_mobile_get_blend_shape_factor_by_phoneme(handle, p_phoneme, blend_shape);
    if (result == 0) {
        return convert2BlendShapeFactory(env, blend_shape);
    }
    return NULL;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobilePhonemeNative_phonemeInput(JNIEnv *env, jobject thiz,
                                                               jbyteArray p_phoneme_buffer,
                                                               jint length) {
    st_handle_t handle = getPhonemeHandle(env, thiz);
    if (handle == NULL) {
        LOGE("handle is null");
        return NULL;
    }
    char *p_phoneme = convert2JByteArrayToCharsNew(env, p_phoneme_buffer);
    return st_mobile_phoneme_input(handle, p_phoneme, length);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobilePhonemeNative_getCurrentBs(JNIEnv *env, jobject thiz, jfloat time,
                                                               jobject out_param) {
    st_handle_t handle = getPhonemeHandle(env, thiz);
    if (handle == NULL) {
        LOGE("handle is null");
        return NULL;
    }
    st_blend_shape_factor_t *blend_shape = new st_blend_shape_factor_t();
    memset(blend_shape, 0, sizeof(st_blend_shape_factor_t));
    int result = st_mobile_get_current_bs(handle, time, blend_shape);
    convert2BlendShapeFactory2(env, blend_shape, out_param);
    delete blend_shape;
    blend_shape = NULL;
    return result;
}

extern "C"
JNIEXPORT int JNICALL
Java_com_softsugar_stmobile_STMobilePhonemeNative_reset(JNIEnv *env, jobject thiz) {

    st_handle_t handle = getPhonemeHandle(env, thiz);
    if (handle == NULL) {
        LOGE("handle is null");
        return NULL;
    }
    int result = st_mobile_phoneme_reset(handle);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobilePhonemeNative_setRegion(JNIEnv *env, jobject thiz, jint region) {
    st_handle_t handle = getPhonemeHandle(env, thiz);
    if (handle == NULL) {
        LOGE("handle is null");
        return NULL;
    }
    int result = st_mobile_phoneme_set_region(handle, region);
    return result;
}