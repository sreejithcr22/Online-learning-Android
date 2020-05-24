package com.codit.interview.aptitude;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by Sreejith on 13-Aug-16.
 */
public class GKQueFragment extends QuestionFragBase {

    public GKQueFragment()
    {
        this.currentFragment=this.FRAG_GK;
    }

    public GKQueFragment(String frag)
    {
        if(frag.equals(FRAG_GK))
        {
            this.currentFragment=FRAG_GK;
        }
        else if(frag.equals(FRAG_GK_FAV))
        {
            this.currentFragment=FRAG_GK_FAV;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUp();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.gk_question, container, false);


            initialize(view);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        try {
            start();
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);

            Toast.makeText(getContext(),"Sorry, something went wrong !",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(getContext(),GKSubActivity.class);
            startActivity(intent);
            APPSTATE.BACK_FLAG=true;
        }
    }
}
