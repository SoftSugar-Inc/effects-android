package com.softsugar.stmobile.engine.glutils;

public class STImageBuffer {
    byte[] imageBuffer;
    int imageFormat;
    int width;
    int height;

    public STImageBuffer(byte[] imageBuffer, int imageFormat, int width, int height){
        this.imageBuffer = imageBuffer;
        this.imageFormat = imageFormat;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public byte[] getImageBuffer() {
        return imageBuffer;
    }

    public int getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(int imageFormat) {
        this.imageFormat = imageFormat;
    }

    public void setImageBuffer(byte[] imageBuffer) {
        this.imageBuffer = imageBuffer;
    }
}
