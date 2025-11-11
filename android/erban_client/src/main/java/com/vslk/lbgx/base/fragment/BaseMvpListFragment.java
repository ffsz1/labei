package com.vslk.lbgx.base.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

/**
 * @author dell
 */
public abstract class BaseMvpListFragment<T extends BaseQuickAdapter, V extends IMvpBaseView, P extends AbstractMvpPresenter<V>>
        extends BaseMvpFragment<V, P> implements OnRefreshListener, OnLoadmoreListener {
    public SmartRefreshLayout srlRefresh;
    public RecyclerView rvList;
    public T mAdapter;
    private int defaultSize = Constants.PAGE_START;
    public int mPage = 1;
    public int pageSize = 20;
    public boolean hasDefualLoadding = true;


    @Override
    public int getRootLayoutId() {
        return R.layout.layout_base_list;
    }

    @Override
    public void onFindViews() {
        getMyArguments();
        mPage = defaultSize;
        srlRefresh = mView.findViewById(R.id.srl_base_refresh);
        rvList = mView.findViewById(R.id.rv_base_list);
        initMyView();
        addItemDecoration();
        rvList.setLayoutManager(initManager());
        mAdapter = initAdpater();
        mAdapter.setEnableLoadMore(false);
        rvList.setAdapter(mAdapter);
        addHead();
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
