package com.softsugar.stmobile.model;

public class STFaceExtraInfo {
    float[][] affineMat;            ///< \~Chinese 仿射变换矩阵
    int modelInputSize;

    public float[][] getAffineMat() {
        return affineMat;
    }
    public void setAffineMat(float[][] affineMat) {
        this.affineMat = affineMat;
    }

    public int getModelInputSize() {
        return modelInputSize;
    }

    public void setModelInputSize(int modelInputSize) {
        this.modelInputSize = modelInputSize;
    }
}
