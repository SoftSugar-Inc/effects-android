package com.softsugar.stmobile.engine;

import android.content.Context;
import android.hardware.SensorEvent;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import com.softsugar.stmobile.STCommonNative;
import com.softsugar.stmobile.STEffectInImage;
import com.softsugar.stmobile.STMobileAnimalNative;
import com.softsugar.stmobile.STMobileColorConvertNative;
import com.softsugar.stmobile.STMobileEffectNative;
import com.softsugar.stmobile.STMobileEffectParams;
import com.softsugar.stmobile.STMobileHumanActionNative;
import com.softsugar.stmobile.STMobileObjectTrackNative;
import com.softsugar.stmobile.engine.glutils.Accelerometer;
import com.softsugar.stmobile.engine.glutils.OpenGLUtils;
import com.softsugar.stmobile.engine.glutils.STEffectsInput;
import com.softsugar.stmobile.engine.glutils.STEffectsOutput;
import com.softsugar.stmobile.engine.glutils.STGLRender;
import com.softsugar.stmobile.engine.glutils.STImageBuffer;
import com.softsugar.stmobile.engine.glutils.STLogUtils;
import com.softsugar.stmobile.engine.glutils.STTextureToBufferManager;
import com.softsugar.stmobile.engine.glutils.STUtils;
import com.softsugar.stmobile.model.STEffect3DBeautyPartInfo;
import com.softsugar.stmobile.model.STEffectBeautyInfo;
import com.softsugar.stmobile.model.STEffectCustomParam;
import com.softsugar.stmobile.model.STEffectModuleInfo;
import com.softsugar.stmobile.model.STEffectRenderInParam;
import com.softsugar.stmobile.model.STEffectRenderOutParam;
import com.softsugar.stmobile.model.STEffectTexture;
import com.softsugar.stmobile.model.STEffectTryonInfo;
import com.softsugar.stmobile.model.STFaceExtraInfo;
import com.softsugar.stmobile.model.STFaceMeshList;
import com.softsugar.stmobile.model.STGanReturn;
import com.softsugar.stmobile.model.STHumanAction;
import com.softsugar.stmobile.model.STImage;
import com.softsugar.stmobile.model.STMobileAnimalResult;
import com.softsugar.stmobile.model.STQuaternion;
import com.softsugar.stmobile.model.STRect;
import com.softsugar.stmobile.params.STHumanActionParamsType;
import com.softsugar.stmobile.params.STResultCode;
import com.softsugar.stmobile.params.STRotateType;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class STEffectsEngine implements STEffectsEngineI {
    private static final String TAG = "STEffectEngine";
    private boolean mCheckLicense;
    private Context mContext;
    private int mRenderMode;
    private Accelerometer mAccelerometer;
    private int mHumanActionCreateConfig = STCommonNative.ST_MOBILE_DETECT_MODE_PREVIEW;
    private STMobileHumanActionNative mSTMobileHumanActionNative;
    private STMobileAnimalNative mSTMobileAnimalNative;
    private STMobileEffectNative mSTMobileEffectNative;
    private STMobileObjectTrackNative mSTMobileObjectTrackNative;
    private STMobileColorConvertNative mSTMobileColorConvertNative;
    private STGLRender mSTGLRender;
    private int mTextureRotate;
    private int mOverlappedBeautyCount;

    private int[] mCameraInputTexture;
    private int[] mRenderOrientations = new int[2];
    private int[] mEffectTextureId;
    private boolean mShowOrigin;

    private STTextureToBufferManager mSTTextureToBufferManager;
    private boolean enableOutputEffectImageBuffer;

    @Override
    public boolean checkLicenseFromBuffer(Context context, byte[] licBuffer) {
        mContext = context;
        mCheckLicense = STLicenseUtils.checkLicenseFromBuffer(context, licBuffer, false);
        return mCheckLicense;
    }

    public static void setEnv(String systemPath) {
        STCommonNative.setEnv(systemPath);
    }

    @Override
    public int init(Context context, int renderMode) {

        mContext = context;
        mRenderMode = renderMode;
        mAccelerometer = new Accelerometer(context);
        mAccelerometer.start();

        if(mRenderMode == STRenderMode.IMAGE.getMode()){
            mHumanActionCreateConfig = STCommonNative.ST_MOBILE_DETECT_MODE_IMAGE;
        }else if(mRenderMode == STRenderMode.VIDEO.getMode()){
            mHumanActionCreateConfig = STCommonNative.ST_MOBILE_DETECT_MODE_VIDEO;
        } else if (mRenderMode == STRenderMode.VIDEO_POST_PROCESS.getMode()) {
            mHumanActionCreateConfig = STCommonNative.ST_MOBILE_DETECT_MODE_VIDEO_POST_PROCESS;
        }

        int result = STResultCode.ST_OK.getResultCode();
        if(mSTMobileHumanActionNative == null){
            mSTMobileHumanActionNative = new STMobileHumanActionNative();
            result = mSTMobileHumanActionNative.createInstance(null, mHumanActionCreateConfig);
            Log.e(TAG, "init human result: "+ result );

            // 背景分割羽化程度[0,1](默认值0.35),0 完全不羽化,1羽化程度最高,在strenth较小时,羽化程度基本不变.值越大,前景与背景之间的过度边缘部分越宽.
            mSTMobileHumanActionNative.setParam(STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_BACKGROUND_BLUR_STRENGTH, 0.35f);
            // 设置face mesh结果输出坐标系,(0: 屏幕坐标系， 1：3d世界坐标系， 2:3d摄像机坐标系,是摄像头透视投影坐标系, 原点在摄像机 默认是0）
            mSTMobileHumanActionNative.setParam(STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_FACE_MESH_OUTPUT_FORMAT, 1.0f);
            // 设置mesh渲染模式
            mSTMobileHumanActionNative.setParam(STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_MESH_MODE, STCommonNative.MESH_CONFIG);
            // 设置人头实例分割
            mSTMobileHumanActionNative.setParam(STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_HEAD_SEGMENT_INSTANCE, 1.0f);
        }

        if(mSTMobileAnimalNative == null){
            mSTMobileAnimalNative = new STMobileAnimalNative();
            result = mSTMobileAnimalNative.createInstance(null, mHumanActionCreateConfig);
            Log.e(TAG, "init animal result: "+ result );
        }

        if(mSTMobileEffectNative == null){
            mSTMobileEffectNative = new STMobileEffectNative();
            result = mSTMobileEffectNative.createInstance(mContext,
                    (mRenderMode == 1|| mRenderMode == 3) ? STMobileEffectNative.EFFECT_CONFIG_IMAGE_MODE : STMobileEffectNative.EFFECT_CONFIG_NONE);
            mSTMobileEffectNative.setParam(STMobileEffectParams.EFFECT_PARAM_QUATERNION_SMOOTH_FRAME, 5);

            mSTMobileEffectNative.setListener(new EffectNativeListener(mSTMobileHumanActionNative));

            Log.e(TAG, "init effects result: "+ result );
        }

        if(mSTMobileObjectTrackNative == null){
            mSTMobileObjectTrackNative = new STMobileObjectTrackNative();
            result = mSTMobileObjectTrackNative.createInstance();
            Log.e(TAG, "init object result: "+ result );
        }

        return result;
    }

    private static class EffectNativeListener implements STMobileEffectNative.Listener {
        private STMobileHumanActionNative mHumanActionNative;

        public EffectNativeListener(STMobileHumanActionNative humanActionNative) {
            mHumanActionNative = humanActionNative;
        }

        @Override
        public void packageStateChange(int packageState, int packageId) {

        }

        @Override
        public void greenSegment(int color) {
            if (null == mHumanActionNative) return;
            mHumanActionNative.setParam((int) STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_GREEN_SEGMENT_COLOR, color);
        }
    }

    @Override
    public int loadSubModel(String modelPath) {
        int result = STResultCode.ST_OK.getResultCode();
        if(mSTMobileHumanActionNative != null){
            result = mSTMobileHumanActionNative.addSubModel(modelPath);
            Log.e(TAG, "loadSubModel modelPath=" + modelPath + "ret=" + result );
        }
        return result;
    }

    @Override
    public int loadSubModelFromBuffer(byte[] modelBuffer) {
        int result = STResultCode.ST_OK.getResultCode();
        if(mSTMobileHumanActionNative != null && modelBuffer != null){
            result = mSTMobileHumanActionNative.addSubModelFromBuffer(modelBuffer, modelBuffer.length);
            Log.e(TAG, "loadSubModel result: "+ result );
        }
        return result;
    }

    @Override
    public int loadAnimalSubModel(String modelPath) {
        int result = STResultCode.ST_OK.getResultCode();
        if(mSTMobileAnimalNative != null){
            result = mSTMobileAnimalNative.addSubModel(modelPath);
            Log.e(TAG, "loadSubModel result: "+ result );
        }
        return result;
    }

    @Override
    public int loadAnimalSubModelFromBuffer(byte[] modelBuffer) {
        int result = STResultCode.ST_OK.getResultCode();
        if(mSTMobileAnimalNative != null && modelBuffer != null){
            result = mSTMobileAnimalNative.addSubModelFromBuffer(modelBuffer, modelBuffer.length);
            Log.e(TAG, "animal addSubModelFromBuffer result: "+ result );
        }
        return result;
    }

    @Override
    public int addPackage(String path) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        ret = mSTMobileEffectNative.addPackage(path);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();
        getTriggerActions();

        mAnimalDetectConfig = mSTMobileEffectNative.getAnimalDetectConfig();

        return ret;
    }

    private void getTriggerActions() {
        mHumanTriggerActions = 0;
        if ((mHumanDetectConfig & STMobileHumanActionNative.ST_MOBILE_DETECT_HAND_GESTURE) > 0) {
            long config = mHumanDetectConfig & STMobileHumanActionNative.ST_MOBILE_DETECT_HAND_GESTURE;
            mHumanTriggerActions = mSTMobileEffectNative.getHumanTriggerActions(config);
        } else {
            mHumanTriggerActions = mHumanDetectConfig;
        }
    }

    public long getHumanTriggerActions() {
        return mHumanTriggerActions;
    }

    @Override
    public int addPackageFromBuffer(byte[] buffer) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null || buffer == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        ret = mSTMobileEffectNative.changePackageFromBuffer(buffer, buffer.length);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();
        getTriggerActions();
        mAnimalDetectConfig = mSTMobileEffectNative.getAnimalDetectConfig();

        return ret;
    }

    @Override
    public int removeEffectById(int id) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        ret = mSTMobileEffectNative.removeEffect(id);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();
        mAnimalDetectConfig = mSTMobileEffectNative.getAnimalDetectConfig();
        return ret;
    }

    @Override
    public int changeStyle(int oldStyleId, String path) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        if (oldStyleId!=-1) {
            ret = mSTMobileEffectNative.removeEffect(oldStyleId);
        }
        ret = mSTMobileEffectNative.addPackage(path);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();
        mAnimalDetectConfig = mSTMobileEffectNative.getAnimalDetectConfig();
        return ret;
    }

    @Override
    public void clearAll() {
        if(mSTMobileEffectNative == null) return;
        mSTMobileEffectNative.clear();
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();
        mAnimalDetectConfig = mSTMobileEffectNative.getAnimalDetectConfig();
    }

    @Override
    public int setPackageBeautyGroupStrength(int packageId, int beautyGroup, float strength) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        ret = mSTMobileEffectNative.setPackageBeautyGroupStrength(packageId, beautyGroup, strength);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();

        return ret;
    }

    @Override
    public int setBeautyMode(int param, int value) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        ret = mSTMobileEffectNative.setBeautyMode(param, value);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();

        return ret;
    }

    @Override
    public int setBeauty(int param, String path) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        ret = mSTMobileEffectNative.setBeauty(param, path);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();

        return ret;
    }

    @Override
    public int setBeautyFromSDPath(int param, String path) {
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();
        return  mSTMobileEffectNative.setBeauty(param, path);
    }


    //gl thread
    @Override
    public int setBeautyFromBuffer(int param, byte[] buffer) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();
        set3dBeauty(param, buffer);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();
        return ret;
    }

    private void set3dBeauty(final int param, final byte[] buffer) {
        if (buffer == null) {
            mSTMobileEffectNative.setBeauty(param, null);
        } else {
            mSTMobileHumanActionNative.setParam(STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_BACKGROUND_BLUR_STRENGTH, 0.35f);
            mSTMobileHumanActionNative.setParam(STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_FACE_MESH_OUTPUT_FORMAT, 1.0f);
            mSTMobileHumanActionNative.setParam(STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_MESH_MODE, STCommonNative.MESH_CONFIG);
            STFaceMeshList meshList = mSTMobileHumanActionNative.getMeshList(STHumanActionParamsType.STMeshType.ST_MOBILE_FACE_MESH);

            mSTMobileEffectNative.setBeautyFromBuffer(param, buffer, buffer.length);
            mSTMobileEffectNative.setFaceMeshList(meshList);
        }
    }

    @Override
    public int setEffectParams(int param, float value){
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        ret = mSTMobileEffectNative.setParam(param, value);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();

        return ret;
    }

    @Override
    public int setBeautyStrength(int type, float value) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        Log.e(TAG, "setBeautyStrength: "+ type+ " "+ value );
        ret = mSTMobileEffectNative.setBeautyStrength(type, value);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();

        return ret;
    }

    @Override
    public int setBeautyParam(int type, float value) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        ret = mSTMobileEffectNative.setBeautyParam(type, value);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();

        return ret;
    }

    @Override
    public STEffect3DBeautyPartInfo[] get3DBeautyParts(){
        if(mSTMobileEffectNative == null)
            return null;

        STEffect3DBeautyPartInfo[] infos = mSTMobileEffectNative.get3DBeautyParts();
        return infos;
    }

    @Override
    public int set3dBeautyPartsStrength(STEffect3DBeautyPartInfo[] effect3DBeautyPartInfo, int length) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        ret = mSTMobileEffectNative.set3dBeautyPartsStrength(effect3DBeautyPartInfo, length);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();

        return ret;
    }

    @Override
    public int setTryOnParam(STEffectTryonInfo info, int type) {
        int ret = STResultCode.ST_OK.getResultCode();
        if(mSTMobileEffectNative == null)
            return STResultCode.ST_E_HANDLE.getResultCode();

        ret = mSTMobileEffectNative.setTryOnParam(info, type);
        mHumanDetectConfig = mSTMobileEffectNative.getHumanActionDetectConfig();

        return ret;
    }

    @Override
    public STEffectTryonInfo getTryOnParam(int tryOnType) {
        if(mSTMobileEffectNative == null)
            return null;

        STEffectTryonInfo info = mSTMobileEffectNative.getTryOnParam(tryOnType);
        return info;
    }

    private int mImageWidth, mImageHeight, mDetectWidth, mDetectHeight;


    protected SensorEvent mSensorEvent;
    private final ExecutorService mDetectThreadPool = Executors.newFixedThreadPool(1);
    private CountDownLatch mCountDownLatch = new CountDownLatch(0);
    private int mCameraInputTextureIndex, mCameraInputBufferIndex;
    private Object mHumanActionLock = new Object();
    private final int[] mNativeRenderOrientations = new int[3];
    private STImageBuffer[] mDetectImageBuffers = new STImageBuffer[2];
    private long mHumanDetectConfig, mAnimalDetectConfig;
    private long mHumanTriggerActions;
    public static final float GPU_RESIZE_RATIO = 1.0f;
    int mCustomEvent;
    private STHumanAction stHumanAction;
    private int mCurrentFrameIndex;
    private boolean mPreFrameIsOrigin;
    private byte[] effectImageRgbaBuffer;

    @Override
    public int processTexture(STEffectsInput input, STEffectsOutput output) {

        if(input == null || input.getTexture() == null || !GLES30.glIsTexture(input.getTexture().getId())){
            Log.e(TAG, "input texture error");
            return STResultCode.ST_E_INVALIDARG.getResultCode();
        }

        if(output == null){
            output = new STEffectsOutput(input.getTexture().getId(), null, null);
        }

        int width = input.getTexture().getWidth();
        int height = input.getTexture().getHeight();
        if(input.getRotate() == 90 || input.getRotate() == 270){
            width = input.getTexture().getHeight();
            height = input.getTexture().getWidth();
        }

        mCurrentFrameIndex++;
        if(mImageWidth != width || mImageHeight != height){
            mImageWidth = width;
            mImageHeight = height;

            mDetectWidth = mImageWidth;
            mDetectHeight = mImageHeight;


            if(mRenderMode == STRenderMode.PREVIEW.getMode() && mSTTextureToBufferManager != null){
                mSTTextureToBufferManager.release();
                mSTTextureToBufferManager = null;
            }

            if(mSTGLRender != null){
                mSTGLRender.init(mImageWidth, mImageHeight, mDetectWidth, mDetectHeight);
                mSTGLRender.calculateVertexBuffer(mImageWidth, mImageHeight, mImageWidth, mImageHeight);
            }

            releaseTexturesAndBuffers();
            mCameraInputBufferIndex = 0;
            mCameraInputTextureIndex = 0;
            mCurrentFrameIndex = 0;
        }

        if(mRenderMode == STRenderMode.PREVIEW.getMode() && mSTTextureToBufferManager == null){
            mSTTextureToBufferManager = new STTextureToBufferManager();
            mSTTextureToBufferManager.init(mDetectWidth, mDetectHeight);
        }

        if(mSTGLRender == null){
            mSTGLRender = new STGLRender();
            mSTGLRender.init(mImageWidth, mImageHeight, mDetectWidth, mDetectHeight);
            mSTGLRender.calculateVertexBuffer(mImageWidth, mImageHeight, mImageWidth, mImageHeight);
            mTextureRotate = input.getRotate();
            mSTGLRender.adjustTextureBuffer(input.getRotate(), true, input.isNeedMirror());
        }

        if(mSTGLRender != null && mTextureRotate != input.getRotate()){
            mTextureRotate = input.getRotate();
            mSTGLRender.adjustTextureBuffer(input.getRotate(), true, input.isNeedMirror());
        }

        if (mCameraInputTexture == null) {
            mCameraInputTexture = new int[3];
            OpenGLUtils.initEffectTexture(mImageWidth, mImageHeight, mCameraInputTexture, GLES20.GL_TEXTURE_2D);
        }

        if(mEffectTextureId == null){
            mEffectTextureId = new int[1];
            OpenGLUtils.initEffectTexture(mImageWidth, mImageHeight, mEffectTextureId, GLES20.GL_TEXTURE_2D);
        }

        if(mDetectImageBuffers == null){
            mDetectImageBuffers = new STImageBuffer[2];
        }

        int rotate = input.getRotate()/90;

        int textureId = OpenGLUtils.NO_TEXTURE;
        if(mRenderMode == STRenderMode.PREVIEW.getMode() && !mSyncRender){
            long startPreprocess = System.currentTimeMillis();
            int[] resizedTexture = new int[1];//resize纹理，读取数据量小，速度快
            mCameraInputTexture[mCameraInputTextureIndex] = mSTGLRender.preProcessAndResizeTexture(input.getTexture().getId(), true, resizedTexture,null,
                    mCameraInputTextureIndex, input.getTexture().getFormat() == STTextureFormat.FORMAT_TEXTURE_OES.getFormat());
            mCameraInputBufferIndex = 1 - mCameraInputBufferIndex;
            mDetectImageBuffers[mCameraInputBufferIndex] = mSTTextureToBufferManager.textureToNv21Buffer(resizedTexture[0], mDetectWidth, mDetectHeight, mCameraInputBufferIndex);
            STLogUtils.i(TAG, "preprocess cost time: " + (System.currentTimeMillis() - startPreprocess));

            mCameraInputTextureIndex = (mCameraInputTextureIndex + 1) % 3;
            mCameraInputTextureIndex = (mCameraInputTextureIndex + 1) % 3;
            textureId = mCameraInputTexture[mCameraInputTextureIndex];

            if(mShowOrigin){
                mPreFrameIsOrigin = true;
                output.setTextureId(textureId);
            }else {
                try {
                    if(mCountDownLatch != null) mCountDownLatch.await();
                } catch (Exception e) {
                    Log.e(TAG, "lock wait: " + e.getMessage());
                }

                mCountDownLatch = new CountDownLatch(1);//reset CountDownLatch
                synchronized (mHumanActionLock) {
                    mSTMobileHumanActionNative.updateNativeHumanActionCache(1 - mCameraInputBufferIndex);
                }
                mDetectThreadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        int deviceOrientation = mAccelerometer == null ? 1 : mAccelerometer.getDirection();
                        int orientation = STUtils.getRenderOrientation(deviceOrientation);
                        mNativeRenderOrientations[mCameraInputBufferIndex] = orientation;//记录渲染时方向
                        synchronized (mHumanActionLock) {
                            long startHumanAction = System.currentTimeMillis();
                            mSTMobileHumanActionNative.nativeHumanActionDetectPtr(mDetectImageBuffers[mCameraInputBufferIndex].getImageBuffer(),
                                    mDetectImageBuffers[mCameraInputBufferIndex].getImageFormat(), mHumanDetectConfig, orientation, mDetectWidth, mDetectHeight);
                            if(GPU_RESIZE_RATIO != 1.0f) mSTMobileHumanActionNative.nativeResizeHumanActionPtr(GPU_RESIZE_RATIO);
                            STLogUtils.i(TAG, "human action cost time: " + (System.currentTimeMillis() - startHumanAction));
                            if (mNeedOutputHumanAction) {
                                stHumanAction = mSTMobileHumanActionNative.getNativeHumanAction();
                            }
                        }

                        if(mAnimalDetectConfig > 0){
                            animalDetect(false, mDetectImageBuffers[mCameraInputBufferIndex].getImageBuffer(),
                                    mDetectImageBuffers[mCameraInputBufferIndex].getImageFormat(), orientation, mDetectWidth, mDetectHeight,
                                    mCameraInputBufferIndex, input.isNeedMirror(), input.getOrientation());
                        }

                        mCountDownLatch.countDown(); // 计数减1
                    }
                });

                //核心渲染部分
                //输入纹理
                STEffectTexture stEffectTexture = new STEffectTexture(textureId, mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);
                //输出纹理，需要在上层初始化
                STEffectTexture stEffectTextureOut = new STEffectTexture(mEffectTextureId[0], mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);

                int event = mCustomEvent;//用户自定义参数设置
                STEffectCustomParam customParam;
                if (mSensorEvent != null && mSensorEvent.values != null && mSensorEvent.values.length > 0) {
                    customParam = new STEffectCustomParam(new STQuaternion(mSensorEvent.values), input.isNeedMirror(), event);
                } else {
                    customParam = new STEffectCustomParam(new STQuaternion(0f, 0f, 0f, 1f), input.isNeedMirror(), event);
                }

            long startRenderTime = System.currentTimeMillis();
            //渲染接口输入参数
            STEffectRenderInParam sTEffectRenderInParam = new STEffectRenderInParam(mSTMobileHumanActionNative.getNativeHumanActionResultCache(1 - mCameraInputBufferIndex), mAnimalFaceInfo[1 - mCameraInputBufferIndex],
                    mNativeRenderOrientations[1 - mCameraInputBufferIndex], mNativeRenderOrientations[1 - mCameraInputBufferIndex], false, customParam, stEffectTexture,
                    mDetectImageBuffers == null || mDetectImageBuffers[1 - mCameraInputBufferIndex] == null ? null : new STEffectInImage(new STImage(mDetectImageBuffers[1 - mCameraInputBufferIndex].getImageBuffer(), mDetectImageBuffers[1 - mCameraInputBufferIndex].getImageFormat(), mDetectWidth, mDetectHeight, mDetectWidth), input.getOrientation(), false));
            sTEffectRenderInParam.setTargetFaceId(input.getTargetFaceId());

            STImage effectImage = null;
            if (output.getEffectImage() != null) {
                STImageBuffer stImageBuffer = output.getEffectImage();
                effectImage = new STImage(stImageBuffer.getImageBuffer(), stImageBuffer.getImageFormat(), stImageBuffer.getWidth(), stImageBuffer.getHeight(), stImageBuffer.getWidth() * 4);
            }

            //渲染接口输出参数
            STEffectRenderOutParam stEffectRenderOutParam = new STEffectRenderOutParam(stEffectTextureOut, effectImage, null);

            mSTMobileEffectNative.render(sTEffectRenderInParam, stEffectRenderOutParam, false);
            STLogUtils.i(TAG, "render cost time total: " + (System.currentTimeMillis() - startRenderTime));
            if (stEffectRenderOutParam.getImage() != null) {
                output.setEffectImage(new STImageBuffer(stEffectRenderOutParam.getImage().getImageData(), input.getOutputBufferFormat(), mImageWidth, mImageHeight));
            }
            output.setHumanAction(stHumanAction);
                if(mCurrentFrameIndex < 2 || mPreFrameIsOrigin){//切换分辨率残影问题
                    textureId = mCameraInputTexture[mCameraInputTextureIndex];
                }else {
                    textureId = stEffectRenderOutParam.getTexture().getId();
                }
                if (event == mCustomEvent) mCustomEvent = 0;

                output.setTextureId(textureId);

                if(mNeedOutputImageBuffer){
                    output.setImage(mDetectImageBuffers[mCameraInputBufferIndex]);
                }
                mPreFrameIsOrigin = false;
            }
        }else if(mRenderMode == STRenderMode.IMAGE.getMode() || mRenderMode == STRenderMode.VIDEO.getMode() || mRenderMode == STRenderMode.VIDEO_POST_PROCESS.getMode()){
            if(mShowOrigin){
                output.setTextureId(textureId);
            }else {
                long humanActionCostTime = System.currentTimeMillis();

            byte[] rgbaBuffer = OpenGLUtils.readPixel(input.getTexture().getId(), mImageWidth, mImageHeight);
            int ret = mSTMobileHumanActionNative.nativeHumanActionDetectPtr(rgbaBuffer, STCommonNative.ST_PIX_FMT_RGBA8888,
                    mHumanDetectConfig, rotate,
                    mImageWidth, mImageHeight);
            mSTMobileHumanActionNative.nativeHumanActionPtrCopy();
            if (mNeedOutputHumanAction) {
                stHumanAction = mSTMobileHumanActionNative.getNativeHumanAction();
            }
            mAnimalFaceInfo[0] = mSTMobileAnimalNative.animalDetect(rgbaBuffer, STCommonNative.ST_PIX_FMT_RGBA8888, rotate, (int)mAnimalDetectConfig, mImageWidth, mImageHeight);

            STEffectTexture stEffectTexture = new STEffectTexture(input.getTexture().getId(), mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);
            STEffectTexture stEffectTextureOut = new STEffectTexture(mEffectTextureId[0], mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);

            int event = mCustomEvent;
            STQuaternion stQuaternion = mSTMobileEffectNative.getDefaultCameraQuaternion(true);
            STEffectCustomParam customParam = null;
            if (stQuaternion != null) {
                customParam = new STEffectCustomParam(new STQuaternion(stQuaternion.getX(), stQuaternion.getY(), stQuaternion.getZ(), stQuaternion.getW()), false, event);
            }

            STEffectRenderInParam sTEffectRenderInParam = new STEffectRenderInParam(mSTMobileHumanActionNative.getNativeHumanActionPtrCopy(), mAnimalFaceInfo[0], rotate, STRotateType.ST_CLOCKWISE_ROTATE_0, false, customParam, stEffectTexture,
                    new STEffectInImage(new STImage(rgbaBuffer, STCommonNative.ST_PIX_FMT_RGBA8888, mImageWidth, mImageHeight, mImageWidth * 4), input.getOrientation(), input.isNeedMirror()));
            STImage effectImage = null;
            if (output.getEffectImage() != null) {
                STImageBuffer stImageBuffer = output.getEffectImage();
                effectImage = new STImage(stImageBuffer.getImageBuffer(), stImageBuffer.getImageFormat(), stImageBuffer.getWidth(), stImageBuffer.getHeight());
            }
            STEffectRenderOutParam stEffectRenderOutParam = new STEffectRenderOutParam(stEffectTextureOut, effectImage, null);
            sTEffectRenderInParam.setTargetSTRect(input.getTargetRect());
            int result = mSTMobileEffectNative.render(sTEffectRenderInParam, stEffectRenderOutParam, false);
            if (mNeedOutputHumanAction) {
                output.setHumanAction(stHumanAction);
            }
            if (stEffectRenderOutParam.getImage() != null) {
                output.setEffectImage(new STImageBuffer(stEffectRenderOutParam.getImage().getImageData(), input.getOutputBufferFormat(), mImageWidth, mImageHeight));
            }
            if (stEffectRenderOutParam.getTexture() != null) {
                textureId = stEffectRenderOutParam.getTexture().getId();
            }

            output.setTextureId(textureId);

                if (event == mCustomEvent) {
                    mCustomEvent = 0;
                }
            }
        }

        return 0;
    }

    private int processTextureSync(STEffectsInput input, STEffectsOutput output) {
        if(input == null || input.getTexture() == null || !GLES30.glIsTexture(input.getTexture().getId())){
            Log.e(TAG, "input texture error");
            return STResultCode.ST_E_INVALIDARG.getResultCode();
        }

        if(output == null){
            output = new STEffectsOutput(input.getTexture().getId(), null, null);
        }

        int width = input.getTexture().getWidth();
        int height = input.getTexture().getHeight();
        if(input.getRotate() == 90 || input.getRotate() == 270){
            width = input.getTexture().getHeight();
            height = input.getTexture().getWidth();
        }

        mCurrentFrameIndex++;
        if(mImageWidth != width || mImageHeight != height){
            mImageWidth = width;
            mImageHeight = height;

            mDetectWidth = mImageWidth;
            mDetectHeight = mImageHeight;


            if(mRenderMode == STRenderMode.PREVIEW.getMode() && mSTTextureToBufferManager != null){
                mSTTextureToBufferManager.release();
                mSTTextureToBufferManager = null;
            }

            if(mSTGLRender != null){
                mSTGLRender.init(mImageWidth, mImageHeight, mDetectWidth, mDetectHeight);
                mSTGLRender.calculateVertexBuffer(mImageWidth, mImageHeight, mImageWidth, mImageHeight);
            }

            releaseTexturesAndBuffers();
            mCameraInputBufferIndex = 0;
            mCameraInputTextureIndex = 0;
            mCurrentFrameIndex = 0;
        }

        if(mRenderMode == STRenderMode.PREVIEW.getMode() && mSTTextureToBufferManager == null){
            mSTTextureToBufferManager = new STTextureToBufferManager();
            mSTTextureToBufferManager.init(mDetectWidth, mDetectHeight);
        }

        if(mSTGLRender == null){
            mSTGLRender = new STGLRender();
            mSTGLRender.init(mImageWidth, mImageHeight, mDetectWidth, mDetectHeight);
            mSTGLRender.calculateVertexBuffer(mImageWidth, mImageHeight, mImageWidth, mImageHeight);
            mTextureRotate = input.getRotate();
            mSTGLRender.adjustTextureBuffer(input.getRotate(), true, input.isNeedMirror());
        }

        if(mSTGLRender != null && mTextureRotate != input.getRotate()){
            mTextureRotate = input.getRotate();
            mSTGLRender.adjustTextureBuffer(input.getRotate(), true, input.isNeedMirror());
        }

        if (mCameraInputTexture == null) {
            mCameraInputTexture = new int[3];
            OpenGLUtils.initEffectTexture(mImageWidth, mImageHeight, mCameraInputTexture, GLES20.GL_TEXTURE_2D);
        }

        if(mEffectTextureId == null){
            mEffectTextureId = new int[1];
            OpenGLUtils.initEffectTexture(mImageWidth, mImageHeight, mEffectTextureId, GLES20.GL_TEXTURE_2D);
        }

        int textureId = OpenGLUtils.NO_TEXTURE;
        if(mRenderMode == STRenderMode.PREVIEW.getMode()){
            long startPreprocess = System.currentTimeMillis();
            int[] resizedTexture = new int[1];//resize纹理，读取数据量小，速度快
            int texture2d = mSTGLRender.preProcessAndResizeTexture(input.getTexture().getId(), true, resizedTexture,null,
                    mCameraInputTextureIndex, input.getTexture().getFormat() == STTextureFormat.FORMAT_TEXTURE_OES.getFormat());
            byte[] rgbaBuffer = OpenGLUtils.readPixel(texture2d, mDetectWidth, mDetectHeight);

            STLogUtils.i(TAG, "preprocess cost time: " + (System.currentTimeMillis() - startPreprocess));

            textureId = texture2d;

            if(mShowOrigin){
                mPreFrameIsOrigin = true;
                output.setTextureId(textureId);
            } else {
                int deviceOrientation = mAccelerometer == null ? 1 : mAccelerometer.getDirection();
                int orientation = STUtils.getRenderOrientation(deviceOrientation);
                //mSTMobileHumanActionNative.nativeHumanActionDetectPtr(rgbaBuffer, STCommonNative.ST_PIX_FMT_RGBA8888, mHumanDetectConfig, orientation, mDetectWidth, mDetectHeight);
                mSTMobileHumanActionNative.nativeHumanActionDetectPtr(rgbaBuffer, STCommonNative.ST_PIX_FMT_RGBA8888, mHumanDetectConfig, orientation, mDetectWidth, mDetectHeight);
                stHumanAction = mSTMobileHumanActionNative.getNativeHumanAction();
                if (stHumanAction!=null) {
                    Log.i(TAG, "faceCount=" + stHumanAction.faceCount);
                }

                //输入纹理
                STEffectTexture stEffectTexture = new STEffectTexture(textureId, mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);
                //输出纹理，需要在上层初始化
                STEffectTexture stEffectTextureOut = new STEffectTexture(mEffectTextureId[0], mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);

                int event = mCustomEvent;//用户自定义参数设置

                //渲染接口输入参数
//                STEffectRenderInParam sTEffectRenderInParam = new STEffectRenderInParam(mSTMobileHumanActionNative.getNativeHumanActionPtrCopy(), null,
//                    mNativeRenderOrientations[1 - mCameraInputBufferIndex], mNativeRenderOrientations[1 - mCameraInputBufferIndex], false, null, stEffectTexture,
//                    mDetectImageBuffers == null || mDetectImageBuffers[1 - mCameraInputBufferIndex] == null ? null : new STEffectInImage(new STImage(mDetectImageBuffers[1 - mCameraInputBufferIndex].getImageBuffer(), mDetectImageBuffers[1 - mCameraInputBufferIndex].getImageFormat(), mDetectWidth, mDetectHeight, mDetectWidth), input.getOrientation(), false));

                // STEffectRenderInParam sTEffectRenderInParam = new STEffectRenderInParam(mSTMobileHumanActionNative.getNativeHumanActionResultPtr(),null,0,0,
                STEffectRenderInParam sTEffectRenderInParam = new STEffectRenderInParam(0,null,0,0,
                        false, null, stEffectTexture, null);
                //sTEffectRenderInParam.setHumanAction(stHumanAction);
                sTEffectRenderInParam.setNativeHumanActionResult(mSTMobileHumanActionNative.getNativeHumanActionResultPtr());

                STImage effectImage = null;
                if (output.getEffectImage() != null) {
                    STImageBuffer stImageBuffer = output.getEffectImage();
                    effectImage = new STImage(stImageBuffer.getImageBuffer(), stImageBuffer.getImageFormat(), stImageBuffer.getWidth(), stImageBuffer.getHeight(), stImageBuffer.getWidth() * 4);
                }

                //渲染接口输出参数
                STEffectRenderOutParam stEffectRenderOutParam = new STEffectRenderOutParam(stEffectTextureOut, effectImage, null);

                mSTMobileEffectNative.render(sTEffectRenderInParam, stEffectRenderOutParam, false);
                if (stEffectRenderOutParam.getImage() != null) {
                    output.setEffectImage(new STImageBuffer(stEffectRenderOutParam.getImage().getImageData(), input.getOutputBufferFormat(), mImageWidth, mImageHeight));
                }
                output.setHumanAction(stHumanAction);
                if(mCurrentFrameIndex < 2 || mPreFrameIsOrigin){//切换分辨率残影问题
                    textureId = mCameraInputTexture[mCameraInputTextureIndex];
                }else {
                    textureId = stEffectRenderOutParam.getTexture().getId();
                }
                if (event == mCustomEvent) mCustomEvent = 0;

                output.setTextureId(textureId);

                if(mNeedOutputImageBuffer){
                    output.setImage(mDetectImageBuffers[mCameraInputBufferIndex]);
                }
                    mPreFrameIsOrigin = false;
                }
        }
        return 0;
    }

    @Override
    public int processBuffer(STEffectsInput input, STEffectsOutput output) {

        if(input == null || input.getImage() == null){
            Log.e(TAG, "input buffer error");
            return STResultCode.ST_E_INVALIDARG.getResultCode();
        }

        if(output == null){
            output = new STEffectsOutput(-1, null, null);
        }

        int width = input.getImage().getWidth();
        int height = input.getImage().getHeight();
        if(input.getRotate() == 90 || input.getRotate() == 270){
            width = input.getImage().getHeight();
            height = input.getImage().getWidth();
        }

        if(mImageWidth != width || mImageHeight != height){
            mImageWidth = width;
            mImageHeight = height;

            mDetectWidth = mImageWidth;
            mDetectHeight = mImageHeight;

            if(mSTMobileColorConvertNative != null){
                mSTMobileColorConvertNative.destroyInstance();
                mSTMobileColorConvertNative = null;
            }

            if(mSTGLRender != null){
                mSTGLRender.init(mImageWidth, mImageHeight, mDetectWidth, mDetectHeight);
                mSTGLRender.calculateVertexBuffer(mImageWidth, mImageHeight, mImageWidth, mImageHeight);
            }

            releaseTexturesAndBuffers();
            mCameraInputBufferIndex = 0;
            mCameraInputTextureIndex = 0;
        }

        if(mSTMobileColorConvertNative == null){
            mSTMobileColorConvertNative = new STMobileColorConvertNative();
            mSTMobileColorConvertNative.createInstance();
            mSTMobileColorConvertNative.setTextureSize(mImageWidth, mImageHeight);
        }

        if(mSTGLRender == null){
            mSTGLRender = new STGLRender();
            mSTGLRender.init(mImageWidth, mImageHeight, mDetectWidth, mDetectHeight);
            mSTGLRender.calculateVertexBuffer(mImageWidth, mImageHeight, mImageWidth, mImageHeight);
            mTextureRotate = input.getRotate();
            mSTGLRender.adjustTextureBuffer(input.getRotate(), true, input.isNeedMirror());
        }

        if(mSTGLRender != null && mTextureRotate != input.getRotate()){
            mTextureRotate = input.getRotate();
            mSTGLRender.adjustTextureBuffer(input.getRotate(), true, input.isNeedMirror());
        }

        if (mCameraInputTexture == null) {
            mCameraInputTexture = new int[3];
            OpenGLUtils.initEffectTexture(mImageWidth, mImageHeight, mCameraInputTexture, GLES20.GL_TEXTURE_2D);
        }

        if(mEffectTextureId == null){
            mEffectTextureId = new int[1];
            OpenGLUtils.initEffectTexture(mImageWidth, mImageHeight, mEffectTextureId, GLES20.GL_TEXTURE_2D);
        }

        if(input.getImage().getImageFormat() == STCommonNative.ST_PIX_FMT_NV21){
            int ret = mSTMobileColorConvertNative.nv21BufferToRgbaTexture(input.getImage().getWidth(), input.getImage().getHeight(),
                    (360 - input.getOrientation())/90, input.isNeedMirror(), input.getImage().getImageBuffer(), mCameraInputTexture[mCameraInputTextureIndex]);
        }else if(input.getImage().getImageFormat() == STCommonNative.ST_PIX_FMT_RGBA8888){
            ByteBuffer buffer = ByteBuffer.wrap(input.getImage().getImageBuffer());
            if(input.getOrientation() == 90 || input.getOrientation() == 270){
                int[] srcTexture = new int[1];
                OpenGLUtils.initEffectTexture(input.getImage().getWidth(), input.getImage().getHeight(), srcTexture, GLES20.GL_TEXTURE_2D);
                OpenGLUtils.loadTexture(buffer, input.getImage().getWidth(), input.getImage().getHeight(), srcTexture[0]);
                mCameraInputTexture[mCameraInputTextureIndex] = mSTGLRender.preProcess(srcTexture[0], null,
                        mCameraInputTextureIndex, false);
                GLES20.glDeleteTextures(1, srcTexture, 0);
            }else {
                OpenGLUtils.loadTexture(buffer, input.getImage().getWidth(), input.getImage().getHeight(), mCameraInputTexture[mCameraInputTextureIndex]);
            }
        }

        mCameraInputTextureIndex = 1 - mCameraInputTextureIndex;
        int textureId = mCameraInputTexture[mCameraInputTextureIndex];
        int rotate = input.getRotate()/90;

        if(mRenderMode == STRenderMode.PREVIEW.getMode() && !mSyncRender){
            try {
                if(mCountDownLatch != null) mCountDownLatch.await();
            } catch (Exception e) {
                Log.e(TAG, "lock wait: " + e.getMessage());
            }

            mCountDownLatch = new CountDownLatch(1);//reset CountDownLatch

            synchronized (mHumanActionLock){
                mSTMobileHumanActionNative.nativeHumanActionPtrCopy();
            }

            mDetectThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    int deviceOrientation = mAccelerometer == null ? 1 : mAccelerometer.getDirection();
                    int renderOrientation = STUtils.getRenderOrientation(deviceOrientation);
                    mRenderOrientations[mCameraInputTextureIndex] = renderOrientation;
                    int detectOrientation = STUtils.getHumanActionOrientation(input.isNeedMirror(), deviceOrientation, input.getOrientation());

                    synchronized (mHumanActionLock) {
                        long startHumanAction = System.currentTimeMillis();
                        // 检测人
//                        mHumanDetectConfig|=STMobileHumanActionNative.ST_MOBILE_DETECT_FINGER;
                        mSTMobileHumanActionNative.nativeHumanActionDetectPtr(input.getImage().getImageBuffer(),
                                input.getImage().getImageFormat(), mHumanDetectConfig, detectOrientation, input.getImage().getWidth(), input.getImage().getHeight());

                        STHumanAction.nativeHumanActionRotateAndMirror(mSTMobileHumanActionNative, mSTMobileHumanActionNative.getNativeHumanActionResultPtr(),
                                 mImageWidth, mImageHeight, input.isNeedMirror() ? 1 : 0, input.getOrientation(), deviceOrientation);
                        STLogUtils.i(TAG, "human action cost time: " + (System.currentTimeMillis() - startHumanAction));
                        if (mNeedOutputHumanAction) {
                            stHumanAction = mSTMobileHumanActionNative.getNativeHumanAction();
                        }
                    }

                    if(mAnimalDetectConfig > 0){
                        if(mAnimalDetectConfig > 0){
                            long catDetectStartTime = System.currentTimeMillis();
                            mAnimalFaceInfo[mCameraInputTextureIndex] = mSTMobileAnimalNative.animalDetect(input.getImage().getImageBuffer(), input.getImage().getImageFormat(),
                                    detectOrientation, (int)mAnimalDetectConfig, input.getImage().getWidth(), input.getImage().getHeight());
                            STLogUtils.i(TAG, "animal detect cost time: " + (System.currentTimeMillis() - catDetectStartTime));

                            mAnimalFaceInfo[mCameraInputTextureIndex] = STUtils.processAnimalFaceResult(mAnimalFaceInfo[mCameraInputTextureIndex],
                                    input.getImage().getWidth(), input.getImage().getHeight(), input.isNeedMirror(), input.getOrientation());
                        }
                    }

                    mCountDownLatch.countDown(); // 计数减1
                }
            });

            //核心渲染部分
            //输入纹理
            STEffectTexture stEffectTexture = new STEffectTexture(textureId, mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);
            //输出纹理，需要在上层初始化
            STEffectTexture stEffectTextureOut = new STEffectTexture(mEffectTextureId[0], mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);

            int event = mCustomEvent;//用户自定义参数设置
            STEffectCustomParam customParam;
            if (mSensorEvent != null && mSensorEvent.values != null && mSensorEvent.values.length > 0) {
                customParam = new STEffectCustomParam(new STQuaternion(mSensorEvent.values), input.isNeedMirror(), event);
            } else {
                customParam = new STEffectCustomParam(new STQuaternion(0f, 0f, 0f, 1f), input.isNeedMirror(), event);
            }

            long startRenderTime = System.currentTimeMillis();
            //渲染接口输入参数
            STEffectRenderInParam sTEffectRenderInParam = new STEffectRenderInParam(mSTMobileHumanActionNative.getNativeHumanActionPtrCopy(), mAnimalFaceInfo[mCameraInputTextureIndex],
                    mNativeRenderOrientations[mCameraInputTextureIndex], mNativeRenderOrientations[mCameraInputTextureIndex], false, customParam, stEffectTexture,null);
            sTEffectRenderInParam.setTargetFaceId(input.getTargetFaceId());

            STImage effectImage = null;
            if (enableOutputEffectImageBuffer) {
                if (effectImageRgbaBuffer == null) {
                    effectImageRgbaBuffer = new byte[mImageWidth * mImageHeight * 4];
                }
                effectImage = new STImage(effectImageRgbaBuffer, STCommonNative.ST_PIX_FMT_RGBA8888, mImageWidth, mImageHeight);
            }

            //渲染接口输出参数
            STEffectRenderOutParam stEffectRenderOutParam = new STEffectRenderOutParam(stEffectTextureOut, effectImage, null);

            mSTMobileEffectNative.render(sTEffectRenderInParam, stEffectRenderOutParam, false);
            STLogUtils.i(TAG, "render cost time total: " + (System.currentTimeMillis() - startRenderTime));

            output.setHumanAction(stHumanAction);
            textureId = stEffectRenderOutParam.getTexture().getId();
            if (event == mCustomEvent) mCustomEvent = 0;

            output.setTextureId(textureId);

            if(mNeedOutputImageBuffer){
                output.setImage(mDetectImageBuffers[mCameraInputBufferIndex]);
            }
        }else if(mRenderMode == STRenderMode.IMAGE.getMode() || mRenderMode == STRenderMode.VIDEO.getMode()){
            long humanActionCostTime = System.currentTimeMillis();

            byte[] rgbaBuffer = OpenGLUtils.readPixel(input.getTexture().getId(), mImageWidth, mImageHeight);
            int ret0 = mSTMobileHumanActionNative.nativeHumanActionDetectPtr(rgbaBuffer, STCommonNative.ST_PIX_FMT_RGBA8888,
                    mHumanDetectConfig, rotate,
                    mImageWidth, mImageHeight);
            mSTMobileHumanActionNative.nativeHumanActionPtrCopy();

            if (mNeedOutputHumanAction) {
                stHumanAction = mSTMobileHumanActionNative.getNativeHumanAction();
            }
            long catDetectStartTime = System.currentTimeMillis();
            mAnimalFaceInfo[0] = mSTMobileAnimalNative.animalDetect(rgbaBuffer, STCommonNative.ST_PIX_FMT_RGBA8888, rotate, (int)mAnimalDetectConfig, mImageWidth, mImageHeight);

            STEffectTexture stEffectTexture = new STEffectTexture(input.getTexture().getId(), mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);
            STEffectTexture stEffectTextureOut = new STEffectTexture(mEffectTextureId[0], mImageWidth, mImageHeight, STCommonNative.ST_PIX_FMT_RGBA8888);

            int event = mCustomEvent;
            STQuaternion stQuaternion = mSTMobileEffectNative.getDefaultCameraQuaternion(true);
            STEffectCustomParam customParam = new STEffectCustomParam(new STQuaternion(stQuaternion.getX(), stQuaternion.getY(), stQuaternion.getZ(), stQuaternion.getW()), false, event);

            STEffectRenderInParam sTEffectRenderInParam = new STEffectRenderInParam(mSTMobileHumanActionNative.getNativeHumanActionPtrCopy(), mAnimalFaceInfo[0], rotate, STRotateType.ST_CLOCKWISE_ROTATE_0, false, customParam, stEffectTexture, null);
            STEffectRenderOutParam stEffectRenderOutParam = new STEffectRenderOutParam(stEffectTextureOut, null, null);
            int result = mSTMobileEffectNative.render(sTEffectRenderInParam, stEffectRenderOutParam, false);

            if (stEffectRenderOutParam.getTexture() != null) {
                textureId = stEffectRenderOutParam.getTexture().getId();
            }

            output.setTextureId(textureId);

            if (event == mCustomEvent) {
                mCustomEvent = 0;
            }
        }

        return 0;
    }

    protected STMobileAnimalResult[] mAnimalFaceInfo = new STMobileAnimalResult[2];
    protected void animalDetect(boolean rotateDetectResult, byte[] imageData, int format, int orientation, int width, int height, int index, boolean needMirror, int cameraOrientation) {
        long catDetectStartTime = System.currentTimeMillis();
        int animalDetectConfig = (int) mSTMobileEffectNative.getAnimalDetectConfig();
        STMobileAnimalResult stMobileAnimalResult = mSTMobileAnimalNative.animalDetect(imageData, format, orientation, animalDetectConfig, width, height);
        stMobileAnimalResult = mSTMobileAnimalNative.animalFaceCopy(stMobileAnimalResult);
        //STAnimalFace[] animalFaces = stMobileAnimalResult.getAnimalFaceArray();
        STLogUtils.i(TAG, "animal detect cost time: " + (System.currentTimeMillis() - catDetectStartTime));

        if (rotateDetectResult) {
            stMobileAnimalResult = processAnimalFaceResult(stMobileAnimalResult, needMirror, cameraOrientation);
        }
        mAnimalFaceInfo[index] = stMobileAnimalResult;
    }

    protected STMobileAnimalResult processAnimalFaceResult(STMobileAnimalResult result, boolean isFrontCamera, int cameraOrientation) {
        if (result == null) {
            return null;
        }
        if (isFrontCamera && cameraOrientation == 90) {
            result = STMobileAnimalNative.animalRotate(mImageHeight, mImageWidth, STRotateType.ST_CLOCKWISE_ROTATE_90, result);
            result = STMobileAnimalNative.animalMirror(mImageWidth, result);
        } else if (isFrontCamera && cameraOrientation == 270) {
            result = STMobileAnimalNative.animalRotate(mImageHeight, mImageWidth, STRotateType.ST_CLOCKWISE_ROTATE_270, result);
            result = STMobileAnimalNative.animalMirror(mImageWidth, result);
        } else if (!isFrontCamera && cameraOrientation == 270) {
            result = STMobileAnimalNative.animalRotate(mImageHeight, mImageWidth, STRotateType.ST_CLOCKWISE_ROTATE_270, result);
        } else if (!isFrontCamera && cameraOrientation == 90) {
            result = STMobileAnimalNative.animalRotate(mImageHeight, mImageWidth, STRotateType.ST_CLOCKWISE_ROTATE_90, result);
        }
        return result;
    }

    private void releaseTexturesAndBuffers(){
        if(mCameraInputTexture != null){
            GLES20.glDeleteTextures(3, mCameraInputTexture, 0);
            mCameraInputTexture = null;
        }

        if(mEffectTextureId != null){
            GLES20.glDeleteTextures(1, mEffectTextureId, 0);
            mEffectTextureId = null;
        }

        if (mDetectImageBuffers != null) {
            mDetectImageBuffers[0] = null;
            mDetectImageBuffers[1] = null;
            mDetectImageBuffers = null;
        }
    }

    @Override
    public void release() {

        if(mSTMobileEffectNative != null){
            mSTMobileEffectNative.destroyInstance();
            mSTMobileEffectNative = null;
        }

        if(mSTMobileHumanActionNative != null){
            mSTMobileHumanActionNative.destroyInstance();
            mSTMobileHumanActionNative = null;
        }

        if(mSTMobileAnimalNative != null){
            mSTMobileAnimalNative.destroyInstance();
            mSTMobileAnimalNative = null;
        }

        if(mRenderMode == STRenderMode.PREVIEW.getMode() && mSTTextureToBufferManager != null){
            mSTTextureToBufferManager.release();
            mSTTextureToBufferManager = null;
        }

        if(mSTGLRender != null){
            mSTGLRender.destroyFrameBuffers();
            mSTGLRender = null;
        }

        if(mCameraInputTexture != null){
            GLES20.glDeleteTextures(3, mCameraInputTexture, 0);
            mCameraInputTexture = null;
        }

        if(mEffectTextureId != null){
            GLES20.glDeleteTextures(1, mEffectTextureId, 0);
            mEffectTextureId = null;
        }

        if(mSTMobileColorConvertNative != null){
            mSTMobileColorConvertNative.destroyInstance();
            mSTMobileColorConvertNative = null;
        }

        mCameraInputBufferIndex = 0;
        mCameraInputTextureIndex = 0;
    }

    @Override
    public int setObjectTarget(STRect rect) {
        return 0;
    }

    @Override
    public STRect objectTrack(float[] score) {
        return null;
    }

    @Override
    public void replayPackageById(int id) {
        if(mSTMobileEffectNative == null) return;
        mSTMobileEffectNative.replayPackage(id);
    }

    @Override
    public void resetDetect() {
        if(mSTMobileAnimalNative != null){
            mSTMobileAnimalNative.reset();
        }
        if(mSTMobileHumanActionNative != null){
            mSTMobileHumanActionNative.reset();
        }
    }

    @Override
    public STEffectBeautyInfo[] getOverlappedBeauty() {
        if(mSTMobileEffectNative == null) return null;
        mOverlappedBeautyCount = mSTMobileEffectNative.getOverlappedBeautyCount();
        return mSTMobileEffectNative.getOverlappedBeauty(mOverlappedBeautyCount);
    }

    @Override
    public long getHumanDetectConfig() {
        return mHumanDetectConfig;
    }

    @Override
    public void addHumanDetectConfig(long detectConfig) {
        mHumanDetectConfig |= detectConfig;
    }

    @Override
    public void removeHumanDetectConfig(long detectConfig) {
        mHumanDetectConfig &= ~detectConfig;
    }

    @Override
    public long getAnimalDetectConfig() {
        return mAnimalDetectConfig;
    }

    @Override
    public int getCustomEvent() {
        return mSTMobileEffectNative.getCustomEventNeeded();
    }

    @Override
    public int changeBg(int stickerId, STImage image) {
        return mSTMobileEffectNative.changeBg(stickerId, image);
    }

    @Override
    public int changeBg2(int stickerId, STEffectTexture st) {
        return mSTMobileEffectNative.changeBg2(stickerId, st);
    }

    @Override
    public int setDetectParams(int type, float value) {
        return mSTMobileHumanActionNative.setParam(type, value);
    }

    boolean mNeedOutputImageBuffer;
    boolean mNeedOutputHumanAction;
    @Override
    public void enableOutputImageBuffer(boolean enable) {
        mNeedOutputImageBuffer = enable;
    }

    @Override
    public void enableOutputEffectImageBuffer(boolean enable) {
        enableOutputEffectImageBuffer = enable;
    }

    @Override
    public void setFaceMeshList() {
        if (mSTMobileHumanActionNative!=null) {
            STFaceMeshList meshList = mSTMobileHumanActionNative.getFaceMeshList();
            mSTMobileEffectNative.setFaceMeshList(meshList);
        }
    }

    public void enableOutputHumanAction(boolean enable) {
        mNeedOutputHumanAction = enable;
    }

    @Override
    public int setEffectModuleInfo(STGanReturn ganReturn, STEffectModuleInfo moduleInfo) {
        int ret = mSTMobileEffectNative.setEffectModuleInfo(ganReturn, moduleInfo);
        return ret;
    }

    @Override
    public void openIsMe(boolean tag) {
        if (tag && mHumanDetectConfig==0) {
            mHumanDetectConfig |= STMobileHumanActionNative.ST_MOBILE_FACE_DETECT;
        }
    }

    @Override
    public STFaceExtraInfo getCurrentFaceInfo() {
        return null;
    }

    @Override
    public void setSensorEvent(SensorEvent event) {
        mSensorEvent = event;
    }

    @Override
    public void setCustomEvent(int customEvent) {
        mCustomEvent = customEvent;
    }

    @Override
    public void setShowOrigin(boolean showOrigin) {
        mShowOrigin = showOrigin;
    }

    boolean mSyncRender;
    public void enableSyncRender(boolean enable){
        mSyncRender = enable;
    }
}
