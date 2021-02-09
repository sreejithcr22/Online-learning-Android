package com.codit.interview.aptitude.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.view.MockRow;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;

/**
 * Created by Sreejith on 11-Sep-16.
 */
public class UserStateDB extends SQLiteOpenHelper {

    public Context context;

    static final int DB_VERSION = 2;
    static final String DB_NAME = "userstate.db";
    static final String TABLE_MOCK_STATE = "mockstate";
    static final String MOCK_COL_TITLE = "title";
    static final String MOCK_COL_SCORE = "score";
    static final String MOCK_COL_IS_FINISHED = "isfinished";
    static final String MOCK_COL_IS_LOCKED = "islocked";

    static final String TABLE_APTI_CATEGS = "apti_categs";
    static final String COL_APTI_CATEGS_CATEG = "category";
    static final String COL_APTI_CATEGS_TIME = "time";
    static final String COL_APTI_CATEGS_PARENT = "parent";
    static final String PARENT_APTI_LOGICAL = "logical";
    static final String PARENT_APTI_QUANTI = "quant";
    static final String PARENT_APTI_VERBAL = "verbal";


    static final String CREATE_TABLE_APTI_CATEGS = "CREATE TABLE " + TABLE_APTI_CATEGS + "(" + COL_APTI_CATEGS_CATEG + " TEXT PRIMARY KEY, " + COL_APTI_CATEGS_TIME + " TEXT, " + COL_APTI_CATEGS_PARENT + " TEXT" + ")";

    static final String CREATE_TABLE = "CREATE TABLE " + TABLE_MOCK_STATE + "(" + MOCK_COL_TITLE + " TEXT PRIMARY KEY, " + MOCK_COL_SCORE + " INTEGER, " + MOCK_COL_IS_FINISHED
            + " TEXT, " + MOCK_COL_IS_LOCKED + " TEXT " + ")";

