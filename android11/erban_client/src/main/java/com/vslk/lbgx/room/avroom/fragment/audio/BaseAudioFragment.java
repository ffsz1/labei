package com.vslk.lbgx.room.avroom.fragment.audio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomMemberComeInfo;
import com.tongdaxing.xchat_core.bean.RoomMicInfo;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.gift.IGiftCoreClient;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCoreClient;
import com.tongdaxing.xchat_core.im.custom.bean.BurstGiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.GiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.MultiGiftAttachment;
import com.tongdaxing.xchat_core.im.message.IIMMessageCoreClient;
import com.tongdaxing.xchat_core.im.room.IIMRoomCoreClient;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.manager.RtcEngineManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.presenter.HomePartyPresenter;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.tongdaxing.xchat_core.room.view.IHomePartyView;
import com.tongdaxing.xchat_core.share.IShareCore;
import com.tongdaxing.xchat_core.share.IShareCoreClient;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.VersionsCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.SpUtils;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.avroom.activity.RoomInviteActivity;
import com.vslk.lbgx.room.avroom.activity.RoomTopicActivity;
import com.vslk.lbgx.room.avroom.adapter.MicroViewAdapter;
import com.vslk.lbgx.room.avroom.fragment.base.BaseRoomFragment;
import com.vslk.lbgx.room.avroom.other.ButtonItemFactory;
import com.vslk.lbgx.room.avroom.other.SoftKeyBoardListener;
import com.vslk.lbgx.room.avroom.widget.dialog.RoomTopicDIalog;
import com.vslk.lbgx.room.gift.GiftDialog;
import com.vslk.lbgx.room.widget.dialog.BigListDataDialog;
import com.vslk.lbgx.room.widget.dialog.GiftRecordDialog;
import com.vslk.lbgx.room.widget.dialog.ListDataDialog;
import com.vslk.lbgx.room.widget.dialog.MicInListDialog;
import com.vslk.lbgx.room.widget.dialog.NewUserInfoDialog;
import com.vslk.lbgx.room.widget.dialog.RewardGiftDialog;
import com.vslk.lbgx.room.widget.dialog.SeaRecordDialog;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.ui.verified.VerifiedDialog;
import com.vslk.lbgx.ui.widget.dialog.ShareDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sharesdk.framework.Platform;

import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_CLOSE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_OPEN;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_TYPE_BURST_GIFT;
import static com.tongdaxing.xchat_core.manager.IMNetEaseManager.SHOW_PASSIVITY_UP_MIC_HINT;

/**
 * 轰趴房间
 *
 * @author chenran
 * @date 2017/7/26
 */
