package com.namgyuworld.voca.util.convert;

import android.content.Context;
import android.content.res.Resources;
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
     * @param dip
     * @return pixel
     */
    public static int ConvertDipToPixel(float dip){
        DisplayMetrics metrics;
        try {
            metrics = Resources.getSystem().getDisplayMetrics();
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    metrics);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Convert pixel to dip according to current display screen
     * @param pixel
     * @return dip
     */
    public static float ConvertPixelToDip(int pixel){
        DisplayMetrics metrics;
        try{
            metrics = Resources.getSystem().getDisplayMetrics();
            return Math.round(pixel / metrics.density);
        }catch (Exception e){
            return 0;
        }
    }
}
