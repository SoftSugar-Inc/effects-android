package com.softsugar.stmobile.model;

/**
 * \~Chinese 区域信息
 */
public class STEffectsTryOnRegionInfo {
    /**
     * \~Chinese 区域id
     */
    int regionId;
    /**
     * \~Chinese 颜色强度，[0, 1.0]
     */
    float strength;

    /**
     * \~Chinese 颜色
     */
    STColor color;

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public STColor getColor() {
        return color;
    }

    public void setColor(STColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "STEffectsTryOnRegionInfo{" +
                "regionId=" + regionId +
                ", strength=" + strength +
                ", color=" + color +
                '}';
    }
}
