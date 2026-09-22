package com.codit.interview.aptitude.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Kept for manifest compatibility. Actual notification logic lives in
 * {@link AlarmReceiver#showNotification(android.content.Context)} to comply
 * with Android 8+ background execution limits (no background startService).
 */
public class RemoteConfigService extends Service {

    public RemoteConfigService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmReceiver.showNotification(this);
        stopSelf(startId);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
