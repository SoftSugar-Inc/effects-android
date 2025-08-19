package com.softsugar.stmobile.engine.glutils;

import static android.opengl.GLES20.GL_NONE;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import com.softsugar.stmobile.STCommonNative;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class STPboManager {
    private final static String TAG = "STPboManager";
    public int mImageWidth, mImageHeight;
    private boolean mIsInitialized;

    public void init(int width, int height) {
        initNv21Program();
        createNv21FrameBuffer(width, height);
        initPixelBuffer(width/4, height * 3/2);
        mIsInitialized = true;
    }

    public static final String vShaderStr =
            "#version 300 es                            \n"+
                    "layout(location = 0) in vec4 a_position;   \n"+
                    "layout(location = 1) in vec2 a_texCoord;   \n"+
                    "out vec2 v_texCoord;                       \n"+
                    "void main()                                \n"+
                    "{                                          \n"+
                    "   gl_Position = a_position;               \n"+
                    "   v_texCoord = a_texCoord;                \n"+
                    "}                                          \n";


    public static final String fNv2TextureShaderStr =
            "#version 300 es\n"+
                    "precision mediump float;\n"+
                    "in vec2 v_texCoord;\n"+
                    "layout(location = 0) out vec4 outColor;\n"+
                    "uniform sampler2D s_TextureMap;\n"+
                    "uniform float u_Offset;\n"+
                    "//Y =  0.299R + 0.587G + 0.114B\n"+
                    "//U = -0.147R - 0.289G + 0.436B\n"+
                    "//V =  0.615R - 0.515G - 0.100B\n"+
                    "const vec3 COEF_Y = vec3( 0.299,  0.587,  0.114);\n"+
                    "const vec3 COEF_U = vec3(-0.147, -0.289,  0.436);\n"+
                    "const vec3 COEF_V = vec3( 0.615, -0.515, -0.100);\n"+
                    "const float UV_DIVIDE_LINE = 2.0 / 3.0;\n"+
                    "void main()\n"+
                    "{\n"+
                    "    vec2 texelOffset = vec2(u_Offset, 0.0);\n"+
                    "    if(v_texCoord.y <= UV_DIVIDE_LINE) {\n"+
                    "        vec2 texCoord = vec2(v_texCoord.x, v_texCoord.y * 3.0 / 2.0);\n"+
                    "        vec4 color0 = texture(s_TextureMap, texCoord);\n"+
                    "        vec4 color1 = texture(s_TextureMap, texCoord + texelOffset);\n"+
                    "        vec4 color2 = texture(s_TextureMap, texCoord + texelOffset * 2.0);\n"+
                    "        vec4 color3 = texture(s_TextureMap, texCoord + texelOffset * 3.0);\n"+
                    "\n"+
                    "        float y0 = dot(color0.rgb, COEF_Y);\n"+
                    "        float y1 = dot(color1.rgb, COEF_Y);\n"+
                    "        float y2 = dot(color2.rgb, COEF_Y);\n"+
                    "        float y3 = dot(color3.rgb, COEF_Y);\n"+
                    "        outColor = vec4(y0, y1, y2, y3);\n"+
                    "    }\n"+
                    "    else {\n"+
                    "        vec2 texCoord = vec2(v_texCoord.x, (v_texCoord.y - UV_DIVIDE_LINE) * 3.0);\n"+
                    "        vec4 color0 = texture(s_TextureMap, texCoord);\n"+
                    "        vec4 color1 = texture(s_TextureMap, texCoord + texelOffset);\n"+
                    "        vec4 color2 = texture(s_TextureMap, texCoord + texelOffset * 2.0);\n"+
                    "        vec4 color3 = texture(s_TextureMap, texCoord + texelOffset * 3.0);\n"+
                    "\n"+
                    "        float v0 = dot(color0.rgb, COEF_V) + 0.5;\n"+
                    "        float u0 = dot(color1.rgb, COEF_U) + 0.5;\n"+
                    "        float v1 = dot(color2.rgb, COEF_V) + 0.5;\n"+
                    "        float u1 = dot(color3.rgb, COEF_U) + 0.5;\n"+
                    "        outColor = vec4(v0, u0, v1, u1);\n"+
                    "    }\n"+
                    "}";

    //顶点坐标
    float vVertices[] = {
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            -1.0f,  1.0f, 0.0f,
            1.0f,  1.0f, 0.0f,
    };

    //fbo 纹理坐标与正常纹理方向不同，原点位于左下角
    float vFboTexCoors[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
    };
    int m_FboProgramObj = GL_NONE;
    int m_FboSamplerLoc = GL_NONE;
    int m_FboTexelOffset = GL_NONE;

    private int[] mNv21Texture;
    private int[] mNv21FrameBuffers;
    public void createNv21FrameBuffer(int width, int height){
        if(mNv21FrameBuffers == null){
            mNv21Texture = new int[1];
            mNv21FrameBuffers = new int[1];
            GLES20.glGenFramebuffers(1, mNv21FrameBuffers, 0);
            GLES20.glGenTextures(1, mNv21Texture, 0);
            bindFrameBuffer(mNv21Texture[0], mNv21FrameBuffers[0], width/4, height * 3/2);
        }
    }

    private static final int VERTEX_POS_INDX = 0;
    private static final int TEXTURE_POS_INDX = 1;
    int[] m_VaoIds = new int[1];
    int[] m_VboIds = new int[4];

    private FloatBuffer vVerticesBuffer;
    private FloatBuffer vFboTexCoorsBuffer;
    int mNv21TextureVertexPosition;
    int mNv21TextureTexturePosition = GL_NONE;
    public void initNv21Program(){
        m_FboProgramObj = OpenGLUtils.loadProgram(vShaderStr, fNv2TextureShaderStr);// 编译链接用于离屏渲染的着色器程序
        m_FboSamplerLoc = GLES20.glGetUniformLocation(m_FboProgramObj, "s_TextureMap");
        m_FboTexelOffset = GLES20.glGetUniformLocation(m_FboProgramObj, "u_Offset");
        mNv21TextureVertexPosition = GLES20.glGetAttribLocation(m_FboProgramObj, "a_position");
        mNv21TextureTexturePosition = GLES20.glGetAttribLocation(m_FboProgramObj, "a_texCoord");

        // 生成 VBO ，加载顶点数据和索引数据
        // Generate VBO Ids and load the VBOs with data
        GLES20.glGenBuffers(4, m_VboIds, 0);
        vVerticesBuffer = ByteBuffer.allocateDirect(vVertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vVerticesBuffer.clear();
        vVerticesBuffer.put(vVertices).position(0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_VboIds[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vVerticesBuffer.capacity() * 4, vVerticesBuffer, GLES20.GL_STATIC_DRAW);

        vFboTexCoorsBuffer = ByteBuffer.allocateDirect(vFboTexCoors.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vFboTexCoorsBuffer.clear();
        vFboTexCoorsBuffer.put(vFboTexCoors).position(0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_VboIds[2]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vFboTexCoorsBuffer.capacity() * 4, vFboTexCoorsBuffer, GLES20.GL_STATIC_DRAW);

        // Generate VAO Ids
        GLES30.glGenVertexArrays(1, m_VaoIds, 0);
        // 初始化用于离屏渲染的 VAO
        // FBO off screen rendering VAO
        GLES30.glBindVertexArray(m_VaoIds[0]);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_VboIds[0]);
        GLES20.glEnableVertexAttribArray(VERTEX_POS_INDX);
        GLES20.glVertexAttribPointer(VERTEX_POS_INDX, 3, GLES20.GL_FLOAT, false, 3 * 4, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, GL_NONE);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_VboIds[2]);
        GLES20.glEnableVertexAttribArray(TEXTURE_POS_INDX);
        GLES20.glVertexAttribPointer(TEXTURE_POS_INDX, 2, GLES20.GL_FLOAT, false, 2 * 4, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, GL_NONE);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, m_VboIds[3]);
        GLES30.glBindVertexArray(GL_NONE);

        Log.e(TAG, "glerror: "+GLES20.glGetError() );
    }

    public byte[] drawRgbaTextureToNv21Buffer(int rgbaTexture, int width, int height){
        if(!mIsInitialized){
            return null;
        }
        GLES30.glPixelStorei(GLES30.GL_UNPACK_ALIGNMENT,1);
        // Do FBO off screen rendering
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mNv21FrameBuffers[0]);
        // 渲染成 NV21 宽度像素变为 1/4 宽度，高度为 height * 1.5
        GLES30.glViewport(0, 0, width / 4, height * 3/2);
        GLES30.glUseProgram(m_FboProgramObj);
        GLES30.glBindVertexArray(m_VaoIds[0]);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, rgbaTexture);
        GLES30.glUniform1i(m_FboSamplerLoc, 0);
        GLES30.glUniform1f(m_FboTexelOffset, 1.f/width);
        GLES30.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES30.glBindVertexArray(0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        byte[] data = bindPixelBuffer(width / 4, height * 3/2);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);

        return data;
