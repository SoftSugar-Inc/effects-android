package com.softsugar.stmobile;

import com.softsugar.stmobile.model.STRect;

/**
 * Created by softsugar on 17-6-5.
 */

public class STMobileObjectTrackNative {

    //供JNI使用，应用不需要关注
    private long objectTrackNativeHandle;

    /**
     * \~Chinese 加载libs \~English Load library
     */
    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    /**
     * \~Chinese 创建通用物体跟踪句柄
     * @return \~Chinese 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English create handle
     * @return \~English 成功返回0，错误返回其他，参考STResultCode
     */
    public native int createInstance();

    /**
     * \~Chinese 设置跟踪目标的矩形
     *
     * @param inputImage  \~Chinese 输入图像数据数组
     * @param inFormat    \~Chinese 输入图片的类型,支持NV21,BGR,BGRA,NV12,RGB等,比如STCommon.ST_PIX_FMT_BGRA8888
     * @param imageWidth  \~Chinese 输入图像的宽度(以像素为单位)
     * @param imageHeight \~Chinese 输入图像的高度(以像素为单位)
     * @param rect        \~Chinese 设置需要跟踪区域矩形框
     * @return \~Chinese 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English Set object track target
     *
     * @param inputImage  \~English input image data
     * @param inFormat    \~English input image format, support NV21,BGR,BGRA,NV12,RGBA
     * @param imageWidth  \~English image width
     * @param imageHeight \~English image height
     * @param rect        \~English target rect
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setTarget(byte[] inputImage, int inFormat, int imageWidth, int imageHeight, STRect rect);

    /**
     * \~Chinese 对连续视频帧中的目标进行实时快速跟踪
     *
     * @param inputImage  \~Chinese 输入图像数据数组
     * @param inFormat    \~Chinese 输入图片的类型,支持NV21,BGR,BGRA,NV12,RGB等,比如STCommon.ST_PIX_FMT_BGRA8888
     * @param imageWidth  \~Chinese 输入图像的宽度(以像素为单位)
     * @param imageHeight \~Chinese 输入图像的高度(以像素为单位)
     * @param score       \~Chinese 输出目标区域的置信度
     * @return \~Chinese 输出指定的目标矩形框,输出实际跟踪的矩形框.目前只能跟踪2^n正方形矩形
     *
     * \~English Track target object
     *
     * @param inputImage  \~English input image data
     * @param inFormat    \~English input image format, support NV21,BGR,BGRA,NV12,RGBA
     * @param imageWidth  \~English image width
     * @param imageHeight \~English image height
     * @param score       \~English score
     * @return \~English output target rect.only support 2^n pixel numbers
     */
    public native STRect objectTrack(byte[] inputImage, int inFormat, int imageWidth, int imageHeight, float[] score);

    /**
     * \~Chinese 重置通用物体跟踪句柄. 清空缓存,删除跟踪目标. \~English reset handle
     */
    public native void reset();

    /**
     * \~Chinese 销毁实例 \~English release handle
     */
    public native void destroyInstance();
}
