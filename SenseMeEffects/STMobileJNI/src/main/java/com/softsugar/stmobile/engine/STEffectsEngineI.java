package com.softsugar.stmobile.engine;

import android.content.Context;
import android.hardware.SensorEvent;

import com.softsugar.stmobile.engine.glutils.STEffectsInput;
import com.softsugar.stmobile.engine.glutils.STEffectsOutput;
import com.softsugar.stmobile.model.STEffect3DBeautyPartInfo;
import com.softsugar.stmobile.model.STEffectBeautyInfo;
import com.softsugar.stmobile.model.STEffectModuleInfo;
import com.softsugar.stmobile.model.STEffectTexture;
import com.softsugar.stmobile.model.STEffectTryonInfo;
import com.softsugar.stmobile.model.STFaceExtraInfo;
import com.softsugar.stmobile.model.STGanReturn;
import com.softsugar.stmobile.model.STImage;
import com.softsugar.stmobile.model.STRect;

/** @noinspection UnusedReturnValue*/
@SuppressWarnings("unused")
public interface STEffectsEngineI {

    /**
     * 鉴权
     * @param context context
     * @param licBuffer license
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    boolean checkLicenseFromBuffer(Context context, byte[] licBuffer);

    /**
     * 初始化 sdk
     * @param context context
     * @param renderMode 渲染模式，参见STRenderMode
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int init(Context context, int renderMode);

    /**
     * 加载单个检测模型
     *
     * @param modelPath 模型文件在SD卡下绝对路径
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int loadSubModel(String modelPath);

    /**
     * 加载单个检测模型
     * @param modelBuffer 模型文件
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int loadSubModelFromBuffer(byte[] modelBuffer);

    /**
     * 加载单个动物检测模型
     * @param modelPath 动物检测模型绝对路径
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int loadAnimalSubModel(String modelPath);

    /**
     * 加载单个动物检测模型
     * @param modelBuffer 动物检测模型
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int loadAnimalSubModelFromBuffer(byte[] modelBuffer);

    /**
     * 添加素材包
     *
     * @param path 待添加的素材包文件路径
     * @return 成功返回素材包ID，错误返回其它，参考STResultCode
     */
    int addPackage(String path);

    /**
     * 切换风格妆素材包
     * @param oldStyleId 已有的风格妆ID
     * @param path 风格妆素材包文件路径
     * @return 成功返回素材包ID，错误返回其它，参考STResultCode
     */
    int changeStyle(int oldStyleId, String path);

    /**
     * 添加素材包
     * @param buffer 待添加的素材包文件
     * @return 成功返回素材包ID，错误返回其它，参考STResultCode
     */
    int addPackageFromBuffer(byte[] buffer);

    /**
     * 删除指定素材包
     *
     * @param id 待删除的素材包ID
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int removeEffectById(int id);

    /**
     * 清空所有素材包
     */
    void clearAll();

    /**
     * 设置贴纸素材包内部美颜组合的强度，强度范围[0.0, 1.0]
     *
     * @param packageId  素材包id
     * @param beautyGroup 美颜组合类型，目前只支持设置美妆、滤镜组合的强度
     * @param strength 强度值
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setPackageBeautyGroupStrength(int packageId, int beautyGroup, float strength);

    /**
     * 设置美颜的模式, 目前仅对磨皮和美白有效
     *
     * @param param 美颜类型 参考 STEffectBeautyType.java
     * @param value 模式 磨皮类型参考 STSmoothMode.java 美白类型参考 STEffectWhitenMode.java
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setBeautyMode(int param, int value);

    /**
     * 加载美颜素材
     *
     * @param param 美颜类型
     * @param path  待添加的素材文件路径
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setBeauty(int param, String path);

    /**
     * 加载美颜素材
     * @param param 美颜类型
     * @param path 待添加的素材文件路径
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setBeautyFromSDPath(int param, String path);

    /**
     * 加载美颜素材
     * @param param 美颜类型
     * @param buffer 待添加的素材文件
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setBeautyFromBuffer(int param, byte[] buffer);

    /**
     * 设置特效参数
     * @param param param
     * @param value value
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setEffectParams(int param, float value);

    /**
     * 设置检测参数
     * @param type type
     * @param value value
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setDetectParams(int type, float value);

    /**
     * 设置美颜的强度
     *
     * @param type  美颜类型
     * @param value 强度
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setBeautyStrength(int type, float value);

    /**
     * 设置美颜相关参数
     * @param type 配置项类型
     * @param value 配置项参数值
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setBeautyParam(int type, float value);

    /**
     * 在调用st_mobile_effect_set_beauty函数加载了3D微整形素材包之后调用。获取到素材包中所有的blendshape名称、index和当前强度[0, 1]
     *
     * @return 成功返回info，错误返回null，参考STResultCode
     */
    STEffect3DBeautyPartInfo[] get3DBeautyParts();

