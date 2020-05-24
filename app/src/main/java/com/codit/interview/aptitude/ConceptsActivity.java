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

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

import static android.view.View.GONE;

/**
 * Created by Sreejith on 21-Aug-16.
 */
public class ConceptsActivity extends NavActivityBase implements ConceptFragment.ConceptInterface{

    public ConceptsActivity() {
        this.currentActivity = OTHER_ACTIVITY;
    }

    ObjectAnimator hide,show;
    CardView bottomLayout;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        HashMap<Integer,Fragment> hm=new HashMap<>();
        hm.put(0,new ConceptFragment(0));
        hm.put(1,new ConceptFragment(1));
        hm.put(2,new ConceptFragment(2));

        HashMap<Integer,String> titles=new HashMap<>();
        titles.put(0,"Quantitative");
        titles.put(1,"Reasoning");
        titles.put(2,"Verbal Ability");


        intitTab(hm,titles,R.layout.activity_interview);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        setTitle("Formulas");


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                if(currentActivity.equals(GK_FRAGMENT)) {
                    if (tab.getPosition() == 1) {
                        tabAdapter.getObject().updateProgress();



                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
