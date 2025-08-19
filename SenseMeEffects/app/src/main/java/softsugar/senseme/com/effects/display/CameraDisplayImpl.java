package softsugar.senseme.com.effects.display;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.softsugar.stmobile.engine.STEffectsEngine;
import com.softsugar.stmobile.engine.STRenderMode;
import com.softsugar.stmobile.engine.STTextureFormat;
import com.softsugar.stmobile.engine.glutils.STEffectsInput;
import com.softsugar.stmobile.engine.glutils.STEffectsOutput;
import com.softsugar.stmobile.engine.glutils.STLogUtils;
import com.softsugar.stmobile.model.STEffectTexture;
import com.softsugar.stmobile.params.STEffectBeautyParams;
import com.softsugar.stmobile.params.STEffectBeautyType;
import com.softsugar.stmobile.params.STResultCode;
import com.softsugar.stmobile.params.STRotateType;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import softsugar.senseme.com.effects.camera.CameraProxy2;
import softsugar.senseme.com.effects.display.glutils.EGLContextHelper;
import softsugar.senseme.com.effects.display.glutils.GlUtil;
import softsugar.senseme.com.effects.display.glutils.OpenGLUtils;
import softsugar.senseme.com.effects.display.glutils.STGLRender;
import softsugar.senseme.com.effects.encoder.MediaVideoEncoder;
import softsugar.senseme.com.effects.encoder.mediacodec.utils.CollectionUtils;
import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem;
import softsugar.senseme.com.effects.entity.ModelItem;
import softsugar.senseme.com.effects.utils.EffectInfoDataHelper;
import softsugar.senseme.com.effects.utils.ReadAssetsJsonFileUtils;
import softsugar.senseme.com.effects.utils.RxEventBus;
import softsugar.senseme.com.effects.view.BeautyItem;

/**
 * 独立的相机显示纹理类，负责相机预览和特效处理
 * 使用HardwareBuffer，Android API要求26及以上
 * @noinspection deprecation
 */
public class CameraDisplayImpl implements GLSurfaceView.Renderer, GLSurfaceView.EGLContextFactory, CameraDisplay {

    private static final String TAG = "CameraDisplayTexture";

    // === 核心组件 ===
    public Context mContext;
    public GLSurfaceView mGlSurfaceView;
    public CameraProxy2 mCameraProxy;
    private final STEffectsEngine mSTEffectsEngine;
    private STGLRender mGLRender;
    private EGLContextHelper mEGLContextHelper = new EGLContextHelper();
    private final EGLContext mEglContext;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    // === 相机和纹理相关 ===
    private int mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int mCameraOrientation = STRotateType.ST_CLOCKWISE_ROTATE_90;
    private int mTextureId = OpenGLUtils.NO_TEXTURE;
    private SurfaceTexture mSurfaceTexture;
    private int[] mBeautifyTextureId;

    // === 尺寸和预览相关 ===
    private int mImageWidth;
    private int mImageHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private ChangePreviewSizeListener mListener;
    private ArrayList<String> mSupportedPreviewSizes;
    private int mCurrentPreview = 0;

    // === 状态控制 ===
    private boolean mCameraChanging = false;
    private boolean mIsChangingPreviewSize = false;
    private boolean mIsPaused = false;
    private boolean mNeedSave = false;
    private boolean mNeedResetEglContext = false;

    // === 特效和美颜相关 ===
    public LinkedHashMap<Integer, String> mCurrentStickerMaps = new LinkedHashMap<>();

    // === 性能统计 ===
    private long mStartTime;
    private float mFps;
    private int mFrameCount = 0;
    private long mFpsStartTime = 0;
    private boolean mIsFirstFpsCount = true;
    private int mFrameCost = 0;

    // === 视频录制 ===
    private MediaVideoEncoder mVideoEncoder;
    private final float[] mTextureEncodeMatrix = {1f, 0f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 1f};
    private int[] mVideoEncoderTexture;

    private String currBeautyAsset;

