package com.vslk.lbgx.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class BaseIndicatorAdapter extends FragmentPagerAdapter {

    private List<Fragment> mTabs;

    public BaseIndicatorAdapter(FragmentManager fm, List<Fragment> tabs) {
        super(fm);
        mTabs = tabs;
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mTabs.get(position);
    }
}
