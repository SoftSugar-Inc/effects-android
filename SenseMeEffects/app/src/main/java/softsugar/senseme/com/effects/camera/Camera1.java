package softsugar.senseme.com.effects.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Camera1相关
 */
public class Camera1 implements CameraInterface {
    private static final String TAG = "Camera1T";
    private Camera mCamera;
    private int mCameraId;

    private Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
    private int maxExposureCompensation, minExposureCompensation;

    private boolean isCameraOpen = false;
    private boolean mCameraOpenFailed = false;
    private int previewWidth = 1280;
    private int previewHeight = 720;

    @Override
    public void init(Context context, GLSurfaceView surfaceView) {

    }

    @Override
    public void openCamera(int cameraId, int width, int height, PreviewCallback listener) {
        LogUtils.iTag(TAG, "openCamera() called with: cameraId = [" + cameraId + "], width = [" + width + "], height = [" + height + "]");
        if (width!=0) {
            previewWidth = width;
            previewHeight = height;
        }
        releaseCamera();
        mCallback = listener;
        try {
            releaseCamera();
            mCamera = Camera.open(cameraId);
            mCamera.getParameters();
            mCameraId = cameraId;
            mCamera.getCameraInfo(cameraId, mCameraInfo);

            setDefaultParameters();

            isCameraOpen = true;
            mCameraOpenFailed = false;
        } catch (Exception e) {
            mCameraOpenFailed = true;
            mCamera = null;
            LogUtils.iTag(TAG, "openCamera fail msg=" + e.getMessage());
//            return false;
        }
    }

