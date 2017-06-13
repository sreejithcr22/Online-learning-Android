package com.codit.interview.aptitude;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

/**
 * Created by Sreejith on 28-Feb-16.
 */
public class MasterDB extends SQLiteAssetHelper {




    private static String COL_QUE="que";
    private static String COL_QNO="qno";
    private static String COL_OPTION1="option1";
    static String COL_OPTION2="option2";
    static String COL_OPTION3="option3";
    static String COL_OPTION4="option4";
    static String COL_ANSWER="answer";
    static String COL_EXPLANATION="explanation";
    static String COL_ATTEMPTED="attempted";
    static String COL_NOTES="notes";
    static String COL_FAV="fav";

    private static int DATABASE_VERSION=1;
    private static String DATABASE_NAME="master.db";

    Context context;

    public MasterDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;


    }

    public int getLastQueNo(String TABLE_NAME)
    {

        SQLiteDatabase db=this.getReadableDatabase();
       String select[]={ "(SELECT max(qno) FROM "+TABLE_NAME+" ) AS max" };
        Cursor cursor= db.query(TABLE_NAME,select,null,null,null,null,null);

   if (cursor!=null)
   {
       cursor.moveToFirst();

       try {
           int lastQ=cursor.getInt(cursor.getColumnIndex("max"));
           return lastQ;
       }
       catch( android.database.CursorIndexOutOfBoundsException e)
       {
           return 0;

       }

   }
       return -1;
    }


    public int updateDB(String TABLE,int qno,String col,String data)
    {
        try
        {
            SQLiteDatabase db=this.getWritableDatabase();
            String where = "qno=?";
            String whereArgs[]={String.valueOf(qno)};


            ContentValues values=new ContentValues();
            values.put(col,data);

            int updateInt= db.update(TABLE, values, where, whereArgs);

            return updateInt;
        }
        catch (Exception e)
        {
            return -1;
        }

    }

    public int updateTime(String TABLE,int qno,String time)
    {
       try
       {
           SQLiteDatabase db=this.getWritableDatabase();

           String where = "qno=?";
           String whereArgs[]={String.valueOf(qno)};

           ContentValues values=new ContentValues();
           values.put("time",time);


           int updateInt= db.update(TABLE, values, where, whereArgs);

           return updateInt;
       }
       catch (Exception e)
       {
           return 0;
       }
    }

    public ArrayList<String> getTimes(String TABLE)
    {
        ArrayList<String> times=new ArrayList<String>();

        SQLiteDatabase db=this.getReadableDatabase();




        String[] cols={"time","attempted"};

        Cursor cursor= db.query(TABLE, cols, null, null, null, null, null);


        if (cursor!=null) {


            cursor.moveToFirst();
            do {

                String attempted=String.valueOf(cursor.getString(cursor.getColumnIndex("attempted")));
                String time=String.valueOf(cursor.getString(cursor.getColumnIndex("time")));

                if(!attempted.equals("null"))
                {
                    if(time.equals("null"))
                    {
                        FirebaseCrash.log("time null,"+" table="+TABLE+", qnp="+String.valueOf(cursor.getPosition()+1));
                        FirebaseCrash.report(new Exception("time null"));
                        times.add("0:36");
                    }
                    else {
                        times.add(time);
                    }
                }



            } while (cursor.moveToNext());
        }

        return times;
    }


    //return an object of class Question
    public Question getQuestion(int qno,String TABLE) throws Exception {

     try
     {
         SQLiteDatabase db=this.getReadableDatabase();

         String TABLE_NAME=TABLE;
         String where="qno=?";
         String whereArgs[]={String.valueOf(qno)};
         Cursor cursor= db.query(TABLE_NAME, null, where, whereArgs, null, null, null);


         if (cursor!=null)
         {
             Question question= new Question();


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




             }while(cursor.moveToNext());



             return question;

         }
         else
         {

             throw new Exception("cursor null,qno="+String.valueOf(qno)+",table="+TABLE);
         }


     }
     catch (Exception e)
     {

         FirebaseCrash.log("qno="+String.valueOf(qno)+",table="+TABLE);
         throw e;
     }


    }

    public ArrayList<String> getAttemptedList(String TABLE_NAME)
    {
        ArrayList<String> attemptedList=new ArrayList<String>();

        ArrayList<String> attempted=new ArrayList<String>();
        ArrayList<String> answer=new ArrayList<String>();

        SQLiteDatabase db=this.getReadableDatabase();

        String[] cols={"attempted","answer"};

        Cursor cursor= db.query(TABLE_NAME, cols, null, null, null, null, null);

        if(cursor!=null)
        {
            cursor.moveToFirst();

            do {

               String attemptedDB= String.valueOf(cursor.getString(cursor.getColumnIndex("attempted")));
                String answerDB=String.valueOf(cursor.getString(cursor.getColumnIndex("answer")));

                attempted.add(attemptedDB);
                answer.add(answerDB);


            }while(cursor.moveToNext());

        }
        else
        {

        }


        for (int i=0;i<attempted.size();i++)
        {

            if(attempted.get(i).equals("null"))
            {
                attemptedList.add("NOT");
            }

                else if(attempted.get(i).equals(answer.get(i)))
            {

                attemptedList.add("CORRECT");

            }
            else
            {
                attemptedList.add("WRONG");

            }


        }



        return  attemptedList;
    }

    public boolean addToFavApti(Question question,String currentTable,int currentQno,String favTable)
    {

        try {
            int prevTotalQue=this.getLastQueNo(favTable);

            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues values=new ContentValues();

            values.put(COL_QUE,question.getQue());
            values.put(COL_OPTION1,question.getOption1());
            values.put(COL_OPTION2,question.getOption2());
            values.put(COL_OPTION3,question.getOption3());
            values.put(COL_OPTION4,question.getOption4());
            values.put(COL_ANSWER,question.getAnswer());
            values.put(COL_EXPLANATION,question.getExplanation());
            values.put(COL_NOTES,question.getNotes());


            db.insert(favTable,null,values);

            if(this.getLastQueNo(favTable)==prevTotalQue+1)
            {
                String where = "qno=?";
                String whereArgs[]={String.valueOf(currentQno)};

                ContentValues val=new ContentValues();
                val.put("fav","true");


                 db.update(currentTable, val, where, whereArgs);

                return true;
            }
            else

            {
                FirebaseCrash.report(new Exception("que fav not added, qno="+String.valueOf(currentQno)+" table="+currentTable));
                return false;
            }


        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
            return false;
        }


    }





}

