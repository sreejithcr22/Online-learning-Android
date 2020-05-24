package com.codit.interview.aptitude;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.NativeExpressAdView;


public class GKFragment extends ParentCategoryHelper {

    NativeExpressAdView nativeExpressAdView;
    CardView adCard,card1;

    public GKFragment() {

        currentFragment="GK";
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_parent_category, container, false);

        adCard=(CardView)view.findViewById(R.id.ad_parent_card);
        card1=(CardView)view.findViewById(R.id.card1);




initialize(view,getContext());

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initializeProgressData();

    }


}
