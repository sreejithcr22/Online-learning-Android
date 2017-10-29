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


        return view;
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeProgressData();
        updateProgress();
        try {
            showAd();
        }
        catch (Exception e){}

    }






}


