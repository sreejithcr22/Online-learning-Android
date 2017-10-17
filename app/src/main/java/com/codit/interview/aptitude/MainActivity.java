package com.codit.interview.aptitude;

import com.appodeal.ads.Appodeal;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;

import android.support.design.widget.AppBarLayout;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;

import android.support.v7.widget.CardView;

import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.MrecCallbacks;
import com.appodeal.ads.MrecView;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends NavActivityBase
{

    ProgressBar loading;
    int overallProgress=0;
    int aptiProgress=0;
    int gkProgress=0;
    int min=0,sec=0;
    String time;
    String hrTime;
    int hrVal=0;
    int minVal=0;
    int maxTime;
    ScrollView scrollView;

    NativeExpressAdView nativeAd;
    int mockAttempted,mockAvgScore;

    String avgTimetring,totalTimeString,maxTimeString;

    int aptiAttempted,gkAttempted,grandTotal,aptiTotal,gkTotal,correctCount,wrongCount,accuracyRate, totlalTime,avgTime;

    AppBarLayout layout;



    CircleProgressBar overallProgressBar,accuracyProgressbar,aptiProgressbar,gkProgressbar,mockProgressbar;
    CardView overallCard,successCard,timeCard,aptiCard,gkCard,mockCard;

    TextView overallText;
    TextView successText;
    TextView timeText;
    TextView aptiText;
    TextView gkText,mockLabel1,mockLabel2,mockLabel3;
    TextView mockText,accurcyLabel1,accurcyLabel2,accurcyLabel3,timeLabel1,timeLabel2,timeLabel3,aptiLabel1,aptiLabel2,aptiLabel3,gkLabel1,gkLabel2,gkLabel3;





    SharedPreferences progressPreference;

    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView overallQueText;
    private TextView overallAttemptedText;
    private TextView aptiTotalText;
    private TextView aptiAttemptedText,overallLabel1,overallLabel2,overallLabel3;
    private TextView gkTotalText;
    private TextView gkAttemptedText;
    private TextView successCorrectText;
    private TextView successWrongText;
    private TextView totalTimeText;
    private TextView maxTimeText;
    private TextView mockCompleted;
    private TextView timeCardText;
    private TextView mockAvgScoreText;

    public MainActivity()
        {
           this.currentActivity=MAIN_ACTIVITY;
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent close=getIntent();
        if(close.getStringExtra("close")!=null) {
            if (close.getStringExtra("close").equals("true")) {
                finish();
            }

        }


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getBaseContext());
        progressPreference=getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);
        int version = 0;
        try {
            version = getPackageManager().getPackageInfo("com.google.android.gms", 0 ).versionCode;

            if(version<10298000) {
                APPSTATE.GOOGLE_PLAY_REQ_VERSION=false;
                Toast.makeText(this, "Update google play services to the latest version for a better experience !", Toast.LENGTH_LONG).show();
            }
            else
            {

                if(progressPreference.getInt("visitCount",0)==0) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int density = metrics.densityDpi;


                    String densityString = "cannot determine";
                    if (density == DisplayMetrics.DENSITY_XXHIGH) {

                        densityString = "xxhdpi";
                    } else if (density == DisplayMetrics.DENSITY_XHIGH) {
                        densityString = "xhdpi";

                    } else if (density == DisplayMetrics.DENSITY_HIGH) {
                        densityString = "hdpi";

                    } else if (density == DisplayMetrics.DENSITY_MEDIUM) {

                        densityString = "mdpi";
                    } else if (density == DisplayMetrics.DENSITY_LOW) {

                        densityString = "ldpi";
                    }


                    Configuration config = getResources().getConfiguration();


                    mFirebaseAnalytics.setUserProperty("display_density", densityString);
                    mFirebaseAnalytics.setUserProperty("small_width", String.valueOf(config.smallestScreenWidthDp));
                    mFirebaseAnalytics.setUserProperty("resolution_width", String.valueOf(metrics.widthPixels));
                    mFirebaseAnalytics.setUserProperty("resolution_height", String.valueOf(metrics.heightPixels));
                }

            }


        } catch (PackageManager.NameNotFoundException e) {

            APPSTATE.GOOGLE_PLAY_REQ_VERSION=false;
            Toast.makeText(this, "Update google play services to the latest version for a better experience !", Toast.LENGTH_LONG).show();
        }



        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(5);




        super.onCreate(savedInstanceState);

        progressPreference=getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);
        if(progressPreference.getInt("visitCount",0)==0)
        {
            scheduleAlarm();
        }




        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);





        MainLoader loader=new MainLoader();
        loader.execute();


        /*==================================================================================================================================*/



    }


    public void showMrec()
    {
        if(Appodeal.isLoaded(Appodeal.MREC))
        {
            Appodeal.show(MainActivity.this, Appodeal.MREC);
        }

        Log.d(TAG, "showMrec: ");
        Appodeal.setMrecViewId(R.id.appodealMrecView);
        Appodeal.setLogLevel(com.appodeal.ads.utils.Log.LogLevel.debug);
        Appodeal.disableLocationPermissionCheck();
        Appodeal.initialize(MainActivity.this, App.APP_KEY, Appodeal.MREC);




        Appodeal.setMrecCallbacks(new MrecCallbacks() {
            @Override
            public void onMrecLoaded(boolean isPrecache) {

                Log.d(TAG, "onMrecLoaded: ");
                if(!App.isAdRemoved()&&App.preferences.getInt("visitCount",0)>=1) {

                    Log.d(TAG, "onMrecLoaded: inside");
                    Appodeal.show(MainActivity.this, Appodeal.MREC);
                }


            }
            @Override
            public void onMrecFailedToLoad() {
                Log.d(TAG, "onMrecFailedToLoad: ");

            }
            @Override
            public void onMrecShown() {
                Log.d(TAG, "onMrecShown: ");
                findViewById(R.id.appodealMrecView).setVisibility(View.VISIBLE);

            }
            @Override
            public void onMrecClicked() {
            }
        });

    }

    @Override
    public void onBackPressed() {

        onBack();

    }


   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

      switch(id)
      {
          case R.id.settings: drawer.openDrawer(settingsNav);
              return true;


      }


        return super.onOptionsItemSelected(item);
    }










    public String convertToTime(int time)
    {
        int sec=0,min=0,hr=0;

        int val=(time/60);

        if(val>=60)
        {
            hr=val/60;
            min=val%60;
            sec=time%60;
        }
        else if(val>=1&&val<60)
        {
            hr=0;
            min=val;
            sec=time%60;
        }
        else
        {
            sec=time;
        }

        String hour,minute,second;

        if(String.valueOf(hr).length()==1)
        {
            hour="0"+String.valueOf(hr);
        }
        else
        {
            hour=String.valueOf(hr);
        }

        if(String.valueOf(min).length()==1)
        {
            minute="0"+String.valueOf(min);
        }
        else
        {
            minute=String.valueOf(min);
        }

        if(String.valueOf(sec).length()==1)
        {
            second="0"+String.valueOf(sec);
        }
        else
        {
            second=String.valueOf(sec);
        }

        return hour+":"+minute+":"+second;
    }




    public void scheduleAlarm()
    {


        AlarmManager alarmMgr;
         PendingIntent alarmIntent;

        alarmMgr = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);




        Calendar calendar = Calendar.getInstance();
       calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);


        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , alarmIntent);

    }

    class MainLoader extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressPreference=getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);

            initialize(0);

            showMrec();

            scrollView= (ScrollView) findViewById(R.id.mainScrollview);
            loading= (ProgressBar) findViewById(R.id.loading);
            loading.setVisibility(View.VISIBLE);


            overallCard= (CardView) findViewById(R.id.overallCard);
            successCard=(CardView)findViewById(R.id.successCard);
            timeCard=(CardView)findViewById(R.id.timeCard);
            aptiCard=(CardView)findViewById(R.id.aptiCard);
            gkCard=(CardView)findViewById(R.id.gkCard);
            mockCard=(CardView)findViewById(R.id.mockCard);




              /*nativeAd=new NativeExpressAdView(getBaseContext());
              nativeAd.setAdListener(new AdListener() {
                  @Override
                  public void onAdLoaded() {
                      super.onAdLoaded();
                      adCard.setVisibility(View.VISIBLE);
                  }
              });

              LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
              params1.setMargins(0,0,0,0);
              nativeAd.setLayoutParams(params1);

              adCard.addView(nativeAd);

              overallCard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                  @Override
                  public void onGlobalLayout() {



                      int width = overallCard.getWidth();
                      int height = overallCard.getHeight();


                      float density=getResources().getDisplayMetrics().density;
                      int actualWidth= (int) (width/ density);
                      int actualHeight= (int) (height/density);
                      int adWidth=actualWidth-10;
                      int adHeight=actualHeight-11;

                      if(adWidth<280)
                          adWidth=280;

                      if (adHeight<80)
                          adHeight=80;

                      nativeAd.setAdSize(new AdSize(adWidth,adHeight));
                      nativeAd.setAdUnitId(getResources().getString(R.string.home_large));


                      AdRequest adRequest = new AdRequest.Builder()
                              .build();

                      if(!App.isAdRemoved())
                          nativeAd.loadAd(adRequest);

                      if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                          adCard.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                      else
                          adCard.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                  }
              });*/







            //titles
            TextView overallCardTitle=(TextView) overallCard.findViewById(R.id.cardTitle);
            TextView successCardTitle=(TextView)successCard.findViewById(R.id.cardTitle);
            TextView timeCardRtitle=(TextView)timeCard.findViewById(R.id.cardTitle);
            TextView aptiCardTitle=(TextView)aptiCard.findViewById(R.id.cardTitle);
            TextView gkCardTitle=(TextView)gkCard.findViewById(R.id.cardTitle);
            TextView mockCardTitle=(TextView)mockCard.findViewById(R.id.cardTitle);

            overallCardTitle.setText("Overall Progress");
            successCardTitle.setText("Accuracy");
            timeCardRtitle.setText("Time");
            aptiCardTitle.setText("Aptitude");
            gkCardTitle.setText("GK");
            mockCardTitle.setText("Mock Tests");

            //values
            overallText= (TextView) overallCard.findViewById(R.id.textView1);
             overallQueText= (TextView) overallCard.findViewById(R.id.textView2);
             overallAttemptedText=(TextView)overallCard.findViewById(R.id.textView3);

            aptiText=(TextView)aptiCard.findViewById(R.id.textView1) ;
             aptiTotalText= (TextView) aptiCard.findViewById(R.id.textView2);
             aptiAttemptedText=(TextView)aptiCard.findViewById(R.id.textView3);

            gkText=(TextView)gkCard.findViewById(R.id.textView1);
             gkTotalText=(TextView)gkCard.findViewById(R.id.textView2);
             gkAttemptedText=(TextView)gkCard.findViewById(R.id.textView3);

            successText=(TextView) successCard.findViewById(R.id.textView1);
             successCorrectText=(TextView)successCard.findViewById(R.id.textView2);
             successWrongText=(TextView)successCard.findViewById(R.id.textView3);

            timeText=(TextView)timeCard.findViewById(R.id.textView1);
             totalTimeText= (TextView) timeCard.findViewById(R.id.textView2);
             maxTimeText= (TextView) timeCard.findViewById(R.id.textView3);

            mockText=(TextView)mockCard.findViewById(R.id.textView1) ;
             mockCompleted=(TextView)mockCard.findViewById(R.id.textView2) ;
             mockAvgScoreText=(TextView)mockCard.findViewById(R.id.textView3) ;

            //labels
             overallLabel1=(TextView)overallCard.findViewById(R.id.label1);
             overallLabel2=(TextView)overallCard.findViewById(R.id.label2);
             overallLabel3=(TextView)overallCard.findViewById(R.id.label3);

             accurcyLabel1=(TextView)successCard.findViewById(R.id.label1);
             accurcyLabel2=(TextView)successCard.findViewById(R.id.label2);
             accurcyLabel3=(TextView)successCard.findViewById(R.id.label3);

             timeLabel1=(TextView)timeCard.findViewById(R.id.label1);
             timeLabel2=(TextView)timeCard.findViewById(R.id.label2);
             timeLabel3=(TextView)timeCard.findViewById(R.id.label3);

             aptiLabel1=(TextView)aptiCard.findViewById(R.id.label1);
             aptiLabel2=(TextView)aptiCard.findViewById(R.id.label2);
             aptiLabel3=(TextView)aptiCard.findViewById(R.id.label3);

             gkLabel1=(TextView)gkCard.findViewById(R.id.label1);
             gkLabel2=(TextView)gkCard.findViewById(R.id.label2);
             gkLabel3=(TextView)gkCard.findViewById(R.id.label3);

             mockLabel1=(TextView)mockCard.findViewById(R.id.label1);
             mockLabel2=(TextView)mockCard.findViewById(R.id.label2);
             mockLabel3=(TextView)mockCard.findViewById(R.id.label3);


