package com.vslk.lbgx.ui.rank.adapter;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.vslk.lbgx.ui.home.adpater.CommonMagicIndicatorAdapter;
import com.vslk.lbgx.ui.widget.ScaleTransitionPagerTitleView;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.TabInfo;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/26
 * 描述        排行榜顶部指示适配器
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class RankingIndicatorAdapter extends CommonNavigatorAdapter {

    private Context mContext;
    private List<TabInfo> mTitleList;
    private int mBottomMargin;

    private int normalColorId = R.color.color_AEACAF;
    private int selectColorId = R.color.color_8C63F6;
    private boolean isLine = true;

    public RankingIndicatorAdapter(Context context, List<TabInfo> titleList, int bottomMargin) {
        mContext = context;
        mTitleList = titleList;
        mBottomMargin = bottomMargin;
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

    public void setLine(boolean line) {
        isLine = line;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int i) {
        ScaleTransitionPagerTitleView scaleTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context);
        scaleTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(mContext, normalColorId));
        scaleTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, selectColorId));
        scaleTransitionPagerTitleView.setMinScale(0.9f);
        scaleTransitionPagerTitleView.setTextSize(size);
        scaleTransitionPagerTitleView.setText(mTitleList.get(i).getName());
        scaleTransitionPagerTitleView.setOnClickListener(view -> {
            if (mOnItemSelectListener != null) {
                mOnItemSelectListener.onItemSelect(i);
            }

        });
        return scaleTransitionPagerTitleView;
    }


    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineHeight(UIUtil.dip2px(mContext, 3));
        indicator.setRoundRadius(UIUtil.dip2px(mContext, 3));
        indicator.setLineWidth(UIUtil.dip2px(mContext, 20));
        if (isLine) {
            indicator.setColors(ContextCompat.getColor(mContext, selectColorId));
        } else {
            indicator.setColors(ContextCompat.getColor(mContext, R.color.transparent));
        }
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.bottomMargin = mBottomMargin;
        indicator.setLayoutParams(lp);
        return indicator;
    }

    private CommonMagicIndicatorAdapter.OnItemSelectListener mOnItemSelectListener;

    public void setOnItemSelectListener(CommonMagicIndicatorAdapter.OnItemSelectListener onItemSelectListener) {
        mOnItemSelectListener = onItemSelectListener;
    }

    public interface OnItemSelectListener {
        void onItemSelect(int position);
    }
}
