package com.softsugar.stmobile;

public class STCommonNative {

    /**
     * \~Chinese 加载libs \~English Load library
     */
    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    // create时指定的config, 需根据使用场景来使用, 预览使用ST_MOBILE_DETECT_MODE_PREVIEW, 离线视频处理使用ST_MOBILE_DETECT_MODE_VIDEO_POST_PROCESS, 图片使用ST_MOBILE_DETECT_MODE_IMAGE
    public final static int ST_MOBILE_TRACKING_MULTI_THREAD = 0x00000000;   ///< \~Chinese 多线程,功耗较多,卡顿较少 \~English multi thread
    public final static int ST_MOBILE_TRACKING_SINGLE_THREAD = 0x00010000;  ///< \~Chinese 单线程,功耗较少,对于性能弱的手机,会偶尔有卡顿现象 \~English single thread
    /// 检测模式
    public final static int ST_MOBILE_DETECT_MODE_VIDEO = 0x00020000;              ///< \~Chinese 旧版本视频检测,效果同预览检测,后续会更新为预览检测 \~English mode video
    public final static int ST_MOBILE_DETECT_MODE_PREVIEW = 0x00020000;            ///< \~Chinese 预览检测 \~English mode preview
    public final static int ST_MOBILE_DETECT_MODE_IMAGE = 0x00040000;              ///< \~Chinese 图片检测 与预览检测互斥,只能同时使用一个 \~English mode image
    public final static int ST_MOBILE_DETECT_MODE_VIDEO_POST_PROCESS = 0x00100000; ///< \~Chinese 视频后处理检测,与预览、图片检测互斥,只能同时使用一个 \~English mode video post process
    public final static int ST_MOBILE_ENABLE_INPUT_CUSTOM = 0x00080000;            ///< \~Chinese 使用用户自定义的结果作为输入,必须和视频/图片模式混用,目前使用人脸结果有效 \~English enable input custom

    // mesh部位
    public static final int HEAD_MESH_CONFIG_FACE = 0b0000001;    ///< \~Chinese 人脸部位 \~English head mesh config face
    public static final int HEAD_MESH_CONFIG_EYES = 0b0000010;    ///< \~Chinese 眼睛部位 \~English head mesh config eyes
    public static final int HEAD_MESH_CONFIG_MOUTH = 0b0000100;   ///< \~Chinese 嘴巴部位 \~English head mesh config mouth
    public static final int HEAD_MESH_CONFIG_SKULL = 0b0001000;   ///< \~Chinese 后脑勺部位 \~English head mesh config skull
    public static final int HEAD_MESH_CONFIG_EAR = 0b0010000;     ///< \~Chinese 耳朵部位 \~English head mesh config ear
    public static final int HEAD_MESH_CONFIG_NECK = 0b0100000;    ///< \~Chinese 脖子部位 \~English head mesh config neck
    public static final int HEAD_MESH_CONFIG_EYEBROW = 0b1000000; ///< \~Chinese 眉毛部位 \~English head mesh config eyebrow

    public final static int MESH_CONFIG = HEAD_MESH_CONFIG_FACE | HEAD_MESH_CONFIG_EYES |
                                            HEAD_MESH_CONFIG_MOUTH | HEAD_MESH_CONFIG_SKULL |
                                            HEAD_MESH_CONFIG_EAR | HEAD_MESH_CONFIG_EYEBROW;

