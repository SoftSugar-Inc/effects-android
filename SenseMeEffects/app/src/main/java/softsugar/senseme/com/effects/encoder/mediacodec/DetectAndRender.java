package softsugar.senseme.com.effects.encoder.mediacodec;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;

import com.blankj.utilcode.util.Utils;
import com.softsugar.stmobile.STCommonNative;
import com.softsugar.stmobile.engine.STEffectsEngine;
import com.softsugar.stmobile.engine.STRenderMode;
import com.softsugar.stmobile.engine.STTextureFormat;
import com.softsugar.stmobile.engine.glutils.STEffectsInput;
import com.softsugar.stmobile.engine.glutils.STEffectsOutput;
import com.softsugar.stmobile.engine.glutils.STImageBuffer;
import com.softsugar.stmobile.model.STEffectTryonInfo;
import com.softsugar.stmobile.model.STImage;
import com.softsugar.stmobile.model.STMobileAnimalResult;
import com.softsugar.stmobile.params.STEffectBeautyType;
import com.softsugar.stmobile.STMobileHumanActionNative;
import com.softsugar.stmobile.params.STEffectParam;
import com.softsugar.stmobile.model.STEffectTexture;

import java.io.File;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.display.glutils.STGLRender;
import softsugar.senseme.com.effects.entity.BasicEffectEntity;
import softsugar.senseme.com.effects.entity.ModelItem;
import softsugar.senseme.com.effects.state.AtyStateContext;
import softsugar.senseme.com.effects.state.IAtyState;
import softsugar.senseme.com.effects.state.TryOnVideoAtyState;
import softsugar.senseme.com.effects.utils.Constants;
import softsugar.senseme.com.effects.utils.ContextHolder;
import softsugar.senseme.com.effects.utils.EffectInfoDataHelper;
import softsugar.senseme.com.effects.utils.GlideUtils;
import softsugar.senseme.com.effects.utils.LocalDataStore;
import softsugar.senseme.com.effects.display.glutils.GlUtil;
import softsugar.senseme.com.effects.utils.ReadAssetsJsonFileUtils;
import softsugar.senseme.com.effects.utils.STUtils;
import softsugar.senseme.com.effects.utils.SaveVideoParamsManager;
import softsugar.senseme.com.effects.view.StickerItem;
import softsugar.senseme.com.effects.view.widget.EffectType;
import softsugar.senseme.com.effects.view.widget.TryOnSeekBarType;

public class DetectAndRender {
    private static final String TAG = "DetectAndRender";

    private STMobileHumanActionNative mSTMobileHumanActionNative;
    private long mHumanActionConfigDetect = STMobileHumanActionNative.ST_MOBILE_FACE_DETECT;

    private STGLRender mSTGLRender = new STGLRender();

    private STEffectsEngine mSTEffectsEngine;
    private boolean mLoadModelDone;

    public DetectAndRender(){
        mSTEffectsEngine = new STEffectsEngine();
        mSTEffectsEngine.init(ContextHolder.getContext(), STRenderMode.VIDEO_POST_PROCESS.getMode());

        String json = ReadAssetsJsonFileUtils.getJson("json_data/json_models.json");
        Type type = GsonUtils.getListType(ModelItem.class);
        List<ModelItem> modelItemList = GsonUtils.fromJson(json, type);
        String root = Objects.requireNonNull(ContextHolder.getContext().getExternalFilesDir(null)).getAbsolutePath() + File.separator;
        for (int i = 0; i < modelItemList.size(); i++) {
            String sdPath = root + modelItemList.get(i).model_asset_path;
            mSTEffectsEngine.loadSubModel(sdPath);
        }

//        byte[] buffer = FileUtils.readFileFromAssets(ContextHolder.getContext(), Constants.MODEL_CAT_FACE);
//        mSTEffectsEngine.loadAnimalSubModelFromBuffer(buffer);
//
//        byte[] buffer1 = FileUtils.readFileFromAssets(ContextHolder.getContext(), Constants.MODEL_DOG_FACE);
//        mSTEffectsEngine.loadAnimalSubModelFromBuffer(buffer1);

        mLoadModelDone = true;

        mSTEffectsEngine.setFaceMeshList();
    }
    public void initHumanAction(){
        if(mSTMobileHumanActionNative == null){
            mSTMobileHumanActionNative = new STMobileHumanActionNative();
        }
    }

