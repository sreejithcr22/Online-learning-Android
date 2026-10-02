package com.codit.interview.aptitude.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.codit.interview.aptitude.MainActivity
import com.codit.interview.aptitude.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/** Schedules the 21:00 "keep your streak" notification via [AlarmManager]. */
@Singleton
class DailyReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun scheduleDailyReminder(hourOfDay: Int = DEFAULT_HOUR) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        val pendingIntent = reminderPendingIntent()

        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            next.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent,
        )
    }

    private fun reminderPendingIntent(): PendingIntent = PendingIntent.getBroadcast(
        context,
        REQUEST_CODE,
        Intent(context, DailyReminderReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    private companion object {
        const val DEFAULT_HOUR = 21
        const val REQUEST_CODE = 1001
    }
}

/** Posts the daily practice reminder. */
class DailyReminderReceiver : android.content.BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != null && intent.action != ACTION_REMIND) return
        val appContext = context.applicationContext
        val notificationManager = appContext.getSystemService(NotificationManager::class.java)
            ?: return
        notificationManager.createNotificationChannel(channel(appContext))

        val launchIntent = Intent(appContext, MainActivity::class.java)
        val contentIntent = TaskStackBuilder.create(appContext)
            .addNextIntent(launchIntent)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val progress = ProgressReader(appContext).overallPercent()

        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setVibrate(longArrayOf(400))
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentTitle(
                if (progress > 0) "You have made $progress% progress !" else PROMOTIONAL_TITLE
            )
            .setContentText(PROMOTIONAL_MESSAGE)
            .setContentIntent(contentIntent)
            .apply { if (progress > 0) setProgress(100, progress, false) }
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun channel(context: Context): NotificationChannel =
        NotificationChannel(
            CHANNEL_ID,
            "Daily Reminder",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Reminds you to keep practising aptitude and GK"
        }

    companion object {
        const val ACTION_REMIND = "com.codit.interview.aptitude.DAILY_REMINDER"
        const val CHANNEL_ID = "daily_reminder"
        const val NOTIFICATION_ID = 5

        private const val PROMOTIONAL_TITLE = "1500+ solved questions and 250+ interview tips"
        private const val PROMOTIONAL_MESSAGE = "Tap to explore more."
    }
}

/** Re-arms the daily reminder after a reboot, which clears all alarms. */
class BootCompletedReceiver : android.content.BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return
        DailyReminderScheduler(context.applicationContext).scheduleDailyReminder()
    }
}

/** Reads just enough state to build the notification's progress bar. */
internal class ProgressReader(context: Context) {

    private val preferences =
        context.getSharedPreferences("progress", Context.MODE_PRIVATE)

    fun overallPercent(): Int {
        val attempted =
            preferences.getInt("QUANTI_ATTEMPTED", 0) +
                preferences.getInt("LOGIC_ATTEMPTED", 0) +
                preferences.getInt("VERBAL_ATTEMPTED", 0) +
                preferences.getInt("GK_ATTEMPTED", 0)
        // Total content shipped in master.db, minus the mock tests (which have their
        // own counters) and the two info-only GK topics.
        val total = PRACTICE_QUESTION_TOTAL
        return if (total == 0) 0 else attempted * 100 / total
    }

    private companion object {
        const val PRACTICE_QUESTION_TOTAL = 1310
    }
}
