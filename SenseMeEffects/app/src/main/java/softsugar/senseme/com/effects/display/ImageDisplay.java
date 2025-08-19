package softsugar.senseme.com.effects.display;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.SensorEvent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.softsugar.stmobile.STCommonNative;
import com.softsugar.stmobile.engine.glutils.STEffectsInput;
import com.softsugar.stmobile.engine.glutils.STEffectsOutput;
import com.softsugar.stmobile.model.STEffectTexture;
import com.softsugar.stmobile.sticker_module_types.STCustomEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import softsugar.senseme.com.effects.SenseMeApplication;
import softsugar.senseme.com.effects.activity.ImageActivity;
import softsugar.senseme.com.effects.display.glutils.GlUtil;
import softsugar.senseme.com.effects.display.glutils.ImageInputRender;
import softsugar.senseme.com.effects.display.glutils.OpenGLUtils;
import softsugar.senseme.com.effects.display.glutils.TextureRotationUtil;
import softsugar.senseme.com.effects.encoder.mediacodec.utils.CollectionUtils;
import softsugar.senseme.com.effects.utils.Constants;

public class ImageDisplay extends BaseDisplay implements Renderer {
    private static final String TAG = "ImageDisplay";

    protected Bitmap mOriginBitmap;

    protected int mImageWidth;
    protected int mImageHeight;
    protected int mDisplayWidth;
    protected int mDisplayHeight;

    protected int mLastBeautyOverlapCount = -1;

    protected Context mContext;
    protected final FloatBuffer mVertexBuffer;
    protected final FloatBuffer mTextureBuffer;
    protected ImageInputRender mImageInputRender;
    protected boolean mInitialized = false;
    protected long mFrameCostTime = 0;
    protected Bitmap mProcessedImage;
    protected boolean mNeedSave = false;

    protected CostChangeListener mCostListener;

    protected boolean mNeedBeautify = false;
    protected boolean mNeedSticker = true;
    protected boolean mNeedFilter = true;
    protected boolean mNeedMakeup = false;
    protected int[] mBeautifyTextureId;
    protected int[] mFilterTextureOutId;

    protected boolean mShowOriginal = false;

    protected static final int MESSAGE_NEED_CHANGE_STICKER = 1001;
    protected static final int MESSAGE_NEED_REMOVE_STICKER = 1004;
    protected static final int MESSAGE_NEED_REMOVEALL_STICKERS = 1005;

    protected HandlerThread mChangeStickerManagerThread;
    protected Handler mChangeStickerManagerHandler;

    protected int mCustomEvent = 0;
    protected SensorEvent mSensorEvent;

    protected int animalFaceLlength = 0;

    protected String[] mCurrentMakeup = new String[Constants.MAKEUP_TYPE_COUNT];
    protected float[] mMakeupStrength = new float[Constants.MAKEUP_TYPE_COUNT];
    protected boolean DEBUG = false;

    /**
     * SurfaceTexureid
     */
    protected int mTextureId = OpenGLUtils.NO_TEXTURE;

    public ImageDisplay(Context context, GLSurfaceView glSurfaceView, Handler handler, String mode) {
        super(glSurfaceView, context, mode);

        mImageInputRender = new ImageInputRender();

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLContextFactory(this);
        glSurfaceView.setRenderer(this);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mHandler = handler;
        mContext = context;

        mVertexBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.CUBE.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexBuffer.put(TextureRotationUtil.CUBE).position(0);

        mTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTextureBuffer.put(TextureRotationUtil.TEXTURE_NO_ROTATION).position(0);
        initFaceAttribute();
        initHandlerManager();
        mSTModuleInfoCallback = new STModuleInfoCallback(mContext, mSTEffectsEngine);
    }

