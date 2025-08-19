package softsugar.senseme.com.effects.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.blankj.utilcode.util.LogUtils;
import android.view.View;
import android.widget.Toast;

import com.softsugar.stmobile.STSoundPlay;

import java.io.File;
import java.io.IOException;

import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.display.VideoPreviewDisplay;
import softsugar.senseme.com.effects.encoder.mediacodec.VideoProcessor;
import softsugar.senseme.com.effects.utils.Constants;
import softsugar.senseme.com.effects.utils.ContextHolder;
import softsugar.senseme.com.effects.utils.DoubleClickUtils;
import softsugar.senseme.com.effects.utils.EffectInfoDataHelper;
import softsugar.senseme.com.effects.utils.FileUtils;
import softsugar.senseme.com.effects.utils.MultiLanguageUtils;
import softsugar.senseme.com.effects.view.widget.TipToast;

public class VideoActivity extends ImageActivity implements DoubleClickUtils.Listener {
    private static final String TAG = "VideoActivityT";

    private String mVideoPath = null;
    private String mOutputPath;

    private ProgressDialog mProgressDialog;

    private static final String EXTRA_PATH = "extra_path";

    public static void actionStart(Activity aty, String path) {
        Intent intent = new Intent();
        intent.setClass(aty, VideoActivity.class);
        intent.putExtra(EXTRA_PATH, path);
        aty.startActivity(intent);
    }

    private void getExtraData() {
        mVideoPath = (String) getIntent().getSerializableExtra(EXTRA_PATH);
    }

    private boolean mIsPreviewing = false;

