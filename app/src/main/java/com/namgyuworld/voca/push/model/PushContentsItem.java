package com.namgyuworld.voca.push.model;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * When server sends push to client using GCM server,
 * it requires some body field
 * <br><br>
 * Created by Daniel Park on 2015-03-22.
 */
public class PushContentsItem {
    private List<String> registration_ids;
    private Map<String, String> data;

    public void addRegId(String regId) {
        if (registration_ids == null)
            registration_ids = new LinkedList<String>();
        registration_ids.add(regId);
    }

    public void createData(String title, String message){
        if(data == null){
            data = new HashMap<String, String>();
        }

        data.put("title", title);
        data.put("msg", message);
    }

    public List<String> getRegistrationIds(){
        return registration_ids;
    }

    public Map<String, String> getData(){
        return data;
    }
}