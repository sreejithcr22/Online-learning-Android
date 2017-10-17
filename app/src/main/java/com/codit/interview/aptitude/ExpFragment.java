package com.codit.interview.aptitude;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.native_ad.views.NativeAdViewNewsFeed;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import static android.view.View.GONE;

/**
 * Created by Sreejith on 15-Mar-16.
 */
public class ExpFragment extends DialogFragment {


    NativeAdViewNewsFeed nav_nf;
    String explanation;

    ExpFragInterface expFragInterface;





    interface ExpFragInterface
    {
         String getExp();
         String getOption();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            expFragInterface=(ExpFragInterface)activity;
        }

        catch (ClassCastException e)
        {
        }
    }

    TextView expText,title;

    String option;


    Dialog dialog;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(!App.isAdRemoved())
        {
            Log.d("appodeal", "onCreate: ");

            Appodeal.setNativeCallbacks(new NativeCallbacks() {
                @Override
                public void onNativeLoaded() {
                    Log.d("appodeal", "onNativeLoaded: ");
                    if(nav_nf!=null&&!App.isAdRemoved()&&nav_nf.getVisibility()==GONE)
                    {
                        nav_nf.setVisibility(View.VISIBLE);
                        nav_nf.setNativeAd(Appodeal.getNativeAds(1).get(0));
                    }
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
            Appodeal.cache(getActivity(), Appodeal.NATIVE,1);
        }

        APPSTATE.EXPL_COUNT++;

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setView(R.layout.exp_fragment);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view=inflater.inflate(R.layout.exp_fragment, null);
        nav_nf = (NativeAdViewNewsFeed) view.findViewById(R.id.native_ad_view_news_feed);




        expText=(TextView)view.findViewById(R.id.expText);
        title=(TextView)view.findViewById(R.id.expTitle);

        explanation= expFragInterface.getExp();
        option=expFragInterface.getOption();

        expText.setText(explanation);
        title.setText("Answer - "+option);

        builder.setCancelable(true);


        builder.setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDialog().dismiss();
            }
        });



        builder.setView(view);
         dialog=builder.create();

        dialog.getWindow().getAttributes().windowAnimations=R.style.CalcAnimation;

        return dialog;

    }



}
