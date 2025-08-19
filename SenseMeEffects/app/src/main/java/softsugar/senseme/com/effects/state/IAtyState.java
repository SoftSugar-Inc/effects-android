package softsugar.senseme.com.effects.state;

import softsugar.senseme.com.effects.activity.BaseActivity;
import softsugar.senseme.com.effects.view.widget.CameraActionBarView;
import softsugar.senseme.com.effects.view.widget.CameraLayout;

public interface IAtyState {
    void initSelfView(BaseActivity aty);
    void initSelfView(CameraLayout view);
    void initSelfView(CameraActionBarView view);
}
