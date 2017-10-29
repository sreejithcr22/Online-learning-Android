package com.codit.interview.aptitude;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.native_ad.views.NativeAdViewAppWall;
import com.appodeal.ads.native_ad.views.NativeAdViewNewsFeed;

import static android.view.View.GONE;


/**
 * Created by Sreejith on 25-Jul-16.
 */
public class ParentCategoryHelper extends Fragment implements View.OnClickListener{

    public String currentFragment;
    NativeAdViewAppWall nav_nf;

    CardView cardView1;
    CardView cardView2;
    CardView cardView3;
    CardView adCard;

    SharedPreferences progressPreference;

    int maxProgress1,maxProgress2,maxProgress3,maxProgress4;
    int progressInt1=0,progressInt2=0,progressInt3=0,progressInt4=0;
    private Handler handler=new Handler();

    ProgressBar progressBar1,progressBar2,progressBar3;

    TextView progressText1,progressText2,progressText3;
    TextView mainTitle1,queText1,topicText1,mainTitle2,queText2,topicText2,mainTitle3,queText3,topicText3;


public void initializeProgressData()
{

    progressPreference=getContext().getSharedPreferences("progress",Context.MODE_PRIVATE);
    int quanti=(progressPreference.getInt("QUANTI_ATTEMPTED",0)*100)/APPSTATE.QUANTI_QUE_COUNT;
    int logical=(progressPreference.getInt("LOGIC_ATTEMPTED",0)*100)/APPSTATE.LOGICAL_QUE_COUNT;
    int verbal=(progressPreference.getInt("VERBAL_ATTEMPTED",0)*100)/APPSTATE.VERBAL_QUE_COUNT;
    int gk=(progressPreference.getInt("GK_ATTEMPTED",0)*100)/APPSTATE.GK_QUE_COUNT;


    if(currentFragment.equals("QUESTION")) {

        maxProgress1 = quanti;
        maxProgress2 = logical;
        maxProgress3 = verbal;
        maxProgress4 = 70;

    }
    else if(currentFragment.equals("GK"))
    {
        maxProgress1 = gk;
        maxProgress2 = 20;
        maxProgress3 = 30;
        maxProgress4 = 40;
    }
}

    public void showAd()
    {
        if(!App.isAdRemoved())
        {




            Appodeal.setNativeCallbacks(new NativeCallbacks() {
                @Override
                public void onNativeLoaded() {
                    Log.d("appodeal", "onNativeLoaded: ");

                    try {
                        if(nav_nf!=null&&!App.isAdRemoved()&&nav_nf.getVisibility()==GONE)
                        {
                            adCard.setLayoutParams(cardView1.getLayoutParams());
                            adCard.setVisibility(View.VISIBLE);
                            nav_nf.setVisibility(View.VISIBLE);
                            nav_nf.setNativeAd(Appodeal.getNativeAds(1).get(0));
                        }
                    }
                    catch (Exception e){}

                }

                @Override
                public void onNativeFailedToLoad() {
                    Log.d("appodeal", "onNativeFailedToLoad: ");
                }

                @Override
                public void onNativeShown(NativeAd nativeAd) {
                    Log.d("appodeal", "onNativeShown: ");
                }

                @Override
                public void onNativeClicked(NativeAd nativeAd) {

                }
            });

            Appodeal.initialize(getActivity(),App.APP_KEY,Appodeal.NATIVE);
            Appodeal.cache(getActivity(), Appodeal.NATIVE,2);


        }

    }


