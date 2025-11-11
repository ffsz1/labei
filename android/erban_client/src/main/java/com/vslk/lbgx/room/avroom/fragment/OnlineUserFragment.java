package com.vslk.lbgx.room.avroom.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.room.avroom.adapter.OnlineUserAdapter;
import com.vslk.lbgx.room.avroom.other.ButtonItemFactory;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.OnlineChatMember;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.presenter.HomePartyUserListPresenter;
import com.tongdaxing.xchat_core.room.view.IHomePartyUserListView;
import com.vslk.lbgx.ui.widget.itemdecotion.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>  在线用户列表 </p>
 *
 * @author Administrator
 * @date 2017/12/4
 */
@CreatePresenter(HomePartyUserListPresenter.class)
public class OnlineUserFragment extends BaseMvpFragment<IHomePartyUserListView, HomePartyUserListPresenter> implements
        BaseQuickAdapter.OnItemClickListener, IHomePartyUserListView, OnlineUserAdapter.OnRoomOnlineNumberChangeListener,
        BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;

    private OnlineUserAdapter mOnlineUserAdapter;
    private int mPage = Constants.PAGE_START;
    private OnLineUserCallback onLineUserCallback;
    private boolean isVideoRoom = false;


    @Override
    public void onDestroy() {
        if (mOnlineUserAdapter != null) {
            mOnlineUserAdapter.release();
        }
        super.onDestroy();
    }

    @Override
    public void onFindViews() {
        mRecyclerView = mView.findViewById(R.id.recycler_view);
        mRefreshLayout = mView.findViewById(R.id.refresh_layout);
    }

    @Override
    public void onSetListener() {
        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                if (!NetworkUtil.isNetAvailable(mContext)) {
                    mRefreshLayout.finishLoadmore();
                    return;
                }
                List<OnlineChatMember> data = mOnlineUserAdapter.getData();
                if (ListUtils.isListEmpty(data)) {
                    mRefreshLayout.finishLoadmore();
                    return;
                }
                loadData(data.size(), mPage + 1);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (!NetworkUtil.isNetAvailable(mContext)) {
                    mRefreshLayout.finishRefresh();
                    return;
                }
                firstLoad();
            }
        });
    }

    @Override
    public void initiate() {
        if (getArguments() != null) {
            isVideoRoom = getArguments().getBoolean("isVideoRoom", false);
        }
        mRefreshLayout.setEnableOverScrollDrag(false);
        mRefreshLayout.setEnableOverScrollBounce(false);
        mRefreshLayout.setEnableHeaderTranslationContent(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), OrientationHelper.VERTICAL,
                3, R.color.color_f1f1f1));
        mOnlineUserAdapter = new OnlineUserAdapter();
        mRecyclerView.setAdapter(mOnlineUserAdapter);
        mOnlineUserAdapter.setOnItemClickListener(this);
        mOnlineUserAdapter.setOnItemChildClickListener(this);
        mOnlineUserAdapter.setListener(this);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.common_refresh_recycler_view;
    }

    public void firstLoad() {
        loadData(0, Constants.PAGE_START);
    }

    private void loadData(int index, int page) {
        getMvpPresenter().requestChatMemberByIndex(page, index, mOnlineUserAdapter == null ? null : mOnlineUserAdapter.getData());
    }

    @Override
    public synchronized void onRequestChatMemberByPageSuccess(List<OnlineChatMember> chatRoomMemberList, int page) {
        mPage = page;
        if (!ListUtils.isListEmpty(chatRoomMemberList)) {
            mOnlineUserAdapter.setNewData(chatRoomMemberList);
            if (mPage == Constants.PAGE_START) {
                mRefreshLayout.finishRefresh();
            } else {
                mRefreshLayout.finishLoadmore();
            }
        } else {
            if (mPage == Constants.PAGE_START) {
                mRefreshLayout.finishRefresh();
            } else {
                mRefreshLayout.finishLoadmore();
            }
        }
    }

    @Override
    public void onRequestChatMemberByPageFail(String errorStr, int page) {
        Logger.i("获取到数据失败,page=" + page);
        if (page == Constants.PAGE_START) {
            mRefreshLayout.finishRefresh();
        } else {
            mRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
        if (onLineUserCallback != null)
            onLineUserCallback.onDismiss();
        RoomInfo currentRoom = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoom != null) {
            List<OnlineChatMember> chatRoomMembers = mOnlineUserAdapter.getData();
            if (ListUtils.isListEmpty(chatRoomMembers)) {
                return;
            }
            if (chatRoomMembers.size() <= position) {
                return;
            }
            IMChatRoomMember chatRoomMember = chatRoomMembers.get(position).chatRoomMember;
            if (onlineItemClick != null) {
                onlineItemClick.onItemClick(chatRoomMember);
                return;
            }
            if (TextUtils.isEmpty(chatRoomMember.getAccount())) {
                return;
            }
            List<ButtonItem> items = ButtonItemFactory.createAllRoomPublicScreenButtonItems(mContext, chatRoomMember.getAccount(), chatRoomMember.getNick(), chatRoomMember.getAvatar());
            if (items == null) {
                return;
            }
            final List<ButtonItem> buttonItems = new ArrayList<>(items);
            ((BaseMvpActivity) mContext).getDialogManager().showCommonPopupDialog(buttonItems, "取消");
        }
    }

    @Override
    public void onMemberIn(String account, List<OnlineChatMember> dataList, ChatRoomMessage chatRoomMessage) {
        getMvpPresenter().onMemberInRefreshData(account, dataList, mPage, chatRoomMessage);
    }

    @Override
    public void onMemberDownUpMic(String account, boolean isUpMic, List<OnlineChatMember> dataList) {
        getMvpPresenter().onMemberDownUpMic(account, isUpMic, dataList, mPage);
    }

    @Override
    public void onUpdateMemberManager(String account, boolean isRemoveManager, List<OnlineChatMember> dataList) {
        getMvpPresenter().onUpdateMemberManager(account, dataList, isRemoveManager, mPage);
    }

    @Override
    public void addMemberBlack() {
    }

    public void setOnLineUserCallback(OnLineUserCallback onLineUserCallback) {
        this.onLineUserCallback = onLineUserCallback;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (onLineUserCallback != null) {
            onLineUserCallback.onDismiss();
        }
        RoomInfo currentRoom = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoom != null) {
            List<OnlineChatMember> chatRoomMembers = mOnlineUserAdapter.getData();
            if (ListUtils.isListEmpty(chatRoomMembers)) {
                return;
            }
            if (chatRoomMembers.size() <= position) {
                return;
            }
            OnlineChatMember chatRoomMember = chatRoomMembers.get(position);
            if (onlineItemClick != null) {
                onlineItemClick.onHoldMicroClick(chatRoomMember);
            }
        }
    }

    public interface OnLineUserCallback {
        void onDismiss();
    }

    private OnlineItemClick onlineItemClick;

    public void setOnlineItemClick(OnlineItemClick onlineItemClick) {
        this.onlineItemClick = onlineItemClick;
    }


    public interface OnlineItemClick {

        default void onItemClick(IMChatRoomMember chatRoomMember) {

        }

        default void onHoldMicroClick(OnlineChatMember chatRoomMember) {

        }
    }
}
