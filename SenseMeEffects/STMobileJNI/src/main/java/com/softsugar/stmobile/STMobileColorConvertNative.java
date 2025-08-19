package com.softsugar.stmobile;


import com.softsugar.stmobile.model.STEffectTexture;

public class STMobileColorConvertNative {
    private long colorConvertNativeHandle;

    /**
     * \~Chinese 加载libs \~English Load library
     */
    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    /**
     * \~Chinese 创建实例
     * @return \~Chinese 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English create handle
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstance();

    /**
     * \~Chinese 设置color convert输入buffer的尺寸，提前调用该接口可以提升后续color convert接口的时间，需要在OpenGL Context中调用
     *
     * @param width  \~Chinese 待转换图像的宽度
     * @param height \~Chinese 待转换图像的高度
     * @return \~Chinese 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English set texture size, need OpenGL Context
     *
     * @param width  \~English width
     * @param height \~English height
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setTextureSize(int width, int height);

    /**
     * \~Chinese 对输入的nv21格式的buffer转换成RGBA格式，并输出到texId对应的OpenGL纹理中，需要在OpenGL Context中调用
     *
     * @param width       \~Chinese 待转换图像的宽度
     * @param height      \~Chinese 待转换图像的高度
     * @param orientation \~Chinese 图像朝向，根据传入图像旋转角度，将图像转正。如果旋转角度为90或270，tex_out的宽高需要与buffer的宽高对调
     * @param needMirror  \~Chinese 是否需要将输出纹理镜像
     * @param imageData   \~Chinese NV21格式的图像buffer
     * @param textureId   \~Chinese RGBA格式输出纹理，需要在调用层预先创建
     * @return \~Chinese 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English Convert nv21 buffer to RGBA texture，set texture size, need OpenGL Context
     *
     * @param width       \~English width
     * @param height      \~English height
     * @param orientation \~English orientation
     * @param needMirror  \~English need mirror
     * @param imageData   \~English image buffer
     * @param textureId   \~English texture id
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int nv21BufferToRgbaTexture(int width, int height, int orientation, boolean needMirror, byte[] imageData, int textureId);

    /**
     * \~Chinese 对输入的纹理转换为nv21格式，并输出到buffer，需要在OpenGL Context中调用
     *
     * @param textureId     \~Chinese RGBA格式输入纹理，需要在调用层预先创建
     * @param width         \~Chinese 纹理的宽度
     * @param height        \~Chinese 纹理的高度
     * @param nv21ImageData \~Chinese 输出NV21格式的图像buffer，需要在应用层分配内存
     * @return \~Chinese 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English Convert rgba texture to nv21 buffer, need OpenGL Context
     *
     * @param textureId     \~English texture id
     * @param width         \~English width
     * @param height        \~English height
     * @param nv21ImageData \~English nv21 image date
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int rgbaTextureToNv21Buffer(int textureId, int width, int height, byte[] nv21ImageData);

    /**
     * \~Chinese 对输入的nv12格式的buffer转换成RGBA格式，并输出到texId对应的OpenGL纹理中，需要在OpenGL Context中调用
     *
     * @param width       \~Chinese 待转换图像的宽度
     * @param height      \~Chinese 待转换图像的高度
     * @param orientation \~Chinese 图像朝向，根据传入图像旋转角度，将图像转正。如果旋转角度为90或270，tex_out的宽高需要与buffer的宽高对调
     * @param needMirror  \~Chinese 是否需要将输出纹理镜像
     * @param imageData   \~Chinese NV21格式的图像buffer
     * @param textureId   \~Chinese RGBA格式输出纹理，需要在调用层预先创建
     * @return \~Chinese 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English Convert nv12 buffer to rgba texture, need OpenGL Context
     *
     * @param width       \~English width
     * @param height      \~English height
     * @param orientation \~English orientation
     * @param needMirror  \~English need mirror
     * @param imageData   \~English NV21 image buffer
     * @param textureId   \~English RGBA texture
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int nv12BufferToRgbaTexture(int width, int height, int orientation, boolean needMirror, byte[] imageData, int textureId);

    /**
     * \~Chinese 对输入的纹理转换为nv12格式，并输出到buffer，需要在OpenGL Context中调用
     *
     * @param textureId     \~Chinese RGBA格式输入纹理，需要在调用层预先创建
     * @param width         \~Chinese 纹理的宽度
     * @param height        \~Chinese  纹理的高度
     * @param nv12ImageData \~Chinese 输出NV12格式的图像buffer，需要在应用层分配内存

     * @return \~Chinese 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English Convert rgba texture to nv12 buffer, need OpenGL Context
     *
     * @param textureId     \~English RGBA格式输入纹理，需要在调用层预先创建
     * @param width         \~English width
     * @param height        \~English height
     * @param nv12ImageData \~English nv21 image buffer

     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int rgbaTextureToNv12Buffer(int textureId, int width, int height, byte[] nv12ImageData);

    /**
     * \~Chinese 对输入的纹理转换为灰度格式，并输出到buffer，需要在OpenGL Context中调用
     *
     * @param textureId      \~Chinese RGBA格式输入纹理，需要在调用层预先创建
     * @param width          \~Chinese 纹理的宽度
     * @param height         \~Chinese 纹理的高度
     * @param gray8ImageData \~Chinese 输出gray8格式的图像buffer，需要在应用层分配内存

     * @return \~Chinese 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English Convert rgba texture to gray8 buffer, need OpenGL Context
     *
     * @param textureId      \~English RGBA texture
     * @param width          \~English width
     * @param height         \~English height
     * @param gray8ImageData \~English gray8 image buffer

     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int rgbaTextureToGray8Buffer(int textureId, int width, int height, byte[] gray8ImageData);

    /**
     * \~Chinese rgba纹理转nv21纹理，图像像素数量减少5/8，需要在OpenGL Context中调用
     *
     * @param rgbaTexture  \~Chinese RGBA格式输入纹理，需要在调用层预先创建
     * @param nv21Texture  \~Chinese 输出的nv21纹理
     * @return \~Chinese 成功返回0，错误返回其他，参考STResultCode
     *
     * \~English Convert rgba texture to nv21 texture, need OpenGL Context
     *
     * @param rgbaTexture  \~Chinese input RGBA texture
     * @param nv21Texture  \~Chinese output nv21 texture
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int rgbaTextureToNv21Texture(STEffectTexture rgbaTexture, STEffectTexture nv21Texture);

    /**
     * \~Chinese 销毁实例 \~English release handle
     */
    public native void destroyInstance();
}
