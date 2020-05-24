package com.codit.interview.aptitude;

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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.crash.FirebaseCrash;

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
    final int[] actualWidth = new int[1];
    final int[] actualHeight = new int[1];
    boolean adFlag=false;


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

        if(currentRow.isAd)
        {
            final CardView card = (CardView) inflater.inflate(R.layout.mock_ad_row, parent, false);

              final NativeExpressAdView adView=new NativeExpressAdView(getContext());
            card.addView(adView,card.getLayoutParams());
            adView.setVisibility(View.GONE);

           if (  progressPreference.getInt("mock_ad_width",0)== 0) {

                card.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressLint("NewApi")
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {

                        int width = card.getWidth();
                        int height = card.getHeight();


                        float density = context.getResources().getDisplayMetrics().density;
                        actualWidth[0] = (int) (width / density);
                        actualHeight[0] = (int) (height / density);

                        progressPreference.edit().putInt("mock_ad_width",actualWidth[0] - 3).apply();
                        progressPreference.edit().putInt("mock_ad_height",actualHeight[0] - 3).apply();


                        card.setRadius(0);
                        card.setPreventCornerOverlap(false);
                        card.setUseCompatPadding(true);

                        card.setContentPadding(-card.getPaddingLeft() - 1, -card.getPaddingRight() - 1, -card.getPaddingTop() - 1, -card.getPaddingTop() - 1);


                        adView.setAdSize(new AdSize(progressPreference.getInt("mock_ad_width",0), progressPreference.getInt("mock_ad_height",0)));

                        adView.setAdUnitId(context.getString(R.string.apti_recycler_1));

                        adView.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                card.findViewById(R.id.ad_loading_text).setVisibility(View.GONE);
                                adView.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);

                                try
                                {
                                    if(adFlag==false) {
                                        adFlag=true;
                                        remove(objects.get(5));
                                        remove(objects.get(9));
                                    }

                                }
                                catch (IndexOutOfBoundsException e)
                                {
                                    FirebaseCrash.log("remove ad row pos="+position+" , array out of bound");
                                    FirebaseCrash.report(e);
                                }
                            }
                        });
                        AdRequest adRequest = new AdRequest.Builder()
                                .build();

                            adView.loadAd(adRequest);


                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                            card.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        else
                            card.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });


            }



            else
            {
                card.setRadius(0);
                card.setPreventCornerOverlap(false);
                card.setUseCompatPadding(true);

                card.setContentPadding(-card.getPaddingLeft() - 1, -card.getPaddingRight() - 1, -card.getPaddingTop() - 1, -card.getPaddingTop() - 1);


                adView.setAdSize(new AdSize(progressPreference.getInt("mock_ad_width",0), progressPreference.getInt("mock_ad_height",0)));

                adView.setAdUnitId(context.getString(R.string.apti_recycler_1));

               adView.setAdListener(new AdListener() {
                   @Override
                   public void onAdLoaded() {
                       super.onAdLoaded();
                       card.findViewById(R.id.ad_loading_text).setVisibility(View.GONE);
                       adView.setVisibility(View.VISIBLE);

                   }

                   @Override
                   public void onAdFailedToLoad(int i) {
                       super.onAdFailedToLoad(i);

                       try
                           {
                               if(adFlag==false) {
                                   adFlag=true;
                                   remove(objects.get(3));
                                   remove(objects.get(8));
                               }

                           }
                       catch (IndexOutOfBoundsException e)
                       {
                           FirebaseCrash.log("remove ad row pos="+position+" , array out of bound");
                           FirebaseCrash.report(e);
                       }
                   }
               });
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .addTestDevice("7DB37A8E11E63AFA1EE5F5E6D9632407")
                        .build();

                adView.loadAd(adRequest);



            }





            return card;
        }

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

class MockRow
{

    String mockTitle;
    int score;
    boolean isFinished;

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }

    boolean isAd;

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    boolean isLocked;

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public MockRow(int score, String mockTitle, boolean isFinished, boolean isLocked) {
        this.score = score;
        this.mockTitle = mockTitle;
        this.isFinished=isFinished;
        this.isLocked=isLocked;
    }

    public MockRow()
    {

    }

    public String getMockTitle() {
        return mockTitle;
    }

    public void setMockTitle(String mockTitle) {
        this.mockTitle = mockTitle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