    private boolean mNeedBeautify, mNeedFilter, mNeedSticker;

    private int mCustomEvent = 0;
    private int[] mBeautifyTextureId, mMakeupTextureId, mFilterTextureOutId, mTextureStickerId;
    protected STMobileAnimalResult mAnimalResult;
    protected int animalFaceLlength = 0;
    protected boolean needAnimalDetect;
    protected boolean hasAddedSticker = false;
    protected boolean hasAdded3DMesh = false;
    protected boolean hasAddedBg = false;

    public int humanActionDetectAndRender(byte[] imageBuffer, int imageFormat, int orientation, int width, int height, int textureId){
        if (mBeautifyTextureId == null) {
            mBeautifyTextureId = new int[1];
            GlUtil.initEffectTexture(width, height, mBeautifyTextureId, GLES20.GL_TEXTURE_2D);
        }

        if (mNeedSticker) {
            if (!hasAddedSticker && mCurrentStickerMaps!=null) {
                Set<Integer> integers = mCurrentStickerMaps.keySet();
                for (Integer packageId : integers) {
                    String path = mCurrentStickerMaps.get(packageId);
                    //Log.d(TAG, "humanActionDetectAndRender: path:" + path);
                    int id = mSTEffectsEngine.addPackage(path);//mNativeManager.getEffectNative().addPackage(path);

                    if (!hasAddedBg && null!=ContextHolder.getCurrentBg() && !TextUtils.isEmpty(ContextHolder.getCurrentBg())){
                        LogUtils.iTag(TAG, "humanActionDetectAndRender: " + ContextHolder.getCurrentBg());
                        Bitmap bitmap  = GlideUtils.INSTANCE.compressBitmap3(ContextHolder.getCurrentBg());
                        byte[] pictureBuffer = STUtils.getRGBAFromBitmap2(bitmap);
                        mSTEffectsEngine.changeBg(id, new STImage(pictureBuffer, STCommonNative.ST_PIX_FMT_RGBA8888, bitmap.getWidth(), bitmap.getHeight()));

//                        mNativeManager.getEffectNative().changeBg(id, new STImage(pictureBuffer, STCommonNative.ST_PIX_FMT_RGBA8888, bitmap.getWidth(), bitmap.getHeight()));
                        hasAddedBg = true;
                    }
                }
                hasAddedSticker = true;
            }
        }
//
        // 设置基础美颜参数
        long startSet3dMeshTime = System.currentTimeMillis();
        IAtyState state = AtyStateContext.getInstance().getState();
        if (state instanceof TryOnVideoAtyState) {
            if (!hasAdded3DMesh) {
                List<BasicEffectEntity> params = SaveVideoParamsManager.getInstance().getParams();
                if (null == params) {
                    for (EffectType type : EffectType.Companion.getAllBasicType()) {
                        mSTEffectsEngine.setBeautyStrength(type.getCode(), 0f);
                    }
                } else {
                    for (BasicEffectEntity item : params) {
                        LogUtils.i(Utils.getApp().getString(R.string.log_try_on_basic) + item);
//                        if (item.getType() == EffectType.TYPE_BASIC_3.getCode() && item.getDes().equals("美白3")) {// 美白3
//                            LogUtils.i(Utils.getApp().getString(R.string.log_try_on_mb) + "设置了美白3");
//                            WhiteningUtils.setWhitening3();
//                        }
//                        if (item.getType() == EffectType.TYPE_BASIC_2.getCode() && item.getDes().equals("美白2")) {// 美白2
//                            WhiteningUtils.firstWhiten2 = true;
//                            WhiteningUtils.setWhitening2(this);
//                            LogUtils.i(Utils.getApp().getString(R.string.log_try_on_mb) + "设置了美白2");
//                        }
//                        if (item.getType() == EffectType.TYPE_BASIC_6.getCode()) {// 磨皮2
//                            LogUtils.i(Utils.getApp().getString(R.string.log_try_on_mp) + "setParam mode" + 2);
//                            setBeautyMode(STEffectBeautyType.EFFECT_BEAUTY_BASE_FACE_SMOOTH, STEffectBeautyType.SMOOTH2_MODE);
//                        }
                        mSTEffectsEngine.setBeautyStrength(item.getType(), item.getStrength());
                    }
                }
//
                // 设置试妆
                HashMap<EffectType, StickerItem> tryOnMap = SaveVideoParamsManager.getInstance().getTryOnMap();
                for(Map.Entry<EffectType, StickerItem> entry : tryOnMap.entrySet()) {
                    mSTEffectsEngine.setBeauty(entry.getKey().getCode(), entry.getValue().path);
                }

//                // 设置光泽度等
//                SaveVideoParamsManager instance = SaveVideoParamsManager.getInstance();
//                HashMap<EffectType, TryOnStrength> tryOnStrengthMap = instance.getTryOnStrength();
//                for(Map.Entry<EffectType, TryOnStrength> entry : tryOnStrengthMap.entrySet()) {
//                    EffectType keyType = entry.getKey();
//                    TryOnStrength value = entry.getValue();
//                    STEffectTryonInfo entityInfo = mSTEffectsEngine.getTryOnParam(keyType.getCode());
//                    if (entityInfo != null) {
//                        if (value.getSeekType() == TryOnSeekBarType.TYPE_STRENGTH) {// 强度
//                            entityInfo.setStrength(value.getProgress());
//                        } else if (value.getSeekType() == TryOnSeekBarType.TYPE_HIGHLIGHT) {// 光泽度
//                            entityInfo.setHighlight(value.getProgress());
//                        } else if (value.getSeekType() == TryOnSeekBarType.TYPE_MIDTONE) {//
//                            entityInfo.setMidtone(value.getProgress());
//                        } else if (value.getSeekType() == TryOnSeekBarType.TYPE_LINE_WIDTH_RATIO) {
//                            entityInfo.setLineWidthRatio(value.getProgress());
//                        }
//                        mSTEffectsEngine.setTryOnParam(entityInfo, keyType.getCode());
//                    }
//                }

                // 设置质地
                if (tryOnMap.containsKey(EffectType.TYPE_TRY_ON_LIP)) {
                    STEffectTryonInfo entityInfo = mSTEffectsEngine.getTryOnParam(EffectType.TYPE_TRY_ON_LIP.getCode());
                    if (entityInfo!=null) {
                        entityInfo.setLipFinishType(SaveVideoParamsManager.getInstance().getLipStickFinish());
                        mSTEffectsEngine.setTryOnParam(entityInfo, EffectType.TYPE_TRY_ON_LIP.getCode());
                    }
                }
            }
            hasAdded3DMesh = true;
        } else {
            if (!hasAdded3DMesh) {
                EnumMap<EffectType, Float> strengthMap = LocalDataStore.getInstance().getBasicStrengthMap();
                Set<EffectType> effectTypes = strengthMap.keySet();
                for (EffectType type : effectTypes) {
                    if (EffectType.Companion.getWzh3d().contains(type)) {
                        continue;
                    }
                    if (strengthMap.get(type) != 0) {
                        mSTEffectsEngine.setBeautyStrength(type.getCode(), strengthMap.get(type));
                    }
                }
                hasAdded3DMesh = true;
            }
        }
        LogUtils.iTag(TAG, " humanActionDetectAndRender set3dMesh cost time:" + (System.currentTimeMillis() - startSet3dMeshTime));

        STImageBuffer image = new STImageBuffer(imageBuffer, imageFormat, width, height);
        STEffectTexture effectTexture = new STEffectTexture(textureId, width, height, STTextureFormat.FORMAT_TEXTURE_OES.getFormat());
        STEffectsInput stEffectsInput = new STEffectsInput(effectTexture, image, 0,
                0, false, false, 0);
        STEffectsOutput stEffectsOutput = new STEffectsOutput(mBeautifyTextureId[0], null, null);
        mSTEffectsEngine.processTexture(stEffectsInput, stEffectsOutput);
        textureId = stEffectsOutput.getTextureId();
        return textureId;
    }

