#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <st_mobile_common.h>
#include "utils.h"
#include <GLES2/gl2.h>

#define  LOG_TAG    "STCommonNative"

extern "C" {
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STCommonNative_stColorConvert(JNIEnv * env, jobject obj, jbyteArray imagesrc,
                                                                                     jbyteArray imagedst, jint imageWidth, jint imageHeight, jint type);
    JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STCommonNative_stImageRotate(JNIEnv * env, jobject obj, jbyteArray imagesrc, jbyteArray imagedst, jint imageWidth, jint imageHeight, jint format, jint rotation);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setEyeblinkThreshold(JNIEnv * env, jobject obj, jfloat threshold);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setMouthahThreshold(JNIEnv * env, jobject obj, jfloat threshold);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setHeadyawThreshold(JNIEnv * env, jobject obj, jfloat threshold);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setHeadpitchThreshold(JNIEnv * env, jobject obj, jfloat threshold);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setBrowjumpThreshold(JNIEnv * env, jobject obj, jfloat threshold);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setSmoothThreshold(JNIEnv * env, jobject obj, jfloat threshold);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setHeadposeThreshold(JNIEnv * env, jobject obj, jfloat threshold);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_glReadPixels(JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height,jint format, jint type);
    JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setEnv(JNIEnv * env, jobject obj, jstring systemPath);
};

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STCommonNative_stColorConvert(JNIEnv * env, jobject obj, jbyteArray imagesrc, jbyteArray imagedst, jint imageWidth, jint imageHeight, jint type)
{
    jbyte *srcdata = (jbyte*) (env->GetPrimitiveArrayCritical(imagesrc, 0));
    jbyte *dstdata = (jbyte*) env->GetPrimitiveArrayCritical(imagedst, 0);

    int result = (int)st_mobile_color_convert((unsigned char *)srcdata,(unsigned char *)dstdata,imageWidth,imageHeight,(st_color_convert_type)type);

    env->ReleasePrimitiveArrayCritical(imagesrc, srcdata, 0);
    env->ReleasePrimitiveArrayCritical(imagedst, dstdata, 0);
    return result;
}

JNIEXPORT jint JNICALL Java_com_softsugar_stmobile_STCommonNative_stImageRotate(JNIEnv * env, jobject obj, jbyteArray imagesrc, jbyteArray imagedst, jint imageWidth, jint imageHeight, jint format, jint rotation)
{
    jbyte *srcdata = (jbyte*) (env->GetPrimitiveArrayCritical(imagesrc, 0));
    jbyte *dstdata = (jbyte*) env->GetPrimitiveArrayCritical(imagedst, 0);

    st_pixel_format pixel_format = (st_pixel_format)format;
    int stride = getImageStride(pixel_format, imageWidth);

    long startTime = getCurrentTime();
    int result = (int)st_mobile_image_rotate((unsigned char *)srcdata,(unsigned char *)dstdata,imageWidth,imageHeight,stride, pixel_format, (st_rotate_type)rotation);
    long afterdetectTime = getCurrentTime();

    env->ReleasePrimitiveArrayCritical(imagesrc, srcdata, 0);
    env->ReleasePrimitiveArrayCritical(imagedst, dstdata, 0);
   return result;
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setEnv(JNIEnv * env, jobject obj, jstring systemPath)
{
    if(systemPath != nullptr){
        // 获取传递的路径
        const char *system_path = env->GetStringUTFChars(systemPath, 0);

        setenv("ADSP_LIBRARY_PATH", system_path, 1);
        setenv("LD_LIBRARY_PATH", system_path, 1);
    }
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setEyeblinkThreshold(JNIEnv * env, jobject obj, jfloat threshold)
{
    float detect_threshold = threshold;
    st_mobile_set_eyeblink_threshold(detect_threshold);
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setMouthahThreshold(JNIEnv * env, jobject obj, jfloat threshold)
{
    float detect_threshold = threshold;
    st_mobile_set_mouthah_threshold(detect_threshold);
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setHeadyawThreshold(JNIEnv * env, jobject obj, jfloat threshold)
{
    float detect_threshold = threshold;
    st_mobile_set_headyaw_threshold(detect_threshold);
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setHeadpitchThreshold(JNIEnv * env, jobject obj, jfloat threshold)
{
    float detect_threshold = threshold;
    st_mobile_set_headpitch_threshold(detect_threshold);
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setBrowjumpThreshold(JNIEnv * env, jobject obj, jfloat threshold)
{
    float detect_threshold = threshold;
    st_mobile_set_browjump_threshold(detect_threshold);
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setSmoothThreshold(JNIEnv * env, jobject obj, jfloat threshold)
{
    float detect_threshold = threshold;
    //st_mobile_set_smooth_threshold(detect_threshold);
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_setHeadposeThreshold(JNIEnv * env, jobject obj, jfloat threshold)
{
    float detect_threshold = threshold;
    st_mobile_set_headpose_threshold(detect_threshold);
}

JNIEXPORT void JNICALL Java_com_softsugar_stmobile_STCommonNative_glReadPixels(JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height,jint format, jint type) {
    glReadPixels(x, y, width, height, format, type, 0);
}

extern "C"
JNIEXPORT int JNICALL
Java_com_softsugar_stmobile_STCommonNative_setLogLevel(JNIEnv *env, jobject thiz, jint log_level) {
    return st_mobile_set_log_level((st_log_level_t)log_level);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_softsugar_stmobile_STCommonNative_getVersion(JNIEnv *env, jclass clazz) {
    const char *version = st_mobile_get_version();
    return charToJstring(env, version);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STCommonNative_getLogLevel(JNIEnv *env, jobject thiz) {
    st_log_level_t stLogLevel;
    st_mobile_get_log_level(&stLogLevel);
    return stLogLevel;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_softsugar_stmobile_STCommonNative_redirectLogToFile(JNIEnv *env, jobject thiz, jstring file_path,
                                                       jboolean tranc_file) {
    int ret=-1;
    const char* p_file_path = env->GetStringUTFChars(file_path, 0);
    ret = st_mobile_redirect_log_to_file(p_file_path,tranc_file);
    env->ReleaseStringUTFChars(file_path, p_file_path);
    return ret;
}