//        return mNv21Texture[0];
    }

    public int drawRgbaTextureToNv21Texture(int rgbaTexture, int width, int height){
        if(!mIsInitialized){
            return -1;
        }
        GLES30.glPixelStorei(GLES30.GL_UNPACK_ALIGNMENT,1);
        // Do FBO off screen rendering
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mNv21FrameBuffers[0]);
        // 渲染成 NV21 宽度像素变为 1/4 宽度，高度为 height * 1.5
        GLES30.glViewport(0, 0, width / 4, height * 3/2);
        GLES30.glUseProgram(m_FboProgramObj);
        GLES30.glBindVertexArray(m_VaoIds[0]);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, rgbaTexture);
        GLES30.glUniform1i(m_FboSamplerLoc, 0);
        GLES30.glUniform1f(m_FboTexelOffset, 1.f/width);
        GLES30.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES30.glBindVertexArray(0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);

        return mNv21Texture[0];
    }

    private boolean mIsFirstFrame = true;
    private byte[] bindPixelBuffer(int width, int height) {
        long start = System.currentTimeMillis();
        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER, mPboIds.get(mPboIndex));
        STCommonNative.glReadPixels(0, 0, mRowStride / mPixelStride, height, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE);

        if (mIsFirstFrame) {//第一帧没有数据跳出
            unbindPixelBuffer();
            mIsFirstFrame = false;
            return new byte[mRowStride * height];
        }
        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER, mPboIds.get(mPboNewIndex));

        //glMapBufferRange会等待DMA传输完成，所以需要交替使用pbo
        ByteBuffer byteBuffer = (ByteBuffer) GLES30.glMapBufferRange(GLES30.GL_PIXEL_PACK_BUFFER, 0, mPboSize, GLES30.GL_MAP_READ_BIT);
        Log.d(TAG, "pbo glReadPixels cost: "+(System.currentTimeMillis() - start) );
        long start1 = System.currentTimeMillis();
        byte[] data = new byte[mRowStride * height];
        byteBuffer.get(data);
        byteBuffer.clear();
        Log.d(TAG, "pbo getData cost: "+(System.currentTimeMillis() - start1) );

