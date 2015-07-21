package com.namgyuworld.voca.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.namgyuworld.utility.Logger;
import com.namgyuworld.voca.R;
import com.namgyuworld.voca.network.URLs;
import com.namgyuworld.voca.push.GCM;
import com.namgyuworld.voca.util.AppUtil;

/**
 * This is for Splash Activity
 * <br><br>
 * Created by Daniel Park on 2015-04-18.
 */
public class SplashActivity extends Activity{

    private Logger LOG = Logger.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (AppUtil.isDebuggable(getApplicationContext())) {
            URLs.setDebug(true);
            LOG.enableLog();  // Enable Log...
        } else {
            URLs.setDebug(false);
            LOG.disableLog(); // Disable Log...
        }

        GCM.getRegistrationID(this);



        // Go to MainActivity in 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}
