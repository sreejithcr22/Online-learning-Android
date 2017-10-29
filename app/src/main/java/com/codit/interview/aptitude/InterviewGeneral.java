package com.codit.interview.aptitude;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.appodeal.ads.Appodeal;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.native_ad.views.NativeAdViewAppWall;
import com.appodeal.ads.native_ad.views.NativeAdViewContentStream;
import com.appodeal.ads.native_ad.views.NativeAdViewNewsFeed;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoOptions;

import static android.view.View.GONE;


public class InterviewGeneral extends Fragment implements View.OnClickListener {

    private ImageButton nextTip, prevTip,buttonFav,buttonShare;
CardView bottomLayout;
    NativeAdViewAppWall nav_nf;

    int interviewCount=0;
    TextView tip,title;
    int currentTipNo,lastTipNo;
    String category;
    String gkInfoTitle;
    boolean isGK=false;

    Tip currentTip;
    LinearLayout interviewLayout;
    ScrollView scrollView;
    CardView titleCard;
    NativeExpressAdView adView;
    View view;
    public static final String INTERVIEW_GENERAL="general";
    public static final String INTERVIEW_DATA_STRUCTURE="data";
    public static final String INTERVIEW_CHASH="chash";

    public static final String INTERVIEW_CPP="cpp";
    public static final String INTERVIEW_NETWORK="network";
    public static final String INTERVIEW_OS="os";
    public static final String INTERVIEW_PYTHON="python";
    public static final String INTERVIEW_DBMS="sql";
    public static final String INTERVIEW_TESTING="testing";
    public static final String INTERVIEW_PHP="php";
    public static final String INTERVIEW_js="js";


    public static String GK_INFO="gk_info";
    static final String INTERVIEW_FAV="fav";
    static final String INTERVIEW_JAVA="java";


    interviewInterface interfaceObj;
    FrameLayout parent;
     SharedPreferences progressPreference;


    public InterviewGeneral()
    {
        setTipNo();
        this.category=INTERVIEW_FAV;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            interfaceObj=(interviewInterface) context;
        }

