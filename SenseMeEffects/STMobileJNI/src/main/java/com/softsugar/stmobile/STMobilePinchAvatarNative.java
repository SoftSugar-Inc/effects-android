package com.softsugar.stmobile;

import android.content.res.AssetManager;

import com.softsugar.stmobile.model.STAnimationTarget;
import com.softsugar.stmobile.model.STBoneTransform;
import com.softsugar.stmobile.model.STColor;
import com.softsugar.stmobile.model.STHumanAction;
import com.softsugar.stmobile.model.STPCController;
import com.softsugar.stmobile.model.STTransform;
import com.softsugar.stmobile.params.STGenAvatarInParam;
import com.softsugar.stmobile.params.STGenAvatarOutParam;

public class STMobilePinchAvatarNative {

    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    //供底层使用，不需要关注
    private long nativeAvatarHandle;


    public static final int ST_MOBILE_GENAVATAR_NONE = 0x0;
    public static final int ST_MOBILE_GENAVATAR_ENABLE_RENDER = 0x1;
    /**
     * \~Chinese 创建avatar句柄
     *
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int createInstance(int config);

//    /**
//     * 从资源文件夹创建avatar句柄
//     *
//     * @param assetModelpath 输入的素材包路径
//     * @param assetManager 资源文件管理器
//     * @return 成功返回0，错误返回其他，参考STCommon.ResultCode
//     */
//    public native int createInstanceFromAssetFile(String assetModelpath, AssetManager assetManager);

    /**
     * \~Chinese 销毁avatar句柄
     *
     */
    public native void destroyInstance();

    /**
     * \~Chinese 加载avatar初始素材包
     *
     * @param packagePath \~Chinese avatar素材包文件路径
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int loadBasePackage(String packagePath, int[] packageId);

    /**
     * \~Chinese 从资源文件夹加载avatar初始素材包
     *
     * @param packagePath \~Chinese avatar素材包文件路径
     * @param assetManager \~Chinese 资源文件管理器
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int loadBasePackageFromAssetFile(String packagePath, AssetManager assetManager, int[] packageId);

    /**
     * \~Chinese 设置avatar模型是否可见（渲染）
     *
     * @param visiable \~Chinese avatar模型是否可见
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setAvatarVisiable(boolean visiable);

    /**
     * \~Chinese 对Avatar脸部骨骼自动变形，捏出接近的人脸
     *
     * @param imageWidth \~Chinese 图像的宽度
     * @param imageHeight \~Chinese 图像的高度
     * @param rotate \~Chinese 图像中人脸的方向,例如STRotateType.ST_CLOCKWISE_ROTATE_0
     * @param humanAction \~Chinese 检测到的人脸结果
     * @param isMale \~Chinese 是否为男性
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int autoPinchFace(int imageWidth, int imageHeight, int rotate, STHumanAction humanAction, boolean isMale);

    /**
     * \~Chinese 获取自动捏脸检测到的feature id，需要在autoPinchFace之后调用
     *
     * @param featureIdx \~Chinese feature类型索引
     * @return \~Chinese 返回检测到的feature id
     */
    public native int getFaceFeatures(int featureIdx);

    /**
     * \~Chinese 更新手动捏脸controller信息
     *
     * @param controllers \~Chinese bone controller数组
     * @param count\~Chinese  bone controller数组大小
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int updateBoneControllerInfo(STPCController[] controllers, int count);

//    /**
//     * 加载预先捏好的avatar骨骼配置文件，在更新鼻子、嘴、眼睛、脸型时调用
//     *
//     * @param path 骨骼配置文件路径
//     * @return 成功返回0，错误返回其他，参考STCommon.ResultCode
//     */
//    public native int loadBoneTransformConfig(String path);

//    /**
//     * 从资源文件夹加载预先捏好的avatar骨骼配置文件，在更新鼻子、嘴、眼睛、脸型时调用
//     *
//     * @param assetPath 骨骼配置文件路径
//     * @param assetManager 资源文件管理器
//     * @return 成功返回0，错误返回其他，参考STCommon.ResultCode
//     */
//    public native int loadBoneTransformConfigFromAssetFile(String assetPath, AssetManager assetManager);

