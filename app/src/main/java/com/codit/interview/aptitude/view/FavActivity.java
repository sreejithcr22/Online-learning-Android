package com.codit.interview.aptitude.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.util.APPSTATE;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

import static android.view.View.GONE;

/**
 * Created by Sreejith on 25-Aug-16.
 */
public  class FavActivity extends NavActivityBase implements InterviewGeneral.interviewInterface,QuestionFragBase.QueFragInterface,CalcFragment.CalcInterface,ExpFragment.ExpFragInterface, NoteFragment.NoteInterface,ConceptFragment.ConceptInterface{


    static String FRAG_GK_FAV="gk_fac";
    static String FRAG_APTI_FAV="apti_fav";

    QuestionFragBase fragment,expOb;
    ObjectAnimator hide,show;


    public FavActivity()
    {
        this.currentActivity=OTHER_ACTIVITY;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HashMap<Integer,Fragment> hm=new HashMap<>();
        hm.put(0,new InterviewGeneral());
        hm.put(2,new ConceptFragment());
        hm.put(1,new GKQueFragment(FRAG_GK_FAV));
        hm.put(3, new QuestionFragment(FRAG_APTI_FAV));

        HashMap<Integer,String> titles=new HashMap<>();
        titles.put(0,"Interview");
        titles.put(1,"GK");
        titles.put(2,"Formulas");
        titles.put(3,"Aptitude");

        intitTab(hm,titles, R.layout.activity_interview);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        getSupportActionBar().hide();

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
    public String getExp() {


        if(tabLayout.getSelectedTabPosition()==1)
        {
            return expOb.sendExpToActivity();
        }
        else if(tabLayout.getSelectedTabPosition()==3)
        {
            return fragment.sendExpToActivity();
        }

        return null;
    }

    @Override
    public String getOption() {
        if(tabLayout.getSelectedTabPosition()==1)
        {
            return expOb.getOption();
        }
        else if(tabLayout.getSelectedTabPosition()==3)
        {
            return fragment.getOption();
        }

        return null;
    }

    @Override
    public void sendNote(String note)
    {

        fragment.updateNote(note);

    }

    @Override
    public void calcCopy(String history)
    {


        String currentNote=fragment.getNote();

        if(currentNote.equals("null"))
        {
            currentNote="";
        }

        String updateNote=currentNote+"\n"+"copied from calculator"+"\n"+history;

        fragment.updateNote(updateNote);

        Toast.makeText(getApplicationContext(),"inside calcCopy in activity",Toast.LENGTH_SHORT).show();

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

        if(ob.getClass()==QuestionFragment.class)
        {
            fragment=ob;
        }
        else if(ob.getClass()==GKQueFragment.class)
        {
            expOb=ob;
        }
        fragment=ob;
    }

    @Override
    public String getMockTitle() {
        return null;
    }

    @Override
    public void resetTimerView() {

    }





    @Override
    public void hideTool(CardView layout, final CardView title) {

        if(title.getVisibility()==GONE)
        {
            return;
        }




        ObjectAnimator hideCard=ObjectAnimator.ofFloat(title,"translationY",0f,-200f);
        // hideCard.setDuration(200);
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

        hide=ObjectAnimator.ofFloat(layout,"translationY",0f,200f);

        //hide.setDuration(250);

        // hide = AnimationUtils.loadAnimation(getBaseContext(),
        //      R.anim.slide_up);

        hide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

               // getSupportActionBar().hide();

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
    public void showTool(final CardView layout, final CardView title) {
        show=ObjectAnimator.ofFloat(layout,"translationY",layout.getTranslationY(),0f);
        show.setDuration(250);
        show.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                layout.setVisibility(View.VISIBLE);


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
    public void showOrHideTool(final CardView layout, final CardView title) {
        if(title.getVisibility()==View.VISIBLE)
        {

            ObjectAnimator hideCard=ObjectAnimator.ofFloat(title,"translationY",0f,-200f);
            // hideCard.setDuration(200);
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

            hide=ObjectAnimator.ofFloat(layout,"translationY",0f,200f);

            //hide.setDuration(250);

            // hide = AnimationUtils.loadAnimation(getBaseContext(),
            //      R.anim.slide_up);

            hide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    //getSupportActionBar().hide();

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
            show=ObjectAnimator.ofFloat(layout,"translationY",layout.getTranslationY(),0f);
            show.setDuration(250);
            show.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layout.setVisibility(View.VISIBLE);



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


}
