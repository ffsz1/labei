package com.vslk.lbgx.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.home.adpater.HomeHotHeaderAdapter;
import com.vslk.lbgx.ui.home.fragment.hot.Hot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * @author dell
 */
public abstract class BaseMvpListFragment<T extends BaseQuickAdapter, V extends IMvpBaseView, P extends AbstractMvpPresenter<V>>
        extends BaseMvpFragment<V, P> implements OnRefreshListener, OnLoadmoreListener {
    public SmartRefreshLayout srlRefresh;
    public RecyclerView rvList;
    public RecyclerView rvTop;
    public T mAdapter;
    private int defaultSize = Constants.PAGE_START;
    public int mPage = 1;
    public int pageSize = 20;
    public boolean hasDefualLoadding = true;
    public HomeHotHeaderAdapter topAdapter;


    @Override
    public int getRootLayoutId() {
        return R.layout.layout_base_list_lb100;
    }

    @Override
    public void onFindViews() {
        getMyArguments();
        mPage = defaultSize;
        srlRefresh = mView.findViewById(R.id.srl_base_refresh);
        rvList = mView.findViewById(R.id.rv_base_list);
        rvTop = new RecyclerView(getActivity());
        rvTop.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        initMyView();
        addItemDecoration();
        rvList.setLayoutManager(initManager());
        mAdapter = initAdpater();
        mAdapter.setEnableLoadMore(false);
        rvList.setAdapter(mAdapter);
        rvTop.setLayoutManager(new GridLayoutManager(mContext, 3));
        topAdapter = new HomeHotHeaderAdapter(mContext);
        topAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AVRoomActivity.start(getActivity(), topAdapter.getData().get(position).getUid());
            }
        });
        rvTop.setAdapter(topAdapter);
        mAdapter.addHeaderView(rvTop);
        addHead();
    }


    //设置数据
    public void setTopData(List<HomeRoom> roomList) {
        if (!ListUtils.isListEmpty(roomList)) {
            rvTop.setVisibility(View.VISIBLE);
            topAdapter.setNewData(roomList);
        } else {
            topAdapter.setNewData(null);
            rvTop.setVisibility(View.GONE);
        }
    }


    protected void addHead() {

    }

    protected void getMyArguments() {

    }


    protected abstract void initMyView();

    /**
     * 刷新开关
     */
    public boolean enableReresh() {
        return true;
    }

    /**
     * 自己设置item 间距
     */
    public void addItemDecoration() {

    }

    /**
     * 自定义提供LayoutManager
     */
    protected RecyclerView.LayoutManager initManager() {
        return new LinearLayoutManager(mContext);
    }

    /**
     * 提供adpater
     */
    protected abstract T initAdpater();

    @Override
    public void onSetListener() {
        initClickListener();
        if (srlRefresh != null) {
            srlRefresh.setOnRefreshListener(this);
            srlRefresh.setOnLoadmoreListener(this);
        }
    }

    protected abstract void initClickListener();

    @Override
    public void initiate() {
        if (hasDefualLoadding) {
            showLoading();
        }
        initData();
    }

    public void initData() {

    }

    @Override
    public void onReloadData() {
        super.onReloadData();
        mPage = defaultSize;
        if (hasDefualLoadding) {
            showLoading();
        }
        initData();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = defaultSize;
        initData();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage++;
        initData();
    }

    public <D> void dealSuccess(ServiceResult<List<D>> response, String emptyStr) {
        hideStatus();
        if (response != null && response.isSuccess() && !ListUtils.isListEmpty(response.getData())) {
            if (mPage == defaultSize) {
                srlRefresh.finishRefresh();
                mAdapter.setNewData(response.getData());
            } else {
                if (ListUtils.isListEmpty(response.getData())) {
                    srlRefresh.finishLoadmoreWithNoMoreData();
                } else {
                    mAdapter.addData(response.getData());
                    srlRefresh.finishLoadmore();
                }
            }
        } else {
            if (mPage == defaultSize) {
                srlRefresh.finishRefresh();
                showNoData(emptyStr);
                srlRefresh.finishLoadmoreWithNoMoreData();
            } else {
                mPage--;
                srlRefresh.finishLoadmore(false);
            }
        }
    }

    public void dealFail(Exception e) {
        hideStatus();
        if (mPage == defaultSize) {
            srlRefresh.finishRefresh(false);
            showNetworkErr();
            srlRefresh.finishLoadmoreWithNoMoreData();
        } else {
            mPage--;
            srlRefresh.finishLoadmore(false);
            toast(e.getMessage());
        }
    }

    public void dealFail(Exception e, String noMsg) {
        hideStatus();
        if (mPage == defaultSize) {
            srlRefresh.finishRefresh(false);
            srlRefresh.finishLoadmoreWithNoMoreData();
            if (NetworkUtil.isNetAvailable(getActivity())) {
                showNoData(noMsg);
            } else {
                showNetworkErr();
            }
        } else {
            mPage--;
            srlRefresh.finishLoadmore(false);
            toast(e.getMessage());
        }
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }
}
