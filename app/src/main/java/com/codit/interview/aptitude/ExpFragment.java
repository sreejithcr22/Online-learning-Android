package com.codit.interview.aptitude;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

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

        APPSTATE.EXPL_COUNT++;

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setView(R.layout.exp_fragment);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view=inflater.inflate(R.layout.exp_fragment, null);

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
