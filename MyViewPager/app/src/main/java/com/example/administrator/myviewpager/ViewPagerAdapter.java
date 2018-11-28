package com.example.administrator.myviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter  extends FragmentPagerAdapter {
    private List<Fragment> mlist=new ArrayList<Fragment>();

    public ViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.mlist=list;


    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }
}
