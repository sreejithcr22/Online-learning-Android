package com.codit.interview.aptitude.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.util.APPSTATE;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;


public class ProgressActivity extends AppCompatActivity {
    boolean isTimeDisplayed;
    boolean showOnlyTime;
    int correct,wrong,notAttempted,total;
    TextView totalText,notText,correctText,wrongText;
    TextView totalTimeText,avgText,leastText,highestText;
    CardView timeCard;


    ArrayList<Integer> timeList;
    private GraphicalView mChart;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTimeDisplayed = false;
        super.onCreate(savedInstanceState);



        if(APPSTATE.CURRENT_THEME!=0)
        {
            setTheme(APPSTATE.CURRENT_THEME);
        }

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);


        setContentView(R.layout.progress_activity_layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        toolbar= (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(APPSTATE.CURRENT_CATEGORY);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

       toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final SharedPreferences progressPreference=getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);
        timeCard=(CardView)findViewById(R.id.chartCard);

        totalText= (TextView) findViewById(R.id.totalText);
        notText=(TextView)findViewById(R.id.notText);
        correctText=(TextView)findViewById(R.id.correctText);
        wrongText=(TextView)findViewById(R.id.wrongText);

        totalTimeText=(TextView)findViewById(R.id.totalTime);
        avgText=(TextView)findViewById(R.id.avgTime);
        leastText=(TextView)findViewById(R.id.minTime);
        highestText=(TextView)findViewById(R.id.maxTime);



        Intent intent = getIntent();

        correct = intent.getIntExtra("correct", 0);
        wrong = intent.getIntExtra("wrong", 0);
        notAttempted = intent.getIntExtra("not attempted", 0);
        showOnlyTime = intent.getBooleanExtra("showTime", true);
        total=intent.getIntExtra("total",0);

        timeList=intent.getIntegerArrayListExtra("timelist");

        showProgressChart();

        if(showOnlyTime) {
            showTimeAnalysis();
        }
        else {
            timeCard.setVisibility(View.GONE);
        }



    }



    public void showProgressChart()
    {

        totalText.append(" "+String.valueOf(total));
        correctText.append(" "+String.valueOf(correct));
        notText.append(" "+String.valueOf(notAttempted));
        wrongText.append(" "+String.valueOf(wrong));

        int[] colors = {  Color.parseColor("#00c853"), Color.RED,
                Color.LTGRAY };

        CategorySeries multipleCategorySeries=new CategorySeries("donut");

        String title[]={"Correct","Wrong","Not Attempted"};
        multipleCategorySeries.add("Correct",correct);
        multipleCategorySeries.add("Wrong",wrong);
        multipleCategorySeries.add("Not Attempted ",notAttempted);


        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < title.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();

            seriesRenderer.setColor(colors[i]);



            //Adding colors to the chart
            defaultRenderer.setLabelsTextSize(getResources().getDimension(R.dimen.progress_label_text_size));

            int labelColor=Color.BLACK;
            if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_BLACK)
            {
                TypedValue typedValue = new TypedValue();

                TypedArray a = this.obtainStyledAttributes(typedValue.data, new int[] { R.attr.blackText });
                labelColor = a.getColor(0, 0);
            }
            defaultRenderer.setLabelsColor(labelColor);
            defaultRenderer.setApplyBackgroundColor(true);
            defaultRenderer.setTextTypeface(Typeface.SANS_SERIF);

            defaultRenderer.setShowLegend(false);

            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }


        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setClickEnabled(true);
        defaultRenderer.setApplyBackgroundColor(true);
        int color=Color.WHITE;
        if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_BLACK)
        {
            TypedValue typedValue = new TypedValue();

            TypedArray a = this.obtainStyledAttributes(typedValue.data, new int[] { R.attr.secondaryColor });
             color = a.getColor(0, 0);

        }
        defaultRenderer.setBackgroundColor(color);

        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
        chartContainer.removeAllViews();

        mChart = ChartFactory.getPieChartView(getBaseContext(),
                multipleCategorySeries, defaultRenderer);


        chartContainer.addView(mChart);

    }

    public void showTimeAnalysis()
    {
        //start

        int maxTime=0;
        int minTime=0;
        int avgTime=0;
        int totalTime=0;

        String max,min,totalText,avg;

        if(!timeList.isEmpty())
        minTime=timeList.get(0);

        for (int time:timeList)
        {

            if(time<minTime)
            {
                minTime=time;
            }
            if(time>maxTime)
            {
                maxTime=time;
            }

            totalTime=totalTime+time;
        }


        if(timeList.size()==0)
        {
            avgTime=0;
        }
        else {
            avgTime = totalTime / timeList.size();
        }

        totalText=getTime(totalTime);
        avg=getTime(avgTime);
        min=getTime(minTime);
        max=getTime(maxTime);

        totalTimeText.append(totalText);
        avgText.append(avg);
        leastText.append(min);
        highestText.append(max);


        XYSeries series = new XYSeries("Time Analysis");

        for(int i=0;i<timeList.size();i++)
        {

            series.add(i,timeList.get(i).doubleValue()/60);
        }


        XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
        dataset.addSeries(series);


        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setFillPoints(true);

        renderer.setLineWidth(4);
        renderer.setDisplayBoundingPoints(true);
        renderer.setShowLegendItem(false);


        int graphColor;

            TypedValue typedValue1 = new TypedValue();

            TypedArray a1 = this.obtainStyledAttributes(typedValue1.data, new int[] { R.attr.progressTextColor });
            graphColor = a1.getColor(0, 0);


        renderer.setColor(graphColor);
// Include low and max value
        renderer.setDisplayBoundingPoints(true);

// we add point markers


        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        //mRenderer.setMargins(new int[]{60,75,0,70});
        mRenderer.setMargins(new int[]{getResources().getInteger(R.integer.progress_time_top_margin),getResources().getInteger(R.integer.progress_time_left_margin),getResources().getInteger(R.integer.progress_time_bottom_margin),getResources().getInteger(R.integer.progress_time_right_margin)});
        mRenderer.setShowTickMarks(true);


        mRenderer.setLabelsTextSize(getResources().getDimension(R.dimen.progress_label_text_size));
        mRenderer.setXLabels(10);

        mRenderer.setXTitle("Questions");

        mRenderer.setZoomEnabled(false,false);
        mRenderer.setShowLabels(true,true);



        mRenderer.setYTitle("Time (minutes)");
        mRenderer.setAxisTitleTextSize(getResources().getDimension(R.dimen.progress_label_text_size));
        mRenderer.setAxesColor(Color.WHITE);

        mRenderer.setPointSize(3);


        if((maxTime)>180)
        {
            mRenderer.setYAxisMax((maxTime/60)+1);
        }
        else
        {
            mRenderer.setYAxisMax(3);
        }


        mRenderer.setYAxisMin(0);
        mRenderer.setYLabels(5);
        mRenderer.setXAxisMax(30);
        mRenderer.setXAxisMin(0);

        int color=Color.BLACK;
        if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_BLACK)
        {
            TypedValue typedValue = new TypedValue();

            TypedArray a = this.obtainStyledAttributes(typedValue.data, new int[] { R.attr.secondaryColor });
            color = a.getColor(0, 0);
        }

        mRenderer.setBackgroundColor(color);

        mRenderer.setApplyBackgroundColor(true);

       // mRenderer.setShowGrid(true); // we show the grid
        mRenderer.setMarginsColor(color);
        mRenderer.setXLabelsPadding(20);
        mRenderer.setYLabelsVerticalPadding(0);
        mRenderer.setYLabelsPadding(30);

        mRenderer.addSeriesRenderer(renderer);



        GraphicalView chartView = ChartFactory.getLineChartView(getApplicationContext(), dataset, mRenderer);

        LinearLayout layout= (LinearLayout) findViewById(R.id.chartGraph);
        layout.removeAllViews();


        layout.addView(chartView);


    }

    public String getTime(int seconds)
    {
        String min,sec;

        min=String.valueOf(seconds/60);
        sec=String.valueOf(seconds%60);

        if(min.length()==1)
        {
            min="0"+min;
        }
        if(sec.length()==1)
        {
            sec="0"+sec;
        }

        return min+":"+sec;
    }

    @Override
    public void onBackPressed() {




        if(APPSTATE.QUESTION_ACTIVITY_FLAG)
        {
            APPSTATE.QUESTION_ACTIVITY_FLAG=false;

            Intent intent=new Intent(getBaseContext(),SubCategoryActivity.class);
            startActivity(intent);
            APPSTATE.BACK_FLAG=true;

        }
        else if (APPSTATE.MOCK_QUESTION_ACTIVITY_FLAG)
        {
            APPSTATE.MOCK_QUESTION_ACTIVITY_FLAG=false;

            super.onBackPressed();
            APPSTATE.BACK_FLAG=true;

        }





    }



}
