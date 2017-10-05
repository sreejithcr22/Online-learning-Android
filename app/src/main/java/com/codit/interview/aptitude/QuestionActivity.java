package com.codit.interview.aptitude;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

public  class QuestionActivity extends NavActivityBase implements QuestionFragBase.QueFragInterface,CalcFragment.CalcInterface,ExpFragment.ExpFragInterface, NoteFragment.NoteInterface{

    TextView secondText,minuteText,colon;
    RelativeLayout relative;
    int mis,ses,width;
     SharedPreferences sharedPref =null;

    boolean resumeFlag;
    boolean stickyFlag;
    SharedPreferences shared;
    QuestionFragBase object;
    public QuestionActivity()
    {
        this.currentActivity=OTHER_ACTIVITY;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!App.isAdRemoved()) {
            showInterAd(getString(R.string.parentToSubInter));
        }

        resumeFlag=false;
        sharedPref=PreferenceManager.getDefaultSharedPreferences(getBaseContext());

         shared=getBaseContext().getSharedPreferences("timer",Context.MODE_PRIVATE);

        shared.edit().putBoolean("stopTimer",false).commit();

        Intent intent=getIntent();

        mis=APPSTATE.CURRENT_MIN;
        ses=APPSTATE.CURRENT_SEC;

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);


        initialize(R.layout.content_question);

        relative=new RelativeLayout(getBaseContext());
         minuteText=new TextView(getBaseContext());
        minuteText.setSingleLine(true);
        minuteText.setId(R.id.minuteText);
        minuteText.setBackgroundColor(Color.BLACK);
        minuteText.setTypeface(Typeface.DEFAULT_BOLD);


         colon=new TextView(getBaseContext());
        colon.setId(R.id.colonText);
        colon.setText(":");
        colon.setTypeface(Typeface.DEFAULT_BOLD);


        RelativeLayout.LayoutParams colonParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        colonParams.leftMargin=getResources().getInteger(R.integer.timer_colon_margin);
        colonParams.rightMargin=getResources().getInteger(R.integer.timer_colon_margin);

        colonParams.addRule(RelativeLayout.RIGHT_OF,minuteText.getId());
        colon.setLayoutParams(colonParams);

        secondText=new TextView(getBaseContext());
        secondText.setTypeface(Typeface.DEFAULT_BOLD);

        secondText.setTextIsSelectable(false);
        secondText.setBackgroundColor(Color.BLACK);
        secondText.setSingleLine(true);
        secondText.setId(R.id.secText);


        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF,colon.getId());


        secondText.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams minParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        minParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        minuteText.setLayoutParams(minParams);
        minuteText.setTextSize(17);
        secondText.setTextSize(17);
        colon.setTextSize(17);



        int paddingLeftRight=getResources().getInteger(R.integer.timer_padding_extra_left_right);
        secondText.setPadding(secondText.getPaddingLeft()+paddingLeftRight,secondText.getPaddingTop()+1,secondText.getPaddingRight()+paddingLeftRight,secondText.getPaddingBottom()+1);
        minuteText.setPadding(minuteText.getPaddingLeft()+paddingLeftRight,minuteText.getPaddingTop()+1,minuteText.getPaddingRight()+paddingLeftRight,minuteText.getPaddingBottom()+1);


        relative.addView(minuteText);
        relative.addView(colon);
        relative.addView(secondText);

        Toolbar.LayoutParams params=new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity= Gravity.END;
        params.leftMargin=getResources().getInteger(R.integer.timer_right_margin);
        params.rightMargin=getResources().getInteger(R.integer.timer_right_margin);

        ImageButton formula=new ImageButton(getBaseContext());
        formula.setImageResource(R.drawable.ic_square_root);
        formula.setBackgroundColor(Color.TRANSPARENT);

        toolbar.addView(relative,params);

        Drawable back;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

             back=getResources().getDrawable(R.drawable.ic_back_arrow);
        }
        else
        {
            back=getResources().getDrawable(R.drawable.ic_back);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            back.setTint(Color.WHITE);
        }
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showReportConfirm();
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);









    }

    private void showReportConfirm() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("View module analytics report?")
                .setPositiveButton("SHOW REPORT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        object.stopTimer(false);

                        APPSTATE.QUESTION_ACTIVITY_FLAG=true;
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
    public String getExp() {

        QuestionFragment questionFragment= (QuestionFragment) getSupportFragmentManager().findFragmentById(R.id.questionFragment);

        return questionFragment.sendExpToActivity();
    }

    @Override
    public String getOption() {

        QuestionFragment questionFragment= (QuestionFragment) getSupportFragmentManager().findFragmentById(R.id.questionFragment);

        return questionFragment.getOption();
    }

    @Override
    public void sendNote(String note)
    {


        try {
            object.setNote(note);
    object.updateNote(note);}


        catch (Exception e)
        {
            FirebaseCrash.log("updateNote() failed inside sendNote()");
            FirebaseCrash.report(e);
        }

    }

    @Override
    public void calcCopy(String history)
    {


        try
        {
            String currentNote=object.getNote();

            if(currentNote.equals("null"))
            {
                currentNote="";
            }

            String updateNote=currentNote+"\n"+"Copied from Calculator :-"+"\n"+history;

            object.updateNote(updateNote);
        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(),"Copy failed !",Toast.LENGTH_SHORT).show();
            FirebaseCrash.log("calc history copy failed inside calcCopy()");
            FirebaseCrash.report(e);
        }

    }

    @Override
    public void onTick(final String time) {


       if(shared.getBoolean("stopTimer",false))
        {
            shared.edit().putBoolean("stopTimer",false).commit();

            object.stopTimer(false);
        }

        try {

        if(time.equals("01:00")) {

            if(sharedPref.getBoolean("timer_alert",true))
            Toast.makeText(this, "1 minute remaining..", Toast.LENGTH_SHORT).show();


        }
        else if(time.equals("00:30")) {

            if(sharedPref.getBoolean("timer_alert",true))
            Toast.makeText(this, "30 seconds remaining..", Toast.LENGTH_SHORT).show();

        }
        else if(time.equals("00:00")) {

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


            if(sharedPref.getBoolean("timeup_vibrate",true)&&sharedPref.getBoolean("timer_alert",true))
            v.vibrate(200);

           if(sharedPref.getBoolean("timer_alert",true))
           {
               final AlertDialog.Builder builder = new AlertDialog.Builder(this);

               View view=getLayoutInflater().inflate(R.layout.timeup,null);
               Button ignore= (Button) view.findViewById(R.id.btnIgnore);

               builder.setView(view);



               AlertDialog dialog = null;

               dialog = builder.create();
               final AlertDialog finalDialog = dialog;
               ignore.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       finalDialog.dismiss();
                   }
               });
               dialog.getWindow().getAttributes().windowAnimations=R.style.CalcAnimation;
               int width=getResources().getDisplayMetrics().widthPixels;

               int widthPixls= (int) (width*.6);

               dialog.show();
               dialog.getWindow().setLayout(widthPixls,dialog.getWindow().getAttributes().height);

               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                   dialog.getWindow().setElevation(0);
               }




           }


            }



        }
        catch (Exception e) {

            e.printStackTrace();

        }

        RelativeLayout.LayoutParams colonParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        colonParams.leftMargin=getResources().getInteger(R.integer.timer_colon_margin);
        colonParams.rightMargin=getResources().getInteger(R.integer.timer_colon_margin);

        colonParams.addRule(RelativeLayout.RIGHT_OF,minuteText.getId());
        colon.setLayoutParams(colonParams);

        minuteText.setBackgroundColor(Color.BLACK);
        secondText.setBackgroundColor(Color.BLACK);
        secondText.setTypeface(Typeface.DEFAULT_BOLD);
        minuteText.setTypeface(Typeface.DEFAULT_BOLD);
        final String times[]=time.split(":");

        if(times[0].contains("-"))
        {

            minuteText.setTextColor(Color.RED);
            secondText.setTextColor(Color.RED);

        }
        else
        {

            minuteText.setTextColor(Color.WHITE);
            secondText.setTextColor(Color.WHITE);
            colon.setTextColor(Color.WHITE);
        }

        if(!minuteText.getText().equals(times[0]))
        {


            ObjectAnimator anim=ObjectAnimator.ofFloat(minuteText,"rotationX",0f,90f);
            anim.setDuration(300);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {


                    minuteText.setText(times[0]);
                    ObjectAnimator anim180=ObjectAnimator.ofFloat(minuteText,"rotationX",90f,180f);
                    anim180.setDuration(300);
                    anim180.setDuration(0);
                    anim180.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                            minuteText.setText(times[0]);
                            ObjectAnimator anim360=ObjectAnimator.ofFloat(minuteText,"rotationX",180f,360f);
                            anim360.setDuration(0);
                            anim360.start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    anim180.start();

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.start();
        }



        ObjectAnimator anim=ObjectAnimator.ofFloat(secondText,"rotationX",0f,90f);
        anim.setDuration(300);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                secondText.setText(times[1]);

                ObjectAnimator anim180=ObjectAnimator.ofFloat(secondText,"rotationX",90f,180f);
                anim180.setDuration(300);
                anim180.setDuration(0);
                anim180.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        minuteText.setWidth(secondText.getWidth());
                        ObjectAnimator anim360=ObjectAnimator.ofFloat(secondText,"rotationX",180f,360f);
                        anim360.setDuration(0);
                        anim360.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                anim180.start();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.start();



    }

    @Override
    public void onTimerStop(String time)
    {


        if(time.equals("null"))
        {
            time="0:36";
        }
        final String[] times=time.split(":");

        RelativeLayout.LayoutParams colonParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        colonParams.leftMargin=0;
        colonParams.rightMargin=0;


        colonParams.addRule(RelativeLayout.RIGHT_OF,minuteText.getId());
        colon.setLayoutParams(colonParams);

        AnimatorSet setOut=new AnimatorSet();
        final AnimatorSet setIn=new AnimatorSet();
        ObjectAnimator fadeOut=ObjectAnimator.ofFloat(minuteText,"translationY",0f,-200f);
        ObjectAnimator fadeOut1=ObjectAnimator.ofFloat(secondText,"translationY",0f,-200f);
        ObjectAnimator fadeOut2=ObjectAnimator.ofFloat(colon,"translationY",0f,-200f);

        ObjectAnimator fadeIn=ObjectAnimator.ofFloat(minuteText,"translationY",200f,0f);
        ObjectAnimator fadeIn1=ObjectAnimator.ofFloat(secondText,"translationY",200f,0f);
        ObjectAnimator fadeIn2=ObjectAnimator.ofFloat(colon,"translationY",200f,0f);

        fadeOut.setDuration(300);
        fadeIn.setDuration(300);
        fadeIn1.setDuration(300);
        fadeOut1.setDuration(300);
        fadeIn2.setDuration(300);
        fadeOut2.setDuration(300);

        setOut.playTogether(fadeOut,fadeOut1,fadeOut2);
        setIn.playTogether(fadeIn,fadeIn1,fadeIn2);


        setOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                String min=times[0];
                String sec=times[1];

                if(times[0].length()==1)
                {
                    min="0"+times[0];
                }
                if(times[1].length()==1)
                {
                    sec="0"+times[1];
                }
                minuteText.setText(min);
                secondText.setText(sec);


                minuteText.setTextColor(Color.WHITE);
                secondText.setTextColor(Color.WHITE);
                colon.setTextColor(Color.WHITE);
                minuteText.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                secondText.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                minuteText.setBackgroundColor(Color.TRANSPARENT);
                secondText.setBackgroundColor(Color.TRANSPARENT);
                setIn.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        setOut.start();




    }

    @Override
    public String getTimerValues() {

        return String.valueOf(mis).toString()+":"+String.valueOf(ses).toString();
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


        ObjectAnimator fadeOut=ObjectAnimator.ofFloat(minuteText,"translationY",0f,-200f);
        ObjectAnimator fadeOut1=ObjectAnimator.ofFloat(secondText,"translationY",0f,-200f);
        ObjectAnimator fadeOut2=ObjectAnimator.ofFloat(colon,"translationY",0f,-200f);

        ObjectAnimator fadeIn=ObjectAnimator.ofFloat(minuteText,"translationY",200f,0f);
        ObjectAnimator fadeIn1=ObjectAnimator.ofFloat(secondText,"translationY",200f,0f);
        ObjectAnimator fadeIn2=ObjectAnimator.ofFloat(colon,"translationY",200f,0f);



        AnimatorSet out=new AnimatorSet();
        final AnimatorSet in=new AnimatorSet();

        out.playTogether(fadeOut,fadeOut1,fadeOut2);
        in.playTogether(fadeIn,fadeIn1,fadeIn2);
        out.setDuration(300);
        in.setDuration(300);

        out.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                in.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        out.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

       menu.findItem(R.id.progressChat).setVisible(true);
        menu.findItem(R.id.formula).setVisible(true);
        menu.findItem(R.id.bug).setVisible(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

            case R.id.formula:showFormula(APPSTATE.CURRENT_ACTIVITY_TITLE);
                return true;

            case R.id.bug:object.sendBugReport();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


public void showFormula(String title)
{
    object.formula(title);
}

    @Override
    public void onBackPressed() {
        showReportConfirm();


    }

    public void goBack()
    {
        object.stopTimer(false);
        Intent intent=new Intent(getBaseContext(),SubCategoryActivity.class);
        startActivity(intent);
        APPSTATE.BACK_FLAG=true;
        APPSTATE.SUB_BACK_FLAG=true;

        if(!App.isAdRemoved())
        AdHelper.showVideoAd();
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




}
