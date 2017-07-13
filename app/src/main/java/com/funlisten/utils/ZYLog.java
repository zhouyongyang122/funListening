package com.funlisten.utils;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ZY on 17/3/14.
 */

public class ZYLog implements HttpLoggingInterceptor.Logger {

    private static final String TAG = "ZYLog";

    public static void e(String msg) {
        Logger.e(msg + "");
    }

    public static void e(String tag, String msg) {
        Logger.t(tag).e(msg + "");
    }

    public static void json(String tag, String json) {
        Logger.t(tag).json(json + "");
    }

    public static void d(String tag, String msg) {
        Logger.t(tag).d(msg + "");
    }

    public static void d(String msg) {
        Logger.d(msg + "");
    }

    public static void i(String tag, String msg) {
        Logger.t(tag).i(msg + "");
    }

    public static void i(String msg) {
        Logger.i(msg);
    }

    public static void init(boolean debug) {
        if (debug) {
            Logger.init(TAG).setMethodCount(0).hideThreadInfo();
        } else {
            Logger.init(TAG).setMethodCount(0).hideThreadInfo().setLogLevel(LogLevel.NONE);
        }
    }

    @Override
    public void log(String message) {
        if (message == null) {
            message = "空数据";
        }
        if (message.startsWith("{")) {
            json(TAG, message);
        } else {
            i(TAG, message);
        }
    }
}
