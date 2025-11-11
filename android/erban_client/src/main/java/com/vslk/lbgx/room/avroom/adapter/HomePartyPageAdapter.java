package com.vslk.lbgx.room.avroom.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
public class HomePartyPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public HomePartyPageAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return (mFragmentList == null || mFragmentList.size() == 0 ? null : mFragmentList.get(position));
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }
}
