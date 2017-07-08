package com.codit.interview.aptitude;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;



public class SubCategoryFragment extends Fragment
{



    private LinkedHashMap<String,String> subCategories=new LinkedHashMap<String,String>();
boolean adFlag=false;
    private TextView subQueTitle;
    ArrayList<String> time=new ArrayList<String>();
    RecyclerView subRecycler;
    String currentSubCategory;
    String timerChangedCtaegory;
    int changedMinutes,changedSeconds;

    public SubCategoryInterface subCategoryInterface;
    interface SubCategoryInterface
    {
        public String getMainCategory();
        public String getTimerChangedCategory();
        public int getChangedMinutes();
        public int getChangedSeconds();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            subCategoryInterface = (SubCategoryInterface) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement interface");

        }
    }

    public SubCategoryFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v= inflater.inflate(R.layout.fragment_sub_category, container, false);
        subRecycler=(RecyclerView)v.findViewById(R.id.subRecycler);
        return  v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        currentSubCategory=APPSTATE.CURRENT_QUE_SUB_CATEGORY;
        timerChangedCtaegory=subCategoryInterface.getTimerChangedCategory();

        if(timerChangedCtaegory!=null)
        {
            changedMinutes=subCategoryInterface.getChangedMinutes();
            changedSeconds=subCategoryInterface.getChangedSeconds();

        }

        //set Adapter data

        if(APPSTATE.SUB_BACK_FLAG)
        {
            adFlag=true;
            APPSTATE.SUB_BACK_FLAG=false;
        }
        if(currentSubCategory!=null) {
            setSubAdapterArray(currentSubCategory);
        }
        else
        {

            Intent intent=new Intent(getContext(),ParentCategory.class);
            APPSTATE.BACK_FLAG=true;
            startActivity(intent);
            Toast.makeText(getContext(),"Something went wrong!, Please reopen this category or restart the app.",Toast.LENGTH_LONG).show();
            return;
        }




        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());

        subRecycler.setLayoutManager(layoutManager);


        Set<String> categories= subCategories.keySet();
        List<String> sub=new ArrayList<>();
        sub.addAll(categories);
        ArrayList<Integer> adPos=new ArrayList<>();

        for (String val:sub) {

            if(val.contains("ad"))
            {
                adPos.add(sub.indexOf(val));
            }
        }
        SubCategoryAdapter subCategoryAdapter= new SubCategoryAdapter(subCategories,getContext(),adPos);




        subRecycler.setAdapter(subCategoryAdapter);
    }

    public void setSubAdapterArray(String currentSubCategory)

    {
        APPSTATE.RECYCLER_AD_COUNT++;
        SharedPreferences progressPreference=getContext().getSharedPreferences("progress", Context.MODE_PRIVATE);



        switch (currentSubCategory)
        {
            case "Quantitative":



                //get time from DB
               for (int i=0;i<APPSTATE.quanti.length;i++)
                {
                    final String current=APPSTATE.quanti[i].getCategory();


                    if(timerChangedCtaegory!=null)
                    {
                        if(timerChangedCtaegory.equals(current))
                        {



                            String min=String.valueOf(changedMinutes).toString();
                            String sec=String.valueOf(changedSeconds).toString();

                            if(min.length()==1)
                            {
                                min="0"+min;
                            }

                            if(sec.length()==1)
                            {
                                sec="0"+sec;
                            }

                            final String newTime=min+":"+sec;
                          APPSTATE.quanti[i].setTime(newTime);


                            new Thread(new Runnable() {
                                @Override
                                public void run() {


                                    UserStateDB userStateDB=new UserStateDB(getContext());
                                    userStateDB.updateTime(current,newTime);
                                }
                            }).start();
                        }


                    }

                    subCategories.put(current,APPSTATE.quanti[i].getTime());

                    if(progressPreference.getInt("visitCount",0)>5 && adFlag==true)
                    {
                        if(i==2||i==10)
                        {
                            subCategories.put("ad"+String.valueOf(i),"ad");

                        }
                    }




                }


                break;

            case "Verbal":


                //get time from DB
                for (int i=0;i<APPSTATE.verbal.length;i++)
                {
                    final String current=APPSTATE.verbal[i].getCategory();


                    if(timerChangedCtaegory!=null)
                    {
                        if(timerChangedCtaegory.equals(current))
                        {


                            //store changed minutes and seconds to db

                            String min=String.valueOf(changedMinutes).toString();
                            String sec=String.valueOf(changedSeconds).toString();

                            if(min.length()==1)
                            {
                                min="0"+min;
                            }

                            if(sec.length()==1)
                            {
                                sec="0"+sec;
                            }

                            final String newTime=min+":"+sec;
                            APPSTATE.verbal[i].setTime(newTime);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    UserStateDB userStateDB=new UserStateDB(getContext());
                                    userStateDB.updateTime(current,newTime);
                                }
                            }).start();

                        }


                    }

                    subCategories.put(current,APPSTATE.verbal[i].getTime());

                    if(progressPreference.getInt("visitCount",0)>5 && adFlag==true)
                    {
                        if(i==2||i==9) {
                            subCategories.put("ad" + String.valueOf(i), "ad");
                        }

                    }

                }

                break;


            case "Logical":


                //get time from DB
                for (int i=0;i<APPSTATE.logical.length;i++)
                {
                    final String current=APPSTATE.logical[i].getCategory();


                    if(timerChangedCtaegory!=null)
                    {
                        if(timerChangedCtaegory.equals(current))
                        {


                            //store changed minutes and seconds to db

                            String min=String.valueOf(changedMinutes).toString();
                            String sec=String.valueOf(changedSeconds).toString();

                            if(min.length()==1)
                            {
                                min="0"+min;
                            }

                            if(sec.length()==1)
                            {
                                sec="0"+sec;
                            }

                            final String newTime=min+":"+sec;
                            APPSTATE.logical[i].setTime(newTime);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    UserStateDB userStateDB=new UserStateDB(getContext());
                                    userStateDB.updateTime(current,newTime);
                                }
                            }).start();

                        }


                    }

                    subCategories.put(current,APPSTATE.logical[i].getTime());

                    if(progressPreference.getInt("visitCount",0)>5 && adFlag==true)
                    {
                        if(i==2||i==9) {
                            subCategories.put("ad" + String.valueOf(i), "ad");
                        }

                    }

                }

                break;


        }


    }






}

