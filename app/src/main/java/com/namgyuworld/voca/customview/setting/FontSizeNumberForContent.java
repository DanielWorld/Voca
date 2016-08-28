package com.namgyuworld.voca.customview.setting;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namgyuworld.voca.util.SharedPrefUtil;
import com.namgyuworld.voca.util.logger.Logger;

/**
 * Created by Daniel Park on 4/14/15.
 */
public class FontSizeNumberForContent extends HorizontalScrollView{

    private final String TAG = FontSizeNumber.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    private SharedPrefUtil mPref;

    LinearLayout subContainer;
    TextView[] i = new TextView[15];

    int[] positionIndex = {21, 41, 69, 108, 142, 183, 207, 243, 273, 327, 391, 420, 470};

    public FontSizeNumberForContent(Context context) {
        super(context);
        initialize(context);
    }

    public FontSizeNumberForContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FontSizeNumberForContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FontSizeNumberForContent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context ctx){

        setBackgroundColor(Color.YELLOW);

        mPref = new SharedPrefUtil(ctx);
        int savedFontSize = mPref.getVocaFontContentSize();

        subContainer = new LinearLayout(ctx);
        for(int j=0; j< i.length; j++){
            i[j] = new TextView(ctx);
            i[j].setText(" " + String.valueOf(j + 10) +" ");


            if(j == (savedFontSize - 10)){
                i[j].setTypeface(null, Typeface.BOLD);
                i[j].setTextSize(j + 10);
            }

//            if(j==1){
//
//            }
            else{
                i[j].setTypeface(null, Typeface.NORMAL);
                i[j].setTextSize(j + 10);
            }

            subContainer.addView(i[j]);
        }
        addView(subContainer);

//        scrollTo(fontDefaultIndex * (savedFontSize - 10), 0);

        if(Build.VERSION.SDK_INT >= 9) {
            // Disabled scrollbar effects
            setOverScrollMode(OVER_SCROLL_NEVER);
        }

        // Hide scrollbars
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);

        // Scroll to current font size position
        final int currentFontSizeIndex = mPref.getVocaFontContentSize() - 10;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    scrollTo(positionIndex[currentFontSizeIndex - 1], 0);
                }catch (Exception e){
                    LOG.e(e.getMessage());
                    scrollTo(0, 0);
                }

            }
        },500);


    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LOG.i("Current X coordination : " + l );

        // Change font size whenever you scroll numbers...

        if(l >= 0 && l < positionIndex[0]) {
            rearrangeTextViewSize(0);
        }
        else if(l >= positionIndex[0] && l < positionIndex[1]){
            rearrangeTextViewSize(1);
        }
        else if(l >= positionIndex[1] && l < positionIndex[2]){
            rearrangeTextViewSize(2);
        }
        else if(l >= positionIndex[2] && l < positionIndex[3]){
            rearrangeTextViewSize(3);
        }
        else if(l >= positionIndex[3] && l < positionIndex[4]){
            rearrangeTextViewSize(4);
        }
        else if(l >= positionIndex[4] && l < positionIndex[5]){
            rearrangeTextViewSize(5);
        }
        else if(l >= positionIndex[5] && l < positionIndex[6]){
            rearrangeTextViewSize(6);
        }
        else if(l >= positionIndex[6] && l < positionIndex[7]){
            rearrangeTextViewSize(7);
        }
        else if(l >= positionIndex[7] && l < positionIndex[8]){
            rearrangeTextViewSize(8);
        }
        else if(l >= positionIndex[8] && l < positionIndex[9]){
            rearrangeTextViewSize(9);
        }
        else if(l >= positionIndex[9] && l < positionIndex[10]){
            rearrangeTextViewSize(10);
        }
        else if(l >= positionIndex[10] && l < positionIndex[11]){
            rearrangeTextViewSize(11);
        }
        else if(l >= positionIndex[11] && l < positionIndex[12]){
            rearrangeTextViewSize(12);
        }
        else{
            rearrangeTextViewSize(13);
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * Rearrange textview size in font size scroll
     * @param index
     */
    private void rearrangeTextViewSize(int index){
        for(int j=0; j < i.length; j++){
            if(j == index) {
                i[j].setTypeface(null, Typeface.BOLD);

                mPref.setVocaFontContentSize(j + 10);
            }else{
                i[j].setTypeface(null, Typeface.NORMAL);
            }
        }
    }
}
