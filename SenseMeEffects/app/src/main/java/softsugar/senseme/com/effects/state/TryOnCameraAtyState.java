package softsugar.senseme.com.effects.state;

import softsugar.senseme.com.effects.activity.BaseActivity;
import softsugar.senseme.com.effects.view.widget.CameraActionBarView;
import softsugar.senseme.com.effects.view.widget.CameraLayout;
import softsugar.senseme.com.effects.view.widget.EffectType;

// TryOn-预览版
public class TryOnCameraAtyState implements IAtyState {

    public EffectType mChildType;

    public TryOnCameraAtyState(EffectType childType) {
        if (childType == null) {
            mChildType = EffectType.TYPE_TRY_ON_BASIC;
        } else {
            this.mChildType = childType;
        }
    }

    @Override
    public void initSelfView(BaseActivity aty) {
        aty.initViewForTryOnState();
    }

    @Override
    public void initSelfView(CameraLayout aty) {

    }

    @Override
    public void initSelfView(CameraActionBarView view) {
        view.initViewForTryOn();
    }
}
