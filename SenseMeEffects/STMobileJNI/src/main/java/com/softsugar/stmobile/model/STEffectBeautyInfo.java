package com.softsugar.stmobile.model;

public class STEffectBeautyInfo {
    int type;       // \~Chinese 美颜类型
    int mode;       // \~Chinese 美颜的模式
    float strength;  //\~Chinese 美颜强度
    byte[] name = new byte[256];    // \~Chinese 所属的素材包的名字

    public float getStrength() {
        return strength;
    }

    public byte[] getName() {
        return name;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getType() {
        return type;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "STEffectBeautyInfo{" +
                "type=" + type +
                ", mode=" + mode +
                ", strength=" + strength +
                '}';
    }
}
