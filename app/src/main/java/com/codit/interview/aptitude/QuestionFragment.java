package com.codit.interview.aptitude;


import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.BannerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.crash.FirebaseCrash;



public class QuestionFragment extends QuestionFragBase implements View.OnClickListener
{


    BannerView banner;

    public QuestionFragment() {
        this.currentFragment=FRAG_APTI;

    }

    public QuestionFragment(String frag)
    {
        if(frag.equals(FRAG_APTI_FAV))
        {
            this.currentFragment=FRAG_APTI_FAV;
        }
        else if(frag.equals(FRAG_GK_FAV))
        {
            this.currentFragment=FRAG_GK_FAV;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {

            setUp();
        }
        catch(Exception e)
        {
            FirebaseCrash.report(e);
            Intent intent=new Intent(getContext(),SubCategoryActivity.class);
            startActivity(intent);
            APPSTATE.BACK_FLAG=true;
            Toast.makeText(getContext(),"Sorry, something went wrong !",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




         View view = null;

        // Inflate the layout for this fragment
        try {
              view = inflater.inflate(R.layout.fragment_question, container, false);

            initialize(view);

            banner= (BannerView) view.findViewById(R.id.appodealBannerView);
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
            Intent intent=new Intent(getContext(),SubCategoryActivity.class);
            startActivity(intent);
            APPSTATE.BACK_FLAG=true;
            Toast.makeText(getContext(),"Sorry, something went wrong !",Toast.LENGTH_SHORT).show();
        }
        showBanner();
        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        try
        {

            start();
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
            Intent intent=new Intent(getContext(),SubCategoryActivity.class);
            startActivity(intent);
            APPSTATE.BACK_FLAG=true;
            Toast.makeText(getContext(),"Sorry, something went wrong !",Toast.LENGTH_SHORT).show();
        }






    }

    public void showBanner()
    {
        if(!App.isAdRemoved())
        {
            Appodeal.setBannerViewId(R.id.appodealBannerView);
            if(Appodeal.isLoaded(Appodeal.BANNER_VIEW))
            {

                banner.setVisibility(View.VISIBLE);
                Appodeal.show(getActivity(), Appodeal.BANNER_BOTTOM);
            }





            Appodeal.setBannerCallbacks(new BannerCallbacks() {
                @Override
                public void onBannerLoaded(int height, boolean isPrecache) {
                    Log.d("Appodeal", "onBannerLoaded");
                    banner.setVisibility(View.VISIBLE);

                    Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);


                }
                @Override
                public void onBannerFailedToLoad() {
                    Log.d("Appodeal", "onBannerFailedToLoad");
                }
                @Override
                public void onBannerShown() {
                    Log.d("Appodeal", "onBannerShown");
                }
                @Override
                public void onBannerClicked() {
                    Log.d("Appodeal", "onBannerClicked");
                }
            });

            Appodeal.initialize(getActivity(), App.APP_KEY, Appodeal.BANNER_VIEW);
        }
        else
        {
            if(banner!=null)
            {
                banner.setVisibility(View.GONE);
                 }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("questions", "onResume: ");
    }
}

