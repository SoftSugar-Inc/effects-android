package com.softsugar.stmobile.params;

import com.softsugar.stmobile.model.STBodyAvatar;
import com.softsugar.stmobile.model.STHumanAction;

public class STGenAvatarInParam {

    private STGenAvatarTexture tex;
    long nativeHumanActionResult;
    private STHumanAction human;
    private STBodyAvatar bodyAvatarArray;
    private STBlendShapeFactory blendFactor;

    private int bodyAvatarCount;
    private int rotate;

    public STGenAvatarInParam(STGenAvatarTexture tex, STHumanAction human, STBodyAvatar bodyAvatarArray, STBlendShapeFactory blendFactor, int bodyAvatarCount, int rotate) {
        this.tex = tex;
        this.human = human;
        this.bodyAvatarArray = bodyAvatarArray;
        this.blendFactor = blendFactor;
        this.bodyAvatarCount = bodyAvatarCount;
        this.rotate = rotate;
    }

    public STGenAvatarInParam(STGenAvatarTexture tex, long nativeHumanActionResult, STBodyAvatar bodyAvatarArray, STBlendShapeFactory blendFactor, int bodyAvatarCount, int rotate) {
        this.tex = tex;
        this.nativeHumanActionResult = nativeHumanActionResult;
        this.bodyAvatarArray = bodyAvatarArray;
        this.blendFactor = blendFactor;
        this.bodyAvatarCount = bodyAvatarCount;
        this.rotate = rotate;
    }

    public int getBodyAvatarCount() {
        return bodyAvatarCount;
    }

    public void setBodyAvatarCount(int bodyAvatarCount) {
        this.bodyAvatarCount = bodyAvatarCount;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public STGenAvatarTexture getTex() {
        return tex;
    }

    public void setTex(STGenAvatarTexture tex) {
        this.tex = tex;
    }

    public STHumanAction getHuman() {
        return human;
    }

    public void setHuman(STHumanAction human) {
        this.human = human;
    }

    public STBodyAvatar getBodyAvatarArray() {
        return bodyAvatarArray;
    }

    public void setBodyAvatarArray(STBodyAvatar bodyAvatarArray) {
        this.bodyAvatarArray = bodyAvatarArray;
    }

    public STBlendShapeFactory getBlendFactor() {
        return blendFactor;
    }

    public void setBlendFactor(STBlendShapeFactory blendFactor) {
        this.blendFactor = blendFactor;
    }
}