    /**
     * \~Chinese 设置avatar皮肤颜色
     *
     * @param color \~Chinese 设置的颜色值
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int changeSkinColor(STColor color);

    /**
     * \~Chinese 从文件加载一个属性模型，在添加头发、眼镜、衣服、裤子、鞋时调用
     *
     * @param featurePath \~Chinese 属性模型文件路径
     * @param featureId \~Chinese 返回属性模型的id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int addFeature(String featurePath, int[] featureId);

    /**
     * \~Chinese 设置属性模型的可见性
     *
     * @param featureId \~Chinese 属性模型的id
     * @param visiable \~Chinese 属性模型是否可见
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setFeatureVisiable(int featureId, boolean visiable);

    /**
     * \~Chinese 从资源文件夹加载一个属性模型
     *
     * @param featurePath \~Chinese 属性模型文件路径
     * @param assetManager \~Chinese 资源文件管理器
     * @param featureId \~Chinese 返回的属性模型的id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int addFeatureFromAssetFile(String featurePath, AssetManager assetManager, int[] featureId);

    /**
     * \~Chinese 设置属性模型的颜色
     *
     * @param featureId \~Chinese 属性模型的id
     * @param color \~Chinese 设置的颜色值
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int changeFeatureColor(int featureId, STColor color);

    /**
     * \~Chinese 移除一个属性模型
     *
     * @param featureId \~Chinese 属性模型的id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int removeFeature(int featureId);

    /**
     * \~Chinese 删除所有属性模型
     *
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int clearFeatures();

    /**
     * \~Chinese 添加一个美妆贴图，在添加眉毛、胡子、眼影时调用
     *
     * @param makeUpPath \~Chinese 美妆贴图文件路径
     * @param makeUpId \~Chinese 返回美妆贴图的id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int addMakeUp(String makeUpPath, int[] makeUpId);

    /**
     * \~Chinese 从资源文件夹添加一个美妆贴图
     *
     * @param makeUpPath \~Chinese 美妆贴图文件路径
     * @param assetManager \~Chinese 资源文件管理器
     * @param makeUpId \~Chinese 返回美妆贴图的id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int addMakeUpFromAssetFile(String makeUpPath, AssetManager assetManager, int[] makeUpId);

    /**
     * \~Chinese 设置美妆贴图的颜色
     *
     * @param makeupId \~Chinese 美妆贴图的id
     * @param color \~Chinese 设置的颜色值
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int changeMakeUpColor(int makeupId, STColor color);

    /**
     * \~Chinese 移除一个美妆贴图
     *
     * @param makeUpId \~Chinese 属性模型的id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int removeMakeUp(int makeUpId);

    /**
     * \~Chinese 删除所有美妆贴图
     *
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int clearMakeUps();

    /**
     * \~Chinese 设置Avatar模型当前朝向视点，在切换初始视点、手动捏脸视点、侧身视点等时使用
     *
     * @param faceMode \~Chinese Avatar模型当前朝向视点，参考STAvatarParams
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setFacingMode(int faceMode);

    /**
     * \~Chinese 旋转Avatar模型
     *
     * @param rotateAngle \~Chinese 旋转角度，此角度是一个增量
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int rotate(float rotateAngle);

    /**
     * \~Chinese 获取Avatar模型当前朝向视点
     *
     * @return \~Chinese 返回Avatar模型当前朝向视点，参考STAvatarParams
     */
    public native int getFacingMode();

    /**
     * \~Chinese 导出当前avatar捏脸配置（骨骼参数，属性信息等）到文件
     *
     * @param exportJsonPath \~Chinese 导出配置文件路径
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int exportPinchConfig(String exportJsonPath);

    /**
     * \~Chinese 获取当前avatar捏脸配置（骨骼参数，属性信息等）导出到buffer需要的字节数
     *
     * @param bufferLength \~Chinese 返回导出到buffer需要的字节数
     * @param dataFromat \~Chinese 配置文件的格式，目前支持导出json、zip两种文件格式
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int getPinchConfigBufferLength(int[] bufferLength, int dataFromat);

    /**
     * \~Chinese 导出当前avatar捏脸配置（骨骼参数，属性信息等）到buffer
     *
     * @param outputBuffer \~Chinese 导出的buffer
     * @param dataFromat \~Chinese 配置文件的格式，目前支持导出json、zip两种文件格式
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int exportPinchConfigToBuffer(byte[] outputBuffer, int dataFromat);

    /**
     * \~Chinese 从文件加载avatar捏脸配置的骨骼信息
     *
     * @param configPath \~Chinese 配置文件路径
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int loadPinchConfig(String configPath);

    /**
     * \~Chinese 从资源文件夹加载avatar捏脸配置的骨骼信息
     *
     * @param path \~Chinese 配置文件路径
     * @param assetManager \~Chinese 资源文件管理器
     * @param dataFromat \~Chinese 配置文件的格式，目前支持导出json、zip两种文件格式
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */

