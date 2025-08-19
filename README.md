# SenseAR Effects特效引擎Android SDK集成Demo

本项目是**商汤科技**提供的[**特效引擎 SDK**](https://sensear.softsugar.com/) Android 集成演示工程，旨在帮助开发者快速了解如何在Android项目中集成与使用我们的特效引擎SDK。您可以通过运行本 Demo，直观体验SDK提供的各类人脸特效、贴纸、美颜、滤镜等功能。

---

## 功能亮点

- 人脸检测与跟踪
- 美颜效果（磨皮、美白、瘦脸等）
- AR 贴纸/道具实时叠加
- 实时滤镜效果
- 视频流处理与渲染
- 高性能渲染支持（基于 OpenGL ES）

---

## 环境要求

- Android Studio 4.0 及以上
- Android API Level 21 (Android 5.0) 及以上
- NDK 27 及以上
- 真机运行（部分功能依赖相机）

---

## 运行Demo

- clone工程到本地
- 使用Android Studio打开SenseMeEffects项目
- 调整local.properties文件中的ndk.dir和sdk.dir路径
- 将从商汤商务渠道获取的license文件放入工程（需要将名字改为"SENSEME.lic"）
- 将工程的applicationId修改为与上述license绑定的包名
- 完成工程编译及App在测试机的安装，运行Demo

> 请[**提交免费试用申请**](https://sensear.softsugar.com/)，或**联系商务**（Tel: 181-1640-5190）获取测试license。

---

## SDK 集成说明

本项目已经完成对SDK的集成，您无需单独引入SDK依赖。

如果您需要在自己的项目中引入SDK，可以选择以下两种集成方式：

### 方式一：源码依赖集成

1. **添加模块依赖**
   在您的app模块的build.gradle文件中添加：
   ```gradle
   dependencies {
       implementation project(':STMobileJNI')
   }
   ```

2. **配置项目结构**
   - 将STMobileJNI模块添加到您的项目中
   - 在settings.gradle中包含该模块：
   ```gradle
   include ':app', ':STMobileJNI'
   ```

### 方式二：AAR依赖集成

1. **添加AAR文件**
   将提供的AAR文件放入app/libs目录下

2. **配置Gradle依赖**
   在app模块的build.gradle文件中添加：
   ```gradle
   dependencies {
       implementation files('libs/STMobileJNI-release.aar')
   }
   ```

### 通用配置步骤

1. **配置CPU架构**
   在app模块的build.gradle文件中配置支持的CPU架构：
   ```gradle
   android {
       defaultConfig {
           ndk {
               abiFilters 'arm64-v8a', 'armeabi-v7a'
           }
       }
       
       packagingOptions {
           pickFirst '**/libc++_shared.so'
           pickFirst '**/libst_mobile.so'
       }
   }
   ```

2. **添加权限**
   在AndroidManifest.xml中添加必要权限：
   
   ```xml
   <uses-permission android:name="android.permission.CAMERA" />
   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
   <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
   <uses-permission android:name="android.permission.INTERNET" />
   ```
   
3. **配置NDK**
   确保项目中包含正确的NDK配置和.so库文件

> [详细接入文档](SenseMeEffects/docs/md文件/androidDevManual.md)

---

## 反馈

- 如果您在使用过程中有遇到什么问题，欢迎提交 [**issue**](https://github.com/SoftSugar-Inc/effects-android/issues)。
- 我们真诚地感谢您的贡献，欢迎通过 GitHub 的 fork 和 pull request 流程来提交代码。
