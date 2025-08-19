package softsugar.senseme.com.effects.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.media.MediaActionSound;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.PermissionUtils;
import com.softsugar.stmobile.STSoundPlay;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import softsugar.senseme.com.effects.display.CameraDisplay;
import softsugar.senseme.com.effects.display.CameraDisplayImpl;
import softsugar.senseme.com.effects.display.ChangePreviewSizeListener;
import softsugar.senseme.com.effects.display.SavePicListener;
import softsugar.senseme.com.effects.encoder.MediaAudioEncoder;
import softsugar.senseme.com.effects.encoder.MediaEncoder;
import softsugar.senseme.com.effects.encoder.MediaMuxerWrapper;
import softsugar.senseme.com.effects.encoder.MediaVideoEncoder;
import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem;
import softsugar.senseme.com.effects.entity.FilterTitleItem;
import softsugar.senseme.com.effects.state.AtyStateContext;
import softsugar.senseme.com.effects.state.CameraAtyState;
import softsugar.senseme.com.effects.state.TryOnCameraAtyState;
import softsugar.senseme.com.effects.state.TryOnVideoAtyState;
import softsugar.senseme.com.effects.state.VideoAtyState;
import softsugar.senseme.com.effects.utils.Accelerometer;
import softsugar.senseme.com.effects.utils.BaseHandler;
import softsugar.senseme.com.effects.utils.CollectionSortUtils;
import softsugar.senseme.com.effects.utils.ContextHolder;
import softsugar.senseme.com.effects.utils.EffectInfoDataHelper;
import softsugar.senseme.com.effects.utils.FileUtils;
import softsugar.senseme.com.effects.utils.LocalDataStore;
import softsugar.senseme.com.effects.utils.MultiLanguageUtils;
import softsugar.senseme.com.effects.utils.RxEventBus;
import softsugar.senseme.com.effects.view.BeautyItem;
import softsugar.senseme.com.effects.view.FilterItem;
import softsugar.senseme.com.effects.view.StickerItem;
import softsugar.senseme.com.effects.view.widget.BasicEffectView;
import softsugar.senseme.com.effects.view.widget.BottomMenuView;
import softsugar.senseme.com.effects.view.widget.CameraActionBarView;
import softsugar.senseme.com.effects.view.widget.CameraLayout;
import softsugar.senseme.com.effects.view.widget.EffectType;
import softsugar.senseme.com.effects.view.widget.FilterView;
import softsugar.senseme.com.effects.view.widget.LinkageEntity;
import softsugar.senseme.com.effects.view.widget.LoadingDialog;
import softsugar.senseme.com.effects.view.widget.RecordView;
import softsugar.senseme.com.effects.view.widget.SlideTextView;
import softsugar.senseme.com.effects.view.widget.StickerView;
import softsugar.senseme.com.effects.view.widget.TipToast;

/** 相机Activity */
@SuppressLint("NonConstantResourceId")
public class CameraActivity extends AppCompatActivity {

    private final static String TAG = "CameraActivityT";

    protected ActivityCameraBinding mBinding;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final DBManager dbManager = new DBManager();
    protected Accelerometer mAccelerometer = null;
    protected LoadingDialog mLoadingDialog;
    private CameraDisplay mCameraDisplay;

    public static final int MSG_SAVING_IMG = 1;
    private static final int MSG_NEED_START_RECORDING = 10;
    private static final int MSG_STOP_RECORDING = 11;
    public final static int MSG_NEED_UPDATE_STICKER_TIPS = 104;
    public static final int MSG_DISMISS_LOADING = 106;

    private Sensor mRotation;
    private boolean mIsRecording = false;
    private String mVideoFilePath = null;
    private MediaActionSound mMediaActionSound;
    private String mCurrentAddPackagePath;
    private final Handler mHandler = new MyHandler(this);

    /** 相机操作栏监听器 */
    private class CameraActionBarListenerImpl implements CameraActionBarView.Listener {
        @Override
        public void onItemPreviewSizeSelected(int index) {
            mCameraDisplay.changePreviewSize(index);
            mBinding.layoutCamera.getBottomMenuView().setWriteColor();
            mBinding.layoutCamera.getBottomMenuView().setStyle(BottomMenuView.WRITE_STYLE);
            mBinding.layoutCamera.getRecord_view().setStyle(RecordView.WRITE_STYLE);
        }
    }
    
