package com.softsugar.stmobile;

import com.softsugar.stmobile.model.STImage;

public class STMobileGreenScreenSegmentNative {

    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }
    private long GreenScreenSegmentNativeHandle;

    /**
     * \~Chinese 创建实例
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int createInstance();

    /**
     * \~Chinese 处理绿幕分割（需在gl线程使用）
     * @param imageObj  \~Chinese 输入图像
     * @param outputTexture \~Chinese 输出绿幕分割结果
     * @return \~Chinese 成功返回0，错误返回其他，参考STUtils.ResultCode
     */
    public native int process(STImage imageObj, int outputTexture);

    /**
     * \~Chinese 释放handle
     */
    public native int destroyInstance();

    /**
     * @param type  \~Chinese 要设置参数的类型
     * @param value \~Chinese 设置的值
     * @return \~Chinese 成功返回0，错误返回其他，参考STUtils.ResultCode
     */
    public native int setParam(int type, float value);

    /**
     * @param type  \~Chinese 参数关键字,和setparam对应
     * @param type \~Chinese 参数取值
     * @return \~Chinese 成功返回0，错误返回其他，参考STUtils.ResultCode
     */
    public native float getParam(int type);
}
