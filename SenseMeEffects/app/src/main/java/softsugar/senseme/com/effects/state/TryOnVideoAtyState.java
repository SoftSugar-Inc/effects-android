package softsugar.senseme.com.effects.state;

import softsugar.senseme.com.effects.activity.BaseActivity;
import softsugar.senseme.com.effects.view.widget.CameraActionBarView;
import softsugar.senseme.com.effects.view.widget.CameraLayout;

// TryOn-图片版
public class TryOnVideoAtyState implements IAtyState {

    @Override
    public void initSelfView(BaseActivity aty) {

    }

    @Override
    public void initSelfView(CameraLayout view) {
        view.initViewForImgTryOn();
    }

    @Override
    public void initSelfView(CameraActionBarView view) {

    }
}