    public void initialize(View view, final Context context)
    {




        //initialize cards
        cardView1=(CardView)view.findViewById(R.id.card1);
        cardView2=(CardView)view.findViewById(R.id.card2);
        cardView3=(CardView)view.findViewById(R.id.card3);
        adCard=(CardView)view.findViewById(R.id.ad_parent_card);
        nav_nf= (NativeAdViewAppWall) view.findViewById(R.id.native_ad_view_news_feed);



        mainTitle1= (TextView) cardView1.findViewById(R.id.mainTitle);
        mainTitle2= (TextView) cardView2.findViewById(R.id.mainTitle);
        mainTitle3= (TextView) cardView3.findViewById(R.id.mainTitle);

        topicText1=(TextView)cardView1.findViewById(R.id.topicText);
        topicText2=(TextView)cardView2.findViewById(R.id.topicText);
        topicText3=(TextView)cardView3.findViewById(R.id.topicText);

        queText1= (TextView) cardView1.findViewById(R.id.queText);
        queText2= (TextView) cardView2.findViewById(R.id.queText);
        queText3= (TextView) cardView3.findViewById(R.id.queText);

        if(currentFragment.equals("GK"))
        {
            cardView2.setVisibility(GONE);
            cardView3.setVisibility(GONE);

            mainTitle1.setText("General Knowledge");


            topicText1.setText("Topics: "+11+"  |  ");


            queText1.setText("Questions: "+300+" ");



        }
        else
        {

            mainTitle1.setText("Quantitative Aptitude");
            mainTitle2.setText("Logical Reasoning");
            mainTitle3.setText("Verbal Ability");

            topicText1.setText("Topics: "+15+"  |  ");
            topicText2.setText("Topics: "+15+"  |  ");
            topicText3.setText("Topics: "+10+"  |  ");

            queText1.setText("Questions: "+372+" ");
            queText2.setText("Questions: "+300+" ");
            queText3.setText("Questions: "+228+" ");

        }


        int totalHeight=getResources().getDisplayMetrics().heightPixels;
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) cardView1.getLayoutParams();
        params.height= (int) (totalHeight*.17);
        cardView1.setLayoutParams(params);
        cardView2.setLayoutParams(params);
        cardView3.setLayoutParams(params);



        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);

        progressBar1= (ProgressBar) cardView1.findViewById(R.id.progressBar);

        progressBar2= (ProgressBar) cardView2.findViewById(R.id.progressBar);
        progressBar3= (ProgressBar) cardView3.findViewById(R.id.progressBar);

        progressText1= (TextView) cardView1.findViewById(R.id.progressText);
        progressText2= (TextView) cardView2.findViewById(R.id.progressText);
        progressText3= (TextView) cardView3.findViewById(R.id.progressText);



    }

    public void updateProgress()
    {
        progressBar1.setMax(100);
        progressBar2.setMax(100);
        progressBar3.setMax(100);


        progressBar1.setProgress(0);
        progressBar2.setProgress(0);
        progressBar3.setProgress(0);



        new Thread(new Runnable() {
            public void run() {


                while (progressInt1 <= maxProgress1 || progressInt2<=maxProgress2 || progressInt3<=maxProgress3 ||progressInt4<=maxProgress4 )
                {
                    handler.post(new Runnable() {
                        public void run() {

                            if(progressInt1<maxProgress1) {
                                progressInt1++;

                                progressBar1.setProgress(progressInt1);
                                progressText1.setText(progressInt1+"%");

                            }

                            if(progressInt2<maxProgress2) {

                                progressInt2++;
                                progressBar2.setProgress(progressInt2);
                                progressText2.setText(progressInt2+"%");
                            }

                            if(progressInt3<maxProgress3) {
                                progressInt3++;

                                progressBar3.setProgress(progressInt3);
                                progressText3.setText(progressInt3+"%");
                            }


                        }
                    });
                    try {

                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



            }
        }).start();

    }

    @Override
    public void onClick(View v)
    {


        String card1Target,card2Target,card3Target;

        if(currentFragment.equals("GK"))
        {
            //change these values later
            card1Target="";
            card2Target="";
            card3Target="";
        }
        else if(currentFragment.equals("QUESTION"))
        {
            card1Target="Quantitative";
            card2Target="Logical";
            card3Target="Verbal";
        }
        else
        {
            card1Target="Quantitative";
            card2Target="Logical";
            card3Target="Verbal";
        }


        int id=v.getId();

        switch (id)
        {
            case R.id.card1:


                goToSub(card1Target);
                break;

            case R.id.card2:
                goToSub(card2Target);
                break;

            case R.id.card3:
                goToSub(card3Target);
                break;
        }
    }

    public void goToSub(String extra)
    {

        if(currentFragment.equals("QUESTION"))
        {

            if(extra.equals("Quantitative"))
            {
                APPSTATE.PARENT_CATEG_QUANTI=true;
                APPSTATE.PARENT_CATEG_LOGICAL=false;
                APPSTATE.PARENT_CATEG_GK=false;
                APPSTATE.PARENT_CATEG_VERBAL=false;

            }
            else if(extra.equals("Logical"))
            {
                APPSTATE.PARENT_CATEG_QUANTI=false;
                APPSTATE.PARENT_CATEG_VERBAL=false;

                APPSTATE.PARENT_CATEG_LOGICAL=true;
                APPSTATE.PARENT_CATEG_GK=false;
            }
            else if(extra.equals("Verbal"))
            {
                APPSTATE.PARENT_CATEG_QUANTI=false;
                APPSTATE.PARENT_CATEG_VERBAL=true;

                APPSTATE.PARENT_CATEG_LOGICAL=false;
                APPSTATE.PARENT_CATEG_GK=false;
            }

            Intent intent = new Intent(getContext(), SubCategoryActivity.class);
            APPSTATE.CURRENT_QUE_SUB_CATEGORY=extra;
            startActivity(intent);

        }

        else if(currentFragment.equals("GK"))
        {
            APPSTATE.CURRENT_QUE_SUB_CATEGORY="GK";

            APPSTATE.PARENT_CATEG_QUANTI=false;
            APPSTATE.PARENT_CATEG_LOGICAL=false;
            APPSTATE.PARENT_CATEG_GK=true;
            APPSTATE.PARENT_CATEG_VERBAL=false;

            Intent intent = new Intent(getContext(), GKSubActivity.class);
            intent.putExtra("Sub Category", extra);
            startActivity(intent);
        }
    }

}
