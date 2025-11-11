package com.vslk.lbgx.ui.home.fragment.hot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;
import com.vslk.lbgx.ui.home.fragment.HomeFragment;
import com.vslk.lbgx.ui.home.view.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class HomeHTFragment extends Fragment {

    NoScrollViewPager nosVp;

    private ArrayList<BaseHTItemFragment> htItemFragments;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        htItemFragments = new ArrayList<>();
        HomePeiPeiFragment homePeiPeiFragment = new HomePeiPeiFragment();
        HomeMenXinFragment homeMenXinFragment = new HomeMenXinFragment();
        htItemFragments.add(new HomeHotFragment());
        htItemFragments.add(homePeiPeiFragment);
        htItemFragments.add(homeMenXinFragment);
        HTVpAdapter htVpAdapter = new HTVpAdapter(getChildFragmentManager());
        nosVp.setOffscreenPageLimit(htItemFragments.size());
        nosVp.setNoScroll(true);
        nosVp.setAdapter(htVpAdapter);
    }

    public void switchTab(int index) {
        switch (index) {
            case HomeFragment.RB_ITEM_ID:
                nosVp.setCurrentItem(0, false);
                break;
            case HomeFragment.RB_ITEM_ID + 1:
                nosVp.setCurrentItem(1, false);
                break;
            case HomeFragment.RB_ITEM_ID + 2:
                nosVp.setCurrentItem(2, false);
                break;
        }
    }

    private class HTVpAdapter extends FragmentPagerAdapter {

        public HTVpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return htItemFragments.get(position);
        }

        @Override
        public int getCount() {
            return htItemFragments.size();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSquareData(Hot.Square square) {
        if (upHeadListener!=null) upHeadListener.onFriends(square.getList());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopList(Hot.TopList list) {
        if (upHeadListener != null) upHeadListener.onTopList(list.getAgreeRecommendRooms());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpTab(Hot.upTab tab) {
        if (upHeadListener != null) upHeadListener.upTabView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hot_tab, null);
        nosVp = rootView.findViewById(R.id.nos_vp);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    private IUpHeadListener upHeadListener;


    public void setUpHeadListener(IUpHeadListener upHeadListener) {
        this.upHeadListener = upHeadListener;
    }

    public interface IUpHeadListener {
        void onTopList(List<HomeRoom> agreeRecommendRooms);

        void onFriends(List<PublicChatRoomAttachment> list);

        void upTabView();
    }
}
