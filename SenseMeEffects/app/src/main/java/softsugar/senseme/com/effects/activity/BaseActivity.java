package softsugar.senseme.com.effects.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.StickerTitleEntity;
import softsugar.senseme.com.effects.databinding.ActivityCameraBinding;
import softsugar.senseme.com.effects.db.DBManager;
import softsugar.senseme.com.effects.db.entity.DBBeautyEntity;
import softsugar.senseme.com.effects.db.entity.DBFilterEntity;
import softsugar.senseme.com.effects.display.BaseDisplay;
import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem;
import softsugar.senseme.com.effects.entity.FilterTitleItem;
import softsugar.senseme.com.effects.state.AtyStateContext;
import softsugar.senseme.com.effects.state.CameraAtyState;
import softsugar.senseme.com.effects.state.ImgAtyState;
import softsugar.senseme.com.effects.state.TryOnCameraAtyState;
import softsugar.senseme.com.effects.state.TryOnImgAtyState;
import softsugar.senseme.com.effects.state.TryOnVideoAtyState;
import softsugar.senseme.com.effects.state.VideoAtyState;
import softsugar.senseme.com.effects.utils.Accelerometer;
import softsugar.senseme.com.effects.utils.CollectionSortUtils;
import softsugar.senseme.com.effects.utils.ContextHolder;
import softsugar.senseme.com.effects.utils.EffectInfoDataHelper;
import softsugar.senseme.com.effects.utils.LocalDataStore;
import softsugar.senseme.com.effects.utils.MultiLanguageUtils;
import softsugar.senseme.com.effects.utils.RxEventBus;
import softsugar.senseme.com.effects.utils.STUtils;
import softsugar.senseme.com.effects.view.BeautyItem;
import softsugar.senseme.com.effects.view.StickerItem;
import softsugar.senseme.com.effects.view.widget.BasicEffectView;
import softsugar.senseme.com.effects.view.widget.BottomMenuView;
import softsugar.senseme.com.effects.view.widget.CameraActionBarView;
import softsugar.senseme.com.effects.view.widget.CameraLayout;
import softsugar.senseme.com.effects.view.widget.DebugInfoView;
import softsugar.senseme.com.effects.view.widget.EffectType;
import softsugar.senseme.com.effects.view.widget.FilterView;
import softsugar.senseme.com.effects.view.widget.LinkageEntity;
import softsugar.senseme.com.effects.view.widget.LoadingDialog;
import softsugar.senseme.com.effects.view.widget.RecordView;
import softsugar.senseme.com.effects.view.widget.SlideTextView;
import softsugar.senseme.com.effects.view.widget.StickerView;
import softsugar.senseme.com.effects.view.widget.TipToast;

