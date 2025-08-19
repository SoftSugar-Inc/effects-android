# 常见问题FAQ

#### 1. 能否使用自己的包名运行sample？
答：可以使用自己包名运行sample，但是sample默认使用服务器license授权，请在代码中先修改为离授权模式，并更换自己的测试license文件即可

#### 2. 如何修改为自定义授权逻辑？
答：1.授权逻辑请参考STLicenseUtils.java文件 2.需要注意使用离线license和服务器托管license的区别 3.需要注意激活码的缓存和使用 4.generateActiveCodeFromBuffer避免多次调用，以防止授权出错

#### 3. 检测接入后无检测结果如何排查？
答：1.首先查看错误码信息 2.排查license是否授权通过，以及确认license提供的能力是否包含检测功能 3.排查检测模型是否加载成功，以及检测config是否配置 4.过滤log “softsugar error"查看是否错误信息

#### 4. 美颜、贴纸等接入后无效果如何排查？
答：1.首先查看错误码信息 2.排查license是否授权通过，以及确认license提供的能力是否包含检测功能 3.确认下接口是否输入了检测结果 4.过滤log “softsugar error"查看是否有错误信息

#### 5. 检测接口传入的方向是（0，90，180，270）还是（0，1，2，3）？
答：检测及渲染接口输入方向均为（0，1，2，3），其定义在STRotateType类中

#### 6. SDK是否支持OES格式纹理输入？
答：渲染接口支持texture_2d格式纹理，不支持OES格式纹理。但是sample中提供了转换函数供使用，详见STGLRender中的preProcess接口

#### 7. 如何将纹理保存为bitmap？
答：首先要将纹理使用readPixel读取到rgba的数据，再保存为bitmap，请参考demo中的ImageDisplay中的textureToBitmap函数

#### 8. 预览不同分辨率，如何使预览保留黑边或者铺满全屏？
答：sample中可以修改顶点坐标来进行调整，参考STGLRender中的calculateVertexBuffer函数，切换分辨率时，使用Math.max(ratio1, ratio2)时预览留短边黑边，使用Math.min(ratio1, ratio2)时预览短边会铺满全屏

#### 9. 如何获取特效处理之后的人脸关键点等信息？
答：特效渲染render接口可以输出处理之后的检测结果，需要将needOutputHumanAction参数输入为true，然后通过输出参数的get接口获取:
```java
mSTMobileEffectNative.render(sTEffectRenderInParam, stEffectRenderOutParam, true);
if(stEffectRenderOutParam != null){
    STHumanAction humanAction = stEffectRenderOutParam.getHumanAction();
}
```

#### 10. 如何获取特效处理之后的图像信息用于保存或者推流？
答：特效渲染render接口可以输出处理之后的buffer数据，将接口中的输出参数设置好内存和格式
```java
//渲染接口输出参数
STImage image = new STImage(new byte[mImageWidth * mImageHeight * 4], mImageWidth, mImageHeight, image, STCommon.ST_PIX_FMT_RGBA8888, 0);
STEffectRenderOutParam stEffectRenderOutParam = new STEffectRenderOutParam(stEffectTextureOut, image, mSTHumanAction[mCameraInputTextureIndex]);

mSTMobileEffectNative.render(sTEffectRenderInParam, stEffectRenderOutParam, true);
if(stEffectRenderOutParam != null){
    STImage outputImage = stEffectRenderOutParam.getImage();
}
```