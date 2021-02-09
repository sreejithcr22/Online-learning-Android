package com.codit.interview.aptitude.view;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.util.APPSTATE;
import com.google.firebase.crash.FirebaseCrash;



public class QuestionFragment extends QuestionFragBase implements View.OnClickListener
{



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






    }




    @Override
    public void onResume() {
        super.onResume();
        Log.d("questions", "onResume: ");
    }
}

