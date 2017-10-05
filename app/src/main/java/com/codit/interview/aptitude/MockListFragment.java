package com.codit.interview.aptitude;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Sreejith on 03-Sep-16.
 */
public class MockListFragment extends Fragment {

    ArrayList<MockRow> objetcs=new ArrayList<MockRow>();
    MockAdapter mockAdapter;

    ListView mockList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mock, container, false);

        mockList = (ListView) view.findViewById(R.id.mockList);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        UserStateDB userStateDB=new UserStateDB(getContext());


        objetcs=userStateDB.readMockRows();

        if(!App.isAdRemoved())
        {
            for (int i = 0; i < objetcs.size(); i++) {
                if (i == 3 || i == 9) {
                    MockRow adrow = new MockRow();
                    adrow.setAd(true);
                    objetcs.add(i, adrow);
                } else {
                    objetcs.get(i).setAd(false);
                }

            }
        }




        mockAdapter=new MockAdapter(getContext(),R.layout.mock_row,objetcs);

        mockList.setAdapter(mockAdapter);


    }
}
