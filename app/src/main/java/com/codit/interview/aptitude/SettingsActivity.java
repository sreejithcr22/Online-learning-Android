package com.codit.interview.aptitude;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.settings_layout);
        Toolbar toolbar= (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);

        Drawable back;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            back=getResources().getDrawable(R.drawable.ic_back_arrow);
        }
        else
        {
            back=getResources().getDrawable(R.drawable.ic_back);
        }

        toolbar.setNavigationIcon(back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getFragmentManager().beginTransaction()
      .replace(R.id.settings_content, new SettingsFragment())
        .commit();




    }

    public static class SettingsFragment extends PreferenceFragment {


        @Override
        public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);


           // Load the preferences from an XML resource
           addPreferencesFromResource(R.xml.settings);






            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

            if(sharedPref.getBoolean("note_save",true))
            {
                findPreference("note_save").setSummary("Enabled");
            }
            else
            {
                findPreference("note_save").setSummary("Disabled");
            }

            if(sharedPref.getBoolean("calc_save",true))
            {
                findPreference("calc_save").setSummary("Enabled");
            }
            else
            {
                findPreference("calc_save").setSummary("Disabled");
            }

            if(sharedPref.getBoolean("timer_alert",true))
            {
                findPreference("timer_alert").setSummary("Enabled");
            }
            else
            {
                findPreference("timer_alert").setSummary("Disabled");
            }


            if(sharedPref.getBoolean("timeup_vibrate",true))
            {
                findPreference("timeup_vibrate").setSummary("Enabled");
            }
            else
            {
                findPreference("timeup_vibrate").setSummary("Disabled");
            }



            sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

                    switch (s)
                    {
                        case "note_save":
                            if(sharedPreferences.getBoolean(s,true))
                        {
                            findPreference(s).setSummary("Enabled");
                        }
                            else
                            {

                                findPreference(s).setSummary("Disabled");
                            }
                            break;
                        case "calc_save":
                            if(sharedPreferences.getBoolean(s,true))
                            {
                                findPreference(s).setSummary("Enabled");
                            }
                            else
                            {

                                findPreference(s).setSummary("Disabled");
                            }
                            break;
                        case "timer_alert":

                            if(sharedPreferences.getBoolean(s,true))
                            {
                                findPreference(s).setSummary("Enabled");
                            }
                            else
                            {

                                findPreference(s).setSummary("Disabled");
                            }
                            break;
                        case "timeup_vibrate":
                            if(sharedPreferences.getBoolean(s,true))
                            {
                                findPreference(s).setSummary("Enabled");
                            }
                            else
                            {

                                findPreference(s).setSummary("Disabled");
                            }
                            break;

                    }
                }
            });


            String time=sharedPref.getString("mock_time","35:00");
            String[] times=time.split(":");
            if(times[0].length()==1)
            {
                times[0]="0"+times[0];
            }
            if (times[1].length()==1)
            {
                times[1]="0"+times[1];
            }
            findPreference("mock_time").setSummary(times[0]+":"+times[1]);


            findPreference("mock_time").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    View titleView=getActivity().getLayoutInflater().inflate(R.layout.dialog_title,null);
                    TextView title=(TextView)titleView.findViewById(R.id.dialogTitle);
                    ImageView icon=(ImageView)titleView.findViewById(R.id.dialogIcon);
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_stopwatch));
                    title.setText("Timer");
                    builder.setCustomTitle(titleView);


                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

                    String time=sharedPref.getString("mock_time","35:00");
                    String[] times=time.split(":");
                    final int min=Integer.parseInt(times[0]);
                    final int sec=Integer.parseInt(times[1]);


                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    //notes body
                    View view = inflater.inflate(R.layout.timer_body, null);

                    final NumberPicker minutesPicker=(NumberPicker) view.findViewById(R.id.minutesPicker);


                    minutesPicker.setMinValue(1);
                    minutesPicker.setMaxValue(60);
                    minutesPicker.setFormatter(new NumberPicker.Formatter() {
                        @Override
                        public String format(int i) {
                            return String.format("%02d",i);
                        }
                    });
                    minutesPicker.setValue(min);
                    minutesPicker.setSelected(false);
                    minutesPicker.setActivated(false);

                    final NumberPicker secondsPicker=(NumberPicker)view.findViewById(R.id.secondsPicker);
                    secondsPicker.setFormatter(new NumberPicker.Formatter() {
                        @Override
                        public String format(int i) {
                            return String.format("%02d",i);
                        }
                    });
                    secondsPicker.setMinValue(00);
                    secondsPicker.setMaxValue(59);

                    secondsPicker.setValue(sec);
                    builder.setView(view);
                    builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //sendTime(minutesPicker,secondsPicker);
                            sharedPref.edit().putString("mock_time",String.valueOf(minutesPicker.getValue())+":"+String.valueOf(secondsPicker.getValue())).commit();
                                    String time=String.valueOf(minutesPicker.getValue())+":"+String.valueOf(secondsPicker.getValue());
                            String[] times=time.split(":");
                            if(times[0].length()==1)
                            {
                                times[0]="0"+times[0];
                            }
                            if (times[1].length()==1)
                            {
                                times[1]="0"+times[1];
                            }
                            findPreference("mock_time").setSummary(times[0]+":"+times[1]);
                        }
                    });
                    builder.setNegativeButton("CANCEL",null);



                     builder.create().show();


                    return true;
                }
            });


            String timerTime=sharedPref.getString("timer_time","04:00");
            String[] timerTimes=timerTime.split(":");
            if(timerTimes[0].length()==1)
            {
                timerTimes[0]="0"+timerTimes[0];
            }
            if (timerTimes[1].length()==1)
            {
                timerTimes[1]="0"+timerTimes[1];
            }
            findPreference("timer_time").setSummary(timerTimes[0]+":"+timerTimes[1]);


            findPreference("timer_time").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    View titleView=getActivity().getLayoutInflater().inflate(R.layout.dialog_title,null);
                    TextView title=(TextView)titleView.findViewById(R.id.dialogTitle);
                    ImageView icon=(ImageView)titleView.findViewById(R.id.dialogIcon);
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_stopwatch));
                    title.setText("Timer");
                    builder.setCustomTitle(titleView);


                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

                    String time=sharedPref.getString("timer_time","02:00");

                    String[] times=time.split(":");
                    final int min=Integer.parseInt(times[0]);
                    final int sec=Integer.parseInt(times[1]);


                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    //notes body
                    View view = inflater.inflate(R.layout.timer_body, null);

                    final NumberPicker minutesPicker=(NumberPicker) view.findViewById(R.id.minutesPicker);


                    minutesPicker.setMinValue(0);
                    minutesPicker.setMaxValue(15);
                    minutesPicker.setFormatter(new NumberPicker.Formatter() {
                        @Override
                        public String format(int i) {
                            return String.format("%02d",i);
                        }
                    });
                    minutesPicker.setValue(min);
                    minutesPicker.setSelected(false);
                    minutesPicker.setActivated(false);

                    final NumberPicker secondsPicker=(NumberPicker)view.findViewById(R.id.secondsPicker);
                    secondsPicker.setFormatter(new NumberPicker.Formatter() {
                        @Override
                        public String format(int i) {
                            return String.format("%02d",i);
                        }
                    });
                    secondsPicker.setMinValue(00);
                    secondsPicker.setMaxValue(59);

                    secondsPicker.setValue(sec);
                    builder.setView(view);
                    builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String oldTime=sharedPref.getString("timer_time","02:00");
                            String time=String.valueOf(minutesPicker.getValue())+":"+String.valueOf(secondsPicker.getValue());

                            String[] times=time.split(":");
                            if(times[0].length()==1)
                            {
                                times[0]="0"+times[0];
                            }
                            if (times[1].length()==1)
                            {
                                times[1]="0"+times[1];
                            }
                            findPreference("timer_time").setSummary(times[0]+":"+times[1]);

                            time=times[0]+":"+times[1];
                            sharedPref.edit().putString("timer_time",time).commit();
                            if(!oldTime.equals(time))
                            {
                                UserStateDB userStateDB=new UserStateDB(getActivity());
                                userStateDB.changeDefaultTime(oldTime,time);
                            }
                        }
                    });
                    builder.setNegativeButton("CANCEL",null);



                    builder.create().show();


                    return true;
                }
            });

           }

    }
}
