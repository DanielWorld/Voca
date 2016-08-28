package com.namgyuworld.voca.util.media;

import android.media.MediaPlayer;

import com.namgyuworld.voca.util.logger.Logger;

/**
 * Created by Daniel Park on 2015-04-11.
 */
public class MediaPlayers {

    private static final String TAG = MediaPlayers.class.getSimpleName();
    private static Logger LOG = Logger.getInstance();

    static MediaPlayer mp3;

    /**
     * Play mp3 file
     * @param audioPath
     */
    public static void AudioPlay(String audioPath){
        LOG.i(audioPath);

        mp3 = new MediaPlayer();
        try{
            mp3.setDataSource(audioPath);
            mp3.prepare();
            mp3.start();
        }catch (Exception e){
            LOG.e(e.getMessage());
        }
    }
}
