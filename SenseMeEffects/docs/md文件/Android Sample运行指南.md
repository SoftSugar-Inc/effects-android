# Android Sample运行指南

## 目录
#### 1.sample使用指南
- 1.1 本地环境配置
- 1.2 使用方式
- 1.3 注意事项
#### 2.sample app文件结构
- 2.1 sample app assets文件结构
- 2.2 sample app java文件结构

## 1 sample使用指南
### 1.1 本地环境配置
- NDK版本推荐：21.0.6113669
- NDK版本要求：16及以上
- CMake版本推荐：3.6.4111459或3.10.2.4988404
- gradle版本推荐：distributionUrl=https\://services.gradle.org/distributions/gradle-6.1.1-all.zip
- gradle tools版本推荐：classpath "com.android.tools.build:gradle:4.0.0"

### 1.2 使用方式
- 解压工程打包文件，找到SenseMeEffects文件目录
- 使用Android Studio打开SenseMeEffects项目
- 调整local.properties文件中的ndk.dir和sdk.dir
- 运行sample

### 1.3 注意事项
- 注意手机是否联网，sample默认会从服务器拉取license
- 不要修改包名，服务器license绑定了包名，修改包名会导致授权失败
- 注意手机时间，服务器license限制了授权时间段


## 2 sample app文件结构

### 2.2 sample app assets文件结构
```
.
├── SenseME_Provisions_v1.0.html //说明文件
├── filter_food //食物滤镜
├── filter_portrait //人物滤镜
├── filter_scenery //风景滤镜
├── filter_still_life //静物滤镜
├── license //本地授权文件夹
├── makeup_all //本地整装素材
├── makeup_blush //本地腮红素材
├── makeup_brow //本地眉毛素材
├── makeup_eye //本地美瞳素材
├── makeup_eyelash //本地眼睫毛素材
├── makeup_highlight //本地修容素材
├── makeup_lip //本地口红素材
├── models //检测模型文件目录
├── newEngine //本地贴纸
├── shader //编解码使用的shader文件
└── whiten_gif.zip //美白模式文件
```
### 2.1 sample app java文件结构
```
.
└── effects
    ├── STLicenseUtils.java //授权帮助文件
    ├── SenseMeApplication.java
    ├── WelcomeActivity.java 
    ├── activity //主要界面，包括预览、图片和视频预览界面等
    │   ├── BaseActivity.java
    │   ├── CameraActivity.java
    │   ├── ImageActivity.java
    │   ├── LoadImageActivity.java
    │   └── VideoActivity.java
    ├── adapter //适配器
    │   ├── BeautyItemAdapter.java
    │   ├── BeautyOptionsAdapter.java
    │   ├── FilterAdapter.java
    │   ├── FilterInfo.java
    │   ├── FullBeautyItemAdapter.java
    │   ├── MakeupAdapter.java
    │   ├── MakeupInfo.java
    │   ├── MediaAdapter.java
    │   ├── NativeStickerAdapter.java
    │   ├── NewStickerAdapter.java
    │   ├── ObjectAdapter.java
    │   ├── StickerAdapter.java
    │   └── StickerOptionsAdapter.java
    ├── camera //相机管理类
    │   ├── CameraProxy.java
    │   └── CameraV2Proxy.java
    ├── display //渲染相关
    │   ├── BaseCameraDisplay.java //相机渲染基类
    │   ├── BaseDisplay.java //渲染基类
    │   ├── CameraDisplaySingleBuffer.java //单输入相机buffer
    │   ├── CameraDisplaySingleTexture.java //单输入相机纹理
    │   ├── ChangePreviewSizeListener.java
    │   ├── ImageDisplay.java //图片渲染
    │   ├── STStickerEventCallback.java //贴纸事件
    │   ├── VideoPreviewDisplay.java //视频预览渲染
    │   └── glutils //渲染工具类
    │       ├── EGLContextHelper.java
    │       ├── GlUtil.java
    │       ├── ImageInputRender.java
    │       ├── OpenGLUtils.java
    │       ├── Rotation.java
    │       ├── STGLRender.java
    │       └── TextureRotationUtil.java
    ├── encoder //视频编解码相关类
    │   ├── MediaAudioEncoder.java
    │   ├── MediaEncoder.java
    │   ├── MediaMuxerWrapper.java
    │   ├── MediaVideoEncoder.java
    │   ├── mediacodec
    │   │   ├── DetectAndRender.java
    │   │   ├── InputSurface.java
    │   │   ├── OutputSurface.java
    │   │   ├── TextureRender.java
    │   │   ├── VideoProcessor.java
    │   │   ├── filter
    │   │   └── utils
    │   └── utils
    │       ├── EGLBase.java
    │       ├── GLDrawer2D.java
    │       └── RenderHandler.java
    ├── utils //帮助工具类
    │   ├── Accelerometer.java
    │   ├── BaseHandler.java
    │   ├── CheckAudioPermission.java
    │   ├── CollectionSortUtils.java
    │   ├── CommomDialog.java
    │   ├── Constants.java
    │   ├── EffectInfoDataHelper.java
    │   ├── FileUtils.java
    │   ├── ImageUtils.java
    │   ├── LocalDataStore.java
    │   ├── LocalDataStoreI.java
    │   ├── LogUtils.java
    │   ├── MultiLanguageUtils.java
    │   ├── NetworkUtils.java
    │   ├── STUtils.java
    │   └── SpUtils.java
    └── view //UI控件
        ├── BeautyItem.java
        ├── BeautyOptionsItem.java
        ├── ByteWrapper.java
        ├── FilterItem.java
        ├── FullBeautyItem.java
        ├── IndicatorSeekBar.java
        ├── MakeUpOptionsItem.java
        ├── MakeupItem.java
        ├── MediaItem.java
        ├── ObjectItem.java
        ├── RoundImageView.java
        ├── StickerItem.java
        ├── StickerOptionsItem.java
        ├── StickerState.java
        ├── VerticalSeekBar.java
        └── widget
            └── StretchTextView.java
```