    public native int loadPinchConfigFromAsset(String path, AssetManager assetManager, int dataFromat);

    /**
     * \~Chinese 从内存加载avatar捏脸配置的骨骼信息
     *
     * @param buffer \~Chinese 配置文件buffer
     * @param length \~Chinese buffer长度
     * @param dataFromat \~Chinese 配置文件的格式，目前支持导出json、zip两种文件格式
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int loadPinchConfigFromBuffer(byte[] buffer, int length, int dataFromat);

    /**
     * \~Chinese 从文件加载avatar骨骼动画
     *
     * @param clipPath \~Chinese 骨骼动画文件路径
     * @param clipId \~Chinese 返回的骨骼动画的id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int loadAnimationClip(String clipPath, int[] clipId);

    /**
     * \~Chinese 内存卸载avatar骨骼动画
     *
     * @param clipId \~Chinese 返回的骨骼动画的id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int unloadAnimationClip(int clipId);

    /**
     * \~Chinese 从资源文件夹加载avatar骨骼动画
     *
     * @param clipPath \~Chinese 骨骼动画文件路径
     * @param assetManager \~Chinese 资源文件管理器
     * @param clipId \~Chinese 返回的骨骼动画的id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int loadAnimationClipFromAssetsFile(String clipPath, AssetManager assetManager, int[] clipId);

    /**
     * \~Chinese 切换到目标avatar骨骼动画
     *
     * @param targets \~Chinese 需要播放的动画序列
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int playAnimationStack(STAnimationTarget[] targets);

    /**
     * \~Chinese 获取当前avatar已经加载的骨骼动画数量
     *
     * @return \~Chinese 返回已经加载的骨骼动画数量
     */
    public native int getAnimationClipCount();

    /**
     * \~Chinese 切换到目标avatar骨骼动画
     *
     * @param clipId \~Chinese 骨骼动画的id
     * @param transitSec \~Chinese 过渡时长
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int changeAnimation(int clipId, float transitSec);

    /**
     * \~Chinese 停止Avatar的骨骼动画
     *
     */
    public native void stopAnimation();

    /**
     * \~Chinese 设置avatar当前的显示模式，目前支持捏脸编辑、脸部实时跟踪、肢体实时跟踪三个模式，其中捏脸编辑显示全身
     *
     * @param displayMode \~Chinese avatar显示模式，参考STAvatarParams
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setDisplayMode(int displayMode);

    /**
     * \~Chinese 获取avatar当前的显示模式
     *
     * @return \~Chinese 返回当前的Avatar显示模式
     */
    public native int getDisplayMode();

    /**
     * \~Chinese 得到Avatar捏脸之后的纹理
     * @param humanActionHandle \~Chinese 人脸检测句柄，用于获取肢体四元数
     * @param humanActionResult \~Chinese 人脸结果句柄
     * @param textureInput \~Chinese 输入纹理
     * @param imageWidth \~Chinese 图像的宽
     * @param imageHeight \~Chinese 图像的高
     * @param rotate \~Chinese 图像中人脸的方向,例如STRotateType.ST_CLOCKWISE_ROTATE_0
     * @param textureOutput \~Chinese 输出纹理
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int processTexture2(long humanActionHandle, long humanActionResult, int textureInput, int imageWidth, int imageHeight, int rotate, int textureOutput);

    /**
     * \~Chinese 将Avatar渲染到输入的目标纹理上
     * @param humanActionNative \~Chinese 人脸检测句柄，用于获取肢体四元数
     * @param textureInput \~Chinese 输入纹理
     * @param imageWidth \~Chinese 图像的宽
     * @param imageHeight \~Chinese 图像的高
     * @param rotate \~Chinese 图像中人脸的方向,例如STRotateType.ST_CLOCKWISE_ROTATE_0
     * @param humanAction \~Chinese 检测到的人脸结果
     * @param textureOutput \~Chinese 输出纹理
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int processTexture(STMobileHumanActionNative humanActionNative, int textureInput, int imageWidth, int imageHeight, int rotate, STHumanAction humanAction, int textureOutput);

    /**
     * \~Chinese 得到Avatar捏脸之后的纹理
     */
    public native int processTextureExtended(STGenAvatarInParam inParam, STGenAvatarOutParam outParam);

