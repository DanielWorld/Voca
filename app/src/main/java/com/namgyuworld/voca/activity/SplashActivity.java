package com.namgyuworld.voca.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.namgyuworld.voca.MainActivity;
import com.namgyuworld.voca.R;

/**
 * This is for Splash Activity
 * <br><br>
 * Created by Daniel Park on 2015-04-18.
 */
public class SplashActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
