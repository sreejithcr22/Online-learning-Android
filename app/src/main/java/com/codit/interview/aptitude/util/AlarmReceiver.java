package com.codit.interview.aptitude.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.model.ParentCategory;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    public static void showNotification(Context context) {
        SharedPreferences progressPreference =
                context.getSharedPreferences("progress", Context.MODE_PRIVATE);
        int grandTotal = APPSTATE.APTI_QUE_COUNT + APPSTATE.GK_QUE_COUNT;
        int aptiAttempted = progressPreference.getInt("APTI_ATTEMPTED", 0);
        int gkAttempted = progressPreference.getInt("GK_ATTEMPTED", 0);
        int overallProgress = grandTotal == 0 ? 0
                : ((aptiAttempted + gkAttempted) * 100) / grandTotal;

        String title;
        String message = "Tap to explore more.";
        if (overallProgress != 0) {
            title = "You have made " + overallProgress + "%" + " progress !";
        } else {
            title = "1500+ solved questions and 250+ interview tips";
        }

        String channelId = "daily_reminder";
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mNotificationManager != null) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Daily Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        Intent resultIntent = new Intent(context, ParentCategory.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ParentCategory.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[400])
                        .setAutoCancel(true)
                        .setSound(uri)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(resultPendingIntent);

        if (overallProgress != 0) {
            mBuilder.setProgress(100, overallProgress, false);
        }

        if (mNotificationManager != null) {
            mNotificationManager.notify(5, mBuilder.build());
        }
    }
}
