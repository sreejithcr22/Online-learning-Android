package com.codit.interview.aptitude;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.HashMap;

/**
 * Created by Sreejith on 15-Aug-16.
 */
public class TabAdapter extends FragmentPagerAdapter {

    HashMap<Integer,Fragment> fragments;
    HashMap<Integer,String> titles;
    public GKFragment object;
    boolean isGK;

    public TabAdapter(FragmentManager fm,HashMap<Integer,Fragment> hm,HashMap<Integer,String> title,boolean flag)
    {
        super(fm);
        this.fragments=hm;
        this.titles=title;
        isGK=flag;

    }

    @Override
    public Fragment getItem(int position) {

        if(isGK) {
            if (position == 1) {

                object = (GKFragment) fragments.get(position);
            }
        }

       return fragments.get(position);

    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
      return titles.get(position);
    }

    public GKFragment getObject()
    {
        if(object!=null)
            return object;

        return null;
    }
}