    public void initRender(int width, int height){
        if(mSTGLRender == null){
            mSTGLRender = new STGLRender();
        }

        mSTGLRender.init(width, height);

        if (!TextUtils.isEmpty(ContextHolder.getStylePath())) {
            //Log.d(TAG, "humanActionDetectAndRender: " + ContextHolder.getStylePath());
            mSTEffectsEngine.addPackage(ContextHolder.getStylePath());
        } else {
            LogUtils.iTag(TAG, "humanActionDetectAndRender style is null");
        }
    }

    public void releaseRender() {
        if (mSTGLRender != null) {
            mSTGLRender.destroy();
            mSTGLRender.destroyFrameBuffers();
            mSTGLRender = null;
        }

        if(mSTEffectsEngine != null){
            mSTEffectsEngine.release();
            mSTEffectsEngine = null;
        }

        if (mFilterTextureOutId != null) {
            GLES20.glDeleteTextures(1, mFilterTextureOutId, 0);
        }
        mFilterTextureOutId = null;

        if (mBeautifyTextureId != null) {
            GLES20.glDeleteTextures(1, mBeautifyTextureId, 0);
        }
        mBeautifyTextureId = null;

        if (mMakeupTextureId != null) {
            GLES20.glDeleteTextures(1, mMakeupTextureId, 0);
        }
        mMakeupTextureId = null;

        if (mTextureStickerId != null) {
            GLES20.glDeleteTextures(1, mTextureStickerId, 0);
        }
        mTextureStickerId = null;
    }

