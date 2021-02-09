package com.codit.interview.aptitude.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.model.ParentCategory;
import com.codit.interview.aptitude.util.APPSTATE;


/**
 * Created by Sreejith on 29-Jul-16.
 */
public class GKSubActivity extends NavActivityBase implements GKSubAdapter.GKSubInterface{


    public GKSubActivity()
    {
        this.currentActivity=this.OTHER_ACTIVITY;

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initialize(R.layout.activity_gksub);

       setTitle("General Knowledge");


    }

    @Override
    public void goToGKQue(View v,String currentCategory) {

        APPSTATE.CURRENT_CATEGORY=currentCategory;

        Intent intent=new Intent(getBaseContext(),GKQuestionActivity.class);

        switch(currentCategory)
        {

            case "Awards And Honors":APPSTATE.CURRENT_TABLE=APPSTATE.GK__AWARDS; break;
            case "India And World History":APPSTATE.CURRENT_TABLE=APPSTATE.GK_INDIAN_HISTORY; break;
            case "World Geography":APPSTATE.CURRENT_TABLE=APPSTATE.GK_GEO; break;
            case "Basic General Knowledge":APPSTATE.CURRENT_TABLE=APPSTATE.GK_BASIC_GK; break;
            case "Computer Awareness":APPSTATE.CURRENT_TABLE=APPSTATE.GK_COMPUTER; break;
            case "Inventions And Discoveries":APPSTATE.CURRENT_TABLE=APPSTATE.GK_INVENTIONS; break;
            case "General Science":APPSTATE.CURRENT_TABLE=APPSTATE.GK_SCIENCE; break;
            case "Sports":APPSTATE.CURRENT_TABLE=APPSTATE.GK_SPORTS; break;
            case "Famous Personalities":APPSTATE.CURRENT_TABLE=APPSTATE.GK_FAMOUS; break;
            case "Indian Geography":APPSTATE.CURRENT_TABLE=APPSTATE.GK_INDIAN_GEO;break;
            case "Indian Politics":APPSTATE.CURRENT_TABLE=APPSTATE.GK_POLITICS;break;




        }

        startActivity(intent);
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
        Intent intent=new Intent(getBaseContext(), ParentCategory.class);
        startActivity(intent);
        APPSTATE.BACK_FLAG=true;
    }
}