@CreatePresenter(HomePartyPresenter.class)
public abstract class BaseAudioFragment extends BaseRoomFragment<IHomePartyView, HomePartyPresenter> implements View.OnClickListener,
        GiftDialog.OnGiftDialogBtnClickListener, ShareDialog.OnShareDialogItemClick, IHomePartyView, MicroViewAdapter.OnMicroItemClickListener {

    protected long myUid;
    protected UserInfo roomOwnner;
    protected IMCustomAttachment giftAttachment;
    protected boolean micInListOption = true;
    protected List<ChatRoomMessage> giftMsgList;
    protected View popupContentView;
    protected PopupWindow popupWindow;
    protected GiftRecordDialog recordDialog;
    protected SeaRecordDialog seaRecordDialog;
    Unbinder unbinder;

    //初始化事件
    protected abstract void initClick();

    //获取状态
    protected abstract void getState();

    //上下麦修改状态
    protected abstract void updateMatchView(boolean isDowm);

    protected abstract void onMicInListChange();

    protected abstract void initMicroAdapter();

    //捡海螺状态更改
    protected abstract void initLotteryBoxChange();

    //进房提示更改
    protected abstract void updateRoomTip(String playInfo);

    protected abstract void bottomView(boolean inputMsgBtn);

    public abstract void onMusicPlayViewUpdate();

    protected abstract void showBottomViewForDifRole();

    public abstract void onUpMicro(int micPosition);

    protected abstract void setLevel(int level);

    protected abstract void setLevelDraw(int draw);

    protected abstract void setComeInfo(RoomMemberComeInfo comeInfo);

    protected abstract void upComeMsg(int vs);

    protected abstract void startComeMsgAnim(TranslateAnimation animation);

    protected abstract void hideInputLayout();

    /**
     * 更新麦位view / 音乐播放器 / 底部：表情、麦克风隐藏显示
     */
    protected abstract void updateView();

    protected abstract void updateMicBtn();

    protected abstract void updateRemoteMuteBtn();

    protected abstract void changeState();

    /**
     * Click     * 显示实名认证提示框
     *
     * @param code
     */
    @Override
    public void showVerifiedDialog(int code, String message) {
        VerifiedDialog verifiedDialog = VerifiedDialog.newInstance(message, code);
        verifiedDialog.show(getChildFragmentManager(), "verifiedDialog");
    }

    @Override
    public void onFindViews() {
        unbinder = ButterKnife.bind(this, mView);
    }

    @Override
    public void initiate() {
        Json configData = CoreManager.getCore(VersionsCore.class).getConfigData();
        micInListOption = configData.num("micInListOption") == 1;
        initLotteryBoxChange();
        giftMsgList = msgFilter(IMNetEaseManager.get().messages);
        if (ListUtils.isListEmpty(giftMsgList)) {
            giftMsgList = new ArrayList<>();
        }
        AvRoomDataManager.get().setMinimize(false);
        observMsg();
        myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        initClick();
        updateView();
        updateMicBtn();
        updateRemoteMuteBtn();
        getMvpPresenter().getTodayRichestManData();
        initMicroAdapter();
        initSubscribe(this);
        getState();
        //最小化检测
        changeState();
       /* if (getActivity() != null)
            PermissionUtils.voicePermission(getActivity());*/
    }


    /**
     * 进入房间时，自己是房主自动上麦，如果不在麦上自动上麦
     * 如果自己不是房主但是在麦上，设置主播角色
     */
    protected void autoUpMicStateChange() {
        final String currentUid = String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        if (AvRoomDataManager.get().mMicQueueMemberMap != null) {
            boolean isOnMic = false;
            RoomMicInfo roomMicInfo = null;
            for (int i = 0; i < AvRoomDataManager.get().mMicQueueMemberMap.size(); i++) {
                RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().mMicQueueMemberMap.valueAt(i);
                if (roomQueueInfo != null && roomQueueInfo.mChatRoomMember != null
                        && Objects.equals(roomQueueInfo.mChatRoomMember.getAccount(), currentUid)) {//自己在队列里面
                    isOnMic = true;
                    roomMicInfo = roomQueueInfo.mRoomMicInfo;
                }
            }
            if (isOnMic) {
                if (!roomMicInfo.isMicMute()) {
                    //开麦
                    RtcEngineManager.get().setRole(io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER);
                    //是否需要开麦
                    if (!AvRoomDataManager.get().mIsNeedOpenMic) {
                        //闭麦
                        RtcEngineManager.get().setMute(true);
                        AvRoomDataManager.get().mIsNeedOpenMic = true;
                    }
                } else {
                    RtcEngineManager.get().setRole(io.agora.rtc.Constants.CLIENT_ROLE_AUDIENCE);
                }
            } else {
                if (AvRoomDataManager.get().isRoomOwner(currentUid)) {
                    final RoomInfo currentRoom = AvRoomDataManager.get().mCurrentRoomInfo;
                    if (currentRoom == null) {
                        return;
                    }
                    onOwnerUpMicroClick(-1, currentRoom.getUid());
                }
            }
        }
    }

    /**
     * 监听送礼物和谁来了的消息
     */
    private void observMsg() {
        IMNetEaseManager.get().subscribeChatRoomMsgFlowable(messages -> {
            if (messages.size() == 0) {
                return;
            }
            for (int i = 0; i < messages.size(); i++) {
                ChatRoomMessage chatRoomMessage = messages.get(i);
                if (chatRoomMessage == null) {
                    return;
                }
                if (IMReportRoute.sendMessageReport.equalsIgnoreCase(chatRoomMessage.getRoute())) {
                    IMCustomAttachment attachment = (IMCustomAttachment) chatRoomMessage.getAttachment();
                    if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT
                            || attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT) {
                        giftMsgList.add(0, chatRoomMessage);
                    }
                    dealWithMsg(attachment);
                }
            }
        }, this);
        IMNetEaseManager.get().subscribeChatRoomEventObservable(roomEvent -> {
            //: 2018/3/14
            if (roomEvent == null || roomEvent.getEvent() != RoomEvent.RECEIVE_MSG) {
                return;
            }
            ChatRoomMessage chatRoomMessage = roomEvent.getChatRoomMessage();
            if (chatRoomMessage == null) {
                return;
            }
            if (IMReportRoute.sendMessageReport.equalsIgnoreCase(chatRoomMessage.getRoute())) {
                IMCustomAttachment attachment = (IMCustomAttachment) chatRoomMessage.getAttachment();
                dealWithMsg(attachment);
            }
        }, this);
    }

    /**
     * 处理自定义消息
     */
    private void dealWithMsg(IMCustomAttachment attachment) {
        LogUtil.d("dealWithMsg", "second = " + attachment.getSecond());
        if (attachment.getSecond() == CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_OPEN) {
            bottomView(true);
        } else if (attachment.getSecond() == CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_CLOSE) {
            bottomView(false);
        }
        if (attachment.getFirst() == CUSTOM_MSG_TYPE_BURST_GIFT) {
            if (attachment instanceof BurstGiftAttachment) {
                BurstGiftAttachment info = (BurstGiftAttachment) attachment;
                if (info.getUid() == CoreManager.getCore(IAuthCore.class).getCurrentUid()) {
                    RewardGiftDialog reward = RewardGiftDialog.newInstance(info.getGiftName(), info.getGiftNum(), info.getPicUrl());
                    reward.show(getChildFragmentManager(), "gift_reward");
                    GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(info.getGiftId());
                    if (giftInfo != null) {
                        giftInfo.setUserGiftPurseNum(giftInfo.getUserGiftPurseNum() + info.getGiftNum());
                    }
                    CoreManager.notifyClients(IGiftCoreClient.class, IGiftCoreClient.refreshFreeGift);
                }
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 释放公屏和麦上的所有信息信息和动画
        long roomUid = intent.getLongExtra(Constants.ROOM_UID, 0);
        if (roomUid != 0 && roomUid != AvRoomDataManager.get().mCurrentRoomInfo.getUid()) {
            releaseView();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        onMusicPlayViewUpdate();
        onInviteUpMicHint();
        dealUserComeMsg();//检查一次是否有用户进人房间的消息未处理
    }

    //新增神豪榜
    protected void showSuperRichPopup() {
        if (popupContentView == null) {
            popupContentView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_super_rich_moon, null);
            popupWindow = new PopupWindow(popupContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);

        //  type 排行榜类型 0巨星榜，1贵族榜，2房间榜
        //   dateType 榜单周期类型 0日榜，1周榜，2总榜
        getMvpPresenter().getTodayRichestManData();
    }

    private void onOwnerUpMicroClick(final int micPosition, final long currentUid) {
        RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoByMicPosition(micPosition);
        if (roomQueueInfo == null) return;
        getMvpPresenter().upMicroPhone(micPosition, currentUid + "", false);
    }


    @Override
    protected void onReconnection(RoomEvent roomEvent) {
        getMvpPresenter().chatRoomReConnect(roomEvent.roomQueueInfo);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onMicInListChange();
    }

    @Override
    protected void upRoom(int event, RoomEvent roomEvent) {
        Log.e("messages1==", JSON.toJSONString(roomEvent));
        switch (event) {
            case RoomEvent.DOWN_CROWDED_MIC://挤下麦
                if (AvRoomDataManager.get().isOwner(roomEvent.getAccount())) {
                    toast(R.string.crowded_down);
                }
                updateMatchView(true);
                break;
            case RoomEvent.ROOM_MANAGER_ADD://添加管理员权限
            case RoomEvent.ROOM_MANAGER_REMOVE://移除管理员权限
                updateView();
                break;
            case RoomEvent.ROOM_INFO_UPDATE:
                Log.e("messages2==", JSON.toJSONString(roomEvent));
                updateView();
                updateRemoteMuteBtn();
                onMicInListChange();
                initLotteryBoxChange();
                updateRoomTip(roomEvent.getRoomInfo().getPlayInfo());
                break;
            case RoomEvent.ADD_BLACK_LIST:
                onChatRoomMemberBlackAdd(roomEvent.getAccount());
                break;
            case RoomEvent.MIC_QUEUE_STATE_CHANGE:
                onQueueMicStateChange(roomEvent.getMicPosition(), roomEvent.getPosState());
                break;
            case RoomEvent.KICK_DOWN_MIC://
                /*getMvpPresenter().downMicroPhone(roomEvent.getMicPosition(), false);*/
                SingleToastUtil.showToast(mContext.getResources().getString(R.string.kick_mic));
                updateMatchView(true);
                break;
            case RoomEvent.DOWN_MIC://下麦
                onDownMicro(roomEvent.getMicPosition());
                updateMatchView(true);
                break;
            case RoomEvent.UP_MIC:
                onUpMicro(roomEvent.getMicPosition());
                updateMatchView(false);
                break;
            case RoomEvent.INVITE_UP_MIC:
                onInviteUpMicHint();
                updateMatchView(false);
                break;
            default:
                break;
        }
    }


    public void onMicStateChanged() {
        updateMicBtn();
    }


    @CoreEvent(coreClientClass = IIMMessageCoreClient.class)
    public void onReceiveRecentContactChanged(List<RecentContact> imMessages) {
        changeState();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo userInfo) {
        changeState();
    }


    //------------------------------IShareCoreClient----------------------------------
    @CoreEvent(coreClientClass = IShareCoreClient.class)
    public void onShareRoom() {
//        toast("分享成功");
    }

    @CoreEvent(coreClientClass = IShareCoreClient.class)
    public void onShareRoomFail() {
        toast("分享失败，请重试");
    }

    @CoreEvent(coreClientClass = IShareCoreClient.class)
    public void onShareRoomCancel() {
        getDialogManager().dismissDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseView();
        isShowing = false;
        unbinder.unbind();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_nick) {
            if (giftAttachment == null) return;
            nickClickDialog(true);
        }
    }

    /**
     * 送礼物的昵称点击弹窗
     */
    private void nickClickDialog(boolean isTarget) {
        String account = "";
        String nick = "";
        String avatar = "";
        if (giftAttachment instanceof GiftAttachment) {
            GiftAttachment attachment = (GiftAttachment) giftAttachment;
            if (isTarget) {
                account = attachment.getGiftRecieveInfo().getUid() + "";
                nick = attachment.getGiftRecieveInfo().getNick();
                avatar = attachment.getGiftRecieveInfo().getAvatar();
            } else {
                account = attachment.getGiftRecieveInfo().getTargetUid() + "";
                nick = attachment.getGiftRecieveInfo().getTargetNick();
                avatar = attachment.getGiftRecieveInfo().getTargetAvatar();
            }
        } else if (giftAttachment instanceof MultiGiftAttachment) {
            MultiGiftAttachment attachment = (MultiGiftAttachment) giftAttachment;
            account = attachment.getMultiGiftRecieveInfo().getUid() + "";
            nick = attachment.getMultiGiftRecieveInfo().getNick();
            avatar = attachment.getMultiGiftRecieveInfo().getAvatar();
        }
        if (TextUtils.isEmpty(account)) return;
        final List<ButtonItem> buttonItems = new ArrayList<>();
        List<ButtonItem> items = ButtonItemFactory.createAllRoomPublicScreenButtonItems(mContext, account, nick, avatar);
        if (items == null) return;
        buttonItems.addAll(items);
        ((BaseMvpActivity) mContext).getDialogManager().showCommonPopupDialog(buttonItems, "取消");
    }


    /**
     * 控制公屏消息是否可以发送消息
     */
    protected void checkForbidSendMsg(ChatRoomMessage chatRoomMessage) {
        if (IMReportRoute.sendMessageReport.equalsIgnoreCase(chatRoomMessage.getRoute())) {
            if (chatRoomMessage.getAttachment() instanceof IMCustomAttachment) {
                if (((IMCustomAttachment) chatRoomMessage.getAttachment()).getSecond()
                        == CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_OPEN) {
                    bottomView(true);
                } else if (((IMCustomAttachment) chatRoomMessage.getAttachment()).getSecond()
                        == CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_CLOSE) {
                    bottomView(false);
                }
            }
        }
    }

    private SoftKeyBoardListener.OnSoftKeyBoardChangeListener
            mOnSoftKeyBoardChangeListener = new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
        @Override
        public void keyBoardShow(int height) {
            /*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
        }

        @Override
        public void keyBoardHide(int height) {
            /*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
            hideInputLayout();
        }
    };


    /**
     * 软键盘显示与隐藏的监听
     */
    protected void softKeyboardListener() {
        SoftKeyBoardListener.setListener(getActivity(), mOnSoftKeyBoardChangeListener);
    }

    @Override
    public void onRechargeBtnClick() {
        WalletActivity.start(getContext());
    }

    @Override
    public void onSendGiftBtnClick(GiftInfo giftInfo, long uid, int number, int currentP) {
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo == null) return;
        if (giftInfo == null) {
            return;
        }
        CoreManager.getCore(IGiftCore.class).sendRoomGift(giftInfo.getGiftId(), uid, currentRoomInfo.getUid(), number,
                giftInfo.getGoldPrice(), currentP);
    }

    @Override
    public void onSendGiftBtnClick(GiftInfo giftInfo, List<MicMemberInfo> micMemberInfos, int number, int currentP) {
        if (giftInfo == null) return;
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo == null) return;
        List<Long> targetUids = new ArrayList<>();
        for (int i = 0; i < micMemberInfos.size(); i++) {
            targetUids.add(micMemberInfos.get(i).getUid());
        }
        CoreManager.getCore(IGiftCore.class).sendRoomMultiGift(giftInfo.getGiftId(), targetUids,
                currentRoomInfo.getUid(), number, giftInfo.getGoldPrice(), currentP);
    }

    /**
     * 邀请上麦提示
     */
    void onInviteUpMicHint() {
        boolean isShowDialog = (boolean) SpUtils.get(getActivity(), SHOW_PASSIVITY_UP_MIC_HINT, false);
        if (isShowDialog) {
            ((BaseMvpActivity) getActivity()).getDialogManager().showOkBigTips(getString(R.string.tip_tips), getString(R.string.embrace_on_mic), true, null);
            SpUtils.put(getActivity(), SHOW_PASSIVITY_UP_MIC_HINT, false);
        }
    }

    protected abstract void onDownMicro(int micPosition);


    protected abstract void onQueueMicStateChange(int micPosition, int micPosState);


    @Override
    public void onSharePlatformClick(Platform platform) {
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null) {
            CoreManager.getCore(IShareCore.class).shareRoom(platform, currentRoomInfo.getUid(),
                    currentRoomInfo.getTitle());
        }
    }


    @Override
    public void resultLoadNormalMembers(List<IMChatRoomMember> chatRoomMemberList) {

    }

    @Override
    public SparseArray<ButtonItem> getAvatarButtonItemList(final int position,
                                                           final IMChatRoomMember chatRoomMember, RoomInfo currentRoom) {
        if (chatRoomMember == null || currentRoom == null) {
            return null;
        }
        SparseArray<ButtonItem> buttonItemMap = new SparseArray<>(10);
        ButtonItem buttonItem1 = ButtonItemFactory.createSendGiftItem(getContext(), chatRoomMember, this);
        ButtonItem buttonItem2 = ButtonItemFactory.createLockMicItem(position, new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                getMvpPresenter().closeMicroPhone(position);
            }
        });
        ButtonItem buttonItem3 = ButtonItemFactory.createKickDownMicItem(chatRoomMember.getNick(), chatRoomMember.getAccount());
        ButtonItem buttonItem4 = ButtonItemFactory.createKickOutRoomItem(String.valueOf(currentRoom.getRoomId()), mContext, chatRoomMember.getAccount(), chatRoomMember.getNick());
        ButtonItem buttonItem5 = ButtonItemFactory.createCheckUserInfoDialogItem(getContext(),
                chatRoomMember.getAccount());
        ButtonItem buttonItem6 = ButtonItemFactory.createDownMicItem();
        ButtonItem buttonItem7 = ButtonItemFactory.createFreeMicItem(position, new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                getMvpPresenter().openMicroPhone(position);
            }
        });
        ButtonItem buttonItem8 = ButtonItemFactory.createMarkManagerListItem(chatRoomMember.getNick(), String.valueOf(currentRoom.getRoomId()),
                chatRoomMember.getAccount(), true);
        ButtonItem buttonItem9 = ButtonItemFactory.createMarkManagerListItem(chatRoomMember.getNick(), String.valueOf(currentRoom.getRoomId()),
                chatRoomMember.getAccount(), false);
        ButtonItem buttonItem10 = ButtonItemFactory.createMarkBlackListItem(String.valueOf(currentRoom.getRoomId()), getContext(),
                chatRoomMember.getAccount(), chatRoomMember.getNick());
        buttonItemMap.put(0, buttonItem1);
        buttonItemMap.put(1, buttonItem2);
        buttonItemMap.put(2, buttonItem3);
        buttonItemMap.put(3, buttonItem4);
        buttonItemMap.put(4, buttonItem5);
        buttonItemMap.put(5, buttonItem6);
        buttonItemMap.put(6, buttonItem7);
        buttonItemMap.put(7, buttonItem8);
        buttonItemMap.put(8, buttonItem9);
        buttonItemMap.put(9, buttonItem10);
        return buttonItemMap;
    }

    @Override
    public void showMicAvatarClickDialog(List<ButtonItem> buttonItemList) {
        if (ListUtils.isListEmpty(buttonItemList)) {
            return;
        }
        getDialogManager().showCommonPopupDialog(buttonItemList, getString(R.string.cancel));
    }

    @Override
    public void showGiftDialog(IMChatRoomMember chatRoomMember) {
        GiftDialog dialog = new GiftDialog(getActivity(), Long.valueOf(chatRoomMember.getAccount()), chatRoomMember.getNick(), chatRoomMember.getAvatar(), false);
        dialog.setGiftDialogBtnClickListener(this);
        dialog.show();
    }

    @Override
    public void showOwnerSelfInfo(IMChatRoomMember chatRoomMember) {
        new NewUserInfoDialog(getActivity(), JavaUtil.str2long(chatRoomMember.getAccount())).show();
    }


    @Override
    public void showMicAvatarUserInfoDialog(String uId) {
        if (TextUtils.isEmpty(uId)) {
            return;
        }
        NewUserInfoDialog dialog = new NewUserInfoDialog(getActivity(), JavaUtil.str2long(uId));
        dialog.show();
    }

    @Override
    public void kickDownMicroPhoneSuccess() {
        updateMicBtn();
        toast(R.string.kick_mic);
    }

    public void eliminateMeiLi() {
        getDialogManager().showOkCancelDialog("确定将麦上用户的魅力值清零吗？", true, new DialogManager.AbsOkDialogListener() {
            @Override
            public void onOk() {
                getMvpPresenter().eliminateMeiLi();
            }
        });
    }

    @Override
    public void showOwnerClickDialog(final RoomMicInfo roomMicInfo, final int micPosition, final long currentUid,
                                     boolean isOwner) {
        List<ButtonItem> buttonItems = new ArrayList<>(4);
        final ButtonItem buttonItem1 = new ButtonItem(getString(R.string.embrace_up_mic),
                new ButtonItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        if(roomMicInfo.isMicLock()){
                            toast("此座位已经被封锁，请先解锁此座位");
                            return;
                        }
                        RoomInviteActivity.openActivity(getActivity(), micPosition);
                    }
                });
        ButtonItem buttonItem2 = new ButtonItem(
                roomMicInfo.isMicMute() ? getString(R.string.no_forbid_mic) : getString(R.string.forbid_mic),
                new ButtonItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        if (roomMicInfo.isMicMute()) {
                            getMvpPresenter().openMicroPhone(micPosition);
                        } else {
                            getMvpPresenter().closeMicroPhone(micPosition);
                        }
                    }
                });
        ButtonItem buttonItem3 = new ButtonItem(roomMicInfo.isMicLock() ? getString(R.string.unlock_mic) :
                getString(R.string.lock_mic), new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                if (roomMicInfo.isMicLock()) {
                    getMvpPresenter().unLockMicroPhone(micPosition);
                } else {
                    getMvpPresenter().lockMicroPhone(micPosition, currentUid);
                }
            }
        });
        ButtonItem buttonItem4 = new ButtonItem("移到此座位", new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                if(roomMicInfo.isMicLock()){
                    toast("此座位已经被封锁，请先解锁此座位");
                    return;
                }
                getMvpPresenter().upMicroPhone(micPosition, currentUid + "", false);
            }
        });
