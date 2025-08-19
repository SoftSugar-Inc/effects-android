package softsugar.senseme.com.effects.camera;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;

import java.util.ArrayList;

public interface CameraInterface {

    int CAMERA_BACK = Camera.CameraInfo.CAMERA_FACING_BACK;
    int CAMERA_FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT;

    void init(Context context, GLSurfaceView surfaceView);

    // 打开相机
    void openCamera(final int cameraId, int width, int height, final PreviewCallback listener);

    void startPreview(SurfaceTexture surfaceTexture);

    void startPreview(SurfaceTexture surfaceTexture, PreviewCallback previewCallback);

    // 打开相机
    void openCamera(final int cameraId, SurfaceTexture surfaceTexture, final PreviewCallback listener);


    // 获取预览方向
    int getOrientation();

    Object getCamera();

    // 设置分辨率
    void setPreviewSize(int width, int height, SurfaceTexture surfaceTexture);

    ArrayList<String> getSupportedPreviewSize(String[] previewSizes);

    // 前后置切换
    void changeCamera(final int cameraId);

    // 获取预览宽高
    int[] getPreviewWH();

    void setMeteringArea(Rect rect);

    void setExposureCompensation(int progress);

    // 获取摄像头数量
    int getNumberOfCameras();

    // camera是否已开启
    boolean isCameraOpen();

    // 释放相机
    void releaseCamera();

    // 是否水平翻转
    boolean isFlipHorizontal();

    // 是否垂直翻转
    boolean isFlipVertical();

}