    public CameraDisplayImpl(Context context, ChangePreviewSizeListener listener, GLSurfaceView glSurfaceView) {
        mContext = context;
        mListener = listener;
        mGlSurfaceView = glSurfaceView;
        
        // 初始化EGL上下文
        initEglContext();
        mEGLContextHelper.eglMakeCurrent();
        mEglContext = mEGLContextHelper.getEGLContext();
        mEGLContextHelper.eglMakeNoCurrent();
        
        // 初始化相机代理
        mCameraProxy = new CameraProxy2(context, glSurfaceView);
        
        // 设置GLSurfaceView
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLContextFactory(this);
        glSurfaceView.setRenderer(this);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        // 初始化GL渲染器
        mGLRender = new STGLRender();

        // 初始化特效引擎
        mSTEffectsEngine = new STEffectsEngine();
        mSTEffectsEngine.init(context, STRenderMode.PREVIEW.getMode());

        // 加载模型
        Disposable disposable = observableLoadModels()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    Log.i("CameraDisplayTexture", "Models loaded successfully");
                    RxEventBus.modelsLoaded.onNext(true);
                });
        compositeDisposable.add(disposable);
    }

    private void initEglContext() {
        try {
            mEGLContextHelper.initEGL();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Observable<Boolean> observableLoadModels() {
        return Observable.create(emitter -> {
            String json = ReadAssetsJsonFileUtils.getJson("json_data/json_models.json");
            Type type = GsonUtils.getListType(ModelItem.class);
            List<ModelItem> modelItemList = GsonUtils.fromJson(json, type);
            String root = Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath() + File.separator;
            for (ModelItem modelItem : modelItemList) {
                String path = root + modelItem.model_asset_path;
                mSTEffectsEngine.loadSubModel(path);
            }
            emitter.onNext(true);
            emitter.onComplete();
        });
    }

    // === EGLContextFactory 实现 ===
    @Override
    public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig) {
        return mEglContext;
    }

    @Override
    public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {

    }

    // === GLSurfaceView.Renderer 实现 ===
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (mIsPaused) return;
        
        GLES20.glEnable(GL10.GL_DITHER);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable(GL10.GL_DEPTH_TEST);
        
        if (mCameraProxy.getCamera() != null) {
            setUpCamera();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        STLogUtils.i(TAG, "onSurfaceChanged");
        if (mIsPaused) return;
        
        adjustViewPort(width, height);
        mGLRender.init(mImageWidth, mImageHeight);
        mStartTime = System.currentTimeMillis();
    }

    private void adjustViewPort(int width, int height) {
        mSurfaceHeight = height;
        mSurfaceWidth = width;
        GLES20.glViewport(0, 0, mSurfaceWidth, mSurfaceHeight);
        mGLRender.calculateVertexBuffer(mSurfaceWidth, mSurfaceHeight, mImageWidth, mImageHeight);
    }

    private final SurfaceTexture.OnFrameAvailableListener mOnFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            mGlSurfaceView.requestRender();
        }
    };

    private void setUpCamera() {
        mCameraChanging = true;
        mCameraProxy.openCamera(mCameraID, mImageHeight, mImageWidth, null);

        // 初始化Camera设备预览需要的显示区域
        if (mTextureId == OpenGLUtils.NO_TEXTURE) {
            mTextureId = OpenGLUtils.getExternalOESTextureID();
        }

        if (mSurfaceTexture == null) {
            mSurfaceTexture = new SurfaceTexture(mTextureId);
            mSurfaceTexture.setOnFrameAvailableListener(mOnFrameAvailableListener);
        }

        String size = mSupportedPreviewSizes.get(mCurrentPreview);
        int index = size.indexOf('x');
        mImageHeight = Integer.parseInt(size.substring(0, index));
        mImageWidth = Integer.parseInt(size.substring(index + 1));

        if (mIsPaused) return;
        
        mCameraProxy.startPreview(mSurfaceTexture);

        boolean flipHorizontal = mCameraProxy.isFlipHorizontal();
        boolean flipVertical = mCameraProxy.isFlipVertical();
        mGLRender.adjustTextureBuffer(mCameraProxy.getOrientation(), flipVertical, flipHorizontal);

        mCameraChanging = false;
        mCameraOrientation = mCameraProxy.getOrientation();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mCameraChanging || mIsChangingPreviewSize || mIsPaused || mCameraProxy.getCamera() == null) {
            return;
        }

        if (mBeautifyTextureId == null) {
            mBeautifyTextureId = new int[1];
            GlUtil.initEffectTexture(mImageWidth, mImageHeight, mBeautifyTextureId, GLES20.GL_TEXTURE_2D);
        }

        if (mVideoEncoderTexture == null) {
            mVideoEncoderTexture = new int[1];
        }

        if (mSurfaceTexture != null && !mIsPaused) {
            mSurfaceTexture.updateTexImage();
        } else {
            return;
        }

        mStartTime = System.currentTimeMillis();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        boolean needMirror = mCameraID == Camera.CameraInfo.CAMERA_FACING_FRONT;
        int format = STTextureFormat.FORMAT_TEXTURE_OES.getFormat();
        STEffectTexture effectTexture = new STEffectTexture(mTextureId, mImageHeight, mImageWidth, format);
        STEffectsInput stEffectsInput = new STEffectsInput(effectTexture, null, mCameraOrientation, mCameraOrientation, needMirror, false, 0);
        STEffectsOutput stEffectsOutput = new STEffectsOutput(mBeautifyTextureId[0], null, null);

        mSTEffectsEngine.processTexture(stEffectsInput, stEffectsOutput);

        int textureId = stEffectsOutput.getTextureId();

        if (mNeedSave) {
            savePicture(textureId);
            mNeedSave = false;
        }

        // 计算FPS
        calculateFPS();

        GLES20.glViewport(0, 0, mSurfaceWidth, mSurfaceHeight);
        mGLRender.onDrawFrame(textureId);

        // 视频录制
        if (mVideoEncoder != null) {
            GLES20.glFinish();
            mVideoEncoderTexture[0] = textureId;
            synchronized (this) {
                if (mVideoEncoder != null) {
                    if (mNeedResetEglContext) {
                        mVideoEncoder.setEglContext(EGL14.eglGetCurrentContext(), mVideoEncoderTexture[0]);
                        mNeedResetEglContext = false;
                    }
                    mVideoEncoder.frameAvailableSoon(mTextureEncodeMatrix);
                }
            }
        }
    }

    private void calculateFPS() {
        mFrameCost = (int) (System.currentTimeMillis() - mStartTime);
        long currentTime = System.currentTimeMillis();
        mFrameCount++;
        
        if (mIsFirstFpsCount) {
            mFpsStartTime = currentTime;
            mIsFirstFpsCount = false;
        } else {
            long elapsed = currentTime - mFpsStartTime;
            if (elapsed >= 1000) {
                mFps = (mFrameCount * 1000f) / elapsed;
                mFpsStartTime = currentTime;
                mFrameCount = 0;
                STLogUtils.i(TAG, "render fps: " + mFps);
            }
        }
    }

    private void savePicture(int textureId) {
        if (textureId == OpenGLUtils.NO_TEXTURE) return;

        ByteBuffer tmpBuffer = ByteBuffer.allocate(mImageHeight * mImageWidth * 4);
        int[] frameBuffers = new int[1];

        GLES20.glGenFramebuffers(1, frameBuffers, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureId, 0);
        GLES20.glReadPixels(0, 0, mImageWidth, mImageHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, tmpBuffer);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        tmpBuffer.position(0);

        if (saveImageListener != null) {
            saveImageListener.onSuccess(tmpBuffer, mImageWidth, mImageHeight);
        }
    }

    // === 生命周期管理 ===
    public void onPause() {
        mIsPaused = true;
        mCameraProxy.releaseCamera();

        mGlSurfaceView.queueEvent(() -> {
            mSTEffectsEngine.resetDetect();
            deleteTextures();
            if (mSurfaceTexture != null) {
                mSurfaceTexture.release();
                mSurfaceTexture = null;
            }
            mGLRender.destroyFrameBuffers();
            mGLRender.mViewPortWidth = 0;
            mGLRender.mViewPortHeight = 0;
        });

        mGlSurfaceView.onPause();
    }

    public void onResume() {
        if (!mCameraProxy.isCameraOpen() && mCameraProxy.getNumberOfCameras() == 1) {
            mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        mSupportedPreviewSizes = mCameraProxy.getSupportedPreviewSize(new String[]{"1280x720", "640x480", "1920x1080"});
        mIsPaused = false;
        mNeedResetEglContext = true;
        if (mGLRender == null) {
            mGLRender = new STGLRender();
        }

        mGlSurfaceView.onResume();
        mGlSurfaceView.forceLayout();
    }

    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

        if (mEGLContextHelper != null) {
            mEGLContextHelper.eglMakeCurrent();
            mSTEffectsEngine.release();
            mEGLContextHelper.eglMakeNoCurrent();
            mEGLContextHelper.release();
            mEGLContextHelper = null;
        }

        mCurrentStickerMaps.clear();
    }

    // === BaseDisplayI 接口实现 ===
    public void setShowOriginal(boolean isShow) {
        mSTEffectsEngine.setShowOrigin(isShow);
    }

    private SavePicListener saveImageListener;
    @Override
    public void setOnSaveImageListener(SavePicListener saveImageListener) {
        this.saveImageListener = saveImageListener;
    }

    @Override
    public void clearAllSticker() {
        if (mCurrentStickerMaps.isEmpty()) return;

        for (Integer packageId : mCurrentStickerMaps.keySet()) {
            mSTEffectsEngine.removeEffectById(packageId);
        }
        mCurrentStickerMaps.clear();
    }

    @Override
    public void resetBasicBeauty(@NonNull BasicBeautyTitleItem currentTitleData, @NotNull ArrayList<BeautyItem> dataList) {
        // 普通美颜
        for (BeautyItem contentEntity : dataList) {
            if (!contentEntity.skip_set_when_reset && !contentEntity.no_seekbar) {
                // 设置mode
                setBeautyModeIfNotNull(contentEntity);

                // 美白项
                if (contentEntity.beauty_type == STEffectBeautyType.EFFECT_BEAUTY_BASE_WHITEN) {
                    handleWhitenBeautyAsset(contentEntity);
                }

                // 磨皮项
                if (contentEntity.beauty_type == STEffectBeautyType.EFFECT_BEAUTY_BASE_FACE_SMOOTH) {
                    setBeautyModeIfNotNull(contentEntity);
                }
                mSTEffectsEngine.setBeautyStrength(contentEntity.beauty_type, contentEntity.getProgress());
                EffectInfoDataHelper.getInstance().setStrength(contentEntity.type, contentEntity.def_strength);

            } else {
                EffectInfoDataHelper.getInstance().setStrength(contentEntity.type, contentEntity.def_strength);
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void setBasicBeauty(BasicBeautyTitleItem titleData, @NonNull BeautyItem contentEntity, float progress) {
        // 互斥处理
        compositeDisposable.add(Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            int[] otherArr = contentEntity.other_mutual_beauty_type;
            if (otherArr != null) {
                for (int arr : otherArr) {
                    mSTEffectsEngine.setBeautyStrength(arr, 0);
                }
            }
            emitter.onNext(true);
            emitter.onComplete();
        }).subscribe());

        // 设置mode
        setBeautyModeIfNotNull(contentEntity);

        // 美白项
        if (contentEntity.beauty_type == STEffectBeautyType.EFFECT_BEAUTY_BASE_WHITEN) {
            handleWhitenBeautyAsset(contentEntity);
        }

        // 磨皮项
        if (contentEntity.beauty_type == STEffectBeautyType.EFFECT_BEAUTY_BASE_FACE_SMOOTH) {
            setBeautyModeIfNotNull(contentEntity);
        }

        mSTEffectsEngine.setBeautyStrength(contentEntity.beauty_type, contentEntity.getProgress());
    }

    // === 特效和美颜相关方法 ===

    public void setFilter(String modelPath) {
        int ret = mSTEffectsEngine.setBeauty(STEffectBeautyType.EFFECT_BEAUTY_FILTER, modelPath);
        LogUtils.iTag(TAG, "setFilterStyle: ret:" + ret);
    }

    public void setFilterStrength(float strength) {
        mSTEffectsEngine.setBeautyStrength(STEffectBeautyType.EFFECT_BEAUTY_FILTER, strength);
    }

    // === 贴纸相关方法 ===
    public synchronized void addSticker(String addSticker) {
        if (addSticker == null || mCurrentStickerMaps.containsValue(addSticker)) {
            return;
        }
        
        int stickerId = mSTEffectsEngine.addPackage(addSticker);
        if (stickerId > 0) {
            CollectionUtils.removeByValue(mCurrentStickerMaps, addSticker);
            mCurrentStickerMaps.put(stickerId, addSticker);
        } else if (stickerId == STResultCode.ST_E_INVALID_FILE_FORMAT.getResultCode()) {
            Log.i(TAG, "file error. remove file");
            com.blankj.utilcode.util.FileUtils.delete(addSticker);
        }
    }

    public void removeSticker(String path) {
        removeSticker(CollectionUtils.getKey(mCurrentStickerMaps, path));
    }

    public void removeSticker(int packageId) {
        int result = mSTEffectsEngine.removeEffectById(packageId);
        if (result == 0) {
            mCurrentStickerMaps.remove(packageId);
        }
    }

    public void replayPackage() {
        for (Integer packageId : mCurrentStickerMaps.keySet()) {
            mSTEffectsEngine.replayPackageById(packageId);
        }
    }

    // === 相机控制相关方法 ===
    public void switchCamera() {
        if (Camera.getNumberOfCameras() == 1 || mCameraChanging) {
            return;
        }
        mCameraID = 1 - mCameraID;
        mCameraChanging = true;

        mGlSurfaceView.queueEvent(() -> {
            deleteCameraPreviewTexture();
            if (mSurfaceTexture != null) {
                mSurfaceTexture.release();
                mSurfaceTexture = null;
            }
            mTextureId = OpenGLUtils.NO_TEXTURE;
            setUpCamera();
            mCameraChanging = false;
        });
        mGlSurfaceView.requestRender();
        mCameraOrientation = mCameraProxy.getOrientation();
    }

    @Override
    public void startCamera() {
        mCameraProxy.startPreview();
    }

    public void changePreviewSize(int currentPreview) {
        if (mCameraProxy.getCamera() == null || mCameraChanging || mIsPaused) {
            return;
        }

        mCurrentPreview = currentPreview;
        mCameraChanging = true;
        mCameraProxy.releaseCamera();

        String size = mSupportedPreviewSizes.get(currentPreview);
        int index = size.indexOf('x');
        mImageHeight = Integer.parseInt(size.substring(0, index));
        mImageWidth = Integer.parseInt(size.substring(index + 1));
        
        mGlSurfaceView.queueEvent(() -> {
            mGLRender.init(mImageWidth, mImageHeight);
            setUpCamera();
            mGLRender.calculateVertexBuffer(mSurfaceWidth, mSurfaceHeight, mImageWidth, mImageHeight);
            if (mListener != null) {
                mListener.onChangePreviewSize(mImageHeight, mImageWidth);
            }
            mCameraChanging = false;
            mIsChangingPreviewSize = false;
            mGlSurfaceView.requestRender();
        });
    }

    // === 视频录制相关方法 ===
    public void setVideoEncoder(final MediaVideoEncoder encoder) {
        mGlSurfaceView.queueEvent(() -> {
            synchronized (this) {
                if (encoder != null && mVideoEncoderTexture != null) {
                    encoder.setEglContext(EGL14.eglGetCurrentContext(), mVideoEncoderTexture[0]);
                }
                mVideoEncoder = encoder;
            }
        });
    }

    // === 工具方法 ===
    private void setBeautyModeIfNotNull(BeautyItem contentEntity) {
        if (contentEntity.mode != null) {
            mSTEffectsEngine.setBeautyMode(contentEntity.beauty_type, contentEntity.mode);
        }
    }

    @SuppressLint("CheckResult")
    private void handleWhitenBeautyAsset(BeautyItem contentEntity) {
        String beautyAssetPath = contentEntity.beauty_asset_path;
        if (beautyAssetPath == null || !beautyAssetPath.equals(currBeautyAsset)) {
            Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                String path = null;
                if (beautyAssetPath != null) {
                    path = mContext.getExternalFilesDir(null) + File.separator + "whiten_assets" + "/" + contentEntity.beauty_asset_path;
                }
                mSTEffectsEngine.setBeautyFromSDPath(contentEntity.beauty_type, path);
                emitter.onNext(true);
                emitter.onComplete();
            }).blockingFirst();
        }
        currBeautyAsset = beautyAssetPath;

        // 设置皮肤分割
        Boolean needOpenWhitenSkinMask = contentEntity.need_open_whiten_skin_mask;
        if (needOpenWhitenSkinMask != null) {
            mSTEffectsEngine.setBeautyParam(STEffectBeautyParams.ENABLE_WHITEN_SKIN_MASK, needOpenWhitenSkinMask ? 1 : 0);
        }
    }

    protected void deleteTextures() {
        if (mBeautifyTextureId != null) {
            GLES20.glDeleteTextures(1, mBeautifyTextureId, 0);
            mBeautifyTextureId = null;
        }
        deleteCameraPreviewTexture();
    }

    private void deleteCameraPreviewTexture() {
        if (mTextureId != OpenGLUtils.NO_TEXTURE) {
            GLES20.glDeleteTextures(1, new int[]{mTextureId}, 0);
        }
        mTextureId = OpenGLUtils.NO_TEXTURE;
    }

    public void setSaveImage() {
        mNeedSave = true;
    }

    // === Getter 方法 ===
    public int getPreviewWidth() {
        return mImageWidth;
    }

    public int getPreviewHeight() {
        return mImageHeight;
    }

    public int getFrameCost() {
        return mFrameCost;
    }

    public float getFpsInfo() {
        return (float) (Math.round(mFps * 10)) / 10;
    }
}