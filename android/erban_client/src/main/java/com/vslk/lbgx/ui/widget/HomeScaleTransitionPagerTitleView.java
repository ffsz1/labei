package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.text.TextPaint;

import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.titles.HomeColorTransitionPagerTitleView;

/**
 * Function:
 * Author: Edward on 2019/6/26
 */
public class HomeScaleTransitionPagerTitleView extends HomeColorTransitionPagerTitleView {

    private float mMinScale = 0.75f;

    public HomeScaleTransitionPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        super.onEnter(index, totalCount, enterPercent, leftToRight);
        // 实现颜色渐变
        setScaleX(mMinScale + (1.0f - mMinScale) * enterPercent);
        setScaleY(mMinScale + (1.0f - mMinScale) * enterPercent);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        super.onLeave(index, totalCount, leavePercent, leftToRight);
        // 实现颜色渐变
        setScaleX(1.0f + (mMinScale - 1.0f) * leavePercent);
        setScaleY(1.0f + (mMinScale - 1.0f) * leavePercent);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        super.onSelected(index, totalCount);
        TextPaint paint = getPaint();
        paint.setFakeBoldText(true);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        super.onDeselected(index, totalCount);
        TextPaint paint = getPaint();
        paint.setFakeBoldText(false);
    }

    public float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(float minScale) {
        mMinScale = minScale;
    }
}

