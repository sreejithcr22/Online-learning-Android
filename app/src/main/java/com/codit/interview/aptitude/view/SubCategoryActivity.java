package com.codit.interview.aptitude.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.model.ParentCategory;
import com.codit.interview.aptitude.util.APPSTATE;

public class SubCategoryActivity extends NavActivityBase implements SubCategoryFragment.SubCategoryInterface,SubCategoryAdapter.AdapterInterface{

    String currentSubCategory;
    int minutes,seconds;
    String timerChangedCaregory;

    public  SubCategoryActivity()
    {
        this.currentActivity=this.OTHER_ACTIVITY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        initialize(R.layout.content_sub_category);



        timerChangedCaregory=null;



        Intent intent=getIntent();
        currentSubCategory= APPSTATE.CURRENT_QUE_SUB_CATEGORY;


        if(APPSTATE.OPEN_FROM_TIMER)
        {
            APPSTATE.OPEN_FROM_TIMER=false;
            minutes=intent.getIntExtra("minutes",0);
            seconds=intent.getIntExtra("seconds",0);
            timerChangedCaregory=APPSTATE.TIMER_CHANGED_CATEGORY;


        }

        getSupportActionBar().setTitle(currentSubCategory);

    }




    public String getMainCategory() {

        if(currentSubCategory!=null)
        {
            return currentSubCategory;
        }
        else
        {
            return null;
        }
    }

    @Override
    public String getTimerChangedCategory() {
        return timerChangedCaregory;
    }

    @Override
    public int getChangedMinutes() {
        return minutes;
    }

    @Override
    public int getChangedSeconds() {
        return seconds;

    }



    @Override
    public void goToQuestion(View v, String category, int min, int sec) {

        APPSTATE.CURRENT_ACTIVITY_TITLE=category;

        Intent intent=new Intent(getApplicationContext(),QuestionActivity.class);

        APPSTATE.CURRENT_MIN=min;
        APPSTATE.CURRENT_SEC=sec;

        APPSTATE.CURRENT_CATEGORY=category;

        switch (category)
        {
            case "Problems On Age":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_APTI_AGE;break;
            case "Time And Work":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_TIME_AND_WORK; break;
            case "Time And Distance":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_TIME_AND_DISTANCE;break;
            case "Average":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_AVG;break;
            case "Profit And Loss":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_PROFIT;break;
            case "Interest":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_INTEREST;break;
            case "Permutation And Combination":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_PERMUTATION;break;
            case "Partnership":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_PARTNERSHIP;break;
            case "Numbers And Decimal Fractions":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_APTI_NUMBERS;break;
            case "HCF And LCM":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_HCF_AND_LCM; break;
            case "Ratio And Proportion":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_RATIO;break;
            case "Boats And Streams":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_BOAT;break;
            case "Alligation or Mixture":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_ALLIGATION;break;
            case "Pipes and Cistern":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_PIPES;break;
            case "Percentage":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_PERCENTAGE;break;
            case "Problems On Trains":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_TRAINS;break;


            case "Analogy":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_ANALOGY;break;
            case "Series":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_SERIES;break;
            case "Seating Arrangement":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_SEAT;break;
            case "Calendar And Clock":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_CALENDAR;break;
            case "Blood Relation":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_BLOOD;break;
            case "Essential Part":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_ESSENTIAL;break;
            case "Direction Sense":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_DIRECTION;break;
            case "Classification":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_CLASSIFICATION;break;
            case "Arithmetic Reasoning":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_ARITHMETIC;break;
            case "Verification of Truth":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_VERIFICATION;break;
            case "Statements and Conclusions":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_STATEMENT_CONCLUSION;break;
            case "Statements and Arguments":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_STATEMENT_ARGUEMENT;break;
            case "Statements and Assumptions":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_STATEMENT_ASSUMPTION;break;
            case "Coding and Decoding":APPSTATE.CURRENT_TABLE=APPSTATE.TABLE_DECODE;break;


            case "Antonyms":APPSTATE.CURRENT_TABLE=APPSTATE.ANTONYMS;break;
            case "Idioms and Phrases":APPSTATE.CURRENT_TABLE=APPSTATE.IDIOMS;break;
            case "Prepositions":APPSTATE.CURRENT_TABLE=APPSTATE.PREPOSITIONS;break;
            case "Synonyms":APPSTATE.CURRENT_TABLE=APPSTATE.SYNONYM;break;
            case "Spelling":APPSTATE.CURRENT_TABLE=APPSTATE.SPELL;break;
            case "One Word Substitution":APPSTATE.CURRENT_TABLE=APPSTATE.SUBSTI;break;
            case "Closet Test":APPSTATE.CURRENT_TABLE=APPSTATE.CLOSET;break;
            case "Paragraph Formation":APPSTATE.CURRENT_TABLE=APPSTATE.PARA;break;
            case "Sentence Arrangement":APPSTATE.CURRENT_TABLE=APPSTATE.ARRANGE;break;
            case "Sentence Completion":APPSTATE.CURRENT_TABLE=APPSTATE.COMPLETE;break;


            default:APPSTATE.CURRENT_TABLE="null table";break;
        }

        startActivity(intent);
    }

