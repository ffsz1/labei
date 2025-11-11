package com.vslk.lbgx.ui.home.adpater;

import android.content.Context;
import android.graphics.Color;

import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.TabInfo;

import java.util.List;

/**
 * <p> 公共多个滑动tab样式 </p>
 *
 * @author Administrator
 * @date 2017/11/15
 */
public class CircleMagicIndicatorAdapter extends CommonNavigatorAdapter {

    private List<TabInfo> mTitleList;

    public CircleMagicIndicatorAdapter(List<TabInfo> titleList) {
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

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
        clipPagerTitleView.setText(mTitleList.get(index).getName());
        clipPagerTitleView.setTextColor(Color.parseColor("#FFFFFF"));
        clipPagerTitleView.setClipColor(Color.parseColor("#FF2079"));
        clipPagerTitleView.setOnClickListener(v -> {
            if (mOnItemSelectListener != null) {
                mOnItemSelectListener.onItemSelect(index);
            }
        });
        return clipPagerTitleView;
    }


    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        float navigatorHeight = context.getResources().getDimension(R.dimen.dp_32);
        float borderWidth = UIUtil.dip2px(context, 1);
        float lineHeight = navigatorHeight - 2 * borderWidth;
        indicator.setLineHeight(lineHeight);
        indicator.setRoundRadius(lineHeight / 2);
        indicator.setYOffset(borderWidth);
        indicator.setColors(Color.parseColor("#FFFFFF"));
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