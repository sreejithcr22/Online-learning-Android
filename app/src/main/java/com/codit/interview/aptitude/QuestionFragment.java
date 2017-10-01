package com.codit.interview.aptitude;


import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.crash.FirebaseCrash;



public class QuestionFragment extends QuestionFragBase implements View.OnClickListener
{



    AdView aptiBanner;



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


            aptiBanner = (AdView) view.findViewById(R.id.aptiBanner);
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
            Intent intent=new Intent(getContext(),SubCategoryActivity.class);
            startActivity(intent);
            APPSTATE.BACK_FLAG=true;
            Toast.makeText(getContext(),"Sorry, something went wrong !",Toast.LENGTH_SHORT).show();
        }
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



        AdRequest adRequest = new AdRequest.Builder()
                .build();

       aptiBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                aptiBanner.setVisibility(View.VISIBLE);
            }
        });

        if(this.currentFragment.equals(FRAG_APTI))
        {
                aptiBanner.loadAd(adRequest);
        }
        else
        {
            aptiBanner.setVisibility(View.GONE);
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("questions", "onResume: ");
    }
}

