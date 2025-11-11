package com.vslk.lbgx.room.avroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.room.avroom.adapter.PkHistoryAdapter;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.pk.IPKCoreClient;
import com.tongdaxing.xchat_core.pk.IPkCore;
import com.tongdaxing.xchat_core.pk.bean.PkVoteInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;

/**
 * PK历史记录页面
 * @author zwk
 */
public class PkHistoryActivity extends BaseActivity implements OnRefreshLoadmoreListener {
    protected RecyclerView mRecyclerView;
    protected SmartRefreshLayout mRefreshLayout;
    protected PkHistoryAdapter mAdapter;
    protected List<PkVoteInfo> datas;
    private int mPage = Constants.PAGE_START;


    public static void start(Context context) {
        Intent intent = new Intent(context, PkHistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk_history);
        initTitleBar("记录");
        initView();
        initListener();
        showLoading();
        initData();
    }

    private void initView() {
        mRefreshLayout = findView(R.id.refresh_layout);
        mRecyclerView = findView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        datas = new ArrayList<>();
        mAdapter = new PkHistoryAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initListener() {
        mRefreshLayout.setOnRefreshLoadmoreListener(this);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter != null && !ListUtils.isListEmpty(mAdapter.getData())) {
                    PkVoteInfo info = mAdapter.getData().get(position);
                    if (info == null) {
                        toast("参数异常！");
                        return;
                    }
                    if (!AvRoomDataManager.get().isOnMic(info.getUid()) || !AvRoomDataManager.get().isOnMic(info.getPkUid())) {
                        toast("PK开启失败，请确认PK用户在麦位上！");
                        return;
                    }
                    getDialogManager().showOkCancelDialog("确认要再次进行PK吗？", true, new DialogManager.OkCancelDialogListener() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onOk() {
                            info.setOpUid(CoreManager.getCore(IAuthCore.class).getCurrentUid());
                            CoreManager.getCore(IPkCore.class).savePK(AvRoomDataManager.get().mCurrentRoomInfo == null ? 0 : AvRoomDataManager.get().mCurrentRoomInfo.getRoomId(), info);
                        }
                    });
                }
            }
        });
    }

    @CoreEvent(coreClientClass = IPKCoreClient.class)
    public void onSavePk(PkVoteInfo pkVoteInfo) {
        finish();
    }

    @CoreEvent(coreClientClass = IPKCoreClient.class)
    public void onSavePkFail(String error) {
        toast(error);
    }

    private void initData() {
        CoreManager.getCore(IPkCore.class).getPkHistoryList(AvRoomDataManager.get().mCurrentRoomInfo == null ? 0 : AvRoomDataManager.get().mCurrentRoomInfo.getRoomId(), mPage);
    }

    @CoreEvent(coreClientClass = IPKCoreClient.class)
    public void onPkHistoryList(List<PkVoteInfo> pkVoteInfos) {
        hideStatus();
        if (mPage == Constants.PAGE_START) {
            mRefreshLayout.finishRefresh();
            if (ListUtils.isListEmpty(pkVoteInfos)) {
                showNoData();
            } else {
                hideStatus();
                mAdapter.setNewData(pkVoteInfos);
            }
        } else {
            mRefreshLayout.finishLoadmore();
            if (!ListUtils.isListEmpty(pkVoteInfos)) {
                mAdapter.addData(pkVoteInfos);
            }
        }
    }

    @CoreEvent(coreClientClass = IPKCoreClient.class)
    public void onPkHistoryListFail(String error) {
        hideStatus();
        if (mPage == Constants.PAGE_START) {
            mRefreshLayout.finishRefresh(0);
            showNetworkErr();
        } else {
            mRefreshLayout.finishLoadmore(0);
            hideStatus();
        }
    }

    @Override
    public void onReloadDate() {
        mPage = Constants.PAGE_START;
        initData();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage++;
        initData();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = Constants.PAGE_START;
        initData();
    }
}
