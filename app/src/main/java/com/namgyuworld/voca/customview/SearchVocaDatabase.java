package com.namgyuworld.voca.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.namgyuworld.voca.R;
import com.namgyuworld.voca.database.VocaDBOpenHelper;
import com.namgyuworld.voca.model.VocaPOJO;
import com.namgyuworld.voca.util.convert.ConvertDipPixel;

import java.util.ArrayList;
import java.util.List;

/**
 * Search vocabulary in database class that extends AutoCompleteTextView
 * <br><br>
 * Created by Daniel Park on 4/13/15.
 */
public class SearchVocaDatabase extends AutoCompleteTextView{
    public SearchVocaDatabase(Context context) {
        super(context);
        initialize(context);
    }

    public SearchVocaDatabase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public SearchVocaDatabase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchVocaDatabase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context){

        // Set hint message
        setHint(context.getString(R.string.hint_search_voca_in_db));
        // set Text size to 12sp
        setTextSize(ConvertDipPixel.ConvertSPToPixel(context, 6.0f));
        // Set max width
        setMaxWidth(ConvertDipPixel.ConvertDipToPixel(context, 150.0f));
        // Set lines
        setMaxLines(1);

        List<VocaPOJO> vocaList = new VocaDBOpenHelper(context).getAllVocaList();

        List<String> mList = new ArrayList<String>();

        // Collect all vocabularies in List
        for(VocaPOJO i : vocaList){
            mList.add(i.getVocaWord());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, mList);
        setAdapter(adapter);
    }
}
