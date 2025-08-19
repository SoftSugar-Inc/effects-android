# SenseMe Effects v9.7 集成文档

## 1 SDK简介
### 1.1 SDK功能简介
- SDK主要检测功能包括：人脸106关键点、人脸240（282）点关键点、背景分割、手势检测等
- SDK主要特效功能包括：美颜、微整形、美妆、贴纸和滤镜等

### 1.2 SDK目录简介
- SDK打包文件主要包括sample、aar文件、STMobileJNI及源码、libst_mobile.so和头文件等
- sample作为特效展示和集成时的参考示例
- aar文件和STMobileJNI，提供SDK核心能力
- 使用c接口文件集成时参考libst_mobile.so和头文件（本文档仅限于使用java接口集成参考）


## 2 项目中导入SDK
### 2.1 使用aar文件
- 打开SenseMe_Effects SDK目录，找到其中的STMobileJNI-xxx-release.aar文件
- 拷贝其到项目中的主模块或集成模块的libs目录下，如拷贝到app/libs/目录下，没有libs文件夹可手动创建
- 打开主模块或集成模块的build.gradle文件，在android{···}中加入查询路径：

  ```java
  repositories {
          flatDir {
              dirs 'libs'
          }
  }
  ```

- 在主模块或集成模块的build.gradle的 dependencies 下加入 SDK 引用：

  ```java
  implementation(name:'STMobileJNI-xxx-release',ext:'aar')
  ```

- 将模型文件和素材文件拷贝到项目主模块或集成模块的assets文件目录备用

- 将授权文件License.lic拷贝到项目主模块或集成模块的assets文件目录备用

### 2.2 使用源码依赖
- 打开SenseMe_Effects SDK目录，找到其中的sample/STMobileJNI文件目录

- 拷贝其到项目目录下，打开项目根目录下的setting.gradle文件，添加module：

  ```java
  include ':app', ':STMobileJNI'
  ```

- 在主模块或集成模块的build.gradle的 dependencies 下加入 SDK 引用：

  ```java
  //添加STMobileJNI的依赖
  implementation project(':STMobileJNI')
  ```

- 将模型文件和素材文件拷贝到项目主模块或集成模块的assets文件目录备用

- 将授权文件License.lic拷贝到项目主模块或集成模块的assets文件目录备用
### 2.3 SDK混淆
- 使用aar或源码依赖，需在项目主模块或集成模块的“proguard-rules.pro”文件中，添加SDK的混淆：
```java
-keep class com.softsugar.stmobile.* { *;}
-keep class com.softsugar.stmobile.model.* { *;}
```

## 3 SDK的授权
- 使用License文件授权之后，才能正常使用SDK。可以使用离线授权文件，也可使用在线服务器托管的授权文件
### 3.1 离线license授权
- 将授权文件License.lic拷贝到项目主模块或集成模块的assets目录或指定文件目录
- 将Sample中的STLicenseUtils类拷贝到项目中
- 打开STLicenseUtils.java文件，修改为离线授权：
```java
//是否使用服务器License鉴权
//true：使用服务器下拉授权文件，使用离线接口生成activeCode
//false: 使用asset文件夹下的 "SenseME.lic"，"SenseME_Online.lic"生成activeCode
private static final boolean USING_SERVER_LICENSE = false;
```
- 根据需要修改license文件路径或名字：
```java
//离线generateActiveCode使用
private static final String LOCAL_LICENSE_NAME = "license/SenseME.lic";
```
- 调用静态方法checkLicense函数授权：
```java
//鉴权方式有两种，离线和在线
public static boolean checkLicense(final Context context){
    if(USING_SERVER_LICENSE){
        return checkLicenseFromServer(context);
    }else{
        return checkLicenseFromLocal(context);
    }
}
```

### 3.2 在线license授权
- 将Sample中的STLicenseUtils类拷贝到项目中
- 打开STLicenseUtils.java文件，修改为在线授权：
```java
//是否使用服务器License鉴权
//true：使用服务器下拉授权文件，使用离线接口生成activeCode
//false: 使用asset文件夹下的 "SenseME.lic"，"SenseME_Online.lic"生成activeCode
private static final boolean USING_SERVER_LICENSE = true;
```
- 调用静态方法checkLicense函数授权：
```java
//鉴权方式有两种，离线和在线
public static boolean checkLicense(final Context context){
    if(USING_SERVER_LICENSE){
        return checkLicenseFromServer(context);
    }else{
        return checkLicenseFromLocal(context);
    }
}
```
## 4 集成准备工作

