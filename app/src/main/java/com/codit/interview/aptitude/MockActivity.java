package com.codit.interview.aptitude;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MockActivity extends NavActivityBase implements MockAdapter.mockAdapterInterface{

    public MockActivity()
    {
        this.currentActivity=OTHER_ACTIVITY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        APPSTATE.MOCK_TEST_ON=false;

        setTitle("Mock Tests");


        initialize(R.layout.activity_mock);
    }

    @Override
    public void goToMockFrag(MockRow obj) {

        Intent intent=new Intent(getBaseContext(),MockQueActivity.class);
        intent.putExtra("title",obj.getMockTitle());
        startActivity(intent);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

        Intent intent=new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
        APPSTATE.BACK_FLAG=true;
        APPSTATE.CURRENT_SELECTED_DRAWER_ITEM=-1;
    }
}
