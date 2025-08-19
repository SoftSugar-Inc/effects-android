#include <jni.h>

#include<fcntl.h>
#include <st_mobile_animal.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include "utils.h"
#include "utils_effects.h"

#define  LOG_TAG    "STMobileAnimal"

extern "C" {
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_createInstanceFromAssetFile(JNIEnv * env, jobject obj, jstring model_path, jint config, jobject assetManager);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_createInstance(JNIEnv * env, jobject obj, jstring modelpath, jint config);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_setParam(JNIEnv * env, jobject obj, jint type, jfloat value);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_reset(JNIEnv * env, jobject obj);
    JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_animalDetect(JNIEnv * env, jobject obj, jbyteArray pInputImage, jint imageFormat,
                                                                                             jint rotate, jint detectConfig, jint imageWidth, jint imageHeight);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_destroyInstance(JNIEnv * env, jobject obj);
    JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_animalMirror(JNIEnv * env, jobject obj, jint width, jobject animalObj);
    JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_animalRotate(JNIEnv * env, jobject obj, jint width, jint height, jint orientation, jobject animalObj);
    JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_animalResize(JNIEnv * env, jobject obj, jfloat scale, jobject animalObj);

    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_addSubModel(JNIEnv * env, jobject obj, jstring modelpath);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_addSubModelFromAssetFile(JNIEnv * env, jobject obj, jstring model_path, jobject assetManager);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_addSubModelFromBuffer(JNIEnv * env, jobject obj, jbyteArray buffer, jint len);
};

static inline jfieldID getAnimalHandleField(JNIEnv *env, jobject obj)
{
    jclass c = env->GetObjectClass(obj);
    // J is the type signature for long:
    return env->GetFieldID(c, "nativeAnimalHandle", "J");
}

void setAnimalHandle(JNIEnv *env, jobject obj, void * h)
{
    jlong handle = reinterpret_cast<jlong>(h);
    env->SetLongField(obj, getAnimalHandleField(env, obj), handle);
}

