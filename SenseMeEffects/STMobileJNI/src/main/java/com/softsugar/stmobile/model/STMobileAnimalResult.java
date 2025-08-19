package com.softsugar.stmobile.model;

public class STMobileAnimalResult {

    // \~Chinese 检测到的动物脸数组
    private STAnimalFace[] animalFaceArray;

    // \~Chinese 动物脸个数
    private int count;

    // \~Chinese 额外信息
    private byte[] extraBuffer;

    private int extraBufferLength;

    public STAnimalFace[] getAnimalFaceArray() {
        return animalFaceArray;
    }

    public void setAnimalFaceArray(STAnimalFace[] animalFaceArray) {
        this.animalFaceArray = animalFaceArray;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public byte[] getExtraBuffer() {
        return extraBuffer;
    }

    public void setExtraBuffer(byte[] extraBuffer) {
        this.extraBuffer = extraBuffer;
    }

    public int getExtraBufferLength() {
        return extraBufferLength;
    }

    public void setExtraBufferLength(int extraBufferLength) {
        this.extraBufferLength = extraBufferLength;
    }
}
