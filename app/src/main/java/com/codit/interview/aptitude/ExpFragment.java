package com.codit.interview.aptitude;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by Sreejith on 15-Mar-16.
 */
public class ExpFragment extends DialogFragment {


    String explanation;

    ExpFragInterface expFragInterface;





    interface ExpFragInterface
    {
         String getExp();
         String getOption();
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

        APPSTATE.EXPL_COUNT++;

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setView(R.layout.exp_fragment);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view=inflater.inflate(R.layout.exp_fragment, null);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int wdth1=view.getWidth();

                float density=getContext().getResources().getDisplayMetrics().density;
             int  width = (int) (wdth1/ density);


            }
        });



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

        NativeExpressAdView nativeExpressAdView= (NativeExpressAdView) view.findViewById(R.id.expNative);
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        if(APPSTATE.EXPL_COUNT%APPSTATE.EXPL_AD_FREQ==0 && APPSTATE.EXPL_COUNT!=0)
        nativeExpressAdView.loadAd(adRequest);

        builder.setView(view);
         dialog=builder.create();

        dialog.getWindow().getAttributes().windowAnimations=R.style.CalcAnimation;

        return dialog;

    }



}