    @Override
    protected void onResume() {
        super.onResume();
        STSoundPlay.getInstance(this).resumeSound();
        mBinding.layoutCamera.getCameraBar().mStartBtn.setVisibility(View.VISIBLE);
        mBinding.layoutCamera.getCameraBar().mStopBtn.setVisibility(View.INVISIBLE);
        mIsPreviewing = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EffectInfoDataHelper.setType(EffectInfoDataHelper.Type.IMG);
        getExtraData();
        super.onCreate(savedInstanceState);

        //btn_print.setOnClickListener(v -> {
        //    Log.d(TAG, "onCreate: " + mImageDisplay.mCurrentStickerMaps);
        //});
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.layoutCamera.getCameraBar().mStartBtn.setVisibility(View.VISIBLE);
        mBinding.layoutCamera.getCameraBar().mStartBtn.setOnClickListener(view -> {
            LogUtils.iTag(TAG, "onClick: 视频播放");
            if (!mIsPreviewing) {
                ((VideoPreviewDisplay) mImageDisplay).StartPlayVideo();
                mBinding.layoutCamera.getCameraBar().mStartBtn.setVisibility(View.INVISIBLE);
                mBinding.layoutCamera.getCameraBar().mStopBtn.setVisibility(View.VISIBLE);
                mIsPreviewing = true;
            }
        });
        mBinding.layoutCamera.getCameraBar().mStopBtn.setOnClickListener(view -> {
            LogUtils.iTag(TAG, "onClick: 视频暂停");
            if (mIsPreviewing) {
                ((VideoPreviewDisplay) mImageDisplay).pauseVideo();
                mBinding.layoutCamera.getCameraBar().mStopBtn.setVisibility(View.INVISIBLE);
                mBinding.layoutCamera.getCameraBar().mStartBtn.setVisibility(View.VISIBLE);
                mIsPreviewing = false;
            }
        });
        mBinding.layoutCamera.getCameraBar().mIvSaveImg.setOnClickListener(view -> {
            mBinding.layoutCamera.getCameraBar().mStopBtn.performClick();
            processVideoAndSave();
            mProgressDialog = ProgressDialog.show(VideoActivity.this, "", "视频保存中...");
            mProgressDialog.show();
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.id_gl_sv) {
            mBinding.layoutCamera.closeTableView();
            mBinding.layoutCamera.showMenuView();
            ///DoubleClickUtils.getInstance().onClick( this);
        }
    }

    @Override
    protected void initDisplay() {
        long startEatTime = System.currentTimeMillis();
        initVideoView();

        //GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.id_gl_sv);
        LogUtils.iTag(TAG, "new VideoPreviewDisplay cost time:" + (System.currentTimeMillis() - startEatTime));
        mImageBitmap = BitmapFactory.decodeResource(ContextHolder.getContext().getResources(), R.drawable.default_face);
        mImageDisplay.setImageBitmap(mImageBitmap);
    }

    private void initVideoView() {
        if (mVideoPath == null) {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "请选择mp4格式视频文件！", Toast.LENGTH_SHORT).show());
            finish();
        }
        mOutputPath = mVideoPath;
    }

    private void processVideoAndSave() {
        MediaPlayer mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setLooping(true);
        try {
            mMediaPlayer.setDataSource(mVideoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mVideoPath);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        if (duration == null) {
            runOnUiThread(() -> Toast.makeText(ContextHolder.getContext(), "读取视频信息失败，请重新选择！", Toast.LENGTH_SHORT).show());
            return;
        }

        int videoDuration = Integer.parseInt(duration);
        if (videoDuration < 1000) {
            runOnUiThread(() -> Toast.makeText(ContextHolder.getContext(), MultiLanguageUtils.getStr(R.string.toast_short_video), Toast.LENGTH_SHORT).show());
            return;
        } else if (videoDuration > 180 * 1000) {
            runOnUiThread(() -> Toast.makeText(ContextHolder.getContext(), "视频太长，只处理前180秒！", Toast.LENGTH_SHORT).show());
            videoDuration = 180 * 1000;
        }
        videoDuration = videoDuration - 100;

        VideoProcessor mVideoProcessor = new VideoProcessor(ContextHolder.getContext());
        mVideoProcessor.setInputVideoPath(mVideoPath);
        mOutputPath = FileUtils.getPath(ContextHolder.getContext(), "/Camera/", System.currentTimeMillis() + ".mp4");
        mVideoProcessor.setOutputVideoPath(mOutputPath);
        mVideoProcessor.setDetectConfig(((VideoPreviewDisplay) mImageDisplay).getHumanActionDetectConfig());
        mVideoProcessor.setRenderOptions(((VideoPreviewDisplay) mImageDisplay).ifNeedBeauty(), ((VideoPreviewDisplay) mImageDisplay).ifNeedNakeup(),
                ((VideoPreviewDisplay) mImageDisplay).ifNeedSticker(), ((VideoPreviewDisplay) mImageDisplay).ifNeedFilter());
//        mVideoProcessor.setCurrentBeautyParams(((VideoPreviewDisplay) mImageDisplay).getBeautifyParamsTypeBase(), ((VideoPreviewDisplay) mImageDisplay).getBeautifyParamsTypeProfessional(), ((VideoPreviewDisplay) mImageDisplay).getBeautifyParamsTypeMicro(), ((VideoPreviewDisplay) mImageDisplay).getBeautifyParamsTypeAdjust());
        mVideoProcessor.setCurrentMakeups(((VideoPreviewDisplay) mImageDisplay).getCurrentMakeupPaths(), ((VideoPreviewDisplay) mImageDisplay).getCurrentMakeupStrengths());
        mVideoProcessor.setCurrentSticker(mImageDisplay.mCurrentStickerMaps);
        mVideoProcessor.setGreenParams(((VideoPreviewDisplay) mImageDisplay).getGreenParamsMap());
        if (mImageDisplay.mCurrentStickerMaps != null)
            LogUtils.iTag(TAG, "stickerMaps:" + mImageDisplay.mCurrentStickerMaps);
        mVideoProcessor.setCurrentFilter(((VideoPreviewDisplay) mImageDisplay).getCurrentFilterPath(), ((VideoPreviewDisplay) mImageDisplay).getCurrentFilterStrength());
        mVideoProcessor.setOnVideoCutFinishListener(new VideoProcessor.OnVideoProcessListener() {
            @Override
            public void onFinish() {
                runOnUiThread(() -> {
                    mProgressDialog.dismiss();
                    TipToast.Companion.makeText(ContextHolder.getContext(), "视频保存成功", Toast.LENGTH_SHORT).show();
                    MediaScannerConnection.scanFile(ContextHolder.getContext(), new String[]{"/storage/emulated/0/DCIM/Camera"}, null, new MediaScannerConnection.OnScanCompletedListener(){
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                            LogUtils.iTag(TAG, "insert onScanCompleted path " + " uri " + uri);
                        }
                    });
                    if (mOutputPath != null) {
                        File mediaFile = new File(mOutputPath);
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(mediaFile);
                        mediaScanIntent.setData(contentUri);
                        ContextHolder.getContext().sendBroadcast(mediaScanIntent);
                        if (Build.VERSION.SDK_INT >= 19) {
                            MediaScannerConnection.scanFile(ContextHolder.getContext(), new String[]{mOutputPath}, null, null);
                            MediaScannerConnection.scanFile(ContextHolder.getContext(), new String[]{"/storage/emulated/0/DCIM/Camera"}, null, null);
                        }
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(mOutputPath).getParentFile().getAbsoluteFile())));
                    }
                });
                mImageDisplay.replayPackage();
            }

            @Override
            public void onFailed() {
                runOnUiThread(() -> {
                    Toast.makeText(ContextHolder.getContext(), "视频编解码失败！", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                });
            }

            @Override
            public void onCanceled() {
                runOnUiThread(() -> {
                    Toast.makeText(ContextHolder.getContext(), MultiLanguageUtils.getStr(R.string.toast_video_error), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                });
            }
        });

        mVideoProcessor.setOnFrameCallbackListener(progress -> runOnUiThread(() -> {
            //float prog = Math.round(progress * 100) / 100;
            //if (prog > 100) {
            //    prog = 100;
            //}
            //    mIsProcessingTV.setText("正在处理视频："+ prog +"%");
        }));

        try {
            mVideoProcessor.processVideo(0, videoDuration * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setBaseDisplay() {
        mImageDisplay = new VideoPreviewDisplay(this, mBinding.idGlSv, mHandler, mVideoPath, Constants.MODE_VIDEO);
        mBaseDisplay = mImageDisplay;
    }

    @Override
    public void oneClick() {
        LogUtils.iTag(TAG, "oneClick() called");
        if (mBinding.triggerTipView.hasOneClick())
            mImageDisplay.changeCustomEvent(false);
    }

    @Override
    public void doubleClick() {
        LogUtils.iTag(TAG, "doubleClick() called");
        if (mBinding.triggerTipView.hasDoubleClick())
            mImageDisplay.changeCustomEvent(true);
    }
}
