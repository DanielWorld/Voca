package com.namgyuworld.voca.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.namgyuworld.voca.activity.MainActivity;
import com.namgyuworld.voca.model.VocaPOJO;
import com.namgyuworld.voca.util.Consts;
import com.namgyuworld.voca.util.convert.StringUtil;
import com.namgyuworld.voca.util.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DanielPark on 2014-12-13.
 */
public class VocaDBOpenHelper extends SQLiteOpenHelper {

    private final String TAG = VocaDBOpenHelper.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    public static final String DB_NAME = "voca.db";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "voca";

    private final String COLUMN_NAME_ID = "_id";
    private final String COLUMN_NAME_VOCA = "_voca";
    private final String COLUMN_NAME_CONTENTS = "_voca_contents";

    private final String[] COLUMN_NAMES = {
            COLUMN_NAME_VOCA, COLUMN_NAME_CONTENTS

    };

    // DEFAULT DATABASE
    public VocaDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TABLE + " ( "
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME_VOCA + " VARCHAR(25) NOT NULL, "
                + COLUMN_NAME_CONTENTS + " VARCHAR(1000) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db); // RECREATE NEW VERSION DB
    }

    /**
     * Get position of voca that you're searching for.
     * @param word
     * @return
     */
    public String getPositionFromVoca(String word){
        SQLiteDatabase db = null;
        Cursor c = null;

        try{
            db = getReadableDatabase();
            c = db.query(DB_TABLE, COLUMN_NAMES, null, null, null, null, null);

            if(!c.moveToFirst()){
                throw new Exception("No column data in Database.");
            }

            for(int i =0; i < c.getCount(); i++) {
                if (c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_VOCA)).equals(word)) {
                    // I've found one
                    // then return current position
                    return String.valueOf(c.getPosition());
                }
                c.moveToNext();
            }
        }catch (Exception e){
            LOG.e(e.getMessage());
        }finally {
            if(c != null)
                c.close();
            if(db != null)
                db.close();
        }

        return null;
    }

    /**
     * Get all Vocabularies
     */
    public List getAllVocaList() {
        SQLiteDatabase db = null;
        Cursor c = null;
        List<VocaPOJO> mList = new ArrayList<VocaPOJO>();

        try {
            db = getWritableDatabase();
            c = db.query(DB_TABLE, COLUMN_NAMES, null, null, null, null, null);

//            if(c.getCount() == 0) // Returns the numbers of rows in the cursor.
//                return mList;

            if (!c.moveToFirst())
                return mList;

//            LOG.i("the numbers of rows :" + c.getCount());
//            LOG.i("dd" + c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_VOCA)));
            for (int i = 0; i < c.getCount(); i++) {
                mList.add(new VocaPOJO(c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_VOCA)), c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_CONTENTS))));
                c.moveToNext();
            }
        } catch (Exception e) {
            LOG.e(e.getMessage());
        } finally {
            if (c != null)
                c.close();
            if (db != null)
                db.close();

            return mList;
        }
    }

    /**
     * Save vocabulary <br>
     * Make sure to avoid the same vocabulary, but change the previous to the new one.
     *
     * @param word     English vocabulary
     * @param contents word's contents
     */
    public void saveVoca(String word, String contents) {
        if (StringUtil.isNullorEmpty(word) || StringUtil.isNullorEmpty(contents))
            return;

        LOG.i("Before saving voca... " + "\nWord:" + word + "\nContents:" + contents);
        SQLiteDatabase db = null;
        ContentValues values = null;
        Cursor c = null;
        try {
            db = getWritableDatabase();

            values = new ContentValues();
            values.put(COLUMN_NAMES[0], word); // Voca
            values.put(COLUMN_NAMES[1], contents); // Voca's contents

            c = db.query(DB_TABLE, COLUMN_NAMES, null, null, null, null, null);
            if (!c.moveToFirst()) {
                LOG.i("Can't move To First.. I don't know why.. maybe there was no data in DB at all.");
                if (c.getCount() != 0) {
                    LOG.i("Don't know what happened.. check your source code!!!");
                    return;
                } else {
                    LOG.i("It turns out that no rows in DB. No problem, just keep going..");
                }

            }

            for (int i = 0; i < c.getCount(); i++) {
                LOG.i("search word[" + i + "] : " + c.getString(c.getColumnIndexOrThrow(COLUMN_NAMES[0])));
                LOG.i("what i am looking for : " + word);
                if (c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_VOCA)).equals(word)) {
                    // the same vocabulary already exist
                    // just update vocabulary
                    db.update(DB_TABLE, values, COLUMN_NAMES[0] + "= ?", new String[]{c.getString(c.getColumnIndexOrThrow(COLUMN_NAMES[0]))});
                    LOG.i("This '" + word + "' already exists. Just update contents of that same word.");
                    return;
                }
                c.moveToNext();
            }
            // No same word was found. Add it to Database.
            LOG.i("db.insert");
            db.insert(DB_TABLE, null, values);


        } catch (Exception e) {
            LOG.e(e.getMessage());
        } finally {
            if (values != null)
                values.clear();
            if (c != null)
                c.close();
            if (db != null)
                db.close();
        }
    }


    /**
     * Update voca
     *
     * @param c        the position of word
     * @param word     English vocabulary
     * @param contents word's meaning
     */
    public void editVoca(Cursor c, String word, String contents) {
        SQLiteDatabase db = null;
        ContentValues values = null;
        try {
            db = getWritableDatabase();
            values = new ContentValues();
            values.put(COLUMN_NAMES[0], word); // Voca
            values.put(COLUMN_NAMES[1], contents); // noun meaning

            db.update(DB_TABLE, values, COLUMN_NAMES[0] + "= ?", new String[]{c.getString(c.getColumnIndexOrThrow(COLUMN_NAMES[0]))});

        } catch (Exception e) {
            LOG.e(e.getMessage());
        } finally {
            if (values != null)
                values.clear();
            if (c != null)
                c.close();
            if (db != null)
                db.close();
        }
    }

    /**
     * Remove vocabulary by word
     *
     * @param word
     */
    public void removeVoca(String word) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.delete(DB_TABLE, COLUMN_NAMES[0] + "= ?", new String[]{word});
        } catch (Exception e) {
            LOG.e(e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
    }

    /**
     * Remove vocabulary by position
     *
     * @param position
     */
    public void removeVoca(int position) {
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getWritableDatabase();
            c = db.query(DB_TABLE, COLUMN_NAMES, null, null, null, null, null);

            if (c.moveToPosition(position)) {
                LOG.i("Delete this " + c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_VOCA)));
                db.delete(DB_TABLE, COLUMN_NAMES[0] + "= ?", new String[]{c.getString(c.getColumnIndexOrThrow(COLUMN_NAMES[0]))});
                // Refresh Main page
                MainActivity.mHanlder.sendEmptyMessage(Consts.REFRESH_VOCA);
            } else {
                LOG.e("Sorry, Failed to find a word that needs to be deleted.");
            }

        } catch (Exception e) {
            LOG.e(e.getMessage());
        } finally {
            if (c != null)
                c.close();
            if (db != null)
                db.close();
        }
    }
}
