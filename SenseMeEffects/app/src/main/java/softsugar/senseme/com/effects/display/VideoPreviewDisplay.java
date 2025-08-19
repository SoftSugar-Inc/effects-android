package softsugar.senseme.com.effects.display;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.view.Surface;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.softsugar.stmobile.STCommonNative;
import com.softsugar.stmobile.engine.glutils.STEffectsInput;
import com.softsugar.stmobile.engine.glutils.STEffectsOutput;
import com.softsugar.stmobile.model.STEffectTexture;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import softsugar.senseme.com.effects.display.glutils.GlUtil;
import softsugar.senseme.com.effects.display.glutils.OpenGLUtils;
import softsugar.senseme.com.effects.display.glutils.STGLRender;
import softsugar.senseme.com.effects.entity.ModelItem;
import softsugar.senseme.com.effects.utils.ReadAssetsJsonFileUtils;
import softsugar.senseme.com.effects.utils.ThreadUtils;

public class VideoPreviewDisplay extends ImageDisplay {

    private SurfaceTexture mVideoTexture;
    private MediaPlayer mMediaPlayer;
    private final String mVideoPath;
    private boolean mNeedPause = true;
    private final Timer mTimer = new Timer();
    private TimerTask mTimerTask;
    private MediaMetadataRetriever mMediaMetadataRetriever;
    private ByteBuffer mRGBABuffer;

    private boolean mIsPaused = false;

    public VideoPreviewDisplay(Context context, GLSurfaceView glSurfaceView, Handler handler, String path, String mode) {
        super(context, glSurfaceView, handler, mode);

        mVideoPath = path;
        long startEatTime = System.currentTimeMillis();
        mGLRender = new STGLRender();
        LogUtils.i("new STGLRender cost time:" + (System.currentTimeMillis() - startEatTime));

        ThreadUtils.getInstance().runOnSubThread(() -> {
            String json = ReadAssetsJsonFileUtils.getJson("json_data/json_models.json");
            Type type = GsonUtils.getListType(ModelItem.class);
            List<ModelItem> modelItemList = GsonUtils.fromJson(json, type);
            String root = Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath() + File.separator;
            for (int i = 0; i < modelItemList.size(); i++) {
                String sdPath = root + modelItemList.get(i);
                mSTEffectsEngine.loadSubModel(sdPath);
            }
        });
    }

    public HashMap<Integer, Float> getGreenParamsMap() {
        return mGreenSegmentParamsMap;
    }

    public long getHumanActionDetectConfig() {
        return mDetectConfig;
    }

    public boolean ifNeedBeauty() {
        return mNeedBeautify;
    }

    public boolean ifNeedNakeup() {
        return mNeedMakeup;
    }

    public boolean ifNeedSticker() {
        return mNeedSticker;
    }

    public boolean ifNeedFilter() {
        return mNeedFilter;
    }


    public String getCurrentStickerPath() {
        return mCurrentSticker;
    }

    public String getCurrentFilterPath() {
        return mCurrentFilterStyle;
    }

    public float getCurrentFilterStrength() {
        return mCurrentFilterStrength;
    }

    public String[] getCurrentMakeupPaths() {
        return mCurrentMakeup;
    }

    public float[] getCurrentMakeupStrengths() {
        return mMakeupStrength;
    }

    @Override
    public void onResume() {
        mIsPaused = false;
        mNeedPause = true;

        mGLRender = new STGLRender();
        mGlSurfaceView.onResume();
        mGlSurfaceView.forceLayout();
        mGlSurfaceView.requestRender();

        if (mNeedFilter) {
            mCurrentFilterStyle = null;
        }

        if (refreshCount < MAX_REFRESH) {
            refreshDisplay();
            refreshCount++;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshCount = 0;
        mIsPaused = true;
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
        mGlSurfaceView.queueEvent(() -> {
            deleteTextures();
            mRGBABuffer = null;
            mGLRender.destroyFrameBuffers();
        });
        mGlSurfaceView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        if (mIsPaused) {
            return;
        }
        adjustViewPort(width, height);
        mGLRender.init(mImageWidth, mImageHeight);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        setUpVideo();
    }

    private void adjustViewPort(int width, int height) {
        mDisplayHeight = height;
        mDisplayWidth = width;
        GLES20.glViewport(0, 0, mDisplayWidth, mDisplayHeight);
        mGLRender.calculateVertexBuffer(mDisplayWidth, mDisplayHeight, mImageWidth, mImageHeight);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!setUpVideoSuccess) return;
        if (mRGBABuffer == null) {
            mRGBABuffer = ByteBuffer.allocate(mImageHeight * mImageWidth * 4);
        }

        if (mBeautifyTextureId == null) {
            mBeautifyTextureId = new int[1];
            GlUtil.initEffectTexture(mImageWidth, mImageHeight, mBeautifyTextureId, GLES20.GL_TEXTURE_2D);
        }

        if (mVideoTexture != null && !mIsPaused) {
            mVideoTexture.updateTexImage();
        } else {
            return;
        }

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mRGBABuffer.rewind();

        long preProcessCostTime = System.currentTimeMillis();
        int textureId = mGLRender.preProcess(mTextureId, mRGBABuffer);
        ////greenSegmentVideo(mDisplayWidth, mDisplayHeight);
        int originalTextureId = textureId;
        LogUtils.i("preprocess cost time: " + (System.currentTimeMillis() - preProcessCostTime));

        if (!mShowOriginal && mSTEffectsEngine != null) {
            STEffectTexture effectTexture = new STEffectTexture(textureId, mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);
            STEffectsInput stEffectsInput = new STEffectsInput(effectTexture, null, 0,
                    0, false, false,STCommonNative.ST_PIX_FMT_RGBA8888);

            STEffectsOutput stEffectsOutput = new STEffectsOutput(mBeautifyTextureId[0], null, null);
            mSTEffectsEngine.processTexture(stEffectsInput, stEffectsOutput);
            textureId = stEffectsOutput.getTextureId();

            GLES20.glViewport(0, 0, mDisplayWidth, mDisplayHeight);
            mGLRender.onDrawFrame(textureId);
        } else {
            GLES20.glViewport(0, 0, mDisplayWidth, mDisplayHeight);
            mGLRender.onDrawFrame(originalTextureId);
        }

        if (refreshCount < MAX_REFRESH) {
            refreshDisplay();
            refreshCount++;
        }
    }

