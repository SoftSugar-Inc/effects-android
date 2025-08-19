package com.softsugar.stmobile;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.softsugar.stmobile.model.STEffect3DBeautyPartInfo;
import com.softsugar.stmobile.model.STEffectBeautyInfo;
import com.softsugar.stmobile.model.STEffectModuleInfo;
import com.softsugar.stmobile.model.STEffectPackageInfo;
import com.softsugar.stmobile.model.STEffectRenderInParam;
import com.softsugar.stmobile.model.STEffectRenderOutParam;
import com.softsugar.stmobile.model.STEffectTexture;
import com.softsugar.stmobile.model.STEffectTryonInfo;
import com.softsugar.stmobile.model.STFaceMeshList;
import com.softsugar.stmobile.model.STGanReturn;
import com.softsugar.stmobile.model.STImage;
import com.softsugar.stmobile.model.STQuaternion;
import com.softsugar.stmobile.model.STSafeRegion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class STMobileEffectNative {

    /**
     * \~Chinese 加载libs \~English Load library
     */
    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    /**
     * \~Chinese 供JNI使用，应用不需要关注 \~English JNI Using
     */
    private long nativeEffectHandle;
    private STSoundPlay mSoundPlay;
    public static final int EFFECT_CONFIG_NONE = 0x0;        ///< \~Chinese 默认模式，输入连续序列帧时使用，比如视频或预览模式 \~English Default mode, used when inputting consecutive frames, such as video or preview mode
    public static final int EFFECT_CONFIG_IMAGE_MODE = 0x2;  ///< \~Chinese 图像模式，输入为静态图像或单帧图像 \~English Image mode, input is image or single frame image

    public static String getFilePath(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('/');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot + 1);
            }
        }
        return filename;
    }

    /**
     * * \~Chinese 创建特效句柄
     * @param context  \~Chinese 上下文
     * @param config  \~Chinese 素材生效模式
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * * \~English create effect handle
     * @param context \~English app context
     * @param config  \~English effect config, include preview/image/video
     * @return return ST_OK or 0 is success, error refer to STResultCode
     */
    public int createInstance(Context context, int config) {
        if (context != null) {
            mSoundPlay = STSoundPlay.getInstance(context);
        }

        String path = "";
        int ret = createInstanceNative(config, path);

        if (0 == ret && mSoundPlay != null) {
            mSoundPlay.setEffectHandle(this);
        }

        return ret;
    }

    /**
     * \~Chinese 按照名字查找lib路径
     *
     * @param context  \~Chinese 应用上下文
     * @param libName  \~Chinese 要查找的lib名字
     * @return \~Chinese 要查找的lib路径
     *
     *  \~English Find lib path by name
     *
     * @param context  \~English app context
     * @param libName  \~English lib name
     * @return \~English the lib path
     */
    public static String findLibrary(Context context, String libName) {
        String result = null;
        ClassLoader classLoader = (context.getClassLoader());
        if (classLoader != null) {
            try {
                Method findLibraryMethod = classLoader.getClass().getMethod("findLibrary", new Class<?>[] { String.class });
                if (findLibraryMethod != null) {
                    Object objPath = findLibraryMethod.invoke(classLoader, new Object[] { libName });
                    if (objPath != null && objPath instanceof String) {
                        result = (String) objPath;
                    }
                }
            } catch (NoSuchMethodException e) {
                Log.e("findLibrary1", e.toString());
            } catch (IllegalAccessException e) {
                Log.e("findLibrary1", e.toString());
            } catch (IllegalArgumentException e) {
                Log.e("findLibrary1", e.toString());
            } catch (InvocationTargetException e) {
                Log.e("findLibrary1", e.toString());
            } catch (Exception e) {
                Log.e("findLibrary1", e.toString());
            }
        }

        return result;
    }

    /**
     * \~Chinese 创建底层特效实例
     *
     * @param config  \~Chinese 素材生效模式
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     * \~Chinese 创建底层特效实例
     *
     * @param config  \~English effect config, include preview/image/video
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    private native int createInstanceNative(int config, String path);

    /**
     * \~Chinese 释放实例 \~English release handle
     */
    public void destroyInstance() {
        destroyInstanceNative();
        if (mSoundPlay != null) {
            mSoundPlay.release();
            mSoundPlay = null;
        }
    }

    /**
     * \~Chinese 释放实例
     * @param needReleaseSoundPlay  \~Chinese 是否释放声音资源
     *
     *\~English release handle
     * @param needReleaseSoundPlay \~English if need release SoundPlay
     */
    public void destroyInstance(boolean needReleaseSoundPlay) {
        destroyInstanceNative();
        if (needReleaseSoundPlay && mSoundPlay != null) {
            mSoundPlay.release();
            mSoundPlay = null;
        }
    }

    /**
     * \~Chinese 通知声音停止函数
     *
     * @param name \~Chinese 结束播放的声音文件名（MB）,默认150MB,素材过大时,循环加载,降低内存； 贴纸较小时,全部加载,降低cpu
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English notify sound play done
     *
     * @param name \~English sound file name
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setSoundPlayDone(String name);

    /**
     * \~Chinese 释放实例
     *
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English release handle
     *
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    private native int destroyInstanceNative();

    /**
     * \~Chinese 设置特效的参数
     *
     * @param param \~Chinese 参数类型
     * @param value \~Chinese 参数数值，具体范围参考参数类型说明
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set param
     *
     * @param param \~English param
     * @param value \~English value
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setParam(int param, float value);

    /**
     * \~Chinese 设置美颜相关参数
     * @param param \~Chinese 配置项类型
     * @param value \~Chinese 配置项参数值
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set beauty param
     * @param param \~English param
     * @param value \~English value
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setBeautyParam(int param, float value);

    /**
     * \~Chinese 获取特效的参数
     *
     * @param param \~Chinese 参数类型
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     *\~English Get effect param
     *
     * @param param \~English param
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native float getParam(int param);

    /**
     * \~Chinese 设置试妆相关参数
     * @param info \~Chinese TryOn接口输入参数
     * @param type \~Chinese 参数类型
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set TryOn param
     * @param info \~English effect TryOn info
     * @param type \~English type
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setTryOnParam(STEffectTryonInfo info, int type);

    /**
     *  \~Chinese 获取试妆相关参数
     * @param tryonType \~Chinese 参数类型
     * @return \~Chinese 返回TryOn信息
     *
     *  \~English Get TryOn param
     * @param tryonType \~English tryOnType
     * @return \~English  return TryOn info
     */
    public native STEffectTryonInfo getTryOnParam(int tryonType);

    /**
     * \~Chinese 设置美颜的模式, 目前仅对磨皮和美白有效
     *
     * @param param \~Chinese 美颜类型
     * @param value \~Chinese 模式
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set the beauty mode, currently only effective for peeling and whitening
     *
     * @param param \~English param
     * @param value \~English value
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setBeautyMode(int param, int value);

    /**
     * \~Chinese 获取美颜的模式, 目前仅对磨皮和美白有效
     *
     * @param param \~Chinese 美颜类型
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Get beauty mode, only effective for peeling and whitening
     *
     * @param param \~English param
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int getBeautyMode(int param);

    /**
     * \~Chinese 添加素材包
     *
     * @param path \~Chinese 待添加的素材包文件路径
     * @return \~Chinese 成功返回package Id，错误返回其它，参考STResultCode
     *
     * \~English Add package
     *
     * @param path \~English file path
     * @return \~English  If successful, return package Id. error refer to STResultCode
     */
    public native int addPackage(String path);

    /**
     * \~Chinese 从assets资源文件添加素材包
     *
     * @param file_path \~Chinese 待添加的素材包文件路径
     * @param assetManager \~Chinese asset资源管理器
     * @return \~Chinese 成功返回package Id，错误返回其它，参考STResultCode
     *
     *  \~English Add package from asset file
     *
     * @param file_path \~English file path
     * @param assetManager\~English asset manager
     * @return \~English  If successful, return package Id. error refer to STResultCode
     */
    public native int addPackageFromAssetsFile(String file_path, AssetManager assetManager);

    /**
     * \~Chinese 从buffer添加素材包
     *
     * @param buffer \~Chinese 文件的buffer
     * @param len    \~Chinese buffer长度
     * @return \~Chinese 成功返回package Id，错误返回其它，参考STResultCode
     *
     *  \~English Add package from buffer
     *
     * @param buffer \~English file buffer
     * @param len    \~English buffer length
     * @return \~English  If successful, return package Id. error refer to STResultCode
     */
    public native int addPackageFromBuffer(byte[] buffer, int len);

    /**
     * \~Chinese 更换缓存中的素材包 (删除已有的素材包)
     *
     * @param path \~Chinese 待添加的素材包文件路径
     * @return \~Chinese 成功返回package Id，错误返回其它，参考STResultCode
     *
     * \~English Change package
     *
     * @param path \~English file path
     * @return \~English  If successful, return package Id. error refer to STResultCode
     */
    public native int changePackage(String path);

    /**
     * \~Chinese 从assets资源文件更换缓存中的素材包 (删除已有的素材包)
     *
     * @param file_path \~Chinese 待添加的素材包文件路径
     * @param assetManager \~Chinese asset资源管理器
     * @return \~Chinese 成功返回package Id，错误返回其它，参考STResultCode
     *
     * \~English Change package from asset file
     *
     * @param file_path \~English file path
     * @param assetManager \~English asset manager
     * @return \~English  If successful, return package Id. error refer to STResultCode
     */
    public native int changePackageFromAssetsFile(String file_path, AssetManager assetManager);

    /**
     * \~Chinese 从buffer更换素材包
     *
     * @param buffer \~Chinese 文件的buffer
     * @param len    \~Chinese buffer长度
     * @return \~Chinese 成功返回package Id，错误返回其它，参考STResultCode
     *
     *  \~English Change package from buffer
     *
     * @param buffer \~English file buffer
     * @param len    \~English buffer length
     * @return \~English  If successful, return package Id. error refer to STResultCode
     */
    public native int changePackageFromBuffer(byte[] buffer, int len);

    /**
     * \~Chinese 删除指定素材包
     * @param id \~Chinese 待删除的素材包ID
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English remove effect by id
     *
     * @param id \~English effect id
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int removeEffect(int id);

    /**
     * \~Chinese 清空所有素材包 \~English cleat all
     */
    public native void clear();

    /**
     * \~Chinese 获取素材信息
     *
     * @param packageId \~Chinese 素材包ID
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Get package info
     *
     * @param packageId \~English package Id
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native STEffectPackageInfo getPackageInfo(int packageId);

    /**
     * \~Chinese 获取素材的贴纸信息
     * @param packageId   \~Chinese 素材包ID
     * @param moduleCount \~Chinese 贴纸信息地址能容纳的贴纸的数量
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Get all module info in package
     *
     * @param packageId   \~English package Id
     * @param moduleCount \~English module count
     * @return \~English return ST_OK or 0 is success, error refer to STResultCode
     */
    public native STEffectModuleInfo[] getModulesInPackage(int packageId, int moduleCount);

    /**
     * \~Chinese 获取贴纸信息
     *
     * @param moduleId \~Chinese 贴纸ID
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~Chinese 获取贴纸信息
     *
     * @param moduleId \~English module Id
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native STEffectModuleInfo getModuleInfo(int moduleId);

    /**
     * \~Chinese 获取覆盖生效的美颜的数量, 仅在添加，更改，移除素材之后调用
     *
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Get info count of overlapped beauty, and call it only after adding, changing, and removing the material
     *
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int getOverlappedBeautyCount();

    /**
     * \~Chinese 获取覆盖生效的美颜的信息, 仅在添加，更改，移除素材之后调用
     *
     * @param beautyCount \~Chinese 起始地址可以容纳美颜信息的数量
     *
     * \~English Get info of overlapped beauty, and call it only after adding, changing, and removing the material
     *
     * @param beautyCount \~English beauty info count
     */
    public native STEffectBeautyInfo[] getOverlappedBeauty(int beautyCount);

    /**
     * \~Chinese 设置美颜的强度
     *
     * @param type  \~Chinese 美颜类型
     * @param value \~Chinese 强度
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set beauty strength
     *
     * @param type  \~English type
     * @param value \~English value
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setBeautyStrength(int type, float value);

    /**
     * \~Chinese 获取美颜的强度
     *
     * @param type \~Chinese 美颜类型
     * @return  \~Chinese 强度
     *
     *\~English Get beauty strength
     *
     * @param type \~English beauty type
     * @return  \~English strength
     */
    public native float getBeautyStrength(int type);

    /**
     * \~Chinese 加载美颜素材
     *
     * @param param \~Chinese 美颜类型 取值参考 STEffectBeautyType.java
     * @param path  \~Chinese 待添加的素材文件路径
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set beauty package
     *
     * @param param \~English beauty param
     * @param path  \~English file path
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setBeauty(int param, String path);

    /**
     * \~Chinese 加载美颜素材
     *
     * @param param        \~Chinese 美颜类型
     * @param file_path    \~Chinese 待添加的素材文件路径
     * @param assetManager \~Chinese asset资源管理器
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     *  \~English Set beauty package form asset file
     *
     * @param param        \~English beauty param
     * @param file_path    \~English file path
     * @param assetManager \~English asset manager
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setBeautyFromAssetsFile(int param, String file_path, AssetManager assetManager);

    /**
     * \~Chinese 从buffer加载美颜素材
     *
     * @param param   \~Chinese 美颜类型
     * @param buffer  \~Chinese 待添加的素材文件buffer
     * @param len     \~Chinese buffer长度
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     *  \~English Set beauty package form buffer
     *
     * @param param   \~English beauty param
     * @param buffer  \~English file buffer
     * @param len     \~English buffer length
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setBeautyFromBuffer(int param, byte[] buffer, int len);

    /**
     * \~Chinese 获取需要的检测配置选项
     * @return \~Chinese 返回config
     *
     * \~English Get STHumanAction detect config
     * @return \~English  return config info
     */
    public native long getHumanActionDetectConfig();

    public native long getHumanTriggerActions(long config);

    /**
     * \~Chinese 获取目前需要的动物检测类型
     * @return \~Chinese 返回config
     *
     * \~English Get animal detect config
     * @return \~English  return config info
     */
    public native long getAnimalDetectConfig();

    /**
     * \~Chinese 获取自定义配置
     * @return \~Chinese 返回config
     *
     *  \~English Get custom param config
     * @return \~English  return config info
     */
    public native long getCustomParamConfig();

    /**
     * \~Chinese 特效渲染, 必须在OpenGL渲染线程中执行
     *
     * @param inputParams           \~Chinese 输入的渲染信息
     * @param outputParams          \~Chinese 输出的渲染信息
     * @param needOutputHumanAction \~Chinese是否输出特效处理之后的检测结果
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Effects render, must be executed in the OpenGL thread
     *
     * @param inputParams           \~English Input rendering information
     * @param outputParams          \~English Output rendering information
     * @param needOutputHumanAction \~English if need output STHumanAction info
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int render(STEffectRenderInParam inputParams, STEffectRenderOutParam outputParams, boolean needOutputHumanAction);

    /**
     * \~Chinese 重新播放素材
     *
     * @param packageId \~Chinese 贴纸id
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Replay the material
     *
     * @param packageId \~English package Id
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int replayPackage(int packageId);

    /**
     * \~Chinese 设置贴纸素材包内部美颜组合的强度，强度范围[0.0, 1.0]
     *
     * @param packageId   \~Chinese 素材包id
     * @param beautyGroup \~Chinese 美颜组合类型，目前只支持设置美妆、滤镜组合的强度
     * @param strength    \~Chinese 强度值
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set the strength of the beauty combination inside the sticker material package, with a strength range of [0.0, 1.0]
     *
     * @param packageId   \~English package id
     * @param beautyGroup \~English beauty group params
     * @param strength    \~English strength
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setPackageBeautyGroupStrength(int packageId, int beautyGroup, float strength);

    /**
     * \~Chinese 释放内部缓存的资源，目前主要是GL相关的渲染资源，需要在GL context中调用
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Release effect resources. Currently, it is mainly GL related rendering resources, which need to be called in GL context
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int releaseCachedResource();

    /**
     * \~Chinese 在调用st_mobile_effect_set_beauty函数加载了3D微整形素材包之后调用。获取到素材包中所有的blendshape名称、index和当前强度[0, 1]
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Obtain all blendshape names, indices, and current strengths in the material package [0,1]
     * \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native STEffect3DBeautyPartInfo[] get3DBeautyParts();

    /**
     * \~Chinese 在调用st_mobile_effect_set_beauty函数加载了3D微整形素材包之后调用。在获取blendshape数组之后，可以依据起信息修改权重[0, 1]，设置给渲染引擎产生效果
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~Chinese \~English After obtaining the blendshape array, the weight [0,1] can be modified based on the initial information and set to produce an effect on the rendering engine
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int set3dBeautyPartsStrength(STEffect3DBeautyPartInfo[] effect3DBeautyPartInfo, int length);

    /**
     * \~Chinese 用于输入STHumanAction的face mesh list信息
     *
     * @param faceMeshList \~Chinese 从STHumanAction中获取的faceMeshList信息
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set face mesh list information
     *
     * @param faceMeshList \~English FaceMeshList information obtained from STHumanAction
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setFaceMeshList(STFaceMeshList faceMeshList);

    /**
     * \~Chinese 获取需要的自定义事件选项
     * @return \~Chinese 自定义事件选项
     *
     *  \~English get the required custom event id
     * @return \~English custom event id
     */
    public native int getCustomEventNeeded();

    /**
     * \~Chinese 获取前摄/后摄对应的默认手机姿态四元数，在处理图片、视频或者没有相应的手机姿态的情况下，需要传入默认的camera_quat
     * @param frontCamera \~Chinese 是否为前置摄像头
     * @return \~Chinese 四元数
     *
     * \~English get the default phone posture quaternion corresponding to forward/backward camera
     * @param frontCamera \~English Is front camera
     * @return \~English Quaternion
     */
    public native STQuaternion getDefaultCameraQuaternion(boolean frontCamera);

    /**
     * \~Chinese 改变素材包背景信息
     * @param stickerId  \~Chinese 素材包ID
     * @param image      \~Chinese 背景图片
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~Chinese 改变素材包背景信息 \~English Change the background of the package
     * @param stickerId   \~English package Id
     * @param image       \~English background image
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int changeBg(int stickerId, STImage image);

    /**
     * \~Chinese 改变素材包背景信息
     * @param stickerId  \~Chinese 素材包ID
     * @param st         \~Chinese 背景图片信息(纹理)
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Change the background of the package
     * @param stickerId  \~English package Id
     * @param st         \~English background image(texture)
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int changeBg2(int stickerId, STEffectTexture st);

    /**
     * \~Chinese 改变素材包背景信息
     * @param packageState  \~Chinese 素材包状态
     * @param packageId     \~Chinese 素材包id
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~Chinese 改变素材包背景信息 \~English Package state change called
     * @param packageState  \~English package state
     * @param packageId     \~English package Id
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public void packageStateChangeCalledByJni(int packageState, int packageId) {
        if (null != mListener) {
            mListener.packageStateChange(packageState, packageId);
        }
    }

    /**
     * \~Chinese 绿幕分割颜色设置
     * @param color  \~Chinese 颜色设置
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Green segment called
     * @param color \~English input color
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public void greenSegmentCalledByJni(int color) {
        if (null != mListener) {
            mListener.greenSegment(color);
        }
    }

    private Listener mListener;

    /**
     * \~Chinese 设置素材包播放状态listener
     * @param listener  \~Chinese 素材包播放状态listener
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode \~English
     *
     * \~English Set package state listener
     * @param listener \~English package state listener
     * @return  \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    /**
     * \~Chinese 素材包播放状态listener \~English package state listener
     */
    public interface Listener {
        /**
         * \~Chinese 素材包的播放状态
         * @param packageState  \~Chinese 素材包状态，参见STEffectPackageState
         * @param packageId \~Chinese 素材包Id
         *
         * \~English package state change
         * @param packageState \~English package state, refer to STEffectPackageState
         * @param packageId    \~English package Id
         */
        void packageStateChange(int packageState, int packageId);

        /**
         * \~Chinese 设置绿幕分割颜色RGB值,默认为绿色,将颜色值（16进制数0xFFFFFF,按R、G、B排列
         * @param color \~Chinese 色值
         *
         * \~English Set RGB value for green screen segmentation color, default to green(0xFFFFFF, arranged in R, G, B order)
         * @param color \~English color
         */
        void greenSegment(int color);
    }

    /**
     * \~Chinese 设置素材包module信息
     * @param ganReturn  \~Chinese gan图像信息
     * @param moduleInfo \~Chinese 素材包module信息
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set the module information of the package
     * @param ganReturn   \~English image of gan return
     * @param moduleInfo  \~English module info
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setEffectModuleInfo(STGanReturn ganReturn, STEffectModuleInfo moduleInfo);

    /**
     * \~Chinese 设置绿幕分割安全区，安全区中的像素将不进行绿幕分割，暴露原图像素
     * @param num         \~Chinese 安全区数组长度
     * @param safeRegions \~Chinese 安全区数组，传入空指针意味着清除所有安全区
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set the green screen segmentation safety zone. The pixels in the safety zone will not undergo green screen segmentation and expose the original pixel.
     * @param num         \~English image of gan return
     * @param safeRegions \~English module info
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setGreenMaskSafeRegions(int num, STSafeRegion[] safeRegions);
}
