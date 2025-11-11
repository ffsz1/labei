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
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.find.adapter.ApplyMsgAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.utils.NetworkUtils;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.bean.ApplyMsgInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

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
public class ApplyMsgListActivity extends BaseMvpActivity<IFindFamilyView, FindFamilyPresenter> implements IFindFamilyView,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener {

    private FamilyInfo familyInfo;
    private int currentPage = PAGE_START_ZERO;

    @BindView(R.id.toolbar) AppToolBar mToolBar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;

    private ApplyMsgAdapter mAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, ApplyMsgListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_msg);
        ButterKnife.bind(this);
        initiate();
        initData();
        onSetListener();
    }

    private void initiate() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new ApplyMsgAdapter();
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        familyInfo = cacheInfo();
        if (familyInfo != null) {
            showLoading();
            getMvpPresenter().getFamilyMessage(currentPage, familyInfo);
        }
    }

    private void onSetListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mToolBar.setOnBackBtnListener(view -> finish());
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    @Override
    public void onRefresh() {
        this.currentPage = PAGE_START_ZERO;
        if (NetworkUtils.isNetworkAvailable(this) && familyInfo != null) {
            getMvpPresenter().getFamilyMessage(currentPage, familyInfo);
        } else {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (NetworkUtils.isNetworkAvailable(this) && familyInfo != null) {
            currentPage++;
            getMvpPresenter().getFamilyMessage(currentPage, familyInfo);
        } else {
            mAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void getApplyMsgList(List<ApplyMsgInfo> list) {
        if (currentPage == PAGE_START_ZERO) {
            hideStatus();
            mRefreshLayout.setRefreshing(false);
            if (!ListUtils.isListEmpty(list)) {
                mAdapter.setNewData(list);
            } else {
                showNoData("暂无申请信息");
            }
        } else {
            if (!ListUtils.isListEmpty(list)) {
                mAdapter.loadMoreComplete();
                mAdapter.addData(list);
            } else {
                mAdapter.loadMoreEnd(true);
            }
        }
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
    }

    @Override
    public void getApplyMsgListFail(String msg) {
        if (currentPage == PAGE_START_ZERO) {
            hideStatus();
            mRefreshLayout.setRefreshing(false);
            showNoData("暂无申请信息");
        } else {
            mAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.confirm:
                operatorApply(position, true);
                break;
            case R.id.ignore:
                operatorApply(position, false);
                break;
            default:
                break;
        }
    }

    /**
     * 忽略申请
     */
    private void operatorApply(int position, boolean isConfirm) {
        FamilyInfo familyInfo = cacheInfo();
        if (familyInfo == null) {
            return;
        }
        ApplyMsgInfo msgInfo = mAdapter.getData().get(position);
        String msg = "确定要" + (isConfirm ? "同意 " : "忽略 ") + msgInfo.getNick() + " 的" + (msgInfo.getType() == 1 ? "加入申请吗？" : "退出申请吗") ;
        getDialogManager().showYuMengOkCancelDialog(msg, true, new DialogManager.OkCancelDialogListener() {

            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                getDialogManager().showProgressDialog(ApplyMsgListActivity.this);
                getMvpPresenter().applyFamily(familyInfo, msgInfo.getUid(), isConfirm ? 1 : 2, msgInfo.getType(), position);
            }
        });
    }

    @Override
    public void applyFamily(String message, int status, int position) {
        getDialogManager().dismissDialog();
        toast("申请消息处理完成");

        //改变消息状态显示
        ApplyMsgInfo msgInfo = mAdapter.getData().get(position);
        msgInfo.setStatus(status);
        mAdapter.setData(position, msgInfo);
    }

    @Override
    public void applyFamilyFail(String message) {
        getDialogManager().dismissDialog();
        toast(message);
    }

    @Override
    public void onReloadDate() {
        this.currentPage = PAGE_START_ZERO;
        if (NetworkUtils.isNetworkAvailable(this) && familyInfo != null) {
            getMvpPresenter().getFamilyMessage(currentPage, familyInfo);
        } else {
            showNoData("暂无申请信息");
        }
    }

    private FamilyInfo cacheInfo() {
        return CoreManager.getCore(IFamilyCore.class).getFamilyInfo();
    }

}
