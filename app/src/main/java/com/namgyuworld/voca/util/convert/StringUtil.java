package com.namgyuworld.voca.util.convert;


/**
 * If String is empty or null, {@link StringUtil} converts String to ""
 * <br><br>
 * Created by Park Namgyu on 4/1/15.
 */
public class StringUtil {

    /**
     * Set Null to "" if str is null.
     * @param str string
     * @return
     */
    public static String setNullToEmpty(String str){
        if(str == null){
            return "";
        }
        return str;
    }

    /**
     * Check if <b>str</b> is null or ""
     * @param str
     * @return <code>true</code> : str is null or ""
     */
    public static boolean isNullorEmpty(String str){
        if(str == null || str.isEmpty()) {
            return true;
        }
        else {
            // Not enough. Make sure to check "    "
            if(str.trim().isEmpty()){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if <b>str</b> is null
     * @param str
     * @return <code>true</code> : str is null
     */
    public static boolean isNull(String str){
        if(str == null){
            return true;
        }
        return false;
    }


}
