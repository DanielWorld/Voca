package com.namgyuworld.voca.util.convert;

/**
 * Created by Daniel Park on 4/20/15.
 */
public class ZeroUtil {

    /**
     * Convert minus number to 0
     * @param input
     * @return
     */
    public static int convertMinusToZero(int input){

        if(input < 0){
            return 0;
        }
        return input;
    }
}
