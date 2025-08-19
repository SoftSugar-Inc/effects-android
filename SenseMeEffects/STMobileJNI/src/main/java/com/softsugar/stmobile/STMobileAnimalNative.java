package com.softsugar.stmobile;


import android.content.res.AssetManager;

import com.softsugar.stmobile.model.STMobileAnimalResult;

/**
 * \~Chinese 动物信息检测 \~English Animal detect
 */
public class STMobileAnimalNative {

    /**
     * \~Chinese 加载libs \~English Load library
     */
    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    //供JNI使用，应用不需要关注
    private long nativeAnimalHandle;

    public final static int ST_MOBILE_CAT_DETECT = 0x00000001;  ///< \~Chinese 猫脸检测 \~English cat detect
    public final static int ST_MOBILE_DOG_DETECT = 0x00000010;  ///< \~Chinese 狗脸检测 \~English dog detect

    public final static int ST_MOBILE_DETECT_MODE_VIDEO = 0x00020000;  ///< \~Chinese 视频检测 \~English detect mode video
    public final static int ST_MOBILE_DETECT_MODE_IMAGE = 0x00040000;  ///< \~Chinese 图片检测 \~English detect mode image

    /**
     * \~Chinese 创建实例
     *
     * @param modelpath \~Chinese 模型文件的,例如models/face_track_2.x.x.model
     * @param config    \~Chinese 配置选项 例如ST_MOBILE_TRACKING_MULTI_THREAD,默认使用双线程跟踪,实时视频预览建议使用该配置.使用单线程算法建议选择（ST_MOBILE_TRACKING_SINGLE_THREAD) 图片版建议选择ST_MOBILE_DETECT_MODE_IMAGE
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     *
     * \~English Create handle
     *
     * @param modelpath \~English modele path, for example models/face_track_2.x.x.model
     * @param config    \~English config, for eample ST_MOBILE_TRACKING_MULTI_THREAD
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstance(String modelpath, int config);

    /**
     * \~Chinese 创建实例
     *
     * @param assetModelpath \~Chinese 模型文件的,例如models/face_track_2.x.x.model
     * @param config         \~Chinese 配置选项 例如ST_MOBILE_TRACKING_MULTI_THREAD,默认使用双线程跟踪,实时视频预览建议使用该配置. 使用单线程算法建议选择（ST_MOBILE_TRACKING_SINGLE_THREAD) 图片版建议选择ST_MOBILE_DETECT_MODE_IMAGE
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     *
     * \~English Create handle from asset file
     *
     * @param assetModelpath \~English asset mode path
     * @param config         \~English config
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstanceFromAssetFile(String assetModelpath, int config, AssetManager assetManager);


    /**
     * \~Chinese 设置参数
     * @param type  \~Chinese 要设置Animal参数的类型
     * @param value \~Chinese 设置的值
     * @return \~Chinese 成功返回0，错误返回其他，参考STUtils.ResultCode
     *
     * \~English Set param
     * @param type  \~English type
     * @param value \~English value
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setParam(int type, float value);

    /**
     * \~Chinese 重置，清除所有缓存信息
     *
     * @return \~Chinese 成功返回0，错误返回其他，参考STUtils.ResultCode
     *
     * \~English reset
     *
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int reset();

    /**
     * \~Chinese 释放instance \~English release handle
     */
    public native void destroyInstance();

    /**
     * \~Chinese 检测动物脸信息
     *
     * @param imgData       \~Chinese 用于检测的图像数据
     * @param imageFormat   \~Chinese 用于检测的图像数据的像素格式,比如STCommon.ST_PIX_FMT_NV12。能够独立提取灰度通道的图像格式处理速度较快，比如ST_PIX_FMT_GRAY8，ST_PIX_FMT_YUV420P，ST_PIX_FMT_NV12，ST_PIX_FMT_NV21
     * @param orientation   \~Chinese 图像中猫脸的方向,例如STRotateType.ST_CLOCKWISE_ROTATE_0
     * @param imageWidth    \~Chinese 用于检测的图像的宽度(以像素为单位)
     * @param imageHeight   \~Chinese 用于检测的图像的高度(以像素为单位)
     * @return \~Chinese 成功返回animal_face检测结果，错误返回null
     *
     * \~English Animal face detect
     *
     * @param imgData       \~English image data
     * @param imageFormat   \~English image format, for example STCommon.ST_PIX_FMT_NV12
     * @param orientation   \~English orientation, for example STRotateType.ST_CLOCKWISE_ROTATE_0
     * @param imageWidth    \~English image width
     * @param imageHeight   \~English image heght
     * @return \~English  return animal detect result
     */
    public native STMobileAnimalResult animalDetect(byte[] imgData, int imageFormat, int orientation, int detectConfig, int imageWidth, int imageHeight);

