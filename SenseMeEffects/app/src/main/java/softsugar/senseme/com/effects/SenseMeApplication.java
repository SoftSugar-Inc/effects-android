package softsugar.senseme.com.effects;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.hjq.toast.Toaster;
import com.softsugar.library.api.Material;
import com.softsugar.stmobile.engine.glutils.STLogUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import softsugar.senseme.com.effects.utils.Constants;
import softsugar.senseme.com.effects.utils.ContextHolder;
import softsugar.senseme.com.effects.utils.MultiLanguageUtils;
import softsugar.senseme.com.effects.utils.SpUtils;
import softsugar.senseme.com.effects.utils.StyleDataNewUtils;
import softsugar.senseme.com.effects.utils.ThreadUtils;
public class SenseMeApplication extends Application {

    public static final String TAG = "SenseMeApplication";
    private static Context mContext;
    private final static String PROCESS = "softsugar.senseme.com.effects";

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.initial(this);
        Material.INSTANCE.init(this, Constants.APP_ID, Constants.APP_KEY);
        StyleDataNewUtils.INSTANCE.init();
        Toaster.init(this);
        LogUtils.Config config = LogUtils.getConfig().setLogSwitch(false);
        ThreadUtils.getInstance().initThreadPool();

        closeAndroidPDialog();

        mContext = this;
        MultiLanguageUtils.initLanguageConfig();
        new WebView(this).destroy();
        SpUtils.init(this);
        Utils.init(this);
        Material.INSTANCE.updateTokenSync();
        initLogSettings();

        STLogUtils.setDebug(true);
    }

    private void initLogSettings() {
        com.blankj.utilcode.util.LogUtils.getConfig()
                .setLogSwitch(Constants.CFG_K_LOG_SWITCH)
                .setBorderSwitch(false)
                .setLogHeadSwitch(true)
                .setGlobalTag("SoftSugarLog");
    }

    private void initWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (!PROCESS.equals(processName)) {
                WebView.setDataDirectorySuffix("softSugar");
            }
        }
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initWebView();
        MultiDex.install(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
