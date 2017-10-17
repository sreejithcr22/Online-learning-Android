package com.codit.interview.aptitude;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import java.util.HashMap;

import static android.view.View.GONE;

/**
 * Created by Sreejith on 12-Aug-16.
 */
public class GKQuestionActivity extends NavActivityBase implements QuestionFragBase.QueFragInterface,ExpFragment.ExpFragInterface,InterviewGeneral.interviewInterface {

    QuestionFragBase object;
    private boolean onlyInfo;


    public GKQuestionActivity()
    {
        this.currentActivity=OTHER_ACTIVITY;
    }
    boolean resumeFlag;
    boolean stickyFlag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showInterAd();

        resumeFlag=false;


        HashMap<Integer,Fragment> hm=new HashMap<Integer, Fragment>();
        HashMap<Integer,String> titles=new HashMap<>();

        if(APPSTATE.CURRENT_CATEGORY==null)
        {
            Toast.makeText(getBaseContext(),"Something went wrong!, Please reopen this topic or restart the app.",Toast.LENGTH_LONG).show();
            goBack();
            return;
        }

        if(APPSTATE.CURRENT_CATEGORY.equals("Awards And Honors")
                ||APPSTATE.CURRENT_CATEGORY.equals("Inventions And Discoveries"))
        {
            hm.put(0,new GKQueFragment());
            hm.put(1,new InterviewGeneral(APPSTATE.CURRENT_CATEGORY));

            titles.put(0,"Questions");
            titles.put(1,"Info");
        }

        else if(APPSTATE.CURRENT_CATEGORY.equals("Books And Authors")||APPSTATE.CURRENT_CATEGORY.equals("Days And Dates")||APPSTATE.CURRENT_CATEGORY.equals("Countries Capitals And Currencies"))
        {
            onlyInfo=true;
            hm.put(0,new InterviewGeneral(APPSTATE.CURRENT_CATEGORY));
            titles.put(0,"Info");
        }
        else
        {
            hm.put(0,new GKQueFragment());

            titles.put(0,"Questions");
        }




        intitTab(hm,titles,R.layout.activity_interview);



        if(tabLayout.getTabCount()==1)
        {
            tabLayout.setVisibility(GONE);
        }

        Drawable back;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            back=getResources().getDrawable(R.drawable.ic_back_arrow);
        }
        else
        {
            back=getResources().getDrawable(R.drawable.ic_back);
        }



        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

onBackPressed();
            }
        });

        getSupportActionBar().setTitle( APPSTATE.CURRENT_CATEGORY);

    }

    @Override
    public void onTick(String time) {

    }

    @Override
    public void onTimerStop(String time) {

    }

    @Override
    public String getTimerValues() {
        return null;
    }

    @Override
    public void getFragObject(QuestionFragBase ob) {
        object=ob;

    }

    @Override
    public String getMockTitle() {
        return null;
    }

    @Override
    public void resetTimerView() {

    }

    @Override
    public String getExp() {


        return object.sendExpToActivity();
    }

    @Override
    public String getOption() {
        return object.getOption();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        if(!onlyInfo) {
            menu.findItem(R.id.progressChat).setVisible(true);
            menu.findItem(R.id.bug).setVisible(true);
        }




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id)
        {

            case R.id.settings: drawer.openDrawer(settingsNav);
                return true;

            case R.id.progressChat:
                object.showMockReport();
                return true;

            case R.id.bug:object.sendBugReport();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }


    public void setImmersive()
    {
        Handler handler=new Handler();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {


                try
                {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);



                    View decor=getWindow().getDecorView();

                    decor.setSystemUiVisibility(  View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                }
                catch (Exception e)
                {

                }
            }
        };

        handler.postDelayed(runnable,1500);



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


        if(stickyFlag) {
            stickyFlag=false;
            return;
        }

        if(hasFocus)
        {

            View decor=getWindow().getDecorView();

            decor.setSystemUiVisibility(  View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        if(!resumeFlag) {

            setImmersive();
            resumeFlag=true;
        }
        else {
            View decor=getWindow().getDecorView();

            decor.setSystemUiVisibility(  View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        stickyFlag=true;



    }


    @Override
    public void hideTool(CardView layout, CardView title) {

    }

    @Override
    public void showTool(CardView layout, CardView title) {

    }

    @Override
    public void showOrHideTool(CardView layout, CardView title) {

    }

    private void showReportConfirm() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("View module analytics report?")
                .setPositiveButton("SHOW REPORT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        object.stopTimer(false);

                        APPSTATE.QUESTION_ACTIVITY_FLAG=false;
                        finish();
                        object.showMockReport();

                    }
                })

                .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goBack();
                    }
                })
                .create().show();

    }

    @Override
    public void onBackPressed() {

       if(onlyInfo)
       {
           goBack();
       }
        else
        {
            showReportConfirm();

        }

    }


    public void goBack()
    {

        Intent intent=new Intent(getBaseContext(),GKSubActivity.class);
        startActivity(intent);
        APPSTATE.BACK_FLAG=true;

        if(!App.isAdRemoved())
        {
            AdHelper.showVideoAd();
        }
    }
}