//set labels
            overallLabel1.setText("Progress");
            overallLabel2.setText("Questions");
            overallLabel3.setText("Attempted");

            accurcyLabel1.setText("Accuracy");
            accurcyLabel2.setText("Correct");
            accurcyLabel3.setText("Wrong");

            timeLabel1.setText("Average");
            timeLabel2.setText("Total");
            timeLabel3.setText("Highest");

            aptiLabel1.setText("Progress");
            aptiLabel2.setText("Questions");
            aptiLabel3.setText("Attempted");

            gkLabel1.setText("Progress");
            gkLabel2.setText("Questions");
            gkLabel3.setText("Attempted");

            mockLabel1.setText("Tests");
            mockLabel2.setText("Completed");
            mockLabel3.setText("Avg Score");


            overallProgressBar=(CircleProgressBar)overallCard.findViewById(R.id.circle);
            accuracyProgressbar=(CircleProgressBar)successCard.findViewById(R.id.circle);
            aptiProgressbar=(CircleProgressBar)aptiCard.findViewById(R.id.circle);
            gkProgressbar =(CircleProgressBar)gkCard.findViewById(R.id.circle);
            mockProgressbar=(CircleProgressBar)mockCard.findViewById(R.id.circle);
             timeCardText= (TextView) timeCard.findViewById(R.id.timeCardText);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            scrollView.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            //set values
            overallText.setText(overallProgress+"%");
            overallQueText.setText(String.valueOf(grandTotal));
            overallAttemptedText.setText(String.valueOf(aptiAttempted+gkAttempted));
            overallText.setText(String.valueOf(overallProgress)+"%");

            successText.setText(accuracyRate+"%");
            successCorrectText.setText(String.valueOf(correctCount));
            successWrongText.setText(String.valueOf(wrongCount));
            accuracyProgressbar.setProgress(accuracyRate);

            timeText.setText(avgTimetring);
            totalTimeText.setText(totalTimeString);
            maxTimeText.setText(maxTimeString);
            timeCardText.setText(totalTimeString);

            aptiText.setText(String.valueOf(aptiProgress)+"%");
            aptiTotalText.setText(String.valueOf(String.valueOf(APPSTATE.APTI_QUE_COUNT)));
            aptiAttemptedText.setText(String.valueOf(aptiAttempted));
            aptiProgressbar.setProgress(aptiProgress);

            gkText.setText(String.valueOf(gkProgress)+"%");
            gkTotalText.setText(String.valueOf(APPSTATE.GK_QUE_COUNT));
            gkAttemptedText.setText(String.valueOf(gkAttempted));
            gkProgressbar.setProgress(gkProgress);

            mockText.setText("10");
            mockCompleted.setText(String.valueOf(mockAttempted));
            mockAvgScoreText.setText(String.valueOf(mockAvgScore));
            mockProgressbar.setProgress(mockAttempted*10);

            final Handler handler=new Handler();
            final int[] progressInt = {0};


            new Thread(new Runnable() {
                @Override
                public void run() {


                    while (progressInt[0] <=overallProgress)
                    {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if( progressInt[0]<=overallProgress) {
                                    overallProgressBar.setProgress(progressInt[0]);
                                }
                                progressInt[0]++;

                            }
                        });


                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();




            if(APPSTATE.CURRENT_THEME!=APPSTATE.THEME_BLACK)
                toolbar.setBackgroundColor(Color.parseColor("#43a047"));




            //show review dialog
            progressPreference=getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);

            if(APPSTATE.reviewFlag==false&&progressPreference.getBoolean("reviewed",false)==false)
            reviewDialog();


           try
           {
               if(progressPreference.getInt("visitCount",0)==1) {
                   if (!drawer.isDrawerOpen(menuView)&&!APPSTATE.DRAWER_FLAG) {
                       drawer.openDrawer(menuView);
                       APPSTATE.DRAWER_FLAG=true;
                   }
               }

           }
           catch (Exception e)
           {
               FirebaseCrash.log("cant open drawer on first open");
               FirebaseCrash.report(e);
           }

        }

        @Override
        protected Void doInBackground(Void... voids) {

            progressPreference=getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);


            mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
            mFirebaseAnalytics.setMinimumSessionDuration(300);


            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    UserStateDB userStateDB=new UserStateDB(getBaseContext());

                    userStateDB.readAptiCategs();

                    ArrayList<MockRow> mockRows=userStateDB.readMockRows();

                    int totalScore=0;
                    for (MockRow row:mockRows) {
                        totalScore=totalScore+row.score;

                        if(row.isFinished)
                        {
                            mockAttempted++;

                        }
                    }

                    if(mockAttempted!=0)
                    {
                        mockAvgScore = totalScore / mockAttempted;

                    }
                    else {
                        mockAvgScore=0;
                    }


                }
            });
            thread.start();



            int visitCount=progressPreference.getInt("visitCount",0);
            if(APPSTATE.GOOGLE_PLAY_REQ_VERSION)
            mFirebaseAnalytics.setUserProperty("open_count",String.valueOf(visitCount));


            if(APPSTATE.visitFlag==false)
            {
                progressPreference.edit().putInt("visitCount",visitCount+1).apply();
                APPSTATE.visitFlag=true;


            }

            aptiAttempted=progressPreference.getInt("APTI_ATTEMPTED",0);
            gkAttempted=progressPreference.getInt("GK_ATTEMPTED",0);

            correctCount=progressPreference.getInt("CORRECT",0);
            wrongCount=progressPreference.getInt("WRONG",0);

            if((correctCount+wrongCount)!=0)
            {
                accuracyRate = (correctCount * 100) / (correctCount + wrongCount);
            }
            else
            {
                accuracyRate=0;
            }
            mFirebaseAnalytics.setUserProperty("accuracy",String.valueOf(accuracyRate));


            totlalTime=progressPreference.getInt("TIME",0);
            maxTime=progressPreference.getInt("max_time",0);

            mFirebaseAnalytics.setUserProperty("total_time",String.valueOf(totlalTime));

            if(aptiAttempted!=0) {
                avgTime = totlalTime / aptiAttempted;


            }
            else
            {
                avgTime=0;
            }
            mFirebaseAnalytics.setUserProperty("avg_time",String.valueOf(avgTime));


            grandTotal=APPSTATE.APTI_QUE_COUNT+APPSTATE.GK_QUE_COUNT;

            overallProgress=((aptiAttempted+gkAttempted)*100)/grandTotal;

            aptiProgress=(aptiAttempted*100)/APPSTATE.APTI_QUE_COUNT;
            gkProgress=(gkAttempted*100)/APPSTATE.GK_QUE_COUNT;

            mFirebaseAnalytics.setUserProperty("total_arrempted",String.valueOf(overallProgress));
            mFirebaseAnalytics.setUserProperty("apti_attempted",String.valueOf(aptiProgress));

            mFirebaseAnalytics.setUserProperty("gk_attempted",String.valueOf(gkProgress));
            mFirebaseAnalytics.setUserProperty("mock_count",String.valueOf(mockAttempted));



            String max=convertToTime(maxTime);
            String maxTime[]=max.split(":");
            int maxMin=Integer.valueOf(maxTime[1]);
            int maxSec=Integer.valueOf(maxTime[2]);
            maxTimeString=String.format("%02d",maxMin)+":"+String.format("%02d",maxSec);


            time=convertToTime(avgTime);

            String timeString[]= time.split(":");
            min=Integer.valueOf(timeString[1]);
            sec=Integer.valueOf(timeString[2]);
            avgTimetring=String.format("%02d",min)+":"+String.format("%02d",sec);

            hrTime=convertToTime(totlalTime);
            String hrTimeString[]= hrTime.split(":");
            hrVal=Integer.valueOf(hrTimeString[0]);
            minVal=Integer.valueOf(hrTimeString[1]);

            totalTimeString= hrVal+"h"+" "+minVal+"m";







            return null;
        }
    }

    public boolean isConnected()
    {
        ConnectivityManager cm =
                (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean connected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return connected;
    }

    public void reviewDialog()
    {

        APPSTATE.reviewFlag=true;
        if(progressPreference.getInt("visitCount",0)%5==0)
        {
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Love the App ?")
                    .setMessage("Please spend a moment to Review the app on play store.")
                    .setPositiveButton("Review", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if(isConnected())
                            {
                                progressPreference=getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);
                                progressPreference.edit().putBoolean("reviewed",true).apply();
                            }

                            openStore();
                        }
                    })
                    .setNegativeButton("Not now",null)
                    .create();

            final Handler reviewHandler=new Handler();
            Thread review=new Thread(new Runnable()
            {
                @Override
                public void run() {

                    reviewHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    },1500);
                }
            });

            review.start();
        }
    }
}
