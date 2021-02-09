package com.codit.interview.aptitude.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.codit.interview.aptitude.model.Question;
import com.google.firebase.crash.FirebaseCrash;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Sreejith on 28-Feb-16.
 */
public class MasterDB extends SQLiteAssetHelper {


    public static final String TAG = "db";


    private static String COL_QUE = "que";
    private static String COL_QNO = "qno";
    private static String COL_OPTION1 = "option1";
    static String COL_OPTION2 = "option2";
    static String COL_OPTION3 = "option3";
    static String COL_OPTION4 = "option4";
    static String COL_ANSWER = "answer";
    static String COL_EXPLANATION = "explanation";
    static String COL_ATTEMPTED = "attempted";
    static String COL_NOTES = "notes";
    static String COL_FAV = "fav";

    private static int DATABASE_VERSION = 2;
    private static String DATABASE_NAME = "master.db";

    Context context;
    private String mUpgradePathFormat="databses" + "/" + "master.db"+"_upgrade_%s-%s.sql";

    public MasterDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;


    }

     InputStream getUpgradeSQLStream(int oldVersion, int newVersion) {

         String path = String.format(mUpgradePathFormat, oldVersion, newVersion);
        try {
            return context.getAssets().open(path);
        } catch (IOException e) {
            Log.w(TAG, "missing database upgrade script: " + path);
            return null;
        }
    }
    void getUpgradeFilePaths(int baseVersion, int start, int end, ArrayList<String> paths) {

        int a;
        int b;

        InputStream is = getUpgradeSQLStream(start, end);
        if (is != null) {
            String path = String.format(mUpgradePathFormat, start, end);
            paths.add(path);
            //Log.d(TAG, "found script: " + path);
            a = start - 1;
            b = start;
            is = null;
        } else {
            a = start - 1;
            b = end;
        }

        if (a < baseVersion) {
            return;
        } else {
            getUpgradeFilePaths(baseVersion, a, b, paths); // recursive call
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onUpgrade(db, oldVersion, newVersion);


        ArrayList<String> paths = new ArrayList<String>();
        getUpgradeFilePaths(oldVersion, newVersion - 1, newVersion, paths);

       /* if (paths.isEmpty()) {
            Log.e(TAG, "no upgrade script path from " + oldVersion + " to " + newVersion);
            throw new SQLiteAssetException("no upgrade script path from " + oldVersion + " to " + newVersion);
        }*/

        // Collections.sort(paths, new VersionComparator());

            try {

                InputStream is = context.getAssets().open("databases/master.db_upgrade_1-2.sql");
                String sql = convertStreamToString(is);
                if (sql != null) {
                    List<String> cmds = splitSqlScript(sql, ';');
                    for (String cmd : cmds) {
                        Log.d("command", "cmd=" + cmd);
                        if (cmd.trim().length() > 0) {
                            db.execSQL(cmd);
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("command", "error------------------------------------------------------------------------------------------: ");
                e.printStackTrace();

                //Log.d("db", "onUpgrade: "+cmd);
            }
        }

        // Log.w(TAG, "Successfully upgraded database " + mName + " from version " + oldVersion + " to " + newVersion);



    public int getLastQueNo(String TABLE_NAME) {

        SQLiteDatabase db = this.getReadableDatabase();
        String select[] = {"(SELECT max(qno) FROM " + TABLE_NAME + " ) AS max"};
        Cursor cursor = db.query(TABLE_NAME, select, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            try {
                int lastQ = cursor.getInt(cursor.getColumnIndex("max"));
                return lastQ;
            } catch (android.database.CursorIndexOutOfBoundsException e) {
                return 0;

            }

        }
        return -1;
    }


    public int updateDB(String TABLE, int qno, String col, String data) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String where = "qno=?";
            String whereArgs[] = {String.valueOf(qno)};


            ContentValues values = new ContentValues();
            values.put(col, data);

            int updateInt = db.update(TABLE, values, where, whereArgs);

            return updateInt;
        } catch (Exception e) {
            return -1;
        }

    }

    public int updateTime(String TABLE, int qno, String time) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String where = "qno=?";
            String whereArgs[] = {String.valueOf(qno)};

            ContentValues values = new ContentValues();
            values.put("time", time);


            int updateInt = db.update(TABLE, values, where, whereArgs);

            return updateInt;
        } catch (Exception e) {
            return 0;
        }
    }

    public ArrayList<String> getTimes(String TABLE) {
        ArrayList<String> times = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();


        String[] cols = {"time", "attempted"};

        Cursor cursor = db.query(TABLE, cols, null, null, null, null, null);


        if (cursor != null) {


            cursor.moveToFirst();
            do {

                String attempted = String.valueOf(cursor.getString(cursor.getColumnIndex("attempted")));
                String time = String.valueOf(cursor.getString(cursor.getColumnIndex("time")));

                if (!attempted.equals("null")) {
                    if (time.equals("null")) {
                        FirebaseCrash.log("time null," + " table=" + TABLE + ", qnp=" + String.valueOf(cursor.getPosition() + 1));
                        FirebaseCrash.report(new Exception("time null"));
                        times.add("0:36");
                    } else {
                        times.add(time);
                    }
                }


            } while (cursor.moveToNext());
        }

        return times;
    }


    //return an object of class Question
    public Question getQuestion(int qno, String TABLE) throws Exception {

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String TABLE_NAME = TABLE;
            String where = "qno=?";
            String whereArgs[] = {String.valueOf(qno)};
            Cursor cursor = db.query(TABLE_NAME, null, where, whereArgs, null, null, null);


            if (cursor != null) {
                Question question = new Question();


                cursor.moveToFirst();
                do {

                    question.setQue(String.valueOf(cursor.getString(cursor.getColumnIndex("que"))));
                    question.setOption1(String.valueOf(cursor.getString(cursor.getColumnIndex("option1"))));
                    question.setOption2(String.valueOf(cursor.getString(cursor.getColumnIndex("option2"))));
                    question.setOption3(String.valueOf(cursor.getString(cursor.getColumnIndex("option3"))));
                    question.setOption4(String.valueOf(cursor.getString(cursor.getColumnIndex("option4"))));
                    question.setQno((cursor.getInt(cursor.getColumnIndex("qno"))));
                    question.setAnswer(String.valueOf(cursor.getString(cursor.getColumnIndex("answer"))));
                    question.setExplanation(String.valueOf(cursor.getString(cursor.getColumnIndex("explanation"))));
                    question.setAttempted(String.valueOf(cursor.getString(cursor.getColumnIndex("attempted"))));
                    question.setNotes(String.valueOf(cursor.getString(cursor.getColumnIndex("notes"))));
                    question.setTime(String.valueOf(cursor.getString(cursor.getColumnIndex("time"))));
                    question.setFav(String.valueOf(cursor.getString(cursor.getColumnIndex("fav"))));


                } while (cursor.moveToNext());


                return question;

            } else {

                throw new Exception("cursor null,qno=" + String.valueOf(qno) + ",table=" + TABLE);
            }


        } catch (Exception e) {

            FirebaseCrash.log("qno=" + String.valueOf(qno) + ",table=" + TABLE);
            throw e;
        }


    }

    public ArrayList<String> getAttemptedList(String TABLE_NAME) {
        ArrayList<String> attemptedList = new ArrayList<String>();

        ArrayList<String> attempted = new ArrayList<String>();
        ArrayList<String> answer = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] cols = {"attempted", "answer"};

        Cursor cursor = db.query(TABLE_NAME, cols, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            do {

                String attemptedDB = String.valueOf(cursor.getString(cursor.getColumnIndex("attempted")));
                String answerDB = String.valueOf(cursor.getString(cursor.getColumnIndex("answer")));

                attempted.add(attemptedDB);
                answer.add(answerDB);


            } while (cursor.moveToNext());

        } else {

        }


        for (int i = 0; i < attempted.size(); i++) {

            if (attempted.get(i).equals("null")) {
                attemptedList.add("NOT");
            } else if (attempted.get(i).equals(answer.get(i))) {

                attemptedList.add("CORRECT");

            } else {
                attemptedList.add("WRONG");

            }


        }


        return attemptedList;
    }

    public boolean addToFavApti(Question question, String currentTable, int currentQno, String favTable) {

        try {
            int prevTotalQue = this.getLastQueNo(favTable);

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COL_QUE, question.getQue());
            values.put(COL_OPTION1, question.getOption1());
            values.put(COL_OPTION2, question.getOption2());
            values.put(COL_OPTION3, question.getOption3());
            values.put(COL_OPTION4, question.getOption4());
            values.put(COL_ANSWER, question.getAnswer());
            values.put(COL_EXPLANATION, question.getExplanation());
            values.put(COL_NOTES, question.getNotes());


            db.insert(favTable, null, values);

            if (this.getLastQueNo(favTable) == prevTotalQue + 1) {
                String where = "qno=?";
                String whereArgs[] = {String.valueOf(currentQno)};

                ContentValues val = new ContentValues();
                val.put("fav", "true");


                db.update(currentTable, val, where, whereArgs);

                return true;
            } else

            {
                FirebaseCrash.report(new Exception("que fav not added, qno=" + String.valueOf(currentQno) + " table=" + currentTable));
                return false;
            }


        } catch (Exception e) {
            FirebaseCrash.report(e);
            return false;
        }


    }


    public  List<String> splitSqlScript(String script, char delim) {
        List<String> statements = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        boolean inLiteral = false;
        char[] content = script.toCharArray();
        for (int i = 0; i < script.length(); i++) {
            if (content[i] == '"') {
                inLiteral = !inLiteral;
            }
            if (content[i] == delim && !inLiteral) {
                if (sb.length() > 0) {
                    statements.add(sb.toString().trim());
                    sb = new StringBuilder();
                }
            } else {
                sb.append(content[i]);
            }
        }
        if (sb.length() > 0) {
            statements.add(sb.toString().trim());
        }
        return statements;
    }

    public  void writeExtractedFileToDisk(InputStream in, OutputStream outs) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer))>0){
            outs.write(buffer, 0, length);
        }
        outs.flush();
        outs.close();
        in.close();
    }

    public  ZipInputStream getFileFromZip(InputStream zipFileStream) throws IOException {
        ZipInputStream zis = new ZipInputStream(zipFileStream);
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            Log.w(TAG, "extracting file: '" + ze.getName() + "'...");
            return zis;
        }
        return null;
    }

    public  String convertStreamToString(InputStream is) {
        return new Scanner(is).useDelimiter("\\A").next();
    }

}



