package com.codit.interview.aptitude.model;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.codit.interview.aptitude.util.APPSTATE;
import com.codit.interview.aptitude.view.GKFragment;
import com.codit.interview.aptitude.view.MainActivity;
import com.codit.interview.aptitude.view.NavActivityBase;
import com.codit.interview.aptitude.view.ParentCategoryFragment;
import com.codit.interview.aptitude.R;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

public class ParentCategory extends NavActivityBase {


    public ParentCategory()
    {
        this.currentActivity=GK_FRAGMENT;
    }
    boolean updatedProgress=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HashMap<Integer,Fragment>hm=new HashMap<>();
        hm.put(0,new ParentCategoryFragment());
        hm.put(1,new GKFragment());

        HashMap <Integer,String> titles=new HashMap<>();
        titles.put(0,"Aptitude");
        titles.put(1,"GK");


        intitTab(hm,titles, R.layout.activity_parent_category);

        setTitle("Questions");





        //initialize(R.layout.activity_parent_category);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {



                if(currentActivity.equals(GK_FRAGMENT)) {
                    if (tab.getPosition() == 1) {

                        if(!updatedProgress)
                        {
                            tabAdapter.getObject().updateProgress();
                            updatedProgress=true;

                        }



                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id)
        {
            case R.id.settings: drawer.openDrawer(settingsNav);
                return true;


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        APPSTATE.BACK_FLAG=true;
        APPSTATE.CURRENT_SELECTED_DRAWER_ITEM=-1;
    }
}
