package com.softsugar.stmobile;

import android.content.Context;

/**
 *  \~Chinese Licence验证JNI定义 \~English Licence authorize utils
 */

public class STMobileAuthentificationNative {

    /**
     * \~Chinese 加载libs \~English Load library
     */
    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    /**
     * \~Chinese 根据授权文件生成激活码, 在使用新的license文件时使用
     *
     * @param context       \~Chinese 上下文环境
     * @param licensePath   \~Chinese license文件路径
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Generate active code
     *
     * @param context     \~English app context
     * @param licensePath \~English license file path
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public static native String generateActiveCode(Context context, String licensePath);

    /**
     * \~Chinese 检查激活码, 必须在所有接口之前调用
     *
     * @param context        \~Chinese 上下文环境
     * @param licensePath    \~Chinese license文件路径
     * @param activationCode \~Chinese 当前设备的激活码
     * @param codeSize       \~Chinese 激活码长度
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Check active code
     *
     * @param context        \~English app context
     * @param licensePath    \~English license file path
     * @param activationCode \~English active code
     * @param codeSize       \~English active code size
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public static native int checkActiveCode(Context context, String licensePath, String activationCode, int codeSize);


    /**
     * \~Chinese 根据授权文件缓存生成激活码, 在使用新的license文件时调用
     *
     * @param context       \~Chinese 上下文环境
     * @param licenseBuffer \~Chinese license文件缓存
     * @param licenseSize   \~Chinese license文件缓存大小
     * @return \~Chinese 返回当前设备的激活码
     *
     * \~English Generate active code from buffer
     *
     * @param context       \~English app context
     * @param licenseBuffer \~English license buffer
     * @param licenseSize   \~English license buffer size
     * @return \~English active code
     */
    public static native String generateActiveCodeFromBuffer(Context context, String licenseBuffer, int licenseSize);

    /**
     * \~Chinese 检查激活码, 必须在所有接口之前调用
     *
     * @param context        \~Chinese 上下文环境
     * @param licenseBuffer  \~Chinese license文件缓存
     * @param licenseSize    \~Chinese license文件缓存大小
     * @param activationCode \~Chinese 当前设备的激活码
     * @param codeSize       \~Chinese 当前设备的激活码大小
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Check active code from buffer
     *
     * @param context        \~English app context
     * @param licenseBuffer  \~English license buffer
     * @param licenseSize    \~English license buffer size
     * @param activationCode \~English active code
     * @param codeSize       \~English  active code size
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public static native int checkActiveCodeFromBuffer(Context context, String licenseBuffer, int licenseSize, String activationCode, int codeSize);
    /**
     * \~Chinese 根据授权文件生成激活码, 在使用新的license文件时使用
     *
     * @param context       \~Chinese 上下文环境
     * @param licensePath   \~Chinese license文件路径
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Generate active code online
     *
     * @param context       \~English app context
     * @param licensePath   \~English license file path
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public static native String generateActiveCodeOnline(Context context, String licensePath);

    /**
     * \~Chinese 根据授权文件缓存生成激活码, 在使用新的license文件时调用
     *
     * @param context       \~Chinese 上下文环境
     * @param licenseBuffer \~Chinese license文件缓存
     * @param licenseSize   \~Chinese license文件缓存大小
     * @return \~Chinese 返回当前设备的激活码
     *
     * \~English Generate active code from buffer online
     *
     * @param context       \~English app context
     * @param licenseBuffer \~English license buffer
     * @param licenseSize   \~English license buffer size
     * @return \~English active code
     */
    public static native String generateActiveCodeFromBufferOnline(Context context, String licenseBuffer, int licenseSize);

    /**
     * \~Chinese 根据授权文件缓存生成激活码, 在使用新的license文件时调用
     *
     * @param licensePath        \~Chinese license文件路径
     * @param activationCodePath \~Chinese 激活码路径
     * @param value              \~Chinese 过期时间
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Get expired time from activate code
     *
     * @param licensePath        \~English license path
     * @param activationCodePath \~English activation code path
     * @param value              \~English expired time
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public static native int getExpiredTimeFromActivateCode(String licensePath, String activationCodePath, long[] value);

    /**
     * \~Chinese 根据授权文件缓存生成激活码, 在使用新的license文件时调用
     *
     * @param licenseBuffer  \~Chinese license文件缓存
     * @param licenseSize    \~Chinese license文件缓存大小
     * @param activationCode \~Chinese 激活码
     * @param codeSize       \~Chinese 激活码大小
     * @param value          \~Chinese 过期时间
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Get expired time from activate code from buffer
     *
     * @param licenseBuffer  \~English license buffer
     * @param licenseSize    \~English license buffer size
     * @param activationCode \~English activation code
     * @param codeSize       \~English activation code size
     * @param value          \~English expired time
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public static native int getExpiredTimeFromActivateCodeFromBuffer(String licenseBuffer, int licenseSize, String activationCode, int codeSize, long[] value);

    /**
     * \~Chinese 根据授权文件缓存生成激活码, 在使用新的license文件时调用
     *
     * @param licensePath        \~Chinese license文件路径
     * @param activationCodePath \~Chinese 激活码路径
     * @param startValue         \~Chinese 开始时间
     * @param endValue           \~Chinese 过期时间
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Get license expired time
     *
     * @param licensePath        \~English license path
     * @param activationCodePath \~English activation code path
     * @param startValue         \~English start time
     * @param endValue           \~English expired time
     * @return \~English return ST_OK or 0 is success, error refer to STResultCode
     */
    public static native int getLicenseExpiredTime(String licensePath, String activationCodePath, int[] startValue, long[] endValue);

    /**
     * \~Chinese 根据授权文件缓存生成激活码, 在使用新的license文件时调用
     *
     * @param licenseBuffer  \~Chinese license文件缓存
     * @param licenseSize    \~Chinese license文件缓存大小
     * @param activationCode \~Chinese 激活码
     * @param codeSize       \~Chinese 激活码大小
     * @param startValue     \~Chinese 开始时间
     * @param endValue       \~Chinese 过期时间
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Get license expired time from buffer
     *
     * @param licenseBuffer  \~English license buffer
     * @param licenseSize    \~English license buffer size
     * @param activationCode \~English activation code
     * @param codeSize       \~English activation code size
     * @param startValue     \~English start time
     * @param endValue       \~English expired time
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public static native int getLicenseExpiredTimeFromBuffer(String licenseBuffer, int licenseSize, String activationCode, int codeSize, long[] startValue, long[] endValue);


}