    // 图像每个像素类型定义
    public final static int ST_PIX_FMT_GRAY8 = 0;    ///< \~Chinese Y    1        8bpp ( 单通道8bit灰度像素 ) \~English Y    1        8bpp
    public final static int ST_PIX_FMT_YUV420P = 1;  ///< \~Chinese YUV  4:2:0   12bpp ( 3通道, 一个亮度通道, 另两个为U分量和V分量通道, 所有通道都是连续的 ) \~English YUV  4:2:0   12bpp
    public final static int ST_PIX_FMT_NV12 = 2;     ///< \~Chinese YUV  4:2:0   12bpp ( 2通道, 一个通道是连续的亮度通道, 另一通道为UV分量交错 ) \~English YUV  4:2:0   12bpp
    public final static int ST_PIX_FMT_NV21 = 3;     ///< \~Chinese YUV  4:2:0   12bpp ( 2通道, 一个通道是连续的亮度通道, 另一通道为VU分量交错 ) \~English YUV  4:2:0   12bpp
    public final static int ST_PIX_FMT_BGRA8888 = 4; ///< \~Chinese BGRA 8:8:8:8 32bpp ( 4通道32bit BGRA 像素 ) \~English BGRA 8:8:8:8  32bpp
    public final static int ST_PIX_FMT_BGR888 = 5;   ///< \~Chinese BGR  8:8:8   24bpp ( 3通道24bit BGR 像素 ) \~English BGR  8:8:8   24bpp
    public final static int ST_PIX_FMT_RGBA8888 = 6; ///< \~Chinese RGRA 8:8:8:8 32bpp ( 4通道32bit RGBA 像素 ) \~English RGRA 8:8:8:8 32bpp
    public final static int ST_PIX_FMT_RGB888 = 7;   ///< \~Chinese RGB  8:8:8   24bpp ( 3通道24bit RGB 像素 ) \~English RGB  8:8:8   24bpp
    public final static int ST_PIX_FMT_FLOAT = 8;    ///< \~Chinese Y    1       32bpp ( 1通道 32bit float 像素) \~English Y    1       32bpp

    /**
     * \~Chinese 进行颜色格式转换, 不建议使用关于YUV420P的转换,速度较慢
     *
     * @param inputImage   \~Chinese 用于待转换的图像数据
     * @param outputImage  \~Chinese 转换后的图像数据
     * @param width        \~Chinese 用于转换的图像的宽度(以像素为单位)
     * @param height       \~Chinese 用于转换的图像的高度(以像素为单位)
     * @param type         \~Chinese 需要转换的颜色格式,参考st_mobile_common.h中的st_color_convert_type
     * @return 成功返回0，错误返回其他，参考STUtils.ResultCode
     *
     * \~English image color convert
     *
     * @param inputImage   \~English input image buffer
     * @param outputImage  \~English output image buffer
     * @param width        \~English width
     * @param height       \~English height
     * @param type         \~English type, refer to enum st_color_convert_type in st_mobile_common.h
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public static native int stColorConvert(byte[] inputImage, byte[] outputImage, int width, int height, int type);

    /**
     * \~Chinese 进行图片旋转
     *
     * @param inputImage   \~Chinese 待旋转的图像数据
     * @param outputImage  \~Chinese 旋转后的图像数据
     * @param width        \~Chinese 用于旋转的图像的宽度(以像素为单位)
     * @param height       \~Chinese 用于旋转的图像的高度(以像素为单位)
     * @param format       \~Chinese 用于旋转的图像的类型
     * @param rotation     \~Chinese 需要旋转的角度，当旋转角度为90度或270度时，交换width和height后，按照新的宽高读取outputImage数据
     * @return \~Chinese 成功返回0，错误返回其他，参考STUtils.ResultCode
     *
     * \~Chinese image rotate
     *
     * @param inputImage   \~English input image buffer
     * @param outputImage  \~English output image buffer
     * @param width        \~English width
     * @param height       \~English height
     * @param format       \~English image format
     * @param rotation     \~English rotation
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public static native int stImageRotate(byte[] inputImage, byte[] outputImage, int width, int height, int format, int rotation);

    /**
     * \~Chinese 设置眨眼动作的阈值,置信度为[0,1], 默认阈值为0.5
     * @param threshold    \~Chinese 阈值
     *
     * \~English set eye blink threshold,range is [0,1], default is 0.5
     * @param threshold    \~English threshold
     */
    public native void setEyeblinkThreshold(float threshold);

    /**
     * \~Chinese 设置张嘴动作的阈值,置信度为[0,1], 默认阈值为0.5
     * @param threshold     \~Chinese 阈值
     *
     * \~English set mouth ah threshold,range is [0,1], default is 0.5
     * @param threshold    \~English threshold
     */
    public native void setMouthahThreshold(float threshold);