void* getAnimalHandle(JNIEnv *env, jobject obj)
{
    jlong handle = env->GetLongField(obj, getAnimalHandleField(env, obj));
    return reinterpret_cast<void *>(handle);
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_createInstance(JNIEnv * env, jobject obj, jstring modelpath, jint config)
{
    st_handle_t  animal_handle = NULL;
    int result = ST_OK;
    if (modelpath == NULL) {
        result = st_mobile_tracker_animal_face_create(NULL, config, &animal_handle);
    } else {
        const char *modelpathChars = env->GetStringUTFChars(modelpath, 0);
        LOGI("-->> modelpath=%s, config=%d", modelpathChars, config);
        result = st_mobile_tracker_animal_face_create(modelpathChars, config, &animal_handle);
        env->ReleaseStringUTFChars(modelpath, modelpathChars);
    }

    if(result != 0){
        LOGE("create handle for animal failed: %d", result);
        return result;
    }
    setAnimalHandle(env, obj, animal_handle);
    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_createInstanceFromAssetFile(JNIEnv * env, jobject obj, jstring model_path, jint config, jobject assetManager){
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
    unsigned char* buffer = NULL;
    int size = 0;
    if (NULL == asset){
        LOGE("open asset file failed");
        return ST_E_FILE_NOT_FOUND;
    }

    size = AAsset_getLength(asset);
    buffer = new unsigned char[size];
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

    if (size < 100){
        LOGE("Model file is too samll");
        if(buffer){
            delete[] buffer;
            buffer = NULL;
        }
        return ST_E_INVALID_FILE_FORMAT;
    }

    //int result = st_mobile_tracker_animal_face_create_from_buffer(buffer, size, (int)config, &handle);
    int result = st_mobile_tracker_animal_face_create(NULL, config, &handle);
    st_mobile_tracker_animal_face_add_sub_model_from_buffer(handle, buffer, size);
    if(buffer){
        delete[] buffer;
        buffer = NULL;
    }

    setAnimalHandle(env, obj, handle);

    if(result != 0){
        LOGE("create handle failed, %d",result);
        return result;
    }
    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_setParam(JNIEnv * env, jobject obj, jint type, jfloat value)
{
    st_handle_t handle = getAnimalHandle(env, obj);
    if(handle == NULL)
    {
        return JNI_FALSE;
    }
    LOGE("set Param for %d, %f", type, value);
    int result = (int)st_mobile_tracker_animal_face_setparam(handle,(st_animal_face_param_type)type,value);
    return 0;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_reset(JNIEnv * env, jobject obj)
{
    st_handle_t animalhandle = getAnimalHandle(env, obj);
    if(animalhandle != NULL)
    {
        return st_mobile_tracker_animal_face_reset(animalhandle);
    }

    return ST_E_HANDLE;
}


JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_animalDetect(JNIEnv * env, jobject obj, jbyteArray pInputImage, jint imageFormat,
                                                                                                   jint rotate, jint detectConfig, jint imageWidth, jint imageHeight)
{
    LOGE("animalDetect, the width is %d, the height is %d, the rotate is %d",imageWidth, imageHeight, rotate);
    st_handle_t animalhandle = getAnimalHandle(env, obj);
    if(animalhandle == NULL)
    {
        LOGE("handle is null");
        return NULL;
    }

    if (pInputImage == NULL) {
        LOGE("input image is null");
        return NULL;
    }

    jbyte *srcdata = (jbyte*) (env->GetByteArrayElements(pInputImage, 0));
    int image_stride = getImageStride((st_pixel_format)imageFormat, imageWidth);

    st_mobile_animal_result_t stMobileAnimalResult = {0};

    int result = -1;
    long startTime = getCurrentTime();
    if(animalhandle != NULL)
    {
        LOGI("before detect");
        result =  st_mobile_tracker_animal_face_track(animalhandle, (unsigned char *)srcdata,  (st_pixel_format)imageFormat,  imageWidth,
                                                imageHeight, image_stride, (st_rotate_type)rotate, detectConfig, &stMobileAnimalResult);
        LOGI("st_mobile_tracker_animal_face_track --- result is %d", result);
    }

    long afterdetectTime = getCurrentTime();
    LOGI("the animal detected time is %ld", (afterdetectTime - startTime));
    LOGE("the animal face count is %d", stMobileAnimalResult.count);
    env->ReleaseByteArrayElements(pInputImage, srcdata, 0);

    jobject animalObj = convert2AnimalResult(env, &stMobileAnimalResult);
    return animalObj;
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_destroyInstance(JNIEnv * env, jobject obj)
{
    st_handle_t animalhandle = getAnimalHandle(env, obj);
    if(animalhandle != NULL)
    {
        LOGI(" animal handle destory");
        setAnimalHandle(env,obj,NULL);
        st_mobile_tracker_animal_face_destroy(animalhandle);
    }
}

JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_animalMirror(JNIEnv * env, jobject obj, jint width, jobject animalObj)
{
    st_mobile_animal_result_t *animal_result = new st_mobile_animal_result_t();
    convert2AnimalResult(env, animalObj, animal_result);
    st_mobile_animal_face_mirror(width, animal_result);
    jobject animal = convert2AnimalResult(env, animal_result);
    st_mobile_animal_face_delete(animal_result);
    st_mobile_animal_face_delete(animal_result);
    return animal;
}

JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_animalRotate(JNIEnv * env, jobject obj, jint width, jint height, jint orientation, jobject animalObj)
{
    st_mobile_animal_result_t *animal_result = new st_mobile_animal_result_t();
    memset(animal_result, 0, sizeof(st_mobile_animal_result_t));
    convert2AnimalResult(env, animalObj, animal_result);
    st_mobile_animal_face_rotate(width, height, (st_rotate_type)orientation, animal_result);
    jobject animal = convert2AnimalResult(env, animal_result);
    st_mobile_animal_face_delete(animal_result);
    return animal;
}

JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_animalResize(JNIEnv * env, jobject obj, jfloat scale, jobject animalObj)
{
    st_mobile_animal_result_t *animal_result = new st_mobile_animal_result_t();
    convert2AnimalResult(env, animalObj, animal_result);
    st_mobile_animal_face_resize(scale, animal_result);
    jobject animal = convert2AnimalResult(env, animal_result);
    st_mobile_animal_face_delete(animal_result);
    st_mobile_animal_face_delete(animal_result);
    return animal;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_addSubModel(JNIEnv * env, jobject obj, jstring modelpath)
{
    st_handle_t humanActionhandle = getAnimalHandle(env, obj);
    if(humanActionhandle == NULL) {
        LOGE("handle is null");
        return ST_E_INVALIDARG;
    }

    if (modelpath == NULL) {
        LOGE("model path is null");
        return ST_E_INVALIDARG;
    }
    const char *modelpathChars = env->GetStringUTFChars(modelpath, 0);
    int result = st_mobile_tracker_animal_face_add_sub_model(humanActionhandle, modelpathChars);
    LOGE("st_mobile_tracker_animal_face_add_sub_model model_path=%s ret=%d", modelpathChars, result);

    LOGE("add sub model result: %d", result);
    env->ReleaseStringUTFChars(modelpath, modelpathChars);
    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_addSubModelFromAssetFile(JNIEnv * env, jobject obj, jstring model_path, jobject assetManager){

    st_handle_t animalhandle = getAnimalHandle(env, obj);
    if(animalhandle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    if(NULL == model_path){
        LOGE("model_file_name is null, create handle with null model");
        return ST_E_FILE_NOT_FOUND;
    }

    if(NULL == assetManager){
        LOGE("assetManager is null");
        return ST_E_INVALIDARG;
    }

    const char* model_file_name_str = env->GetStringUTFChars(model_path, 0);
    if(NULL == model_file_name_str) {
        LOGE("change model_file_name to c_str failed");
        return ST_E_INVALIDARG;
    }

    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
    if(NULL == mgr) {
        LOGE("native assetManager is null");
        return ST_E_INVALIDARG;
    }

    AAsset* asset = AAssetManager_open(mgr, model_file_name_str, AASSET_MODE_UNKNOWN);
    env->ReleaseStringUTFChars(model_path, model_file_name_str);
    if (NULL == asset) {
        LOGE("open asset file failed");
        return ST_E_FILE_NOT_FOUND;
    }

    unsigned char* buffer = NULL;
    long size = 0;
    size = AAsset_getLength(asset);
    buffer = new unsigned char[size];
    memset(buffer, '\0', size);

    long readSize = AAsset_read(asset, buffer, size);
    if (readSize != size) {
        AAsset_close(asset);
        if(buffer){
            delete[] buffer;
            buffer = NULL;
        }
        return ST_E_FILE_NOT_FOUND;
    }

    AAsset_close(asset);

    if (size < 1000) {
        LOGE("Model file is too short");
        if (buffer) {
            delete[] buffer;
            buffer = NULL;
        }
        return ST_E_INVALIDARG;
    }

    int result = st_mobile_tracker_animal_face_add_sub_model_from_buffer(animalhandle, buffer, size);
    if(buffer){
        delete[] buffer;
        buffer = NULL;
    }

    if(result != 0){
        LOGE("add sub model failed, %d",result);
        return result;
    }

    return result;
}


JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileAnimalNative_addSubModelFromBuffer(JNIEnv * env, jobject obj, jbyteArray buffer, jint len)
{
    int result = ST_OK;

    if (buffer == NULL) {
        LOGE("buffer is null");
        return ST_E_INVALIDARG;
    }

    st_handle_t handle = getAnimalHandle(env, obj);
    if(handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    jbyte *srcdata = (jbyte*) (env->GetByteArrayElements(buffer, 0));

    result = st_mobile_tracker_animal_face_add_sub_model_from_buffer(handle, (const unsigned char *)srcdata, len);

    env->ReleaseByteArrayElements(buffer, srcdata, 0);
    if(result != 0){
        LOGE("add_sub_model_from_buffer failed, %d",result);
    }

    return result;
}


extern "C"
JNIEXPORT jobject JNICALL
Java_com_softsugar_stmobile_STMobileAnimalNative_animalFaceCopy(JNIEnv *env, jobject thiz, jobject animalObj) {
    int result = ST_OK;
    st_mobile_animal_result_t *animal_result_src = new st_mobile_animal_result_t();
    st_mobile_animal_result_t *animal_result_dst = new st_mobile_animal_result_t();
    memset(animal_result_src, 0, sizeof(st_mobile_animal_result_t));
    memset(animal_result_dst, 0, sizeof(st_mobile_animal_result_t));
    convert2AnimalResult(env, animalObj, animal_result_src);
    st_mobile_animal_face_copy(animal_result_src, animal_result_dst);
    jobject animal = convert2AnimalResult(env, animal_result_dst);

    st_mobile_animal_face_delete(animal_result_src);
    st_mobile_animal_face_delete(animal_result_dst);
    return animal;
}