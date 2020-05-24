package com.codit.interview.aptitude;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sreejith on 13-Aug-16.
 */
public class QuestionFragBase extends Fragment implements View.OnClickListener {



    FirebaseAnalytics mFirebaseAnalytics;
    boolean updateTime;
    ArrayList<Integer> timeList=new ArrayList<>();
    int previousQno=0;
    Timer timer,tempTimer;
    TimerTask timerTask,tempTimerTask;
    final Handler handler=new Handler();
    int sec;
    int min;
    int realMin,realSec;
    String secVal,minVal;
    int actualMin,actualSec;

    LinearLayout queParent;

    String currentMockTitle;
    ArrayList<String> attemptedList;
    Boolean nextOrPrevious;

    ArrayAdapter<String> spinnerAdapter;
    Question currentQuestion;
    int previousId;
    int currentQno;
    String currentAns;

    String checkedAnswer;
    String currentChecked;
    int lastQueNo;
    String currentTable;
    TextView questionText,qno;
    RadioGroup radioOptionsGroup;
    RadioButton option1;
    RadioButton option2;
    RadioButton option3;
    RadioButton option4;
    ImageButton buttonFinishTest;
    ImageButton buttonNext,buttonPrevious;
    ImageButton go1,go2,go3,go4;

  public static final String TABLE_GK_FAV="gk_fav_table";
    public static final String TABLE_APTI_FAV="fav_table";

    ImageButton buttonCalc,buttonNotes,buttonShare,buttonFavs,buttonExplain;
    View tool;

    static String FRAG_GK="gk";
    static String FRAG_APTI="apti";
    static String FRAG_GK_FAV="gk_fac";
    static String FRAG_APTI_FAV="apti_fav";
    static String FRAG_MOCK="mock";

    String currentFragment;

    AppCompatSpinner qnoSpinner;

    QueFragInterface interfaceObj;

    LinearLayout parent,goButtonLayout;
    SharedPreferences preferences;


    SharedPreferences progressPreference;
    public interface QueFragInterface
    {
        public void onTick(String time);
        public void onTimerStop(String time);
        public String getTimerValues();
        public void getFragObject(QuestionFragBase ob);
        public String getMockTitle();
        public void resetTimerView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            interfaceObj=(QueFragInterface) context;
        }