    /**
     * 在调用st_mobile_effect_set_beauty函数加载了3D微整形素材包之后调用。在获取blendshape数组之后，可以依据起信息修改权重[0, 1]，设置给渲染引擎产生效果。
     *
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int set3dBeautyPartsStrength(STEffect3DBeautyPartInfo[] effect3DBeautyPartInfo, int length);

    /**
     * 设置试妆相关参数
     * @param info TryOn接口输入参数
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setTryOnParam(STEffectTryonInfo info, int type);

    /**
     * 获取试妆相关参数
     */
    STEffectTryonInfo getTryOnParam(int tryOnType);

    /**
     * 特效渲染，必须在OpenGL渲染线程中执行（处理texture方式）
     *
     * @param inputParams  输入渲染信息
     * @param outputParams 输出渲染信息
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int processTexture(STEffectsInput inputParams, STEffectsOutput outputParams);

    /**
     * 特效渲染，必须在OpenGL渲染线程中执行（处理buffer方式）
     * @param inputParams  输入渲染信息
     * @param outputParams 输出渲染信息
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int processBuffer(STEffectsInput inputParams, STEffectsOutput outputParams);

    /**
     * 释放资源
     */
    void release();

    /**
     * 设置跟踪目标的矩形
     * @param rect        设置需要跟踪区域矩形框
     * @return 成功返回0，错误返回其他，参考STResultCode
     */
    int setObjectTarget(STRect rect);

    /**
     * 对连续视频帧中的目标进行实时快速跟踪
     * @param score       输出目标区域的置信度
     * @return 输出指定的目标矩形框,输出实际跟踪的矩形框.目前只能跟踪2^n正方形矩形
     */
    STRect objectTrack(float[] score);

    /**
     * Replay 素材包
     * @param packageId 素材包ID
     */
    void replayPackageById(int packageId);

    /**
     * 重置，清除所有缓存信息
     */
    void resetDetect();

    /**
     * 获取覆盖生效的美颜的信息, 仅在添加，更改，移除素材之后调用
     */
    STEffectBeautyInfo[] getOverlappedBeauty();

    /**
     * 获取human检测config
     */
    long getHumanDetectConfig();

    /**
     * 新增检测项
     * @param detectConfig 示例：STMobileHumanActionNative.ST_MOBILE_DETECT_EXTRA_FACE_POINTS
     */
    void addHumanDetectConfig(long detectConfig);

    /**
     * 删除检测项
     * @param detectConfig 示例：STMobileHumanActionNative.ST_MOBILE_DETECT_EXTRA_FACE_POINTS
     */
    void removeHumanDetectConfig(long detectConfig);

    /**
     * 获取特定检测config对应的触发Action，目前主要是手势检测存在不同Action
     */
    long getHumanTriggerActions();

    /**
     * 获取Animal检测config
     */
    long getAnimalDetectConfig();

    /**
     * 获取自定义事件
     */
    int getCustomEvent();

    /**
     * 设置绿幕分割
     */
    int changeBg(int stickerId, STImage image);

    /**
     * 设置绿幕分割
     */
    int changeBg2(int stickerId, STEffectTexture st);

    /**
     * 输出原图 buffer
     */
    void enableOutputImageBuffer(boolean enable);

    /**
     * 输出渲染后 buffer
     */
    void enableOutputEffectImageBuffer(boolean enable);

    /**
     * 3d Mesh
     */
    void setFaceMeshList();

    /**
     * 是否输出检测结果
     */
    void enableOutputHumanAction(boolean enable);

    int setEffectModuleInfo(STGanReturn ganReturn, STEffectModuleInfo moduleInfo);

    /**
     * IS Me
     * @param tag isMe开关
     */
    void openIsMe(boolean tag);

    /**
     * 获取当前检测的人脸信息
     */
    STFaceExtraInfo getCurrentFaceInfo();

    void setSensorEvent(SensorEvent event);
    void setCustomEvent(int customEvent);
    void setShowOrigin(boolean showOrigin);
}
