package softsugar.senseme.com.effects.display;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.softsugar.stmobile.STCommonNative;
import com.softsugar.stmobile.STMobileEffectNative;
import com.softsugar.stmobile.STSoundPlay;
import com.softsugar.stmobile.engine.STEffectsEngine;
import com.softsugar.stmobile.model.STEffectModuleInfo;
import com.softsugar.stmobile.model.STGanReturn;
import com.softsugar.stmobile.model.STImage;

import softsugar.senseme.com.effects.R;

public class STModuleInfoCallback {
    private static final String TAG = "STModuleInfoCallback";

    public final static String GAN_URL2 = "https://sf.softsugar.com:30380/alg-dispatcher/task/v1/gan";

    private STEffectsEngine mSTEffectsEngine;

    public STModuleInfoCallback(Context context, STEffectsEngine stEffectsEngine){
        mSTEffectsEngine = stEffectsEngine;
        Log.e(TAG, "getInstance: "+STSoundPlay.getInstance(context) );
        if(STSoundPlay.getInstance(context) != null){
        }
    }

    private void setEffectModuleInfo(STImage stImage, STEffectModuleInfo moduleInfo) {
        STGanReturn ganReturn = new STGanReturn(stImage);
        if(mSTEffectsEngine != null){
            if(stImage == null) {
                LogUtils.i(Utils.getApp().getString(R.string.log_gan_setmoduleinfo));
            }
            mSTEffectsEngine.setEffectModuleInfo(ganReturn, moduleInfo);
        }
    }

    public enum GanErrorEnum {
        E_NO_FACE,
        E_NO_NET,
        E_NO_FACE_ANDROID_HOR_SCREEN, // 横屏
    }

    public interface Listener {
        void onError(GanErrorEnum error, int id);
    }

    private static Listener listener;
    public static void setListener(Listener listener) {
        STModuleInfoCallback.listener = listener;
    }
}
