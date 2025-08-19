package com.softsugar.stmobile.engine.glutils;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.os.Build;
import android.util.Log;

import com.softsugar.hardwarebuffer.STMobileHardwareBufferNative;
import com.softsugar.stmobile.STCommonNative;
import com.softsugar.stmobile.STMobileColorConvertNative;
import com.softsugar.stmobile.model.STEffectTexture;

public class STTextureToBufferManager{
    public static final int ANDROID_MIN_HARDWAREBUFFER_VERSION = 26;

    protected STPboManager mSTPboManager = new STPboManager();
    protected STMobileHardwareBufferNative mSTMobileHardwareBufferNative;
    protected byte[][] imageDatas;
    protected STImageBuffer[] imageBuffers;
    boolean mHighApiLevel;
    private STMobileColorConvertNative mSTMobileColorConvertNative = new STMobileColorConvertNative();
    private int[] mNv21Texture;

    public void init(int width, int height){
        mHighApiLevel = Build.VERSION.SDK_INT >= ANDROID_MIN_HARDWAREBUFFER_VERSION;
        mSTPboManager.init(width, height);

        if(mSTMobileColorConvertNative == null){
            mSTMobileColorConvertNative = new STMobileColorConvertNative();
        }
        mSTMobileColorConvertNative.createInstance();

        if(mHighApiLevel){
            mSTMobileHardwareBufferNative = new STMobileHardwareBufferNative();
            mSTMobileHardwareBufferNative.init(width/4, height * 3/2, STMobileHardwareBufferNative.HARDWARE_BUFFER_FORMAT_RGBA, STMobileHardwareBufferNative.HARDWARE_BUFFER_USAGE_DOWNLOAD);
        }

        int format = STCommonNative.ST_PIX_FMT_NV21;//mHighApiLevel ? STCommonNative.ST_PIX_FMT_RGBA8888 : STCommonNative.ST_PIX_FMT_NV21;
        if(imageDatas == null){
            //imageDatas = new byte[2][width * height * (mHighApiLevel ? 4 : 3/2)];
            imageDatas = new byte[2][width * height * 3/2];
        }

        if(imageBuffers == null){
            imageBuffers = new STImageBuffer[2];
            imageBuffers[0] = new STImageBuffer(imageDatas[0], format, width, height);
            imageBuffers[1] = new STImageBuffer(imageDatas[1], format, width, height);
        }
    }

    private int[] mHardwareBufferTexture = new int[2];
    public STImageBuffer textureToNv21Buffer(int textureId, int width, int height, int bufferIndex){
        if(mSTPboManager == null) return null;

        if(mHighApiLevel){
            if(mSTMobileHardwareBufferNative == null) return null;
            mHardwareBufferTexture[bufferIndex] = mSTMobileHardwareBufferNative.getTextureId();

            if(mNv21Texture == null){
                mNv21Texture = new int[1];
                GlUtil.initEffectTexture(width/4, height *3/2, mNv21Texture, GLES20.GL_TEXTURE_2D);
            }

            STEffectTexture rgbaTexture = new STEffectTexture(textureId, width, height, STCommonNative.ST_PIX_FMT_RGBA8888);
            STEffectTexture yuvTexture = new STEffectTexture(mNv21Texture[0], width/4, height *3/2, STCommonNative.ST_PIX_FMT_RGBA8888);

            mSTMobileColorConvertNative.rgbaTextureToNv21Texture(rgbaTexture, yuvTexture);
//            int nv21Texture = mSTPboManager.drawRgbaTextureToNv21Texture(textureId, width, height);
            mSTPboManager.copy2DTextureToOesTexture(yuvTexture.getId(), mHardwareBufferTexture[bufferIndex], width/4, height * 3/2, bufferIndex);
            mSTMobileHardwareBufferNative.downloadRgbaImage(width/4, height * 3/2, imageDatas[bufferIndex]);

        }else {
            imageDatas[bufferIndex] = mSTPboManager.drawRgbaTextureToNv21Buffer(textureId, width, height);
            imageBuffers[bufferIndex].setImageBuffer(imageDatas[bufferIndex]);
        }

        imageBuffers[bufferIndex].setImageBuffer(imageDatas[bufferIndex]);
        return imageBuffers[bufferIndex];
    }

    public void release(){
        if(mSTMobileHardwareBufferNative != null){
            Log.e("csw", "mSTMobileHardwareBufferNative release: " );
            mSTMobileHardwareBufferNative.release();
            mSTMobileHardwareBufferNative = null;
        }

        mSTPboManager.releasePbo();
        imageBuffers = null;
        if (imageDatas != null) {
            imageDatas[0] = null;
            imageDatas[1] = null;
            imageDatas = null;
        }

        if(mSTMobileColorConvertNative != null){
            mSTMobileColorConvertNative.destroyInstance();
            mSTMobileColorConvertNative = null;
        }

        if(mNv21Texture != null){
            GLES30.glDeleteTextures(1, mNv21Texture, 0);
            mNv21Texture = null;
        }
    }
}
