package com.namgyuworld.voca.push.util;

import com.namgyuworld.voca.push.model.PushContentsItem;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This class has no compatibility. <br>
 *     This is only for my own POJO.
 *     Convert Java object to JSON String & send
 * <br><br>
 * Created by Daniel Park on 2015-03-22.
 */
public class ConvertJavaToJson {

    private final String TAG = "ConvertJavaToJson";

    public void writeValue(DataOutputStream outputStream, PushContentsItem contents){
        String json = "";

        json += "{" + "\"data\":{";
        // add title
        json += "\"title\":" + "\""+ contents.getData().get("title") + "\",";
        // add msg
        json += "\"msg\":" + "\"" +contents.getData().get("msg") +"\"";
        json += "},";
        // data end

        json += "\"registration_ids\":[";

        for(int i=0; i < contents.getRegistrationIds().size(); i++){
            json += "\""+contents.getRegistrationIds().get(i) + "\"";
            json += ",";
        }
        // Last "," should be removed.
        json = json.substring(0, json.length()-1);

        json += "]}";

//        Log.i(TAG, json);

        // Write JSON to DataOutputStream
        try {
            outputStream.writeBytes(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
