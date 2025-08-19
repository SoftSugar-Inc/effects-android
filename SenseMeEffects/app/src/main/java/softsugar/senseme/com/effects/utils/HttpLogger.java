package softsugar.senseme.com.effects.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    // 日志打印开关控制
    private static final boolean DEBUG = true;

    @Override
    public void log(@NonNull String s) {
        if (DEBUG) {
            Log.i("OkHttp", s);
        }
    }
}
