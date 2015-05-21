package com.namgyuworld.voca.util.filepath;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Get voca mp3 file path
 * <br><br>
 * Created by Daniel Park on 2015-04-11.
 */
public class FilePath {

    /**
     * Get voca mp3 file path (/sdcard/Android/data/<package-name>/files/audio/<word>.mp3
     *
     * @param ctx
     * @param word
     */
    public static String getVocaMP3ExternalPath(Context ctx, String word) {
        return ctx.getExternalFilesDir("").getAbsolutePath() + File.separator + "audio" + File.separator + word + ".mp3";
    }

    /**
     * Get voca mp3 file path ( /data/data/<package-name>/files/audio/<word>.mp3)
     *
     * @param ctx
     * @param word
     * @return
     */
//    public static String getVocaMP3InternalPath(Context ctx, String word) {
//        return ctx.getFilesDir().getAbsolutePath() + File.separator + "audio" + File.separator + word + ".mp3";
//    }

    /**
     * Get file download path (/sdcard/Download/<fileName>
     * @param ctx
     * @param fileName
     * @return
     */
    public static String getVocaFileDownloadPath(Context ctx, String fileName){
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileName;
    }

    /**
     * Get database path
     * @param ctx
     * @param databaseName
     * @return
     */
    public static String getVocaDatabasePath(Context ctx, String databaseName){
        return ctx.getDatabasePath(databaseName).toString();
    }
}