### 4.1 模型文件的使用
- 模型在算法检测时使用，包括人脸检测模型、背景分割模型等，添加不同模型配合license提供不同的检测能力
- 将模型拷贝到工程assets目录或其中的指定目录，使用模型路径和名字作为索引，sample中参考FileUtils.java，以assets根目录为例：
```java
public static final String MODEL_NAME_FACE_ATTRIBUTE = "models/M_SenseME_Attribute_1.0.1.model";
public static final String MODEL_NAME_AVATAR_HELP = "models/M_SenseME_Avatar_Help_2.2.0.model";
public static final String MODEL_NAME_CATFACE_CORE = "models/M_SenseME_CatFace_3.0.0.model";
public static final String MODEL_NAME_FACE_EXTRA = "models/M_SenseME_Face_Extra_Advanced_6.0.13.model";// 282
public static final String MODEL_NAME_LIPS_PARSING = "models/M_SenseAR_Segment_MouthOcclusion_FastV1_1.1.3.model";

public static final String MODEL_NAME_ACTION = "models/M_SenseME_Face_Video_7.0.0.model";
public static final String MODEL_NAME_HAND = "models/M_SenseME_Hand_5.4.0.model";
public static final String MODEL_NAME_SEGMENT = "models/M_SenseME_Segment_4.14.1.model";
public static final String MODEL_NAME_HAIR = "models/M_SenseME_Segment_Hair_1.3.4.model";
public static final String HEAD_SEGMENT_MODEL_NAME = "models/M_SenseME_Segment_Head_1.0.3.model";

public static final String HEAD_SEGMENT_DBL = "models/M_Segment_DBL_Face_1.0.7.model";
public static final String MODEL_SEGMENT_SKY = "models/M_SenseAR_Segment_Sky_1.0.3.model";
public static final String MODEL_NAME_DOG = "models/M_SenseME_DogFace_2.0.0.model";
public static final String MODEL_SEGMENT_SKIN = "models/M_Segment_Skin_1.1.1.model";
public static final String MODEL_3DMESH = "models/M_SenseAR_3DMesh_Face_2.0.2.model";
```
- SDK支持从assets文件中加载模型和从sd卡路径加载模型。以从assets加载为例：
```java
// 根据模型路径，读取byte[]
byte[] buffer = FileUtils.readFileFromAssets(context, Constants.getModelList().get(i));
// 模型数据传入loadSubModelFromBuffer()
mSTEffectsEngine.loadSubModelFromBuffer(buffer);
```
### 4.2 素材文件的使用
- 素材文件包括贴纸素材、美妆素材和滤镜素材
- 将需要的素材文拷贝到件工程assets目录或其中的指定目录，初始化是将其拷贝到sd卡。特效接口需传入素材的真实路径。例：
```java
//切换贴纸
int packageId = stEffectsEngine.changePackage(currentStickerFilePath);

//切换美妆素材
stEffectsEngine.setBeauty(MAKEUP_TYPE, currentMakeupFilePath);

```



