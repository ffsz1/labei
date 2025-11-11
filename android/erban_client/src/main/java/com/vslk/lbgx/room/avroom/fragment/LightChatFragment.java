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
 * 轻聊房
 *
 * @author chenran
 * @date 2017/10/1
 */
public class LightChatFragment extends AbsRoomFragment {

    private ViewPager viewPager;
    private LightChatRoomFragment roomFragment;
    private LightChatConsumeFragment listFragment;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_chatroom_main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewpager);

        mFragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    if (roomFragment == null) {
                        roomFragment = new LightChatRoomFragment();
                    }
                    return roomFragment;
                } else {
                    if (listFragment == null) {
                        listFragment = new LightChatConsumeFragment();
                    }
                    return listFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        viewPager.setAdapter(mFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setCurrentItem(0);
    }

    FragmentPagerAdapter mFragmentPagerAdapter;

    public void showListFragment() {
        viewPager.setCurrentItem(1, true);
    }

    public void showRoomFragment() {
        viewPager.setCurrentItem(0, true);
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

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
//                listFragment.onPagerSelect();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


    @Override
    public void onShowActivity(List<ActionDialogInfo> dialogInfos) {
        if (roomFragment != null)
            roomFragment.showActivity(dialogInfos);
    }

    @Override
    public void onRoomOnlineNumberSuccess(int onlineNumber) {
        if (roomFragment != null)
            roomFragment.setListeningNumber();
    }
}