    /** 滑动文本监听器 */
    private class SlideTextListenerImpl implements SlideTextView.Listener {
        @Override
        public void onClickMenuListener(int position) {
            handleMenuClick(position);
        }
    }
    
    /** 底部菜单监听器 */
    private class BottomMenuListenerImpl implements BottomMenuView.Listener {
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
                emitter.onComplete();
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(hasFilter -> {
                if (hasFilter) {
                    mBinding.layoutCamera.getFilterView().clearContentSelected();
                }
            }));
        }

        @Override
        public void onClickBeauty() {
            mBinding.layoutCamera.hideMenuView();
            mBinding.layoutCamera.getBasicEffectView().setVisibility(View.VISIBLE);

            // 先恢复UI,再用overlap信息覆盖
            ArrayList<BeautyItem> backupList = mBinding.layoutCamera.getBasicEffectView().getBackupList();
            if (backupList != null) {
                for(BeautyItem item : backupList) {
                    mBinding.layoutCamera.getBasicEffectView().overlapBefore(item);
                }
            }
        }
    }
    
    /** 录制视图监听器 */
    private class RecordViewListenerImpl implements RecordView.Listener {
        @Override
        public void recordStateChangeListener(@NotNull RecordView.StateType status) {
            // BaseActivity recordStateChangeListener logic
            mBinding.layoutCamera.recordViewStatus(status);
            if (status == RecordView.StateType.START_RECORD) {
                Message msg = mHandler.obtainMessage(MSG_NEED_START_RECORDING);
                mHandler.sendMessage(msg);
                mIsRecording = true;
            } else if (status == RecordView.StateType.STOP_RECORD) {
                Message msg = mHandler.obtainMessage(MSG_STOP_RECORDING);
                mHandler.sendMessage(msg);
                mIsRecording = false;
            } else if (status == RecordView.StateType.CLICK_DELETE) {
                mBinding.layoutCamera.getCameraBar().showBtn();
                mCameraDisplay.startCamera();
                if (mVideoFilePath != null) {
                    File file = new File(mVideoFilePath);
                    file.delete();
                }
            }
            else if (status == RecordView.StateType.TAKE_PIC) {
                mCameraDisplay.setSaveImage();
                if (mMediaActionSound != null) {
                    mMediaActionSound.play(MediaActionSound.SHUTTER_CLICK);
                }
                TipToast.Companion.makeText(ContextHolder.getContext(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /** 滤镜视图监听器 */
    private class FilterViewListenerImpl implements FilterView.Listener {
        @Override// 设置滤镜
        public void onItemClickFilter(int position, @Nullable FilterTitleItem titleEntity, @NotNull LinkageEntity contentEntity, boolean selected, float strength, @NotNull RecyclerView.Adapter<?> adapter) {
            FilterItem entity = (FilterItem) contentEntity;
            mCameraDisplay.setFilter(entity.model);
            mCameraDisplay.setFilterStrength(strength);
        }

        @Override// 清空滤镜
        public void onClickClearFilter() {
            mCameraDisplay.setFilter(null);
        }

        @Override// 设置滤镜强度
        public void onProgressChangedFilter(@NotNull FilterTitleItem titleEntity, @Nullable SeekBar seekBar, float progress, boolean fromUser) {
            mCameraDisplay.setFilterStrength(progress);
        }
    }
    
    /** 触摸事件监听器 */
    private class OnTouchListenerImpl implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return onTouchOriginal(view, motionEvent);
        }
    }
    
    /** 基础特效监听器 */
    private class BasicEffectListenerImpl implements BasicEffectView.Listener {
        @Override
        public void onProgressChangedBasicEffect(BasicBeautyTitleItem titleData, @NotNull BeautyItem contentEntity, float progress, boolean fromUser) {
            mCameraDisplay.setBasicBeauty(titleData, contentEntity, progress);
        }

        @Override
        public void onClickResetBasicEffect(@NotNull BasicBeautyTitleItem currentTitleData, @NotNull ArrayList<BeautyItem> dataList) {
            EnumMap<EffectType, Float> strengthsMap = EffectType.Companion.getStrengthMap(currentTitleData.type);

            compositeDisposable.add(observableMap(strengthsMap).subscribe(beautyItems -> {
                for (BeautyItem item : beautyItems) {
                    mBinding.layoutCamera.getBasicEffectView().setHighLightNew2(item);
                }
            }));
            mCameraDisplay.resetBasicBeauty(currentTitleData, dataList);
        }
    }

    private final CameraActionBarListenerImpl mCameraActionBarListener = new CameraActionBarListenerImpl();
    private final SlideTextListenerImpl mSlideTextListener = new SlideTextListenerImpl();
    private final BottomMenuListenerImpl mBottomMenuListener = new BottomMenuListenerImpl();
    private final RecordViewListenerImpl mRecordViewListener = new RecordViewListenerImpl();
    private final FilterViewListenerImpl mFilterViewListener = new FilterViewListenerImpl();
    private final OnTouchListenerImpl mOnTouchListener = new OnTouchListenerImpl();
    private final BasicEffectListenerImpl mBasicEffectListener = new BasicEffectListenerImpl();
    private void handleMenuClick(int position) {
        mBinding.layoutCamera.onClickMenuListener(position);
    }

    /** 消息处理器 */
    private static class MyHandler extends BaseHandler<CameraActivity> {


        MyHandler(CameraActivity cameraActivity) {
            super(cameraActivity);
        }


        @Override
        protected void handleMessage(final CameraActivity mActivity, Message msg) {
            if (msg.what == MSG_SAVING_IMG) {
                ByteBuffer data = (ByteBuffer) msg.obj;
                Bundle bundle = msg.getData();
                int imageWidth = bundle.getInt("imageWidth");
                int imageHeight = bundle.getInt("imageHeight");
                mActivity.onPictureTaken(data, FileUtils.getOutputMediaFile(), imageWidth, imageHeight);
            } else if (msg.what == MSG_NEED_START_RECORDING) {
                mActivity.startRecording();
                mActivity.mBinding.layoutCamera.closeTableView();
            } else if (msg.what == MSG_STOP_RECORDING) {
                new Handler().postDelayed(() -> {

                    if (mActivity.mIsRecording) {
                        return;
                    }
                    mActivity.stopRecording();
                    if (mActivity.mVideoFilePath != null) {
                        File file = new File(mActivity.mVideoFilePath);
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(file);
                        mediaScanIntent.setData(contentUri);
                        mActivity.sendBroadcast(mediaScanIntent);


                        MediaScannerConnection.scanFile(mActivity, new String[]{mActivity.mVideoFilePath}, null, null);
                    }
                    mActivity.notifyVideoUpdate(mActivity.mVideoFilePath);
                }, 100);
            } else if (msg.what == MSG_NEED_UPDATE_STICKER_TIPS) {
//                long action = mActivity.mCameraDisplay.getStickerTriggerAction();
//                mActivity.mBinding.triggerTipView.showActiveTips(action);
//                mActivity.mBinding.triggerTipView.showCustomEventTips(mActivity.mCameraDisplay.getStickerCustomEventAction());
            } else if(msg.what == MSG_DISMISS_LOADING){
                mActivity.dismissLoading();
            }
        }
    }

    /**
     * 添加生命周期观察者
     * 让CameraLayout能够监听Activity的生命周期
     */
    private void addObserver() {
        getLifecycle().addObserver(mBinding.layoutCamera);
    }

    public void initListener() {
        mCameraDisplay.setOnSaveImageListener(new SavePicListener() {
            @Override
            public void onSuccess(ByteBuffer tmpBuffer, int width, int height) {
                onPictureTaken(tmpBuffer, FileUtils.getOutputMediaFile(), width, height);
            }
        });

        mBinding.layoutCamera.getStickerView().setMListener(new StickerView.Listener() {
            @Override
            public void onItemClickSticker(int position, @androidx.annotation.Nullable StickerTitleEntity titleEntity, @NonNull LinkageEntity contentEntity, boolean selected) {
                assert contentEntity instanceof StickerItem;
                StickerItem item = (StickerItem) contentEntity;
                if (null != titleEntity) {
                    if (titleEntity.type == EffectType.TYPE_STICKER_ADD) {// 叠加
                        if (selected) {
                            mCameraDisplay.addSticker(item.path);
                            mCurrentAddPackagePath = item.path;
                        } else {
                            mCameraDisplay.removeSticker(mCurrentAddPackagePath);
                        }
                    } else if (titleEntity.type == EffectType.TYPE_STICKER_TRACK) {// 通用物体追踪
                    } else {
                        if (selected) {
                            mCameraDisplay.clearAllSticker();
                            mCameraDisplay.addSticker(item.path);
                            mCurrentAddPackagePath = item.path;
                        } else {
                            mCameraDisplay.removeSticker(mCurrentAddPackagePath);
                        }
                    }
                }
            }

            @Override
            public void onClickClearSticker() {// 清空贴纸
                mCameraDisplay.clearAllSticker();
            }

            @Override
            public void onSelectedObjectTrack() {// 通用物体追踪 自动切换后置
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
                mCameraDisplay.replayPackage();
            }

            @Override
            public void onClickIsMe() {
                //noinspection Convert2Lambda
            }
        });

    }

    /**
     * 显示加载对话框
     * 在执行耗时操作时调用，为用户提供视觉反馈
     */
    protected void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    /**
     * 关闭加载对话框
     * 确保在UI线程中执行，避免线程安全问题
     */
    public void dismissLoading() {
        this.runOnUiThread(() -> {
            if (mLoadingDialog == null) {
                return;
            }
            mLoadingDialog.dismiss();
        });
    }

    /**
     * 附加基础上下文，支持多语言
     * 在Activity创建之前调用，用于语言国际化支持
     * 
     * @param newBase 新的上下文
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtils.attachBaseContext(newBase));
    }

    // ==================== 生命周期方法 ====================
    /**
     * Activity创建时的初始化方法
     * 
     * 主要完成以下初始化工作：
     * 1. 基础组件初始化（加速度计、震动器等）
     * 2. 视图绑定和布局设置
     * 3. 相机显示器初始化
     * 4. 数据和监听器初始化
     * 5. 传感器和音效初始化
     * 6. 调试视图设置（DEBUG模式下）
     * 
     * @param savedInstanceState 保存的实例状态
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ===== 第一阶段：基础初始化 =====
        super.onCreate(savedInstanceState);
        mAccelerometer = new Accelerometer(getApplicationContext());  // 初始化加速度计
        // ===== 第二阶段：视图初始化 =====
        long startSetContentViewTime = System.currentTimeMillis();
        mBinding = ActivityCameraBinding.inflate(LayoutInflater.from(this));
        setContentView(mBinding.getRoot());

        // ===== 第三阶段：核心组件初始化 =====
        addObserver();      // 添加生命周期观察者

        // 创建相机显示纹理对象，这是整个相机预览和特效处理的核心组件
        mCameraDisplay = new CameraDisplayImpl(this, mChangePreviewSizeListener, mBinding.idGlSv);

        initView();         // 初始化视图
        initData();         // 初始化数据
        initListener();     // 初始化监听器

        // ===== 第四阶段：相机特定初始化 =====
        EffectInfoDataHelper.setType(EffectInfoDataHelper.Type.CAMERA);  // 设置特效类型为相机
        showLoading();  // 显示加载对话框

        // ===== 第五阶段：事件和监听器设置 =====
        setListener();  // 设置各种监听器

        // ===== 第七阶段：传感器和音效初始化 =====
        mMediaActionSound = new MediaActionSound();  // 初始化拍照音效
    }

    public void initData() {
        initFilterData();

        Disposable disposable = Observable.fromCallable(() -> {
                    ArrayList<StickerTitleEntity> stickerOptionsListNew = LocalDataStore.getInstance().getStickerOptionsListNew();
                    HashMap<EffectType, List<StickerItem>> stickerContentList = LocalDataStore.getInstance().getStickerContentList();
                    return stickerContentList;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stickerContentList -> {
                    ArrayList<StickerTitleEntity> stickerOptionsListNew = LocalDataStore.getInstance().getStickerOptionsListNew();
                    mBinding.layoutCamera.getStickerView().init(stickerOptionsListNew, stickerContentList, new StickerView.FinishCallback() {
                        @Override
                        public void finishExecute() {
                        }
                    });
                });
        compositeDisposable.add(disposable);
    }

    /**
     * 初始化视图组件
     * 
     * 主要完成以下工作：
     * 1. 设置各种视图的监听器
     * 2. 初始化相机相关的UI组件
     * 3. 配置曝光调节条和覆盖层
     */
    protected void initView() {
        // ===== 基础视图监听器设置 =====
        mBinding.layoutCamera.getCameraBar().setListener(mCameraActionBarListener);
        mBinding.layoutCamera.getRecord_view().setListener(mRecordViewListener);
        mBinding.layoutCamera.getBottomMenuView().setListener(mBottomMenuListener);
        mBinding.layoutCamera.getBasicEffectView().setListener(mBasicEffectListener);

        // 原图显示按钮设置
        findViewById(R.id.ib_show_original_bottom).setOnTouchListener(mOnTouchListener);

        mBinding.layoutCamera.showStatusByMode();

        // 切换相机按钮
        findViewById(R.id.tv_change_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraDisplay != null) mCameraDisplay.switchCamera();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        mBinding.layoutCamera.closeTableView();
        mBinding.layoutCamera.showMenuView();
        return true;
    }

    private final ChangePreviewSizeListener mChangePreviewSizeListener = (previewW, previewH) -> CameraActivity.this.runOnUiThread(() -> mBinding.idPreviewLayout.requestLayout());

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("process_killed", true);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Activity恢复时的处理
     * 
     * 主要完成：
     * 1. 启动加速度计监听
     * 2. 重新播放特效包
     * 3. 注册传感器监听
     * 4. 恢复相机预览
     * 5. 请求相机权限
     */
    @Override
    protected void onResume() {
        // ===== 基础组件恢复 =====
        super.onResume();
        mAccelerometer.start();  // 启动加速度计
        
        // ===== 相机相关恢复 =====
        mCameraDisplay.replayPackage();
        EffectInfoDataHelper.setType(EffectInfoDataHelper.Type.CAMERA);

        mCameraDisplay.onResume();
        mCameraDisplay.setShowOriginal(false);
        mIsRecording = false;
        mBinding.layoutCamera.getCameraBar().startShowCpuInfo((CameraDisplayImpl) mCameraDisplay);
        
        // 请求相机权限
        PermissionUtils.permission(Manifest.permission.CAMERA)
                        .callback(new PermissionUtils.FullCallback() {
                            @Override
                            public void onGranted(@NonNull List<String> list) {
                                // 权限已授予，无需特殊处理
                            }

                            @Override
                            public void onDenied(@NonNull List<String> list, @NonNull List<String> list1) {
                                // 权限被拒绝，可在此处理
                            }
                        }).request();
    }

    /**
     * Activity暂停时的处理
     * 
     * 主要完成：
     * 1. 停止加速度计监听
     * 2. 暂停录制视图
     * 3. 释放相机资源
     * 4. 停止正在进行的录制
     * 5. 取消传感器监听
     */
    @Override
    protected void onPause() {
        // ===== 基础组件暂停 =====
        super.onPause();
        mAccelerometer.stop();  // 停止加速度计
        mBinding.layoutCamera.getRecord_view().onPause();  // 暂停录制视图
        
        // ===== 相机相关暂停 =====
        mBinding.layoutCamera.getCameraBar().release();
        STSoundPlay.getInstance(this).pauseSound();
        
        // ===== 录制状态处理 =====
        // 如果正在录制，则停止录制并删除临时文件
        if (mIsRecording) {
            mHandler.removeMessages(MSG_STOP_RECORDING);
            stopRecording();

            if (mVideoFilePath != null) {
                File file = new File(mVideoFilePath);
                file.delete();
            }
            mIsRecording = false;
        }
        mCameraDisplay.onPause();  // 暂停相机显示器
    }

    @Override
    protected void onDestroy() {
        // CameraActivity specific cleanup
        mCameraDisplay.onDestroy();
        if (mMediaActionSound != null) {
            mMediaActionSound.release();
            mMediaActionSound = null;
        }
        
        // BaseActivity cleanup
        if (AtyStateContext.getInstance().getState() instanceof TryOnVideoAtyState) {
            AtyStateContext.getInstance().setState(new TryOnCameraAtyState(null));
        }
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

        super.onDestroy();
    }

    private void onPictureTaken(ByteBuffer data, File file, int mImageWidth, int mImageHeight) {
        if (mImageWidth <= 0 || mImageHeight <= 0)
            return;
        Bitmap srcBitmap = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
        data.position(0);
        srcBitmap.copyPixelsFromBuffer(data);
        saveToSDCard(file, srcBitmap);
        srcBitmap.recycle();
    }

    private void saveToSDCard(File file, Bitmap bmp) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        if (mHandler != null) {
            String path = file.getAbsolutePath();
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            MediaScannerConnection.scanFile(this, new String[]{path}, null, null);
        }
    }

    private void notifyVideoUpdate(String videoFilePath) {
        if (videoFilePath == null || videoFilePath.length() == 0) {
            return;
        }
        File file = new File(videoFilePath);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{videoFilePath}, null, null);
    }

    /**
     * callback methods from encoder
     */
    private final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
        @Override
        public void onPrepared(final MediaEncoder encoder) {
            if (encoder instanceof MediaVideoEncoder && mCameraDisplay != null)
                mCameraDisplay.setVideoEncoder((MediaVideoEncoder) encoder);
        }

        @Override
        public void onStopped(final MediaEncoder encoder) {
            if (encoder instanceof MediaVideoEncoder && mCameraDisplay != null)
                mCameraDisplay.setVideoEncoder(null);
        }
    };

    /** 媒体混合器，用于合并视频和音频流 */
    private MediaMuxerWrapper mMuxer;

    /**
     * 开始录制视频
     * 
     * 创建视频和音频编码器，开始录制过程
     * 录制的视频文件格式为MP4，包含视频和音频轨道
     */
    private void startRecording() {
        try {
            // 创建媒体混合器，指定输出格式为MP4
            mMuxer = new MediaMuxerWrapper(".mp4");
            
            // 创建视频编码器，使用当前预览尺寸
            new MediaVideoEncoder(mMuxer, mMediaEncoderListener, 
                mCameraDisplay.getPreviewWidth(), mCameraDisplay.getPreviewHeight());
            
            // 创建音频编码器（目前总是启用）
            //if (mIsHasAudioPermission) {
                new MediaAudioEncoder(mMuxer, mMediaEncoderListener);
            //}
            
            // 准备并开始录制
            mMuxer.prepare();
            mMuxer.startRecording();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录制视频
     * 
     * 停止录制过程，获取录制文件路径，并进行垃圾回收
     */
    private void stopRecording() {
        if (mMuxer != null) {
            mVideoFilePath = mMuxer.getFilePath();  // 获取录制文件路径
            mMuxer.stopRecording();                 // 停止录制
        }
        System.gc();  // 建议进行垃圾回收，释放录制过程中占用的内存
    }

    // Additional BaseActivity methods
    @SuppressLint("ClickableViewAccessibility")
    protected void setListener() {
        Disposable disposable = RxEventBus.modelsLoaded.subscribe(aBoolean -> {
            dismissLoading();
            recover();
        });
        compositeDisposable.add(disposable);
        
        if (AtyStateContext.getInstance().getState() instanceof CameraAtyState) {
            EffectInfoDataHelper.setType(EffectInfoDataHelper.Type.CAMERA);
            mBinding.layoutCamera.getLlBottomText().setListener(mSlideTextListener);
        } else {
            EffectInfoDataHelper.setType(EffectInfoDataHelper.Type.IMG);
            mBinding.layoutCamera.getLlBottomText().setListener(null);
        }
        
        mBinding.layoutCamera.getBasicEffectView().mIbShowOriginal.setOnTouchListener(this::onTouchOriginal);
        mBinding.layoutCamera.getFilterView().mIbShowOriginal.setOnTouchListener(this::onTouchOriginal);

//        mBinding.layoutCamera.getCameraBar().mIvSaveImg.setOnClickListener(view -> {
//            mCameraDisplay.setSaveImage();
//            TipToast.Companion.makeText(ContextHolder.getContext(), Toast.LENGTH_SHORT).show();
//        });
    }

    private void initFilterData() {
        mBinding.layoutCamera.getFilterView().init(LocalDataStore.getInstance().getFilterOptionsList(), 
            LocalDataStore.getInstance().getFilterContentList());
        mBinding.layoutCamera.getFilterView().setListener(mFilterViewListener);
    }

    public void recover() {
        // 只有预览模式才会恢复上次退出的效果,图片模式走的默认参数
        if ((AtyStateContext.getInstance().getState() instanceof CameraAtyState) || (AtyStateContext.getInstance().getState() instanceof VideoAtyState)) { // Removed ImgAtyState
            recoverUIEffect();
        }
    }

    private final Runnable recoveryBasic = () -> {
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
                        mCameraDisplay.setBasicBeauty(null, beautyItem, beautyItem.getProgress());
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
            mCameraDisplay.setFilter(dbFilter.filterPath);
            mCameraDisplay.setFilterStrength(dbFilter.filterStrength);
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
    
                            mCameraDisplay.setBasicBeauty(null, beautyItem, beautyItem.getProgress());
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

    private boolean onTouchOriginal(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.ib_show_original_bottom ||
                view.getId() == R.id.ib_show_original) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                mCameraDisplay.setShowOriginal(true);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                mCameraDisplay.setShowOriginal(false);
            }
            return true;
        }
        return false;
    }

}
