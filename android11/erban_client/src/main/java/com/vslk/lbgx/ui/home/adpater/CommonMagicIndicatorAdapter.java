package com.vslk.lbgx.ui.home.adpater;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
 * <p> 公共多个滑动tab样式 </p>
 *
 * @author Administrator
 * @date 2017/11/15
 */
public class CommonMagicIndicatorAdapter extends CommonNavigatorAdapter {

    private Context mContext;
    private List<TabInfo> mTitleList;
    private int mBottomMargin;

    private int normalColorId = R.color.color_AEACAF;
    private int selectColorId = R.color.color_8C63F6;

    public CommonMagicIndicatorAdapter(Context context, List<TabInfo> titleList, int bottomMargin) {
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
        indicator.setColors(ContextCompat.getColor(mContext, selectColorId));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.bottomMargin = mBottomMargin;
        indicator.setLayoutParams(lp);
        return indicator;
    }

    private OnItemSelectListener mOnItemSelectListener;

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        mOnItemSelectListener = onItemSelectListener;
    }

    public interface OnItemSelectListener {
        void onItemSelect(int position);
    }
}