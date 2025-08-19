package com.softsugar.stmobile;

import android.content.res.AssetManager;

import com.softsugar.stmobile.model.STImage;
import com.softsugar.stmobile.model.STMobile106;


/**
 * \~Chinese 人脸比对JNI定义 \~English Face verify api
 */
public class STMobileFaceVerifyNative {
    /**
     * \~Chinese 加载libs \~English Load library
     */
    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    //供底层使用，不需要关注
    private long STMobileFaceVerifyNativeHandle;

    /**
     * \~Chinese 创建实例
     *
     * @param modelpath \~Chinese 模型路径
     * @return 成功返回0，错误返回其他，参考STCommon.ResultCode
     *
     * \~English create handle
     *
     * @param modelpath \~English model path
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstance(String modelpath);

    /**
     * \~Chinese 从assets资源文件创建实例
     *
     * @param assetModelpath \~Chinese 模型路径
     * @return 成功返回0，错误返回其他，参考STCommon.ResultCode
     *
     * \~English create handle from asset file
     *
     * @param assetModelpath \~English asset model path
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstanceFromAssetFile(String assetModelpath, AssetManager assetManager);

    /**
     * \~Chinese 获取人脸特征
     *
     * @param image    \~Chinese 用于检测的图像数据
     * @param face106  \~Chinese 输入待处理的人脸信息，需要包括关键点信息
     * @return 成功返回0，错误返回其他，参考STCommon.ResultCode
     *
     * \~English Get face feature
     *
     * @param image    \~English image info
     * @param face106  \~English face 106 info
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native byte[] getFeature(STImage image, STMobile106 face106);

    /**
     * \~Chinese 获取人脸比对分数
     *
     * @param feature_01  \~Chinese 输入人脸特征1
     * @param feature_02  \~Chinese 输入人脸特征2
     * @return 成功返回0，错误返回其他，参考STCommon.ResultCode
     *
     * \~English Get face features compare score
     *
     * @param feature_01  \~English face feature 1
     * @param feature_02  \~English face feature 2
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native float getFeaturesCompareScore(byte[] feature_01, byte[] feature_02);

    /**
     * \~Chinese 释放实例 \~English release handle
     */
    public native void destroyInstance();
}
