#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "st_mobile_effect.h"
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include "stmobile_sound_play_jni.h"
#include "utils.h"
#include "jvmutil.h"
#include "utils_effects.h"
#include<fcntl.h>
#include <sstream>

#define  LOG_TAG    "STMobileEffectNative"

extern "C" {
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_createInstanceNative(JNIEnv * env, jobject obj, jint config, jstring path);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_destroyInstanceNative(JNIEnv * env, jobject obj);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setParam(JNIEnv * env, jobject obj, jint param, jfloat value);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeautyParam(JNIEnv * env, jobject obj, jint param, jfloat value);
    JNIEXPORT jfloat JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getParam(JNIEnv * env, jobject obj, jint param);
    JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getTryOnParam(JNIEnv *env, jobject thiz, jint param);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setTryOnParam(JNIEnv *env, jobject obj, jobject info, jint type);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_addPackage(JNIEnv * env, jobject obj, jstring path);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_addPackageFromAssetsFile(JNIEnv * env, jobject obj, jstring file_path, jobject assetManager);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_addPackageFromBuffer(JNIEnv * env, jobject obj, jbyteArray buffer, jint len);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_changePackage(JNIEnv * env, jobject obj, jstring path);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_changePackageFromAssetsFile(JNIEnv * env, jobject obj, jstring file_path, jobject assetManager);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_changePackageFromBuffer(JNIEnv * env, jobject obj, jbyteArray buffer, jint len);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_removeEffect(JNIEnv * env, jobject obj, jint id);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_clear(JNIEnv * env, jobject obj);
    JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getPackageInfo(JNIEnv * env, jobject obj, jint packageId);
    JNIEXPORT jobjectArray JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getModulesInPackage(JNIEnv * env, jobject obj, jint packageId, jint moduleCount);
    JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getModuleInfo(JNIEnv * env, jobject obj, jint moduleId);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getOverlappedBeautyCount(JNIEnv *env, jobject obj);
    JNIEXPORT jobjectArray JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getOverlappedBeauty(JNIEnv * env, jobject obj, jint beautyCount);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeautyStrength(JNIEnv * env, jobject obj, jint type, jfloat value);
    JNIEXPORT jfloat JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getBeautyStrength(JNIEnv *env, jobject obj, jint type);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeauty(JNIEnv * env, jobject obj, jint param, jstring path);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeautyFromBuffer(JNIEnv * env, jobject obj, jint param, jbyteArray buffer, jint len);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeautyMode(JNIEnv *env, jobject obj,jint param, jint value);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getBeautyMode(JNIEnv *env, jobject obj,jint param);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeautyFromAssetsFile(JNIEnv * env, jobject obj, jint param, jstring file_path, jobject assetManager);
    JNIEXPORT jlong JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getHumanActionDetectConfig(JNIEnv *env, jobject obj);
    JNIEXPORT jlong JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getHumanTriggerActions(JNIEnv *env, jobject obj, jlong config);
    JNIEXPORT jlong JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getAnimalDetectConfig(JNIEnv *env, jobject obj);
    JNIEXPORT jlong JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getCustomParamConfig(JNIEnv *env, jobject obj);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_render(JNIEnv * env, jobject obj, jobject inputParams, jobject outputParams, jboolean needOutputHumanAction);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_replayPackage(JNIEnv * env, jobject obj, jint packageId);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setPackageBeautyGroupStrength(JNIEnv * env, jobject obj, jint pag_id, jint group, jfloat strength);
    JNIEXPORT jobjectArray JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_get3DBeautyParts(JNIEnv *env, jobject thiz);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setGreenMaskSafeRegions(JNIEnv *env, jobject thiz, jint num, jobjectArray safeRegions);
};

static inline jfieldID getEffectHandleField(JNIEnv *env, jobject obj)
{
    jclass c = env->GetObjectClass(obj);
    // J is the type signature for long:
    return env->GetFieldID(c, "nativeEffectHandle", "J");
}

void setEffectHandle(JNIEnv *env, jobject obj, void *h)
{
    jlong handle = reinterpret_cast<jlong>(h);
    env->SetLongField(obj, getEffectHandleField(env, obj), handle);
}

void *getEffectHandle(JNIEnv *env, jobject obj)
{
    jlong handle = env->GetLongField(obj, getEffectHandleField(env, obj));
    return reinterpret_cast<void *>(handle);
}

