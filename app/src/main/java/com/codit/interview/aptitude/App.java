package com.codit.interview.aptitude;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * Created by Sreejith on 01-Oct-17.
 */

public class App extends Application {




    static SharedPreferences preferences;
    public static final String APP_KEY="a9e3e5ec7a3264b5afa303523979a060cfa9362658273c8b";
    static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("debug", "onCreate: ");
        context=getApplicationContext();

        AdHelper.initialize(getContext());
        preferences=context.getSharedPreferences("progress", Context.MODE_PRIVATE);



    }

    public static Context getContext() {
        return context;
    }

    public static void removeAds()
    {
        preferences.edit().putBoolean("remove_ads",true).apply();

    }

    public static boolean isAdRemoved()
    {
        return preferences.getBoolean("remove_ads",false);
    }


}