    public void releaseHumanAction(){
        if(mSTMobileHumanActionNative != null){
            mSTMobileHumanActionNative.destroyInstance();
            mSTMobileHumanActionNative = null;
        }
    }

    public STGLRender getSTGLRender(){
        return mSTGLRender;
    }

    public void setDetectConfig(Long config){
        mHumanActionConfigDetect = config;
    }

    public void setRenderOptions(boolean needBeauty, boolean needMakeup, boolean needSticker, boolean needFilter){
        mNeedBeautify = needBeauty;
        mNeedSticker = needSticker;
        mNeedFilter = needFilter;
    }

    private String mStickerPath;
    private String mCurrentStickerPath;
    private LinkedHashMap<Integer, String> mCurrentStickerMaps;
    public void setCurrentStickerPath(String path){
        mStickerPath = path;
    }

    public void setCurrentStickerPath(LinkedHashMap<Integer, String> map){
        LogUtils.iTag(TAG, "setCurrentStickerPath: " + map);
        hasAddedSticker = false;
        hasAdded3DMesh = false;
        mCurrentStickerMaps = map;
    }

    public void setCurrentBeautyParams(){
        setBeautyParamsForHandle();
    }

    private String[] mMakeupPaths = new String[Constants.MAKEUP_TYPE_COUNT];
    private float[] mMakeupStrengths = new float[Constants.MAKEUP_TYPE_COUNT];
    public void setCurrentMakeups(String[] paths, float[] strengths){
        for (int i = 0; i < paths.length; i++){
            mMakeupPaths[i] = paths[i];
            mMakeupStrengths[i] = strengths[i];

            if(mMakeupPaths[i] != null && EffectInfoDataHelper.getInstance().getMakeupHigh()){
                int ret = mSTEffectsEngine.setBeauty(convertMakeupTypeToNewType(i), mMakeupPaths[i]);
            }

            mSTEffectsEngine.setBeautyStrength(convertMakeupTypeToNewType(i), mMakeupStrengths[i]);
        }
    }

    public void setGreenParamsMap(HashMap<Integer, Float> map){
        for (int key : map.keySet()) {
            if(key == STEffectParam.EFFECT_PARAM_GREEN_COLOR_BALANCE || key == STEffectParam.EFFECT_PARAM_GREEN_SPILL_BY_ALPHA){
                mSTEffectsEngine.setEffectParams(key,
                        map.get(key));
            }else {
                mSTMobileHumanActionNative.setParam(key, map.get(key));
            }

            Log.e(TAG, "setGreenParamsMap: "+ key +" "+  map.get(key));
        }
    }

    public int convertMakeupTypeToNewType(int type){
        int newType = 0;

        if(type == 1){
            newType = 406;
        } else if(type == 2){
            newType = 403;
        }else if(type == 3){
            newType = 402;
        }else if(type == 4){
            newType = 404;
        }else if(type == 5){
            newType = 405;
        }else if(type == 6){
            newType = 407;
        }else if(type == 7){
            newType = 408;
        }else if(type == 8){
            newType = 409;
        }else if(type == 9){
            newType = 401;
        }

        return newType;
    }

    private String mFilterPath;
    private float mFilterStrength;
    public void setCurrentFilter(String path, float strength){
        mFilterPath = path;
        mFilterStrength = strength;

        mSTEffectsEngine.setBeauty(STEffectBeautyType.EFFECT_BEAUTY_FILTER, mFilterPath);
        mSTEffectsEngine.setBeautyStrength(STEffectBeautyType.EFFECT_BEAUTY_FILTER, mFilterStrength);
    }

    private void setBeautyParamsForHandle(){
    }
}
