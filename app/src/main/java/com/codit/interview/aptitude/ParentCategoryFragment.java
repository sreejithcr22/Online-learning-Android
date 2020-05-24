package com.codit.interview.aptitude;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;




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

    }






}


