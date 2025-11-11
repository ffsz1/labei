package com.vslk.lbgx.ui.common.widget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.ui.home.adpater.CommonMagicIndicatorAdapter;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.TabInfo;

import java.util.List;

/**
 * Function:
 * Author: Edward on 2019/4/10
 */
public class RoomMagicIndicatorAdapter extends CommonNavigatorAdapter {

    private Context mContext;
    private List<TabInfo> mTitleList;

    private int normalColorId = R.color.color_999999;
    private int selectColorId = R.color.color_FF0D6D;
    private boolean isWrap = false;

    public RoomMagicIndicatorAdapter(Context context, List<TabInfo> titleList) {
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

        CommonPagerTitleView pagerTitleView = new CommonPagerTitleView(context);
        pagerTitleView.setContentView(R.layout.layout_img_indicator);

        final ImageView indicator = pagerTitleView.findViewById(R.id.indicator_iv);
        final TextView indicatorText = pagerTitleView.findViewById(R.id.indicator);
        indicatorText.setTextSize(size);

        indicatorText.setText(mTitleList.get(i).getName());
        pagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

            @Override
            public void onSelected(int index, int totalCount) {
                indicatorText.getPaint().setFakeBoldText(true);
                if (mOnPagerTitleChangeListener != null) {
                    mOnPagerTitleChangeListener.onSelected(index, totalCount);
                }
            }

            @Override
            public void onDeselected(int index, int totalCount) {
                indicatorText.getPaint().setFakeBoldText(false);
                if (mOnPagerTitleChangeListener != null) {
                    mOnPagerTitleChangeListener.onDeselected(index, totalCount);
                }
            }

            @Override
            public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
//                // 实现颜色渐变
//                int color = ArgbEvaluatorHolder.eval(leavePercent,
//                        ContextCompat.getColor(mContext, selectColorId),
//                        ContextCompat.getColor(mContext, normalColorId));
//                indicatorText.setTextColor(color);
                indicatorText.setScaleX(1.4f + (1f - 1.4f) * leavePercent);
                indicatorText.setScaleY(1.4f + (1f - 1.4f) * leavePercent);

                if (leavePercent > 0.75) {
                    indicator.setVisibility(View.INVISIBLE);
                    indicatorText.setTextColor(ContextCompat.getColorStateList(mContext, normalColorId));
                }
                if (mOnPagerTitleChangeListener != null) {
                    mOnPagerTitleChangeListener.onDeselected(index, totalCount);
                }
            }

            @Override
            public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
//                // 实现颜色渐变
//                int color = ArgbEvaluatorHolder.eval(enterPercent,
//                        ContextCompat.getColor(mContext, normalColorId),
//                        ContextCompat.getColor(mContext, selectColorId));
//                indicatorText.setTextColor(color);
                indicatorText.setScaleX(1f + (1.4f - 1f) * enterPercent);
                indicatorText.setScaleY(1f + (1.4f - 1f) * enterPercent);
                if (enterPercent > 0.75) {
                    indicator.setVisibility(View.VISIBLE);
                    indicatorText.setTextColor(ContextCompat.getColorStateList(mContext, selectColorId));
                }
            }
        });
        pagerTitleView.setOnClickListener(view -> {
            if (mOnItemSelectListener != null) {
                mOnItemSelectListener.onItemSelect(i);
            }
        });
        return pagerTitleView;
    }


    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }

    private CommonMagicIndicatorAdapter.OnItemSelectListener mOnItemSelectListener;

    public void setOnItemSelectListener(CommonMagicIndicatorAdapter.OnItemSelectListener onItemSelectListener) {
        mOnItemSelectListener = onItemSelectListener;
    }

    private CommonPagerTitleView.OnPagerTitleChangeListener mOnPagerTitleChangeListener;

    public void setOnPagerTitleChangeListener(CommonPagerTitleView.OnPagerTitleChangeListener onPagerTitleChangeListener) {
        mOnPagerTitleChangeListener = onPagerTitleChangeListener;
    }
}