## 5 代码中接入特效
- 特效功能使用STEffectsEngine实现
### 5.1 特效句柄的初始化
- 特效句柄初始化
```java
int result = init(Context context, int renderMode);
```
- renderMode模式
```java
public enum STRenderMode {
    PREVIEW (0),// 预览模式
    IMAGE (1),  // 图片模式
    VIDEO (2);  // 视频模式
}
```
### 5.2 设置美颜
- 美颜基础美颜、高级美颜和微整形等对应的枚举类型如下，参见STEffectBeautyType.java：
```java
// 基础美颜 base
public static final int EFFECT_BEAUTY_BASE_WHITTEN                      = 101;  // 美白
public static final int EFFECT_BEAUTY_BASE_REDDEN                       = 102;  // 红润
public static final int EFFECT_BEAUTY_BASE_FACE_SMOOTH                  = 103;  // 磨皮

// 美形 reshape
public static final int EFFECT_BEAUTY_RESHAPE_SHRINK_FACE               = 201;  // 瘦脸
public static final int EFFECT_BEAUTY_RESHAPE_ENLARGE_EYE               = 202;  // 大眼
public static final int EFFECT_BEAUTY_RESHAPE_SHRINK_JAW                = 203;  // 小脸
public static final int EFFECT_BEAUTY_RESHAPE_NARROW_FACE               = 204;  // 窄脸
public static final int EFFECT_BEAUTY_RESHAPE_ROUND_EYE                 = 205;  // 圆眼

// 微整形 plastic
public static final int EFFECT_BEAUTY_PLASTIC_THINNER_HEAD              = 301;  // 小头
public static final int EFFECT_BEAUTY_PLASTIC_THIN_FACE                 = 302;  // 瘦脸型
public static final int EFFECT_BEAUTY_PLASTIC_CHIN_LENGTH               = 303;  // 下巴
public static final int EFFECT_BEAUTY_PLASTIC_HAIRLINE_HEIGHT           = 304;  // 额头
public static final int EFFECT_BEAUTY_PLASTIC_APPLE_MUSLE               = 305;  // 苹果肌
public static final int EFFECT_BEAUTY_PLASTIC_NARROW_NOSE               = 306;  // 瘦鼻翼
public static final int EFFECT_BEAUTY_PLASTIC_NOSE_LENGTH               = 307;  // 长鼻
public static final int EFFECT_BEAUTY_PLASTIC_PROFILE_RHINOPLASTY       = 308;  // 侧脸隆鼻
public static final int EFFECT_BEAUTY_PLASTIC_MOUTH_SIZE                = 309;  // 嘴型
public static final int EFFECT_BEAUTY_PLASTIC_PHILTRUM_LENGTH           = 310;  // 缩人中
public static final int EFFECT_BEAUTY_PLASTIC_EYE_DISTANCE              = 311;  // 眼距
public static final int EFFECT_BEAUTY_PLASTIC_EYE_ANGLE                 = 312;  // 眼睛角度
public static final int EFFECT_BEAUTY_PLASTIC_OPEN_CANTHUS              = 313;  // 开眼角
public static final int EFFECT_BEAUTY_PLASTIC_BRIGHT_EYE                = 314;  // 亮眼
public static final int EFFECT_BEAUTY_PLASTIC_REMOVE_DARK_CIRCLES       = 315;  // 祛黑眼圈
public static final int EFFECT_BEAUTY_PLASTIC_REMOVE_NASOLABIAL_FOLDS   = 316;  // 祛法令纹
public static final int EFFECT_BEAUTY_PLASTIC_WHITE_TEETH               = 317;  // 白牙
public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_CHEEKBONE          = 318;  // 瘦颧骨
public static final int EFFECT_BEAUTY_PLASTIC_OPEN_EXTERNAL_CANTHUS     = 319;  // 开外眼角比例
public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_JAWBONE            = 320;  // 瘦下颔
public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_ROUND_FACE         = 321;  // 圆脸瘦脸
public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_LONG_FACE          = 322;  // 长脸瘦脸
public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_GODDESS_FACE       = 323;  // 女神瘦脸
public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_NATURAL_FACE       = 324;  // 自然瘦脸

// 调整 tone
public static final int EFFECT_BEAUTY_TONE_CONTRAST                     = 601;  // 对比度
public static final int EFFECT_BEAUTY_TONE_SATURATION                   = 602;  // 饱和度
public static final int EFFECT_BEAUTY_TONE_SHARPEN                      = 603;  // 锐化
public static final int EFFECT_BEAUTY_TONE_CLEAR                        = 604;  // 清晰度
// 背景虚化
public static final int EFFECT_BEAUTY_TONE_BOKEH                        = 605;  // 背景虚化
```
- 设置基础美颜、高级美颜和微整形强度。以设置美白、瘦脸和瘦鼻翼强度为0.5为例
```java
//设置美白
mSTEffectsEngine.setBeautyStrength(STEffectBeautyType.EFFECT_BEAUTY_BASE_WHITTEN, 0.5f);

//设置瘦脸
mSTEffectsEngine.setBeautyStrength(STEffectBeautyType.EFFECT_BEAUTY_RESHAPE_SHRINK_FACE, 0.5f);

//设置瘦鼻翼
mSTEffectsEngine.setBeautyStrength(STEffectBeautyType.EFFECT_BEAUTY_PLASTIC_NARROW_NOSE, 0.5f);

```
- 设置美白和磨皮模式，此接口目前只支持美白和磨皮：
```java
//设置美白模式, 0为ST_BEAUTIFY_WHITEN_STRENGTH, 1为ST_BEAUTIFY_WHITEN2_STRENGTH, 2为ST_BEAUTIFY_WHITEN3_STRENGTH
mSTEffectsEngine.setBeautyMode(STEffectBeautyType.EFFECT_BEAUTY_BASE_WHITTEN, 0);

//设置磨皮模式, 默认值2.0, 1表示对全图磨皮, 2表示精细化磨皮
mSTEffectsEngine.setBeautyMode(STEffectBeautyType.EFFECT_BEAUTY_BASE_FACE_SMOOTH, 2);
```

