package com.vslk.lbgx.room.avroom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.vslk.lbgx.room.avroom.fragment.base.AbsRoomFragment;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;

import java.util.List;

/**
 * 拍卖房界面
 * Created by 2016/9/22.
 *
 * @author Administrator
 */
public class AuctionFragment extends AbsRoomFragment implements View.OnClickListener {

    private ViewPager viewPager;
    private AuctionRoomFragment roomFragment;
    private AuctionListFragment listFragment;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_chatroom_main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    roomFragment = new AuctionRoomFragment();
                    return roomFragment;
                } else {
                    listFragment = new AuctionListFragment();
                    return listFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onFindViews() {

    }

    @Override
    public void onSetListener() {

    }

    @Override
    public void initiate() {

    }

    @Override
    public void onClick(View v) {

    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                listFragment.onPagerSelect();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    public void onShowActivity(List<ActionDialogInfo> dialogInfos) {
        if (roomFragment != null && dialogInfos != null && dialogInfos.size() > 0) {
            roomFragment.showActivity(dialogInfos);
        }
    }

    @Override
    public void onRoomOnlineNumberSuccess(int onlineNumber) {
        if (roomFragment != null)
            roomFragment.setOnlineNumber();
    }
}