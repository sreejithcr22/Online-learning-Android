package com.codit.interview.aptitude.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
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

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.model.Tip;
import com.codit.interview.aptitude.util.APPSTATE;
import com.codit.interview.aptitude.util.InterviewDB;
import com.google.android.gms.ads.NativeExpressAdView;

import static android.view.View.GONE;

/**
 * Created by Sreejith on 21-Aug-16.
 */
public class ConceptFragment extends Fragment implements View.OnClickListener{


    int interviewCount=0;
    static String CATEGORY_QUANT="quant_formula";
    static String CATEGORY_REASONING="reasoning_formula";
    static String CATEGORY_VERBAL="verbal_formula";
    static String CATEGORY_FAV="concept_fav";


    private ImageButton nextTip, prevTip,buttonFav,buttonShare;
    CardView bottomLayout;

    TextView tip,title;
    int currentTipNo,lastTipNo;
    String category;

    Tip currentTip;
    LinearLayout interviewLayout;
    ScrollView scrollView;
    CardView titleCard;

    View view;
    NativeExpressAdView adView;
    ConceptInterface interfaceObj;
    FrameLayout parent;
    SharedPreferences progressPreference;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            interfaceObj=(ConceptInterface) context;
        }

        catch (ClassCastException e)
        {
        }
    }


    public void setTipNo()
    {
        currentTipNo=1;
    }

    public interface ConceptInterface
    {
        public void hideTool(CardView layout, CardView title);
        void showTool(CardView layout,CardView title);
        void showOrHideTool(CardView layout,CardView title);

    }

    public ConceptFragment()
    {        setTipNo();

        //populate data from fav table
        this.category=CATEGORY_FAV;
    }

    public ConceptFragment(int pos) {

        setTipNo();

        switch (pos)
        {
            case 0:category=CATEGORY_QUANT;
                break;
            case 1:category=CATEGORY_REASONING;
                break;
            case 2:category=CATEGORY_VERBAL;
                break;

                    }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        progressPreference=getContext().getSharedPreferences("progress", Context.MODE_PRIVATE);

        view=inflater.inflate(R.layout.fragment_interview, container, false);

        bottomLayout= (CardView) view.findViewById(R.id.fabtoolbar_toolbar);

        titleCard=(CardView)view.findViewById(R.id.titleCard);

        parent= (FrameLayout) view.findViewById(R.id.container);

        View bottomShadow=view.findViewById(R.id.interview_bottom_bar_shadow);

        if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_BLACK)
        {
            bottomShadow.setVisibility(GONE);
        }



        scrollView= (ScrollView) view.findViewById(R.id.scrollView);

        if (APPSTATE.CURRENT_THEME == APPSTATE.THEME_DEFAULT) {
            scrollView.setBackgroundColor(Color.parseColor("#f5f5f5"));

        }
        else if (APPSTATE.CURRENT_THEME == APPSTATE.THEME_BLACK)
        {
            scrollView.setBackgroundColor(Color.parseColor("#37474f"));

        }
        tip= (TextView) view.findViewById(R.id.tipText);
        title= (TextView) view.findViewById(R.id.title);

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

        if(category.equals(CATEGORY_FAV))
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

        lastTipNo=getLastTipNo(getContext(),category);
        displayTip(getContext(),currentTipNo,category,tip,title,true);


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


            noContent.setLayoutParams(params);




        }




    }


    public void addToFav()
    {
        InterviewDB interviewDB=new InterviewDB(getContext());

        if (interviewDB.addFav(currentTip,category,CATEGORY_FAV))
        {
            Toast.makeText(getContext(),"Added to favourites !",Toast.LENGTH_SHORT).show();
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

        }
        else
        {
            Toast.makeText(getContext(),"Sorry, Something went wrong !",Toast.LENGTH_SHORT).show();
        }


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



}
