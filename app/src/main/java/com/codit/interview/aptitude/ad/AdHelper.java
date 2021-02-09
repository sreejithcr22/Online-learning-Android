package com.codit.interview.aptitude.ad;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


public class AdHelper {

    private static AdHelper instance;
    private InterstitialAd mInterstitialAd;
    private static final String AD_ID = "ca-app-pub-3940256099942544/1033173712";
    private static final String TEST_ID = "ca-app-pub-3940256099942544/1033173712";
    private int counter = 0;
    private static final int frequency = 3;
    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            if ((counter % frequency) == 0 && mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
    };


    private AdHelper(Context context) {
        MobileAds.initialize(context);

    }

    public static AdHelper getInstance(Context context) {
        if (instance == null) instance = new AdHelper(context);
        return instance;
    }

    public void showAd(Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(AD_ID);
        mInterstitialAd.setAdListener(adListener);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        counter++;
    }


}