- 设置背景虚化模式，背景虚化1和背景虚化2

```java
// 设置背景虚化1
mSTEffectsEngine.setBeautyMode(STEffectBeautyType.EFFECT_BEAUTY_TONE_BOKEH, 0);

// 设置背景虚化2
mSTEffectsEngine.setBeautyMode(STEffectBeautyType.EFFECT_BEAUTY_TONE_BOKEH, 1);
```

- 设置3D微整形

设置3D微整形效果，调用`get3DBeautyParts()`获取素材包中`blendshape`名称、`index`和当前强度，然后将获取的结果按需修改，最后将修改后的数据通过` set3dBeautyPartsStrength`传给库里。

```java
// 加载3D微整形资源配置文件(GL线程)
byte[] buffer = FileUtils.readFileFromAssets(ContextHolder.getContext(), "basic/3DMicroPlasticDefault.zip");
int result = stEffectsEngine.setBeautyFromBuffer(EFFECT_BEAUTY_3D_MICRO_PLASTIC, buffer);

// 获取到素材包中所有的blendshape名称、index和当前强度[0, 1]
public STEffect3DBeautyPartInfo[] get3DBeautyParts();

// 设置3D微整形要调用getMeshList接口，并将结果在调用set3dBeautyPartsStrength之前传到库里
public void setFaceMeshList();

// 在获取blendshape数组之后，可以依据起信息修改权重[0, 1]，设置给渲染引擎产生效果
public int set3dBeautyPartsStrength(STEffect3DBeautyPartInfo[] effect3DBeautyPartInfo, int length)
```

### 5.3 设置美妆

- 美妆类型枚举，参见STEffectBeautyType.java：
```java
// 美妆 makeup
public static final int EFFECT_BEAUTY_HAIR_DYE                          = 401;  // 染发
public static final int EFFECT_BEAUTY_MAKEUP_LIP                        = 402;  // 口红
public static final int EFFECT_BEAUTY_MAKEUP_CHEEK                      = 403;  // 腮红
public static final int EFFECT_BEAUTY_MAKEUP_NOSE                       = 404;  // 修容
public static final int EFFECT_BEAUTY_MAKEUP_EYE_BROW                   = 405;  // 眉毛
public static final int EFFECT_BEAUTY_MAKEUP_EYE_SHADOW                 = 406;  // 眼影
public static final int EFFECT_BEAUTY_MAKEUP_EYE_LINE                   = 407;  // 眼线
public static final int EFFECT_BEAUTY_MAKEUP_EYE_LASH                   = 408;  // 眼睫毛
public static final int EFFECT_BEAUTY_MAKEUP_EYE_BALL                   = 409;  // 美瞳
public static final int EFFECT_BEAUTY_MAKEUP_PACKED                     = 410;  ///< 打包的美妆素材，可能包含一到多个单独的美妆模块，另外，添加时会替换所有现有美妆

```
- 设置美妆，以设置口红素材为例：
```java
//设置口红素材
stEffectsEngine.setBeauty(STEffectBeautyType.EFFECT_BEAUTY_MAKEUP_LIP, typePath);

//从assets文件目录设置口红素材
stEffectsEngine.setBeautyFromAssetsFile(STEffectBeautyType.EFFECT_BEAUTY_MAKEUP_LIP, assetsTypePath， mContext.getAssets());

```
- 设置美妆强度, 以设置口红强度为0.5为例：
```java
//设置口红强度为0.5
stEffectsEngine.setBeautyStrength(STEffectBeautyType.EFFECT_BEAUTY_MAKEUP_LIP, 0.5f);
```

