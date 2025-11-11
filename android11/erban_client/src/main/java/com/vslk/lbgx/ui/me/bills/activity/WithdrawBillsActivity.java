package com.vslk.lbgx.ui.me.bills.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.vslk.lbgx.ui.me.bills.fragment.BillIncomeFragment;
import com.vslk.lbgx.ui.me.bills.fragment.BillRedFragment;
import com.vslk.lbgx.ui.me.bills.fragment.WithdrawBillsFragment;
import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.home.adpater.CommonMagicIndicatorAdapter;
import com.vslk.lbgx.ui.me.bills.adapter.WithdrawAdapter;
import com.vslk.lbgx.ui.widget.magicindicator.MagicIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.ViewPagerHelper;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.TabInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现账单记录
 *
 * @author dell
 */
public class WithdrawBillsActivity extends BaseActivity implements View.OnClickListener, CommonMagicIndicatorAdapter.OnItemSelectListener {

    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private AppToolBar mToolBar;

    public static void start(Context context) {
        Intent intent = new Intent(context, WithdrawBillsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_bills);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mMagicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator2);
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.vPager);
    }

    private void initData() {
        List<Fragment> list = new ArrayList<>();
        list.add(new BillIncomeFragment());
        list.add(new WithdrawBillsFragment());
        list.add(new BillRedFragment());
        mViewPager.setAdapter(new WithdrawAdapter(getSupportFragmentManager(), list));
        mViewPager.setOffscreenPageLimit(list.size());
        initMagicIndicator2();
    }

    private void setListener() {
        back(mToolBar);
    }

    private void initMagicIndicator2() {
        final String[] titles = getResources().getStringArray(R.array.bill_withdraw_titles);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        List<TabInfo> tabInfoList = new ArrayList<>(2);
        for (int i = 0; i < titles.length; i++) {
            tabInfoList.add(new TabInfo(i, titles[i]));
        }
        CommonMagicIndicatorAdapter adapter = new CommonMagicIndicatorAdapter(this, tabInfoList, 0);
        adapter.setSelectColorId(R.color.color_6E42D2);
        adapter.setSize(18);
        adapter.setOnItemSelectListener(this);
        commonNavigator.setAdapter(adapter);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    public void onItemSelect(int position) {
        mViewPager.setCurrentItem(position);
    }
}