//        if(!roomMicInfo.isMicLock()){
//            buttonItems.add(buttonItem1);
//            buttonItems.add(buttonItem4);
//        }
        buttonItems.add(buttonItem1);
        buttonItems.add(buttonItem2);
        buttonItems.add(buttonItem3);
        if (!isOwner) {
            buttonItems.add(buttonItem4);
        }
        getDialogManager().showCommonPopupDialog(buttonItems, getString(R.string.cancel));
    }


    @Override
    public void notifyBottomBtnState() {
        showBottomViewForDifRole();
    }


    @Override
    public void reDaySuperRichFailure(String error) {
        //不做任何处理
    }

    @Override
    public void onAvatarBtnClick(int position) {
        getMvpPresenter().avatarClick(position);
    }

    @Override
    public void onUpMicBtnClick(int position, IMChatRoomMember chatRoomMember) {
        getMvpPresenter().microPhonePositionClick(position, chatRoomMember);
    }

    @Override
    public void onLockBtnClick(int position) {
        getMvpPresenter().unLockMicroPhone(position);
    }

    @Override
    public void onRoomSettingsClick() {
        if (AvRoomDataManager.get().isRoomOwner() || AvRoomDataManager.get().isRoomAdmin()) {
            RoomTopicActivity.start(getContext());
        } else {
            RoomTopicDIalog roomTopicDIalog = new RoomTopicDIalog();
            roomTopicDIalog.show(getChildFragmentManager());
        }
    }

    @Override
    public void onContributeListClick() {
        BigListDataDialog bigListDataDialog = BigListDataDialog.newContributionListInstance(getActivity());
        bigListDataDialog.setSelectOptionAction(new BigListDataDialog.SelectOptionAction() {
            @Override
            public void optionClick() {
                getDialogManager().showProgressDialog(mContext, "请稍后");
            }

            @Override
            public void onDataResponse() {

                //请求结束前退出可能会导致奔溃，直接捕获没关系
                try {
                    getDialogManager().dismissDialog();
                } catch (Exception e) {

                }

            }
        });
        bigListDataDialog.show(getChildFragmentManager());

//        ListDataDialog.newContributionListInstance(getActivity()).show(getChildFragmentManager());
    }

    @Override
    public void onOnlinePeopleClick() {
        ListDataDialog.newOnlineUserListInstance(getActivity()).show(getChildFragmentManager());
    }


    protected void showMicInListDialog() {
        if (!micInListOption) {
            return;
        }
        MicInListDialog micInListDialog = new MicInListDialog(mContext);
        boolean isRoomOwner = AvRoomDataManager.get().isRoomOwner();
        micInListDialog.isAdmin = AvRoomDataManager.get().isRoomAdmin() || isRoomOwner;
        micInListDialog.isRoomOwner = isRoomOwner;
        micInListDialog.iSubmitAction = () -> {
            boolean checkInMicInlist = AvRoomDataManager.get().checkInMicInlist();
            if (checkInMicInlist) {
                getMvpPresenter().removeMicInList();
            } else {
                getMvpPresenter().addMicInList();
            }

        };
        micInListDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 100) {
            if (data != null && data.getExtras() != null) {
                String account = data.getExtras().getString("account");
                String nickName = data.getExtras().getString("nickName");
                if (TextUtils.isEmpty(account)) return;
                int micPosition = data.getExtras().getInt(Constants.KEY_POSITION, Integer.MIN_VALUE);
                if (micPosition == Integer.MIN_VALUE) return;

                //抱人上麦
                getMvpPresenter().inviteMicroPhone(nickName, JavaUtil.str2long(account), micPosition);
            }
        }
    }

    void onChatRoomMemberBlackAdd(String account) {
        if (AvRoomDataManager.get().isRoomOwner(account)) {
            //当前是房主
            AvRoomDataManager.get().mRoomCreateMember = null;
        }
    }

    //防止外部新消息回调过来这边正在执行动画影响显示
    private boolean isShowing = false;

    @CoreEvent(coreClientClass = IAVRoomCoreClient.class)
    public void dealUserComeMsg() {
        if (isShowing) {
            return;
        }
        final RoomMemberComeInfo comeInfo = AvRoomDataManager.get().getAndRemoveFirstMemberComeInfo();
        if (comeInfo != null) {
            isShowing = true;
            /*显示座驾动画*/
            if (userComeAction != null && StringUtils.isNotEmpty(comeInfo.getCarImgUrl())) {
                userComeAction.showCar(comeInfo.getCarImgUrl());
            }
            /*显示弹幕 等级标志 + XXX + 骑着“XXX” 来了*/
            int level = comeInfo.getExperLevel();
//            level = 39;
            if (level >= 10) {//大于10级，显示用户进来的弹幕
                setLevel(level);
//                if (level < 20) {
//                    setLevelDraw(R.drawable.bg_come_msg_tip_lv10_19);
//                } else if (level < 30) {
//                    setLevelDraw(R.drawable.bg_come_msg_tip_lv20_29);
//                } else if (level < 40) {
//                    setLevelDraw(R.drawable.bg_come_msg_tip_lv30_39);
//                } else if (level < 50) {
//                    setLevelDraw(R.drawable.bg_come_msg_tip_lv40_49);
//                } else {
//                    setLevelDraw(R.drawable.bg_come_msg_tip_lv50_59);
//                }
                setLevelDraw(R.drawable.shape_80000000_r_10);
                setComeInfo(comeInfo);
                //做弹幕动画
                float inInterval = 0.16f;
                float inSpeed = 3f;
                float inValue = inInterval * inSpeed;

                float middleInterval = 0.95f;
                float middleSpeed = 0.06f;
                float middleValue = inValue + (middleInterval - inInterval) * middleSpeed;

                float outSpeed = 2.65f;
                TranslateAnimation animation = new TranslateAnimation(ScreenUtil.getDisplayWidth(),
                        -getResources().getDimensionPixelOffset(R.dimen.layout_come_msg_width),
                        0, 0);
                animation.setDuration(4000);
                animation.setInterpolator(new Interpolator() {
                    @Override
                    public float getInterpolation(float v) {
                        if (v < inInterval) {
                            return inSpeed * v;
                        } else if (v < middleInterval) {
                            return inValue + middleSpeed * (v - inInterval);
                        } else {
                            return middleValue + outSpeed * (v - middleInterval);
                        }
                    }
                });
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        upComeMsg(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        upComeMsg(View.INVISIBLE);
//                        AvRoomDataManager.get().addMemberComeInfo(comeInfo);
                        isShowing = false;
                        if (AvRoomDataManager.get().getMemberComeSize() > 0) {//队列中还有其他弹幕未处理完
                            dealUserComeMsg();//继续处理
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                startComeMsgAnim(animation);
            } else {
                isShowing = false;
            }
        } else {
            isShowing = false;
        }
    }

    @CoreEvent(coreClientClass = IAVRoomCoreClient.class)
    public void sendMsg(String msg) {
        if (getMvpPresenter() != null) {
            getMvpPresenter().sendTextMsg(msg);
        }

    }

    @CoreEvent(coreClientClass = IAVRoomCoreClient.class)
    public void micInlistMoveToTop(int micPosition, String roomId, long uid) {
        if (getMvpPresenter() != null) {

            if (TextUtils.isEmpty(roomId)) {
                roomId = AvRoomDataManager.get().mCurrentRoomInfo.getRoomId() + "";
            }
            getMvpPresenter().updataQueueExBySdk(micPosition, roomId, uid);
        }

    }

    @CoreEvent(coreClientClass = IAVRoomCoreClient.class)
    public void onMicInListToUpMic(int micPosition, String uid) {
        if (getMvpPresenter() != null && micPosition != -1) {
            AvRoomDataManager.get().mIsNeedOpenMic = false;
            getMvpPresenter().upMicroPhone(micPosition, uid, false, true);
            toast("您上麦了");
        }

    }

    /**
     * 自己发送消息成功 -- 更新礼物记录
     */
    @CoreEvent(coreClientClass = IIMRoomCoreClient.class)
    public void onSendRoomMessageSuccess(ChatRoomMessage message) {
        if (message == null) {
            return;
        }
        if (IMReportRoute.sendMessageReport.equalsIgnoreCase(message.getRoute())) {
            IMCustomAttachment attachment = (IMCustomAttachment) message.getAttachment();
            if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT
                    || attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT) {
                giftMsgList.add(0, message);
            }
        }
    }

}
