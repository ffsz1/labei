package com.vslk.lbgx.ui.rank.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vslk.lbgx.base.adapter.BaseIndicatorAdapter;
import com.vslk.lbgx.base.fragment.BaseLazyFragment;
import com.vslk.lbgx.ui.rank.view.IChangeTabListener;
import com.vslk.lbgx.ui.widget.OnPageChangeListener;
import com.tongdaxing.erban.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/26
 * 描述        用户排行榜fragment
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class RankingListFragment extends BaseLazyFragment implements RadioGroup.OnCheckedChangeListener {

    private ViewPager mViewPager;

    public static final int RANKING_TYPE_CHARM = 0, RANKING_TYPE_TYCOON = 1;
    private List<Fragment> mTabs;
    public int type = RANKING_TYPE_CHARM;
    private BaseIndicatorAdapter mTabAdapter;
    private RadioGroup mRadioGroup;
    private IChangeTabListener iChangeTabListener;
    public int currentPosition = 0;
    private RadioButton day;
    private RadioButton week;
    private RadioButton month;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IChangeTabListener) {
            iChangeTabListener = (IChangeTabListener) context;
        }
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_ranking_list;
    }

    @Override
    protected void onInitArguments(Bundle bundle) {
        if (bundle != null) {
            type = bundle.getInt("type", 0);
        }
    }

    @Override
    public void onFindViews() {
        mViewPager = mView.findViewById(R.id.viewPager);
        mRadioGroup = mView.findViewById(R.id.radioGroup);
        day = mView.findViewById(R.id.day);
        week = mView.findViewById(R.id.week);
        month = mView.findViewById(R.id.month);

        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                RankingListFragment.this.onPageSelected(position);
            }
        });
    }

    private void onPageSelected(int position) {
        mRadioGroup.check(position == 0 ? R.id.day : position == 1 ? R.id.week : R.id.month);
        this.currentPosition = position;
        mViewPager.setCurrentItem(position);
        if (iChangeTabListener != null) {
            iChangeTabListener.loadCurrentUserRankingData(type, position);
        }
    }

    @Override
    public void initiate() {

        day.setTextColor(type == 1 ? ContextCompat.getColorStateList(getContext(),R.color.selector_ranking_button_tycoon) : ContextCompat.getColorStateList(getContext(),R.color.selector_ranking_button_charm));
        day.setBackgroundResource(type == 1 ? R.drawable.rangking_rich_tab_background : R.drawable.rangking_charme_tab_background);
        week.setTextColor(type == 1 ? ContextCompat.getColorStateList(getContext(),R.color.selector_ranking_button_tycoon) : ContextCompat.getColorStateList(getContext(),R.color.selector_ranking_button_charm));
        week.setBackgroundResource(type == 1 ? R.drawable.rangking_rich_tab_background : R.drawable.rangking_charme_tab_background);
        month.setTextColor(type == 1 ? ContextCompat.getColorStateList(getContext(),R.color.selector_ranking_button_tycoon) : ContextCompat.getColorStateList(getContext(),R.color.selector_ranking_button_charm));
        month.setBackgroundResource(type == 1 ? R.drawable.rangking_rich_tab_background : R.drawable.rangking_charme_tab_background);
    }

    @Override
    protected void onLazyLoadData() {
        mTabs = new ArrayList<>();
        mTabs.add(RankingItemFragment.newInstance(type, 0));
        mTabs.add(RankingItemFragment.newInstance(type, 1));
        mTabs.add(RankingItemFragment.newInstance(type, 2));

        mTabAdapter = new BaseIndicatorAdapter(getChildFragmentManager(), mTabs);
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onSetListener() {
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    public static Fragment newInstance(int type) {
        Bundle bundle = new Bundle();
        RankingListFragment fragment = new RankingListFragment();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.day:
                if (currentPosition == 0) {
                    return;
                }
                onPageSelected(0);
                break;
            case R.id.week:
                if (currentPosition == 1) {
                    return;
                }
                onPageSelected(1);
                break;
            case R.id.month:
                if (currentPosition == 2) {
                    return;
                }
                onPageSelected(2);
                break;
            default:
                break;
        }
    }
}
