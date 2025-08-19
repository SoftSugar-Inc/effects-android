package softsugar.senseme.com.effects.utils;

import android.content.Context;
import com.blankj.utilcode.util.LogUtils;

import java.util.EnumMap;
import java.util.List;

import javax.microedition.khronos.egl.EGLContext;

import softsugar.senseme.com.effects.view.MakeupItem;
import softsugar.senseme.com.effects.view.StickerItem;
import softsugar.senseme.com.effects.view.widget.EffectType;

public class ContextHolder {

    static Context ApplicationContext;

    private static boolean camera2Mode;
    private static boolean checkLicenseSuccess;

    private static EnumMap<EffectType, List<MakeupItem>> makeUpMap;

    private static EnumMap<EffectType, List<StickerItem>> styleMap;

    private static EGLContext mEGLContext;

    private static String stylePath;
    private static String styleMakeUpStrength;
    private static String styleFilterStrength;

    private static String currentBg;

    public static String getStylePath() {
        return stylePath;
    }

    public static void setStylePath(String stylePath) {
        ContextHolder.stylePath = stylePath;
    }

    public static String getStyleMakeUpStrength() {
        return styleMakeUpStrength;
    }

    public static void setStyleMakeUpStrength(String styleMakeUpStrength) {
        ContextHolder.styleMakeUpStrength = styleMakeUpStrength;
    }

    public static String getStyleFilterStrength() {
        return styleFilterStrength;
    }

    public static void setStyleFilterStrength(String styleFilterStrength) {
        ContextHolder.styleFilterStrength = styleFilterStrength;
    }

    public static EGLContext getEGLContext() {
        return mEGLContext;
    }

    public static void setmEGLContext(EGLContext mEGLContext) {
        ContextHolder.mEGLContext = mEGLContext;
    }

    public static EnumMap<EffectType, List<MakeupItem>> getMakeUpMap() {
        return makeUpMap;
    }

    public static void setMakeUpMap(EnumMap<EffectType, List<MakeupItem>> makeUpMap) {
        ContextHolder.makeUpMap = makeUpMap;
    }

    public static EnumMap<EffectType, List<StickerItem>> getStyleMap() {
        return styleMap;
    }

    public static void setStyleMap(EnumMap<EffectType, List<StickerItem>> styleMap) {
        ContextHolder.styleMap = styleMap;
    }

    public static void initial(Context context) {
        ApplicationContext = context;
        camera2Mode = Constants.CFG_K_USECAMERA2;
    }

    public static Context getContext() {
        return ApplicationContext;
    }

    public static String getCurrentBg() {
        return currentBg;
    }

    public static void setCurrentBg(String currentBg) {
        ContextHolder.currentBg = currentBg;
    }

    public static boolean isCamera2Mode() {
        return camera2Mode;
    }

    public static void setCamera2Mode(boolean camera2Mode) {
        ContextHolder.camera2Mode = camera2Mode;
    }

    public static boolean isCheckLicenseSuccess() {
        return checkLicenseSuccess;
    }

    public static void setCheckLicenseSuccess(boolean checkLicenseSuccess) {
        ContextHolder.checkLicenseSuccess = checkLicenseSuccess;
    }
}
