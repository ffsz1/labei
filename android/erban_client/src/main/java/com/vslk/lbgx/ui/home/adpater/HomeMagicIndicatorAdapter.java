package com.vslk.lbgx.ui.home.adpater;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.vslk.lbgx.ui.widget.HomeScaleTransitionPagerTitleView;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.TabInfo;

import java.util.List;

/**
 * Function:
 * Author: Edward on 2019/6/15
 */
public class HomeMagicIndicatorAdapter extends CommonNavigatorAdapter {

    private Context mContext;
    private List<TabInfo> mTitleList;

    private int normalColorId = R.color.color_AEACAF;
    private int selectColorId = R.color.color_8C63F6;

    public HomeMagicIndicatorAdapter(Context context, List<TabInfo> titleList) {
        mContext = context;
        mTitleList = titleList;
    }

    private int size = 16;

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getCount() {
        return mTitleList == null ? 0 : mTitleList.size();
    }

    public void setNormalColorId(@ColorRes int normalColorId) {
        this.normalColorId = normalColorId;
    }

    public void setSelectColorId(@ColorRes int selectColorId) {
        this.selectColorId = selectColorId;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int i) {
        HomeScaleTransitionPagerTitleView titleView = new HomeScaleTransitionPagerTitleView(context);
        titleView.setNormalColor(ContextCompat.getColor(mContext, normalColorId));
        titleView.setSelectedColor(ContextCompat.getColor(mContext, selectColorId));
        titleView.setMinScale(0.7f);
        titleView.setTextSize(size);
        titleView.setText(mTitleList.get(i).getName());
        titleView.setOnClickListener(view -> {
            if (mOnItemSelectListener != null) {
                mOnItemSelectListener.onItemSelect(i);
            }
        });
        return titleView;
    }


    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }

    private HomeMagicIndicatorAdapter.OnItemSelectListener mOnItemSelectListener;

    public void setOnItemSelectListener(HomeMagicIndicatorAdapter.OnItemSelectListener onItemSelectListener) {
        mOnItemSelectListener = onItemSelectListener;
    }

    public interface OnItemSelectListener {
        void onItemSelect(int position);
    }
}
