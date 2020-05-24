package com.codit.interview.aptitude;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;




public class RemoteConfigService extends IntentService {


   SharedPreferences progressPreference;


    public RemoteConfigService() {
        super("RemoteConfigService");
    }




    @Override
    protected void onHandleIntent(Intent intent) {

        showNotification();

    }


    public  void showNotification()
    {

        progressPreference=getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);
        int grandTotal=APPSTATE.APTI_QUE_COUNT+APPSTATE.GK_QUE_COUNT;

       int  aptiAttempted=progressPreference.getInt("APTI_ATTEMPTED",0);
        int gkAttempted=progressPreference.getInt("GK_ATTEMPTED",0);
        int overallProgress=((aptiAttempted+gkAttempted)*100)/grandTotal;


        String title;
        String message;

        if(overallProgress!=0) {
            title = "You have made " + overallProgress + "%" + " progress !";
        }
        else {
            title=null;
        }
        message="Tap to explore more.";
        Intent resultIntent = new Intent(this, MainActivity.class);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,PendingIntent.FLAG_UPDATE_CURRENT);




        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        androidx.core.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)



                        .setVibrate(new long[400])
                        .setAutoCancel(true)

                        .setSound(uri)
                ;

        if(title!=null)
        {

            mBuilder .setProgress(100,overallProgress,false)
                .setContentTitle(title)
                    .addAction(new androidx.core.app.NotificationCompat.Action(R.mipmap.ic_launcher,"CONTINUE",resultPendingIntent));


        }
        else {

            mBuilder.setContentTitle("1500+ solved questions and 250+ interview tips");
            mBuilder.setContentText(message)
                    .addAction(new androidx.core.app.NotificationCompat.Action(R.mipmap.ic_launcher,"OPEN",resultPendingIntent));

        }

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(5, mBuilder.build());

    }








}
