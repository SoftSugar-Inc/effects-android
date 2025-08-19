package com.softsugar.stmobile.model;

public class STColor {
    public float r;
    public float g;
    public float b;
    public float a;

    public STColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public float getG() {
        return g;
    }

    public float getR() {
        return r;
    }

    public String toHexString(){
        int A = (int)a * 256;
        int R = (int)r * 256;
        int G = (int)g * 256;
        int B = (int)b * 256;
        return "0x"+A + R + G + B;
    }

    @Override
    public String toString() {
        return "STColor{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }
}
