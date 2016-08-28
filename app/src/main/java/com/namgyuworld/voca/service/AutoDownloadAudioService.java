package com.namgyuworld.voca.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import com.namgyuworld.voca.database.VocaDBOpenHelper;
import com.namgyuworld.voca.model.VocaPOJO;
import com.namgyuworld.voca.util.download.DownloadAudio;
import com.namgyuworld.voca.util.filepath.FilePath;
import com.namgyuworld.voca.util.logger.Logger;

import java.util.List;

/**
 * Service that downloads mp3 files...
 * <br><br>
 * Created by Daniel Park on 2015-04-12.
 */
public class AutoDownloadAudioService extends IntentService{

    private static final String SERVICE_NAME = "auto_download_audio_service";

    private static final String TAG = AutoDownloadAudioService.class.getSimpleName();
    private static Logger LOG = Logger.getInstance();

    private static long sTriggerTime;
    private static long sPriorityDebug = 3000;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * SERVICE_NAME :  Used to name the worker thread, important only for debugging.
     */
    public AutoDownloadAudioService() {
        super(SERVICE_NAME);
    }

    public static final void startService(Context context){
// 예약된 서비스 작업이 없을 때
        if(!existsAlarm()){
            synchronized (AutoDownloadAudioService.class){
                if(!existsAlarm()){
                    Toast.makeText(context, "Start downloading auto mp3 Service...", Toast.LENGTH_SHORT).show();
                    LOG.i("Create new audio download schedule..");
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    // app이 debug mode라면 sPriorityDebug, 아니라면 sPriority초 + 부팅 시간 뒤에 동작합니다.v
                    final long triggerAtMillis = SystemClock.elapsedRealtime() + sPriorityDebug;
                    Intent service = new Intent(context, AutoDownloadAudioService.class);
                    PendingIntent operation = PendingIntent.getService(context, 0, service, PendingIntent.FLAG_UPDATE_CURRENT);
                    am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, operation);

                    // 마지막 예약 시간 저장
                    sTriggerTime = triggerAtMillis;
                    LOG.i("Saved service alarm time, After " + String.valueOf(triggerAtMillis / 1000) + " passed, start Service.");
                }
            }
        }else{
            LOG.i("Already reserved Service exists..");
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LOG.i("onHandleIntent()");

        String audioPath = "http://ssl.gstatic.com/dictionary/static/sounds/de/0/";
        // Get All word list
        List<VocaPOJO> mList = new VocaDBOpenHelper(getApplicationContext()).getAllVocaList();
        for(VocaPOJO i : mList) {
            String audioDownDirPath = FilePath.getVocaMP3ExternalPath(getApplicationContext(), i.getVocaWord());
            DownloadAudio.startDownload(audioPath + i.getVocaWord() + ".mp3", audioDownDirPath);
        }
    }

    private static boolean existsAlarm() {
        return sTriggerTime != 0 && sTriggerTime > SystemClock.elapsedRealtime();
    }
}
