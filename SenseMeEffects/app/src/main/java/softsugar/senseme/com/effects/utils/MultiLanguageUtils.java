package softsugar.senseme.com.effects.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import androidx.annotation.StringRes;
import android.util.DisplayMetrics;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.util.Locale;

import softsugar.senseme.com.effects.SenseMeApplication;
import softsugar.senseme.com.effects.activity.WelcomeActivity;

public class MultiLanguageUtils {
    private static final String TAG = "MultiLanguageUtils";
    public static Context appContext = SenseMeApplication.getContext();

    public static void initLanguageConfig() {
        Resources resources = appContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale;
        if (isUserSet()) {
            locale = getSPSelectedLocale();
        } else {
            locale = getSystemLocale();
        }
        LogUtils.iTag(TAG, "initLanguageConfig local: " + locale.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            config.setLocales(new LocaleList(locale));
            appContext.createConfigurationContext(config);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
    }

    public static void setChinese() {
        setLanguageSave(LANGUAGE_ZH_RCN);
    }

    public static void setEnglish() {
        setLanguageSave(LANGUAGE_ENGLISH);
    }

    public static void setJapan() {
        setLanguageSave(LANGUAGE_JAPAN);
    }

    public static void setKorean() {
        setLanguageSave(LANGUAGE_KOREAN);
    }


    private static final int LANGUAGE_DEFAULT = 0;
    private static final int LANGUAGE_ENGLISH = 1;
    private static final int LANGUAGE_ZH_RCN = 2;
    private static final int LANGUAGE_ZH_RTW = 3;
    private static final int LANGUAGE_KOREAN = 4;// 韩语 KOREAN
    private static final int LANGUAGE_JAPAN = 5;// 日语 JAPAN
    private static final String LANGUAGE_KEY = "language";

    private static void setLanguageSave(int lang) {
        setLang(lang);
        saveCurrentMode(lang);
    }

    private static Locale getLocal(int lang) {
        Locale locale;
        switch (lang) {
            case LANGUAGE_DEFAULT:
                locale = Locale.getDefault();
                break;
            case LANGUAGE_ENGLISH:
                locale = Locale.US;
                break;
            case LANGUAGE_ZH_RCN:
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case LANGUAGE_ZH_RTW:
                locale = new Locale("zh", "TW");
                break;
            case LANGUAGE_KOREAN:
                locale = Locale.KOREAN;
                break;
            case LANGUAGE_JAPAN:
                locale = Locale.JAPAN;
                break;
            default:
                locale = Locale.SIMPLIFIED_CHINESE;
        }
        return locale;
    }

    private static void setLang(int lang) {
        Locale locale = getLocal(lang);
        Resources resources = appContext.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            config.setLocales(new LocaleList(locale));
            appContext.createConfigurationContext(config);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
    }

    private static void saveCurrentMode(int lang) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(LANGUAGE_KEY, lang);
        editor.apply();
    }

    public static String getStr(@StringRes int resId) {
        return appContext.getResources().getString(resId);
    }

    public static Locale getSPSelectedLocale() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(appContext);
        int flag = preference.getInt(LANGUAGE_KEY, LANGUAGE_ZH_RCN);
        return getLocal(flag);
    }

    public static Locale getSystemLocale() {
        Locale currentSystemLocale;
        // API >= 24 （7.0）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentSystemLocale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            currentSystemLocale = Resources.getSystem().getConfiguration().locale;
        }
        LogUtils.i("getSystemLocale:" + currentSystemLocale.getLanguage());
        // 中文简体
        if (currentSystemLocale.getLanguage().contains("zh")) {
            currentSystemLocale = Locale.SIMPLIFIED_CHINESE;
        }
        // 韩语
        else if (currentSystemLocale.getLanguage().contains("ko")){
            currentSystemLocale = Locale.KOREAN;
        }
        // 日语
        else if (currentSystemLocale.getLanguage().contains("ja")){
            currentSystemLocale = Locale.JAPAN;
        }
        else {// 非中文
            currentSystemLocale = Locale.US;
        }
        return currentSystemLocale;
    }

    private static boolean isUserSet() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(appContext);
        int flag = preference.getInt(LANGUAGE_KEY, -1);
        return flag != -1;
    }

    public static void restart(Activity act) {
        Intent intent = new Intent(act, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        act.startActivity(intent);
    }

    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// 8.0
            return updateResources(context);
        } else {
            return context;
        }
    }

    public static boolean isEn() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(appContext);
        int flag = preference.getInt(LANGUAGE_KEY, LANGUAGE_ZH_RCN);
        boolean localIsEnglish = getLocal(flag).getLanguage().equals("en");
        if (localIsEnglish) return true;
        Locale systemLocale = getSystemLocale();
        if (systemLocale == Locale.US) {
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context) {
        Resources resources = context.getResources();
        Locale locale;
        if (isUserSet()) {
            locale = getSPSelectedLocale();
        } else {
            locale = getSystemLocale();
        }
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }
}
