package com.codit.interview.aptitude;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.native_ad.views.NativeAdViewNewsFeed;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import static android.view.View.GONE;


public class ParentCategoryFragment extends ParentCategoryHelper

{

    CardView adCard,card1;
   SharedPreferences progressPreference;

    public ParentCategoryFragment()
    {
        currentFragment="QUESTION";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        progressPreference=getContext().getSharedPreferences("progress", Context.MODE_PRIVATE);

        View view=inflater.inflate(R.layout.fragment_parent_category, container, false);

        initialize(view,getContext());

       // card1=(CardView)view.findViewById(R.id.card1);
        //nav_nf= (NativeAdViewNewsFeed) view.findViewById(R.id.native_ad_view_news_feed);

        return view;
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


            /*adCard.setLayoutParams(card1.getLayoutParams());
            nativeExpressAdView=new NativeExpressAdView(getContext());
            nativeExpressAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adCard.setVisibility(View.VISIBLE);
                }
            });
            adCard.removeAllViews();
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(0,0,0,0);

            nativeExpressAdView.setLayoutParams(params);



            adCard.addView(nativeExpressAdView);

            card1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("NewApi")
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    int width = card1.getWidth();
                    int height = card1.getHeight();

                    float density=getResources().getDisplayMetrics().density;
                    int actualWidth= (int) (width/ density);
                    int actualHeight= (int) (height/density);
                    int adWidth=actualWidth-10;
                    int adHeight=actualHeight-11;

                    if(adWidth<280)
                        adWidth=280;

                    if (adHeight<80)
                        adHeight=80;


                    nativeExpressAdView.setAdSize(new AdSize(adWidth,adHeight));

                    nativeExpressAdView.setAdUnitId(getString(R.string.parent_categ_native));
                    AdRequest adRequest = new AdRequest.Builder()
                            .build();

                    if(!App.isAdRemoved())
                    nativeExpressAdView.loadAd(adRequest);



                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                        adCard.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    else
                        adCard.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });*/




        initializeProgressData();
        updateProgress();
        try {
            showAd();
        }
        catch (Exception e){}

    }






}


