package com.softsugar.stmobile.engine.glutils;

import android.util.Log;

/**
 * Log工具类，精简版本
 */
public class STLogUtils {

    // 控制日志输出级别
    private static boolean isDebug = true;

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static void d(String tag, String message) {
        if (isDebug) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (isDebug) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (isDebug) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (isDebug) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (isDebug) {
            Log.e(tag, message, throwable);
        }
    }
}
