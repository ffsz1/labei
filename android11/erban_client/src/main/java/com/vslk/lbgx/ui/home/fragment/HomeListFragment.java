package com.vslk.lbgx.ui.home.fragment;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseMvpListFragment;
import com.vslk.lbgx.presenter.home.HomeListPresenter;
import com.vslk.lbgx.presenter.home.IHomeListView;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.home.adpater.HomeNormalAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

/**
 * 首页普通分类列表页
 * @zwk
 */
@CreatePresenter(HomeListPresenter.class)
public class HomeListFragment extends BaseMvpListFragment<HomeNormalAdapter, IHomeListView, HomeListPresenter> implements IHomeListView, BaseQuickAdapter.OnItemClickListener {
    private int tabId = -1;
    @Override
    protected void getMyArguments() {
        tabId = getArguments().getInt("tabId", -1);
    }

    @Override
    protected void initMyView() {
        hasDefualLoadding = false;
        setClickReload(false);
        srlRefresh.setEnableRefresh(false);
        srlRefresh.setEnableOverScrollDrag(false);
        srlRefresh.setEnableOverScrollBounce(false);
    }

    @Override
    protected HomeNormalAdapter initAdpater() {
        return new HomeNormalAdapter(mContext);
    }

    @Override
    protected void initClickListener() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onLazyLoadData() {
        super.onLazyLoadData();
        mPage = Constants.PAGE_START;
        showLoading();
        getMvpPresenter().getHomeRoomListById(tabId, mPage, pageSize);
    }

    @Override
    public void getHomeRoomListByIdSuccess(ServiceResult<List<HomeRoom>> homeRoomList) {
        dealSuccess(homeRoomList, "该分类下面还没有房间哦！");
    }

    @Override
    public void getHomeRoomListByIdFail(Exception error) {
        dealFail(error, "该分类下面还没有房间哦！");
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        getMvpPresenter().getHomeRoomListById(tabId, mPage, pageSize);
    }

    @Override
    public void onReloadData() {
        super.onReloadData();
        getMvpPresenter().getHomeRoomListById(tabId, mPage, pageSize);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        getMvpPresenter().getHomeRoomListById(tabId, mPage, pageSize);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData()) && mAdapter.getData().size() > position) {
            AVRoomActivity.start(getActivity(), mAdapter.getData().get(position).getUid());
        }
    }
}