### 5.4 设置滤镜
- 滤镜类型枚举，参见STEffectBeautyType.java：
```java
// 滤镜 filter
public static final int EFFECT_BEAUTY_FILTER                            = 501;  // 滤镜
```
- 设置滤镜：
```java
//设置滤镜素材
stEffectsEngine.setBeauty(STEffectBeautyType.EFFECT_BEAUTY_FILTER, path);

//从assets文件目录设置滤镜素材
stEffectsEngine.setBeautyFromAssetsFile(STEffectBeautyType.EFFECT_BEAUTY_FILTER, assetsTypePath， mContext.getAssets());

```
- 设置滤镜强度, 以设置滤镜强度为0.5为例：
```java
//设置滤镜强度为0.5
stEffectsEngine.setBeautyStrength(STEffectBeautyType.EFFECT_BEAUTY_FILTER, 0.5f);
```
### 5.5 设置贴纸
- 添加贴纸
```java
//添加贴纸
int packageId = stEffectsEngine.addPackage(mCurrentStickerPath);

//从assets文件目录添加贴纸
int packageId = stEffectsEngine.addPackageFromAssetsFile(mCurrentStickerPath, mContext.getAssets());
```
- 贴纸声音自定义播放，贴纸声音默认使用STSoundPlay进行管理，其实现为单实例，与特效句柄绑定，用户可重写PlayControlListener来实现自定义声音播放逻辑。
```java
/**
 * 音频播放监听器
 */
public interface PlayControlListener {
    /**
     * 加载音频素材callback
     *
     * @param name    音频名称
     * @param content 音频内容
     */
    void onSoundLoaded(String name, byte[] content);

    /**
     * 播放音频callback
     *
     * @param name 音频名称
     * @param loop 循环次数，0表示无限循环，直到onStopPlay回调，大于0表示循环次数
     */
    void onStartPlay(String name, int loop);

    /**
     * 停止播放callback
     *
     * @param name 音频名称
     */
    void onStopPlay(String name);

    /**
     * 暂停播放callback
     *
     * @param name 音频名称
     */
    void onSoundPause(String name);

    /**
     * 重新播放callback
     *
     * @param name 音频名称
     */
    void onSoundResume(String name);
}

/**
 * 设置播放控制监听器
 *
 * @param listener listener为null，SDK默认处理，若不为null，用户自行处理
 */
public void setPlayControlListener(PlayControlListener listener) {
    if (listener != null) {
        mPlayControlDefaultListener = listener;
    }
}

```
- 贴纸Trigger信息，贴纸中可包含trigger信息，用于触发贴纸特效，如眨眼、手势等动作，使用getHumanActionDetectConfig接口重新配置检测config
```java
public long getStickerTriggerAction() {
    return stEffectsEngine.getHumanDetectConfig();
}
```
### 5.6 设置试妆TryOn

- 添加口红或染发素材

```java
 /**
  * 加载美颜素材
  * @param param 美颜类型
  * @param path  待添加的素材文件路径
  * @return 成功返回0，错误返回其它，参考STResultCode
  */
public native int setBeauty(int param, String path);
```

- 设置TryOn参数

设置试妆之前需要先调用getTryOnParam()获取试妆参数，修改参数后再调用setTryOnParam传入。

```java
/**
 * 获取试妆相关参数
 */
public native STEffectTryonInfo getTryOnParam();

/**
 * 设置试妆相关参数
 * @param info TryOn接口输入参数
 * @return 成功返回0，错误返回其它，参考STResultCode
 */
public native int setTryOnParam(STEffectTryonInfo info, int type);
```

接口输入参数(STEffectTryonInfo)说明。

```java
int type;           // 美颜类型
STColor color;      // 颜色
float strength;     // 强度
float midtone;      // 明暗度
float highlight;    // 高光
```

### 5.7 设置风格

- 添加风格

```java
//切换风格
int packageId = stEffectsEngine.addPackage(mCurrentStylePath);

//从assets文件目录切换风格
int packageId = stEffectsEngine.addPackageFromAssetsFile(mCurrentStylePath, mContext.getAssets());
```

- 设置风格中滤镜和美妆强度

```java
    /**
     * 设置贴纸素材包内部美颜组合的强度，强度范围[0.0, 1.0]
     *
     * @param packageId  素材包id
     * @param beautyGroup 美颜组合类型，目前只支持设置美妆、滤镜组合的强度
     * @param strength 强度值
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    public native int setPackageBeautyGroupStrength(int packageId, int beautyGroup, float strength);
```

### 5.8 特效渲染

