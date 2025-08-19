package softsugar.senseme.com.effects.encoder.mediacodec;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import com.softsugar.stmobile.STCommonNative;
import com.softsugar.stmobile.params.STRotateType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;

import softsugar.senseme.com.effects.SenseMeApplication;
import softsugar.senseme.com.effects.encoder.mediacodec.filter.AFilter;
import softsugar.senseme.com.effects.encoder.mediacodec.filter.NoFilter;
import softsugar.senseme.com.effects.encoder.mediacodec.filter.RotationOESFilter;
import softsugar.senseme.com.effects.encoder.mediacodec.utils.EasyGlUtils;
import softsugar.senseme.com.effects.encoder.mediacodec.utils.MatrixUtils;
import softsugar.senseme.com.effects.encoder.mediacodec.utils.VideoInfo;

/**
 * Code for rendering a texture onto a surface using OpenGL ES 2.0.
 */
public class TextureRender {
    private static final String TAG = "TextureRender";
    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
    //    private final float[] mTriangleVerticesData = {//原来的纹理坐标体系是错误的
//            // X, Y, Z, U, V
//            -1.0f, -1.0f, 0, 0.f, 0.f,
//            1.0f, -1.0f, 0, 1.f, 0.f,
//            -1.0f, 1.0f, 0, 0.f, 1.f,
//            1.0f, 1.0f, 0, 1.f, 1.f,
//    };
    private final float[] mTriangleVerticesData = {
            // X, Y, Z, U, V
            -1.0f, -1.0f, 0, 0.f, 1.f,
            1.0f, -1.0f, 0, 1.f, 1.f,
            -1.0f, 1.0f, 0, 0.f, 0.f,
            1.0f, 1.0f, 0, 1.f, 0.f,
    };
    private FloatBuffer mTriangleVertices;
    private static final String VERTEX_SHADER =
            "uniform mat4 uMVPMatrix;\n" +
                    "uniform mat4 uSTMatrix;\n" +
                    "attribute vec4 aPosition;\n" +
                    "attribute vec4 aTextureCoord;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "void main() {\n" +
                    "  gl_Position = uMVPMatrix * aPosition;\n" +
                    "  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n" +
                    "}\n";
    private static final String FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +      // highp here doesn't seem to matter
                    "varying vec2 vTextureCoord;\n" +
                    "uniform samplerExternalOES sTexture;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n";
    private float[] mMVPMatrix = new float[16];
    private float[] mSTMatrix = new float[16];
    private int mProgram;
    private int mTextureID = -12345;
    private int muMVPMatrixHandle;
    private int muSTMatrixHandle;
    private int maPositionHandle;
    private int maTextureHandle;

    //======================clip========================
    float[] SM = new float[16];     //用于显示的变换矩阵
    boolean isClipMode;
    int clipViewWidth;
    int clipViewHeight;
    int clipEncodeWidth;
    int clipEncodeHeight;


    //======================zoom========================
    //创建帧缓冲区
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[2];
    AFilter mShow;
    RotationOESFilter rotationFilter;
    //第一段视频宽高(旋转后)
    int viewWidth;
    int viewHeight;
    //当前视频宽高(旋转后)
    int videoWidth;
    int videoHeight;
    //最终显示的宽高
    int width;
    int height;
    int x;
    int y;
    boolean videoChanged = false;
    //第一段视频信息
    VideoInfo info;

    private DetectAndRender mDetectAndRender;

    /**
     * 用于后台绘制的变换矩阵
     */
    private float[] OM;

    public TextureRender(VideoInfo info) {
        mDetectAndRender = new DetectAndRender();
        this.info=info;
        mTriangleVertices = ByteBuffer.allocateDirect(
                mTriangleVerticesData.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangleVertices.put(mTriangleVerticesData).position(0);
        Matrix.setIdentityM(mSTMatrix, 0);
        Resources resources = SenseMeApplication.getContext().getResources();
        mShow = new NoFilter(resources);
        mShow.setMatrix(MatrixUtils.flip(MatrixUtils.getOriginalMatrix(), false, true));
        rotationFilter = new RotationOESFilter(resources);

        mDetectAndRender.initHumanAction();
    }

    public int getTextureId() {
        return mTextureID;
    }

    public void drawFrame(SurfaceTexture st) {
        zoomDraw(st);
    }

    private ByteBuffer mBuffer;
    public void zoomDraw(SurfaceTexture st){
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        GLES20.glViewport(0, 0, viewWidth, viewHeight);
        rotationFilter.draw();
        GLES20.glFinish();
        EasyGlUtils.unBindFrameBuffer();

        if (videoChanged) {
            GLES20.glViewport(x, y, width, height);
        }

        if (mBuffer == null) {
            mBuffer = ByteBuffer.allocate(width * height * 4);
        }
        mBuffer.rewind();
        int textureFlip = mDetectAndRender.getSTGLRender().saveTextureToFrameBufferFlip(fTexture[0], mBuffer);

        textureFlip = mDetectAndRender.humanActionDetectAndRender(mBuffer.array(), STCommonNative.ST_PIX_FMT_RGBA8888, STRotateType.ST_CLOCKWISE_ROTATE_0, width, height, textureFlip);
        int texture = mDetectAndRender.getSTGLRender().textureFlip(textureFlip);

        mShow.setTextureId(texture);
        mShow.draw();

        GLES20.glFinish();
    }

    /**
     * Initializes GL state.  Call this after the EGL surface has been created and made current.
     */
    public void surfaceCreated() {
        mProgram = createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        if (mProgram == 0) {
            throw new RuntimeException("failed creating program");
        }
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        checkGlError("glGetAttribLocation aPosition");
        if (maPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        checkGlError("glGetAttribLocation aTextureCoord");
        if (maTextureHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        checkGlError("glGetUniformLocation uMVPMatrix");
        if (muMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }
        muSTMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uSTMatrix");
        checkGlError("glGetUniformLocation uSTMatrix");
        if (muSTMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uSTMatrix");
        }
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        mTextureID = textures[0];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);
        checkGlError("glBindTexture mTextureID");
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);//改为线性过滤,是画面更加平滑(抗锯齿)
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
        checkGlError("glTexParameter");

        mShow.create();
        rotationFilter.create();
        rotationFilter.setTextureId(mTextureID);

        GLES20.glGenFramebuffers(1, fFrame, 0);

        if (info.rotation == 0 || info.rotation == 180) {
            EasyGlUtils.genTexturesWithParameter(2, fTexture, 0, GLES20.GL_RGBA, info.width, info.height);
            viewWidth = info.width;
            viewHeight = info.height;
        } else {
            EasyGlUtils.genTexturesWithParameter(2, fTexture, 0, GLES20.GL_RGBA, info.height, info.width);
            viewWidth = info.height;
            viewHeight = info.width;
        }

        rotationFilter.setRotation(info.rotation);

        mDetectAndRender.initRender(viewWidth, viewHeight);
    }

    /**
     * Replaces the fragment shader.
     */
    public void changeFragmentShader(String fragmentShader) {
        GLES20.glDeleteProgram(mProgram);
        mProgram = createProgram(VERTEX_SHADER, fragmentShader);
        if (mProgram == 0) {
            throw new RuntimeException("failed creating program");
        }
    }

    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        checkGlError("glCreateShader type=" + shaderType);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e(TAG, "Could not compile shader " + shaderType + ":");
            Log.e(TAG, " " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
        return shader;
    }

    private int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        checkGlError("glCreateProgram");
        if (program == 0) {
            Log.e(TAG, "Could not create program");
        }
        GLES20.glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");
        GLES20.glAttachShader(program, pixelShader);
        checkGlError("glAttachShader");
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e(TAG, "Could not link program: ");
            Log.e(TAG, GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            program = 0;
        }
        return program;
    }

    public void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    public void onVideoSizeChanged(VideoInfo info) {

        setVideoWidthAndHeight(info);
        adjustVideoPosition();
        videoChanged = true;
    }

    public void setVideoWidthAndHeight(VideoInfo info) {
        rotationFilter.setRotation(info.rotation);
        if (info.rotation == 0 || info.rotation == 180) {
            this.videoWidth = info.width;
            this.videoHeight = info.height;
        } else {
            this.videoWidth = info.height;
            this.videoHeight = info.width;
        }
    }

    private void adjustVideoPosition() {
        float w = (float) viewWidth / videoWidth;
        float h = (float) viewHeight / videoHeight;
        if (w < h) {
            width = viewWidth;
            height = (int) ((float) videoHeight * w);
        } else {
            width = (int) ((float) videoWidth * h);
            height = viewHeight;
        }
        x = (viewWidth - width) / 2;
        y = (viewHeight - height) / 2;
    }

    public void setDetectConfig(Long config){
        if(mDetectAndRender == null){
            return;
        }
        mDetectAndRender.setDetectConfig(config);
    }

    public void setRenderOptions(boolean needBeauty, boolean needMakeup, boolean needSticker, boolean needFilter){
        if(mDetectAndRender == null){
            return;
        }
        mDetectAndRender.setRenderOptions(needBeauty, needMakeup, needSticker, needFilter);
    }

    public void setCurrentBeautyParams(float[] paramsBase, float[] paramsProfessional, float[] paramsMicro, float[] paramsAdjust){
        if(mDetectAndRender == null){
            return;
        }
        mDetectAndRender.setCurrentBeautyParams();
    }

    public void setCurrentMakeups(String[] paths, float[] strengths){
        if(mDetectAndRender == null){
            return;
        }
        mDetectAndRender.setCurrentMakeups(paths, strengths);
    }

    public void setCurrentFilter(String path, float strength){
        if(mDetectAndRender == null){
            return;
        }
        mDetectAndRender.setCurrentFilter(path, strength);
    }

    public void setCurrentStickerPath(String path){
        if(mDetectAndRender == null){
            return;
        }
        mDetectAndRender.setCurrentStickerPath(path);
    }

    public void setCurrentStickerPath(LinkedHashMap<Integer, String> map){
        if(mDetectAndRender == null){
            return;
        }
        mDetectAndRender.setCurrentStickerPath(map);
    }

    public void setGreenParamsMap(HashMap<Integer, Float> map){
        if(mDetectAndRender == null){
            return;
        }
        mDetectAndRender.setGreenParamsMap(map);
    }

    private boolean mNeedAnimalDetect = false;
    public void enableAnimalDetect(){
        mNeedAnimalDetect = true;
    }

    public void release(){
        if(mDetectAndRender == null){
            return;
        }
        mDetectAndRender.releaseHumanAction();
        mDetectAndRender.releaseRender();

        if(mBuffer != null){
            mBuffer.clear();
            mBuffer = null;
        }
    }
}