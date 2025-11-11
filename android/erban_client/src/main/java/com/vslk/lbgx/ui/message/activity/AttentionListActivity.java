package com.vslk.lbgx.ui.message.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.event.ToHim;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.ui.message.adapter.AttentionListAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.praise.IPraiseClient;
import com.tongdaxing.xchat_core.user.AttentionCore;
import com.tongdaxing.xchat_core.user.AttentionCoreClient;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.tongdaxing.erban.R.id.swipe_refresh;

/**
 * 关注列表
 *
 * @author dell
 */
public class AttentionListActivity extends BaseActivity {

    private AppToolBar mToolBar;
    private RecyclerView mRecylcerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AttentionListActivity mActivity;
    private AttentionListAdapter adapter;
    private List<AttentionInfo> mAttentionInfoList = new ArrayList<>();

    private int mPage = Constants.PAGE_START;

    public static void start(Context context) {
        Intent intent = new Intent(context, AttentionListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_attention);
        initTitleBar(getString(R.string.my_attention));
        initView();
        setListener();
        initData();
    }

    private void setListener() {
        mToolBar.setOnBackBtnListener(view -> finish());
        swipeRefreshLayout.setOnRefreshListener(onRefreshLisetener);
        adapter = new AttentionListAdapter(mAttentionInfoList);

        adapter.setRylListener(new AttentionListAdapter.onClickListener() {
            @Override
            public void rylListeners(AttentionInfo attentionInfo) {
                if (Constants.OFFICIAL == attentionInfo.getUid()) {
                    return;
                }
                UserInfoActivity.start(mActivity, attentionInfo.getUid());
            }

            @Override
            public void findHimListeners(AttentionInfo attentionInfo) {
                if (Constants.OFFICIAL == attentionInfo.getUid()) {
                    return;
                }
                if (attentionInfo.getUserInRoom() != null) {
                    ToHim.postToHim(attentionInfo.getUserInRoom().getUid(), this.getClass().getName());
                } else {
                    ToHim.postToHim(attentionInfo.getUid(), this.getClass().getName());
                }

            }
        });
        adapter.setOnLoadMoreListener(() -> {
            mPage++;
            onRefreshing();
        }, mRecylcerView);
    }

    private void initData() {
        mRecylcerView.setAdapter(adapter);
        showLoading();
        onRefreshing();
    }

    private void initView() {
        mActivity = this;
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
        mRecylcerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(swipe_refresh);
        mRecylcerView.setLayoutManager(new LinearLayoutManager(mActivity));

    }

    SwipeRefreshLayout.OnRefreshListener onRefreshLisetener = () -> {
        mPage = Constants.PAGE_START;
        onRefreshing();
    };

    private void onRefreshing() {
        CoreManager.getCore(AttentionCore.class)
                .getAttentionList(CoreManager.getCore(IAuthCore.class).getCurrentUid(), mPage, Constants.PAGE_SIZE);
    }

    @CoreEvent(coreClientClass = AttentionCoreClient.class)
    public void onGetAttentionList(List<AttentionInfo> attentionInfoList, int page) {
        mPage = page;
        if (!ListUtils.isListEmpty(attentionInfoList)) {
            if (mPage == Constants.PAGE_START) {
                hideStatus();
                swipeRefreshLayout.setRefreshing(false);
                mAttentionInfoList.clear();
                adapter.setNewData(attentionInfoList);
                if (attentionInfoList.size() < Constants.PAGE_SIZE) {
                    adapter.setEnableLoadMore(false);
                }
            } else {
                adapter.loadMoreComplete();
                adapter.addData(attentionInfoList);
            }
        } else {
            if (mPage == Constants.PAGE_START) {
                showNoData(getString(R.string.no_attention_text));
            } else {
                adapter.loadMoreEnd(true);
            }

        }
    }

    @CoreEvent(coreClientClass = AttentionCoreClient.class)
    public void onGetAttentionListFail(String error, int page) {
        mPage = page;
        if (mPage == Constants.PAGE_START) {
            swipeRefreshLayout.setRefreshing(false);
            showNetworkErr();
        } else {
            adapter.loadMoreFail();
            toast(error);
        }
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraise(long uid, boolean showNotice) {
        List<AttentionInfo> data = adapter.getData();
        if (!ListUtils.isListEmpty(data)) {
            ListIterator<AttentionInfo> iterator = data.listIterator();
            for (; iterator.hasNext(); ) {
                AttentionInfo attentionInfo = iterator.next();
                if (attentionInfo.isValid() && attentionInfo.getUid() == uid) {
                    iterator.remove();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View.OnClickListener getLoadListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPage = Constants.PAGE_START;
                showLoading();
                onRefreshing();
            }
        };
    }
}


