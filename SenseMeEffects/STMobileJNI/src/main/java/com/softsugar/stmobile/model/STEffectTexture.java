package com.softsugar.stmobile.model;

public class STEffectTexture {
    int id;
    int width;
    int height;
    int format;

    /**
     *  \~Chinese 纹理参数
     *
     * @param id     \~Chinese 纹理id
     * @param width  \~Chinese 纹理宽度
     * @param height \~Chinese 纹理高度
     * @param format \~Chinese 纹理格式 目前仅支持RGBA
     *
     * \~English effects texture
     *
     * @param id     \~English texture id
     * @param width  \~English width
     * @param height \~English height
     * @param format \~English texture format（RGBA）
     */
    public STEffectTexture(int id, int width, int height, int format){
        this.id = id;
        this.width = width;
        this.height = height;
        this.format = format;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