        catch (ClassCastException e)
        {
        }
    }



    public void setUp()
    {

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());


        progressPreference=getContext().getSharedPreferences("progress",Context.MODE_PRIVATE);

        preferences=getContext().getSharedPreferences("current_qno",Context.MODE_PRIVATE);
        currentQno=preferences.getInt(APPSTATE.CURRENT_TABLE,1);
        if(currentQno!=1)
            Toast.makeText(getContext(),"Resuming from Q."+String.valueOf(currentQno),Toast.LENGTH_SHORT).show();

        interfaceObj.getFragObject(this);




        String module=APPSTATE.CURRENT_CATEGORY,parentCategory=APPSTATE.CURRENT_QUE_SUB_CATEGORY;

 if(currentFragment.equals(FRAG_GK))
 {
     currentTable=APPSTATE.CURRENT_TABLE;

}
        else if(currentFragment.equals(FRAG_APTI))
{
    currentTable=APPSTATE.CURRENT_TABLE;
}

 else if(currentFragment.equals(FRAG_MOCK))
 {

     currentMockTitle=interfaceObj.getMockTitle();
     module=currentMockTitle;
     parentCategory="Mock Test";

     switch (currentMockTitle)
     {
         case "Mock Test 1":currentTable="mock1";break;
         case "Mock Test 2":currentTable="mock2";break;
         case "Mock Test 3":currentTable="mock3";break;
         case "Mock Test 4":currentTable="mock4";break;
         case "Mock Test 5":currentTable="mock5";break;
         case "Mock Test 6":currentTable="mock6";break;
         case "Mock Test 7":currentTable="mock7";break;
         case "Mock Test 8":currentTable="mock8";break;
         case "Mock Test 9":currentTable="mock9";break;
         case "Mock Test 10":currentTable="mock10";break;

     }
 }
        else if(currentFragment.equals(FRAG_GK_FAV)){
     currentTable=TABLE_GK_FAV;
     module="GK fav";
     parentCategory="fav";
 }
        else if(currentFragment.equals(FRAG_APTI_FAV)){
     currentTable=TABLE_APTI_FAV;
     module="GK fav";
     parentCategory="fav";

 }


 if(APPSTATE.GOOGLE_PLAY_REQ_VERSION)
 {

     Bundle bundle1=new Bundle();
     bundle1.putString(FirebaseAnalytics.Param.ITEM_ID,module);
     bundle1.putString(FirebaseAnalytics.Param.CONTENT_TYPE,parentCategory);
      mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle1);
 }



        preferences=getContext().getSharedPreferences("current_qno",Context.MODE_PRIVATE);
        currentQno=preferences.getInt(currentTable,1);


        previousId=0;
    }


    public void initialize(final View view)
    {
        parent= (LinearLayout) view;

        queParent= (LinearLayout) view.findViewById(R.id.queParent);

        goButtonLayout=(LinearLayout)view.findViewById(R.id.goButtonLayout);

        //Spinner
        qnoSpinner=(AppCompatSpinner)view.findViewById(R.id.qnoSpinner);




        if (currentFragment.equals(FRAG_APTI)|| currentFragment.equals(FRAG_APTI_FAV))
        {
            //toolbar menus
            buttonNotes=(ImageButton)view.findViewById(R.id.buttonNotes);
            buttonNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNote();
                }
            });


            buttonCalc=(ImageButton)view.findViewById(R.id.buttonCalc);
            buttonCalc .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCalc();
                }
            });



        }


        //common fields.....................

        if(true) {


            buttonFavs = (ImageButton) view.findViewById(R.id.buttonFav);

            if (currentFragment.equals(FRAG_APTI_FAV) || currentFragment.equals(FRAG_GK_FAV)) {
                buttonFavs.setVisibility(View.GONE);
                View favSep=view.findViewById(R.id.favSep);
                favSep.setVisibility(View.GONE);
            }
            buttonFavs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    addToFavApti();
                }
            });

            buttonShare = (ImageButton) view.findViewById(R.id.buttonShare);
            buttonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_SEND);


                    intent.setType("text/plain");

                    String text = "Hey , Can you answer this question?\n \n" + currentQuestion.getQue() + "\n\n a) " + currentQuestion.getOption1() + "\n\n b) " + currentQuestion.getOption2() + "\n\n c) " + currentQuestion.getOption3() + "\n\n d) " + currentQuestion.getOption4() + "\n\n"+"For More  Solved Interview Questions And Answers Download the Free All In One Offline Interview Preparation Guide.\n"+"https://play.google.com/store/apps/details?id="+getContext().getPackageName();
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(intent);

                }
            });


            buttonExplain = (ImageButton) view.findViewById(R.id.buttonExplain);
            buttonExplain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showExplanation();
                }
            });
        }
        //question field

        questionText=(TextView)view.findViewById(R.id.questionText);


        ScrollView scrollView=(ScrollView)view.findViewById(R.id.scrollText);
        final boolean[] flag = {false};

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction())
                {

                    case  MotionEvent.ACTION_DOWN:
                        flag[0] =false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        flag[0] =true;
                       break;

                    case MotionEvent.ACTION_UP:


                        if(flag[0] || currentFragment.equals(FRAG_APTI_FAV)) {
                            break;
                        }


                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                                |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                        getActivity().getWindow().addFlags( WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN

                        );

                        getActivity().getWindow().getDecorView().setSystemUiVisibility(  View.SYSTEM_UI_FLAG_VISIBLE);


                        final Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            public void run () {

                                try
                                {
                                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                                            |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                                    View decor=getActivity().getWindow().getDecorView();

                                    decor.setSystemUiVisibility(  View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                                }
                                catch(Exception e)
                                {
                                }

                            }
                        };

                        handler.postDelayed(runnable, 3000);

                        break;
                }



                return false;
            }
        });
        //qno

        //Radio buttons
        option1=(RadioButton)view.findViewById(R.id.option1);
        option2=(RadioButton)view.findViewById(R.id.option2);
        option3=(RadioButton)view.findViewById(R.id.option3);
        option4=(RadioButton)view.findViewById(R.id.option4);

        //set onclick listener for radio buttons
        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        //4 go buttons
        go1=(ImageButton)view.findViewById(R.id.go1);
        go2=(ImageButton)view.findViewById(R.id.go2);
        go3=(ImageButton)view.findViewById(R.id.go3);
        go4=(ImageButton)view.findViewById(R.id.go4);

        go1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                setGoResource();

                submit(go1);
            }
        });

        go2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setGoResource();
                submit(go2);
            }
        });

        go3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGoResource();
                submit(go3);
            }
        });

        go4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGoResource();
                submit(go4);
            }
        });



        //radio button group
        radioOptionsGroup=(RadioGroup)view.findViewById(R.id.radioOptionsGroup);

        radioOptionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
       });


        //next button
        buttonNext=(ImageButton)view.findViewById(R.id.buttonNext);

        //set onclick listener
        buttonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                //INVISIBLE all go buttons
                go1.setVisibility(View.INVISIBLE);
                go2.setVisibility(View.INVISIBLE);
                go3.setVisibility(View.INVISIBLE);
                go4.setVisibility(View.INVISIBLE);

                //deselect all radio buttons
                radioOptionsGroup.clearCheck();




                //display next question
                currentQno=currentQno+1;

                if(currentQno>lastQueNo)
                {
                    currentQno--;
                    Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                }


                if(currentQno<=lastQueNo) {


                    qnoSpinner.setSelection(currentQno - 1,true);

                }




            }
        });

        //previous button
        buttonPrevious=(ImageButton)view.findViewById(R.id.buttonPrevious);
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //INVISIBLE all go buttons
                go1.setVisibility(View.INVISIBLE);
                go2.setVisibility(View.INVISIBLE);
                go3.setVisibility(View.INVISIBLE);
                go4.setVisibility(View.INVISIBLE);


                //deselect all radio buttons
                radioOptionsGroup.clearCheck();



                //display next question
                currentQno = currentQno - 1;

                if(currentQno<1)
                {

                    currentQno++;
                    Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    //vibrator.vibrate(100);



                    vibrator.vibrate(100);

                }


                if(currentQno>=1) {

                    qnoSpinner.setSelection(currentQno - 1,true);
                }



            }
        });

    }

    //display question method
    public void displayQuestion(int qno,String TABLE)
    {




        preferences.edit().putInt(currentTable,qno).apply();
        updateTime=true;




        try {

                MasterDB masterDB=new MasterDB(getContext());
                currentQuestion=masterDB.getQuestion(qno,TABLE);





        }

         catch (Exception e)
        {
            FirebaseCrash.log("caught exception on read question");
            FirebaseCrash.report(e);

            Intent intent;
            if(currentFragment.equals(FRAG_APTI))
            {
                intent=new Intent(getContext(),SubCategoryActivity.class);
            }
            else if(currentFragment.equals(FRAG_GK))
            {
                intent =new Intent(getContext(),GKSubActivity.class);
            }
            else if(currentFragment.equals(FRAG_MOCK))
            {
                intent=new Intent(getContext(),MockActivity.class);
            }
            else{
                intent=new Intent(getContext(),MainActivity.class);
            }
            startActivity(intent);
            APPSTATE.BACK_FLAG=true;
            Toast.makeText(getContext(),"Sorry, Something went wrong!",Toast.LENGTH_SHORT).show();
        }

        if(currentQuestion==null)
        {
            FirebaseCrash.log("currentQuestion==nul, qno="+currentQno+" , table="+currentTable);
            FirebaseCrash.report(new Exception("current que null"));

            Intent intent;
            if(currentFragment.equals(FRAG_APTI))
            {
                intent=new Intent(getContext(),SubCategoryActivity.class);
            }
            else if(currentFragment.equals(FRAG_GK))
            {
                intent =new Intent(getContext(),GKSubActivity.class);
            }
            else if(currentFragment.equals(FRAG_MOCK))
            {
                intent=new Intent(getContext(),MockActivity.class);
            }
            else{
                intent=new Intent(getContext(),MainActivity.class);
            }
            startActivity(intent);
            APPSTATE.BACK_FLAG=true;
        }



        try{




            questionText.setText(currentQuestion.getQue()+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");


            if(currentQuestion.getFav().equals("true"))
            {
                buttonFavs.setColorFilter(Color.parseColor("#d50000"));
                buttonFavs.setEnabled(false);

            }
            else
            {
                buttonFavs.setColorFilter(Color.BLACK);
                buttonFavs.setEnabled(true);
            }
            //  }


            if(currentQuestion.getAttempted()!=null)
            {



                ImageButton goButton=null;
                RadioButton button=null;
                switch (currentQuestion.getAttempted())

                {
                    case "option1":
                        goButton=(ImageButton)go1;
                        button=(RadioButton)option1;
                        break;

                    case "option2":
                        goButton=(ImageButton)go2;

                        button=(RadioButton)option2;
                        break;

                    case "option3":
                        goButton=(ImageButton)go3;

                        button=(RadioButton)option3;
                        break;

                    case "option4":
                        goButton=(ImageButton)go4;

                        button=(RadioButton)option4;
                        break;
                }

                if(button!=null)
                {

                    if(!currentFragment.equals(FRAG_MOCK)) {
                        updateTime = false;

                        interfaceObj.onTimerStop(currentQuestion.getTime());
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                        button.callOnClick();
                    }
                    else
                    {
                        button.performClick();
                    }


                }

                option1.setText(currentQuestion.getOption1());
                option2.setText(currentQuestion.getOption2());
                option3.setText(currentQuestion.getOption3());
                option4.setText(currentQuestion.getOption4());

                if(goButton!=null)
                {
                    submit(goButton);
                }

            }





        }catch (Exception e)
        {

            FirebaseCrash.report(e);

            Toast.makeText(getContext(),"Sorry, Something went wrong!",Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }



    }


    public int updateDB(String col,String data)
    {
        MasterDB masterDB=new MasterDB(getContext());

        return masterDB.updateDB(currentTable,currentQno,col,data);
    }

    //submit button onclick listener
    public void submit(final ImageButton button) {




        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);
        //stop timer

        if(currentFragment.equals(FRAG_APTI))
       stopTimer(updateTime);





        final RadioButton radio;
        switch (button.getId())
        {
            case R.id.go1:radio=option1;break;
            case R.id.go2:radio=option2;break;
            case R.id.go3:radio=option3;break;
            case R.id.go4:radio=option4;break;

            default:radio=null;
        }

        currentAns=currentQuestion.getAnswer();
        Animation rotation=AnimationUtils.loadAnimation(getContext(),R.anim.rotate180);

        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(attemptedList.get(currentQno-1).equals("NOT"))
                {

                    if(currentFragment.equals(FRAG_APTI)) {

                        if(APPSTATE.PARENT_CATEG_QUANTI)
                        {
                            progressPreference.edit().putInt("QUANTI_ATTEMPTED",progressPreference.getInt("QUANTI_ATTEMPTED",0)+1).apply();
                        }
                        else if(APPSTATE.PARENT_CATEG_LOGICAL)
                        {
                            progressPreference.edit().putInt("LOGIC_ATTEMPTED",progressPreference.getInt("LOGIC_ATTEMPTED",0)+1).apply();
                        }
                        else if(APPSTATE.PARENT_CATEG_VERBAL)
                        {
                            progressPreference.edit().putInt("VERBAL_ATTEMPTED",progressPreference.getInt("VERBAL_ATTEMPTED",0)+1).apply();
                        }

                        int attem=progressPreference.getInt("APTI_ATTEMPTED",0);


                        progressPreference.edit().putInt("APTI_ATTEMPTED", attem+1).apply();
                    }
                    else if(currentFragment.equals(FRAG_GK))
                    {

                        int attem=progressPreference.getInt("GK_ATTEMPTED",0);

                        progressPreference.edit().putInt("GK_ATTEMPTED", attem+1).apply();
                    }
                }

                if(currentAns.equals(checkedAnswer))
                {
                    if(attemptedList.get(currentQno-1).equals("NOT"))
                    {
                        int correctCount=progressPreference.getInt("CORRECT",0);
                        progressPreference.edit().putInt("CORRECT",correctCount+1).apply();
                    }

                    button.setImageResource(R.drawable.ic_correct);
                    button.setColorFilter(Color.GREEN);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        radio.setBackground(getResources().getDrawable(R.drawable.radio_bg_correct));
                    }

                    attemptedList.set(currentQno-1,"CORRECT");

                }
                else
                {

                    if(attemptedList.get(currentQno-1).equals("NOT")) {
                        int wrongCount = progressPreference.getInt("WRONG", 0);
                        progressPreference.edit().putInt("WRONG", wrongCount + 1).apply();
                    }

                    button.setImageResource(R.drawable.ic_wrong);
                    button.setColorFilter(Color.RED);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        radio.setBackground(getResources().getDrawable(R.drawable.radio_bg_wrong));
                    }


                    attemptedList.set(currentQno-1,"WRONG");


                }


                Animation rotate360=AnimationUtils.loadAnimation(getContext(),R.anim.rotate360);
                rotate360.setDuration(100);
                rotate360.start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rotation.setDuration(100);
        button.startAnimation(rotation);




        //update 'attempted ' column with the current checked option number
       Thread updateThread=new Thread(new Runnable() {
            @Override
            public void run() {
               int updatedInt=updateDB("attempted",checkedAnswer);

                if (updatedInt<0)
                {

                    FirebaseCrash.log("updating attempted column for que - "+currentQno+" and for table - "+currentTable+ " failed");
                    FirebaseCrash.report(new Exception("updating attempted column failed"));

                }
            }
        });

        updateThread.start();


    }



    //get the question number of last questio  of current selected table
    public void getMaxQno(String TABLE)
    {
        MasterDB masterDB=new MasterDB(getContext());
        lastQueNo=masterDB.getLastQueNo(TABLE);

    }


    //induvidual radio button on click listener
    @Override
    public void onClick(View v) {


        int selectedColor;
        int defaultColor;

        if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_BLACK)
        {
             selectedColor=Color.WHITE;
             defaultColor=Color.WHITE;

        }
        else
        {
            selectedColor=Color.WHITE;
            defaultColor=Color.BLACK;
        }



       TranslateAnimation translateAnimation = new TranslateAnimation(-60,0,0,0);
        int animationTime = 100;
        translateAnimation.setDuration(animationTime);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setFillAfter(true);
        goButtonLayout.startAnimation(translateAnimation);
        goButtonLayout.setVisibility(View.VISIBLE);


       setGoResource();




        switch (v.getId())
        {
            case R.id.option1:checkedAnswer="option1";

                option1.setTextColor(selectedColor);
                option2.setTextColor(defaultColor);
                option3.setTextColor(defaultColor);
                option4.setTextColor(defaultColor);
                go1.setVisibility(View.VISIBLE);

                if (go2.getVisibility()==View.VISIBLE)
                { go2.setVisibility(View.INVISIBLE);}

                if (go3.getVisibility()==View.VISIBLE)
                {  go3.setVisibility(View.INVISIBLE);}

                if (go4.getVisibility()==View.VISIBLE)
                {  go4.setVisibility(View.INVISIBLE);}



                break;

            case R.id.option2:checkedAnswer="option2";

                option1.setTextColor(defaultColor);
                option2.setTextColor(selectedColor);
                option3.setTextColor(defaultColor);
                option4.setTextColor(defaultColor);
                go2.setVisibility(View.VISIBLE);

                if (go1.getVisibility()==View.VISIBLE)
                { go1.setVisibility(View.INVISIBLE);}

                if (go3.getVisibility()==View.VISIBLE)
                {  go3.setVisibility(View.INVISIBLE);}

                if (go4.getVisibility()==View.VISIBLE)
                {  go4.setVisibility(View.INVISIBLE);}


                break;
            case R.id.option3:checkedAnswer="option3";
                option1.setTextColor(defaultColor);
                option2.setTextColor(defaultColor);
                option3.setTextColor(selectedColor);
                option4.setTextColor(defaultColor);

                go3.setVisibility(View.VISIBLE);

                if (go1.getVisibility()==View.VISIBLE)
                { go1.setVisibility(View.INVISIBLE);}

                if (go2.getVisibility()==View.VISIBLE)
                {  go2.setVisibility(View.INVISIBLE);}

                if (go4.getVisibility()==View.VISIBLE)
                {  go4.setVisibility(View.INVISIBLE);}

                break;
            case R.id.option4:checkedAnswer="option4";

                option1.setTextColor(defaultColor);
                option2.setTextColor(defaultColor);
                option3.setTextColor(defaultColor);
                option4.setTextColor(selectedColor);
                go4.setVisibility(View.VISIBLE);

                if (go1.getVisibility()==View.VISIBLE)
                { go1.setVisibility(View.INVISIBLE);}

                if (go3.getVisibility()==View.VISIBLE)
                {  go3.setVisibility(View.INVISIBLE);}

                if (go2.getVisibility()==View.VISIBLE)
                {  go2.setVisibility(View.INVISIBLE);}

                break;
            default:Toast.makeText(getContext(),"No options selected",Toast.LENGTH_SHORT).show();


        }


    }



    public class customAdapter extends ArrayAdapter<String>
    {
        ArrayList<String> arrayList;
        ArrayList<String> listAttempted;
        Context context;


        public customAdapter(Context context, int textViewResourceId, ArrayList<String> arrayList, ArrayList<String> listAttempted)
        {
            super(context, textViewResourceId, arrayList);

            this.arrayList=arrayList;
            this.listAttempted=listAttempted;
            this.context=context;
        }


       @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent)
        {

            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            TextView textView=new TextView(context);
            textView.setText("Q."+arrayList.get(position));
            textView.setEllipsize(null);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return textView;
        }

        public View getCustomView(final int position, View convertView, final ViewGroup parent)
        {

            LayoutInflater inflater=getActivity().getLayoutInflater();
            final View row;
           try
           {

               row=inflater.inflate(R.layout.spinner_item, parent, false);
               row.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {



                       Toast.makeText(getContext(),"Q. "+String.valueOf(position+1)+" selected",Toast.LENGTH_SHORT).show();

                       qnoSpinner.setSelection(position,true);

                   }
               });

               TextView itemText=(TextView)row.findViewById(R.id.spinnerItemText);
               ImageView result= (ImageView) row.findViewById(R.id.result);

               itemText.setText("Q. "+arrayList.get(position));

               if(listAttempted.get(position).equals("NOT"))
               {

               }

               else if(listAttempted.get(position).equals("WRONG"))
               {
                   result.setImageResource(R.drawable.ic_wrong);
               }
               else if (listAttempted.get(position).equals("CORRECT")) {
                   result.setImageResource(R.drawable.ic_correct);
               }


               if(currentQno==Integer.parseInt(arrayList.get(position)))
               {



                   if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_DEFAULT) {

                       row.setBackgroundColor(Color.parseColor("#4caf50"));
                   }
                   else
                   {
                       row.setBackgroundColor(Color.parseColor("#00acc1"));

                   }
               }
           }
           catch(Exception e)
            {
                return null;
            }

            return row;
        }

    }


