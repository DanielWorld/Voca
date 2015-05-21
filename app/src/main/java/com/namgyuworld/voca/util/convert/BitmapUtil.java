package com.namgyuworld.voca.util.convert;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Daniel Park on 2015-04-19.
 */
public class BitmapUtil {

    /**
     * Decode bitmap from file
     * @param filePath
     * @param outPadding
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws java.io.FileNotFoundException
     */
    public static Bitmap decodeBitmapFromFile(String filePath, Rect outPadding, int reqWidth, int reqHeight) throws FileNotFoundException {

        // Convert String to File
        File file = new File(filePath);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new FileInputStream(file), outPadding, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(new FileInputStream(file), outPadding, options);

    }

    /**
     * Decode bitmap from resource
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapFromResouce(Resources res, int resId, int reqWidth, int reqHeight){

        // First decode with inJustDecodeBounds=true to check dimesions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Resize bitmap image
     * @param bitmap
     * @param maxResolution
     * @return
     */
    public static Bitmap resizeBitmapImage(Bitmap bitmap, int maxResolution){
        // Original bitmap width
        int width = bitmap.getWidth();
        // Original bitmap height
        int height = bitmap.getHeight();
        // new resized bitmap width
        int newWidth = width;
        // new resized bitmap height
        int newHeight = height;
        // resizing ratio
        float resizingRate = 0.0f;

        // Landscape image
        if(width > height){
            if(maxResolution < width){
                resizingRate = maxResolution / (float) width;
                newHeight = (int) (height * resizingRate);
                newWidth = maxResolution;
            }
        }
        else{
            // Portrait image
            if(maxResolution < height){
                resizingRate = maxResolution / (float) height;
                newWidth = (int) (width * resizingRate);
                newHeight = maxResolution;
            }
        }
        // Return resized image
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }
}
