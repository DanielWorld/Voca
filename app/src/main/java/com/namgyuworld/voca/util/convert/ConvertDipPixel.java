package com.namgyuworld.voca.util.convert;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Convert between Dip and Pixel class
 * <br><br>
 * Created by Park Namgyu on 3/31/15.
 */
public class ConvertDipPixel {

    /**
     * Convert dip to pixel according to current display screen
     * @param mApplicationContext - I want you to deliver getApplicationContext()
     * @param dip
     * @return pixel
     */
    public static int ConvertDipToPixel(Context mApplicationContext, float dip){
        // Get device display metrics
        DisplayMetrics metrics = mApplicationContext.getResources().getDisplayMetrics();

        int pixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
        return pixel;
    }

    /**
     * Convert pixel to dip according to current display screen
     * @param mApplicationContext - I want you to deliver getApplicationContext()
     * @param pixel
     * @return dip
     */
    public static float ConvertPixelToDip(Context mApplicationContext, int pixel){
        // Get device display metrics
        DisplayMetrics metrics = mApplicationContext.getResources().getDisplayMetrics();

        float dip = pixel / (metrics.densityDpi / 160f);
        return dip;
    }

    /**
     * Convert pixel to sp according to current display screen
     * @param mApplicationContext
     * @param sp
     * @return
     */
    public static int ConvertSPToPixel(Context mApplicationContext, float sp){
        // Get device display metrics
        DisplayMetrics metrics = mApplicationContext.getResources().getDisplayMetrics();

        int pix = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
        return pix;
    }

}
