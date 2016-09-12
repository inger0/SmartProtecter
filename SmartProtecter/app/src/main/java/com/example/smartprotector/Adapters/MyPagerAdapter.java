package com.example.smartprotector.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Huhu on 7/13/15.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentlist;


    public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentlist) {
        super(fm);
        this.fragmentlist = fragmentlist;

    }

    @Override
    public Fragment getItem(int i) {
        return fragmentlist.get(i);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }
}
