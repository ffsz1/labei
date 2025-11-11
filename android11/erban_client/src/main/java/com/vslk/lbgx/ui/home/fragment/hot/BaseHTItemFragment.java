package com.vslk.lbgx.ui.home.fragment.hot;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tongdaxing.xchat_core.home.BannerInfo;
import com.tongdaxing.xchat_core.home.HomeInfo;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.home.IHomeCoreClient;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.vslk.lbgx.base.fragment.BaseMvpListFragment;
import com.vslk.lbgx.presenter.home.HomeHotPresenter;
import com.vslk.lbgx.presenter.home.IHomeHotView;
import com.vslk.lbgx.ui.home.adpater.HomeHotAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public abstract class BaseHTItemFragment extends BaseMvpListFragment<HomeHotAdapter, IHomeHotView, HomeHotPresenter>
        implements IHomeHotView, BaseQuickAdapter.OnItemClickListener {

    @Override
    protected void initMyView() {
        setClickReload(false);
        hasDefualLoadding = false;
        srlRefresh.setEnableRefresh(true);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) srlRefresh.getLayoutParams();
        lp.setMargins(0, 0, 0, 0);
        srlRefresh.setLayoutParams(lp);
    }

    @Override
    protected void addHead() {
//        rvList.setBackgroundResource(R.color.white);
//        head = new HomeHotHeaderView(mContext);
//        mAdapter.setHeaderAndEmpty(true);
//        mAdapter.addHeaderView(head);
    }

    @Override
    public void initData() {
//        getMvpPresenter().getTopBanner();
    }


    @Override
    protected void onLazyLoadData() {
        onRefreshHomeData();
    }

    private void onRefreshHomeData() {
        if (getMvpPresenter() != null && getMvpPresenter().getMvpView() != null) {
            initData();
        } else {
            dealFail(new Exception(), "暂时还没有热门房间哦");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        onRefreshHomeData();
    }


    @Override
    protected HomeHotAdapter initAdpater() {
        return new HomeHotAdapter(getContext());
    }


    int mDistance = 0;
    final int maxDistance = 255;

    @Override
    protected void initClickListener() {
        mAdapter.setOnItemClickListener(this);
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mDistance += dy;
                float percent = mDistance * 1f / maxDistance;//百分比
                int alpha = (int) (percent * 255);
                CoreManager.notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_RECYCLER_VIEW_LISTENER, alpha);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void getSquareData(List<PublicChatRoomAttachment> list) {
        EventBus.getDefault().post(new Hot.Square(list));
    }

    @Override
    public void getHomeHotRoomSuccess(HomeInfo result) {

    }

    @Override
    public void getPeiPeiList(List<HomeRoom> result) {

    }

    @Override
    public void getHomeHotRoomFail(Exception error) {
        srlRefresh.finishRefresh();
        dealFail(error, "暂时还没有数据哦");
    }


    @Override
    public void getHomeBannerSuccess(List<BannerInfo> response) {
        srlRefresh.finishRefresh();
    }

    @Override
    public void getHomeBannerFail(String errors) {
        srlRefresh.finishRefresh();
    }


    @CoreEvent(coreClientClass = IHomeCoreClient.class)
    public void onAutoJump(BannerInfo bannerInfo) {
        autoJump(bannerInfo.getSkipType(), bannerInfo.getSkipUri(), bannerInfo.getSkipUri(), null);
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onGetUserRoomFail(String msg) {
        toast(msg);
    }
}
