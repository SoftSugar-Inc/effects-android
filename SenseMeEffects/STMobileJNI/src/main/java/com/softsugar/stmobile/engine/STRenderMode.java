package com.softsugar.stmobile.engine;

public enum STRenderMode {
    PREVIEW (0),
    IMAGE (1),
    VIDEO (2),
    VIDEO_POST_PROCESS (3);

    private final int mode;
    STRenderMode(int expressionCode) {
        this.mode = expressionCode;
    }

    public int getMode() {
        return mode;
    }
}
