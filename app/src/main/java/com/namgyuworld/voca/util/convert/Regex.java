package com.namgyuworld.voca.util.convert;

import com.namgyuworld.utility.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel Park on 4/13/15.
 */
public class Regex {

    private static final String TAG = Regex.class.getSimpleName();
    private static Logger LOG = Logger.getInstance();

    /**
     *
     * ^ : Start of the line <br>
     * [a-z0-9_-] : Match characters and symbols in the list, a-z, 0-9, underscore, hyphen <br>
     * {3,15} : Length at least 3 characters and maximum length of 15 <br>
     * $ : End of the line
     *
     */
    private final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";

    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    /**
     *
     * ( : Start of group <br>
     * (?=.*\d) : must contains one digit from 0-9 <br>
     * (?=.*[a-z]) : must contains one lowercase characters <br>
     * (?=.*[A-Z]) : must contains one uppercase characters <br>
     * (?=.*[@#$%]) : must contains one special symbols in the list "@#$%" <br>
     * . : match anything with previous condition checking <br>
     * {6,20} : length at least 6 characters and maximum of 20 <br>
     * ) : End of group
     *
     */
    private final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,12})";

    /**
     * Convert input String to alphabets only. [a-zA-Z] <br>
     *      e.g) "03de8283ffe&*&" -> "deffe"
     * @param word input String
     * @return
     */
    public String convertToOnlyAlphabets(String word){
        LOG.i(TAG, "Previous word : " + word);

        String changedWord = "";

        LOG.i(TAG, "Online word : " + word);
        // Only alphabet characters are accepted..
        String[] a = word.split("");
        List<String> newVoca = new ArrayList<String>();
        for(String str : a){
            if(Pattern.matches("[a-zA-z]+", str)){
                newVoca.add(str);
                LOG.i(TAG, "patterning string : " + str);
            }
        }
        // List to String
        for(String s : newVoca){
            changedWord += s;
        }

        LOG.i(TAG, "Changed word :" + word);
        return changedWord;
    }

    /**
     * Check if this password is right for our regulation.
     *
     * @param input
     * @return
     */
    public boolean IsPasswordCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }
}
