package com.codit.interview.aptitude;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Sreejith on 08-Jul-16.
 */
public class InterviewDB extends SQLiteAssetHelper {

    private static int DATABASE_VERSION=1;
    private static String DATABASE_NAME="interview.db";
    static String COL_TIP_NO="tipno";
    static String COL_TIP="tip";
    static String COL_TITLE="title";
    Context context;

    public InterviewDB(Context context)
    {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }


    public Tip getTip(int tipNo,String TABLE)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String where="tipno=?";
        String whereArgs[]={String.valueOf(tipNo)};
        Cursor cursor= db.query(TABLE, null, where, whereArgs, null, null, null);

        if(cursor!=null)
        {
            Tip tip=new Tip();



               cursor.moveToFirst();
               do {

                   tip.setTipNo((cursor.getInt(cursor.getColumnIndex("tipno"))));
                   tip.setTitle(String.valueOf(cursor.getString(cursor.getColumnIndex("title"))));
                   tip.setTip(String.valueOf(cursor.getString(cursor.getColumnIndex("tip"))));
                   tip.setFav(String.valueOf(cursor.getString(cursor.getColumnIndex("fav"))));


               }while(cursor.moveToNext());



            return tip;
        }
        else
        {
            return null;
        }

    }

    public Tip getTip(String title,String TABLE)
    {

        SQLiteDatabase db=this.getReadableDatabase();
        String where=COL_TITLE+"=?";
        String whereArgs[]={title};
        Cursor cursor= db.query(TABLE, null, where, whereArgs, null, null, null);

        if(cursor!=null)
        {
            Tip tip=new Tip();


            try {

                cursor.moveToFirst();
                do {

                    tip.setTipNo((cursor.getInt(cursor.getColumnIndex("tipno"))));
                    tip.setTitle(String.valueOf(cursor.getString(cursor.getColumnIndex("title"))));
                    tip.setTip(String.valueOf(cursor.getString(cursor.getColumnIndex("tip"))));
                    tip.setFav(String.valueOf(cursor.getString(cursor.getColumnIndex("fav"))));


                } while (cursor.moveToNext());


            }
            catch(CursorIndexOutOfBoundsException e)
            {
                return null;
            }
            return tip;
        }
        else
        {
            Toast.makeText(context,"No record found",Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    public boolean addFav(Tip tip,String currentTable,String favTable)
    {

       try
       {
           int prevTotalQue=this.getLastTipNo(favTable);

           SQLiteDatabase db=this.getWritableDatabase();

           ContentValues values=new ContentValues();
           values.put(COL_TIP,tip.getTip());
           values.put(COL_TITLE,tip.getTitle());

           db.insert(favTable,null,values);


           if(this.getLastTipNo(favTable)==prevTotalQue+1)
           {

               String where = COL_TIP_NO+"=?";
               String whereArgs[]={String.valueOf(tip.getTipNo())};

               ContentValues val=new ContentValues();
               val.put("fav","true");


               int updateInt= db.update(currentTable, val, where, whereArgs);

               if(updateInt!=1)
               {

                   FirebaseCrash.report(new Exception("0 rows updated, table="+currentTable+"tip no="+String.valueOf(tip.getTipNo())));
               }

               return true;
           }
           else
           {
               FirebaseCrash.log("0 rows updated, table="+currentTable+"tip no="+String.valueOf(tip.getTipNo()));
               FirebaseCrash.report(new Exception("add to fav error,getLastTipNo not equal to prevTotalQue+1"));
               return false;
           }

       }
       catch (Exception e)
       {

           e.printStackTrace();
           FirebaseCrash.log("0 rows updated, table="+currentTable+"tip no="+String.valueOf(tip.getTipNo()));
           FirebaseCrash.report(e);

           return false;
       }

    }

    public int getLastTipNo(String TABLE_NAME)
    {


        SQLiteDatabase db=this.getReadableDatabase();
        String select[]={ "(SELECT max(tipno) FROM "+TABLE_NAME+" ) AS max" };
        Cursor cursor= db.query(TABLE_NAME,select,null,null,null,null,null);

        if (cursor!=null)
        {
            cursor.moveToFirst();

            try
            {
                 int lastQ = cursor.getInt(cursor.getColumnIndex("max"));
                return lastQ;
            }
            catch(android.database.CursorIndexOutOfBoundsException e)
            {
                return 0;
            }

        }
        return -1;
    }


}
