package com.namgyuworld.voca.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.namgyuworld.utility.Logger;
import com.namgyuworld.voca.BuildConfig;
import com.namgyuworld.voca.R;
import com.namgyuworld.voca.network.URLs;
import com.namgyuworld.voca.push.GCM;
import com.namgyuworld.voca.util.AppUtil;

/**
 * This is for Splash Activity
 * <br><br>
 * Created by Daniel Park on 2015-04-18.
 */
public class SplashActivity extends Activity {

    private Logger LOG = Logger.getInstance();

    TextView appVersionView;

    Handler mHandler;
    Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appVersionView = (TextView) findViewById(R.id.appVersionView);

        if (AppUtil.isDebuggable(getApplicationContext())) {
            URLs.setDebug(true);
            LOG.enableLog();  // Enable Log...
        } else {
            URLs.setDebug(false);
            LOG.disableLog(); // Disable Log...
        }

        try {
            if (BuildConfig.DEBUG)    // 디버그는 색 다르게 표시.
                appVersionView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            appVersionView.setText(String.format("%s (%s)%s",
                    BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.DEBUG ? " - INTERNAL USE ONLY" : ""));
        } catch (Exception ignore) {
        }

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };

        mHandler.postDelayed(mRunnable, 2000);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        super.onDestroy();
    }
}
