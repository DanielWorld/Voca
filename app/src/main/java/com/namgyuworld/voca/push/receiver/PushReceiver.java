package com.namgyuworld.voca.push.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.namgyuworld.voca.activity.MainActivity;
import com.namgyuworld.voca.R;
import com.namgyuworld.voca.util.Logger;

/**
 * Get push notification for GCM (Google Cloud Messaging).
 * <br><br>
 * Created by Daniel Park on 2015-04-12.
 */
public class PushReceiver extends BroadcastReceiver{

    private final String TAG = PushReceiver.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        String messageType = getMessageType(intent);

        // If bundle is empty, then leave
        if (!extras.isEmpty()) {
            if (messageType.equals("gcm")) {
                String pushMsg = intent.getStringExtra("msg");
                String pushTitle = intent.getStringExtra("title");
                LOG.i(TAG, "GCM Message\n title : "+ pushTitle + "\n" + " message : "+ pushMsg);

                // show push
                notifyNotification(context, pushTitle, pushMsg);

            }

        }
    }

    /**
     * Check if this message came from GCM
     * @param intent
     * @return
     */
    private String getMessageType(Intent intent) {
        String str1 = intent.getAction();
        if (!"com.google.android.c2dm.intent.RECEIVE".equals(str1)) {
            return null;
        }
        String str2 = intent.getStringExtra("message_type");
        if (str2 != null) {
            return str2;
        }
        return "gcm";
    }

    /**
     * Show push
     * @param context
     * @param title
     * @param msg
     */
    private void notifyNotification(Context context, String title, String msg){
        int requestCode = 1;

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_voca);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(msg);
        mBuilder.setAutoCancel(true);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, 0);
        mBuilder.setContentIntent(pendingIntent);

        nm.notify(requestCode, mBuilder.build());

    }
}
