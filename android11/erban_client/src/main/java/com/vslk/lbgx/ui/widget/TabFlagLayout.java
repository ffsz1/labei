package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.ConvertUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.TabInfo;

import java.util.List;

/**
 * 单选按钮列表 -- 用于首页显示分类
 *
 * @author zwk
 */
public class TabFlagLayout extends HorizontalScrollView implements RadioGroup.OnCheckedChangeListener {
    private Context mContext;
    private RadioGroup mTabContainer;
    private int currentPosition = 0;
    private OnTabItemSelectListener onTabItemSelectListener;
    private int screenWith;
    private int margin = 20;
    private int height = 44;

    public TabFlagLayout(Context context) {
        this(context, null);
    }

    public TabFlagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabFlagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        screenWith = ScreenUtil.getScreenWidth(context);
        margin = ConvertUtils.dp2px(10);
        height = ConvertUtils.dp2px(22);
        LinearLayout llContainer = new LinearLayout(context);
        llContainer.setOrientation(LinearLayout.HORIZONTAL);
        llContainer.setLayoutParams(new HorizontalScrollView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(llContainer);
        mTabContainer = new RadioGroup(context);
        mTabContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabContainer.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        mTabContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        llContainer.addView(mTabContainer);
        TabInfo hot = new TabInfo(-1, "热门");
        addItem(0, hot);
        setSelectPosition(0);
        mTabContainer.setOnCheckedChangeListener(this);
    }

    public void addData(List<TabInfo> response) {
        if (response == null)
            return;
        if (mContext == null)
            return;
        if (mTabContainer != null && mTabContainer.getChildCount() > 1)
            mTabContainer.removeViews(1, mTabContainer.getChildCount() - 1);
        for (int i = 1; i < response.size(); i++) {
            addItem(i, response.get(i));
        }
    }

    private void addItem(int i, TabInfo tabInfo) {
        RadioButton rbItem = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.item_tag_flag, null);
        mTabContainer.addView(rbItem);
        RadioGroup.LayoutParams rp = (RadioGroup.LayoutParams) rbItem.getLayoutParams();
        rp.height = height;
        rp.setMargins(i == 0 ? margin * 2 : margin, 0, margin - 4, 0);
        rbItem.setPadding(margin, 0, margin, 0);
        rbItem.setLayoutParams(rp);
        rbItem.setId(i);
        rbItem.setText(tabInfo.getName());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (currentPosition == checkedId)
            return;
        currentPosition = checkedId;
        if (onTabItemSelectListener != null)
            onTabItemSelectListener.onItemSelectListener(this, checkedId);
        View child = mTabContainer.getChildAt(checkedId);
        if (child == null)
            return;
        int[] location = new int[2];
        child.getLocationOnScreen(location);
        int offset = location[0] + child.getMeasuredWidth() / 2 - screenWith / 2;
        post(new Runnable() {
            @Override
            public void run() {
                smoothScrollBy(offset, 0);
            }
        });
    }


    public interface OnTabItemSelectListener {
        void onItemSelectListener(HorizontalScrollView parent, int position);
    }

    public void setOnTabItemSelectListener(OnTabItemSelectListener onTabItemSelectListener) {
        this.onTabItemSelectListener = onTabItemSelectListener;
    }

    public void setSelectPosition(int position) {
        if (mTabContainer != null) {
            if (mTabContainer.getChildAt(position) != null && mTabContainer.getChildAt(position) instanceof RadioButton) {
                currentPosition = position;
                ((RadioButton) mTabContainer.getChildAt(position)).setChecked(true);
            }
        }
    }
}