    protected void initHandlerManager() {
        mChangeStickerManagerThread = new HandlerThread("ChangeStickerManagerThread");
        mChangeStickerManagerThread.start();
        mChangeStickerManagerHandler = new Handler(mChangeStickerManagerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MESSAGE_NEED_CHANGE_STICKER:
                        String sticker = (String) msg.obj;
                        mCurrentSticker = sticker;
                        int packageId1 = mSTEffectsEngine.addPackage(mCurrentSticker);
                        if (packageId1 > 0) {
                            mLastBeautyOverlapCount = -1;
                            CollectionUtils.removeByValue(mCurrentStickerMaps, sticker);
                            mCurrentStickerMaps.put(packageId1, sticker);
                        }
                        updateHumanActionDetectConfig();
                        updateAnimalDetectConfig();
                        refreshDisplay();
                        break;
                    case MESSAGE_NEED_REMOVE_STICKER:
                        int packageId = (int) msg.obj;
                        int result = mSTEffectsEngine.removeEffectById(packageId);

                        if (mCurrentStickerMaps != null && result == 0) {
                            mCurrentStickerMaps.remove(packageId);
                        }
                        updateHumanActionDetectConfig();
                        break;
                    case MESSAGE_NEED_REMOVEALL_STICKERS:
                        if (mCurrentStickerMaps != null) {
                            mCurrentStickerMaps.clear();
                        }
                        mSTEffectsEngine.clearAll();
                        updateHumanActionDetectConfig();
                        refreshDisplay();
                        break;
                    case 1006:
                        String addSticker = (String) msg.obj;
                        mCurrentSticker = addSticker;
                        int stickerId = mSTEffectsEngine.addPackage(mCurrentSticker);
                        if (stickerId > 0) {
                            if (mCurrentStickerMaps != null) {
                                mCurrentStickerMaps.put(stickerId, mCurrentSticker);
                            }
                        } else {
                            Toast.makeText(SenseMeApplication.getContext(), "添加太多贴纸了", Toast.LENGTH_SHORT).show();
                        }
                        updateHumanActionDetectConfig();
                        updateAnimalDetectConfig();
                        refreshDisplay();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    protected void initFaceAttribute() {
    }

    public void enableBeautify(boolean needBeautify) {
        mNeedBeautify = needBeautify;
    }

    public void enableFilter(boolean needFilter) {
        mNeedFilter = needFilter;
        if (!needFilter) {
            refreshDisplay();
        }
    }

    public void changeSticker(String sticker) {
        mChangeStickerManagerHandler.removeMessages(MESSAGE_NEED_CHANGE_STICKER);
        Message msg = mChangeStickerManagerHandler.obtainMessage(MESSAGE_NEED_CHANGE_STICKER);
        msg.obj = sticker;

        mChangeStickerManagerHandler.sendMessage(msg);
    }

    public void removeSticker(String path) {
        removeSticker(CollectionUtils.getKey(mCurrentStickerMaps, path));
    }

    public void removeSticker(int packageId) {
        mChangeStickerManagerHandler.removeMessages(MESSAGE_NEED_REMOVE_STICKER);
        Message msg = mChangeStickerManagerHandler.obtainMessage(MESSAGE_NEED_REMOVE_STICKER);
        msg.obj = packageId;
        mChangeStickerManagerHandler.sendMessage(msg);
        refreshDisplay();
    }

    public void enableSave(boolean save) {
        mNeedSave = save;
        refreshDisplay();
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glDisable(GL10.GL_DITHER);
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glDisable(GL10.GL_CULL_FACE);

        mImageInputRender.init();

        updateHumanActionDetectConfig();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mDisplayWidth = width;
        mDisplayHeight = height;
        adjustImageDisplaySize();
        mInitialized = true;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        long frameStartTime = System.currentTimeMillis();
        if (!mInitialized)
            return;
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        int textureId = OpenGLUtils.NO_TEXTURE;

        if (mOriginBitmap != null && mTextureId == OpenGLUtils.NO_TEXTURE) {
            mTextureId = OpenGLUtils.loadTexture(mOriginBitmap, OpenGLUtils.NO_TEXTURE);
            textureId = mTextureId;
        } else if (mTextureId != OpenGLUtils.NO_TEXTURE) {
            textureId = mTextureId;
        } else {
            return;
        }

        if (mBeautifyTextureId == null) {
            mBeautifyTextureId = new int[1];
            GlUtil.initEffectTexture(mImageWidth, mImageHeight, mBeautifyTextureId, GLES20.GL_TEXTURE_2D);
        }

        if (mOriginBitmap != null) {
            if (!mShowOriginal && mSTEffectsEngine != null) {
                STEffectTexture effectTexture = new STEffectTexture(mTextureId, mImageWidth,mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);
                STEffectsInput stEffectsInput = new STEffectsInput(effectTexture, null, 0, 0, false, false,STCommonNative.ST_PIX_FMT_RGBA8888);

                STEffectsOutput stEffectsOutput = new STEffectsOutput(mBeautifyTextureId[0], null, null);
                mSTEffectsEngine.processTexture(stEffectsInput, stEffectsOutput);

                textureId = stEffectsOutput.getTextureId();
                GLES20.glViewport(0, 0, mDisplayWidth, mDisplayHeight);

                mImageInputRender.onDrawFrame(textureId, mVertexBuffer, mTextureBuffer);
            } else {
                mImageInputRender.onDisplaySizeChanged(mDisplayWidth, mDisplayHeight);
                mImageInputRender.onDrawFrame(mTextureId, mVertexBuffer, mTextureBuffer);
            }
            GLES20.glFinish();
        }

        mFrameCostTime = System.currentTimeMillis() - frameStartTime;
        LogUtils.i(TAG, "image onDrawFrame, the time for frame process is " + (System.currentTimeMillis() - frameStartTime));

        if (mCostListener != null) {
            mCostListener.onCostChanged((int) mFrameCostTime);
        }
        if (mNeedSave) {
            textureToBitmap(textureId);
            mNeedSave = false;
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled())
            return;
        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();
        mOriginBitmap = bitmap;
        adjustImageDisplaySize();
        refreshDisplay();
    }

    public void setShowOriginal(boolean isShow) {
        mShowOriginal = isShow;
        refreshDisplay();
    }

    public void refreshDisplay() {
        LogUtils.iTag(TAG, "refreshDisplay() called");
        mGlSurfaceView.requestRender();
        updateHumanActionDetectConfig();
    }

    public void onResume() {
        mGlSurfaceView.onResume();
        if (mNeedFilter) {
            mCurrentFilterStyle = null;
        }
    }

    public void onPause() {
        mGlSurfaceView.queueEvent(this::deleteTextures);
        mGlSurfaceView.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
        if (mEGLContextHelper != null) {
            mEGLContextHelper.eglMakeCurrent();
            mSTEffectsEngine.release();
            mEGLContextHelper.eglMakeNoCurrent();

            mEGLContextHelper.release();
            mEGLContextHelper = null;
        }
        mChangeStickerManagerThread.quitSafely();
        if (mCurrentStickerMaps != null) {
            mCurrentStickerMaps.clear();
            mCurrentStickerMaps = null;
        }
    }

    protected void adjustImageDisplaySize() {
        float ratio1 = (float) mDisplayWidth / mImageWidth;
        float ratio2 = (float) mDisplayHeight / mImageHeight;
        float ratioMax = Math.max(ratio1, ratio2);
        int imageWidthNew = Math.round(mImageWidth * ratioMax);
        int imageHeightNew = Math.round(mImageHeight * ratioMax);

        float ratioWidth = imageWidthNew / (float) mDisplayWidth;
        float ratioHeight = imageHeightNew / (float) mDisplayHeight;

        float[] cube = new float[]{
                TextureRotationUtil.CUBE[0] / ratioHeight, TextureRotationUtil.CUBE[1] / ratioWidth,
                TextureRotationUtil.CUBE[2] / ratioHeight, TextureRotationUtil.CUBE[3] / ratioWidth,
                TextureRotationUtil.CUBE[4] / ratioHeight, TextureRotationUtil.CUBE[5] / ratioWidth,
                TextureRotationUtil.CUBE[6] / ratioHeight, TextureRotationUtil.CUBE[7] / ratioWidth,
        };
        mVertexBuffer.clear();
        mVertexBuffer.put(cube).position(0);
    }

    protected void textureToBitmap(int textureId) {
        int[] mFrameBuffers = new int[1];
        if (textureId == OpenGLUtils.NO_TEXTURE) {
            return;
        }
        ByteBuffer mTmpBuffer = ByteBuffer.allocate(mImageHeight * mImageWidth * 4);
        if (textureId != OpenGLUtils.NO_TEXTURE) {
            GLES20.glGenFramebuffers(1, mFrameBuffers, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureId, 0);
        }
        GLES20.glReadPixels(0, 0, mImageWidth, mImageHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, mTmpBuffer);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        mProcessedImage = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
        mProcessedImage.copyPixelsFromBuffer(mTmpBuffer);

        if (mFrameBuffers != null) {
            GLES20.glDeleteFramebuffers(1, mFrameBuffers, 0);
            mFrameBuffers = null;
        }

        Message msg = Message.obtain(mHandler);
        msg.what = ImageActivity.MSG_SAVING_IMG;
        msg.sendToTarget();
    }

    public Bitmap getBitmap() {
        return mProcessedImage;
    }

    protected void deleteTextures() {
        if (mTextureId != OpenGLUtils.NO_TEXTURE)
            mGlSurfaceView.queueEvent(() -> {
                GLES20.glDeleteTextures(1, new int[]{mTextureId}, 0);
                mTextureId = OpenGLUtils.NO_TEXTURE;
                if (mBeautifyTextureId != null) {
                    GLES20.glDeleteTextures(1, mBeautifyTextureId, 0);
                    mBeautifyTextureId = null;
                }
                if (mFilterTextureOutId != null) {
                    GLES20.glDeleteTextures(1, mFilterTextureOutId, 0);
                    mFilterTextureOutId = null;
                }
            });
    }

    public interface CostChangeListener {
        void onCostChanged(int value);
    }

    public void setCostChangeListener(CostChangeListener listener) {
        mCostListener = listener;
    }

    public void changeCustomEvent(boolean doubleClick) {
        mCustomEvent = mSTEffectsEngine.getCustomEvent();
        if (doubleClick) {
            mCustomEvent &= ~STCustomEvent.ST_CUSTOM_EVENT_SCREEN_TAP;
        } else {
            mCustomEvent &= ~STCustomEvent.ST_CUSTOM_EVENT_SCREEN_DOUBLE_TAP;
        }
        mSTEffectsEngine.setCustomEvent(mCustomEvent);
        mGlSurfaceView.requestRender();
    }

    public void setSensorEvent(SensorEvent event) {
        mSensorEvent = event;
    }

}
