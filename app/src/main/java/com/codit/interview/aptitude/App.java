package com.codit.interview.aptitude;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.appodeal.ads.Appodeal;


/**
 * Created by Sreejith on 01-Oct-17.
 */

public class App extends MultiDexApplication {

    static SharedPreferences preferences;
    public static final String APP_KEY="a9e3e5ec7a3264b5afa303523979a060cfa9362658273c8b";
    static Context context;



    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("debug", "onCreate: ");
        context=getApplicationContext();

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
