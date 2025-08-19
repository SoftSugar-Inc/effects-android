package com.softsugar.stmobile.params;

import java.util.Arrays;

public class STBlendShapeFactory {

    //\~Chinese 表情基数组
    private float[] blendShapes;

    //\~Chinese 表情基个数(0或52)
    private int count;

    // \~Chinese 不同部位的bs值
    private int region;

    public STBlendShapeFactory(){}

    public STBlendShapeFactory(float[] blendShapes, int count, int region) {
        this.blendShapes = blendShapes;
        this.count = count;
        this.region = region;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public float[] getBlendShapes() {
        return blendShapes;
    }

    public void setBlendShapes(float[] blendShapes) {
        this.blendShapes = blendShapes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "STBlendShapeFactory{" +
                "blendShapes=" + Arrays.toString(blendShapes) +
                ", count=" + count +
                ", region=" + region +
                '}';
    }
}
