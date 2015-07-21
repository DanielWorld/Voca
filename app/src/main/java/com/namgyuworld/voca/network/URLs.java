package com.namgyuworld.voca.network;

import java.util.Locale;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p>
 * This file is part of Voca (https://github.com/DanielWorld)
 * Created by danielpark on 2015. 7. 20..
 */
public class URLs {

    private static boolean isDebug;

    /**
     * Set debug mode or not
     * @param flag
     */
    public static void setDebug(boolean flag){isDebug = flag;}

    private static final String URL_HOST = "http://192.168.0.46:8080";
    private static final String URL_DEV = "http://192.168.0.46:8080/spring";

    /**
     * Method
     */
    private static final String FORMAT_SEND_REG_ID = "%s/push/sendRegID";

    public enum ConnectionMethod{
        /* Send registration ID to My Server for Push */
        SEND_REG_ID
    }

    /**
     * Get url to send whatever it is...
     * @return
     */
    public static String getURL(ConnectionMethod method){
        if(method == null){
            throw new IllegalArgumentException("method can't be null.");
        }

        // Get Host url
        final String hostURL = isDebug ? URL_DEV : URL_HOST;

        switch (method){
            case SEND_REG_ID:
                return String.format(Locale.US, FORMAT_SEND_REG_ID, hostURL);
            default:
                return null;
        }
    }
}
