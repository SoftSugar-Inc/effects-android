package com.softsugar.stmobile.model;

import com.softsugar.stmobile.STEffectInImage;

public class STEffectRenderInParam {
    long nativeHumanActionResult;  // \~Chinese jni内部human action指针  \~English native human action result
    STHumanAction humanAction;  // \~Chinese java human action对象  \~English java human action, nativeHumanActionResult and humanAction only one is need
    private STMobileAnimalResult animalResult;  // \~Chinese 动物检测信息  \~English animal result
    int rotate;  // \~Chinese 旋转角度  \~English rotation
    int frontRotate;  // \~Chinese 前景角度  \~English front rotation
    boolean needMirror;  // \~Chinese 是否需要镜像  \~English if need mirror
    STEffectCustomParam customParam;  // \~Chinese 自定义事件  \~English custom params
    STEffectTexture texture;  // \~Chinese 输入纹理  \~English input texture
    STEffectInImage image;  // \~Chinese 输入图像（图像和纹理只需要输入一个）  \~English input image（input null if no need）
    double timeStamp; // \~Chinese 时间戳  \~English timeStamp
    private int targetFaceId = -1;    // 除了目标人脸后，其它人脸无特效，默认-1，不做处理

    private STRect targetFaceSTRect;  // 除了目标人脸后，其它人脸无特效，默认null，不做处理


    /**
     * \~Chinese 引擎渲染接口输入参数
     *
     * @param nativeHumanActionResult \~Chinese 人脸等检测结果，不需要可传0（如基础美颜、滤镜功能）
     * @param animalResult            \~Chinese 动物检测信息，不需要可传null
     * @param rotate                  \~Chinese 人脸在纹理中的方向
     * @param needMirror              \~Chinese  传入图像与显示图像是否是镜像关系
     * @param customParam             \~Chinese 用户自定义参数，不需要可传null
     * @param texture                 \~Chinese 输入纹理，不可传null
     * @param image                   \~Chinese 输入图像数据，不需要可传null
     */
    /**
     * \~English Effects render input params
     *
     * @param nativeHumanActionResult \~English native human action result
     * @param animalResult            \~English animal info
     * @param rotate                  \~English face rotation
     * @param needMirror              \~English if need mirror
     * @param customParam             \~English custom param
     * @param texture                 \~English input texture
     * @param image                   \~English input image（input null if no need）
     */
    public STEffectRenderInParam(long nativeHumanActionResult, STMobileAnimalResult animalResult, int rotate, int frontRotate, boolean needMirror, STEffectCustomParam customParam, STEffectTexture texture, STEffectInImage image){
        this.nativeHumanActionResult = nativeHumanActionResult;
        this.animalResult = animalResult;
        this.rotate = rotate;
        this.frontRotate = frontRotate;
        this.needMirror = needMirror;
        this.customParam = customParam;
        this.texture = texture;
        this.image = image;
        this.timeStamp = System.currentTimeMillis();
    }

    public boolean isNeedMirror() {
        return needMirror;
    }

    public int getFrontRotate() {
        return frontRotate;
    }

    public int getRotate() {
        return rotate;
    }

    public STEffectCustomParam getCustomParam() {
        return customParam;
    }

    public STHumanAction getHumanAction() {
        return humanAction;
    }


    public STEffectTexture getTexture() {
        return texture;
    }

    public STEffectInImage getImage() {
        return image;
    }

    public STMobileAnimalResult getAnimalResult() {
        return animalResult;
    }

    public void setAnimalResult(STMobileAnimalResult animalResult) {
        this.animalResult = animalResult;
    }

    public void setCustomParam(STEffectCustomParam customParam) {
        this.customParam = customParam;
    }

    public void setFrontRotate(int frontRotate) {
        this.frontRotate = frontRotate;
    }

    public void setHumanAction(STHumanAction humanAction) {
        this.humanAction = humanAction;
    }

    public void setImage(STEffectInImage image) {
        this.image = image;
    }

    public void setNeedMirror(boolean needMirror) {
        this.needMirror = needMirror;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public void setTexture(STEffectTexture texture) {
        this.texture = texture;
    }

    public long getNativeHumanActionResult() {
        return nativeHumanActionResult;
    }

    public void setNativeHumanActionResult(long nativeHumanActionResult) {
        this.nativeHumanActionResult = nativeHumanActionResult;
    }

    public STRect getTargetSTRect() {
        return targetFaceSTRect;
    }

    public void setTargetSTRect(STRect targetFaceSTRect) {
        this.targetFaceSTRect = targetFaceSTRect;
    }

    public int getTargetFaceId() {
        return targetFaceId;
    }

    public void setTargetFaceId(int targetFaceId) {
        this.targetFaceId = targetFaceId;
    }
}
