package com.codit.interview.aptitude.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.codit.interview.aptitude.R;

/**
 * Created by Sreejith on 05-Sep-16.
 */
public class MockQueFragment extends QuestionFragBase {


    public MockQueFragment()
    {
        this.currentFragment=FRAG_MOCK;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try
        {
            setUp();
        }
        catch (Exception e)
        {
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_mock_que, container, false);



try
{
    initialize(view);

}
catch (Exception e)
{
}
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        start();
    }

}
