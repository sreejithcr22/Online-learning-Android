package com.codit.interview.aptitude.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.util.APPSTATE;


/**
 * Created by Sreejith on 05-Sep-16.
 */
public class MockQueActivity extends AppCompatActivity implements QuestionFragBase.QueFragInterface,ExpFragment.ExpFragInterface{

    String title;

    TextView secondText,minuteText,colon;
    RelativeLayout relative;
    QuestionFragBase object;
    boolean alertShown=false;
    boolean resumeFlag;
    boolean stickyFlag;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        resumeFlag=false;
        Intent intent=getIntent();
        title= intent.getStringExtra("title");

        APPSTATE.MOCK_TEST_ON=true;


        if(APPSTATE.CURRENT_THEME!=0)
        {
            setTheme(APPSTATE.CURRENT_THEME);
        }
        else
        {
            APPSTATE.CURRENT_THEME=APPSTATE.THEME_DEFAULT;
            setTheme(APPSTATE.THEME_DEFAULT);

        }
        setContentView(R.layout.activity_mock_que);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if(title==null)
        {
            title="Mock Test";
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        // params.leftMargin=10;


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

        Toolbar.LayoutParams params1=new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.gravity= Gravity.END;

        params1.rightMargin=getResources().getInteger(R.integer.submit_right_margin);
        params1.leftMargin=getResources().getInteger(R.integer.submit_left_margin);


        params.rightMargin=getResources().getInteger(R.integer.timer_right_margin);

        TextView submit=new TextView(getBaseContext());
        submit.setText("SUBMIT");
        submit.setTextSize(16);
submit.setPadding(getResources().getInteger(R.integer.submit_left_padding),getResources().getInteger(R.integer.submit_top_padding),getResources().getInteger(R.integer.submit_right_padding),getResources().getInteger(R.integer.submit_bottom_padding));
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int color = typedValue.data;
        submit.setBackgroundColor(color);
        submit.setTextColor(Color.WHITE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitTest();

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            submit.setElevation(2);
        }
        //submit.setTypeface(null,Typeface.BOLD);


        toolbar.addView(submit,params1);

        toolbar.addView(relative,params);

        Drawable back;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            back=getResources().getDrawable(R.drawable.ic_back_arrow);
        }
        else
        {
            back=getResources().getDrawable(R.drawable.ic_back);
        }

        toolbar.setNavigationIcon(back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setTitle("");

    }


    public void submitTest()
    {

        if(!APPSTATE.MOCK_TEST_ON)
        {
            Toast.makeText(getBaseContext(),"Test submitted already !",Toast.LENGTH_SHORT).show();
            return;
        }



        object.finishTest();

    }

    @Override
    public void onBackPressed() {


        if(!APPSTATE.MOCK_TEST_ON) {
            Intent intent=new Intent(getBaseContext(),MockActivity.class);
            startActivity(intent);
            APPSTATE.BACK_FLAG=true;
            return;
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Mock test is in progress. Are you sure you want to go back?")
                .setPositiveButton("GO BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        APPSTATE.MOCK_TEST_ON=false;
                        Intent intent=new Intent(getBaseContext(),MockActivity.class);
                        startActivity(intent);
                        APPSTATE.BACK_FLAG=true;

                    }
                })
                .setNegativeButton("STAY",null)
                .create().show();
    }

    @Override
    public void onTick(String time) {


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
                //minuteText.setBackgroundColor(Color.BLACK);
                //secondText.setBackgroundColor(Color.BLACK);
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

                            //secondText.setText(times[1]);
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
    public void onTimerStop(String time) {

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


                minuteText.setTextColor(Color.BLACK);
                secondText.setTextColor(Color.BLACK);
                colon.setTextColor(Color.BLACK);
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
        return null;
    }

    @Override
    public void getFragObject(QuestionFragBase ob) {
         object=ob;

    }

    @Override
    public String getMockTitle() {
        return title;
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
    public String getExp() {
        return object.sendExpToActivity();
    }

    @Override
    public String getOption() {
        return object.getOption();
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



        if(!APPSTATE.MOCK_TEST_ON&&!alertShown)

        {

            alertShown=true;
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Explanations are now available !")
                    .setIcon(R.drawable.ic_light_bulb)

                    .setCancelable(true)
                    .setPositiveButton("OK",null)
                    .create().show();
        }


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
