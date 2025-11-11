package com.vslk.lbgx.base.fragment;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

public abstract class BaseNewListFragment<T extends BaseQuickAdapter> extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {
    public SmartRefreshLayout srlRefresh;
    public RecyclerView rvList;
    public T mAdapter;
    public int mPage = Constants.PAGE_START;
    public int pageSize = 20;

    @Override
    public int getRootLayoutId() {
        return R.layout.layout_base_list;
    }

    @Override
    public void onFindViews() {
        srlRefresh = mView.findViewById(R.id.srl_base_refresh);
        rvList = mView.findViewById(R.id.rv_base_list);
        addItemDecoration(rvList);
        rvList.setLayoutManager(initManager());
        mAdapter = initAdpater();
        mAdapter.setEnableLoadMore(false);
        rvList.setAdapter(mAdapter);
    }

    /**
     * 刷新开关
     *
     * @return
     */
    public boolean enableReresh() {
        return true;
    }

    /**
     * 自己设置item 间距
     *
     * @param rvList
     */
    private void addItemDecoration(RecyclerView rvList) {
    }

    /**
     * 自定义提供LayoutManager
     *
     * @return
     */
    protected abstract RecyclerView.LayoutManager initManager();

    /**
     * 提供adpater
     *
     * @return
     */
    protected abstract T initAdpater();

    @Override
    public void onSetListener() {
        if (srlRefresh != null) {
            srlRefresh.setOnRefreshListener(this);
            srlRefresh.setOnLoadmoreListener(this);
        }
    }

    @Override
    public void initiate() {
        showLoading();
        initData();
    }

    public void initData() {

    }

    @Override
    public void onReloadData() {
        super.onReloadData();
        mPage = Constants.PAGE_START;
        showLoading();
        initData();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = Constants.PAGE_START;
        initData();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage++;
        initData();
    }

    public <T> void dealSuccess(ServiceResult<List<T>> response,String emptyStr){
        if (response != null && response.isSuccess() && response.getData() != null) {
            hideStatus();
            if (mPage == Constants.PAGE_START) {//
                srlRefresh.finishRefresh();
                mAdapter.setNewData(response.getData());
            } else {
                if (ListUtils.isListEmpty(response.getData())){
                    srlRefresh.finishLoadmore();
                }else {
                    mAdapter.addData(response.getData());
                    srlRefresh.finishLoadmore();
                }
            }
        } else {
            if (mPage == Constants.PAGE_START) {
                srlRefresh.finishRefresh();
                showNoData(emptyStr);
            } else {
                srlRefresh.finishLoadmore(false);
            }
        }
    }

    public void dealFail(Exception e){
        if (mPage == Constants.PAGE_START) {
            hideStatus();
            srlRefresh.finishRefresh();
            showNetworkErr();
        } else {
            mPage--;
            srlRefresh.finishLoadmore(false);
            toast(e.getMessage());
        }
    }
}
