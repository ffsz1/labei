package com.vslk.lbgx.ui.home.adpater;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeVpAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles;

    public HomeVpAdapter(ArrayList<Fragment> mList, List<String> mTitles, FragmentManager fm) {
        super(fm);
        this.mTitles = mTitles;
        if (mList == null) return;
        this.mFragments.addAll(mList);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        if (!((Fragment) object).isAdded() || !mFragments.contains(object)) {
            return PagerAdapter.POSITION_NONE;
        }
        return mFragments.indexOf(object);
    }
}