- 特效渲染接口需在opengl线程调用
```java
//渲染接口输入参数
STEffectsInput stEffectsInput = new STEffectsInput(mTextureId, mImageHeight, mImageWidth, mCameraProxy.getOrientation(),
                    mCameraProxy.getOrientation(), mCameraID == Camera.CameraInfo.CAMERA_FACING_FRONT, false,0);
//渲染接口输出参数
STEffectsOutput stEffectsOutput = new STEffectsOutput(mBeautifyTextureId[0], null, null);
// 渲染
mSTEffectsEngine.processTexture(stEffectsInput, stEffectsOutput);
```
- 接口输入参数(STEffectsInput)说明
```java
public class STEffectsInput {
  // 输入纹理
  private STEffectTexture texture;
  // 输入buffer 
  private STImageBuffer image;
  // camera预览方向
  private int orientation;
  // 是否需要mirror图像
  private boolean needMirror;
  // 是否需要输出buffer
  private boolean needOutputBuffer;
  // 输出buffer格式
  private int outputBufferFormat;
  
  ...
}
```

- 接口输出参数(STEffectRenderOutParam)说明
```java
public class STEffectRenderOutParam {
  // 输出纹理信息，需上层初始化
  private STEffectTexture texture;
  // 输出图像数据，用于推流等场景，不需要可传null
  private STImage image;
  输出脸部变形后的人脸检测结果，不需要可传null
  private STHumanAction humanAction;
  
  ...
}
```

### 5.9 绿幕分割

设置绿幕分割背景图片

```java
int changeBg(int stickerId, STImage image);
```

绿幕分割视频版

绿幕分割视频版实现流程：初始化MediaPlayer，并传入视频地址，输出Mediaplay的数据到SurfaceTexture上，调用SurfaceTexture的updateTexImage来从视频流中获取新一帧的数据，获取到的是OES纹理，将OES纹理转为2D纹理，再将纹理信息传入changeBg2()接口中。
调用示例如下：

```java
    // 获取视频OES纹理，再将OES纹理转2D纹理
    protected void greenSegmentVideo() {
        if (mTextureIdBGVideo == OpenGLUtils.NO_TEXTURE) {
            mTextureIdBGVideo = OpenGLUtils.getExternalOESTextureID();
            mGreenSegVideoSurfaceTexture = new SurfaceTexture(mTextureIdBGVideo);
        }
        if (TextUtils.isEmpty(mGreenSegVideoPath) && greenSegmentBgVideoWidth == 0) return;
        long time = System.currentTimeMillis();
        if (!TextUtils.isEmpty(mGreenSegVideoPath) && greenSegmentBgVideoWidth != 0) {
            mGreenSegVideoSurfaceTexture.updateTexImage();
            try {
                int[] widthHeight = confirmWidthAndHeight(greenSegmentBgVideoRotation);
                mGLRender.calculateVertexBuffer2(widthHeight[0], widthHeight[1], widthHeight[0], widthHeight[1]);
                int textureId = mGLRender.preProcess3(mTextureIdBGVideo, widthHeight[0], widthHeight[1]);
                greenSegmentVideo(textureId, widthHeight[0], widthHeight[1]);
            } catch (Exception e) {
                return;
            }
        }
    }

    // 调用changeBg2()
    public void greenSegmentVideo(int texureId, int width, int height) {
        STEffectTexture texture = new STEffectTexture(texureId, width, height, 0);
        Set<Integer> integers = mCurrentStickerMaps.keySet();
        for (Integer packageId : integers) {
            int ret = stEffectsEngine.changeBg2(packageId, texture);
            mGlSurfaceView.requestRender();
        }
    }
```



### 5.10 GanSkin

GanSkin接入流程，根据白名单中芯片型号按需加载额外libs文件，再根据芯片型号设置素材包。白名单具体使用逻辑参见sample中的WhiteListUtils.java。

- 相关API

```
    /**
     * 加载美颜素材
     *
     * @param param 美颜类型
     * @param path  待添加的素材文件路径
     * @return 成功返回0，错误返回其它，参考STResultCode
     */
    int setBeauty(int param, String path);
```

- 使用示例

