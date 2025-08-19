package com.softsugar.stmobile.engine.glutils;

import android.opengl.GLES20;
import android.opengl.GLES30;

import java.nio.ByteBuffer;

public class STSplitTextureManager {
    private final static String TAG = "STBlitFrameBufferManager";
    private int[] mSmallFrameBuffer;
    private int[] mLargeFrameBuffer;

    public void init() {
        if(mSmallFrameBuffer == null){
            mSmallFrameBuffer = new int[2];
            GLES30.glGenFramebuffers(2, mSmallFrameBuffer, 0);
        }

        if(mLargeFrameBuffer == null){
            mLargeFrameBuffer = new int[2];
            GLES30.glGenFramebuffers(2, mLargeFrameBuffer, 0);
        }
    }

    public void release(){
        if (mSmallFrameBuffer != null) {
            GLES30.glDeleteFramebuffers(2, mSmallFrameBuffer, 0);
            mSmallFrameBuffer = null;
        }

        if (mLargeFrameBuffer != null) {
            GLES30.glDeleteFramebuffers(2, mLargeFrameBuffer, 0);
            mLargeFrameBuffer = null;
        }
    }

    public void splitTexture(int srcTexture, int dstTextureLeft, int dstTextureRight, int width, int height, ByteBuffer bufferLeft, ByteBuffer bufferRight){
        if(mSmallFrameBuffer == null){
            mSmallFrameBuffer = new int[2];
            GLES30.glGenFramebuffers(2, mSmallFrameBuffer, 0);
        }

        if(mLargeFrameBuffer == null){
            mLargeFrameBuffer = new int[2];
            GLES30.glGenFramebuffers(2, mLargeFrameBuffer, 0);
        }

        //left
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mLargeFrameBuffer[0]);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, srcTexture);
        GLES30.glFramebufferTexture2D(GLES30.GL_READ_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, srcTexture, 0);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, mSmallFrameBuffer[0]);
        GLES30.glFramebufferTexture2D(GLES30.GL_DRAW_FRAMEBUFFER,
                GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, dstTextureLeft, 0);
        GLES30.glBlitFramebuffer(0, 0, width/2, height, 0, 0, width/2, height, GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_LINEAR);
        if(bufferLeft != null){
            GLES20.glReadPixels(0, 0, width/2, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bufferLeft);
        }

        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        //right
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mLargeFrameBuffer[0]);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, srcTexture);
        GLES30.glFramebufferTexture2D(GLES30.GL_READ_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, srcTexture, 0);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, mSmallFrameBuffer[1]);
        GLES30.glFramebufferTexture2D(GLES30.GL_DRAW_FRAMEBUFFER,
                GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, dstTextureRight, 0);
        GLES30.glBlitFramebuffer(width/2, 0, width, height, 0, 0, width/2, height, GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_LINEAR);
        if(bufferRight != null){
            GLES20.glReadPixels(width/2, 0, width/2, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bufferRight);
        }

        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
    }

    public void mergeTexture(int dstTexture, int srcTextureLeft, int srcTextureRight, int width, int height){
        if(mSmallFrameBuffer == null){
            mSmallFrameBuffer = new int[2];
            GLES30.glGenFramebuffers(2, mSmallFrameBuffer, 0);
        }

        if(mLargeFrameBuffer == null){
            mLargeFrameBuffer = new int[2];
            GLES30.glGenFramebuffers(2, mLargeFrameBuffer, 0);
        }

        //left
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mSmallFrameBuffer[0]);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, srcTextureLeft);
        GLES30.glFramebufferTexture2D(GLES30.GL_READ_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, srcTextureLeft, 0);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, mLargeFrameBuffer[1]);
        GLES30.glFramebufferTexture2D(GLES30.GL_DRAW_FRAMEBUFFER,
                GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, dstTexture, 0);
        GLES30.glBlitFramebuffer(0, 0, width/2, height, 0, 0, width/2, height, GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_LINEAR);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        //right
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mSmallFrameBuffer[1]);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, srcTextureRight);
        GLES30.glFramebufferTexture2D(GLES30.GL_READ_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, srcTextureRight, 0);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, mLargeFrameBuffer[1]);
        GLES30.glFramebufferTexture2D(GLES30.GL_DRAW_FRAMEBUFFER,
                GLES30.GL_COLOR_ATTACHMENT1, GLES30.GL_TEXTURE_2D, dstTexture, 0);
        GLES30.glBlitFramebuffer(0, 0, width/2, height, width/2, 0, width, height, GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_LINEAR);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
    }
}
