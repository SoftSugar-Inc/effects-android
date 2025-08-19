package softsugar.senseme.com.effects.display;


import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.softsugar.stmobile.STMobileEffectParams;
import com.softsugar.stmobile.STMobileHumanActionNative;
import com.softsugar.stmobile.engine.STEffectsEngine;
import com.softsugar.stmobile.engine.STRenderMode;
import com.softsugar.stmobile.model.STEffect3DBeautyPartInfo;
import com.softsugar.stmobile.params.STEffectBeautyParams;
import com.softsugar.stmobile.params.STEffectBeautyType;
import com.softsugar.stmobile.params.STHumanActionParamsType;
import com.softsugar.stmobile.params.STSmoothMode;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import softsugar.senseme.com.effects.SenseMeApplication;
import softsugar.senseme.com.effects.activity.BaseActivity;
import softsugar.senseme.com.effects.activity.CameraActivity;
import softsugar.senseme.com.effects.camera.CameraProxy2;
import softsugar.senseme.com.effects.display.glutils.EGLContextHelper;
import softsugar.senseme.com.effects.display.glutils.STGLRender;
import softsugar.senseme.com.effects.encoder.mediacodec.utils.CollectionUtils;
import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem;
import softsugar.senseme.com.effects.entity.ModelItem;
import softsugar.senseme.com.effects.helper.MediaPlayController;
import softsugar.senseme.com.effects.state.AtyStateContext;
import softsugar.senseme.com.effects.state.CameraAtyState;
import softsugar.senseme.com.effects.state.IAtyState;
import softsugar.senseme.com.effects.utils.Constants;
import softsugar.senseme.com.effects.utils.EffectInfoDataHelper;
import softsugar.senseme.com.effects.utils.FileUtils;
import softsugar.senseme.com.effects.utils.ReadAssetsJsonFileUtils;
import softsugar.senseme.com.effects.utils.RxEventBus;
import softsugar.senseme.com.effects.view.BeautyItem;
import softsugar.senseme.com.effects.view.FilterItem;
import softsugar.senseme.com.effects.view.widget.LinkageEntity;

public abstract class BaseDisplay implements GLSurfaceView.EGLContextFactory, BaseDisplayI {
    private static final String TAG = "BaseDisplay";

    protected GLSurfaceView mGlSurfaceView;
    protected EGLContext mEglContext;
    protected EGLContextHelper mEGLContextHelper = new EGLContextHelper();
    public STModuleInfoCallback mSTModuleInfoCallback;
    protected long mDetectConfig = 0;
    protected boolean mNeedAnimalDetect = false;
    public LinkedHashMap<Integer, String> mCurrentStickerMaps = new LinkedHashMap<>();
    protected String mCurrentFilterStyle;
    protected float mCurrentFilterStrength;
    protected STGLRender mGLRender;
    protected STEffectsEngine mSTEffectsEngine;

    protected float mFilterStrength = 0.80f;
    protected String mCurrentSticker;
    protected boolean mNeedResetEglContext = false;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected Handler mHandler;
    /** @noinspection deprecation*/
    protected int mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    protected boolean mCameraChanging = false;
    public CameraProxy2 mCameraProxy;
    protected boolean needOutputHumanAction;
    private final Context mContext;
    protected boolean isActivityDestroyed = false;
    protected boolean enableOutputEffectImageBuffer;

    public BaseDisplay(GLSurfaceView glSurfaceView, Context context, String mode) {
        mContext = context;
        initEglContext();
        if (mEGLContextHelper != null) {
            mEGLContextHelper.eglMakeCurrent();
            mEglContext = mEGLContextHelper.getEGLContext();
            mEGLContextHelper.eglMakeNoCurrent();
        }
        this.mGlSurfaceView = glSurfaceView;
        glSurfaceView.setEGLContextFactory(this);
        needOutputHumanAction = false;

        initEffectsEngine(context, mode);
    }

    protected Observable<Boolean> observableLoadModels() {
        return Observable.create(emitter -> {
            String json = ReadAssetsJsonFileUtils.getJson("json_data/json_models.json");
            Type type = GsonUtils.getListType(ModelItem.class);
            List<ModelItem> modelItemList = GsonUtils.fromJson(json, type);
            String root = Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath() + File.separator;
            for (int i = 0; i < modelItemList.size(); i++) {
                String path = root + modelItemList.get(i).model_asset_path;
                mSTEffectsEngine.loadSubModel(path);
            }

            emitter.onNext(true);
            emitter.onComplete();
        });
    }