    /**
     * \~Chinese 设置张嘴动作的阈值,置信度为[0,1], 默认阈值为0.5
     * @param threshold     \~Chinese 阈值
     *
     * \~English set head yaw threshold,range is [0,1], default is 0.5
     * @param threshold    \~English threshold
     */
    public native void setHeadyawThreshold(float threshold);

    /**
     * \~Chinese 设置张嘴动作的阈值,置信度为[0,1], 默认阈值为0.5
     * @param threshold     \~Chinese 阈值
     *
     * \~English set head pitch threshold,range is [0,1], default is 0.5
     * @param threshold    \~English threshold
     */
    public native void setHeadpitchThreshold(float threshold);

    /**
     * \~Chinese 设置张嘴动作的阈值,置信度为[0,1], 默认阈值为0.5
     * @param threshold     \~Chinese 阈值
     *
     * \~English set brow jump threshold, range is [0,1], default is 0.5
     * @param threshold    \~English threshold
     */
    public native void setBrowjumpThreshold(float threshold);

    /**
     * \~Chinese 设置人脸106点平滑的阈值. 若不设置, 使用默认值. 默认值0.8, 建议取值范围：[0.0, 1.0]. 阈值越大, 去抖动效果越好, 跟踪延时越大
     * @param threshold    \~Chinese  阈值
     *
     * \~English set smooth threshold,range is [0,1], default is 0.5
     * @param threshold    \~English threshold
     */
    public native void setSmoothThreshold(float threshold);

    /**
     * \~Chinese 设置人脸三维旋转角度去抖动的阈值. 若不设置, 使用默认值. 默认值0.5, 建议取值范围：[0.0, 1.0]. 阈值越大, 去抖动效果越好, 跟踪延时越大
     * @param threshold     \~Chinese 阈值
     *
     * \~English set head pose threshold,range is [0,1], default is 0.5
     * @param threshold    \~English threshold
     */
    public native void setHeadposeThreshold(float threshold);

    /**
     * \~Chinese 从gpu拷贝图像数据到cpu
     * @param x      \~Chinese x轴起始地址
     * @param y      \~Chinese y轴起始地址
     * @param width  \~Chinese 拷贝数据宽度
     * @param height \~Chinese 拷贝数据高度
     * @param format \~Chinese GL图像格式，例如：GLES30.GL_RGBA
     * @param type   \~Chinese GL数据格式，例如：GLES30.GL_UNSIGNED_BYTE
     *
     * \~English Copy image date from GPU to CPU
     * @param x      \~English start x
     * @param y      \~English start y
     * @param width  \~English image width
     * @param height \~English image height
     * @param format \~English GL image format，for example GLES30.GL_RGBA
     * @param type   \~English GL data type，for example GLES30.GL_UNSIGNED_BYTE
     **/
    public static native void glReadPixels(int x, int y, int width, int height, int format, int type);

    /**
     * \~Chinese 设置st_mobile当前的log层级，层级关系为自底向上的包含关系，如ST_LOG_ERROR包含所有其他可log的level。将当前的log层级设置为ST_LOG_DISABLE将禁用所有log。该接口保证线程安全。
     * @param logLevel \~Chinese 将设置的log层级 取值参考 STLogLevel.java
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set log level
     * @param logLevel \~English log level, refer to STLogLevel.java
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     **/
    public static native int setLogLevel(int logLevel);

    /**
     * \~Chinese 获取sdk版本号
     * @return \~Chinese sdk版本号
     *
     * \~English get sdk version info
     * @return \~English sdk version info
     */
    public static native String getVersion();

    /**
     * 获取当前的log层级
     */
    public static native int getLogLevel();

    /**
     * 将log重定向到文件中，如果传入的文件路径为空，则重置为输出到标准设备流
     * @param filePath 重定向log文件的全路径
     * @param trancFile 是否清除文件内容，true - 清除文件内容，false - 不清除文件内容
     * @return 成功返回0，错误返回其他，参考STUtils.ResultCode
     */
    public static native int redirectLogToFile(String filePath, boolean trancFile);

    /**
     * \~Chinese 设置env
     * @param systemPath \~Chinese systemPath
     *
     * \~English Set env
     * @param systemPath \~Chinese systemPath
     **/
    public static native void setEnv(String systemPath);
}
