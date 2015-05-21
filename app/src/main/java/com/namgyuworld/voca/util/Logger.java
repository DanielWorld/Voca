package com.namgyuworld.voca.util;

import android.util.Log;

import com.namgyuworld.voca.util.convert.StringUtil;

/**
 * The package of Log <br>
 * (Log.i / Log.v / Log.e / Log.w / Log.d)
 * <br><br>
 * Created by Daniel Park on 2015-03-22.
 */
public class Logger {

    private static Logger sThis;

    public static Logger getInstance() {
        if (sThis == null)
            sThis = new Logger();

        return sThis;
    }

    private boolean mLogFlag;

    /**
     * Enable Log function
     */
    public void enableLog() {
        mLogFlag = true;
    }

    /**
     * Set disable Log function
     */
    public void disableLog() {
        mLogFlag = false;
    }

    public void v(String tag, String msg) {
        if (mLogFlag) {
            if(StringUtil.isNull(tag) || StringUtil.isNull(msg)){
                Log.v(StringUtil.setNullToEmpty(tag), StringUtil.setNullToEmpty(msg));
            }
            else {
                Log.v(tag, msg);
            }
        }
    }

    public void d(String tag, String msg) {
        if (mLogFlag) {
            if(StringUtil.isNull(tag) || StringUtil.isNull(msg)){
                Log.d(StringUtil.setNullToEmpty(tag), StringUtil.setNullToEmpty(msg));
            }
            else {
                Log.d(tag, msg);
            }
        }
    }

    public void e(String tag, String msg) {
        if (mLogFlag) {
            if(StringUtil.isNull(tag) || StringUtil.isNull(msg)){
                Log.e(StringUtil.setNullToEmpty(tag), StringUtil.setNullToEmpty(msg));
            }
            else {
                Log.e(tag, msg);
            }
        }
    }

    public void i(String tag, String msg) {
        if (mLogFlag) {
            if(StringUtil.isNull(tag) || StringUtil.isNull(msg)){
                Log.i(StringUtil.setNullToEmpty(tag), StringUtil.setNullToEmpty(msg));
            }
            else {
                Log.i(tag, msg);
            }
        }
    }

    public void w(String tag, String msg) {
        if (mLogFlag) {
            if(StringUtil.isNull(tag) || StringUtil.isNull(msg)){
                Log.w(StringUtil.setNullToEmpty(tag), StringUtil.setNullToEmpty(msg));
            }
            else {
                Log.w(tag, msg);
            }
        }
    }
}