    public UserStateDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_APTI_CATEGS);

        sqLiteDatabase.beginTransaction();

       try
       {
           for (int k = 1; k <= 10; k++) {

               ContentValues values = new ContentValues();
               values.put(MOCK_COL_TITLE, "Mock Test " + String.valueOf(k));
               values.put(MOCK_COL_SCORE, 0);
               values.put(MOCK_COL_IS_LOCKED, "true");
               values.put(MOCK_COL_IS_FINISHED, "false");

               sqLiteDatabase.insert(TABLE_MOCK_STATE, null, values);


           }

           sqLiteDatabase.setTransactionSuccessful();
       }
       catch (Exception e)
       {
           Toast.makeText(context,"exception",Toast.LENGTH_SHORT).show();
           Log.d("user", "onCreate: "+e.getMessage());
           FirebaseCrash.log("create mock state table error");
           FirebaseCrash.report(e);
       }
       finally {
           sqLiteDatabase.endTransaction();
       }


        initAptiCategs(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.d("user", String.valueOf(i)+";"+String.valueOf(i1));

        if(i==1&&i1==2)
        {
            Log.d("user", "onUpgrade: inside");
            ContentValues values = new ContentValues();
            values.put(COL_APTI_CATEGS_CATEG, "Partnership");

            values.put(COL_APTI_CATEGS_TIME, "02:00");
            values.put(COL_APTI_CATEGS_PARENT, PARENT_APTI_QUANTI);

            sqLiteDatabase.insert(TABLE_APTI_CATEGS, null, values);
        }

    }

    public ArrayList<MockRow> readMockRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOCK_STATE, null, null, null, null, null, null);


        if (cursor != null) {
            ArrayList<MockRow> rows = new ArrayList<MockRow>();



            cursor.moveToFirst();

            do {
                MockRow mockRow = new MockRow();

                mockRow.setMockTitle(cursor.getString(cursor.getColumnIndex(MOCK_COL_TITLE)));
                mockRow.setScore(cursor.getInt(cursor.getColumnIndex(MOCK_COL_SCORE)));
                String isFinished = (cursor.getString(cursor.getColumnIndex(MOCK_COL_IS_FINISHED)));

                if (isFinished.equals("false")) {
                    mockRow.setFinished(false);
                } else if (isFinished.equals("true")) {
                    mockRow.setFinished(true);
                }

                String isLocked = cursor.getString(cursor.getColumnIndex(MOCK_COL_IS_LOCKED));

                if (isLocked.equals("false")) {
                    mockRow.setLocked(false);
                } else if (isLocked.equals("true")) {
                    mockRow.setLocked(false);
                }


                rows.add(mockRow);


            } while (cursor.moveToNext());

            return rows;

        } else {
            return null;
        }

    }


    public void readAptiCategs() {


        SQLiteDatabase db = this.getReadableDatabase();

        String where = COL_APTI_CATEGS_PARENT + "=?";
        String[] whereArgsQuanti = {PARENT_APTI_QUANTI};
        String[] whereArgsLogical = {PARENT_APTI_LOGICAL};
        String[] whereArgsVerbal = {PARENT_APTI_VERBAL};

        Cursor cursor = db.query(TABLE_APTI_CATEGS, null, where, whereArgsQuanti, null, null, null);

        APPSTATE.quanti = new APPSTATE.AptiParentCategs[cursor.getCount()];

        cursor.moveToFirst();


        int i = 0;
        do {
            String category = cursor.getString(cursor.getColumnIndex(COL_APTI_CATEGS_CATEG));
            String time = cursor.getString(cursor.getColumnIndex(COL_APTI_CATEGS_TIME));

            APPSTATE.quanti[i] = new APPSTATE.AptiParentCategs(category, time);
            i++;

        } while (cursor.moveToNext());


        Cursor cursor1 = db.query(TABLE_APTI_CATEGS, null, where, whereArgsLogical, null, null, null);

        APPSTATE.logical = new APPSTATE.AptiParentCategs[cursor1.getCount()];

        cursor1.moveToFirst();


        int j = 0;
        do {
            String category = cursor1.getString(cursor1.getColumnIndex(COL_APTI_CATEGS_CATEG));
            String time = cursor1.getString(cursor1.getColumnIndex(COL_APTI_CATEGS_TIME));

            APPSTATE.logical[j] = new APPSTATE.AptiParentCategs(category, time);
            j++;

        } while (cursor1.moveToNext());





        //verbal

        Cursor cursor2 = db.query(TABLE_APTI_CATEGS, null, where, whereArgsVerbal, null, null, null);

        APPSTATE.verbal = new APPSTATE.AptiParentCategs[cursor2.getCount()];

        cursor2.moveToFirst();


        int k = 0;
        do {
            String category = cursor2.getString(cursor2.getColumnIndex(COL_APTI_CATEGS_CATEG));
            String time = cursor2.getString(cursor2.getColumnIndex(COL_APTI_CATEGS_TIME));

            APPSTATE.verbal[k] = new APPSTATE.AptiParentCategs(category, time);
            k++;

        } while (cursor2.moveToNext());


    }

    public void updateScore(String mockTitle, int score) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String where = MOCK_COL_TITLE + "=?";
            String whereArgs[] = {mockTitle};


            ContentValues values = new ContentValues();
            values.put(MOCK_COL_SCORE, score);
            values.put(MOCK_COL_IS_FINISHED, "true");

             db.update(TABLE_MOCK_STATE, values, where, whereArgs);
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
        }

    }




    public void initAptiCategs(SQLiteDatabase db) {


        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putString("timer_time", "02:00").commit();
        APPSTATE.DEFAULT_TIME = sharedPref.getString("timer_time", "02:00");
        Log.d("apti", "initAptiCategs: ");
        String[] quantiArray = context.getResources().getStringArray(R.array.QuantitativeCategories);
        String[] logicalArray = context.getResources().getStringArray(R.array.LogicalCategories);
        String[] verbalArray=context.getResources().getStringArray(R.array.VerbalAbilityCategories);




        db.beginTransaction();

       try
       {
           for (String category : quantiArray) {
               ContentValues values = new ContentValues();
               values.put(COL_APTI_CATEGS_CATEG, category);
               Log.d("user", "initAptiCategs: "+category);
               values.put(COL_APTI_CATEGS_TIME, APPSTATE.DEFAULT_TIME);
               values.put(COL_APTI_CATEGS_PARENT, PARENT_APTI_QUANTI);

               db.insert(TABLE_APTI_CATEGS, null, values);

           }


           for (String category1 : logicalArray) {
               ContentValues values = new ContentValues();
               values.put(COL_APTI_CATEGS_CATEG, category1);
               values.put(COL_APTI_CATEGS_TIME, APPSTATE.DEFAULT_TIME);
               values.put(COL_APTI_CATEGS_PARENT, PARENT_APTI_LOGICAL);

               db.insert(TABLE_APTI_CATEGS, null, values);


           }


           for (String category2 : verbalArray) {
               ContentValues values = new ContentValues();
               values.put(COL_APTI_CATEGS_CATEG, category2);
               values.put(COL_APTI_CATEGS_TIME, APPSTATE.DEFAULT_TIME);
               values.put(COL_APTI_CATEGS_PARENT, PARENT_APTI_VERBAL);

               db.insert(TABLE_APTI_CATEGS, null, values);


           }

           db.setTransactionSuccessful();
       }
       catch (Exception e)
       {
           FirebaseCrash.log("create apti category tables with tim failed");
           FirebaseCrash.report(e);
       }
       finally {
           db.endTransaction();
       }



    }

    public void updateTime(String category, String time) {

        SQLiteDatabase db = this.getWritableDatabase();

        String where = COL_APTI_CATEGS_CATEG + "=?";
        String whereArgs[] = {category};
        ContentValues values = new ContentValues();
        values.put(COL_APTI_CATEGS_TIME, time);
        int count = db.update(TABLE_APTI_CATEGS, values, where, whereArgs);


        if(count!=1)
        {
            FirebaseCrash.report(new Exception("time update error, "+count+" rows updated for category="+category+" and time ="+time));
        }


    }

    public void changeDefaultTime(String oldTime, String newTime) {
        SQLiteDatabase db = this.getWritableDatabase();


        for (int i = 0; i < APPSTATE.logical.length; i++) {

            if (APPSTATE.logical[i].getTime().equals(oldTime)) {
                APPSTATE.logical[i].setTime(newTime);

            }
        }

        for (int i = 0; i < APPSTATE.quanti.length; i++) {

            if (APPSTATE.quanti[i].getTime().equals(oldTime)) {
                APPSTATE.quanti[i].setTime(newTime);

            }
        }


        db.beginTransaction();

        try
        {
            for (int i = 0; i < APPSTATE.logical.length; i++) {

                String where = COL_APTI_CATEGS_CATEG + "=?";
                String whereArgs[] = {APPSTATE.logical[i].getCategory()};
                ContentValues values = new ContentValues();
                values.put(COL_APTI_CATEGS_TIME, APPSTATE.logical[i].getTime());
                db.update(TABLE_APTI_CATEGS, values, where, whereArgs);
            }

            for (int i = 0; i < APPSTATE.quanti.length; i++) {

                String where = COL_APTI_CATEGS_CATEG + "=?";
                String whereArgs[] = {APPSTATE.quanti[i].getCategory()};
                ContentValues values = new ContentValues();
                values.put(COL_APTI_CATEGS_TIME, APPSTATE.quanti[i].getTime());
                db.update(TABLE_APTI_CATEGS, values, where, whereArgs);


            }
            db.setTransactionSuccessful();

        }
        catch (Exception e)
        {
            Toast.makeText(context,"Sorry, Something went wrong",Toast.LENGTH_SHORT).show();
            FirebaseCrash.log("change default time method - time="+newTime);
            FirebaseCrash.report(e);
        }
        finally {
            db.endTransaction();
        }

    }
}