    @Override
    public void showTimer(final int minutes, final int seconds, String currentCategory) {





setupTimerDialog(minutes,seconds,currentCategory);







    }

    public void setupTimerDialog(int minutes, int seconds, final String current)
    {




        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View titleView=getLayoutInflater().inflate(R.layout.dialog_title,null);
        TextView title=(TextView)titleView.findViewById(R.id.dialogTitle);
        ImageView icon=(ImageView)titleView.findViewById(R.id.dialogIcon);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_stopwatch));
        title.setText("Timer");
        builder.setCustomTitle(titleView);


        LayoutInflater inflater = getLayoutInflater();

        //notes body
        View view = inflater.inflate(R.layout.timer_body, null);

        final NumberPicker minutesPicker=(NumberPicker) view.findViewById(R.id.minutesPicker);


        minutesPicker.setMinValue(1);
        minutesPicker.setMaxValue(15);
        minutesPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d",i);
            }
        });
        minutesPicker.setValue(minutes);
        minutesPicker.setSelected(false);

        minutesPicker.setActivated(false);

        final NumberPicker secondsPicker=(NumberPicker)view.findViewById(R.id.secondsPicker);
        secondsPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d",i);
            }
        });
        secondsPicker.setMinValue(00);
        secondsPicker.setMaxValue(59);

        secondsPicker.setValue(seconds);
        builder.setView(view);
        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                APPSTATE.TIMER_CHANGED_CATEGORY=current;
                sendTime(minutesPicker,secondsPicker);

            }
        });
        builder.setNegativeButton("CANCEL",null)
                .setNeutralButton("DEFAULT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        APPSTATE.TIMER_CHANGED_CATEGORY=current;
                        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                        String time=sharedPref.getString("timer_time","02:00");
                        String[] times=time.split(":");
                        final int min=Integer.parseInt(times[0]);
                        final int sec=Integer.parseInt(times[1]);


                        minutesPicker.setValue(min);
                        secondsPicker.setValue(sec);
                        sendTime(minutesPicker,secondsPicker);

                    }
                });

        builder.create().show();






    }

    public void sendTime(NumberPicker minutesPicker,NumberPicker secondsPicker)
    {
        APPSTATE.OPEN_FROM_TIMER=true;
        int newMinutes=minutesPicker.getValue();
        int newSeconds=secondsPicker.getValue();

        Intent intent=new Intent(getApplicationContext(),SubCategoryActivity.class);
        intent.putExtra("minutes",newMinutes);
        intent.putExtra("seconds",newSeconds);


        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(), ParentCategory.class);
        startActivity(intent);
        APPSTATE.BACK_FLAG=true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id)
        {
            case R.id.settings: drawer.openDrawer(settingsNav);
                return true;


        }


        return super.onOptionsItemSelected(item);
    }



}