    /**
     * \~Chinese 在肢体驱动时没有人体的情况下用来重置Avatar模型
     *
     */
    public native void resetBodyPose();

    /**
     * \~Chinese 获取avatar捏脸需要的检测配置
     *
     * @return \~Chinese 返回捏脸需要的检测配置
     */
    public native long getDetectConfig();

    /**
     * \~Chinese 设置avatar背景图片, 仅支持RGBA图像格式
     *
     * @param imageData \~Chinese 图像数据
     * @param backgroundType \~Chinese 背景类型
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setBackgroundFromBuffer(byte[] imageData, int backgroundType, int[] backgroudnId);

    /**
     * \~Chinese 设置avatar背景颜色
     *
     * @param color \~Chinese 设置的北京颜色
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setBackgroundColor(STColor color);

    /**
     * \~Chinese 通过文件路径设置avatar背景
     *
     * @param path \~Chinese 图片文件路径
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setBackgroundFromPath(String path, int[] backgroudnId);

    /**
     * \~Chinese 删除指定avatar背景
     *
     * @param backgroudnId \~Chinese 图片文件路径
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int removeBackground(int backgroudnId);

    /**
     * \~Chinese 设置摄像机参数
     *
     * @param position \~Chinese 摄像机位置
     * @param target \~Chinese 摄像机观察位置
     * @param up \~Chinese 摄像机局部坐标系向上方向
     * @param transitSec \~Chinese 过渡时长
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setCameraLookAt(float[] position, float[] target, float[] up, float transitSec);

    /**
     * \~Chinese 设置透视投影摄像机参数
     *
     * @param fov \~Chinese 摄像机fov，单位为度
     * @param aspect \~Chinese 摄像机投影面宽高比
     * @param znear \~Chinese 摄像机近平面
     * @param zfar \~Chinese 摄像机远平面
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setCameraPerspective(float fov, float aspect, float znear, float zfar);

    /**
     * \~Chinese 设置正交投影摄像机参数
     *
     * @param znear \~Chinese 摄像机近平面
     * @param zfar \~Chinese 摄像机远平面
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setCameraOrthogonal(float left, float right, float bottom, float top, float znear, float zfar);


    /**
     * \~Chinese 设置是否锁定人脸对齐使用的相机（不受lookat、perspective接口调用的影响）
     *
     * @param lock \~Chinese 是否锁定人脸对齐使用的相机
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int lockFaceFittingCamera(boolean lock);

    /**
     * \~Chinese 设置是否锁定人脸对齐使用的相机（不受lookat、perspective接口调用的影响）
     *
     * @param facePoseType \~Chinese ST_RESET_FACE_POSE_CURRENT 则将头部的正向姿态设为当前姿态，为 ST_RESET_FACE_POSE_FRONT 则为初始姿态
     * @return v成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int resetFacePose(int facePoseType);

    public native int addAvatar(String avatarPath, int[] avatarId);

    public native int addAvatarFromBuffer(String assetModelpath, AssetManager assetManager, int[] avatarId);

    public native int removeAvatar(int packageId);

    public native int rotateAvatar(float rotateAngle);

    public native int translateAvatar(float[] translate);

    public native int scaleAvatar(float[] scale);

    public native int activeAvatar(int avatarId);

    public native int getActivedAvatarId(int[] avatarId);

    /**
     * \~Chinese 克隆指定avatar，生产新的avatar，克隆会包括feature与makeup
     *
     * @param srcAvatarId \~Chinese 已存在的、要被克隆的avatar模型id;
     * @param avatarId \~Chinese 返回的avatar模型id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int cloneAvatar(int srcAvatarId, int[] avatarId);

    /**
     * \~Chinese 设置指定属性模型的显示与否
     *
     * @param featureId \~Chinese 属性模型的id
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int freezeFeatureDynamicBone(int featureId);

    public native int loadGenfaceConfig(String modelPath, String configPath, String pinchModelPath);

    public native int loadGenfaceConfigFromBuffer(AssetManager assetManager, String modelPath, String configPath, String pinchModelPath);

    public native int genFace(byte[] data, int format, int imageWidth, int imageHeight, int rotate, STHumanAction humanAction, boolean isMale, int avatarId);

    public native int resetGenFace(int avatarId);

    public native int unloadGenfaceConfig();

    /**
     * \~Chinese Avatar肢体驱动时的fitting系数，将在从图像计算的pose的基础上再叠加接口传入的系数。一般在减小或者增大跟随幅度时使用
     *
     * @param part \~Chinese 要设置缩放因子的Avatar身体部位
     * @param scale \~Chinese 缩放因子，应该大于0
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setFittingScale(int part, float scale);

    /**
     * \~Chinese 是否在肢体驱动模式下开启头部影响上半身摆动的IK效果
     *
     * @param enable \~Chinese true - 启用效果，false - 禁用效果
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int setUpbodyIkEnabled(boolean enable);


    public native int getEyebrowType();

    /**
     * \~Chinese 添加asset素材包
     * @param path \~Chinese 素材包的资源路经
     * @param assetId \~Chinese 返回的输入素材ID
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int addAsset(String path, int[] assetId);

    /**
     * \~Chinese 添加asset素材包
     * @param path \~Chinese 素材包的资源路经
     * @param assetManager \~Chinese 资源文件管理器
     * @param assetId \~Chinese 返回的输入素材ID
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int addAssetFromAssetFile(String path, AssetManager assetManager, int[] assetId);

    /**
     * \~Chinese 改变asset素材包中的素材颜色
     * @param assetId \~Chinese 已加载的asset素材ID
     * @param color \~Chinese 设置的颜色值
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int changeAssetColor(int assetId, STColor[] color, int count);

    /**
     * \~Chinese 移除asset素材包
     * @param assetId \~Chinese 已加载的asset素材ID
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int removeAsset(int assetId);

    /**
     * \~Chinese 移除所有asset素材包
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int removeAllAsset();

    public native int getFaceExpression(int width, int height, int rotate, STHumanAction humanAction, float[] values);


    public static final int AVATAR_FEATURE_IDX_COUNT = 6;
    /**
     * \~Chinese 获取人脸BlendShape数量
     *
     * @return \~Chinese 成功返回数量，错误返回其他，参考STCommon.ResultCode
     */
    public native int genPinchFaceBlendShapeCount();

