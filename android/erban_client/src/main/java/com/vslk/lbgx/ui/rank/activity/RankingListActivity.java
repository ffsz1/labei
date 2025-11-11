package com.vslk.lbgx.ui.rank.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.base.adapter.BaseIndicatorAdapter;
import com.vslk.lbgx.ui.home.adpater.CommonMagicIndicatorAdapter;
import com.vslk.lbgx.ui.rank.adapter.RankingIndicatorAdapter;
import com.vslk.lbgx.ui.rank.fragment.RankingListFragment;
import com.vslk.lbgx.ui.rank.view.IChangeTabListener;
import com.vslk.lbgx.ui.widget.magicindicator.MagicIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.RankingInfo;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vslk.lbgx.ui.rank.fragment.RankingListFragment.RANKING_TYPE_CHARM;
import static com.vslk.lbgx.ui.rank.fragment.RankingListFragment.RANKING_TYPE_TYCOON;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/26
 * 描述        排行榜
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class RankingListActivity extends BaseActivity implements CommonMagicIndicatorAdapter.OnItemSelectListener, IChangeTabListener {

    @BindView(R.id.indicator)
    MagicIndicator mIndicator;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.arrow_back)
    ImageView mArrowBack;
    @BindView(R.id.ll_my_data)
    LinearLayout llMyData;
    @BindView(R.id.iv_user_head)
    ImageView ivUserHead;
    @BindView(R.id.tv_value)
    TextView tvValue;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.ranking_bg)
    ImageView mRankingBg;

    List<Fragment> mTabs;
    private BaseIndicatorAdapter mTabAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, RankingListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rank);
        ButterKnife.bind(this);
        initView();
        loadCurrentUserRankingData(1, 0);
        setOnListener();
    }

    private int curType = -1, curDateType = -1;//用于防止频繁加载

    /**
     * @param type
     * @param datetype
     */
    @Override
    public void loadCurrentUserRankingData(int type, int datetype) {
        if (type == curType && curDateType == datetype) {
            return;
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));//fixme 重现这里的uid为0的情况
        params.put("type", String.valueOf(type + 1));
        params.put("datetype", String.valueOf(datetype + 1));
        params.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        OkHttpManager.getInstance().getRequest(UriProvider.getMyselfRankingValue(), params, new OkHttpManager.MyCallBack<ServiceResult<RankingInfo>>() {
            @Override
            public void onError(Exception e) {
                llMyData.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(ServiceResult<RankingInfo> response) {
                if (response == null) {
                    onError(new Exception());
                    return;
                }
                if (response.isSuccess() && response.getData() != null && response.getData().getTotalNum() > 0) {
                    llMyData.setVisibility(View.VISIBLE);
                    ImageLoadUtils.loadCircleImage(getBaseContext(), response.getData().getAvatar(), ivUserHead, R.drawable.ic_default_avatar);
                    tvValue.setText(String.valueOf((int) response.getData().getTotalNum()));
                    if (type == 0) {
                        ivIcon.setImageResource(R.mipmap.ic_ranking_charm);
                    } else {
                        ivIcon.setImageResource(R.mipmap.ic_ranking_gold);
                    }
                } else {
                    onError(new Exception());
                }
            }
        });
    }

    private RankingListFragment rankingListFragment1, rankingListFragment2;

    private void initView() {
        mTabs = new ArrayList<>(2);
        rankingListFragment1 = (RankingListFragment) RankingListFragment.newInstance(RANKING_TYPE_TYCOON);
        rankingListFragment2 = (RankingListFragment) RankingListFragment.newInstance(RANKING_TYPE_CHARM);
        mTabs.add(rankingListFragment1);
        mTabs.add(rankingListFragment2);

        List<TabInfo> tabInfoList = new ArrayList<>();
        tabInfoList.add(new TabInfo(2, getString(R.string.ranking_list_for_tycoon)));
        tabInfoList.add(new TabInfo(1, getString(R.string.ranking_list_for_charm)));
        RankingIndicatorAdapter indicatorAdapter = new RankingIndicatorAdapter(this, tabInfoList, 0);
        indicatorAdapter.setNormalColorId(R.color.white_transparent_50);
        indicatorAdapter.setSelectColorId(R.color.white);
        indicatorAdapter.setOnItemSelectListener(this);
        indicatorAdapter.setLine(false);
        indicatorAdapter.setSize(18);
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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.onPageSelected(position);
                changeCurrentUserRankingData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mIndicator.onPageScrollStateChanged(state);
            }
        });
    }

    private void changeCurrentUserRankingData(int position) {
        if (position == 0) {
            if (rankingListFragment1 != null) {
                loadCurrentUserRankingData(rankingListFragment1.type, rankingListFragment1.currentPosition);
                mRankingBg.setImageResource(R.mipmap.ranking_tycoon_bg);
            }
        } else {
            if (rankingListFragment2 != null) {
                loadCurrentUserRankingData(rankingListFragment2.type, rankingListFragment2.currentPosition);
                mRankingBg.setImageResource(R.mipmap.ranking_charm_bg);
            }
        }
    }

    @Override
    public void onItemSelect(int position) {
        mViewPager.setCurrentItem(position);
        changeCurrentUserRankingData(position);
    }

    private void setOnListener() {
        mArrowBack.setOnClickListener(view -> finish());
    }
}
