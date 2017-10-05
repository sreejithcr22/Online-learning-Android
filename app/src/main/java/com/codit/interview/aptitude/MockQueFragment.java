package com.codit.interview.aptitude;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Sreejith on 05-Sep-16.
 */
public class MockQueFragment extends QuestionFragBase {


    public MockQueFragment()
    {
        this.currentFragment=FRAG_MOCK;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try
        {
            setUp();
        }
        catch (Exception e)
        {
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_mock_que, container, false);

        if(!App.isAdRemoved())
        {
            final AdView adView= (AdView) view.findViewById(R.id.mockBanner);

            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adView.setVisibility(View.VISIBLE);
                }
            });
            adView.loadAd(adRequest);
        }



try
{
    initialize(view);

}
catch (Exception e)
{
}
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        start();
    }

}
