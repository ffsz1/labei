package com.tongdaxing.xchat_framework.common.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Function:
 * Author: Edward on 2019/5/17
 */
public class CommonPagerAdapter extends PagerAdapter {

    private List<View> mLists;

    public CommonPagerAdapter(List<View> array) {
        this.mLists = array;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mLists.get(position));
        return mLists.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

}
