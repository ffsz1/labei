package com.vslk.lbgx.ui.message.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.message.adapter.FriendListAdapter;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.BR;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.im.friend.IIMFriendCore;
import com.tongdaxing.xchat_core.im.friend.IIMFriendCoreClient;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 创建者      Created by dell
 * 创建时间    2018/11/22
 * 描述        好友列表
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FriendListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.toolbar) AppToolBar mToolBar;

    private FriendListAdapter mAdapter;
    private FriendHandler handler = new FriendHandler(this);

    public static void start(Context context) {
        Intent intent = new Intent(context, FriendListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        ButterKnife.bind(this);
        initiate();
    }

    private void initiate() {
        mAdapter = new FriendListAdapter(R.layout.list_item_friend, BR.userInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mToolBar.setOnBackBtnListener(view -> finish());
        handler.sendMessageDelayed(handler.obtainMessage(), 500);
    }

    @CoreEvent(coreClientClass = IIMFriendCoreClient.class)
    public void onFriendListUpdate(List<NimUserInfo> userInfos) {
        setData(userInfos);
    }

    private void setData(List<NimUserInfo> userInfos) {
        hideStatus();
        if (!ListUtils.isListEmpty(userInfos)) {
            mAdapter.setNewData(userInfos);
        } else {
            showNoData(getString(R.string.no_frenids_text));
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo userInfo) {
        showLoading();
        handler.sendMessageDelayed(handler.obtainMessage(), 500);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter == null || ListUtils.isListEmpty(mAdapter.getData())) {
            toast("数据异常请稍后重试");
            return;
        }
        NimUIKit.startP2PSession(this, mAdapter.getData().get(position).getAccount());
    }

    static class FriendHandler extends Handler {

        private WeakReference<FriendListActivity> mReference;

        FriendHandler(FriendListActivity activity) {
            this.mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mReference == null || mReference.get() == null) {
                return;
            }
            List<NimUserInfo> userInfos = CoreManager.getCore(IIMFriendCore.class).getMyFriends();
            mReference.get().setData(userInfos);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