    @Override
    public void startPreview(SurfaceTexture surfaceTexture) {
        try {
            if (mCamera == null) {
                return;
            }
            if (surfaceTexture != null && mCamera != null)
                mCamera.setPreviewTexture(surfaceTexture);

            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startPreview(SurfaceTexture surfaceTexture, PreviewCallback previewCallback) {
        mCallback = previewCallback;
        try {
            if (mCamera == null) {
                return;
            }
            if (surfaceTexture != null && mCamera != null)
                mCamera.setPreviewTexture(surfaceTexture);

            if (mPreviewCallback != null && mCamera != null) {
                mCamera.setPreviewCallbackWithBuffer(mPreviewCallback);
                mCamera.addCallbackBuffer(new byte[previewWidth * previewHeight * ImageFormat.getBitsPerPixel(ImageFormat.NV21) / 8]);
                mCamera.addCallbackBuffer(new byte[previewWidth * previewHeight * ImageFormat.getBitsPerPixel(ImageFormat.NV21) / 8]);
                mCamera.addCallbackBuffer(new byte[previewWidth * previewHeight * ImageFormat.getBitsPerPixel(ImageFormat.NV21) / 8]);
            }
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PreviewCallback mCallback;

    @Override
    public void openCamera(int cameraId, SurfaceTexture surfaceTexture, PreviewCallback listener) {
        releaseCamera();
        mCallback = listener;
        try {
            releaseCamera();
            mCamera = Camera.open(cameraId);
            mCamera.getParameters();
            mCameraId = cameraId;
            mCamera.getCameraInfo(cameraId, mCameraInfo);

            setDefaultParameters();

            isCameraOpen = true;
            mCameraOpenFailed = false;
        } catch (Exception e) {
            mCameraOpenFailed = true;
            mCamera = null;
            LogUtils.iTag(TAG, "openCamera fail msg=" + e.getMessage());
//            return false;
        }
//        return true;
    }

    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            LogUtils.iTag(TAG, "onPreviewFrame onPreviewFrame: -----");
            if (mCallback!=null) {
                mCallback.onPreviewFrame(data);
                camera.addCallbackBuffer(data);
            }
        }
    };

    private void setDefaultParameters() {
        Camera.Parameters parameters = mCamera.getParameters();
        Log.e(TAG, "parameters: " + parameters.flatten());
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes != null && flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }

        Point previewSize = getSuitablePreviewSize();
        //	parameters.setPreviewSize(previewSize.x, previewSize.y);
        parameters.setPreviewSize(previewWidth, previewHeight);
        Point pictureSize = getSuitablePictureSize();
        parameters.setPictureSize(pictureSize.x, pictureSize.y);
//		mCamera.setFaceDetectionListener(new MyFaceDetectionListener());
        mCamera.setParameters(parameters);
        maxExposureCompensation = parameters.getMaxExposureCompensation() / 2;
        minExposureCompensation = parameters.getMinExposureCompensation() / 2;

        setCameraFps(mCamera, 25);
    }

    public void setCameraFps(Camera camera, int targetFps) {
        if (camera == null) {
            return;
        }

        Camera.Parameters parameters = camera.getParameters();

        // 将目标帧率转换为 1,000 的倍数，因为大部分设备以此单位处理帧率
        int targetFpsInThousand = targetFps * 1000;

        // 获取支持的预览帧率范围
        List<int[]> supportedFpsRanges = parameters.getSupportedPreviewFpsRange();

        // 选择一个最接近目标帧率的范围
        int[] bestRange = null;
        for (int[] range : supportedFpsRanges) {
            if (range[0] <= targetFpsInThousand && range[1] >= targetFpsInThousand) {
                bestRange = range;
                break;
            }
        }

        // 如果找到了合适的范围，设置帧率范围
        if (bestRange != null) {
            parameters.setPreviewFpsRange(bestRange[0], bestRange[1]);
        } else {
            // 如果没有精确匹配到目标帧率，可以尝试设置帧率为 targetFps
            parameters.setPreviewFrameRate(targetFps);
        }

        // 设置其他参数
        camera.setParameters(parameters);
    }

    private Point getSuitablePreviewSize() {
        Point defaultsize = new Point(1920, 1080);
        if (mCamera != null) {
            List<Camera.Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
            for (Camera.Size s : sizes) {
                if ((s.width == defaultsize.x) && (s.height == defaultsize.y)) {
                    return defaultsize;
                }
            }
            return new Point(640, 480);
        }
        return null;
    }

    private Point getSuitablePictureSize() {
        Point defaultsize = new Point(4608, 3456);
        //	Point defaultsize = new Point(3264, 2448);
        if (mCamera != null) {
            Point maxSize = new Point(0, 0);
            List<Camera.Size> sizes = mCamera.getParameters().getSupportedPictureSizes();
            for (Camera.Size s : sizes) {
                if ((s.width == defaultsize.x) && (s.height == defaultsize.y)) {
                    return defaultsize;
                }
                if (maxSize.x < s.width) {
                    maxSize.x = s.width;
                    maxSize.y = s.height;
                }
            }
            return maxSize;
        }
        return null;
    }

    public void setExposureCompensation(int progerss) {
        Camera.Parameters params = mCamera.getParameters();
        int value = 0;
        if (progerss >= 50) {
            int tmp = progerss - 50;
            tmp = tmp * maxExposureCompensation;
            value = tmp / 50;
        } else {
            int tmp = 50 - progerss;
            tmp = minExposureCompensation * tmp;
            value = tmp / 50;
        }
        params.setExposureCompensation(value);
        mCamera.setParameters(params);
    }

    @Override
    public int getOrientation() {
        if(mCameraInfo == null){
            return 0;
        }
        return mCameraInfo.orientation;
    }

    @Override
    public Object getCamera() {
        return mCameraInfo;
    }

    @Override
    public void setPreviewSize(int width, int height, SurfaceTexture surfaceTexture) {
        LogUtils.iTag(TAG, "setPreviewSize() called with: width = [" + width + "], height = [" + height + "]");
        if (mCamera == null)
            return;
        mCamera.stopPreview();
        previewWidth = width;
        previewHeight = height;
//        Camera.Parameters parameters = mCamera.getParameters();
//        parameters.setPreviewSize(width, height);
        if (mCamera == null)
            return;
        //mCamera.setParameters(parameters);

        previewWidth = width;
        previewHeight = height;
        openCamera(mCameraId, surfaceTexture, mCallback);
    }

    @Override
    public ArrayList<String> getSupportedPreviewSize(String[] previewSizes) {
        ArrayList<String> result = new ArrayList<String>();
//        if (mCamera != null) {
//            List<Camera.Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
//            for (String candidate : previewSizes) {
//                int index = candidate.indexOf('x');
//                if (index == -1) continue;
//                int width = Integer.parseInt(candidate.substring(0, index));
//                int height = Integer.parseInt(candidate.substring(index + 1));
//                for (Camera.Size s : sizes) {
//                    if ((s.width == width) && (s.height == height)) {
//                        result.add(candidate);
//                    }
//                }
//            }
//        }
        result.add("1280x720");
        result.add("640x480");
        result.add("1920x1080");
        return result;
    }

    @Override
    public void changeCamera(int cameraId) {

    }

    @Override
    public int[] getPreviewWH() {
        return new int[0];
    }

    @Override
    public void setMeteringArea(Rect rect) {
        List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
        meteringAreas.add(new Camera.Area(rect, 1));

        if(mCamera != null){
            try{
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setMeteringAreas(meteringAreas);
                mCamera.setParameters(parameters);
            }catch (Exception e){
                Log.e(TAG, "onFaceDetection exception: " + e.getMessage());
            }
        }
    }

    @Override
    public int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }

    @Override
    public boolean isCameraOpen() {
        return isCameraOpen;
    }

    @Override
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
            isCameraOpen = false;
        }
    }

    @Override
    public boolean isFlipHorizontal() {
        if (mCameraInfo == null) {
            return false;
        }
        return mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ? true : false;
    }

    @Override
    public boolean isFlipVertical() {
        if (mCameraInfo == null) {
            return false;
        }
        return (mCameraInfo.orientation == 90 || mCameraInfo.orientation == 270) ? true : false;
    }

}
