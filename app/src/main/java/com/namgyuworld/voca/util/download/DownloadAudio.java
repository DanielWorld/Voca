package com.namgyuworld.voca.util.download;

import android.os.AsyncTask;

import com.namgyuworld.utility.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Daniel Park on 4/10/15.
 */
public class DownloadAudio {

    private static final String TAG = DownloadAudio.class.getSimpleName();
    private static Logger LOG = Logger.getInstance();


    /**
     * Start download mp3 file from URL
     *
     * @param audioURL
     * @param audioDownDirPath
     */
    public static void startDownload(final String audioURL, final String audioDownDirPath) {
        LOG.i(TAG, audioURL);

        final File file = new File(audioDownDirPath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            // If there isn't folder there, it causes error
            catch (IOException e) {
                new File(file.getParent()).mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e1) {
                    LOG.e(TAG, e1.getMessage());
                }
            } finally {
                LOG.i(TAG, "File was created");
            }
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    URL url = new URL(audioURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(3000);
                    conn.setUseCaches(false);

                    int totalSize = conn.getContentLength();

                    if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                        LOG.i(TAG, "Start downloading mp3 file...");

                        /** Start download */
                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), totalSize);
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                        int data = 0;
                        while ((data = bis.read()) != -1)
                            bos.write(data);

                        LOG.i(TAG, "Download audio file has completed!");
                        bos.close();
                        bis.close();
                        conn.disconnect();
                    } else {
                        LOG.e(TAG, "failed to connect to Server...");
                    }
                } catch (Exception e) {
                    LOG.e(TAG, e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();


    }
}
