package com.codit.interview.aptitude;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1=new Intent(context,RemoteConfigService.class);
        context.startService(intent1);
    }
}
