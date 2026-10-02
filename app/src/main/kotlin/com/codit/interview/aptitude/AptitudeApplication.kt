package com.codit.interview.aptitude

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.codit.interview.aptitude.notification.DailyReminderScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application entry point and Hilt root.
 *
 * The daily-practice reminder is scheduled here (and re-armed by
 * [com.codit.interview.aptitude.notification.BootCompletedReceiver]) rather than from
 * an activity, so it survives the user never opening a screen.
 */
@HiltAndroidApp
class AptitudeApplication : MultiDexApplication() {

    @Inject
    lateinit var reminderScheduler: DailyReminderScheduler

    override fun onCreate() {
        super.onCreate()
        reminderScheduler.scheduleDailyReminder()
    }
}
