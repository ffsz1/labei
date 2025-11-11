package com.vslk.lbgx.ui.find.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.presenter.find.family.FindFamilyPresenter;
import com.vslk.lbgx.presenter.find.family.IFindFamilyView;
import com.vslk.lbgx.ui.find.activity.family.FamilyInfoActivity;
import com.vslk.lbgx.ui.find.adapter.FindFamilyAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.utils.NetworkUtils;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.IFamilyCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

import static com.tongdaxing.xchat_core.Constants.PAGE_START_ZERO;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/26
 * 描述        家族列表界面
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(FindFamilyPresenter.class)
public class FamilyListFragment extends BaseMvpFragment<IFindFamilyView, FindFamilyPresenter>
        implements IFindFamilyView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener {

    private int current = PAGE_START_ZERO;

    private RecyclerView mRecyclerView;
    private FindFamilyAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_family_list;
    }

    @Override
    public void onFindViews() {
        mRecyclerView = mView.findViewById(R.id.recycler_view);
        mRefreshLayout = mView.findViewById(R.id.refresh_layout);
    }

    @Override
    public void onSetListener() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void initiate() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new FindFamilyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);

        showLoading();
        getMvpPresenter().getFamilyList(current);
    }

    @Override
    public void onRefresh() {
        this.current = PAGE_START_ZERO;
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            //获取用户是否加入家族
            getMvpPresenter().checkFamilyJoin();
            getMvpPresenter().getFamilyList(current);
        } else {
            toast("网络异常");
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            this.current++;
            getMvpPresenter().getFamilyList(current);
        } else {
            toast("网络异常");
            mAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
            FamilyInfo cacheInfo = mAdapter.getData().get(position);
            boolean isMyFamily = CoreManager.getCore(IFamilyCore.class).checkIsMyFamily(cacheInfo);
            CoreManager.getCore(IFamilyCore.class).setCacheInfo(cacheInfo);
            FamilyInfoActivity.start(getActivity(), isMyFamily);
        }
    }

    @Override
    public void getFamilyListSuccess(List<FamilyInfo> familynIfos) {
        if (current == PAGE_START_ZERO) {
            hideStatus();
            mRefreshLayout.setRefreshing(false);
            if (!ListUtils.isListEmpty(familynIfos)) {
                mAdapter.setNewData(familynIfos);
            } else {
                showNoData("暂无任何家族信息");
            }
        } else {
            if (!ListUtils.isListEmpty(familynIfos)) {
                mAdapter.loadMoreComplete();
                mAdapter.addData(familynIfos);
            } else {
                mAdapter.loadMoreEnd(true);
            }
        }
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
    }

    @Override
    public void getFamilyListFail(String e) {
        if (current == PAGE_START_ZERO) {
            hideStatus();
            mRefreshLayout.setRefreshing(false);
            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                showNoData("暂无任何家族信息");
            } else {
                showNetworkErr();
            }
        } else {
            mAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void onReloadData() {
        showLoading();
        getMvpPresenter().getFamilyList(current);
    }

    @Override
    public void getCheckFamilyJoinSuccess(FamilyInfo info) {
        CoreManager.notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_REFRESH_INFO);
    }

    public static Fragment newInstance() {
        return new FamilyListFragment();
    }
}
