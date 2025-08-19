package com.softsugar.stmobile.model;

import java.util.Arrays;

public class STEffectTryonInfo {
    STColor color;      // \~Chinese 颜色
    float strength;     // \~Chinese 颜色强度, [0, 1.0]
    float lineWidthRatio;// \~Chinese 唇线线宽比例
    float midtone;      // \~Chinese 明暗度
    float highlight;    // \~Chinese 高光
    int lipFinishType;  // \~Chinese 口红质地类型
    int regionCount;    // \~Chinese 当前效果的区域数量
    STEffectsTryOnRegionInfo[] regionInfo;// \~Chinese 区域信息，最多支持6个区域

    public int getRegionCount() {
        return regionCount;
    }

    public void setRegionCount(int regionCount) {
        this.regionCount = regionCount;
    }

    public int getLipFinishType() {
        return lipFinishType;
    }

    public void setLipFinishType(int lipFinishType) {
        this.lipFinishType = lipFinishType;
    }

    public STColor getColor() {
        return color;
    }

    public void setColor(STColor color) {
        this.color = color;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public float getMidtone() {
        return midtone;
    }

    public void setMidtone(float midtone) {
        this.midtone = midtone;
    }

    public float getHighlight() {
        return highlight;
    }

    public void setHighlight(float highlight) {
        this.highlight = highlight;
    }

    public STEffectsTryOnRegionInfo[] getRegionInfo() {
        return regionInfo;
    }

    public void setRegionInfo(STEffectsTryOnRegionInfo[] regionInfo) {
        this.regionInfo = regionInfo;
    }

    public float getLineWidthRatio() {
        return lineWidthRatio;
    }

    public void setLineWidthRatio(float lineWidthRatio) {
        this.lineWidthRatio = lineWidthRatio;
    }

    @Override
    public String toString() {
        return "STEffectTryonInfo{" +
                "color=" + color +
                ", strength=" + strength +
                ", midtone=" + midtone +
                ", highlight=" + highlight +
                ", lineWidthRatio=" + lineWidthRatio +
                ", lipFinishType=" + lipFinishType +
                ", regionCount=" + regionCount +
                ", regionInfo=" + Arrays.toString(regionInfo) +
                '}';
    }
}
