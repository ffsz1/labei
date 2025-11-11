package com.vslk.lbgx.room.avroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
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
import com.tongdaxing.xchat_core.room.model.AuctionModel;
import com.tongdaxing.xchat_core.room.presenter.LightChatOnlinePresenter;
import com.tongdaxing.xchat_core.room.view.ILightChatOnlineView;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.avroom.adapter.OnlineUserAdapter;
import com.vslk.lbgx.room.avroom.other.ButtonItemFactory;
import com.vslk.lbgx.room.widget.dialog.AuctionDialog;
import com.vslk.lbgx.room.widget.dialog.NewUserInfoDialog;
import com.vslk.lbgx.utils.UIHelper;

import java.util.List;

/**
 * <p> 经聊房在线听众 </p>
 *
 * @author jiahui
 * @date 2017/12/25
 */
@CreatePresenter(LightChatOnlinePresenter.class)
public class LightChatOnlineActivity extends BaseMvpActivity<ILightChatOnlineView, LightChatOnlinePresenter>
        implements ILightChatOnlineView, BaseQuickAdapter.OnItemClickListener, OnlineUserAdapter.OnRoomOnlineNumberChangeListener {

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private OnlineUserAdapter mOnlineUserAdapter;


    private int mPage = Constants.PAGE_START;

    public static void openActivity(FragmentActivity fragmentActivity, int micPosition) {
        Intent intent = new Intent(fragmentActivity, LightChatOnlineActivity.class);
        intent.putExtra(Constants.KEY_POSITION, micPosition);
        fragmentActivity.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        if (mOnlineUserAdapter != null)
            mOnlineUserAdapter.release();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_invite);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        mOnlineUserAdapter = new OnlineUserAdapter();
        mRecyclerView.setAdapter(mOnlineUserAdapter);
        setListener();
        showLoading();
        firstLoad();
    }

    private void setListener() {
        mOnlineUserAdapter.setListener(this);
        mOnlineUserAdapter.setOnItemClickListener(this);
        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                if (!NetworkUtil.isNetAvailable(LightChatOnlineActivity.this)) {
                    mRefreshLayout.finishLoadmore();
                    return;
                }
                List<OnlineChatMember> data = mOnlineUserAdapter.getData();
                if (ListUtils.isListEmpty(data)) {
                    mRefreshLayout.finishLoadmore();
                    return;
                }
                loadData(data.get(data.size() - 1).chatRoomMember.getEnter_time());
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (!NetworkUtil.isNetAvailable(LightChatOnlineActivity.this)) {
                    mRefreshLayout.finishRefresh();
                    return;
                }
                firstLoad();
            }
        });
    }

    public void firstLoad() {
        mPage = Constants.PAGE_START;
        loadData(0);
    }

    private void loadData(long time) {
        getMvpPresenter().requestChatMemberByPage(mPage, time,
                mOnlineUserAdapter == null ? null : mOnlineUserAdapter.getData());
    }

    @Override
    public void onReloadDate() {
        super.onReloadDate();
        showLoading();
        firstLoad();
    }

    @Override
    public void onRequestChatMemberByPageSuccess(List<OnlineChatMember> memberList, int page) {
        mPage = page;
        if (mPage == Constants.PAGE_START) {
            mRefreshLayout.finishRefresh(0);
            if (ListUtils.isListEmpty(memberList)) {
                showNoData();
            } else {
                hideStatus();
                mOnlineUserAdapter.setNewData(memberList);
            }
        } else {
            mRefreshLayout.finishLoadmore(0);
            if (!ListUtils.isListEmpty(memberList)) {
                mOnlineUserAdapter.addData(memberList);
            }
        }
    }


    @Override
    public void onRequestChatMemberByPageFail(String errorStr, int page) {
        mPage = page;
        if (mPage == Constants.PAGE_START) {
            mRefreshLayout.finishRefresh(0);
            showNetworkErr();
        } else {
            mRefreshLayout.finishLoadmore(0);
            hideStatus();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        if (!isValid()) return;
        List<OnlineChatMember> data = mOnlineUserAdapter.getData();
        if (ListUtils.isListEmpty(data)) return;
        getMvpPresenter().onItemClick(data.get(i));
    }

    @Override
    public SparseArray<ButtonItem> getButtonItemList(final IMChatRoomMember chatRoomMember, final int position) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) return null;
        String roomId = String.valueOf(roomInfo.getRoomId());
        String account = chatRoomMember.getAccount();
        String nick = chatRoomMember.getNick();
        SparseArray<ButtonItem> buttonItemMap = new SparseArray<>();
        // 踢出房间
        ButtonItem buttonItem4 = ButtonItemFactory.createKickOutRoomItem(roomId, this, account, nick);
        // 查看资料
        ButtonItem buttonItem5 = ButtonItemFactory.createCheckUserInfoDialogItem(this, account);
        //下麦
        ButtonItem buttonItem6 = ButtonItemFactory.createDownMicItem();
        //取消静麦
        ButtonItem buttonItem7 = ButtonItemFactory.createFreeMicItem(position, () -> {
//                        getMvpPresenter().openMicroPhone(position);
        });
        // 设置管理员
        ButtonItem buttonItem8 = ButtonItemFactory.createMarkManagerListItem(chatRoomMember.getNick(), roomId, account, true);
        //取消管理员
        ButtonItem buttonItem9 = ButtonItemFactory.createMarkManagerListItem(chatRoomMember.getNick(),roomId, account, false);
        // 加入黑名单
        ButtonItem buttonItem10 = ButtonItemFactory.createMarkBlackListItem(roomId, this, account, nick);
        // 抱他上麦
        ButtonItem buttonItem1 = ButtonItemFactory.createInviteOnMicItem(chatRoomMember.getNick(), chatRoomMember.getAccount());
        ButtonItem buttonItem13 = new ButtonItem("发起竞拍", () -> {
            if (AuctionModel.get().isInAuctionNow()) {
                SingleToastUtil.showToast(LightChatOnlineActivity.this, "正在竞拍,请先结束竞拍!");
                return;
            }
            AuctionDialog dialog = new AuctionDialog(LightChatOnlineActivity.this, JavaUtil.str2long(chatRoomMember.getAccount()), false);
            dialog.setOnClickItemListener(new AuctionDialog.OnClickItemListener() {
                @Override
                public void onClickHead() {
                    UIHelper.showUserInfoAct(LightChatOnlineActivity.this, JavaUtil.str2long(chatRoomMember.getAccount()));
                }

                @Override
                public void onClickBegin(int price) {
                    RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                    if (currentRoomInfo == null) return;
                    AuctionModel.get().startAuction(currentRoomInfo.getUid(), JavaUtil.str2long(chatRoomMember.getAccount()),
                            price, 30, 10, "暂无竞拍描述").subscribe();
                }
            });
            dialog.show();
        });
        buttonItemMap.put(ButtonItem.SEND_KICKOUT_ROOM_ITEM, buttonItem4);
        buttonItemMap.put(ButtonItem.SEND_SHOW_USER_INCO_ITEM, buttonItem5);
        buttonItemMap.put(ButtonItem.SEND_DOWN_MIC_ITEM, buttonItem6);
        buttonItemMap.put(ButtonItem.SEND_OPEN_MUTE_ITEM, buttonItem7);
        buttonItemMap.put(ButtonItem.SEND_MARK_MANAGER_ITEM, buttonItem8);
        buttonItemMap.put(ButtonItem.SEND_NOMARK_MANAGER_ITEM, buttonItem9);
        buttonItemMap.put(ButtonItem.SEND_MARK_BLACK_ITEM, buttonItem10);
        buttonItemMap.put(ButtonItem.SEND_INVITE_MIC_ITEM, buttonItem1);
        buttonItemMap.put(ButtonItem.START_AUCTION, buttonItem13);
        return buttonItemMap;
    }

    @Override
    public void showItemClickDialog(List<ButtonItem> buttonItemList) {
        if (ListUtils.isListEmpty(buttonItemList)) return;
        getDialogManager().showCommonPopupDialog(buttonItemList, getString(R.string.cancel), false);
    }

    @Override
    public void showUserInfoDialog(String account) {
        if (TextUtils.isEmpty(account)) return;
        NewUserInfoDialog.showUserDialog(this, Long.parseLong(account));
    }

    @Override
    public void onMemberIn(String account, List<OnlineChatMember> dataList, ChatRoomMessage chatRoomMessage) {
        getMvpPresenter().onMemberInRefreshData(account, dataList, mPage);
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
        finish();
    }
}
