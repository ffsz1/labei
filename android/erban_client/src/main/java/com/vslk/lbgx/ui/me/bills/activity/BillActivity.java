package com.vslk.lbgx.ui.me.bills.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.base.adapter.BaseIndicatorAdapter;
import com.vslk.lbgx.ui.home.adpater.CommonMagicIndicatorAdapter;
import com.vslk.lbgx.ui.me.bills.fragment.BillChargeFragment;
import com.vslk.lbgx.ui.me.bills.fragment.BillExpenseFragment;
import com.vslk.lbgx.ui.widget.magicindicator.MagicIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.ViewPagerHelper;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.TabInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 新皮：账单页面
 *
 * @author zwk 2018/5/30
 */
public class BillActivity extends BaseActivity implements CommonMagicIndicatorAdapter.OnItemSelectListener {

    private AppToolBar mToolBar;
    private ViewPager vpBill;
    private MagicIndicator mIndicator;
    private CommonMagicIndicatorAdapter mMsgIndicatorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        setSwipeBackEnable(false);
        initView();
        initListener();
    }

    private void initView() {
        mIndicator = (MagicIndicator) findViewById(R.id.mi_bill_indicator);
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
        vpBill = (ViewPager) findViewById(R.id.vp_bill);
        List<Fragment> mTabs = new ArrayList<>(2);
        mTabs.add(new BillExpenseFragment());
        mTabs.add(new BillChargeFragment());
        List<TabInfo> tabInfoList = new ArrayList<>();
        tabInfoList.add(new TabInfo(1, "送礼记录"));
        tabInfoList.add(new TabInfo(2, "金币收入"));
        mMsgIndicatorAdapter = new CommonMagicIndicatorAdapter(this, tabInfoList, 0);
        mMsgIndicatorAdapter.setSelectColorId(R.color.color_6E42D2);
        mMsgIndicatorAdapter.setSize(17);

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mMsgIndicatorAdapter);
        mIndicator.setNavigator(commonNavigator);
        // must after setNavigator
        LinearLayout titleContainer = commonNavigator.getTitleContainer();
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        vpBill.setAdapter(new BaseIndicatorAdapter(getSupportFragmentManager(), mTabs));
        vpBill.setOffscreenPageLimit(2);
        ViewPagerHelper.bind(mIndicator, vpBill);
    }

    private void initListener() {
        mMsgIndicatorAdapter.setOnItemSelectListener(this);
        back(mToolBar);
    }

    @Override
    public void onItemSelect(int position) {
        vpBill.setCurrentItem(position);
    }

}