int package_state_change(void* handle, const st_effect_package_info_t* p_package_info) {
    JNIEnv *env;
    bool isAttached = false;
    getEnv(&env,&isAttached);
    if(!env) {
        return -1;
    }
    if (p_package_info == NULL) {
        return -1;
    }
    const char *kSTMobileEffectNativePath = "com/softsugar/stmobile/STMobileEffectNative";
    jclass clazz = env->FindClass(kSTMobileEffectNativePath);
    jmethodID packageStateChangeCalledByJniId = env->GetMethodID(clazz, "packageStateChangeCalledByJni", "(II)V");
    env->CallVoidMethod(gStickerObject, packageStateChangeCalledByJniId, (int)p_package_info->state, (int)p_package_info->package_id);
    env->DeleteLocalRef(clazz);
    return 0;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_createInstanceNative(JNIEnv * env, jobject obj, jint config, jstring path)
{
//    const char *pathChars = env->GetStringUTFChars(path, 0);
//    LOGE("csw jni so path: %s", pathChars);
//    setenv("ADSP_LIBRARY_PATH", pathChars, 1 /*override*/) == 0;
//
//    std::stringstream path1;
//    path1 << "/vendor/lib64/";
//    setenv("LD_LIBRARY_PATH", path1.str().c_str(), 1 /*override*/) == 0;

    gStickerObject = env->NewGlobalRef(obj);

    st_handle_t handle;
    int result = st_mobile_effect_create_handle(config, &handle);
    LOGE("st_mobile_effect_create_handle called.");
    if(result != 0){
        if(gStickerObject != NULL) {
            env->DeleteGlobalRef(gStickerObject);
            gStickerObject = NULL;
        }

        LOGE("create handle failed");
        return result;
    }

    int ret = st_mobile_effect_set_module_state_change_callback(handle, sound_state_changed);
    st_mobile_effect_set_packaged_state_change_callback(handle, package_state_change);

    setEffectHandle(env, obj, handle);
    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_destroyInstanceNative(JNIEnv * env, jobject obj)
{
    st_handle_t handle = getEffectHandle(env, obj);
    if(handle == NULL){
        return ST_E_HANDLE;
    }
    setEffectHandle(env, obj, NULL);
    int ret = st_mobile_effect_destroy_handle(handle);
    //LOGE("st_mobile_effect_destroy_handle called:%d", ret);
    return ST_OK;
}

JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getTryOnParam(JNIEnv *env, jobject thiz, jint param) {
    st_handle_t handle = getEffectHandle(env, thiz);
    if(handle == NULL){
        return NULL;
    }
    auto *try_on_info = new st_effect_tryon_info_t;
    memset(try_on_info, 0, sizeof(st_effect_tryon_info_t));
    int result = ST_OK;
    result = st_mobile_effect_get_tryon_param(handle,static_cast<st_effect_beauty_type_t>(param), try_on_info);
    LOGE("try_on ret %d", result);
    if (result == ST_OK) {
        jobject tryOnInfoObj = convert2TryOn(env, try_on_info);
        delete try_on_info;
        return tryOnInfoObj;
    }
    return NULL;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setTryOnParam(JNIEnv *env, jobject obj, jobject info, jint type) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    st_effect_tryon_info_t *input_param = new st_effect_tryon_info_t;
    convert2TryOn(env, info, input_param);

    if (handle != NULL) {
        result = st_mobile_effect_set_tryon_param(handle, static_cast<st_effect_beauty_type_t>(type), input_param);
        LOGE("tryon result: %d", result);
    }
    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setParam(JNIEnv * env, jobject obj, jint param, jfloat value)
{
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);
    if(handle != NULL){
        result = st_mobile_effect_set_param(handle, (st_effect_param_t)param, value);
        LOGE("st_mobile_effect_set_param param=%d value=%f ret=%d", param, value, result);
    }
    return result;
}

JNIEXPORT jfloat JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getParam(JNIEnv * env, jobject obj, jint param)
{
    float value = 0.0f;
    st_handle_t handle = getEffectHandle(env, obj);
    if(handle != NULL){
        st_mobile_effect_get_param(handle, (st_effect_param_t)param, &value);
    }

    return value;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeautyParam(JNIEnv * env, jobject obj, jint param, jfloat value)
{
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);
    if(handle != NULL){
        result = st_mobile_effect_set_beauty_param(handle, (st_effect_beauty_param_t)param, value);
        LOGE("st_mobile_effect_set_beauty_param param=%d value:%f", param, value);
    }
    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeautyMode(JNIEnv *env, jobject obj,
                                                               jint param, jint value) {
    //LOGE("setBeautyMode: param:%d value:%d ", param, value);
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);
    if(handle != NULL){
        result = st_mobile_effect_set_beauty_mode(handle, (st_effect_beauty_type_t)param, value);
        LOGE("st_mobile_effect_set_beauty_mode param=%d value=%d ret=%d", param, value, result);
    }
    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getBeautyMode(JNIEnv *env, jobject obj,
                                                               jint param) {
    //int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);
    jint mode = -1;
    if(handle != NULL){
        st_mobile_effect_get_beauty_mode(handle, (st_effect_beauty_type_t)param, &mode);
    }
    return mode;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_addPackage(JNIEnv * env, jobject obj, jstring path)
{
//    LOGE("Java_com_softsugar_stmobile_STMobileEffectNative_addPackage");
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    const char *pathChars = NULL;
    if (path != NULL) {
        pathChars = env->GetStringUTFChars(path, 0);
    }

    int packageId = 0;
    if(handle != NULL) {
        result = st_mobile_effect_add_package(handle,pathChars, &packageId);
        LOGE("st_mobile_effect_add_package p_package_path=%s ret=%d", pathChars, result);
    }
    if (pathChars != NULL) {
        env->ReleaseStringUTFChars(path, pathChars);
    }

    if(result == ST_OK){
        return packageId;
    } else{
        return result;
    }
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_addPackageFromAssetsFile(JNIEnv * env, jobject obj, jstring file_path, jobject assetManager)
{
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    int packageId = 0;

    if(NULL == assetManager){
        LOGE("assetManager is null");
        return ST_E_INVALIDARG;
    }

    if(file_path == NULL){
        LOGE("add package null");
        return ST_E_INVALIDARG;
    }

    const char* file_name_str = env->GetStringUTFChars(file_path, 0);
    if(NULL == file_name_str) {
        LOGE("file_name to c_str failed, add effect to null");
        return ST_E_INVALIDARG;
    }

    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
    if(NULL == mgr) {
        LOGE("native assetManager is null");
        return ST_E_INVALIDARG;
    }

    AAsset* asset = AAssetManager_open(mgr, file_name_str, AASSET_MODE_UNKNOWN);
    env->ReleaseStringUTFChars(file_path, file_name_str);
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

        return ST_E_INVALID_FILE_FORMAT;
    }

    AAsset_close(asset);

    if (size < 100) {
        LOGE("file is too short");
        if (buffer) {
            delete[] buffer;
            buffer = NULL;
        }
        return ST_E_INVALID_FILE_FORMAT;
    }

    st_effect_buffer_t *effect_buffer = new st_effect_buffer_t;
    memset(effect_buffer, 0, sizeof(st_effect_buffer_t));
    effect_buffer->data_ptr = (char*)buffer;
    effect_buffer->data_len = size;

    if(handle != NULL) {
        result = st_mobile_effect_add_package_from_buffer(handle, effect_buffer, &packageId);
        LOGE("st_mobile_effect_add_package_from_buffer ret=%d", result);
    }

    delete(effect_buffer);

    if(buffer){
        delete[] buffer;
        buffer = NULL;
    }

    if(result == ST_OK){
        return packageId;
    } else{
        LOGE("add_package_from_buffer failed, %d",result);
        return result;
    }
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_addPackageFromBuffer(JNIEnv * env, jobject obj, jbyteArray buffer, jint len)
{
    int result = ST_OK;

    if (buffer == NULL) {
        LOGE("buffer is null");
        return ST_E_INVALIDARG;
    }

    st_handle_t handle = getEffectHandle(env, obj);
    if(handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    int packageId = 0;
    jbyte *srcdata = (jbyte*) (env->GetByteArrayElements(buffer, 0));

    st_effect_buffer_t *effect_buffer = new st_effect_buffer_t;
    memset(effect_buffer, 0, sizeof(st_effect_buffer_t));
    effect_buffer->data_ptr = (char*)srcdata;
    effect_buffer->data_len = len;

    result = st_mobile_effect_add_package_from_buffer(handle, effect_buffer, &packageId);

    delete(effect_buffer);
    env->ReleaseByteArrayElements(buffer, srcdata, 0);

    if(result == ST_OK){
        return packageId;
    } else{
        LOGE("add_package_from_buffer failed, %d",result);
        return result;
    }
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_changePackage(JNIEnv * env, jobject obj, jstring path)
{
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    const char *pathChars = NULL;
    if (path != NULL) {
        pathChars = env->GetStringUTFChars(path, 0);
    }

    int packageId = 0;
    result = st_mobile_effect_change_package(handle,pathChars, &packageId);

    if (pathChars != NULL) {
        env->ReleaseStringUTFChars(path, pathChars);
    }

    if(result == ST_OK){
        return packageId;
    } else{
        return result;
    }
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_changePackageFromAssetsFile(JNIEnv * env, jobject obj, jstring file_path, jobject assetManager)
{
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    int packageId = 0;

    if(NULL == assetManager){
        LOGE("assetManager is null");
        return ST_E_INVALIDARG;
    }

    if(file_path == NULL){
        LOGE("change package null");
        return ST_E_INVALIDARG;
    }

    const char* file_name_str = env->GetStringUTFChars(file_path, 0);
    if(NULL == file_name_str) {
        LOGE("file_name to c_str failed, add effect to null");
        return ST_E_INVALIDARG;
    }

    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
    if(NULL == mgr) {
        LOGE("native assetManager is null");
        return ST_E_INVALIDARG;
    }

    AAsset* asset = AAssetManager_open(mgr, file_name_str, AASSET_MODE_UNKNOWN);
    env->ReleaseStringUTFChars(file_path, file_name_str);
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

        return ST_E_INVALID_FILE_FORMAT;
    }

    AAsset_close(asset);

    if (size < 100) {
        LOGE("file is too short");
        if (buffer) {
            delete[] buffer;
            buffer = NULL;
        }
        return ST_E_INVALID_FILE_FORMAT;
    }

    st_effect_buffer_t *effect_buffer = new st_effect_buffer_t;
    memset(effect_buffer, 0, sizeof(st_effect_buffer_t));
    effect_buffer->data_ptr = (char*)buffer;
    effect_buffer->data_len = size;

    if(handle != NULL) {
        result = st_mobile_effect_change_package_from_buffer(handle, effect_buffer, &packageId);
    }

    delete(effect_buffer);

    if(buffer){
        delete[] buffer;
        buffer = NULL;
    }

    if(result == ST_OK){
        return packageId;
    } else{
        LOGE("add_package_from_buffer failed, %d",result);
        return result;
    }
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_changePackageFromBuffer(JNIEnv * env, jobject obj, jbyteArray buffer, jint len)
{
    int result = ST_OK;

    if (buffer == NULL) {
        LOGE("buffer is null");
        return ST_E_INVALIDARG;
    }

    st_handle_t handle = getEffectHandle(env, obj);
    if(handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    int packageId = 0;
    jbyte *srcdata = (jbyte*) (env->GetByteArrayElements(buffer, 0));

    st_effect_buffer_t *effect_buffer = new st_effect_buffer_t;
    memset(effect_buffer, 0, sizeof(st_effect_buffer_t));
    effect_buffer->data_ptr = (char*)srcdata;
    effect_buffer->data_len = len;

    result = st_mobile_effect_change_package_from_buffer(handle, effect_buffer, &packageId);

    delete(effect_buffer);
    env->ReleaseByteArrayElements(buffer, srcdata, 0);

    if(result == ST_OK){
        return packageId;
    } else{
        LOGE("add_package_from_buffer failed, %d",result);
        return result;
    }
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_removeEffect(JNIEnv * env, jobject obj, jint id){
    LOGI("enter st_mobile_effect_remove_package");
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    if(handle != NULL) {
        result = st_mobile_effect_remove_package(handle, id);
        LOGE("st_mobile_effect_remove_package id=%d", id);
    }

    return result;
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_clear(JNIEnv * env, jobject obj){
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
    }

    if(handle != NULL) {
        int ret = st_mobile_effect_clear_packages(handle);
    }
}

JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getPackageInfo(JNIEnv * env, jobject obj, jint packageId)
{
    st_handle_t handle = getEffectHandle(env, obj);

    if(handle == NULL){
        return NULL;
    }

    st_effect_package_info_t *package_info = new st_effect_package_info_t;
    memset(package_info, 0, sizeof(st_effect_package_info_t));

    int result = ST_OK;
    result = st_mobile_effect_get_package_info(handle, packageId, package_info);

    if(result == ST_OK){
        jobject packageInfoObj = convert2EffectPackageInfo(env, package_info);
        return packageInfoObj;
    }

    return NULL;
}

JNIEXPORT jobjectArray JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getModulesInPackage(JNIEnv * env, jobject obj, jint packageId, jint moduleCount)
{
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
        return NULL;
    }

    st_effect_module_info_t* pModuleInfos = NULL;
    if (moduleCount > 0) {
        pModuleInfos = (st_effect_module_info_t *)malloc(moduleCount * sizeof(st_effect_module_info_t));
    }
    result = st_mobile_effect_get_modules_in_package(handle, packageId, pModuleInfos, moduleCount);
    jclass module_info_cls = env->FindClass("com/softsugar/stmobile/model/STEffectModuleInfo");
    jobjectArray moduleInfos = (env)->NewObjectArray(moduleCount, module_info_cls, 0);

    if (result == ST_OK) {
        for (int i = 0; i < moduleCount; ++i) {
            jobject moduleInfoObj = convert2EffectModuleInfo(env, &pModuleInfos[i]);
            if (moduleInfoObj != NULL) {
                env->SetObjectArrayElement(moduleInfos, i, moduleInfoObj);
            }
            env->DeleteLocalRef(moduleInfoObj);
        }
    }

    env->DeleteLocalRef(module_info_cls);
    safe_delete_array(pModuleInfos);

    return moduleInfos;
}

JNIEXPORT jobject JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getModuleInfo(JNIEnv * env, jobject obj, jint moduleId)
{
    st_handle_t handle = getEffectHandle(env, obj);

    if(handle == NULL){
        return NULL;
    }

    st_effect_module_info_t *module_info = new st_effect_module_info_t;
    memset(module_info, 0, sizeof(st_effect_module_info_t));

    int result = ST_OK;
    result = st_mobile_effect_get_module_info(handle, moduleId, module_info);

    if(result == ST_OK){
        jobject moduleInfoObj = convert2EffectModuleInfo(env, module_info);
        return moduleInfoObj;
    }

    return NULL;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getOverlappedBeautyCount(JNIEnv *env, jobject obj) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if(handle == NULL){
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    int count = 0;
    result =  st_mobile_effect_get_overlapped_beauty_count(handle, &count);

    if(result == ST_OK){
        return count;
    }

    return result;
}

JNIEXPORT jobjectArray JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getOverlappedBeauty(JNIEnv * env, jobject obj, jint beautyCount)
{
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
        return NULL;
    }

    st_effect_beauty_info_t* pBeautyInfos = NULL;
    if (beautyCount > 0) {
        pBeautyInfos = (st_effect_beauty_info_t *)malloc(beautyCount * sizeof(st_effect_beauty_info_t));
    }
    result = st_mobile_effect_get_overlapped_beauty(handle, pBeautyInfos, beautyCount);
    jclass beauty_info_cls = env->FindClass("com/softsugar/stmobile/model/STEffectBeautyInfo");
    jobjectArray beautyInfos = (env)->NewObjectArray(beautyCount, beauty_info_cls, 0);

    if (result == ST_OK) {
        for (int i = 0; i < beautyCount; ++i) {
            jobject beautyInfoObj = convert2EffectBeautyInfo(env, &pBeautyInfos[i]);
            if (beautyInfoObj != NULL) {
                env->SetObjectArrayElement(beautyInfos, i, beautyInfoObj);
            }
            env->DeleteLocalRef(beautyInfoObj);
        }
    }

    env->DeleteLocalRef(beauty_info_cls);
    safe_delete_array(pBeautyInfos);

    return beautyInfos;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeautyStrength(JNIEnv * env, jobject obj, jint type, jfloat value)
{
    st_handle_t handle = getEffectHandle(env, obj);
    if(handle == NULL){
        return ST_E_HANDLE;
    }
    int result = st_mobile_effect_set_beauty_strength(handle,(st_effect_beauty_type_t)type, value);
    LOGE("st_mobile_effect_set_beauty_strength type=%d value:%f", type, value);
    return result;
}

JNIEXPORT jfloat JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getBeautyStrength(JNIEnv *env, jobject obj, jint type) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if(handle == NULL){
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    float value = 0.0f;
    result =  st_mobile_effect_get_beauty_strength(handle, (st_effect_beauty_type_t)type, &value);

    if(result == ST_OK){
        return value;
    }

    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeauty(JNIEnv * env, jobject obj, jint param, jstring path)
{
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    const char *pathChars = NULL;
    if (path != NULL) {
        pathChars = env->GetStringUTFChars(path, 0);
    }
    int packageId = 0;
    result = st_mobile_effect_set_beauty(handle, (st_effect_beauty_type_t)param, pathChars);
    LOGE("st_mobile_effect_set_beauty param=%d path=%s ret=%d", param, pathChars, result);
    if (pathChars != NULL) {
        env->ReleaseStringUTFChars(path, pathChars);
    }

    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeautyFromBuffer(JNIEnv * env, jobject obj, jint param, jbyteArray buffer, jint len)
{
    int result = ST_OK;

    if (buffer == NULL) {
        LOGE("buffer is null");
        return ST_E_INVALIDARG;
    }

    st_handle_t handle = getEffectHandle(env, obj);
    if(handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    int packageId = 0;
    jbyte *srcdata = (jbyte*) (env->GetByteArrayElements(buffer, 0));

    st_effect_buffer_t *effect_buffer = new st_effect_buffer_t;
    memset(effect_buffer, 0, sizeof(st_effect_buffer_t));
    effect_buffer->data_ptr = (char*)srcdata;
    effect_buffer->data_len = len;

    result = st_mobile_effect_set_beauty_from_buffer(handle, (st_effect_beauty_type_t)param, effect_buffer);
    LOGE("st_mobile_effect_set_beauty_from_buffer called. ret=%d", result);
    delete(effect_buffer);
    env->ReleaseByteArrayElements(buffer, srcdata, 0);

    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setBeautyFromAssetsFile(JNIEnv * env, jobject obj, jint param, jstring file_path, jobject assetManager)
{
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    int packageId = 0;

    if(NULL == assetManager){
        LOGE("assetManager is null");
        return ST_E_INVALIDARG;
    }

    if(file_path == NULL){
        LOGE("set beauty null");
        const char *pathChars = NULL;
        int ret = st_mobile_effect_set_beauty(handle, (st_effect_beauty_type_t)param,
                                                          pathChars);
        LOGE("set beauty null %d", ret);
        return ST_E_INVALIDARG;
    }

    const char* file_name_str = env->GetStringUTFChars(file_path, 0);
    if(NULL == file_name_str) {
        LOGE("file_name to c_str failed, add effect to null");
        return ST_E_INVALIDARG;
    }

    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
    if(NULL == mgr) {
        LOGE("native assetManager is null");
        return ST_E_INVALIDARG;
    }

    AAsset* asset = AAssetManager_open(mgr, file_name_str, AASSET_MODE_UNKNOWN);
    env->ReleaseStringUTFChars(file_path, file_name_str);
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

        return ST_E_INVALID_FILE_FORMAT;
    }

    AAsset_close(asset);

    if (size < 100) {
        LOGE("file is too short");
        if (buffer) {
            delete[] buffer;
            buffer = NULL;
        }
        return ST_E_INVALID_FILE_FORMAT;
    }

    st_effect_buffer_t *effect_buffer = new st_effect_buffer_t;
    memset(effect_buffer, 0, sizeof(st_effect_buffer_t));
    effect_buffer->data_ptr = (char*)buffer;
    effect_buffer->data_len = size;

    if(handle != NULL) {
        result = st_mobile_effect_set_beauty_from_buffer(handle, (st_effect_beauty_type_t)param, effect_buffer);
        LOGE("st_mobile_effect_set_beauty_from_buffer ret=%d", result);
    }

    delete(effect_buffer);

    if(buffer){
        delete[] buffer;
        buffer = NULL;
    }

    return result;
}

JNIEXPORT jlong JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getHumanActionDetectConfig(JNIEnv *env, jobject obj) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if(handle == NULL){
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    uint64_t value = 0;
    result =  st_mobile_effect_get_detect_config(handle, (uint64_t*)&value);

    if(result == ST_OK){
        return value;
    }

    return result;
}

JNIEXPORT jlong JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getHumanTriggerActions(JNIEnv *env, jobject obj, jlong config) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if(handle == NULL){
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    uint64_t trigger_actions = 0;
    uint64_t config64 = static_cast<uint64_t>(config);
    if (config & ST_MOBILE_DETECT_HAND_GESTURE) {
        result =  st_mobile_effect_get_human_trigger_actions(handle, config64, (uint64_t*)&trigger_actions);
    }

    if(result == ST_OK){
        return trigger_actions;
    }

    return result;
}

JNIEXPORT jlong JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getAnimalDetectConfig(JNIEnv *env, jobject obj) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if(handle == NULL){
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    uint64_t value = 0;
    result =  st_mobile_effect_get_animal_detect_config(handle, (uint64_t*)&value);

    if(result == ST_OK){
        return value;
    }

    return result;
}

JNIEXPORT jlong JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_getCustomParamConfig(JNIEnv *env, jobject obj) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if(handle == NULL){
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    uint64_t value = 0;
    result =  st_mobile_effect_get_custom_param_config(handle, (uint64_t*)&value);

    if(result == ST_OK){
        return value;
    }

    return result;
}

// 判断人脸是否包含在矩形框中
bool is_face_in_rect(st_rect_t rect, int left, int top, int right, int bottom) {
    // 检查人脸的坐标是否完全位于矩形框的边界内
    if (left >= rect.left && top >= rect.top &&
        right <= rect.right && bottom <= rect.bottom) {
        return true; // 人脸完全在矩形框内
    } else {
        return false; // 人脸不在矩形框内
    }
}

//bool is_face_in_rect(st_rect_t rect, int left, int top, int right, int bottom) {
//    // 检查人脸的坐标是否完全位于矩形框的边界内
//    if (left >= rect.left && top >= rect.top &&
//        right <= rect.right && bottom <= rect.bottom) {
//        return true; // 人脸完全在矩形框内
//    } else {
//        return false; // 人脸不在矩形框内
//    }
//}

// 计算矩形的面积
int calculate_area(st_rect_t rect) {
    int width = rect.right - rect.left;
    int height = rect.bottom - rect.top;
    if (width <= 0 || height <= 0) {
        return 0; // 非法矩形或面积为 0
    }
    return width * height;
}
// 判断两个矩形是否有交集（允许误差）
bool is_face_in_rect(st_rect_t detectedFace, st_rect_t inputRect){
    LOGE("detectedFace left=%d top=%d, right=%d, bottom=%d", detectedFace.left, detectedFace.top, detectedFace.right, detectedFace.bottom);
    // 如果 这两个矩形框80%重叠则返回true
// 计算交集矩形
    int intersectionLeft = detectedFace.left > inputRect.left ? detectedFace.left : inputRect.left;
    int intersectionTop = detectedFace.top > inputRect.top ? detectedFace.top : inputRect.top;
    int intersectionRight = detectedFace.right < inputRect.right ? detectedFace.right : inputRect.right;
    int intersectionBottom = detectedFace.bottom < inputRect.bottom ? detectedFace.bottom : inputRect.bottom;

    // 判断是否有交集
    if (intersectionLeft >= intersectionRight || intersectionTop >= intersectionBottom) {
        return false; // 无交集
    }

    // 计算交集面积
    st_rect_t intersectionRect = {
            .left = intersectionLeft,
            .top = intersectionTop,
            .right = intersectionRight,
            .bottom = intersectionBottom
    };
    int intersectionArea = calculate_area(intersectionRect);

    // 计算原矩形的面积
    int detectedFaceArea = calculate_area(detectedFace);
    int inputRectArea = calculate_area(inputRect);

    // 判断是否交集面积占 80% 或更多
    double overlapRatio1 = (double)intersectionArea / detectedFaceArea;
    double overlapRatio2 = (double)intersectionArea / inputRectArea;

    return overlapRatio1 >= 0.8 || overlapRatio2 >= 0.8;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_render(JNIEnv * env, jobject obj, jobject inputParams, jobject outputParams, jboolean needOutputHumanAction)
{
    st_handle_t handle = getEffectHandle(env, obj);

    if(handle == NULL){
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    st_effect_render_in_param_t *in_param = new st_effect_render_in_param_t;
    if (!convert2st_effect_render_in_param(env, inputParams, in_param)) {
        memset(in_param, 0, sizeof(st_effect_render_in_param_t));
    }

    st_effect_render_out_param_t *out_param = new st_effect_render_out_param_t;
    if (!convert2st_effect_render_out_param(env, outputParams, out_param)) {
        memset(out_param, 0, sizeof(st_effect_render_out_param_t));
    }

    if(needOutputHumanAction && in_param->p_human != nullptr && out_param->p_human == nullptr){
        out_param->p_human = new st_mobile_human_action_t;
        memset(out_param->p_human, 0, sizeof(st_mobile_human_action_t));
        st_mobile_human_action_copy(in_param->p_human, out_param->p_human);
    }

    jclass clazz = env->FindClass("com/softsugar/stmobile/model/STEffectRenderInParam");
    jfieldID fieldTargetFaceID = env->GetFieldID(clazz, "targetFaceId", "I");
    int targetFaceID = env->GetIntField(inputParams, fieldTargetFaceID);

    st_rect_t st_rect = {};
    memset(&st_rect, 0, sizeof(st_rect_t));
    bool has_target_rect = convert2STRect(env, inputParams, &st_rect);
    // LOGE("st_rect_t left=%d top=%d right=%d bottom=%d", st_rect.left, st_rect.top, st_rect.right, st_rect.bottom);
    env->DeleteLocalRef(clazz);

    st_effect_render_in_param_t in_param_tmp = {0};
    st_mobile_human_action_t tmp = {0};
    st_mobile_human_action_segments_t tmp_segs = {0};
    if (targetFaceID != -1 || has_target_rect) {
        tmp = *in_param->p_human;
        tmp_segs = *in_param->p_human->p_segments;
        tmp.p_segments = &tmp_segs;

        in_param_tmp = *in_param;
        in_param_tmp.p_human = &tmp;
        //in_param_tmp.animal_face_count  = 0;
        //in_param_tmp.p_animal_face = nullptr;
    }

    int find_idx = -1;
    if ((targetFaceID != -1 || has_target_rect) && in_param->p_human != NULL && in_param->p_human->face_count >= 1) {
        int count = in_param->p_human->face_count;
        for (int i=0; i<count; i++) {
            st_mobile_face_t *pFace = in_param->p_human->p_faces + i;
            if (pFace->face106.ID == targetFaceID || is_face_in_rect(pFace->face106.rect, st_rect)) {
                find_idx = i;
                break;
            }
        }
        LOGE("find_idx %d", find_idx);
        if (find_idx >= 0) {// 有目标人脸
            in_param_tmp.p_human->face_count = 1;
            in_param_tmp.p_human->p_faces = in_param->p_human->p_faces + find_idx;

            st_mobile_human_action_segments_t *pSegments = in_param_tmp.p_human->p_segments;
            if (pSegments) {
                if (pSegments->head_count > 1) {
                    for (int i=0; i<pSegments->head_count;i++) {
                        st_mobile_segment_t *pSegment = pSegments->p_head + i;
                        if (pSegment->face_id == targetFaceID) {
                            pSegments->p_head = pSegment;
                            pSegments->head_count = 1;
                            break;
                        }
                    }
                }
                if (pSegments->face_occlusion_count> 1) {
                    for (int i=0; i<pSegments->face_occlusion_count;i++) {
                        st_mobile_segment_t *pSegment = pSegments->p_face_occlusion + i;
                        if (pSegment->face_id == targetFaceID) {
                            pSegments->p_face_occlusion = pSegment;
                            pSegments->face_occlusion_count = 1;
                            break;
                        }
                    }
                }
                if (pSegments->mouth_parse_count> 1) {
                    for (int i=0; i<pSegments->mouth_parse_count; i++) {
                        st_mobile_segment_t *pSegment = pSegments->p_mouth_parse + i;
                        if (pSegment->face_id == targetFaceID) {
                            pSegments->p_mouth_parse = pSegment;
                            pSegments->mouth_parse_count = 1;
                            break;
                        }
                    }
                }

                if (pSegments->skin_count> 1) {
                    for (int i=0; i<pSegments->skin_count; i++) {
                        st_mobile_segment_t *pSegment = pSegments->p_skin + i;
                        if (pSegment->face_id == targetFaceID) {
                            pSegments->p_skin = pSegment;
                            pSegments->skin_count = 1;
                            break;
                        }
                    }
                }
            }
        } else {
            memset(in_param_tmp.p_human, 0x0, sizeof(st_mobile_human_action_t));
        }
    }

//    if(in_param->p_human != nullptr) {
//        LOGE("p_faces ptr=%p", in_param->p_human->p_faces);
//    }

    int result;
    if (targetFaceID != -1 || has_target_rect) {
        result = st_mobile_effect_render(handle, &in_param_tmp, out_param);
    } else {
        result = st_mobile_effect_render(handle, in_param, out_param);
    }

    if(result == ST_OK){
        convert2STEffectRenderOutParam(env, out_param, outputParams);
    }

    releaseEffectRenderInputParams(in_param);
    releaseEffectRenderOutputParams(out_param);

    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_replayPackage(JNIEnv * env, jobject obj, jint packageId){
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, obj);

    if (handle == NULL) {
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    if(handle != NULL) {
        result = st_mobile_effect_replay_package(handle, packageId);
    }

    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileEffectNative_setSoundPlayDone(JNIEnv *env, jobject obj,
                                                                  jstring name) {
    st_handle_t handle = getEffectHandle(env, obj);
    if(handle == NULL) {
        LOGE("effectHandle is null");
        return ST_E_HANDLE;
    }
    if (name != NULL) {
        const char *nameCstr = NULL;
        nameCstr = env->GetStringUTFChars(name, 0);
        if (nameCstr != NULL) {
            st_effect_module_info_t moduleInfo;
            memset(&moduleInfo, 0, sizeof(moduleInfo));
            moduleInfo.type = EFFECT_MODULE_SOUND;
            strcpy(moduleInfo.name, nameCstr);
            moduleInfo.state = EFFECT_MODULE_PAUSED_LAST_FRAME;

            int ret = st_mobile_effect_set_module_info(handle, &moduleInfo);
            LOGI("st_mobile_effect_set_module_info ret: %d", ret);
            env->ReleaseStringUTFChars(name, nameCstr);
        } else {
            LOGE("Sound name is NULL");
            return ST_E_INVALIDARG;
        }

        LOGI("Set play done success");
        return ST_OK;
    }

    return ST_OK;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_setPackageBeautyGroupStrength(JNIEnv * env, jobject obj, jint pag_id, jint group, jfloat strength)
{
    st_handle_t handle = getEffectHandle(env, obj);
    if(handle == NULL){
        return ST_E_HANDLE;
    }
    int result = st_mobile_effect_set_package_beauty_group_strength(handle, pag_id, (st_effect_beauty_group_t)group, strength);
    LOGE("st_mobile_effect_set_package_beauty_group_strength group=%d strength=%f", group, strength);

    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileEffectNative_releaseCachedResource(JNIEnv *env, jobject thiz) {
    st_handle_t handle = getEffectHandle(env, thiz);
    if(handle == NULL){
        return ST_E_HANDLE;
    }
    return st_mobile_effect_release_cached_resource(handle);
}

JNIEXPORT jobjectArray JNICALL Java_com_softsugar_stmobile_STMobileEffectNative_get3DBeautyParts(JNIEnv *env, jobject thiz) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, thiz);
    if(handle == NULL){
        return NULL;
    }

    int count = 0;
    st_moobile_effect_get_3d_beauty_parts_count(handle, &count);

    st_effect_3D_beauty_part_info_t* beauty_part_info = NULL;
    if (count > 0) {
        beauty_part_info = (st_effect_3D_beauty_part_info_t *)malloc(count * sizeof(st_effect_3D_beauty_part_info_t));
    }

    if (handle != NULL) {
        result = st_mobile_effect_get_3d_beauty_parts(handle, beauty_part_info, count);
    }
    LOGE("st_mobile_effect_get_3d_beauty_parts ret=%d", result);

    jclass partInfoClazz = env->FindClass("com/softsugar/stmobile/model/STEffect3DBeautyPartInfo");
    jobjectArray partInfoArray = env->NewObjectArray(count, partInfoClazz, 0);
    for(int i = 0; i < count; i++) {
        jobject partInfoObj;
        partInfoObj = convert2Effect3DBeautyPartInfo(env, beauty_part_info + i);
        env->SetObjectArrayElement(partInfoArray, i, partInfoObj);
        env->DeleteLocalRef(partInfoObj);
    }
    env->DeleteLocalRef(partInfoClazz);
    if (beauty_part_info) {
        safe_delete(beauty_part_info);
    }
    return partInfoArray;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileEffectNative_set3dBeautyPartsStrength(JNIEnv *env, jobject thiz,
                                                                              jobjectArray effect3_dbeauty_part_info, jint length) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, thiz);
    if(handle == NULL){
        return ST_E_HANDLE;
    }
    st_effect_3D_beauty_part_info_t *info_array = new st_effect_3D_beauty_part_info_t[length];
    for(int i = 0; i < length; i++) {
        jobject infoObj = env->GetObjectArrayElement(effect3_dbeauty_part_info, i);
        if(!convert2Effect3DBeautyPartInfo(env, infoObj, info_array + i)) {
            memset(&info_array, 0, sizeof(info_array));
        }
        env->DeleteLocalRef(infoObj);
    }

    for(int i = 0;i<length;i++) {
        st_effect_3D_beauty_part_info_t *inputParam = (info_array + i);
        //LOGE("3DBeautyPartInfo--- name:%s, strength: %f , part_id %d", inputParam->name, inputParam->strength, inputParam->part_id);
    }
    result = st_mobile_effect_set_3d_beauty_parts_strength(handle, info_array, length);
    //LOGE("set_3d_beauty_part result %d", result);
    if (info_array) {
        delete[] info_array;
        info_array = NULL;
    }
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileEffectNative_setFaceMeshList(JNIEnv *env, jobject thiz,
                                                                 jobject face_mesh_list) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, thiz);
    if(handle == NULL){
        return ST_E_HANDLE;
    }
    if (face_mesh_list == NULL)
        return -1;

    st_mobile_face_mesh_list_t *p_face_mesh_list = new st_mobile_face_mesh_list_t;
    memset(p_face_mesh_list, 0, sizeof(st_mobile_face_mesh_list_t));
    convert2FaceMeshList(env, face_mesh_list, p_face_mesh_list);

    result = st_mobile_effect_set_face_mesh_list(handle, p_face_mesh_list);
    LOGE("st_mobile_effect_set_face_mesh_list ret=%d", result);

    if (p_face_mesh_list->p_face_mesh_index) {
        safe_delete_array(p_face_mesh_list->p_face_mesh_index);
    }
    safe_delete(p_face_mesh_list);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileEffectNative_getCustomEventNeeded(JNIEnv *env, jobject thiz) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, thiz);

    if(handle == NULL){
        LOGE("handle is null");
        return ST_E_HANDLE;
    }

    uint64_t value = 0;
    result =  st_mobile_effect_get_custom_event_config(handle, (uint64_t*)&value);

    if(result == ST_OK){
        return value;
    }

    return result;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_softsugar_stmobile_STMobileEffectNative_getDefaultCameraQuaternion(JNIEnv *env,
                                                                            jobject thiz,
                                                                            jboolean front_camera) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, thiz);
    if(handle == NULL){
        return NULL;
    }
    st_quaternion_t *quaternion = new st_quaternion_t();
    st_mobile_effect_get_default_camera_quaternion(handle, front_camera, quaternion);
    jobject obj = convert2Quaternion(env, quaternion);
    if (quaternion) {
        safe_delete(quaternion);
    }
    return obj;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileEffectNative_changeBg(JNIEnv *env, jobject thiz,
                                                          jint packageId, jobject image) {
    LOGE("changeBg called");
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, thiz);
    if(handle == NULL){
        return NULL;
    }

    st_effect_package_info_t *package_info = new st_effect_package_info_t;
    memset(package_info, 0, sizeof(st_effect_package_info_t));
    st_mobile_effect_get_package_info(handle, packageId, package_info);
    int moduleCount = package_info->module_count;
    if (moduleCount == 0)
        return result;
    st_effect_module_info_t* pModuleInfos = NULL;
    if (moduleCount > 0) {
        pModuleInfos = (st_effect_module_info_t *)malloc(moduleCount * sizeof(st_effect_module_info_t));
    }
    st_mobile_effect_get_modules_in_package(handle, packageId, pModuleInfos, package_info->module_count);
    st_effect_module_info_t moduleInfo = pModuleInfos[0];
    moduleInfo.rsv_type = EFFECT_RESERVED_IMAGE;
    st_image_t *st_image = new st_image_t();
    convert2Image(env, image, st_image);
    st_image->stride = getImageStride(st_image->pixel_format, st_image->width);
    moduleInfo.reserved = st_image;

    st_result_t ret = st_mobile_effect_set_module_info(handle, &moduleInfo);
    if (st_image) {
        safe_delete_array(st_image->data);
        safe_delete(st_image);
    }
    safe_delete(package_info);
    safe_delete_array(pModuleInfos);
    return ret;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileEffectNative_changeBg2(JNIEnv *env, jobject thiz,
                                                           jint packageId, jobject stEffectTexture) {
    LOGE("changeBg called");
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, thiz);
    if(handle == NULL){
        return NULL;
    }

    st_effect_package_info_t *package_info = new st_effect_package_info_t;
    memset(package_info, 0, sizeof(st_effect_package_info_t));
    st_mobile_effect_get_package_info(handle, packageId, package_info);
    int moduleCount = package_info->module_count;
    if (moduleCount == 0)
        return result;
    st_effect_module_info_t* pModuleInfos = NULL;
    if (moduleCount > 0) {
        pModuleInfos = (st_effect_module_info_t *)malloc(moduleCount * sizeof(st_effect_module_info_t));
    }
    st_mobile_effect_get_modules_in_package(handle, packageId, pModuleInfos, package_info->module_count);
    st_effect_module_info_t moduleInfo = pModuleInfos[0];
    moduleInfo.rsv_type = EFFECT_RESERVED_TEXTURE;

    st_mobile_texture_t mobile_texture;
    memset(&mobile_texture, 0 , sizeof(mobile_texture));
    convert2st_effect_texture(env, stEffectTexture, &mobile_texture);
    mobile_texture.format = ST_PIX_FMT_RGBA8888;
    moduleInfo.reserved = &mobile_texture;
    st_result_t ret = st_mobile_effect_set_module_info(handle, &moduleInfo);
    safe_delete(package_info);
    safe_delete_array(pModuleInfos);
    return ret;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileEffectNative_setEffectModuleInfo(JNIEnv *env, jobject thiz,
                                                           jobject ganReturn, jobject moduleInfo) {
    LOGE("changeBg called");
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, thiz);
    if(handle == NULL){
        return NULL;
    }

    st_effect_module_info_t *module_info = new st_effect_module_info_t;
    if (!convert2st_effect_module_info(env, module_info, moduleInfo)) {
        memset(moduleInfo, 0, sizeof(st_effect_module_info_t));
    }

    st_gan_return_t *gan_return = new st_gan_return_t;
    if (!convert2GanReturn(env, ganReturn, gan_return)) {
        memset(gan_return, 0, sizeof(st_gan_return_t));
    }

    module_info->type = EFFECT_MODULE_GAN_IMAGE;
    module_info->rsv_type = EFFECT_RESERVED_GAN_SERVER;
    if (gan_return->out_image == NULL) {
        module_info->reserved = NULL;
    } else {
        module_info->reserved = gan_return;
    }
    st_result_t ret = st_mobile_effect_set_module_info(handle, module_info);
    safe_delete(gan_return);
    safe_delete(module_info);
    return ret;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STMobileEffectNative_setGreenMaskSafeRegions(JNIEnv *env, jobject thiz,
                                                           jint num, jobjectArray safeRegions) {
    int result = ST_OK;
    st_handle_t handle = getEffectHandle(env, thiz);
    if(handle == NULL){
        return NULL;
    }

    st_effect_safe_region_t *region_array = new st_effect_safe_region_t[num];
    for(int i = 0; i < num; i++) {
        jobject infoObj = env->GetObjectArrayElement(safeRegions, i);
        if(!convert2st_effect_safe_region(env, infoObj, region_array + i)) {
            memset(&region_array, 0, sizeof(region_array));
        }
        env->DeleteLocalRef(infoObj);
    }

    result = st_mobile_effect_set_greenmask_safe_regions(handle, region_array, num);
    if (region_array) {
        delete[] region_array;
        region_array = NULL;
    }
    return result;
}