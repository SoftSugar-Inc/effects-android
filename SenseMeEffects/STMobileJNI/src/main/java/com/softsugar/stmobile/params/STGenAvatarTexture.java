package com.softsugar.stmobile.params;

public class STGenAvatarTexture {

    private int id;
    private int width;
    private int height;
    private int format;

    public STGenAvatarTexture(int id, int width, int height, int format) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.format = format;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }
}
