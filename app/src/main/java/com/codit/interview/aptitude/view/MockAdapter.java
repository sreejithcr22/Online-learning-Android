package com.codit.interview.aptitude.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.util.APPSTATE;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Sreejith on 03-Sep-16.
 */
public class MockAdapter extends ArrayAdapter<MockRow> {

    interface mockAdapterInterface
    {
        void goToMockFrag(MockRow obj);
    }
    SharedPreferences progressPreference;

    mockAdapterInterface interfaceObj;
    static ArrayList<MockRow> objects;
    Context context;
    int layoutResourceId;


    public MockAdapter(Context context, int resource, ArrayList objects) {
        super(context, resource, objects);

        this.objects=objects;
        this.context=context;
        layoutResourceId=resource;
        this.interfaceObj=(mockAdapterInterface)context;
        progressPreference=context.getSharedPreferences("progress", Context.MODE_PRIVATE);


    }

    @Override
    public void remove(MockRow object) {
        objects.remove(object);

        MockAdapter.this.notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);



        final MockRow currentRow=objects.get(position);


        convertView = inflater.inflate(layoutResourceId, parent, false);
        CardView row=(CardView)convertView.findViewById(R.id.rowCard);
        TextView title= (TextView) convertView.findViewById(R.id.mockTitle);
        TextView score=(TextView)convertView.findViewById(R.id.score);
        TextView questionCount=(TextView)convertView.findViewById(R.id.questionCount);
        CardView scoreCard=(CardView)convertView.findViewById(R.id.scoreCard);
        TextView status=(TextView)convertView.findViewById(R.id.status);


        if(currentRow.isFinished)
        {
            status.append("Attempted");

            int  color=Color.parseColor("#43a047");
            if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_BLACK)
                color=Color.parseColor("#26C6DA");

            questionCount.setTextColor(color);
            status.setTextColor(color);



        }
        else
        {
            status.append("Not Attempted");

        }

        score.setText(String.valueOf(currentRow.getScore()));
        title.setText(currentRow.getMockTitle());



        scoreCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Score "+String.valueOf(currentRow.getScore())+" out of 25",Toast.LENGTH_SHORT).show();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                APPSTATE.CURRENT_CATEGORY=currentRow.getMockTitle();

                if(currentRow.isFinished)
                {
                    final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("This test is already attempted !")

                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("ATTEMPT AGAIN", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    interfaceObj.goToMockFrag(objects.get(position));
                                }
                            })
                            .setTitle(objects.get(position).getMockTitle())
                            .create().show();
                }

                else
                {


                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

                    String time=sharedPref.getString("mock_time","35:00");
                    String[] times=time.split(":");
                    String time1=times[0]+"m "+times[1]+"s";

                        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setMessage("There are 25 questions and the duration is "+time1+". All the best !")

                                .setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setPositiveButton("START TEST", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        interfaceObj.goToMockFrag(objects.get(position));
                                    }
                                })
                                .create().show();




                }


            }
        });

        return convertView;
    }


}

