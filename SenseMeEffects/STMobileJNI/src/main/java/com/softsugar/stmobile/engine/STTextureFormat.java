package com.softsugar.stmobile.engine;

public enum STTextureFormat {
    FORMAT_TEXTURE_2D(0),
    FORMAT_TEXTURE_OES (1);

    private final int format;
    STTextureFormat(int expressionCode) {
        this.format = expressionCode;
    }

    public int getFormat() {
        return format;
    }
}
