package com.vslk.lbgx.ui.rank.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.base.adapter.BaseIndicatorAdapter;
import com.tongdaxing.xchat_core.WebUrl;
import com.vslk.lbgx.ui.home.adpater.CommonMagicIndicatorAdapter;
import com.vslk.lbgx.ui.rank.fragment.GradeRuleFragment;
import com.vslk.lbgx.ui.widget.dialog.ShareDialog;
import com.vslk.lbgx.ui.widget.magicindicator.MagicIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.ViewPagerHelper;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_core.redpacket.bean.WebViewInfo;
import com.tongdaxing.xchat_core.share.IShareCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/3
 * 描述        用户等级排行信息
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class UserGradeRuleActivity extends BaseActivity implements CommonMagicIndicatorAdapter.OnItemSelectListener,
        ShareDialog.OnShareDialogItemClick {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    MagicIndicator mIndicator;
    @BindView(R.id.arrow_back)
    ImageView arrowBack;
    @BindView(R.id.share)
    ImageView share;

    List<Fragment> mTabs;
    private BaseIndicatorAdapter mTabAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserGradeRuleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_grade_rule);
        ButterKnife.bind(this);
        onFindViews();
        onSetListener();
    }

    private void onFindViews() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mIndicator = (MagicIndicator) findViewById(R.id.indicator);

        mTabs = new ArrayList<>();
        mTabs.add(GradeRuleFragment.newInstance(false));
        mTabs.add(GradeRuleFragment.newInstance(true));

        List<TabInfo> tabInfoList = new ArrayList<>();
        tabInfoList.add(new TabInfo(1, getString(R.string.gold_grade)));
        tabInfoList.add(new TabInfo(2, getString(R.string.charm_grade)));
        CommonMagicIndicatorAdapter indicatorAdapter = new CommonMagicIndicatorAdapter(this, tabInfoList, 0);
        indicatorAdapter.setSelectColorId(R.color.color_ff2d3a47);
        indicatorAdapter.setOnItemSelectListener(this);

        indicatorAdapter.setSize(17);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(indicatorAdapter);
        mIndicator.setNavigator(commonNavigator);

        // must after setNavigator
        LinearLayout titleContainer = commonNavigator.getTitleContainer();
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        mTabAdapter = new BaseIndicatorAdapter(getSupportFragmentManager(), mTabs);
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOffscreenPageLimit(2);
        ViewPagerHelper.bind(mIndicator, mViewPager);
    }

    @Override
    public void onItemSelect(int position) {
        mViewPager.setCurrentItem(position);
    }

    private void onSetListener() {
        arrowBack.setOnClickListener(view -> finish());
        share.setOnClickListener(view -> {
            ShareDialog shareDialog = new ShareDialog(this);
            shareDialog.setOnShareDialogItemClick(this);
            shareDialog.show();
        });
    }

    @Override
    public void onSharePlatformClick(Platform platform) {
        WebViewInfo webViewInfo = new WebViewInfo();
        webViewInfo.setTitle(getString(R.string.share_h5_title));
        webViewInfo.setImgUrl(WebUrl.SHARE_DEFAULT_LOGO);
        webViewInfo.setDesc(getString(R.string.share_h5_desc));
        webViewInfo.setShowUrl(WebUrl.SHARE_DOWNLOAD);
        CoreManager.getCore(IShareCore.class).shareH5(webViewInfo, platform);
    }
}