//        Bitmap imageBitmap = STUtils.NV21ToRGBABitmap(data, width*4, height * 2/3);
//        Log.e("csw", "imageBitmap: "+imageBitmap.getWidth() );

        GLES30.glUnmapBuffer(GLES30.GL_PIXEL_PACK_BUFFER);
        unbindPixelBuffer();

        return data;
    }

    public void releasePbo(){
        destroyPixelBuffers();

        if (mNv21FrameBuffers != null) {
            GLES20.glDeleteFramebuffers(1, mNv21FrameBuffers, 0);
            mNv21FrameBuffers = null;
        }

        if (mNv21Texture != null) {
            GLES20.glDeleteTextures(1, mNv21Texture, 0);
            mNv21Texture = null;
        }

        if (m_VboIds != null) {
            GLES30.glDeleteBuffers(4, m_VboIds, 0);
        }

        if (m_VaoIds != null) {
            GLES30.glDeleteVertexArrays(1, m_VaoIds, 0);
        }

        if (mOesFrameBuffer != null) {
            GLES20.glDeleteFramebuffers(2, mOesFrameBuffer, 0);
            mOesFrameBuffer = null;
        }

        if (mCopySrcFrameBuffer != null) {
            GLES20.glDeleteFramebuffers(2, mCopySrcFrameBuffer, 0);
            mCopySrcFrameBuffer = null;
        }
    }


    private IntBuffer mPboIds;
    private int mPboSize;
    private final int mPixelStride = 4;//RGBA 4字节
    private int mRowStride;//对齐4字节
    private int mPboIndex;
    private int mPboNewIndex;
    //初始化2个pbo，交替使用
    public void initPixelBuffer(int width, int height) {
        if (mPboIds != null) destroyPixelBuffers();
        if (mPboIds != null) return;

        //OpenGLES默认应该是4字节对齐应，但是不知道为什么在索尼Z2上效率反而降低
        //并且跟ImageReader最终计算出来的rowStride也和我这样计算出来的不一样，这里怀疑跟硬件和分辨率有关
        //这里默认取得128的倍数，这样效率反而高，为什么？
        final int align = 16;//128字节对齐
//        mRowStride = (width * mPixelStride + (align - 1)) & ~(align - 1);
        mRowStride = width * mPixelStride;

        mPboSize = mRowStride * height;

        mPboIds = IntBuffer.allocate(2);
        GLES30.glGenBuffers(2, mPboIds);

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER, mPboIds.get(0));
        GLES30.glBufferData(GLES30.GL_PIXEL_PACK_BUFFER, mPboSize, null, GLES30.GL_STATIC_READ);

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER, mPboIds.get(1));
        GLES30.glBufferData(GLES30.GL_PIXEL_PACK_BUFFER, mPboSize, null, GLES30.GL_STATIC_READ);

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER, 0);

        mPboIndex = 0;
        mPboNewIndex = 1;
    }

    //解绑pbo
    private void unbindPixelBuffer() {
        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER, 0);

        mPboIndex = (mPboIndex + 1) % 2;
        mPboNewIndex = (mPboNewIndex + 1) % 2;
    }

    private void destroyPixelBuffers() {
        if (mPboIds != null) {
            GLES30.glDeleteBuffers(2, mPboIds);
            mPboIds = null;
        }
    }

    private void bindFrameBuffer(int textureId, int frameBuffer, int width, int height) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,textureId, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private int[] mOesFrameBuffer;
    private int[] mCopySrcFrameBuffer;
    public void copy2DTextureToOesTexture(int srcTexture, int dstTexture, int width, int height, int index){
        if(mOesFrameBuffer == null){
            mOesFrameBuffer = new int[2];
            GLES20.glGenFramebuffers(2, mOesFrameBuffer, 0);
        }

        if(mCopySrcFrameBuffer == null){
            mCopySrcFrameBuffer = new int[2];
            GLES20.glGenFramebuffers(2, mCopySrcFrameBuffer, 0);
        }

        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mCopySrcFrameBuffer[index]);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, srcTexture);
        GLES30.glFramebufferTexture2D(GLES30.GL_READ_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, srcTexture, 0);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, mOesFrameBuffer[index]);
        GLES30.glFramebufferTexture2D(GLES30.GL_DRAW_FRAMEBUFFER,
                GLES30.GL_COLOR_ATTACHMENT0, GLES11Ext.GL_TEXTURE_EXTERNAL_OES, dstTexture, 0);
        GLES30.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_LINEAR);

        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
    }
}
