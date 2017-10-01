package com.codit.interview.aptitude;

import android.app.Application;
import android.content.Context;
import android.util.Log;


/**
 * Created by Sreejith on 01-Oct-17.
 */

public class App extends Application {





    static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("debug", "onCreate: ");
        context=getApplicationContext();

        AdHelper.initialize(getContext());



    }

    public static Context getContext() {
        return context;
    }


}
