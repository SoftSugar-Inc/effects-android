package com.softsugar.stmobile.model;

public class STMobileTransform {
    private float[] position;
    private float[] eulerAngle;
    private float[] scale;

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getEulerAngle() {
        return eulerAngle;
    }

    public void setEulerAngle(float[] eulerAngle) {
        this.eulerAngle = eulerAngle;
    }

    public float[] getScale() {
        return scale;
    }

    public void setScale(float[] scale) {
        this.scale = scale;
    }
}