@SuppressLint("NonConstantResourceId")
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, CameraActionBarView.Listener, SlideTextView.Listener, BottomMenuView.Listener, RecordView.Listener, FilterView.Listener, View.OnTouchListener, BasicEffectView.Listener {
    private static final String TAG = "BaseActivity";

    protected ActivityCameraBinding mBinding;
    public static final String EXTRA_EFFECT_TYPE = "extra_effect_type";
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final DBManager dbManager = new DBManager();

    protected Bitmap mGuideBitmap;
    protected EffectType mEffectType;
    protected BaseDisplay mBaseDisplay;
    protected Accelerometer mAccelerometer = null;
    private Vibrator vibrator;

    protected DebugInfoView view_debug;

    public static void actionStart(Activity aty, EffectType type) {
        Intent intent = aty.getIntent();
        intent.putExtra(EXTRA_EFFECT_TYPE, type);
        intent.setClass(aty, CameraActivity.class);
        aty.startActivity(intent);
    }

    private void getExtraData() {
        mEffectType = (EffectType) getIntent().getSerializableExtra(EXTRA_EFFECT_TYPE);
        if (mEffectType != null) LogUtils.iTag(TAG, "getExtraData: " + mEffectType.name());
    }

    @Override
    protected void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccelerometer = new Accelerometer(getApplicationContext());
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        LogUtils.i("BaseActivity- onCreate -----");
        getExtraData();
        long startSetContentViewTime = System.currentTimeMillis();
        mBinding = ActivityCameraBinding.inflate(LayoutInflater.from(this));
        setContentView(mBinding.getRoot());

        LogUtils.i("BaseActivity setContentView cost time:" + (System.currentTimeMillis() - startSetContentViewTime));

        long time2 = System.currentTimeMillis();
        LogUtils.i("BaseActivity 1 cost time:" + (System.currentTimeMillis() - time2));
        addObserver();
        setBaseDisplay();
        initView();
        initData();
        initListener();
        AtyStateContext.getInstance().initView(this);
        LogUtils.i("BaseActivity cost time:" + (System.currentTimeMillis() - time2));
    }

    public void initViewForTryOnState() {
    }

    private void addObserver() {
        getLifecycle().addObserver(mBinding.layoutCamera);
    }

    public void initListener() {
        compositeDisposable.add(RxEventBus.vibrator.subscribe(aBoolean -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(50);
            }
        }));
        mBinding.layoutCamera.getStickerView().setMListener(new StickerView.Listener() {
            @Override
            public void onItemClickSticker(int position, @androidx.annotation.Nullable StickerTitleEntity titleEntity, @NonNull LinkageEntity contentEntity, boolean selected) {
                LogUtils.iTag(TAG, "onItemClickSticker: " + contentEntity);
                assert contentEntity instanceof StickerItem;
                StickerItem item = (StickerItem) contentEntity;
                if (null != titleEntity) {
                    if (titleEntity.type == EffectType.TYPE_STICKER_ADD) {// 叠加
                        if (selected) {
                            mBaseDisplay.addSticker(item.path);
                            mCurrentAddPackagePath = item.path;
                        } else {
                            mBaseDisplay.removeSticker(mCurrentAddPackagePath);
                        }
                    } else if (titleEntity.type == EffectType.TYPE_STICKER_TRACK) {// 通用物体追踪
                        if (selected) {
                            mGuideBitmap = STUtils.getImageFromAssetsFile(ContextHolder.getContext(), item.iconUrl);
                        }
                    } else {
                        if (selected) {
                            mBaseDisplay.removeAllStickers(false);
                            mBaseDisplay.addSticker(item.path);
                            mCurrentAddPackagePath = item.path;
                        } else {
                            mBaseDisplay.removeSticker(mCurrentAddPackagePath);
                        }
                    }
                }
            }

            @Override
            public void onClickClearSticker() {// 清空贴纸
                mBaseDisplay.clearAllSticker();
            }

            @Override
            public void onSelectedObjectTrack() {// 通用物体追踪 自动切换后置
                //noinspection deprecation
                if (mBaseDisplay.getCameraID() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    mBaseDisplay.switchCameraNew();
                }
            }

            @Override
            public void onGanStickerSelected(boolean selected) {//
            }
        });
        mAccelerometer.setListener(orientation -> {
        });
        mBinding.layoutCamera.setListener(new CameraLayout.Listener() {
            @Override
            public void onClickResetIsMe() {

            }

            @Override
            public void onClickReplay() {
                mBaseDisplay.replayPackage();
            }

            @Override
            public void onClickIsMe() {
                //noinspection Convert2Lambda
            }
        });

    }

    protected LoadingDialog mLoadingDialog;

    protected void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    public void dismissLoading() {
        this.runOnUiThread(() -> {
            if (mLoadingDialog == null) {
                return;
            }
            mLoadingDialog.dismiss();
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtils.attachBaseContext(newBase));
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void setListener() {
        Disposable disposable = RxEventBus.modelsLoaded.subscribe(aBoolean -> {
            dismissLoading();
            recover();
        });
        compositeDisposable.add(disposable);
        long startTime = System.currentTimeMillis();
        if (AtyStateContext.getInstance().getState() instanceof CameraAtyState) {
            LogUtils.i("state info camera aty state");
            EffectInfoDataHelper.setType(EffectInfoDataHelper.Type.CAMERA);
            mBinding.layoutCamera.getLlBottomText().setListener(this);
        } else {
            LogUtils.iTag(TAG, "state info setListener: img video");
            EffectInfoDataHelper.setType(EffectInfoDataHelper.Type.IMG);
            mBinding.layoutCamera.getLlBottomText().setListener(null);
        }
        mBinding.layoutCamera.getBasicEffectView().mIbShowOriginal.setOnTouchListener(this::onTouchOriginal);
        mBinding.layoutCamera.getFilterView().mIbShowOriginal.setOnTouchListener(this::onTouchOriginal);
        if (AtyStateContext.getInstance().getState() instanceof ImgAtyState || AtyStateContext.getInstance().getState() instanceof TryOnImgAtyState) {
            LogUtils.i("state info img state");
            mBinding.layoutCamera.getCameraBar().mIvSaveImg.setOnClickListener(view -> {
                mBaseDisplay.enableSave(true);
                TipToast.Companion.makeText(ContextHolder.getContext(), Toast.LENGTH_SHORT).show();
            });
        }
        LogUtils.i("setListener() cost time:" + (System.currentTimeMillis() - startTime));
    }

    private void initFilterData() {
        long startTime = System.currentTimeMillis();
        mBinding.layoutCamera.getFilterView().init(LocalDataStore.getInstance().getFilterOptionsList(), LocalDataStore.getInstance().getFilterContentList());
        mBinding.layoutCamera.getFilterView().setListener(this);
        LogUtils.i(TAG, "initFilterData cost time:" + (System.currentTimeMillis() - startTime));
    }

    protected void initView() {
        long startTime = System.currentTimeMillis();
        mBinding.layoutCamera.getCameraBar().setListener(this);
        mBinding.layoutCamera.getRecord_view().setListener(this);
        mBinding.layoutCamera.getBottomMenuView().setListener(this);
        mBinding.layoutCamera.getBasicEffectView().setListener(this);

        findViewById(R.id.ib_show_original_bottom).setOnClickListener(this);
        findViewById(R.id.ib_show_original_bottom).setOnTouchListener(this);

        mBinding.layoutCamera.showStatusByMode();
        LogUtils.i("initView() cost time" + (System.currentTimeMillis() - startTime));
    }

    public void onClick(View v) {
    }

    private ArrayList<StickerTitleEntity> stickerOptionsListNew;

    public void initData() {
        long startTime = System.currentTimeMillis();
        // Image processing functionality removed - these states no longer exist
        if (false) { // Disabled - ImgAtyState and TryOnImgAtyState removed
            EffectInfoDataHelper.getInstance().clear();
        }
        initFilterData();
        LogUtils.i("initData setDataNew cost time:" + (System.currentTimeMillis() - startTime));

        Disposable disposable = Observable.fromCallable(() -> {
                    ArrayList<StickerTitleEntity> stickerOptionsListNew = LocalDataStore.getInstance().getStickerOptionsListNew();
                    BaseActivity.this.stickerOptionsListNew = stickerOptionsListNew;
                    HashMap<EffectType, List<StickerItem>> stickerContentList = LocalDataStore.getInstance().getStickerContentList();
                    return stickerContentList;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stickerContentList -> {
                    mBinding.layoutCamera.getStickerView().init(stickerOptionsListNew, stickerContentList, new StickerView.FinishCallback() {
                        @Override
                        public void finishExecute() {
                            showViewByEffectType();
                        }
                    });
                });
        compositeDisposable.add(disposable);

    }

    public void recover() {
        // 只有预览模式才会恢复上次退出的效果,图片模式走的默认参数
        if ((AtyStateContext.getInstance().getState() instanceof CameraAtyState) || (AtyStateContext.getInstance().getState() instanceof VideoAtyState)) { // Removed ImgAtyState
            recoverUIEffect();
        }
    }

    private final Runnable recoveryBasic = () -> {
        Log.i(TAG, "recoveryBasic Runnable called");
        // 恢复基础美颜UI，先取出json中所有的item，再查找数据库，如果有则修改strength
        compositeDisposable.add(Observable.create((ObservableOnSubscribe<BeautyItem>) emitter -> {
            ArrayList<BeautyItem> beautyItemList = LocalDataStore.getInstance().getAllBaseBeautyItems();
            for(BeautyItem item : beautyItemList) {
                DBBeautyEntity dbBeauty = dbManager.queryBaseBeauty(item.uid);
                if(dbBeauty != null) {
                    item.setProgress(dbBeauty.strength);
                }
                emitter.onNext(item);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BeautyItem>() {
            @Override
            public void accept(BeautyItem beautyItem) throws Throwable {
                mBinding.layoutCamera.getBasicEffectView().setHighLightNew2(beautyItem);
            }
        }));

        // 恢复基础美颜效果
        ArrayList<BasicBeautyTitleItem> beautyOptionsList = LocalDataStore.getInstance().getBeautyOptionsList();
        for (BasicBeautyTitleItem item : beautyOptionsList) {
            String[] assetsPathSubMenus = item.assets_path_sub_menus;
            for (String assetPath:assetsPathSubMenus) {
                ArrayList<BeautyItem> beautyList = LocalDataStore.getInstance().getBeautyList(assetPath, null);
                for(BeautyItem beautyItem: beautyList) {
                    DBBeautyEntity dbItem = dbManager.queryBaseBeauty(beautyItem.uid);
                    if(null != dbItem) {
                        beautyItem.setProgress(dbItem.strength);
                    }

                    if (!beautyItem.skip_set_when_restart && beautyItem.getProgress() != 0) {
                        mBaseDisplay.setBasicBeauty(null, beautyItem, beautyItem.getProgress());
                    }
                }
            }
        }
    };

    private final Runnable recoveryFilter = () -> {
        // 恢复滤镜效果
        DBFilterEntity dbFilter = dbManager.queryFilter();
        if (dbFilter != null) {
            EnumMap<EffectType, Integer> selectedIndexMap = new EnumMap<>(EffectType.class);
            EffectType effectType = EffectType.Companion.getTypeByName(dbFilter.groupEnumName);
            selectedIndexMap.put(effectType, dbFilter.selectedPosition);

            // 恢复UI
            EnumMap<EffectType, Float> strengthsMap = new EnumMap<>(EffectType.class);
            strengthsMap.put(effectType, dbFilter.filterStrength);
            mBinding.layoutCamera.getFilterView().setHighLight(null, selectedIndexMap, strengthsMap);

            // 恢复效果
            mBaseDisplay.setFilterStyle("filter_portrait", null, dbFilter.filterPath);
            mBaseDisplay.setFilterStrength(dbFilter.filterStrength);
            mBaseDisplay.enableFilter(true);
        }
    };

    private void recoverUIEffect() {
        LinkedHashMap<Runnable, Long> map = new LinkedHashMap<>();
        map.put(recoveryBasic, EffectInfoDataHelper.getInstance().getBasicStamp());
        map.put(recoveryFilter, dbManager.getFilterTime());
        // 预览下需要记录参数，恢复顺序有要求
        if (AtyStateContext.getInstance().getState() instanceof CameraAtyState) {
            map.clear();
            map.put(recoveryBasic, EffectInfoDataHelper.getInstance().getBasicStamp());
            LinkedHashMap<Runnable, Long> mapNeedSort = new LinkedHashMap<>();
            mapNeedSort.put(recoveryFilter, dbManager.getFilterTime());
            map.putAll(CollectionSortUtils.sort(mapNeedSort));
        }
        for (Map.Entry<Runnable, Long> entry : map.entrySet()) {
            entry.getKey().run();
        }

        // 先恢复基础美颜，再恢复风格妆，在风格妆之后调整的参数再设置一遍 风格妆之后设置的参数再设置一遍
        if (AtyStateContext.getInstance().getState() instanceof CameraAtyState) {
            ArrayList<BeautyItem> beautyItems = dbManager.queryBaseBeauty();
            long styleTimestamp = dbManager.getStyleTime();
            if (styleTimestamp != 0) {
                for (BeautyItem beautyItem : beautyItems) {
                    if (beautyItem.timestamp > styleTimestamp) {
                        if (!beautyItem.skip_set_when_restart && beautyItem.getProgress() != 0) {
                            Log.i(TAG, "align set base beauty uid=" + beautyItem.uid + " strength=" + beautyItem.getProgress());
                            mBaseDisplay.setBasicBeauty(null, beautyItem, beautyItem.getProgress());
                        }
                    }
                }
            }
        }
    }
    private Observable<ArrayList<BeautyItem>> observableMap(EnumMap<EffectType, Float> strengthMap) {
        return Observable.just(strengthMap).map(effectTypeFloatEnumMap -> {
            ArrayList<BeautyItem> beautyItems = new ArrayList<>();
            for (Map.Entry<EffectType, Float> entry : strengthMap.entrySet()) {
                BeautyItem beautyItem = new BeautyItem();
                beautyItem.beauty_type = entry.getKey().getCode();
                beautyItem.enum_name = entry.getKey().name();
                beautyItem.setProgress(entry.getValue());
                beautyItems.add(beautyItem);
            }
            return beautyItems;
        });
    }

    // 高亮
    private void showViewByEffectType() {
        mBinding.layoutCamera.getBottomMenuView().post(() -> {
            if (EffectType.Companion.getBasicList().contains(mEffectType)) {//美颜
                onClickBeauty();
                mBinding.layoutCamera.getBasicEffectView().setHighLight(mEffectType, null, null);
            } else if (EffectType.Companion.getFilterList().contains(mEffectType)) {//滤镜
                onClickFilter();
                mBinding.layoutCamera.getFilterView().setHighLight(mEffectType, null, null);
            } else if (EffectType.Companion.getMakeupList().contains(mEffectType)) {//美妆
                onClickMakeup();
            } else if (EffectType.Companion.getStickerList().contains(mEffectType)) {//贴纸
                onClickSticker();
                mBinding.layoutCamera.getStickerView().setHighLight(mEffectType);
            } else if (EffectType.Companion.getStyleList().contains(mEffectType)) {
                mBinding.layoutCamera.getLlBottomText().performClickStyle();

                EnumMap<EffectType, Integer> styleContentSelectedIndexMap = LocalDataStore.getInstance().getStyleContentSelectedIndexMap();

                if (hasSelected(styleContentSelectedIndexMap)) {
                } else {
                }
            }
        });
    }

    private boolean hasSelected(EnumMap<EffectType, Integer> styleContentSelectedIndexMap) {
        boolean flag = false;
        Set<EffectType> effectTypes = styleContentSelectedIndexMap.keySet();
        for (EffectType type : effectTypes) {
            Integer integer = styleContentSelectedIndexMap.get(type);
            if (integer != null && integer >= 0) flag = true;
        }
        return flag;
    }

    @Override
    public void onClickMenuListener(int position) {
        mBinding.layoutCamera.onClickMenuListener(position);
    }

    @Override
    public void onClickSticker() {
        mBinding.layoutCamera.hideMenuView();
        mBinding.layoutCamera.getClSticker().setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickMakeup() {
    }

    @Override
    public void onClickFilter() {
        mBinding.layoutCamera.hideMenuView();
        mBinding.layoutCamera.getBottomMenuView().setVisibility(View.INVISIBLE);
        mBinding.layoutCamera.getFilterView().setVisibility(View.VISIBLE);

        // 先恢复手动调节的UI，再根据overlap信息更新UI
        mBinding.layoutCamera.getFilterView().restoreUI();
        compositeDisposable.add(Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
//            boolean hasFilter = false;
//            STEffectBeautyInfo[] overlapInfoArr = mBaseDisplay.getOverlappedBeauty();
//            if (overlapInfoArr != null) {
//                for (STEffectBeautyInfo item : overlapInfoArr) {
//                    if (item.getType() / 100 == 5) {
//                        hasFilter = true;
//                        break;
//                    }
//                }
//            }
//            emitter.onNext(hasFilter);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hasFilter -> {
            if (hasFilter) {
                mBinding.layoutCamera.getFilterView().clearContentSelected();
            }
        }));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return onTouchOriginal(view, motionEvent);
    }

    private boolean onTouchOriginal(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.ib_show_original_bottom ||
                view.getId() == R.id.ib_show_original) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                mBaseDisplay.setShowOriginal(true);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                mBaseDisplay.setShowOriginal(false);
            }
            return true;
        }
        return false;
    }

    protected abstract void setBaseDisplay();

    @Override
    public void onClickBeauty() {
        mBinding.layoutCamera.hideMenuView();
        mBinding.layoutCamera.getBasicEffectView().setVisibility(View.VISIBLE);

        //STEffectBeautyInfo[] beautyInfoArr = mBaseDisplay.getOverlappedBeauty();
        //recoverBasicUI();
        // 先恢复UI,再用overlap信息覆盖
        ArrayList<BeautyItem> backupList = mBinding.layoutCamera.getBasicEffectView().getBackupList();
        if (backupList != null) {
            for(BeautyItem item : backupList) {
                mBinding.layoutCamera.getBasicEffectView().overlapBefore(item);
            }
        }

        // 有一种情况，点击风格妆后，展开base beauty ui，再取消风格妆，这个时候overlap中没有信息，要从数据库中读取数据并恢复UI
        //noinspection RedundantLengthCheck
//        if (beautyInfoArr != null && beautyInfoArr.length > 0) {
//            for (STEffectBeautyInfo item : beautyInfoArr) {
//                Log.i(TAG, "STEffectBeautyInfo item=" + item.toString());
//                Disposable disposable = Observable.just(item).map(stEffectBeautyInfo -> {
//                    BeautyItem beautyItem = new BeautyItem();
//                    beautyItem.beauty_type = stEffectBeautyInfo.getType();
//                    beautyItem.mode = stEffectBeautyInfo.getMode();
//                    beautyItem.setProgress(stEffectBeautyInfo.getStrength());
//                    return beautyItem;
//                }).subscribe(beautyItem -> {
//                    mBinding.layoutCamera.getBasicEffectView().setHighLightNew(beautyItem);
//                });
//                compositeDisposable.add(disposable);
//            }
//        }
    }

    @Override// 设置滤镜
    public void onItemClickFilter(int position, @Nullable FilterTitleItem titleEntity, @NotNull LinkageEntity contentEntity, boolean selected, float strength, @NotNull RecyclerView.Adapter<?> adapter) {
        mBaseDisplay.setFilter(contentEntity, strength);
    }

    @Override// 清空滤镜
    public void onClickClearFilter() {
        mBaseDisplay.clearFilter();
    }

    @Override// 设置滤镜强度
    public void onProgressChangedFilter(@NotNull FilterTitleItem titleEntity, @Nullable SeekBar seekBar, float progress, boolean fromUser) {
        mBaseDisplay.setFilterStrength(progress);
    }

    // 录制视频
    @Override
    public void recordStateChangeListener(@NotNull RecordView.StateType status) {
        mBinding.layoutCamera.recordViewStatus(status);
    }

    private String mCurrentAddPackagePath;

    @Override
    public void onProgressChangedBasicEffect(BasicBeautyTitleItem titleData, @NotNull BeautyItem contentEntity, float progress, boolean fromUser) {
        mBaseDisplay.setBasicBeauty(titleData, contentEntity, progress);
    }

    @Override
    public void onClickResetBasicEffect(@NotNull BasicBeautyTitleItem currentTitleData, @NotNull ArrayList<BeautyItem> dataList) {
        EnumMap<EffectType, Float> strengthsMap = EffectType.Companion.getStrengthMap(currentTitleData.type);

        compositeDisposable.add(observableMap(strengthsMap).subscribe(beautyItems -> {
            for (BeautyItem item : beautyItems) {
                mBinding.layoutCamera.getBasicEffectView().setHighLightNew2(item);
            }
        }));
        mBaseDisplay.resetBaseEffect(currentTitleData, dataList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (AtyStateContext.getInstance().getState() instanceof TryOnVideoAtyState) {
            AtyStateContext.getInstance().setState(new TryOnCameraAtyState(null));
        }
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        if (AtyStateContext.getInstance().getState() instanceof TryOnCameraAtyState) {
            //mBinding.layoutCamera.onActivityResult(requestCode, resultCode, data);
            //super.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAccelerometer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAccelerometer.stop();
        mBinding.layoutCamera.getRecord_view().onPause();
    }

}