public void start()
{


    if(currentFragment.equals(FRAG_APTI))
    {
        String time=interfaceObj.getTimerValues();

        String times[]=time.split(":");
        realMin=Integer.valueOf(times[0]);
        realSec=Integer.valueOf(times[1]);
    }
    else if(currentFragment.equals(FRAG_MOCK))
    {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String time=sharedPref.getString("mock_time","35:00");

        String[] times=time.split(":");
        final int min=Integer.parseInt(times[0]);
        final int sec=Integer.parseInt(times[1]);


        Toast.makeText(getContext(),"Test duration  "+String.format("%02d",min)+"m "+String.format("%02d",sec)+"s",Toast.LENGTH_LONG).show();

        realMin=min;
        realSec=sec;
        startTimer();
    }
    nextOrPrevious=false;



    //get no of questions
    getMaxQno(currentTable);

    //get times from DB

    try {

       if(currentFragment.equals(FRAG_APTI))
       {
           MasterDB masterDB = new MasterDB(getContext());
           ArrayList<String> tempTimes = masterDB.getTimes(currentTable);

           for (String value : tempTimes) {


               String[] values = value.split(":");
               int min = Integer.parseInt(values[0]);
               int sec = Integer.parseInt(values[1]);
               int seconds = (min * 60) + sec;

               timeList.add(seconds);
           }
       }


        //populate spinner
        setSpinner();


        qnoSpinner.setSelection(currentQno - 1);

    }
    catch(android.database.CursorIndexOutOfBoundsException e)
    {

        TextView noContent=new TextView(getContext());

        noContent.setGravity(Gravity.CENTER);

        noContent.setText("No records found !");
        int color=Color.parseColor("#ffffff");

        if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_DEFAULT)
        {
            color=Color.parseColor("#ffffff");
        }
        else if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_BLACK)
        {
            color=Color.parseColor("#455a64");
        }

        noContent.setBackgroundColor(color);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        parent.removeAllViews();
        parent.addView(noContent,params);

    }

    catch(NumberFormatException e)
    {

        e.printStackTrace();

        FirebaseCrash.log("table="+currentTable+" exception should have been caught inside master db");
        FirebaseCrash.report(e);

      getActivity().onBackPressed();
        Toast.makeText(getContext(),"Sorry something went wrong !",Toast.LENGTH_SHORT).show();



    }


}

    //set spinner
    public void setSpinner()
    {


        ArrayList<String> arrayList=new ArrayList<>();
        MasterDB masterDB=new MasterDB(getContext());
        attemptedList = masterDB.getAttemptedList(currentTable);


        for(int i=1;i<=lastQueNo;i++)

        {

            arrayList.add(String.valueOf(i));
        }

        spinnerAdapter=new customAdapter(getContext(),R.id.spinnerItemText,arrayList,attemptedList);

        qnoSpinner.setAdapter(spinnerAdapter);





        qnoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                option1.setEnabled(true);
                option2.setEnabled(true);
                option3.setEnabled(true);
                option4.setEnabled(true);

                final int queNo= Integer.parseInt(parent.getSelectedItem().toString());


                ObjectAnimator rotate;
                rotate=ObjectAnimator.ofFloat(queParent,"rotationY",0f,90f);

                if(previousQno>queNo)
                {
                    rotate=ObjectAnimator.ofFloat(queParent,"rotationY",0f,-90f);

                }


                rotate.setTarget(queParent);
                rotate.setDuration(175);



                rotate.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {



                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {



                        //clear fields
                        questionText.setText("");
                        option4.setText("");
                        option3.setText("");
                        option2.setText("");
                        option1.setText("");
                        //start


                        int color;
                        if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_BLACK)
                        {
                            option1.setTextColor(Color.WHITE);
                            option2.setTextColor(Color.WHITE);
                            option3.setTextColor(Color.WHITE);
                            option4.setTextColor(Color.WHITE);

                            color=Color.parseColor("#00acc1");


                        }
                        else
                        {
                            option1.setTextColor(Color.BLACK);
                            option2.setTextColor(Color.BLACK);
                            option3.setTextColor(Color.BLACK);
                            option4.setTextColor(Color.BLACK);
                            color=Color.parseColor("#43a047");
                        }

                        go1.setColorFilter(color);
                        go2.setColorFilter(color);
                        go3.setColorFilter(color);
                        go4.setColorFilter(color);

                       /* LinearLayout.LayoutParams params1= (LinearLayout.LayoutParams) option1.getLayoutParams();
                        params1.rightMargin=params1.leftMargin;
                        option1.setLayoutParams(params1);

                        LinearLayout.LayoutParams params2= (LinearLayout.LayoutParams) option2.getLayoutParams();
                        params2.rightMargin=params2.leftMargin;
                        option2.setLayoutParams(params2);

                        LinearLayout.LayoutParams params3= (LinearLayout.LayoutParams) option3.getLayoutParams();
                        params3.rightMargin=params2.leftMargin;
                        option3.setLayoutParams(params3);

                        LinearLayout.LayoutParams params4= (LinearLayout.LayoutParams) option4.getLayoutParams();
                        params4.rightMargin=params4.leftMargin;
                        option4.setLayoutParams(params4);*/


                        if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_BLACK) {

                            option1.setBackgroundResource(R.drawable.radio_selector_orange);
                            option2.setBackgroundResource(R.drawable.radio_selector_orange);
                            option3.setBackgroundResource(R.drawable.radio_selector_orange);
                            option4.setBackgroundResource(R.drawable.radio_selector_orange);
                        }
                        else
                        {
                            option1.setBackgroundResource(R.drawable.radio_selector);
                            option2.setBackgroundResource(R.drawable.radio_selector);
                            option3.setBackgroundResource(R.drawable.radio_selector);
                            option4.setBackgroundResource(R.drawable.radio_selector);

                        }

                        //end-clear fields

                      ObjectAnimator nintyTo180=ObjectAnimator.ofFloat(queParent,"rotationY",90f,180f);

                        if(previousQno>queNo)
                        {
                            nintyTo180=ObjectAnimator.ofFloat(queParent,"rotationY",-90f,-180f);
                        }
                        nintyTo180.setDuration(175);
                        nintyTo180.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {


                                ObjectAnimator rotateFast=ObjectAnimator.ofFloat(queParent,"rotationY",180f,360f);

                                if(previousQno>queNo)
                                {
                                    rotateFast=ObjectAnimator.ofFloat(queParent,"rotationY",-180f,-360f);
                                }
                                rotateFast.setDuration(0);
                                rotateFast.start();

                                previousQno=queNo;


                                if(currentFragment.equals(FRAG_APTI))
                                {
                                    stopTimer(false);

                                    if(attemptedList.get(queNo-1).equals("NOT"))
                                    {
                                        startTimer();
                                    }

                                }


                                //INVISIBLE all go buttons
                                go1.setVisibility(View.INVISIBLE);
                                go2.setVisibility(View.INVISIBLE);
                                go3.setVisibility(View.INVISIBLE);
                                go4.setVisibility(View.INVISIBLE);


                                //deselect all radio buttons
                                radioOptionsGroup.clearCheck();


                                currentQno=queNo;

                                if(currentQno>=2) {
                                    if (buttonPrevious.isClickable() == false) {
                                        buttonPrevious.setColorFilter(Color.CYAN);
                                        buttonPrevious.setEnabled(true);
                                        buttonPrevious.setClickable(true);
                                        buttonPrevious.setFocusable(true);
                                    }
                                }

                                if(currentQno<lastQueNo) {
                                    if (buttonNext.isClickable() == false) {
                                        buttonNext.setColorFilter(Color.CYAN);
                                        buttonNext.setEnabled(true);
                                        buttonNext.setClickable(true);
                                        buttonNext.setFocusable(true);
                                    }

                                }
                                //end
                                if(queNo<=lastQueNo&&queNo>=1) {


                                    displayQuestion(queNo, currentTable);

                                }




                                AnimatorSet scaleUpXY=new AnimatorSet();
                                ObjectAnimator scaleYUp=ObjectAnimator.ofFloat(queParent,"scaleY",.75f,1f);
                                scaleYUp.setDuration(150);
                                ObjectAnimator scaleXUp=ObjectAnimator.ofFloat(queParent,"scaleX",.75f,1f);
                                scaleXUp.setDuration(150);
                                scaleUpXY.setInterpolator(new AccelerateInterpolator());

                                scaleUpXY.playTogether(scaleYUp,scaleXUp);
                                scaleUpXY.start();


                            }


                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        nintyTo180.start();




                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                ObjectAnimator scaleXDown=ObjectAnimator.ofFloat(queParent,"scaleX",1f,.75f);

                scaleXDown.setDuration(150);

                ObjectAnimator scaleYDown=ObjectAnimator.ofFloat(queParent,"scaleY",1f,.75f);
                scaleYDown.setDuration(150);

                final ObjectAnimator finalRotate = rotate;
                scaleXDown.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        finalRotate.start();

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                AnimatorSet scaleDownXY=new AnimatorSet();
                scaleDownXY.playTogether(scaleYDown,scaleXDown);
                scaleDownXY.setInterpolator(new AccelerateInterpolator());
                scaleDownXY.start();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }

    //show notes fragment
    public void showNote()
    {
        try
        {
            NoteFragment noteFragment=new NoteFragment();

            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();

            MasterDB masterDB=new MasterDB(getContext());

            currentQuestion=masterDB.getQuestion(currentQno,currentTable);

            String tag=currentQuestion.getNotes();

            if(tag.equals("null"))
            {
                tag="";
            }

            noteFragment.show(fragmentManager, tag);
        }
        catch (Exception e)
        {
        }
    }

    //shows calc fragment
    public void showCalc()
    {
        CalcFragment calcFragment=new CalcFragment();
        FragmentManager fragmentManager=getFragmentManager();
        calcFragment.show(fragmentManager,"calc fragment");
    }

    //show explanation
    public void showExplanation()
    {

        if(currentFragment.equals(this.FRAG_MOCK)&&APPSTATE.MOCK_TEST_ON)
        {

            Toast.makeText(getContext(),"Explanation will be available once you submit the test",Toast.LENGTH_SHORT).show();
            return;
        }

        if(attemptedList.get(currentQno-1).equals("NOT"))
        {
            Toast.makeText(getContext(),"You must attempt first !",Toast.LENGTH_SHORT).show();
            return;
        }
        ExpFragment expFragment=new ExpFragment();

        expFragment.show(getFragmentManager(),"exp fragment");


    }

    public void addToFavApti()
    {


            Toast.makeText(getContext(),"Added to Favourites!",Toast.LENGTH_SHORT).show();
            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(buttonFavs, "scaleX",1f, 1.5f);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(buttonFavs, "scaleY",1f, 1.5f);
            AnimatorSet scaleDown=new AnimatorSet();
            scaleDown.setDuration(100);
            scaleDown.playTogether(scaleDownX,scaleDownY);


            ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(buttonFavs, "scaleX",1.5f, 1f);
            ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(buttonFavs, "scaleY",1.5f, 1f);

            final AnimatorSet scaleUp=new AnimatorSet();
            scaleUp.setDuration(100)
                    .playTogether(scaleUpX,scaleUpY);

            scaleDown.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    buttonFavs.setColorFilter(Color.parseColor("#d50000"));
                    buttonFavs.setEnabled(false);
                    scaleUp.start();

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            scaleDown.start();

        String favTable=null;
        if(currentFragment.equals(FRAG_APTI)||currentFragment.equals(FRAG_MOCK))

        {favTable=TABLE_APTI_FAV;}
        else if (currentFragment.equals(FRAG_GK))
        {favTable=TABLE_GK_FAV;}

        final String finalFavTable = favTable;
        new Thread(new Runnable() {
            @Override
            public void run() {
                MasterDB masterDB=new MasterDB(getContext());

                masterDB.addToFavApti(currentQuestion,currentTable,currentQno, finalFavTable);
            }
        }).start();

    }

    public void updateNote(final String note)
    {
        Thread noteThread=new Thread(new Runnable() {
            @Override
            public void run() {
                MasterDB masterDB= new MasterDB(getContext());

                int updateInt=masterDB.updateDB(currentTable,currentQno,"notes",note);

                if(updateInt==0)
                {
                    FirebaseCrash.log("update note failed -- table="+currentTable+" que="+currentQno);
                    FirebaseCrash.report(new Exception("update note failed -- table="+currentTable+" que="+currentQno));
                }

            }
        });

        noteThread.start();


    }


    public String getNote()
    {
        return currentQuestion.getNotes();
    }
    public void setNote(String note)
    {
        currentQuestion.setNotes(note);
    }

    public String sendExpToActivity()
    {
        if(currentQuestion!=null) {

            if(currentQuestion.getExplanation().equals("null"))
            {
                switch(currentQuestion.answer)
                {
                    case "option1": return "Correct answer is "+currentQuestion.option1;
                    case "option2": return "Correct answer is "+currentQuestion.option2;
                    case "option3": return "Correct answer is "+currentQuestion.option3;
                    case "option4": return "Correct answer is "+currentQuestion.option4;
                }

            }
            return currentQuestion.getExplanation();
        }

        else
        {return null;}
    }

    public String getOption()
    {
        if(currentQuestion!=null)
        {
            switch (currentQuestion.getAnswer())
            {
                case "option1":return "option A";
                case "option2":return "option B";
                case "option3":return "option C";
                case "option4":return "option D";
                default:return "unknown";
            }

        }

        return "unknown";
    }



    public void stopTimer(boolean updateDB)
    {

        if(timer!=null)
        {
            timer.cancel();

            timer=null;



            if(updateDB)
            {
                if(!currentFragment.equals(FRAG_MOCK))
                {
                    int time=progressPreference.getInt("TIME",0);
                    progressPreference.edit().putInt("TIME",time+((actualMin*60)+actualSec)).apply();
                    timeList.add((actualMin*60)+actualSec);
                    int maxTime=progressPreference.getInt("max_time",0);
                    if(maxTime<(actualMin*60)+actualSec)
                    {progressPreference.edit().putInt("max_time",(actualMin*60)+actualSec).apply();}


                    Thread timeThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MasterDB masterDB=new MasterDB(getContext());

                            int update= masterDB.updateTime(currentTable,currentQno,String.valueOf(actualMin)+":"+String.valueOf(actualSec));
                            if(update==0)
                            {
                                FirebaseCrash.log("time update error");
                                FirebaseCrash.report(new Exception("time update error table="+currentTable+" que="+currentQno));
                            }
                        }
                    });
                    timeThread.start();

                    interfaceObj.onTimerStop(String.valueOf(actualMin)+":"+String.valueOf(actualSec));
                }
                else {
                    Toast.makeText(getContext(),"Time - "+String.valueOf(actualMin)+Html.fromHtml("<sub>m</sub>")+" "+String.valueOf(actualSec)+Html.fromHtml("<sub>s</sub>"),Toast.LENGTH_SHORT).show();
                }

            }


            //save to db


        }
    }


    public void startTimer()
    {

        if(timer==null) {

            min=realMin;
            sec=realSec;

            actualMin = 0;
            actualSec = -1;
            timer = new Timer();
            interfaceObj.resetTimerView();

            initializeTimerTask();

            timer.schedule(timerTask, 0, 1000);
            tempTimer=timer;
            tempTimerTask=timerTask;
        }

    }


    public void initializeTimerTask()
    {

        timerTask=new TimerTask() {
            @Override
            public void run() {



                if(actualSec==60)
                {
                    actualSec=0;
                    actualMin++;
                }
                actualSec++;



                handler.post(new Runnable() {
                    @Override
                    public void run() {


                            if(String.valueOf(sec).length()==1)
                            {
                                secVal="0"+String.valueOf(sec);
                            }
                            else
                            {
                                secVal=String.valueOf(sec);
                            }

                            if(String.valueOf(min).length()==1)
                            {
                                minVal="0"+String.valueOf(min);
                            }
                            else
                            {
                                minVal=String.valueOf(min);
                            }
                            interfaceObj.onTick(String.valueOf(minVal+":"+secVal));

                        if(sec==0)
                        {
                            sec=60;
                            min=min-1;




                        }

                        sec--;




                    }
                });


            }
        };
    }

    public void finishTest()
    {



        int countNotAttempted=0;

        for (String status:attemptedList)
        {
           if(status.equals("NOT"))
           {
               countNotAttempted++;
           }
        }




            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setCancelable(false)
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

        String submitString;

        if (countNotAttempted!=0)
        {
            submitString="Submit Anyway";
            builder.setMessage(String.valueOf(countNotAttempted)+" questions not attempted")
                    .setPositiveButton(submitString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            APPSTATE.MOCK_TEST_ON=false;
                            APPSTATE.MOCK_QUESTION_ACTIVITY_FLAG=true;
                            showMockReport();
                        }
                    });

        }
        else
        {
            submitString="Submit";
           builder .setMessage("Do you really want to submit ?")
                   .setPositiveButton(submitString, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                           APPSTATE.MOCK_TEST_ON=false;
                           APPSTATE.MOCK_QUESTION_ACTIVITY_FLAG=true;
                           showMockReport();
                       }
                   });

        }
        builder.create().show();


    }

    public void showMockReport()
    {
     if(currentFragment.equals(FRAG_MOCK))
        stopTimer(true);

        int not,correct,wrong;
        not=correct=wrong=0;
        for (String status:attemptedList) {

            switch(status)
            {
                case "CORRECT":correct++;break;
                case "WRONG":wrong++;break;
                case "NOT":not++;break;
            }

        }

        if(currentFragment.equals(FRAG_MOCK)) {
            //update db

            final int finalCorrect = correct;
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    UserStateDB userStateDB = new UserStateDB(getContext());
                    userStateDB.updateScore(currentMockTitle, finalCorrect);
                }
            });
            thread.start();

        }


        Intent intent=new Intent(getContext(),ProgressActivity.class);
        intent.putExtra("correct",correct);
        intent.putExtra("wrong",wrong);
        intent.putExtra("not attempted",not);
        intent.putExtra("total",attemptedList.size());


        if(currentFragment.equals(FRAG_MOCK)||currentFragment.equals(FRAG_GK)) {
            intent.putExtra("showTime", false);
        }
        else {
            intent.putExtra("showTime", true);
            intent.putIntegerArrayListExtra("timelist",timeList);

        }




        startActivity(intent);

    }



    @Override
    public void onResume() {
        super.onResume();



    }

    public void formula(String title)
    {
        InterviewDB interviewDB=new InterviewDB(getContext());

        String table="quant_formula";

        if(APPSTATE.PARENT_CATEG_QUANTI)
            table="quant_formula";
        else if(APPSTATE.PARENT_CATEG_LOGICAL)
            table="reasoning_formula";
        else if(APPSTATE.PARENT_CATEG_VERBAL)
            table="verbal_formula";



        Tip tip=interviewDB.getTip(title,table);

        if(tip==null)
        {
            Toast.makeText(getContext(),"Formula not available",Toast.LENGTH_SHORT).show();
            return;
        }

        View view=getActivity().getLayoutInflater().inflate(R.layout.interview_layout,null);

        int width=getResources().getDisplayMetrics().widthPixels;
        int height=getResources().getDisplayMetrics().heightPixels;
        int widthPixels=width-width/10;
        int heightPixels= (int) (height*.4);

        TextView titleView,formulaView;
        titleView=(TextView)view.findViewById(R.id.title);
        formulaView=(TextView)view.findViewById(R.id.tipText);


        titleView.setText(tip.getTitle());
        formulaView.setText(tip.getTip());

        formulaView.setMinimumHeight(heightPixels);
        titleView.setMinimumHeight((int) (heightPixels*.2));

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setNegativeButton("BACK",null);

        Dialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.CalcAnimation;





        dialog.getWindow().setLayout(widthPixels,WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();

       try
       {
           if(currentFragment.equals(FRAG_APTI)||currentFragment.equals(FRAG_GK))
           {
               SharedPreferences progressPreference=getContext().getSharedPreferences("progress",Context.MODE_PRIVATE);


               int attemptedAfter=0;
               for (String status:attemptedList) {

                   if(status.equals("CORRECT")||status.equals("WRONG"))
                   {
                       attemptedAfter++;
                   }

               }

               Float progress= Float.valueOf((attemptedAfter*100)/attemptedList.size());
               progressPreference.edit().putFloat(APPSTATE.CURRENT_CATEGORY,progress).apply();


           }

       }
       catch (Exception e)
       {
           FirebaseCrash.report(e);
       }
    }

    public void setGoResource() {
        go1.setImageResource(R.drawable.ic_submit);
        go2.setImageResource(R.drawable.ic_submit);
        go3.setImageResource(R.drawable.ic_submit);
        go4.setImageResource(R.drawable.ic_submit);
    }



    public void sendBugReport()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View titleView=getActivity().getLayoutInflater().inflate(R.layout.dialog_title,null);
        TextView title=(TextView)titleView.findViewById(R.id.dialogTitle);
        ImageView icon=(ImageView)titleView.findViewById(R.id.dialogIcon);
        icon.setImageResource(R.drawable.ic_bug);
        title.setText("Report Error");
        builder.setCustomTitle(titleView);


        View view=getActivity().getLayoutInflater().inflate(R.layout.bug_report_layout,null);

                builder.setNegativeButton("CANCEL",null)
                .setView(view)
                .setTitle("Report Error");


        TextView bugTopicText= (TextView) view.findViewById(R.id.bug_topic_text);
        TextView bugQ=(TextView) view.findViewById(R.id.bug_qno);
        final EditText  answer= (EditText) view.findViewById(R.id.input_corret_ans);
        final EditText  exp= (EditText) view.findViewById(R.id.input_correct_exp);

        bugTopicText.setText(String.valueOf(APPSTATE.CURRENT_CATEGORY));
        bugQ.setText(String.valueOf(String.valueOf(currentQno)));


        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            try
            {
                if(!answer.getText().toString().equals(""))
                {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference myRef = database.getReference("error_report");

                    myRef.push().setValue(new BugReport(APPSTATE.CURRENT_CATEGORY,currentQno,answer.getText().toString(),exp.getText().toString()));
                    Toast.makeText(getContext(),"Sending error report..",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendBugReport();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(getContext(), "Could not send error report", Toast.LENGTH_SHORT).show();
                FirebaseCrash.report(e);
            }
            }
        });

        builder.create().show();

    }

}
