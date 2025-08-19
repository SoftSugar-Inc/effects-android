package softsugar.senseme.com.effects.state;

import com.blankj.utilcode.util.LogUtils;

import softsugar.senseme.com.effects.activity.BaseActivity;
import softsugar.senseme.com.effects.view.widget.CameraActionBarView;
import softsugar.senseme.com.effects.view.widget.CameraLayout;

/**
 * interface IAtyState:
 * ImgAtyState:图片模式
 * ImgTryOnAtyState:图片TryOn模式
 * CameraAtyState:相机预览模式
 */
public class AtyStateContext {
    private static final String TAG = "AtyStateContext";
    
    private IAtyState mAtyState = new CameraAtyState();

    private static AtyStateContext instance = null;

    private AtyStateContext() {

    }

    public static AtyStateContext getInstance() {
        synchronized (AtyStateContext.class) {
            if (instance == null) {
                instance = new AtyStateContext();
            }
        }
        return instance;
    }

    public void setState(IAtyState state) {
        LogUtils.iTag(TAG, "setState() called with: state = [" + state + "]");
        mAtyState = state;
    }

    public IAtyState getState() {
        LogUtils.iTag(TAG, "getState() called" + mAtyState);
        return mAtyState;
    }

//    public void setState(EffectType type) {
//        LogUtils.iTag(TAG, "setState() called with: type = [" + type + "]");
//        if (type == EffectType.TYPE_TRY_ON_BASIC) {
//            mAtyState = new TryOnCameraAtyState(null);
//        } else {
//            mAtyState = new CameraAtyState();
//        }
//    }

    public void initView(BaseActivity aty) {
        mAtyState.initSelfView(aty);
    }

    public void initView(CameraLayout view) {
        mAtyState.initSelfView(view);
    }

    public void initView(CameraActionBarView view) {
        mAtyState.initSelfView(view);
    }

    public void release() {
        LogUtils.iTag(TAG, "release() called");
//        instance = null;
    }
}