    public native STMobileAnimalResult animalFaceCopy(STMobileAnimalResult animalSrc);

    /**
     * \~Chinese 镜像猫脸检测结果
     *
     * @param width  \~Chinese 用于转换的图像的宽度(以像素为单位)
     * @param result \~Chinese 旋转animal_face检测结果
     * @return \~Chinese 成功返回新animal_face检测结果，错误返回null
     *
     * \~English Mirror animal info
     *
     * @param width  \~English width
     * @param result \~English animal result
     * @return \~English  return animal detect result
     */
    public static native STMobileAnimalResult animalMirror(int width, STMobileAnimalResult result);

    /**
     * \~Chinese 旋转animal检测结果
     *
     * @param width        \~Chinese 用于转换的图像的宽度(以像素为单位)
     * @param height       \~Chinese 用于转换的图像的高度(以像素为单位)
     * @param orientation  \~Chinese 顺时针旋转的角度,例如STRotateType.ST_CLOCKWISE_ROTATE_90（旋转90度）
     * @param result       \~Chinese 旋转animal_face检测结果
     * @return \~Chinese 成功返回新animal_face检测结果，错误返回null
     *
     * \~English rotate animal info
     *
     * @param width        \~English width
     * @param height       \~English height
     * @param orientation  \~English orientation, for example STRotateType.ST_CLOCKWISE_ROTATE_90
     * @param result       \~English animal_face result
     * @return \~English  return animal detect result
     */
    public static native STMobileAnimalResult animalRotate(int width, int height, int orientation, STMobileAnimalResult result);

    /**
     * \~Chinese resize animal检测结果
     *
     * @param scale  \~Chinese  缩放的尺度
     * @param result \~Chinese 旋转animal_face检测结果
     * @return \~Chinese 成功返回新animal_face检测结果，错误返回null
     *
     * \~English resize animal info
     *
     * @param scale  \~English  scale
     * @param result \~English animal face info
     * @return \~English  return animal detect result
     */
    public static native STMobileAnimalResult animalResize(float scale, STMobileAnimalResult result);

    /**
     * \~Chinese 添加子模型.
     *
     * @param modelPath \~Chinese 模型文件的路径. 后添加的会覆盖之前添加的同类子模型。加载模型耗时较长, 建议在初始化创建句柄时就加载模型
     * @return \~Chinese 成功返回0，错误返回其他，参考STUtils.ResultCode
     *
     * \~English add sub model.
     *
     * @param modelPath \~English model path
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int addSubModel(String modelPath);


    /**
     * \~Chinese 添加子模型.
     *
     * @param buffer \~Chinese 模型文件buffer. 后添加的会覆盖之前添加的同类子模型。加载模型耗时较长, 建议在初始化创建句柄时就加载模型
     * @param len    \~English 模型文件buffer长度
     * @return \~Chinese 成功返回0，错误返回其他，参考STUtils.ResultCode
     *
     * \~English add sub model from buffer.
     *
     * @param buffer \~English model buffer
     * @param len    \~English model buffer length
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int addSubModelFromBuffer(byte[] buffer, int len);


    /**
     * \~Chinese 从资源文件夹添加子模型.
     *
     * @param assetModelpath \~Chinese 模型文件的路径. 后添加的会覆盖之前添加的同类子模型。加载模型耗时较长, 建议在初始化创建句柄时就加载模型
     * @param assetManager   \~Chinese 资源文件管理器
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     *
     * \~English Add sub model from asset file
     *
     * @param assetModelpath \~English asset model path
     * @param assetManager   \~English asset manager
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int addSubModelFromAssetFile(String assetModelpath, AssetManager assetManager);
}
