package com.namgyuworld.voca.util.logger;

import android.util.Log;

import com.namgyuworld.voca.BuildConfig;

/**
 * Voca 용 Logger
 * <br><br>
 * Copyright (c) 2014-2016 op7773hons@gmail.com
 * Created by Daniel Park on 2016-08-28.
 */
public class Logger {

    private static Logger sThis;

    public Logger(){}

    public synchronized static final Logger getInstance() {
        if (sThis == null)
            sThis = new Logger();

        return sThis;
    }

    private final String TAG = "VocaLogger";
    private boolean mLogFlag = BuildConfig.DEBUG;

    public void v(String msg) {
        if (mLogFlag) {
            Log.v(TAG, "" + msg);
        }
    }

    public void d(String msg) {
        if (mLogFlag) {
            Log.d(TAG, "" + msg);
        }
    }

    public void e(String msg) {
        if (mLogFlag) {
            Log.e(TAG, "" + msg);
        }
    }

    public void i(String msg) {
        if (mLogFlag) {
            Log.i(TAG, "" + msg);
        }
    }

    public void w(String msg) {
        if (mLogFlag) {
            Log.w(TAG, "" + msg);
        }
    }

    public void wtf(String msg) {
        if (mLogFlag) {
            Log.wtf(TAG, "" + msg);
        }
    }
}
