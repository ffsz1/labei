package com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.titles;

import android.content.Context;

import com.vslk.lbgx.ui.home.fragment.HomeFragment;
import com.tongdaxing.erban.R;

/**
 * Function:
 * Author: Edward on 2019/6/26
 */
public class HomeColorTransitionPagerTitleView extends HomeSimplePagerTitleView {
    public HomeColorTransitionPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        int color;
        if (index == 1) {
            if (HomeFragment.alpha1 >= 255) {
                color = R.color.color_C280FF;
            } else {
                color = R.color.white;
            }
        } else {
            color = R.color.color_C280FF;
        }
        setTextColor(getContext().getResources().getColor(color));
    }

    @Override
    public void onSelected(int index, int totalCount) {
        super.onSelected(index, totalCount);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        super.onDeselected(index, totalCount);
    }
}

