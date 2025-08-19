package com.softsugar.stmobile.params;

public class STGenAvatarOutParam {
    STGenAvatarTexture tex;

    public STGenAvatarOutParam(STGenAvatarTexture tex) {
        this.tex = tex;
    }

    public STGenAvatarTexture getTex() {
        return tex;
    }

    public void setTex(STGenAvatarTexture tex) {
        this.tex = tex;
    }
}
