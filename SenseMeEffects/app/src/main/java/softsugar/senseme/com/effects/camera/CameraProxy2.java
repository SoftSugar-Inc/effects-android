package softsugar.senseme.com.effects.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Build;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;

import softsugar.senseme.com.effects.utils.Constants;
import softsugar.senseme.com.effects.utils.ContextHolder;

public class CameraProxy2 {
    private static final String TAG = "CameraProxy2T";

    private final CameraInterface mCamera;

    @SuppressLint("ObsoleteSdkInt")
    public CameraProxy2(Context context, GLSurfaceView glSurfaceView) {
        if (ContextHolder.isCamera2Mode() && Build.VERSION.SDK_INT > Constants.ANDROID_MIN_SDK_VERSION) {
            LogUtils.iTag(TAG, "CameraProxy2: camera2");
            mCamera = new Camera2();
        } else {
            mCamera = new Camera1();
            LogUtils.iTag(TAG, "CameraProxy2: camera1 ");
        }
        mCamera.init(context, glSurfaceView);
    }

    private SurfaceTexture mSurfaceTexture;
    private PreviewCallback mPreviewCallback;

    public void openCamera(final int cameraId, int width, int height, final PreviewCallback previewCallback) {
        LogUtils.iTag(TAG, "openCamera() called with: cameraId = [" + cameraId + "], width = [" + width + "], height = [" + height + "]");
        mCamera.releaseCamera();
        mCamera.openCamera(cameraId, width, height, new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data) {
                if (null != previewCallback)
                    previewCallback.onPreviewFrame(data);
            }
        });
    }

    // 开启相机
    @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
    public void openCamera(final int cameraId, SurfaceTexture surfaceTexture, final PreviewCallback listener) {
        if (surfaceTexture == null) return;
        mSurfaceTexture = surfaceTexture;
        mPreviewCallback = listener;
        mCamera.openCamera(cameraId, surfaceTexture, new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data) {
                if (null != listener)
                    listener.onPreviewFrame(data);
            }
        });
    }

    public void openCamera(final int cameraId) {
        LogUtils.iTag(TAG, "openCamera() called with: cameraId = [" + cameraId + "]");
        if (mSurfaceTexture == null/* || mPreviewCallback == null*/) return;
        mCamera.openCamera(cameraId, mSurfaceTexture, new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data) {
                if (null != mPreviewCallback)
                    mPreviewCallback.onPreviewFrame(data);
            }
        });
    }

    public void startPreview(SurfaceTexture surfaceTexture) {
        mCamera.startPreview(surfaceTexture);
    }

    public void startPreview(SurfaceTexture surfaceTexture, final PreviewCallback listener) {
//        mPreviewCallback = listener;
//        mSurfaceTexture = surfaceTexture;
//        if (mSurfaceTexture == null || mPreviewCallback == null || mCamera.isCameraOpen()) return;
//        mCamera.openCamera(1, surfaceTexture, listener);
        mCamera.startPreview(surfaceTexture, listener);
    }

    public void startPreview(int cameraId, SurfaceTexture surfaceTexture, final PreviewCallback listener) {
        LogUtils.iTag(TAG, "startPreview() called with: cameraId = [" + cameraId + "], surfaceTexture = [" + "], listener = [" + "]");
        mPreviewCallback = listener;
        mSurfaceTexture = surfaceTexture;
        if (mSurfaceTexture == null || mCamera.isCameraOpen()) return;
        mCamera.openCamera(cameraId, surfaceTexture, listener);
    }

    public void setExposureCompensation(int progress) {
        mCamera.setExposureCompensation(progress);
    }

    public void setMeteringArea(Rect rect) {
        mCamera.setMeteringArea(rect);
    }

    public void handleZoom(boolean isZoom) {

    }

    public void startPreview() {
    }

    // 设置预览分辨率
    public void setPreviewSize(int width, int height, SurfaceTexture surfaceTexture) {
        mCamera.setPreviewSize(width, height, surfaceTexture);
    }

    // 支持的分辨率列表
    public ArrayList<String> getSupportedPreviewSize(String[] previewSizes) {
        return mCamera.getSupportedPreviewSize(previewSizes);
    }

    public Object getCamera() {
        return mCamera.getCamera();
    }

    // 前后摄像头切换
    public void changeCamera(final int cameraId) {
        LogUtils.iTag(TAG, "changeCamera() called with: cameraId = [" + cameraId + "]");
        mCamera.changeCamera(cameraId);
    }

    // 获取方向
    public int getOrientation() {
        return mCamera.getOrientation();
    }

    // camera 是否已开启
    public boolean isCameraOpen() {
        return mCamera.isCameraOpen();
    }

    public boolean cameraOpenFailed() {
        return !mCamera.isCameraOpen();
    }

    // 是否水平翻转(如果是前置)
    public boolean isFlipHorizontal() {
        return mCamera.isFlipHorizontal();
    }

    // 是否垂直翻转
    public boolean isFlipVertical() {
        return mCamera.isFlipVertical();
    }

    public void stopPreview() {
    }

    // 获取摄像头数量
    public int getNumberOfCameras() {
        return mCamera.getNumberOfCameras();
    }

    // 获取预览的宽
    public int getPreviewWidth() {
        return mCamera.getPreviewWH()[0];
    }

    // 获取预览的高
    public int getPreviewHeight() {
        return mCamera.getPreviewWH()[1];
    }

    // 相机释放
    public void releaseCamera() {
        mCamera.releaseCamera();
    }
}
