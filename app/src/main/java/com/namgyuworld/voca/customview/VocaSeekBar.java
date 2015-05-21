package com.namgyuworld.voca.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.namgyuworld.voca.R;
import com.namgyuworld.voca.util.convert.BitmapUtil;

/**
 * Created by Daniel Park on 4/20/15.
 */
public class VocaSeekBar extends SeekBar{
    public VocaSeekBar(Context context) {
        super(context);
        init(context);
    }

    public VocaSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VocaSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VocaSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context ctx){
        Resources res = ctx.getResources();

        Bitmap bitmap = BitmapUtil.decodeBitmapFromResouce(res, R.drawable.ic_voca, 45, 45);
        setThumb(new BitmapDrawable(res, bitmap));
        setProgressDrawable(res.getDrawable(R.drawable.seekbar));

    }
}
