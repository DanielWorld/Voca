package com.namgyuworld.voca.push;

import android.content.Context;
import android.os.AsyncTask;

import com.namgyuworld.voca.push.model.PushContentsItem;
import com.namgyuworld.voca.push.util.ConvertJavaToJson;
import com.namgyuworld.voca.push.util.GoogleCloudMessaging;
import com.namgyuworld.voca.util.AppUtil;
import com.namgyuworld.voca.util.Logger;
import com.namgyuworld.voca.util.SharedPrefUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Daniel Park on 2015-04-12.
 */
public class GCM {

    private static final String TAG = GCM.class.getSimpleName();
    private static Logger LOG = Logger.getInstance();

    private static GoogleCloudMessaging gcm;
    private static SharedPrefUtil mPref;

    public GCM(){}

    // get RegistrationID from GCM
    static public void getRegistrationID(final Context mContext){
        /**
         * Google Developer console project number
         */
        final String SENDER_ID = "This is sender id (project number)";

        mPref = new SharedPrefUtil(mContext);
        gcm = GoogleCloudMessaging.getInstance(mContext);

        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... params) {
                /**
                 * Before getting registration id, check if versionCode is changed
                 */
                int currentVersionCode = AppUtil.getAppVersionCode(mContext);
                int previousVersionCode = mPref.getAppVersionCode();

                if (previousVersionCode == currentVersionCode && mPref.getAppVersionCode() != 0) {
                    LOG.i(TAG, "Same versionCode.. No need to request Registration ID");
//                    return "previous registraion ID";
                    return mPref.getGCMRegistrationID();
                } else {
                    LOG.i(TAG, "Different versionCode. Request new Registration ID");
                    // Initialize the record that previous redId was sent to Server
                    mPref.setGCMRegIdToServer(false);
                }

                String regId = "";
                try {
                    regId = gcm.register(SENDER_ID);
                    LOG.i(TAG, "Registraion ID : \n" + regId);
                } catch (Exception e) {
                    LOG.e(TAG, e.getMessage());
                }

                // Save app versionCode to Sharedpreferences
                mPref.setAppVersionCode(currentVersionCode);
                // Save regId to Sharedpreferences
                mPref.setGCMRegistrationID(regId);

                // Check if regid was sent to Server
                if (!mPref.isGCMRegIdToServer()) {
                    LOG.i(TAG, "Try to send GCM regId to server");
                    mPref.setGCMRegIdToServer(true);
                    // Send to Server your registration ID
                    sendRegIDtoServer(regId);
                }
                return regId;
            }

            @Override
            protected void onPostExecute(String s) {
                // This is for push notification test
//                 sendPushToGCM("skdfjklds", "dfjldskfjkl");
            }
        }.execute();
    }

    /**
     * Send registration ID to Server
     */
    private static void sendRegIDtoServer(String regId) {
        //...
        //...
    }

    /**
     * Send push message to GCM server.
     *
     * @param pushTitle - the title of Push
     * @param pushMessage - the message of Push
     */
    static public void sendPushToGCM(String pushTitle, String pushMessage) {

        /**
         * Create Message and store in PushContentsItem
         */
        final PushContentsItem contents = new PushContentsItem();
        contents.createData(pushTitle, pushMessage);
        contents.addRegId(mPref.getGCMRegistrationID());

        /**
         * Google Developer console Server api key
         */
        final String API_KEY = "This is Server api key";

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    // URL
                    URL url = new URL("https://android.googleapis.com/gcm/send");

                    // Open connection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // Specify POST method
                    conn.setRequestMethod("POST");

                    // Set the headers
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", "key=" + API_KEY);

                    conn.setDoOutput(true);

                    // Add JSON data into POST request body
                    // Convert Java object to Json
                    ConvertJavaToJson mapper = new ConvertJavaToJson();

                    // Get connection output stream
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                    // Copy push contents "JSON" info
                    mapper.writeValue(wr, contents);

                    // Send the request
                    wr.flush();

                    // close
                    wr.close();

                    // Get the response
                    int responseCode = conn.getResponseCode();
                    LOG.i(TAG, "response code : " + responseCode);


                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Print result
                    LOG.i(TAG, response.toString());

                } catch (MalformedURLException e) {
                    LOG.e(TAG, e.getMessage());
                } catch (IOException e) {
                    LOG.e(TAG, e.getMessage());
                } catch (Exception e) {
                    LOG.e(TAG, e.getMessage());
                }
                return null;
            }
        }.execute();

    }
}
