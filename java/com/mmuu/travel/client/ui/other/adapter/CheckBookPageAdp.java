package com.mmuu.travel.client.ui.other.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mmuu.travel.client.base.MFBaseFragment;

import java.util.ArrayList;

/**
 * Created by XIXIHAHA on 2016/12/26.
 */

public class CheckBookPageAdp extends FragmentPagerAdapter {

    private ArrayList<MFBaseFragment> list;

    public CheckBookPageAdp(FragmentManager fm) {
        super(fm);
    }

    public void setList(ArrayList<MFBaseFragment> list) {
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }
}
