package softsugar.senseme.com.effects.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import com.blankj.utilcode.util.LogUtils;

import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import softsugar.senseme.com.effects.utils.Constants;

/**
 * Camera2相关
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2 implements CameraInterface {
    private static final String TAG = "Camera2T";

    private CameraManager cameraManager;
    private String mCameraId;
    private Context mContext;
    private CameraCaptureSession mCameraCaptureSession;
    private static CameraDevice mCameraDevice;
    private HandlerThread mCameraThread;
    private Handler mCameraHandler;
    private CaptureRequest mCaptureRequest;
    private boolean isCameraOpen;
    private CameraCharacteristics cameraInfo;
    private Size[] outputSizes;
    private static Range<Integer>[] fpsRanges;

    // 默认预览分辨率
    private int previewWidth = 1280;
    private int previewHeight = 720;
    private GLSurfaceView mGLSurfaceView;

    @Override
    public void init(Context context, GLSurfaceView surfaceView) {
        mGLSurfaceView = surfaceView;
        mContext = context;
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }

    private CountDownLatch openCameraCountDownLatch = new CountDownLatch(1);

    @Override
    public void openCamera(int cameraId, int width, int height, PreviewCallback listener) {
        isCameraOpen = false;
        openCameraCountDownLatch = new CountDownLatch(1);
        LogUtils.iTag(TAG, "openCamera() called with: cameraId = [" + cameraId + "], width = [" + width + "], height = [" + height + "]");
        if (width!=0) {
            previewWidth = width;
            previewHeight = height;
        }
        releaseCamera();
        mListener = listener;
        try {
            for (String id : cameraManager.getCameraIdList()) {
                //Log.d(TAG, "openCamera() called with: cameraId = [" + id + "], surfaceTexture = [" + surfaceTexture + "], listener = [" + listener + "]");
                cameraInfo = cameraManager.getCameraCharacteristics(id);
                if (cameraInfo.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                }
                mCameraId = cameraId + "";

                // 该相机的FPS范围
                fpsRanges = cameraInfo.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
                Log.e(TAG, "SYNC_MAX_LATENCY_PER_FRAME_CONTROL: " + Arrays.toString(fpsRanges));

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        try {
            //检查权限
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //打开相机，第一个参数指示打开哪个摄像头，第二个参数stateCallback为相机的状态回调接口，第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            LogUtils.iTag(TAG, "openCamera: ${mCameraId}:" + mCameraId);
            cameraInfo = cameraManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap streamConfigurationMap = cameraInfo.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            outputSizes = streamConfigurationMap.getOutputSizes(ImageFormat.JPEG);
            cameraManager.openCamera(mCameraId, stateCallback, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        try {
            openCameraCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openCamera(int cameraId, SurfaceTexture surfaceTexture, PreviewCallback listener) {

    }

    private PreviewCallback mListener;
//    private Handler mainHandler = new Handler(Looper.getMainLooper());


    @Override
    public void setPreviewSize(int width, int height, SurfaceTexture surfaceTexture) {
        previewWidth = width;
        previewHeight = height;
        if (mCameraDevice == null || !isCameraOpen) return;
        releaseCamera();
        mImageData = null;
        openCamera(Integer.valueOf(mCameraId), surfaceTexture, mListener);
    }

    @Override
    public ArrayList<String> getSupportedPreviewSize(String[] previewSizes) {
        ArrayList<String> result = new ArrayList<>();
//        for (String candidate : previewSizes) {
//            int index = candidate.indexOf('x');
//            if (index == -1) continue;
//            int width = Integer.parseInt(candidate.substring(0, index));
//            int height = Integer.parseInt(candidate.substring(index + 1));
//            for (Size s : outputSizes) {
//                if ((s.getWidth() == width) && (s.getHeight() == height)) {
//                    result.add(candidate);
//                }
//            }
//        }

        //"1280x720", "640x480", "1920x1080"
        result.add("1280x720");
        result.add("640x480");
        result.add("1920x1080");
        return result;
    }

    @Override
    public void changeCamera(int cameraId) {
        if (mCameraDevice == null) return;
        LogUtils.iTag(TAG, "changeCamera() called with: cameraId = [" + cameraId + "]");
        releaseCamera();
//        openCamera(cameraId, mListener);
    }

    @Override
    public int[] getPreviewWH() {
        return new int[]{previewWidth, previewHeight};
    }

    @Override
    public void setMeteringArea(Rect rect) {

    }

    @Override
    public void setExposureCompensation(int progress) {

    }

    @Override
    public int getNumberOfCameras() {
        int num = 0;
        try {
            String[] array = cameraManager.getCameraIdList();
            num = array.length;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return num;
    }

    @Override
    public boolean isCameraOpen() {
        return isCameraOpen;
    }

    @Override
    public void releaseCamera() {
        isCameraOpen = false;
        closePreviewSession();
        LogUtils.iTag(TAG, "releaseCamera() called");
//        closePreviewSession();
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }

        if (null != mImageReader) {
            mImageReader.close();
            mImageReader = null;
        }

        if (null != mCameraCaptureSession) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
    }

    @Override
    public boolean isFlipHorizontal() {
        // 对于一般手机而言
        // 后置摄像头一般为 “0”，常量值为 CameraCharacteristics.LENS_FACING_FRONT；
        // 前置摄像头一般为 “1”，常量值为 CameraCharacteristics.LENS_FACING_BACK
        if (mCameraDevice == null) return false;
        int cameraId = cameraInfo.get(CameraCharacteristics.LENS_FACING);
        LogUtils.iTag(TAG, "isFlipHorizontal: " + cameraId);
        return cameraId == CameraCharacteristics.LENS_FACING_FRONT;
    }

    @Override
    public boolean isFlipVertical() {
        if (mCameraDevice == null) return false;
        int orientation = getOrientation();
        return orientation == 90 || orientation == 270;
    }

    @Override
    public int getOrientation() {
        if (mCameraDevice == null) return -1;
        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(mCameraId);
            return characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public CameraManager getCamera() {
        return cameraManager;
    }

    private ImageReader mImageReader;
    private CaptureRequest.Builder mCaptureRequestBuilder;

    @Override
    public void startPreview(SurfaceTexture surfaceTexture, PreviewCallback previewCallback) {
        mListener = previewCallback;
        try {
            surfaceTexture.setDefaultBufferSize(previewWidth, previewHeight);
            Surface surface = new Surface(surfaceTexture);
            try {
                mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                LogUtils.iTag(TAG, "startPreview() called" + previewWidth + "*" + previewHeight);
                mImageReader = ImageReader.newInstance(previewWidth, previewHeight, ImageFormat.YUV_420_888, 2);

                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mCameraHandler);
                mCaptureRequestBuilder.addTarget(mImageReader.getSurface());

                mCaptureRequestBuilder.addTarget(surface);
                mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        try {
                            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_SCENE_MODE_FACE_PRIORITY);// CONTROL_MODE_AUTO
                            mCaptureRequestBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, CameraMetadata.STATISTICS_FACE_DETECT_MODE_SIMPLE);

                            mCaptureRequest = mCaptureRequestBuilder.build();
                            mCameraCaptureSession = session;
                            mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, mCameraHandler);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, mCameraHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startPreview(SurfaceTexture surfaceTexture) {
        try {
            surfaceTexture.setDefaultBufferSize(previewWidth, previewHeight);
            Surface surface = new Surface(surfaceTexture);
            try {
                mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                LogUtils.iTag(TAG, "startPreview() called" + previewWidth + "*" + previewHeight);
                mImageReader = ImageReader.newInstance(previewWidth, previewHeight, ImageFormat.YUV_420_888, 2);
                if (Constants.ENABLE_25FPS) {
                    mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, new Range<>(25,25));
                }

                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mCameraHandler);
                mCaptureRequestBuilder.addTarget(mImageReader.getSurface());

                mCaptureRequestBuilder.addTarget(surface);
                mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        try {
                            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_SCENE_MODE_FACE_PRIORITY);// CONTROL_MODE_AUTO
                            mCaptureRequestBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, CameraMetadata.STATISTICS_FACE_DETECT_MODE_SIMPLE);

                            mCaptureRequest = mCaptureRequestBuilder.build();
                            mCameraCaptureSession = session;
                            mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, mCameraHandler);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, mCameraHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            //Log.d(TAG, "onImageAvailable: ------");
            if (!isCameraOpen) return;
            Image image = reader.acquireLatestImage();
            byte[] nv21Data = yuv_420_8888_to_nv21(image);
            if (null!=mListener)
            mListener.onPreviewFrame(mImageData);
//            image.close();
        }
    };

    private byte[] mImageData;
    private Object mImageDataLock = new Object();

    private byte[] yuv_420_8888_to_nv21(Image image) {
        if (image == null) return null;
        try {
            ByteBuffer bufferY = image.getPlanes()[0].getBuffer();
            byte[] dataY = new byte[bufferY.remaining()];
            bufferY.get(dataY);
            ByteBuffer bufferUV = image.getPlanes()[2].getBuffer();
            byte[] dataUV = new byte[bufferUV.remaining()];
            bufferUV.get(dataUV);

            try {
                image.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (mImageData == null || mImageData.length != previewWidth * previewHeight * 3 / 2) {
                    mImageData = new byte[previewWidth * previewHeight * 3 / 2];
                }
                synchronized (mImageDataLock) {
                    System.arraycopy(dataY, 0, mImageData, 0, dataY.length);
                    System.arraycopy(dataUV, 0, mImageData, previewWidth * previewHeight, dataUV.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mImageData;
    }

    // 摄像头状态回调
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            // 开启预览
//            startPreview();
            isCameraOpen = true;
            openCameraCountDownLatch.countDown();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            LogUtils.iTag(TAG, "CameraDevice Disconnected");
            mCameraDevice = camera;
            releaseCamera();
            resetCameraVariables();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.e(TAG, "CameraDevice Error" + error);
            mCameraDevice = camera;
            releaseCamera();
            resetCameraVariables();
        }
    };

    private void resetCameraVariables() {
        mCameraDevice = null;
        cameraInfo = null;
    }

    public void closePreviewSession() {
        if (mCameraCaptureSession != null && mCameraDevice != null) {
            try {
                mCameraCaptureSession.stopRepeating();
                mCameraCaptureSession.abortCaptures();
                mCameraCaptureSession.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