    protected void initEffectsEngine(final Context context, String mode) {
        int renderMode;
        if (mode.equals(Constants.MODE_CAMERA)) {
            renderMode = STRenderMode.PREVIEW.getMode();
        } else if(mode.equals(Constants.MODE_IMG)) {
            renderMode = STRenderMode.IMAGE.getMode();
        } else {
            renderMode = STRenderMode.VIDEO.getMode();
        }
        if (mSTEffectsEngine == null) {
            mSTEffectsEngine = new STEffectsEngine();
            mSTEffectsEngine.init(context, renderMode);
            setParam(STMobileEffectParams.EFFECT_PARAM_DISABLE_TRIGER_OVERLAP, 1);
            setParam(STMobileEffectParams.EFFECT_PARAM_MAX_MEMORY_BUDGET_MB, 1024);
        }

        // 加载通用模型->加载GAN模型->加载3D微整形素材->恢复效果
        //noinspection rawtypes,unchecked
        Disposable disposable = observableLoadModels()
                //.flatMap(ret -> observableLoad3DPlastic())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer) o -> {
                    if (mContext instanceof BaseActivity) {
                        BaseActivity aty = (BaseActivity) mContext;
                        aty.dismissLoading();
                        Log.i("lugq", "modelsLoaded");
                    }
                    RxEventBus.modelsLoaded.onNext(true);
                });
        compositeDisposable.add(disposable);
    }

    public void setWhitenFromAssetsFileSync(final String path) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        mGlSurfaceView.queueEvent(() -> {
            byte[] buffer = FileUtils.readFileFromAssets(SenseMeApplication.getContext(), path);
            mSTEffectsEngine.setBeautyFromBuffer(STEffectBeautyType.EFFECT_BEAUTY_BASE_WHITEN, buffer);
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAllStickers(boolean needRefresh) {
        if (mediaPlayController != null) mediaPlayController.releasePlayer();
        if (null == mCurrentStickerMaps) return;
        Set<Integer> integers = mCurrentStickerMaps.keySet();
        for (Integer packageId : integers) {
            mSTEffectsEngine.removeEffectById(packageId);
        }

        if (mCurrentStickerMaps != null) {
            mCurrentStickerMaps.clear();
        }
        updateHumanActionDetectConfig();
        if (needRefresh)
            refreshDisplay();
    }

    /**
     * human action detect的配置选项,根据渲染接口需要配置
     */
    public void updateHumanActionDetectConfig() {
        mDetectConfig = mSTEffectsEngine.getHumanDetectConfig();

        // isMe模式下，detect至少要保留106检测
        if (mDetectConfig == 0) {
            IAtyState state = AtyStateContext.getInstance().getState();
            if (state instanceof CameraAtyState) {
                boolean isMeModel = ((CameraAtyState) state).isMeModel;
                if (isMeModel) {
                    mDetectConfig = STMobileHumanActionNative.ST_MOBILE_FACE_DETECT;
                }
            }
        }
    }

    public void updateAnimalDetectConfig() {
        mNeedAnimalDetect = mSTEffectsEngine.getAnimalDetectConfig() > 0;
    }

    @Override
    public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig) {
        return mEglContext;
    }

    @Override
    public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
    }

    private void initEglContext() {
        try {
            mEGLContextHelper.initEGL();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mEGLContextHelper.eglMakeCurrent();
        mEglContext = mEGLContextHelper.getEGLContext();
        mEGLContextHelper.eglMakeNoCurrent();
    }

    public abstract void setShowOriginal(boolean isShow);

    public void setFilterStyle(String filterType, String filterName, String modelPath) {
        mCurrentFilterStyle = modelPath;
        int ret = mSTEffectsEngine.setBeauty(STEffectBeautyType.EFFECT_BEAUTY_FILTER, modelPath);
        refreshDisplay();
        LogUtils.iTag(TAG, "setFilterStyle: ret:" + ret);
    }

    public void setFilterStrength(float strength) {
        mFilterStrength = strength;
        mCurrentFilterStrength = strength;
        mSTEffectsEngine.setBeautyStrength(STEffectBeautyType.EFFECT_BEAUTY_FILTER, strength);
        refreshDisplay();
    }

    public abstract void enableFilter(boolean needFilter);


    public abstract void enableSave(boolean save);

    public void removeSticker(String path) {
        removeSticker(CollectionUtils.getKey(mCurrentStickerMaps, path));

        if ((mSTEffectsEngine.getHumanDetectConfig() & STMobileHumanActionNative.ST_MOBILE_NAIL_DETECT) != 0) {
            Log.i(TAG, "有指甲检测");
            mSTEffectsEngine.setDetectParams(STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_DELAY_FRAME, 2);
        } else {
            mSTEffectsEngine.setDetectParams(STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_DELAY_FRAME, 0);
            Log.i(TAG, "没有指甲检测");
        }
    }

    public void removeSticker(int packageId) {
        int result = mSTEffectsEngine.removeEffectById(packageId);

        if (mCurrentStickerMaps != null && result == 0) {
            mCurrentStickerMaps.remove(packageId);
        }
    }

    public synchronized void addSticker(String addSticker) {
        if (null == addSticker || mCurrentStickerMaps.containsValue(addSticker)) {
            return;
        }
        mCurrentSticker = addSticker;
        int stickerId = mSTEffectsEngine.addPackage(mCurrentSticker);
        if (stickerId > 0) {
            if (mCurrentStickerMaps != null) {
                CollectionUtils.removeByValue(mCurrentStickerMaps, addSticker);
                mCurrentStickerMaps.put(stickerId, mCurrentSticker);
            }
        } else if (stickerId == -33) {
            LogUtils.iTag(TAG, "addSticker: 素材包已在内存中重复加载~");
        } else if (stickerId == -8) {
            Log.i(TAG, "file error. remove file");
            com.blankj.utilcode.util.FileUtils.delete(addSticker);
        }
        updateHumanActionDetectConfig();
        updateAnimalDetectConfig();
        if (null != mHandler) {
            Message messageAdd = mHandler.obtainMessage(CameraActivity.MSG_NEED_UPDATE_STICKER_TIPS);
            mHandler.sendMessage(messageAdd);
        }
        refreshDisplay();
    }

    public abstract void refreshDisplay();

    public void replayPackage() {
        Set<Integer> integers = mCurrentStickerMaps.keySet();
        for (Integer packageId : integers) {
            mSTEffectsEngine.replayPackageById(packageId);
        }
    }

    public void setParam(int param, float value) {
        mSTEffectsEngine.setEffectParams(param, value);
    }

    public int setBeautyParam(int param, float value) {
        return mSTEffectsEngine.setBeautyParam(param, value);
    }

    public long getStickerTriggerAction() {
        return mSTEffectsEngine.getHumanTriggerActions();
    }

    public long getStickerCustomEventAction() {
        return mSTEffectsEngine.getCustomEvent();
    }

    protected MediaPlayController mediaPlayController;
    protected String mGreenSegVideoPath;

    // 重置：重置当前tab的列表
    @Override
    public void resetBaseEffect(@NonNull BasicBeautyTitleItem currentTitleData, @NotNull ArrayList<BeautyItem> dataList) {
        // 3d微整形
        if (currentTitleData.uid.equals(Constants.UID_TITLE_3D_PLASTIC)) {
            for (BeautyItem item3dPlastic:dataList) {
                set3dPlasticStrength(item3dPlastic);
                EffectInfoDataHelper.getInstance().setStrength(item3dPlastic.type, item3dPlastic.def_strength);
            }
            mGlSurfaceView.requestRender();

            return;
        }

        // 普通
        for (BeautyItem contentEntity:dataList) {
            if(!contentEntity.skip_set_when_reset && !contentEntity.no_seekbar) {
                // 设置mode (先设置mode再加载素材包)
                if (contentEntity.mode != null) {
                    mSTEffectsEngine.setBeautyMode(contentEntity.beauty_type, contentEntity.mode);
                }

                // 美白项
                if (contentEntity.beauty_type == STEffectBeautyType.EFFECT_BEAUTY_BASE_WHITEN) {
                    // 设置美白素材包
                    String beautyAssetPath = contentEntity.beauty_asset_path;
                    if (beautyAssetPath == null || !beautyAssetPath.equals(currBeautyAsset)) {
                        boolean ret = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
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
                    if (contentEntity.need_open_whiten_skin_mask) {
                        mSTEffectsEngine.setBeautyParam(STEffectBeautyParams.ENABLE_WHITEN_SKIN_MASK, 1);
                    } else {
                        mSTEffectsEngine.setBeautyParam(STEffectBeautyParams.ENABLE_WHITEN_SKIN_MASK, 0);
                    }
                }

                // 磨皮项
                if (contentEntity.beauty_type == STEffectBeautyType.EFFECT_BEAUTY_BASE_FACE_SMOOTH) {
                    mSTEffectsEngine.setBeautyMode(contentEntity.beauty_type, contentEntity.mode);
                }
                mSTEffectsEngine.setBeautyStrength(contentEntity.beauty_type, contentEntity.getProgress());

                EffectInfoDataHelper.getInstance().setStrength(contentEntity.type, contentEntity.def_strength);
                updateHumanActionDetectConfig();
                mGlSurfaceView.requestRender();
            } else {
                EffectInfoDataHelper.getInstance().setStrength(contentEntity.type, contentEntity.def_strength);
            }
        }
        mGlSurfaceView.requestRender();
    }

    @Override
    public void setFilter(LinkageEntity contentEntity, float strength) {
        FilterItem entity = (FilterItem) contentEntity;
        setFilterStyle("filter_portrait", entity.name, entity.model);
        setFilterStrength(strength);
        enableFilter(true);
    }

    @Override
    public void clearFilter() {
        setFilterStyle(null, null, null);
        enableFilter(false);
    }

    private String currBeautyAsset;

    private void set3dPlasticStrength(BeautyItem contentEntity) {
        compositeDisposable.add(Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                    STEffect3DBeautyPartInfo[] plastic3dParts = mSTEffectsEngine.get3DBeautyParts();
                    if (plastic3dParts != null) {
                        for (STEffect3DBeautyPartInfo item : plastic3dParts) {
                            if (item.getNameStr().equals(contentEntity.uid)) {
                                item.setStrength(contentEntity.getProgress());
                            }
                        }
                    }
                    int length = plastic3dParts == null ? 0 : plastic3dParts.length;
                    mSTEffectsEngine.set3dBeautyPartsStrength(plastic3dParts, length);
                    emitter.onNext(true);
                    emitter.onComplete();
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    mGlSurfaceView.requestRender();
                }));
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

        // 设置mode (先设置mode再加载素材包)
        if (contentEntity.mode != null) {
            mSTEffectsEngine.setBeautyMode(contentEntity.beauty_type, contentEntity.mode);
        }

        // 美白项
        if (contentEntity.beauty_type == STEffectBeautyType.EFFECT_BEAUTY_BASE_WHITEN) {

            // 设置美白素材包
            String beautyAssetPath = contentEntity.beauty_asset_path;
            if (beautyAssetPath == null || !beautyAssetPath.equals(currBeautyAsset)) {
                //noinspection ResultOfMethodCallIgnored
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
        }

        // 设置皮肤分割
        if (contentEntity.need_open_whiten_skin_mask!=null) {
            if (contentEntity.need_open_whiten_skin_mask) {
                mSTEffectsEngine.setBeautyParam(STEffectBeautyParams.ENABLE_WHITEN_SKIN_MASK, 1);
            } else {
                mSTEffectsEngine.setBeautyParam(STEffectBeautyParams.ENABLE_WHITEN_SKIN_MASK, 0);
            }
        }

        // 磨皮项
        if (contentEntity.beauty_type == STEffectBeautyType.EFFECT_BEAUTY_BASE_FACE_SMOOTH && contentEntity.mode != null) {
            mSTEffectsEngine.setBeautyMode(contentEntity.beauty_type, contentEntity.mode);
        }

        // 全身磨皮单独需要依赖磨皮2
        if (null != contentEntity.uid && contentEntity.uid.equals("full_body_smooth")) {
            mSTEffectsEngine.setBeautyMode(STEffectBeautyType.EFFECT_BEAUTY_BASE_FACE_SMOOTH, STSmoothMode.EFFECT_SMOOTH_FACE_DETAILED);
        }
        mSTEffectsEngine.setBeautyStrength(contentEntity.beauty_type, contentEntity.getProgress());
        updateHumanActionDetectConfig();
        mGlSurfaceView.requestRender();
    }

    @Override
    public void clearAllSticker() {
        mGreenSegVideoPath = "";
        removeAllStickers(true);
    }

    public int getCameraID() {
        return mCameraID;
    }

    public void switchCameraNew() {
        mNeedResetEglContext = true;
        //noinspection deprecation
        if (mCameraChanging || Camera.getNumberOfCameras() == 1) {
            return;
        }
        mCameraChanging = true;
        mCameraID = 1 - mCameraID;
        if (mCameraProxy != null) {
            mCameraProxy.releaseCamera();
            mGlSurfaceView.queueEvent(() -> {
                deleteTextures();
                setUpCamera();
                mCameraChanging = false;
            });
        }
    }

    protected void deleteTextures() {

    }

    protected void setUpCamera() {

    }

    protected void onDestroy() {
        isActivityDestroyed = true;

        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    protected HashMap<Integer, Float> mGreenSegmentParamsMap = new HashMap<>();

}