    private int refreshCount = 0;
    private static final int MAX_REFRESH = 10;

    public void prepareVideoAndStart() {
        index = 1;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(MediaPlayer::start);
        Surface surface = new Surface(mVideoTexture);
        mMediaPlayer.setSurface(surface);
        surface.release();
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mVideoPath);
            Thread.sleep(10);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnCompletionListener(mp -> {
            if (mNeedPause) {
                return;
            }
            // 在播放完毕被回调
            if (mMediaPlayer != null) {
                mMediaPlayer.seekTo(0);
            }
            if (mTimerTask != null) {
                if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                    mMediaPlayer.getCurrentPosition();
                }
                mTimerTask.cancel();
            }
            StartPlayVideo();
        });
    }

    private void confirmWidthAndHeight(String rotation) {
        try {
            if (rotation == null) {
                mImageHeight = Integer.parseInt(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)); // 视频高度
                mImageWidth = Integer.parseInt(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)); // 视频宽度
                mGLRender.adjustVideoTextureBuffer(180, true, false);

                return;
            }
            switch (Integer.parseInt(rotation)) {
                case 0:
                    mImageHeight = Integer.parseInt(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)); // 视频高度
                    mImageWidth = Integer.parseInt(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)); // 视频宽度
                    mGLRender.adjustVideoTextureBuffer(180, true, false);
                    break;
                case 90:
                    mImageWidth = Integer.parseInt(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)); // 视频高度
                    mImageHeight = Integer.parseInt(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)); // 视频宽度
                    mGLRender.adjustVideoTextureBuffer(90, true, false);
                    break;
                case 180:
                    mImageHeight = Integer.parseInt(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)); // 视频高度
                    mImageWidth = Integer.parseInt(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)); // 视频宽度
                    mGLRender.adjustVideoTextureBuffer(0, true, false);
                    break;
                case 270:
                    mImageWidth = Integer.parseInt(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)); // 视频高度
                    mImageHeight = Integer.parseInt(mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)); // 视频宽度
                    mGLRender.adjustVideoTextureBuffer(270, true, false);
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    int index = 1;

    private final SurfaceTexture.OnFrameAvailableListener mOnFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {

        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            mGlSurfaceView.requestRender();
            if (animalFaceLlength == 0) {
                LogUtils.iTag("onFrameAvailable() called with: surfaceTexture = [" + index + "]");
            }
            index++;
            if (mNeedPause && mMediaPlayer != null) {
                mMediaPlayer.seekTo(0);
                mMediaPlayer.pause();
            }
        }
    };

    private boolean setUpVideoSuccess;

    private void setUpVideo() {
        // 初始化Camera设备预览需要的显示区域(mSurfaceTexture)
        if (mTextureId == OpenGLUtils.NO_TEXTURE) {
            mTextureId = OpenGLUtils.getExternalOESTextureID();

            mVideoTexture = new SurfaceTexture(mTextureId);
            mVideoTexture.setOnFrameAvailableListener(mOnFrameAvailableListener);
        }

        try {
            mMediaMetadataRetriever = new MediaMetadataRetriever();
            mMediaMetadataRetriever.setDataSource(mVideoPath);
            setUpVideoSuccess = true;
        } catch (Exception e) {
            LogUtils.e("setUpVideo: " + e.getMessage());
            setUpVideoSuccess = false;
            return;
        }

        String mVideoRotation = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
        confirmWidthAndHeight(mVideoRotation);
        //开始播放
        prepareVideoAndStart();
        setUpVideoSuccess = true;
    }

    public void StartPlayVideo() {
        index = 1;
        if (mMediaPlayer != null) {
            mNeedPause = false;
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.getCurrentPosition();
                    }

                    if (mMediaPlayer != null) {
                        mMediaPlayer.getDuration();
                    }
                }
            };
            mGlSurfaceView.queueEvent(() -> mMediaPlayer.start());
            mTimer.schedule(mTimerTask, 0, 500);
        }
    }

    public void pauseVideo() {
        if (mMediaPlayer != null) {
            try {
                mGlSurfaceView.queueEvent(() -> mMediaPlayer.pause());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
