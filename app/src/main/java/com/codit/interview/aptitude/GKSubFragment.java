package com.codit.interview.aptitude;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by Sreejith on 13-Aug-16.
 */
public class GKSubFragment extends Fragment {

    RecyclerView subRecycler;

    HashMap<String,String> subCategories=new HashMap<String,String>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_sub_category, container, false);

        subRecycler= (RecyclerView) view.findViewById(R.id.subRecycler);

        String categories[]=getResources().getStringArray(R.array.GKBasicCategories);

        for(int i=0;i<categories.length;i++)
        {
            subCategories.put(categories[i],String.valueOf(60+i));
        }

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        subRecycler.setLayoutManager(linearLayoutManager);

        GKSubAdapter adapter=new GKSubAdapter(subCategories,getContext());
        subRecycler.setAdapter(adapter);




        return view;
    }
}
