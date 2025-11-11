package com.vslk.lbgx.room.avroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.avroom.adapter.RoomInviteAdapter;
import com.hncxco.library_ui.widget.AppToolBar;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.room.bean.OnlineChatMember;
import com.tongdaxing.xchat_core.room.presenter.RoomInvitePresenter;
import com.tongdaxing.xchat_core.room.view.IRoomInviteView;

import java.util.List;

/**
 * <p> 抱人上麦 </p>
 *
 * @author jiahui
 * @date 2017/12/21
 */
@CreatePresenter(RoomInvitePresenter.class)
public class RoomInviteActivity extends BaseMvpActivity<IRoomInviteView, RoomInvitePresenter>
        implements IRoomInviteView, RoomInviteAdapter.OnItemClickListener, RoomInviteAdapter.OnRoomOnlineNumberChangeListener {
    private RoomInviteAdapter mRoomInviteAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private int mPage = Constants.PAGE_START;
    private int micPosition;

    public static void openActivity(FragmentActivity fragmentActivity, int micPosition) {
        Intent intent = new Intent(fragmentActivity, RoomInviteActivity.class);
        intent.putExtra(Constants.KEY_POSITION, micPosition);
        fragmentActivity.startActivityForResult(intent, 200);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_invite);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        ((AppToolBar) findViewById(R.id.toolbar)).setOnBackBtnListener(view -> finish());
        Intent intent = getIntent();
        if (intent != null) {
            micPosition = intent.getIntExtra(Constants.KEY_POSITION, Integer.MIN_VALUE);
        }
        mRoomInviteAdapter = new RoomInviteAdapter(this, this);
        mRecyclerView.setAdapter(mRoomInviteAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRoomInviteAdapter.setOnRoomOnlineNumberChangeListener(this);
        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                if (!NetworkUtil.isNetAvailable(RoomInviteActivity.this)) {
                    mRefreshLayout.finishLoadmore();
                    return;
                }
                List<IMChatRoomMember> data = mRoomInviteAdapter.getChatRoomMemberList();
                if (ListUtils.isListEmpty(data)) {
                    mRefreshLayout.finishLoadmore();
                    return;
                }
                loadData(data.size(), mPage + 1);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (!NetworkUtil.isNetAvailable(RoomInviteActivity.this)) {
                    mRefreshLayout.finishRefresh();
                    return;
                }
                firstLoad();
            }
        });
        showLoading();
        firstLoad();
    }

    public void firstLoad() {
        loadData(0, Constants.PAGE_START);
    }

    private void loadData(int index, int page) {
        getMvpPresenter().requestChatMemberByPage(index, page);
    }

    @Override
    public void onRequestChatMemberByPageSuccess(List<OnlineChatMember> memberList, int page) {

    }

    @Override
    public void onRequestChatMemberByPageFail(String errorStr, int page) {
        if (mPage == Constants.PAGE_START) {
            mRefreshLayout.finishRefresh(0);
            showNoData(getString(R.string.data_error));
        } else {
            mRefreshLayout.finishLoadmore(0);
        }
    }

    @Override
    public void onRequestMemberByPageSuccess(List<IMChatRoomMember> memberList, int page) {
        mPage = page;
        if (mPage == Constants.PAGE_START) {
            List<IMChatRoomMember> chatRoomMemberList = mRoomInviteAdapter.getChatRoomMemberList();
            if (!ListUtils.isListEmpty(chatRoomMemberList)) {
                chatRoomMemberList.clear();
            }
            mRefreshLayout.finishRefresh(0);
            if (ListUtils.isListEmpty(memberList)) {
                showNoData("暂无可抱用户");
            } else {
                hideStatus();
                mRoomInviteAdapter.setNewData(memberList);
            }
        } else {
            mRefreshLayout.finishLoadmore(0);
            if (!ListUtils.isListEmpty(memberList)) {
                mRoomInviteAdapter.addNewData(memberList);
            }
        }
    }

    @Override
    public void onClick(IMChatRoomMember chatRoomMember) {
        if (chatRoomMember == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("account", chatRoomMember.getAccount());
        intent.putExtra("nickName", chatRoomMember.getNick());
        intent.putExtra(Constants.KEY_POSITION, micPosition);
        setResult(100, intent);
        finish();
    }

    @Override
    public void onReloadDate() {
        super.onReloadDate();
        showLoading();
        firstLoad();
    }

    @Override
    public void onRoomOnlineNumberChange(int number) {
        if (number == 0) {
            showNoData();
        }
    }

    @Override
    protected void onDestroy() {
        if (mRoomInviteAdapter != null) {
            mRoomInviteAdapter.onRelease();
        }
        super.onDestroy();
    }

}
