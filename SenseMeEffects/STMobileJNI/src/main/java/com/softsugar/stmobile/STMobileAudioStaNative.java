package com.softsugar.stmobile;

import android.content.res.AssetManager;

import com.softsugar.stmobile.params.STAudio;
import com.softsugar.stmobile.params.STBlendShapeFactory;

public class STMobileAudioStaNative {

    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    //供JNI使用，应用不需要关注
    private long nativeAudioStaHandle;

    /**
     * \~Chinese 创建的音频驱动句柄
     * @param zipPath \~Chinese ppg模型路径，包含ppg AB两个模型, 3个参数文件, 中音文音素等文件
     * @param ppgZipPath \~Chinese 卡通查找表或STA算法模型
     * @param language \~Chinese 语言模式(0:英文 1:中文)
     * @return \~Chinese 成功返回ST_OK, 失败返回其他错误码, 错误码定义在st_mobile_common.h中, 如ST_E_FAIL等
     */
    public native int createInstance(String zipPath, String ppgZipPath, int language);

    /**
     * \~Chinese 创建的音频驱动句柄
     * @param zipPath \~Chinese ppg模型路径，包含ppg AB两个模型, 3个参数文件, 中音文音素等文件
     * @param ppgZipPath \~Chinese 卡通查找表或STA算法模型
     * @param language \~Chinese 语言模式(0:英文 1:中文)
     * @param assetManager \~Chinese 资源管理器
     * @return \~Chinese 成功返回ST_OK, 失败返回其他错误码, 错误码定义在st_mobile_common.h中, 如ST_E_FAIL等
     */
    public native int createInstanceFromAssetFile(String zipPath, String ppgZipPath, int language, AssetManager assetManager);

    /**
     * \~Chinese 输入音频 备注：支持两种场景，1） 独立的音频输入 2）TTS返回的分段音频输入
     * @param audio \~Chinese 音频数据
     * @return \~Chinese 成功返回ST_OK, 失败返回其他错误码, 错误码定义在st_mobile_common.h中, 如ST_E_FAIL等
     */
    public synchronized native int audioStaInput(STAudio audio);

    /**
     * \~Chinese 按时间戳获取bs系数
     * @param time \~Chinese 时间戳(单位是秒)
     * @param outParam \~Chinese 输出bs系数
     * @return \~Chinese 成功返回ST_OK, 失败返回其他错误码, 错误码定义在st_mobile_common.h中, 如ST_E_FAIL等
     */
    public native int getCurrentBs(int sentenceId, float time, STBlendShapeFactory outParam);

    /**
     * \~Chinese 重置接口，停止内部当前语句处理，清空缓存
     */
    public synchronized native int reset();

    /**
     * \~Chinese 销毁STA句柄
     * @return \~Chinese 成功返回ST_OK, 失败返回其他错误码, 错误码定义在st_mobile_common.h中, 如ST_E_FAIL等
     */
    public native int destroy();

    private Listener mListener;

    /**
     * \~Chinese BS状态获取回调，供JNI使用，应用不需要关注
     */
    private void audioStaFunc(boolean status) {
        if (mListener != null) {
            mListener.audioStaFuncCallback(status);
        }
    }

    public interface Listener {
        void audioStaFuncCallback(boolean status);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }
}
