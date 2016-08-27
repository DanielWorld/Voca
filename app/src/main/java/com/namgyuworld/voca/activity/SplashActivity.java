package com.namgyuworld.voca.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

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

    final int REQUEST_PERMISSION = 1001;

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

        // Daniel (2016-08-27 17:35:16): Check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                return;
            }
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {

            // Daniel (2016-08-01 11:19:11): Not matched!
            if (permissions.length != grantResults.length) {
                finish();
                return;
            }

            for (int index = 0; index < grantResults.length; index++) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(permissions[index])) {
                        Toast.makeText(SplashActivity.this, R.string.required_permissions_message, Toast.LENGTH_LONG).show();

                        // Daniel (2016-05-02 12:00:45): Let them move to permission settings page
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                        // 종료
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        super.onDestroy();
    }
}
