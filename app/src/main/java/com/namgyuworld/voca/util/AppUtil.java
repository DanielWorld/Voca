package com.namgyuworld.voca.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;

import javax.security.auth.x500.X500Principal;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Application Utility <br>
 * - <b>versionCode</b> <br>
 * - <b>versionName</b> <br>
 * - <b>isDebuggable</b>
 * <br><br>
 * Created by Daniel Park on 2015-04-09.
 */
public class AppUtil {

    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    /**
     * Get application version code
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get application version name
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Check if the application is signed by Debug keystore.
     *
     * @param ctx
     * @return
     */
    public static boolean isDebuggable(Context ctx) {
        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            for (int i = 0; i < signatures.length; i++) {
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                if (cert.getSubjectX500Principal().equals(DEBUG_DN)) {
                    return true;
                }
            }
        } catch (NameNotFoundException e) {
            // debuggable variable will remain false
        } catch (CertificateException e) {
            // debuggable variable will remain false
        }
        return false;
    }

    /**
     * Get current device resolution's width
     * @return device resolution's width
     */
    public static int displayWidth(Activity mActivity) {
        // Get Display resolution
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            // Available only equal or higher than API 13
            Point size = new Point();
            display.getSize(size);
            return size.x;
        } else {
            return display.getWidth();
        }
    }

    /**
     * Get current device resolution's height
     * @return device resolution's height
     */
    public static int displayHeight(Activity mActivity) {
        // Get Display resolution
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            // Available only equal or higher than API 13
            Point size = new Point();
            display.getSize(size);
            return size.y;
        } else {
            return display.getHeight();
        }
    }
}
