package com.softsugar.stmobile;

import android.content.res.AssetManager;

import com.softsugar.stmobile.params.STBlendShapeFactory;

public class STMobilePhonemeNative {

    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    //供JNI使用，应用不需要关注
    @SuppressWarnings("unused")
    private long nativePhonemeHandle;

    /**
     * \~Chinese 创建音素句柄
     *
     * @param phonemeFilePath \~Chinese 音素查找表
     * @return \~Chinese 成功返回ST_OK, 失败返回其他错误码, 错误码定义在st_mobile_common.h中,如ST_E_FAIL等
     */
    public native int createInstance(String phonemeFilePath, int type);

    /**
     * \~Chinese 创建音素句柄
     * @param phonemeFilePath \~Chinese 音素查找表
     * @param assetManager \~Chinese 资源文件管理器
     * @return \~Chinese 成功返回ST_OK, 失败返回其他错误码, 错误码定义在st_mobile_common.h中,如ST_E_FAIL等
     */
    public native int createInstanceFromAssetFile(String phonemeFilePath, int type, AssetManager assetManager);

    /**
     * \~Chinese 根据mode输出对应的bs值，默认全脸bs值. 注意不要在正在处理语句时更改mode值，会导致bs值不稳定
     * @param region \~Chinese 输出不同部位bs值，如 ST_BS_MOUTH | ST_BS_EYE 输出嘴部和眼部的有效bs值，其他部位值为-1．
     * @return \~Chinese 成功返回ST_OK, 失败返回其他错误码, 错误码定义在st_mobile_common.h中,如ST_E_FAIL等
     */
    public native int setRegion(int region);

    /**
     * \~Chinese 根据音素获取一组bs系数
     *
     * @param phoneme \~Chinese 输入音素, 比如"a", "a1"等音素
     * @return \~Chinese 返回一组bs系数
     */
    public native STBlendShapeFactory getBlendShapeFactorByPhoneme(String phoneme);

    /**
     * \~Chinese 输入音素buffer, sdk内部需要根据输入的音素buffer做预处理
     */
    public native int phonemeInput(byte[] p_phoneme_buffer, int length);

    /**
     * \~Chinese 获取当前时间戳的bs系数, 上层在传入时间戳时需要保持获取的bs跟音频播放同步
     * @param time \~Chinese 当前播放音频的时间戳, 单位秒(s)
     * @param outParam \~Chinese 输出bs系数
     * @return \~Chinese 成功返回ST_OK, 失败返回其他错误码, 错误码定义在st_mobile_common.h中,如ST_E_FAIL等
     */
    public native int getCurrentBs(float time, STBlendShapeFactory outParam);

    /**
     * \~Chinese 重置, 清除bs缓存. 当传入不同语句时, 建议调用reset
     */
    public native int reset();

    /**
     * \~Chinese 销毁音素句柄
     *
     * @return \~Chinese 成功返回ST_OK, 失败返回其他错误码, 错误码定义在st_mobile_common.h中,如ST_E_FAIL等
     */
    public native int destroy();

}
