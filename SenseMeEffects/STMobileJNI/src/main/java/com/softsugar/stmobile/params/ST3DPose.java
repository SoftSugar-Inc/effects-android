package com.softsugar.stmobile.params;

import java.util.Arrays;

public class ST3DPose {

    private float[] mat;
    private int extraBits;

    public float[] getMat() {
        return mat;
    }

    public void setMat(float[] mat) {
        this.mat = mat;
    }

    public int getExtraBits() {
        return extraBits;
    }

    public void setExtraBits(int extraBits) {
        this.extraBits = extraBits;
    }

    @Override
    public String toString() {
        return "ST3DPose{" +
                "mat=" + Arrays.toString(mat) +
                ", extraBits=" + extraBits +
                '}';
    }
}
