package com.softsugar.stmobile;

import android.content.res.AssetManager;

import com.softsugar.stmobile.model.STFaceAttribute;
import com.softsugar.stmobile.model.STMobile106;
import com.softsugar.stmobile.model.STMobileFaceInfo;


/**
 * \~Chinese 人脸属性JNI定义 \~English Face attribute api
 */
public class STMobileFaceAttributeNative {
    /**
     * \~Chinese 加载libs \~English Load library
     */
    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    //供底层使用，不需要关注
    private long nativeHandle;

    /**
     * \~Chinese 人脸属性检测
     *
     * @param image      \~Chinese 用于检测的图像数据
     * @param format     \~Chinese 用于检测的图像数据的像素格式, 支持所有彩色图像格式，推荐STCommon.ST_PIX_FMT_BGR888，不建议使用STCommon.ST_PIX_FMT_GRAY8结果不准确
     * @param width      \~Chinese 用于检测的图像的宽度(以像素为单位)
     * @param height     \~Chinese 用于检测的图像的高度(以像素为单位)
     * @param mobile106  \~Chinese 输入待处理的人脸信息，需要包括关键点信息
     * @param attributes \~Chinese [out]输出人脸属性
     * @return 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English face attribute detect
     *
     * @param image      \~English image date
     * @param format     \~English image format, STCommon.ST_PIX_FMT_BGR888
     * @param width      \~English image width
     * @param height     \~English image height
     * @param mobile106  \~English face 106 info
     * @param attributes \~English [out]face attributes
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int detect(byte[] image, int format, int width, int height, STMobile106[] mobile106, STFaceAttribute[] attributes);

    /**
     * \~Chinese 人脸属性检测
     *
     * @param image      \~Chinese 用于检测的图像数据
     * @param format     \~Chinese 用于检测的图像数据的像素格式, 支持所有彩色图像格式，推荐STCommon.ST_PIX_FMT_BGR888，不建议使用STCommon.ST_PIX_FMT_GRAY8结果不准确
     * @param width      \~Chinese 用于检测的图像的宽度(以像素为单位)
     * @param height     \~Chinese 用于检测的图像的高度(以像素为单位)
     * @param faces      \~Chinese 输入待处理的人脸信息
     * @param attributes [out]输出人脸属性
     * @return 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English face attribute detect
     *
     * @param image      \~English image date
     * @param format     \~English image format, STCommon.ST_PIX_FMT_BGR888
     * @param width      \~English image width
     * @param height     \~English image height
     * @param faces      \~English face info
     * @param attributes \~English [out]face attributes
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int detect2(byte[] image, int format, int width, int height, STMobileFaceInfo[] faces, STFaceAttribute[] attributes);

    /**
     * \~Chinese 人脸属性检测
     *
     * @param image      \~Chinese 用于检测的图像数据
     * @param format     \~Chinese 用于检测的图像数据的像素格式, 支持所有彩色图像格式，推荐STCommon.ST_PIX_FMT_BGR888，不建议使用STCommon.ST_PIX_FMT_GRAY8结果不准确
     * @param width      \~Chinese 用于检测的图像的宽度(以像素为单位)
     * @param height     \~Chinese 用于检测的图像的高度(以像素为单位)
     * @param mobile106  \~Chinese 输入待处理的人脸信息，需要包括关键点信息
     * @param attributes \~Chinese [out]输出人脸属性
     * @return 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English face attribute detect
     *
     * @param image      \~English image date
     * @param format     \~English image format, STCommon.ST_PIX_FMT_BGR888
     * @param width      \~English image width
     * @param height     \~English image height
     * @param mobile106  \~English face 106 info
     * @param attributes \~English [out]face attributes
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int detect_ext(byte[] image, int format, int width, int height, STMobile106[] mobile106, float[][] attributes);

    /**
     * \~Chinese 人脸属性检测
     *
     * @param image      \~Chinese 用于检测的图像数据
     * @param format     \~Chinese 用于检测的图像数据的像素格式, 支持所有彩色图像格式，推荐STCommon.ST_PIX_FMT_BGR888，不建议使用STCommon.ST_PIX_FMT_GRAY8结果不准确
     * @param width      \~Chinese 用于检测的图像的宽度(以像素为单位)
     * @param height     \~Chinese 用于检测的图像的高度(以像素为单位)
     * @param faces      \~Chinese 输入待处理的人脸信息
     * @param attributes \~Chinese [out]输出人脸属性
     * @return 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English face attribute detect
     *
     * @param image      \~English image date
     * @param format     \~English image format, STCommon.ST_PIX_FMT_BGR888
     * @param width      \~English image width
     * @param height     \~English image height
     * @param faces      \~English face info
     * @param attributes \~English [out]face attributes
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int detect_ext2(byte[] image, int format, int width, int height, STMobileFaceInfo[] faces, float[][] attributes);

    /**
     * \~Chinese 创建实例
     *
     * @param modelPath \~Chinese 模型路径
     * @return 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English create handle from asset file
     *
     * @param modelPath \~English model path
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstance(String modelPath);

    /**
     * \~Chinese 从assets资源文件创建实例
     *
     * @param assetModelPath \~Chinese 模型路径
     * @return 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English create handle from asset file
     *
     * @param assetModelPath \~English asset model path
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstanceFromAssetFile(String assetModelPath, AssetManager assetManager);

    /**
     * \~Chinese 释放实例 \~English release handle
     */
    public native void destroyInstance();
}
