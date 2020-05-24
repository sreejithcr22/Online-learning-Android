package com.codit.interview.aptitude;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

import static android.view.View.GONE;

public class InterviewActivity extends NavActivityBase implements InterviewGeneral.interviewInterface{
    ObjectAnimator hide,show;
   public InterviewActivity()
    {
        this.currentActivity=OTHER_ACTIVITY;
    }


    CardView bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        HashMap<Integer,Fragment> hm=new HashMap<>();
        hm.put(0,new InterviewGeneral(0));
        hm.put(1,new InterviewGeneral(1));
        hm.put(2,new InterviewGeneral(2));
        hm.put(3,new InterviewGeneral(3));
        hm.put(4,new InterviewGeneral(4));
        hm.put(5,new InterviewGeneral(5));
        hm.put(6,new InterviewGeneral(6));
        hm.put(7,new InterviewGeneral(7));
        hm.put(8,new InterviewGeneral(8));
        hm.put(9,new InterviewGeneral(9));
        hm.put(10,new InterviewGeneral(10));
        hm.put(11,new InterviewGeneral(11));



        HashMap<Integer,String> titles=new HashMap<>();
        titles.put(0,"HR");
        titles.put(1,"Data Structure");
        titles.put(2,"Java");
        titles.put(3,"C/C++");
        titles.put(4,"C#");
        titles.put(5,"Python");
        titles.put(6,"PHP");
        titles.put(7,"Java Script");
        titles.put(8,"Software Testing");
        titles.put(9,"Network");
        titles.put(10,"DBMS");
        titles.put(11,"OS");

        intitTab(hm,titles,R.layout.activity_interview);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        setTitle("Interview");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
        APPSTATE.BACK_FLAG=true;
        APPSTATE.CURRENT_SELECTED_DRAWER_ITEM=-1;
    }

    @Override
    public void hideTool(CardView layout, final CardView title) {

        if(!getSupportActionBar().isShowing())
        {
            return;
        }
        bottomLayout=layout;




        ObjectAnimator hideCard=ObjectAnimator.ofFloat(title,"translationY",0f,-200f);
        hideCard.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                title.setVisibility(GONE);
                getSupportActionBar().hide();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        hide=ObjectAnimator.ofFloat(bottomLayout,"translationY",0f,200f);

        hide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        AnimatorSet hideSet=new AnimatorSet();
        hideSet.playTogether(hide,hideCard);
        hideSet.setDuration(250);
        hideSet.start();


    }

    @Override
    public void showTool(CardView layout, final CardView title) {
        bottomLayout=layout;
        show=ObjectAnimator.ofFloat(bottomLayout,"translationY",bottomLayout.getTranslationY(),0f);
        show.setDuration(250);
        show.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                getSupportActionBar().show();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                bottomLayout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        title.setVisibility(View.VISIBLE);
        ObjectAnimator showCard=ObjectAnimator.ofFloat(title,"translationY",title.getTranslationY(),0f);
        showCard.setDuration(250);
        showCard.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                title.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        AnimatorSet showSet=new AnimatorSet();
        showSet.setDuration(250);
        showSet.playTogether(show,showCard);
        showSet.start();

    }

    @Override
    public void showOrHideTool(CardView layout, final CardView title) {
        if(getSupportActionBar().isShowing())
        {
            bottomLayout=layout;

            ObjectAnimator hideCard=ObjectAnimator.ofFloat(title,"translationY",0f,-200f);
            hideCard.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    title.setVisibility(GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            hide=ObjectAnimator.ofFloat(bottomLayout,"translationY",0f,200f);

            hide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    getSupportActionBar().hide();

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });


            AnimatorSet hideSet=new AnimatorSet();
            hideSet.playTogether(hide,hideCard);
            hideSet.setDuration(250);
            hideSet.start();

        }
        else
        {
            bottomLayout=layout;
            show=ObjectAnimator.ofFloat(bottomLayout,"translationY",bottomLayout.getTranslationY(),0f);
            show.setDuration(250);
            show.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    getSupportActionBar().show();
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    bottomLayout.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            title.setVisibility(View.VISIBLE);
            ObjectAnimator showCard=ObjectAnimator.ofFloat(title,"translationY",title.getTranslationY(),0f);
            showCard.setDuration(250);
            showCard.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    title.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            AnimatorSet showSet=new AnimatorSet();
            showSet.setDuration(250);
            showSet.playTogether(show,showCard);
            showSet.start();

        }
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
