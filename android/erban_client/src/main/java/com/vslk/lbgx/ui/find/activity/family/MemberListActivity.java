package com.vslk.lbgx.ui.find.activity.family;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.presenter.find.family.FindFamilyPresenter;
import com.vslk.lbgx.presenter.find.family.IFindFamilyView;
import com.vslk.lbgx.ui.find.adapter.MembersAdapter;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.utils.NetworkUtils;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.bean.MemberInfo;
import com.tongdaxing.xchat_core.find.family.bean.MemberListInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tongdaxing.xchat_core.Constants.PAGE_START_ZERO;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/27
 * 描述        申请记录
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(FindFamilyPresenter.class)
public class MemberListActivity extends BaseMvpActivity<IFindFamilyView, FindFamilyPresenter> implements
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, IFindFamilyView,
        BaseQuickAdapter.OnItemClickListener {

    private boolean isMyFamily;
    private FamilyInfo familyInfo;
    private int currentPage = PAGE_START_ZERO;

    @BindView(R.id.toolbar) AppToolBar mToolBar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;

    private MembersAdapter mAdapter;

    public static void start(Context context, boolean isMyFamily) {
        Intent intent = new Intent(context, MemberListActivity.class);
        intent.putExtra("isMyFamily", isMyFamily);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isMyFamily = getIntent().getBooleanExtra("isMyFamily", false);
        setContentView(R.layout.activity_apply_msg);
        ButterKnife.bind(this);
        initiate();
        initData();
        onSetListener();
    }

    private void initiate() {
        mToolBar.setTitle("家族成员");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MembersAdapter();
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        familyInfo = cacheInfo();
        if (familyInfo != null) {
            showLoading();
            getMvpPresenter().getMemberList(currentPage, familyInfo);
        }
    }

    private void onSetListener() {
        mAdapter.setOnItemClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mToolBar.setOnBackBtnListener(view -> finish());
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    @Override
    public void onRefresh() {
        this.currentPage = PAGE_START_ZERO;
        if (NetworkUtils.isNetworkAvailable(this) && familyInfo != null) {
            getMvpPresenter().getMemberList(currentPage, familyInfo);
        } else {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (NetworkUtils.isNetworkAvailable(this) && familyInfo != null) {
            currentPage++;
            getMvpPresenter().getMemberList(currentPage, familyInfo);
        } else {
            mAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
            MemberInfo memberInfo = mAdapter.getData().get(position);
            UserInfoActivity.start(this, memberInfo.getUid());
        }
    }

    @Override
    public void getMemberListSuccess(MemberListInfo memberListInfo) {
        if (currentPage == PAGE_START_ZERO) {
            hideStatus();
            mRefreshLayout.setRefreshing(false);
            if (!ListUtils.isListEmpty(memberListInfo.getFamilyTeamJoinDTOS())) {
                mAdapter.setNewData(memberListInfo.getFamilyTeamJoinDTOS());
            } else {
                showNoData("暂无任何申请记录");
            }
        } else {
            if (!ListUtils.isListEmpty(memberListInfo.getFamilyTeamJoinDTOS())) {
                mAdapter.loadMoreComplete();
                mAdapter.addData(memberListInfo.getFamilyTeamJoinDTOS());
            } else {
                mAdapter.loadMoreEnd(true);
            }
        }
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
    }

    @Override
    public void getMemberListFail(String msg) {
        if (currentPage == PAGE_START_ZERO) {
            hideStatus();
            mRefreshLayout.setRefreshing(false);
            showNoData("暂无任何申请记录");
        } else {
            mAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void getApplyMsgListFail(String msg) {

    }

    @Override
    public void onReloadDate() {
        this.currentPage = PAGE_START_ZERO;
        if (NetworkUtils.isNetworkAvailable(this) && familyInfo != null) {
            getMvpPresenter().getMemberList(currentPage, familyInfo);
        } else {
            showNoData("暂无任何申请记录");
        }
    }

    private FamilyInfo cacheInfo() {
        if (isMyFamily) {
            return CoreManager.getCore(IFamilyCore.class).getFamilyInfo();
        }
        return CoreManager.getCore(IFamilyCore.class).getCacheInfo();
    }
}