        catch (ClassCastException e)
        {
        }
    }


    public void setTipNo()
    {
        currentTipNo=1;
    }

    public interface interviewInterface
    {
        public void hideTool(CardView layout, CardView title);
        void showTool(CardView layout,CardView title);
        void showOrHideTool(CardView layout,CardView title);

    }




    @SuppressLint("ValidFragment")
    public InterviewGeneral(int pos) {

        setTipNo();

        switch(pos)
        {
            case 0:
                category=this.INTERVIEW_GENERAL;
                break;

            case 1:
                category=this.INTERVIEW_DATA_STRUCTURE;
                break;
            case 2:
                category=this.INTERVIEW_JAVA;break;

            case 3:
                category=this.INTERVIEW_CPP;break;

            case 4:
                category=this.INTERVIEW_CHASH;break;

            case 5:
                category=this.INTERVIEW_PYTHON;break;

            case 6:
                category=this.INTERVIEW_PHP;break;

            case 7:
                category=this.INTERVIEW_js;break;

            case 8:
                category=this.INTERVIEW_TESTING;break;

            case 9:
                category=this.INTERVIEW_NETWORK;break;

            case 10:
                category=this.INTERVIEW_DBMS;break;

            case 11:
                category=this.INTERVIEW_OS;break;



        }



    }

    public InterviewGeneral(String currentCategory)
    {
        setTipNo();
        this.category=GK_INFO;
        gkInfoTitle=currentCategory;
        isGK=true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        progressPreference=getContext().getSharedPreferences("progress", Context.MODE_PRIVATE);
        view = inflater.inflate(R.layout.fragment_interview, container, false);


        bottomLayout = (CardView) view.findViewById(R.id.fabtoolbar_toolbar);

        titleCard = (CardView) view.findViewById(R.id.titleCard);

        parent = (FrameLayout) view.findViewById(R.id.container);
        nav_nf = (NativeAdViewAppWall) view.findViewById(R.id.native_ad_view_news_feed);


        View bottomShadow = view.findViewById(R.id.interview_bottom_bar_shadow);

        if (APPSTATE.CURRENT_THEME == APPSTATE.THEME_BLACK) {
            bottomShadow.setVisibility(GONE);
        }


        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        if (APPSTATE.CURRENT_THEME == APPSTATE.THEME_DEFAULT) {
            scrollView.setBackgroundColor(Color.parseColor("#f5f5f5"));

        }
        else if (APPSTATE.CURRENT_THEME == APPSTATE.THEME_BLACK)
        {
            scrollView.setBackgroundColor(Color.parseColor("#37474f"));

        }


        tip= (TextView) view.findViewById(R.id.tipText);
        title= (TextView) view.findViewById(R.id.title);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),title.getText(),Toast.LENGTH_SHORT).show();
            }
        });

         interviewLayout= (LinearLayout) view.findViewById(R.id.interview_layout);

        nextTip = (ImageButton) view.findViewById(R.id.nextTip);
        prevTip=(ImageButton)view.findViewById(R.id.prevTip);
        buttonFav= (ImageButton) view.findViewById(R.id.fav);
        buttonShare=(ImageButton)view.findViewById(R.id.share);

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);


                intent.setType("text/plain");


                String text = "Download the Free All In One Offline Interview Preparation Guide.\n"+"https://play.google.com/store/apps/details?id="+getContext().getPackageName()+"\n\n"
                        +currentTip.getTitle()+"\n\n-"+currentTip.getTip();

                        intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(intent);
            }
        });



        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addToFav();
            }
        });

        if(category.equals(INTERVIEW_FAV))
        {
            buttonFav.setVisibility(GONE);
            View favSeperator=view.findViewById(R.id.fav_seperator);
            favSeperator.setVisibility(GONE);
        }



        nextTip.setOnClickListener(this);
        prevTip.setOnClickListener(this);



        final float[] initialX = new float[1];
        final float[] initialY = new float[1];

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                int action = event.getActionMasked();

                switch (action) {

                    case MotionEvent.ACTION_DOWN:
                        initialX[0] = event.getX();
                        initialY[0] = event.getY();

                        break;


                    case MotionEvent.ACTION_UP:
                  float finalX = event.getX();
                     float finalY = event.getY();

                   if (initialX[0] < finalX) {
                        }

                      if (initialX[0] > finalX) {
                        }
                    if (initialY[0] < finalY) {
                        interfaceObj.showTool(bottomLayout,titleCard);
                        }
                    if (initialY[0] > finalY) {
                        interfaceObj.hideTool(bottomLayout,titleCard);

                          }

                       if (initialY[0] == finalY) {

                           interfaceObj.showOrHideTool(bottomLayout,titleCard);

                       }

                  break;



                }
                return false;
            }
        });

        return view;
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isGK)
        {

            bottomLayout.setVisibility(View.GONE);
           InterviewDB gkDB=new InterviewDB(getContext());
            Tip gk=gkDB.getTip(gkInfoTitle,category);
            tip.setText(gk.getTip());
            title.setText(gk.getTitle());


        }
        else {
            lastTipNo = getLastTipNo(getContext(), category);
            displayTip(getContext(), currentTipNo, category, tip, title, true);
        }


    }



    public  void displayTip(Context context, final int tipno, final String tableName, final TextView tipText, final TextView titleText,boolean forward)
    {

        interviewCount++;

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        final InterviewDB interviewDB=new InterviewDB(context);


        try
        {


            float hideEnd;
            float showStart;
            if(forward)
            {
                 hideEnd=-interviewLayout.getWidth();
                showStart=interviewLayout.getWidth();
            }

            else
            {
             hideEnd=interviewLayout.getWidth();
                showStart=-interviewLayout.getWidth();
            }

            final ObjectAnimator hide=ObjectAnimator.ofFloat(interviewLayout,"translationX",0f,hideEnd);
            hide.setDuration(200);
            hide.setInterpolator(new AccelerateInterpolator());

            ObjectAnimator showTranslate=ObjectAnimator.ofFloat(interviewLayout,"translationX",showStart,0f);
            showTranslate.setDuration(200);
            showTranslate.setInterpolator(new DecelerateInterpolator());


            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(interviewLayout, "scaleX",1f, .7f);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(interviewLayout, "scaleY",1f, .7f);
            AnimatorSet scaleDown = new AnimatorSet();
            scaleDown.playTogether(scaleDownX,scaleDownY);
            scaleDown.setDuration(180);

            ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(interviewLayout, "scaleX", .7f,1f);
            ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(interviewLayout, "scaleY", .7f,1f);
            final AnimatorSet scaleUp = new AnimatorSet();
            scaleUp.play(scaleUpX).with(scaleUpY);
            scaleUp.setDuration(180);

            AnimatorSet anim=new AnimatorSet();
            anim.playSequentially(scaleDown,hide,showTranslate,scaleUp);

            hide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                   //set values

                    try{

                        if(!App.isAdRemoved())
                        {
                            nav_nf.setVisibility(GONE);

                            showAd();

                        }


                        currentTip=interviewDB.getTip(tipno,tableName);
                    tipText.setText(currentTip.getTip());
                    titleText.setText(String.valueOf(currentTip.getTipNo())+" . "+currentTip.getTitle());





                       if(currentTip.getFav().equals("true"))
                       {
                           buttonFav.setEnabled(false);
                           buttonFav.setColorFilter(Color.parseColor("#d50000"));
                       }
                       else {
                           buttonFav.setEnabled(true);
                           buttonFav.setColorFilter(Color.BLACK);
                       }
                   }

                    catch (android.database.CursorIndexOutOfBoundsException e)
                    {

                        e.printStackTrace();

                        interviewLayout.setVisibility(GONE);



                        TextView noContent=new TextView(getContext());
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
                        noContent.setGravity(Gravity.CENTER);
                        // params.gravity=Gravity.CENTER_HORIZONTAL;

                        noContent.setLayoutParams(params);
                        parent.removeAllViews();
                        parent.addView(noContent);

                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });



            anim.start();

            //anim end




        }
        catch (android.database.CursorIndexOutOfBoundsException e)
        {

            e.printStackTrace();

           interviewLayout.setVisibility(GONE);



            TextView noContent=new TextView(getContext());
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


            noContent.setGravity(Gravity.CENTER);
           // params.gravity=Gravity.CENTER_HORIZONTAL;


            noContent.setLayoutParams(params);




        }




    }

    public void addToFav()
    {
        Toast.makeText(getContext(),"Added to favourites!",Toast.LENGTH_SHORT).show();
           ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(buttonFav, "scaleX",1f, 1.5f);
           ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(buttonFav, "scaleY",1f, 1.5f);
           AnimatorSet scaleDown=new AnimatorSet();
           scaleDown.setDuration(100);
           scaleDown.playTogether(scaleDownX,scaleDownY);


           ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(buttonFav, "scaleX",1.5f, 1f);
           ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(buttonFav, "scaleY",1.5f, 1f);

           final AnimatorSet scaleUp=new AnimatorSet();
           scaleUp.setDuration(100)
                   .playTogether(scaleUpX,scaleUpY);

           scaleDown.addListener(new Animator.AnimatorListener() {
               @Override
               public void onAnimationStart(Animator animator) {

               }

               @Override
               public void onAnimationEnd(Animator animator) {
                   buttonFav.setColorFilter(Color.parseColor("#d50000"));
                   buttonFav.setEnabled(false);
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

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                InterviewDB interviewDB=new InterviewDB(getContext());
                interviewDB.addFav(currentTip,category,INTERVIEW_FAV);



            }
        });
        thread.start();





    }

    public static int getLastTipNo(Context context,String tableName)
    {
        InterviewDB db=new InterviewDB(context);
        return db.getLastTipNo(tableName);
    }



    @Override
    public void onClick(View v) {


        switch(v.getId())
        {

            case R.id.nextTip:

                if(currentTipNo!=lastTipNo)
                {
                    currentTipNo++;
                    displayTip(getContext(),currentTipNo,category,tip,title,true);
                }
                else
                {

                    Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(50);                }
                break;

            case R.id.prevTip:
                if(currentTipNo!=1)
                {
                    currentTipNo--;
                    displayTip(getContext(),currentTipNo,category,tip,title,false);
                }
                else
                {
                    Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(50);
                }
                break;
        }
    }


    public void showAd()
    {
        if(!App.isAdRemoved())
        {


            Log.d("appodeal", "onCreate: ");

            Appodeal.setNativeCallbacks(new NativeCallbacks() {
                @Override
                public void onNativeLoaded() {
                    Log.d("appodeal", "onNativeLoaded: ");

                    try {
                        if(nav_nf!=null&&!App.isAdRemoved()&&nav_nf.getVisibility()==GONE)
                        {
                            nav_nf.setVisibility(View.VISIBLE);
                            nav_nf.setNativeAd(Appodeal.getNativeAds(1).get(0));
                        }
                    }
                    catch (Exception e){}

                }

                @Override
                public void onNativeFailedToLoad() {
                    Log.d("appodeal", "onNativeFailedToLoad: ");
                }

                @Override
                public void onNativeShown(NativeAd nativeAd) {
                    Log.d("appodeal", "onNativeShown: ");
                }

                @Override
                public void onNativeClicked(NativeAd nativeAd) {

                }
            });

            Appodeal.initialize(getActivity(),App.APP_KEY,Appodeal.NATIVE);
            Appodeal.cache(getActivity(), Appodeal.NATIVE,1);

        }

    }


}
