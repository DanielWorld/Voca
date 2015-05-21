package com.namgyuworld.voca.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Daniel Park on 4/10/15.
 */
public class SharedPrefUtil {
    private static SharedPreferences mPref;

    private static final String KEY_PREF = "key_pref";
    private final String KEY_CURRENT_PAGE = "key_current_page";
    private final String KEY_VOCA_FONT_SIZE = "key_voca_font_size";
    private final String KEY_VOCA_FONT_CONTENT_SIZE = "key_voca_font_content_size";
    private final String KEY_VERSION_CODE = "key_version_code";
    private final String KEY_GCM_REG_ID = "key_gcm_reg_id";
    private final String KEY_SEND_GCM_REG_ID_TO_SERVER = "key_send_gcm_reg_id_to_server";
    private final String KEY_SEEK_BAR_STATE = "key_seek_bar_state";

    public SharedPrefUtil(Context ctx){
        if(mPref == null)
        mPref = ctx.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
    }

    /**
     * Save version code
     * @param versionCode
     */
    public void setAppVersionCode(int versionCode){
        mPref.edit().putInt(KEY_VERSION_CODE, versionCode).commit();
    }

    /**
     * Get version code
     * @return
     */
    public int getAppVersionCode(){
        return mPref.getInt(KEY_VERSION_CODE, 1);
    }

    /**
     * Save GCM Registration ID
     * @param regId
     */
    public void setGCMRegistrationID(String regId){
        mPref.edit().putString(KEY_GCM_REG_ID, regId).commit();
    }

    /**
     * Get GCM Registration ID
     * @return
     */
    public String getGCMRegistrationID(){
        return mPref.getString(KEY_GCM_REG_ID, "");
    }

    /**
     * Send GCM Registration id to server
     * @param flag
     */
    public void setGCMRegIdToServer(boolean flag){
        mPref.edit().putBoolean(KEY_SEND_GCM_REG_ID_TO_SERVER, flag).commit();
    }

    /**
     * Check if app sent GCM registration id to server
     * @return
     */
    public boolean isGCMRegIdToServer(){
        return mPref.getBoolean(KEY_SEND_GCM_REG_ID_TO_SERVER, false);
    }

    /**
     * Save the current page
     */
    public void setCurrentPage(int position){
        mPref.edit().putInt(KEY_CURRENT_PAGE, position).commit();
    }
    /**
     * Load the current page
     */
    public int getCurrentPage(){
        return mPref.getInt(KEY_CURRENT_PAGE, 0);
    }
    /**
     * Save the voca font size
     * @param size
     */
    public void setVocaFontSize(int size){
        mPref.edit().putInt(KEY_VOCA_FONT_SIZE, size).commit();
    }
    /**
     * Get voca font size
     * @return
     */
    public int getVocaFontSize(){
        return mPref.getInt(KEY_VOCA_FONT_SIZE, 10);
    }

    /**
     * Save voca font content size
     * @param size
     */
    public void setVocaFontContentSize(int size){
        mPref.edit().putInt(KEY_VOCA_FONT_CONTENT_SIZE, size).commit();
    }

    /**
     * Get voca font content size
     * @return
     */
    public int getVocaFontContentSize(){
        return mPref.getInt(KEY_VOCA_FONT_CONTENT_SIZE, 10);
    }

    /**
     * Set Voca seek bar visible or not
     * @param flag
     */
    public void setVocaSeekBarVisible(boolean flag){
        mPref.edit().putBoolean(KEY_SEEK_BAR_STATE, flag).commit();
    }

    /**
     * Check if Voca seek bar is visible
     * @return
     */
    public boolean isVocaSeekBarVisible(){
        return mPref.getBoolean(KEY_SEEK_BAR_STATE, false);
    }
}
