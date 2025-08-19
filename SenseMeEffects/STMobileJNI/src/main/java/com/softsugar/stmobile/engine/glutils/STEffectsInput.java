package com.softsugar.stmobile.engine.glutils;

import com.softsugar.stmobile.model.STEffectTexture;
import com.softsugar.stmobile.model.STRect;

public class STEffectsInput {

    //\~Chinese 输入纹理
    private STEffectTexture texture;

    //\~Chinese 输入buffer
    private STImageBuffer image;

    //\~Chinese camera预览方向
    private int orientation;

    //\~Chinese 人脸转正的方向
    private int rotate;

    //\~Chinese 是否需要mirror图像
    private boolean needMirror;

    //\~Chinese 是否需要输出buffer
    private boolean needOutputBuffer;

    //\~Chinese 输出buffer格式
    private int outputBufferFormat;

    //\~Chinese 除了目标人脸后，其它人脸无特效，默认-1，不做处理
    private int targetFaceId = -1;

    //\~Chinese 除了目标人脸后，其它人脸无特效，默认null，不做处理
    private STRect targetFaceRect;

    /**
     * \~Chinese 渲染输入参数
     *
     * @param texture            \~Chinese 输入纹理
     * @param orientation        \~Chinese 图像image中人脸朝向
     * @param rotate             \~Chinese 图像image生成纹理后旋转需要的方向
     * @param needMirror         \~Chinese 是否需要mirror图像
     * @param needOutputBuffer   \~Chinese 是否需要输出buffer
     * @param outputBufferFormat \~Chinese 输出buffer格式
     * @return 错误码，参见EffectsParams.ResultCode
     */
    public STEffectsInput(STEffectTexture texture, STImageBuffer image, int orientation, int rotate, boolean needMirror, boolean needOutputBuffer, int outputBufferFormat) {
        this.texture = texture;
        this.image = image;
        this.orientation = orientation;
        this.rotate = rotate;
        this.needMirror = needMirror;
        this.needOutputBuffer = needOutputBuffer;
        this.outputBufferFormat = outputBufferFormat;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean isNeedMirror() {
        return needMirror;
    }

    public boolean isNeedOutputBuffer() {
        return needOutputBuffer;
    }

    public int getRotate() {
        return rotate;
    }

    public int getOutputBufferFormat() {
        return outputBufferFormat;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setNeedMirror(boolean needMirror) {
        this.needMirror = needMirror;
    }

    public void setRotate(int rotateO) {
        this.rotate = rotate;
    }

    public void setNeedOutputBuffer(boolean needOutputBuffer) {
        this.needOutputBuffer = needOutputBuffer;
    }

    public void setOutputBufferFormat(int outputBufferFormat) {
        this.outputBufferFormat = outputBufferFormat;
    }

    public int getTargetFaceId() {
        return targetFaceId;
    }

    public STRect getTargetRect() {
        return targetFaceRect;
    }

    public void setTargetRect(STRect targetRect) {
        this.targetFaceRect = targetRect;
    }

    public void setTargetFaceId(int targetFaceId) {
        this.targetFaceId = targetFaceId;
    }

    public STEffectTexture getTexture() {
        return texture;
    }

    public STImageBuffer getImage() {
        return image;
    }

    public void setImage(STImageBuffer image) {
        this.image = image;
    }

    public void setTexture(STEffectTexture texture) {
        this.texture = texture;
    }
}
