package com.namgyuworld.voca.push.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Daniel Park on 2015-03-22.
 */
public class GoogleCloudMessaging {

    private static final String TAG = GoogleCloudMessaging.class.getSimpleName();

    static GoogleCloudMessaging sThis;
    private Context mContext;
    private PendingIntent mPendingIntent;
    final BlockingQueue<Intent> mBlockingQueue = new LinkedBlockingQueue();

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        public void handleMessage(Message msg){
            Intent localIntent = (Intent) msg.obj;
            GoogleCloudMessaging.this.mBlockingQueue.add(localIntent);
        }
    };
    private Messenger mMessenger = new Messenger(this.mHandler);

    public GoogleCloudMessaging(){}

    public static synchronized GoogleCloudMessaging getInstance(Context context){
        if (sThis == null)
        {
            sThis = new GoogleCloudMessaging();
            sThis.mContext = context;
        }
        return sThis;
    }

    public String register(String... senderIds) throws IOException{

        Log.i(TAG, "register");
        // Check this method is called on Main Thread.
        // (Not allowed to run this on Main Thread)
//        if(Looper.getMainLooper() == Looper.getMainLooper()){
//            Log.i(TAG, "Main thread error");
//            throw new IOException("MAIN_THREAD");
//        }
        this.mBlockingQueue.clear();
        b(senderIds);
        try{
            Intent localIntent = (Intent) this.mBlockingQueue.poll(5000L, TimeUnit.MILLISECONDS);
            if(localIntent == null){
                throw new IOException("GoogleCloudMessaging : SERVICE_NOT_AVAILABLE");
            }
            String str1 = localIntent.getStringExtra("registration_id");
            if(str1 != null){
                return str1;
            }
            throw new IOException("GoogleCloudMessaging : SERVICE_NOT_AVAILABLE");
        }catch(InterruptedException e){
            throw new IOException("Sorry! Something's wrong with Registraion ID for GCM");
        }
    }


    private void b(String... senderIds){
        String str = c(senderIds);
        Intent localIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        localIntent.setPackage("com.google.android.gms");
        localIntent.putExtra("google.messenger", this.mMessenger);
        b(localIntent);
        localIntent.putExtra("sender", str);
        this.mContext.startService(localIntent);
    }

    synchronized void b(Intent paramIntent){
        if (this.mPendingIntent == null) {
            this.mPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(), 0);
        }
        paramIntent.putExtra("app", this.mPendingIntent);
    }

    String c(String... paramVarArgs){
        if((paramVarArgs == null) || (paramVarArgs.length == 0)){
            throw new IllegalArgumentException("No sender ids for GCM");
        }
        StringBuilder localStringBuilder = new StringBuilder(paramVarArgs[0]);
        for(int i = 1; i < paramVarArgs.length; i++){
            localStringBuilder.append(',').append(paramVarArgs[i]);
        }
        return localStringBuilder.toString();
    }
}