```java
// 步骤1：参考WhiteListUtils.java加载额外libs文件
WhiteListUtils.checkWhiteListAndLoadExtraLibs(ContextHolder.getContext())

// 步骤2：加载GAN肤质素材包
protected int loadGanModel(){
  if(WhiteListUtils.getGanModelType() == WhiteListUtils.STGanSkinModelType.MODEL_TYPE_MTK.getType()){
    Log.e("WhiteListUtils", "gan model: mtk");
    String ganPath = ContextHolder.getContext().getExternalFilesDir(null) + File.separator + Constants.GAN_SKIN_MODEL_MTK;
    ret = mSTEffectsEngine.setBeauty(EFFECT_BEAUTY_BASE_FACE_SMOOTH, ganPath);
} else if(WhiteListUtils.getGanModelType() == WhiteListUtils.STGanSkinModelType.MODEL_TYPE_QCOM.getType()){
    Log.e("WhiteListUtils", "gan model: qcom");
    String ganPath = ContextHolder.getContext().getExternalFilesDir(null) + File.separator + Constants.GAN_SKIN_MODEL_QNN;
    ret = mSTEffectsEngine.setBeauty(EFFECT_BEAUTY_BASE_FACE_SMOOTH, ganPath);
}else {
    Log.e("WhiteListUtils", "gan model: ocl");
    String ganPath = ContextHolder.getContext().getExternalFilesDir(null) + File.separator + Constants.GAN_SKIN_MODEL_OCL;
    ret = mSTEffectsEngine.setBeauty(EFFECT_BEAUTY_BASE_FACE_SMOOTH, ganPath);
  }
}

// 步骤3：设置mode
setBeautyMode(STEffectBeautyType.EFFECT_BEAUTY_BASE_FACE_SMOOTH, STSmoothMode.EFFECT_SMOOTH_FACE_EVEN);
```



### 5.11 获取当前特效覆盖的美颜参数

- 获取覆盖生效的美颜的信息, 仅在添加、更改和移除贴纸素材之后调用。使用如下：
```java
//切换贴纸素材
int packageId = stEffectsEngine.changePackage(mCurrentStickerPath);

//取覆盖生效的美颜的信息
if (beautyOverlapCount > 0) {
    STEffectBeautyInfo[] beautyInfos = stEffectsEngine.getOverlappedBeauty(beautyOverlapCount);
    //TODO：根据获取的刷新UI等
}

```
- getOverlappedBeauty接口输出参数
```java
public class STEffectBeautyInfo {
    int type;       // 美颜类型
    int mode;       // 美颜的模式
    float strength;  //美颜强度
    byte[] name = new byte[256];    // 所属的素材包的名字
}
```

### 5.12 特效句柄销毁
- 特效句柄销毁，需在opengl线程调用
```java
stEffectsEngine.release();
```
## 6 帧处理流程
- 以sample中单输入texture为例，参见CameraDisplayTexture
### 6.1 相机回调
- 相机回调设置，获取相机nv21数据
```java
private void setUpCamera(){
	// 初始化Camera设备预览需要的显示区域(mSurfaceTexture)
   if(mTextureId == OpenGLUtils.NO_TEXTURE){
	   mTextureId = OpenGLUtils.getExternalOESTextureID();
	   mSurfaceTexture = new SurfaceTexture(mTextureId);
    }
    ... ...
    //mSurfaceTexture添加相机回调（texture和buffer）
    mCameraProxy.startPreview(mSurfaceTexture,mPreviewCallback);
}
private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
    @Override
    public void onPreviewFrame(final byte[] data, Camera camera) {

        if (mCameraChanging || mCameraProxy.getCamera() == null) {
            return ;
        }
        ... ...
        //更新texture
        mGlSurfaceView.requestRender();
    }
};

```


### 6.2 帧渲染
- 调用processTexture()接口传入输入和输出参数进行特效渲染处理，需要在opengl线程调用
```java
//渲染接口输入参数
STEffectsInput stEffectsInput = new STEffectsInput(
      mTextureId, 
      mImageHeight, 
      mImageWidth, 
      mCameraProxy.getOrientation(),
      mCameraProxy.getOrientation(), 
      mCameraID == Camera.CameraInfo.CAMERA_FACING_FRONT, false,0);
//渲染接口输出参数
STEffectsOutput stEffectsOutput = new STEffectsOutput(mBeautifyTextureId[0], null, null);

// 输入参数输出参数传入渲染接口
mSTEffectsEngine.processTexture(stEffectsInput, stEffectsOutput);

// 得到渲染后的纹理
textureId = stEffectsOutput.getTextureId();

```

