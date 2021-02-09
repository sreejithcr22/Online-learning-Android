package com.codit.interview.aptitude.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.util.UserStateDB;

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





        mockAdapter=new MockAdapter(getContext(),R.layout.mock_row,objetcs);

        mockList.setAdapter(mockAdapter);


    }
}
