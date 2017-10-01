package com.codit.interview.aptitude;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


/**
 * Created by Sreejith on 01-Oct-17.
 */

public class AdHelper {



    private static final int REWARD_COINS =100 ;
    static RewardedVideoAd videoAd;
    static Context appContext;
    static  final String VIDEO_AD_ID="ca-app-pub-7040172378865675/6753783991";

    static boolean isRewarded=false;

    public static void initialize(Context context)
    {
        appContext=context;
        MobileAds.initialize(context, VIDEO_AD_ID);
        videoAd=MobileAds.getRewardedVideoAdInstance(context);
        videoAd.setRewardedVideoAdListener(new Listener());

        loadVideoAd();
    }



    public static void showVideoAd() {


        if (videoAd.isLoaded()) {
            videoAd.show();
        }
        else {
            loadVideoAd();


        }
    }

    private static void loadVideoAd() {
        if(!videoAd.isLoaded())
            videoAd.loadAd(VIDEO_AD_ID, new AdRequest.Builder().build());

    }

    static class Listener implements RewardedVideoAdListener
    {


        @Override
        public void onRewardedVideoAdLoaded() {



        }

        @Override
        public void onRewardedVideoAdOpened() {

        }

        @Override
        public void onRewardedVideoStarted() {


        }

        @Override
        public void onRewardedVideoAdClosed() {

           /* if(isRewarded)
            {
                isRewarded=false;
                if(!isHint) {
                    Toast.makeText(appContext,"Rewarded",Toast.LENGTH_SHORT).show();
                    SessionManager.setTotalCoins(SessionManager.getTotalCoins() + REWARD_COINS);
                }
                else
                {
                    Toast.makeText(appContext,"hint",Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new HintVideoEvent());
                }
            }
            else
            {
                if (!isHint)
                    Toast.makeText(appContext,"Watch the full video to earn reward",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(appContext,"Watch the full video to get a hint",Toast.LENGTH_SHORT).show();
            }*/
            loadVideoAd();

        }

        @Override
        public void onRewarded(RewardItem rewardItem) {


        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
            loadVideoAd();

        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {

                //Toast.makeText(appContext, "Video failed to load, Please check your internet connection and try again !", Toast.LENGTH_SHORT).show();
            loadVideoAd();

        }
    }


}