## 7 通用物体追踪
- 通用物体追踪使用STMobileObjectTrackNative实现
### 7.1 创建通用物体追踪句柄
- 创建通用物体追踪句柄
```java
protected void initObjectTrack() {
    int result = mSTMobileObjectTrackNative.createInstance();
}
```

### 7.2 设置通用物体追踪目标区域
- 设置通用物体追踪目标区域
```java
STRect inputRect = new STRect(mTargetRect.left, mTargetRect.top, mTargetRect.right, mTargetRect.bottom);
mSTMobileObjectTrackNative.setTarget(mImageData, STCommon.ST_PIX_FMT_NV21, mImageHeight, mImageWidth, inputRect);
```
### 7.3 通用物体追踪
- 通用物体追踪
```java
STRect outputRect = mSTMobileObjectTrackNative.objectTrack(mImageData, STCommon.ST_PIX_FMT_NV21, mImageHeight, mImageWidth, score);
```
- 获取通用物体追踪区域，objectTrack接口输出的Rect即图像中目标区域，每帧均会更新

### 7.4 通用物体追踪句柄销毁
- 通用物体追踪句柄销毁
```java
 mSTMobileObjectTrackNative.destroyInstance();
```
## 8 人脸属性检测
- 人脸属性检测使用STMobileFaceAttributeNative实现
### 8.1 创建人脸属性检测句柄
- 创建人脸属性检测句柄，传入人脸属性模型路径
```java
//从绝对路径创建
int result =  mSTMobileFaceAttributeNative.createInstance(modelpath);

//从assets路径创建
int result =  mSTMobileFaceAttributeNative.createInstanceFromAssetFile(modelpath, mContext.getAssets());

```

### 8.2 人脸属性检测
- 人脸属性检测
```java
STMobile106[] arrayFaces = null;
arrayFaces = humanAction.getMobileFaces();

if (arrayFaces != null && arrayFaces.length != 0) { // face attribute
    STFaceAttribute[] arrayFaceAttribute = new STFaceAttribute[arrayFaces.length];
    int result = mSTFaceAttributeNative.detect(data, STCommon.ST_PIX_FMT_NV21, mImageHeight, mImageWidth, arrayFaces, arrayFaceAttribute);
    if (result == 0) {
        if (arrayFaceAttribute[0].getAttributeCount() > 0) {
            mFaceAttribute = STFaceAttribute.getFaceAttributeString(arrayFaceAttribute[0]);
        } else {
            mFaceAttribute = "null";
        }
    }
}
```
### 8.3 人脸属性检测句柄销毁
- 人脸属性检测句柄销毁
```java
mSTMobileFaceAttributeNative.destroyInstance();
```

## 9 旋转和方向等相关说明
### 9.1 相机的方向
- 前置摄像头：绝大部分手机的前置摄像头的CameraInfo.orientation为270，特殊机型为90。
- 后置摄像头：绝大部分手机的后置摄像头的CameraInfo.orientation为90，特殊机型为270。
```java
private CameraInfo mCameraInfo = new CameraInfo();
//获取相机的方向
public int getOrientation(){
	if(mCameraInfo == null){
		return 0;
	}
	return mCameraInfo.orientation;
}
```

## 10 支持 16 KB 页面大小

自 2025 年 11 月 1 日起，Google Play 要求所有针对 Android 15（API 35）及更高版本的应用必须支持 16 KB 页面大小。

### 10.1 适配步骤

#### 10.1.1 NDK版本配置

确保使用 NDK r27 或更高版本。在项目的 `local.properties` 文件中添加：

```properties
ndk.dir=/Users/username/Library/Android/sdk/ndk/27.0.11902837
```

或在 `build.gradle` 中指定：

```gradle
android {
    ndkVersion "27.0.11902837"
}
```

#### 10.1.2 开启灵活页面大小支持

在模块级 `build.gradle` 文件中添加：

```gradle
android {
    defaultConfig {
        externalNativeBuild {
            cmake {
                arguments "-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON"
            }
        }
    }
}
```

对于使用 `Application.mk` 的项目，添加：

```makefile
APP_SUPPORT_FLEXIBLE_PAGE_SIZES := true
```

### 10.2 测试方法

使用Android 15模拟器中的16KB页面大小系统映像或支持的Pixel设备（通过开发者选项启用）进行测试。

更多详细信息，请参考[Android官方文档](https://developer.android.com/guide/practices/page-sizes?hl=zh-cn)。



