package com.softsugar.stmobile.engine.glutils;

import com.softsugar.stmobile.model.STHumanAction;

public class STEffectsOutput {
    private int textureId;
    private STImageBuffer image;
    private STImageBuffer effectImage;
    private STHumanAction humanAction;

    /**
     * 渲染输输出参数
     *
     * @param textureId 输出纹理
     * @param image     输出图像
     */
    public STEffectsOutput(int textureId, STImageBuffer image, STImageBuffer effectImage){
        this.textureId = textureId;
        this.image = image;
        this.effectImage = effectImage;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public void setImage(STImageBuffer image) {
        this.image = image;
    }

    public int getTextureId() {
        return textureId;
    }

    public STImageBuffer getImage() {
        return image;
    }

    public STHumanAction getHumanAction() {
        return humanAction;
    }

    public void setHumanAction(STHumanAction humanAction) {
        this.humanAction = humanAction;
    }

    public STImageBuffer getEffectImage() {
        return effectImage;
    }

    public void setEffectImage(STImageBuffer effectImage) {
        this.effectImage = effectImage;
    }
}
