package softsugar.senseme.com.effects.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaActionSound;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.softsugar.stmobile.STSoundPlay;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.display.ImageDisplay;
import softsugar.senseme.com.effects.state.AtyStateContext;
import softsugar.senseme.com.effects.state.TryOnCameraAtyState;
import softsugar.senseme.com.effects.state.TryOnImgAtyState;
import softsugar.senseme.com.effects.utils.Accelerometer;
import softsugar.senseme.com.effects.utils.BaseHandler;
import softsugar.senseme.com.effects.utils.Constants;
import softsugar.senseme.com.effects.utils.DoubleClickUtils;
import softsugar.senseme.com.effects.utils.EffectInfoDataHelper;
import softsugar.senseme.com.effects.utils.FileUtils;
import softsugar.senseme.com.effects.utils.GlideUtils;
import softsugar.senseme.com.effects.utils.MultiLanguageUtils;
import softsugar.senseme.com.effects.utils.ThreadUtils;

public class ImageActivity extends BaseActivity implements View.OnClickListener, SensorEventListener, DoubleClickUtils.Listener {
    private final static String TAG = "ImageActivity";

    protected Accelerometer mAccelerometer = null;
    protected ImageDisplay mImageDisplay;

    protected Bitmap mImageBitmap;
    public static final int MSG_SAVING_IMG = 1;
    public static final int MSG_SAVED_IMG = 2;
    public final static int MSG_SHOW_MASK = 105;
    protected boolean mPermissionDialogShowing = false;
    private Bitmap greyBitmap;
    private int[] pixels;

    private MediaActionSound mMediaActionSound;

    protected final Handler mHandler = new MyHandler(this);

    @Override
    public void onSensorChanged(SensorEvent event) {
        mImageDisplay.setSensorEvent(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public final static int MSG_NEED_UPDATE_STICKER_TIPS = 104;

    @Override
    public void onItemPreviewSizeSelected(int index) {

    }

    @Override
    public void oneClick() {
        LogUtils.i("oneClick");
        if (mBinding.triggerTipView.hasOneClick())
            mImageDisplay.changeCustomEvent(false);
    }

    @Override
    public void doubleClick() {
        if (mBinding.triggerTipView.hasDoubleClick())
            mImageDisplay.changeCustomEvent(true);
    }

    private static class MyHandler extends BaseHandler<ImageActivity> {

        public MyHandler(ImageActivity aty) {
            super(aty);
        }

        @Override
        protected void handleMessage(ImageActivity aty, Message msg) {
            if (msg.what == MSG_SAVING_IMG) {
                aty.saveToSDCard(FileUtils.getOutputMediaFile(), aty.mImageDisplay.getBitmap());
            }
            if (msg.what  == MSG_NEED_UPDATE_STICKER_TIPS) {
                long action = aty.mImageDisplay.getStickerTriggerAction();
                aty.mBinding.triggerTipView.showActiveTips(action);
                aty.mBinding.triggerTipView.showCustomEventTips(aty.mImageDisplay.getStickerCustomEventAction());
            }
        }
    }

    public Bitmap grey2Bitmap(byte[] values, int picW, int picH) {
        if (values == null || picW <= 0 || picH <= 0)
            return null;
        //使用8位来保存图片
        if (greyBitmap == null) {
            greyBitmap = Bitmap.createBitmap(picW, picH, Bitmap.Config.ARGB_8888);
        }
        if (pixels == null) {
            pixels = new int[picW * picH];
        }
        for (int i = 0; i < pixels.length; ++i) {
            //关键代码，生产灰度图
            pixels[i] = values[i] * 256 * 256 + values[i] * 256 + values[i] + 0xFF000000;
        }
        greyBitmap.setPixels(pixels, 0, picW, 0, 0, picW, picH);
        values = null;
        //pixels = null;
        return greyBitmap;
    }

    private static final String EXTRA_PATH = "extra_path";

    public static void actionStart(Activity aty, String path) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PATH, path);
        intent.setClass(aty, ImageActivity.class);
        aty.startActivity(intent);
    }