    /**
     * \~Chinese 获取人脸Bone数量
     *
     * @return \~Chinese 成功返回数量，错误返回其他，参考STCommon.ResultCode
     */
    public native int genPinchFaceBoneCount();

    /**
     * \~Chinese 获取人脸自动生成结果
     *
     * @param imageWidth \~Chinese 图像宽度
     * @param imageHeight \~Chinese 图像宽度
     * @param rotate \~Chinese 图像中人脸朝向
     * @param humanAction \~Chinese 检测到的人脸数据
     * @param isMale \~Chinese 男女
     * @param boneTransformCount \~Chinese bone数量
     * @param boneTransformArray \~Chinese bone数组
     * @param blendShapeSize \~Chinese BlendShape数量
     * @param blendShapeArray \~Chinese BlendShape数组
     * @param featureIdCount \~Chinese featureId数量
     * @param featureIdArray \~Chinese featureId数组
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     */
    public native int getAutoPinchResult(int imageWidth, int imageHeight, int rotate, STHumanAction humanAction, boolean isMale,
                                         int boneTransformCount, STBoneTransform[] boneTransformArray, int blendShapeSize, float[] blendShapeArray, int featureIdCount, int[] featureIdArray);

    public native STTransform[] calcStandardPose(int width, int height, int rotate, STHumanAction humanAction, float fov);

    public native int setOption(int option, boolean val);
}
