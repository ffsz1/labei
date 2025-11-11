package com.vslk.lbgx.ui.me.bills.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * <p>提现记录adapter  </p>
 * Created by Administrator on 2017/11/7.
 */
public class WithdrawAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public WithdrawAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