    private void getExtraData() {
        String path = (String) getIntent().getSerializableExtra(EXTRA_PATH);
        ThreadUtils.getInstance().runOnSubThread(() -> {
            LogUtils.iTag(TAG, "getExtraData: " + path);
            mImageBitmap = GlideUtils.INSTANCE.compressBitmap(path);
            ThreadUtils.getInstance().runOnUIThread(() -> mImageDisplay.setImageBitmap(mImageBitmap));
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EffectInfoDataHelper.setType(EffectInfoDataHelper.Type.IMG);
        getExtraData();
        super.onCreate(savedInstanceState);
        showLoading();
        MultiLanguageUtils.initLanguageConfig();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        //todo 判断是否存在rotation vector sensor
        mRotation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        setListener();
        initEvents();
        mMediaActionSound = new MediaActionSound();
    }

    protected void initDisplay() {

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void initView() {
        super.initView();
        mAccelerometer = new Accelerometer(getApplicationContext());
        findViewById(R.id.iv_close_sticker).setBackground(getResources().getDrawable(R.drawable.close_sticker_selected));
        initDisplay();
    }

    protected void initEvents() {
        mImageDisplay.setCostChangeListener(value -> runOnUiThread(() ->
                mBinding.layoutCamera.getCameraBar().getTvDebugInfo().setText(getString(R.string.debug_info, value, 0f, "null"))));
        findViewById(R.id.id_gl_sv).setOnClickListener(this);
        mImageDisplay.enableBeautify(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.id_gl_sv) {
            mBinding.layoutCamera.closeTableView();
            mBinding.layoutCamera.showMenuView();
            oneClick();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("process_killed", true);
        super.onSaveInstanceState(savedInstanceState);
    }

    private SensorManager mSensorManager;
    private Sensor mRotation;

    @Override
    protected void onResume() {
        LogUtils.i(TAG, "onResume");
        super.onResume();
        mAccelerometer.start();
        mSensorManager.registerListener(this, mRotation, SensorManager.SENSOR_DELAY_GAME);

        mImageDisplay.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        STSoundPlay.getInstance(this).pauseSound();
        mSensorManager.unregisterListener(this);
        if (!mPermissionDialogShowing) {
            mAccelerometer.stop();
            mImageDisplay.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        mImageDisplay.onDestroy();
        EffectInfoDataHelper.getInstance().clear();
        super.onDestroy();

        if (mMediaActionSound != null) {
            mMediaActionSound.release();
            mMediaActionSound = null;
        }
        if (AtyStateContext.getInstance().getState() instanceof TryOnImgAtyState) {
            AtyStateContext.getInstance().setState(new TryOnCameraAtyState(null));
        }
    }

    protected void saveToSDCard(File file, Bitmap bmp) {
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

            if (Build.VERSION.SDK_INT >= 19) {

                MediaScannerConnection.scanFile(this, new String[]{path}, null, null);
            }

            mHandler.sendEmptyMessage(MSG_SAVED_IMG);
        }
    }

    public boolean isEmpty(String str) {
        if (str != null) str = str.trim();
        return TextUtils.isEmpty(str) || "null".equalsIgnoreCase(str);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.ib_show_original_bottom) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                mImageDisplay.setShowOriginal(true);
                findViewById(R.id.tv_change_camera).setEnabled(false);
                DoubleClickUtils.getInstance().onTouch(motionEvent, this);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                mImageDisplay.setShowOriginal(false);
                findViewById(R.id.tv_change_camera).setEnabled(true);
            }
            return true;
        } else {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                DoubleClickUtils.getInstance().onTouch(motionEvent, this);
            }
        }
        return false;
    }

    @Override
    protected void setBaseDisplay() {
        mImageDisplay = new ImageDisplay(this, mBinding.idGlSv, mHandler, Constants.MODE_IMG);
        mBaseDisplay = mImageDisplay;
    }

    @Override
    public void onClickSticker() {
        super.onClickSticker();
    }

}
