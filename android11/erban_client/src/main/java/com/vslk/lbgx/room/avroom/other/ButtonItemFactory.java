package com.vslk.lbgx.room.avroom.other;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.AuctionModel;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.avroom.activity.RoomBlackListActivity;
import com.vslk.lbgx.room.avroom.activity.RoomManagerListActivity;
import com.vslk.lbgx.room.gift.GiftDialog;
import com.vslk.lbgx.room.widget.dialog.AuctionDialog;
import com.vslk.lbgx.room.widget.dialog.NewUserInfoDialog;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_ADD_ROOM_BLACK;

//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_LET_SOMEONE_OUT_ROOM;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_SET_MANAGER_CLOSE;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_SET_MANAGER_OPEN;


/**
 * @author chenran
 * @date 2017/10/11
 */
public class ButtonItemFactory {

    /**
     * 公屏点击的所有items
     *
     * @return -
     */
    public static List<ButtonItem> createAllRoomPublicScreenButtonItems(Context context, String account, String nick, String avatar) {

        if (AvRoomDataManager.get().mCurrentRoomInfo == null || TextUtils.isEmpty(account)) {
            return null;
        }
        List<ButtonItem> buttonItems = new ArrayList<>();
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        String roomId = String.valueOf(roomInfo.getRoomId());
        LogUtils.d("ButtonItemFactory", "account:" + account + "   " + "nick:" + nick);
        boolean isMyself = AvRoomDataManager.get().isOwner(account);
        boolean isTargetGuess = AvRoomDataManager.get().isGuess(account);
        boolean isTargetRoomAdmin = AvRoomDataManager.get().isRoomAdmin(account);
        boolean isTargetRoomOwner = AvRoomDataManager.get().isRoomOwner(account);
        boolean isTargetOnMic = AvRoomDataManager.get().isOnMic(account);
        boolean isInAuctionNow = AuctionModel.get().isInAuctionNow();
        boolean isTypeAuction = roomInfo.getType() == RoomInfo.ROOMTYPE_AUCTION;
        // 房主点击
        if (AvRoomDataManager.get().isRoomOwner()) {
            if (isTargetRoomOwner) {
                // 非竞拍房
                if (!isTypeAuction) {
                    showUserDialog(context, account);
                    return null;
                }
                //送礼物
                buttonItems.add(createSendGiftItem(context, account, nick, avatar));
                // 查看资料
                buttonItems.add(createCheckUserInfoDialogItem(context, account));
                // 发起竞拍
                buttonItems.add(createStartAuctionItem(context, JavaUtil.str2long(roomId), account));
            } else if (isTargetRoomAdmin || isTargetGuess) {

                //送礼物
                buttonItems.add(createSendGiftItem(context, account, nick, avatar));
                // 查看资料
                buttonItems.add(createCheckUserInfoDialogItem(context, account));
                // 发起竞拍
                if (isTypeAuction && !isInAuctionNow) {
                    buttonItems.add(createStartAuctionItem(context, JavaUtil.str2long(roomId), account));
                }
                // 抱Ta上麦 （无法选择麦位，暂时去掉）
//                if (!isTargetOnMic) {
//                    buttonItems.add(createInviteOnMicItem(nick, account));
//                }
                // 踢出房间
                buttonItems.add(createKickOutRoomItem(roomId, context, account, nick));
                // 增加/移除管理员
                buttonItems.add(createMarkManagerListItem(nick, roomId, account, isTargetGuess));
                // 加入黑名单
                buttonItems.add(createMarkBlackListItem(roomId, context, account, nick));
            }
        } else if (AvRoomDataManager.get().isRoomAdmin()) {
            if (isMyself) {
                // 非竞拍房
                if (!isTypeAuction) {
                    showUserDialog(context, account);
                    return null;
                }
                // 查看资料
                buttonItems.add(createCheckUserInfoDialogItem(context, account));
                // 发起竞拍
                buttonItems.add(createStartAuctionItem(context, JavaUtil.str2long(roomId), account));
            } else if (isTargetRoomAdmin || isTargetRoomOwner) {
                if (isTargetOnMic) {
                    showUserDialog(context, account);
                    return null;
                }
                //送礼物
//                showGiftDialog(context, account, nick, avatar);
                //资料卡片
                showUserDialog(context, account);
                return null;
//                buttonItems.add(createSendGiftItem(context, account, nick, avatar));
                // 查看资料
//                buttonItems.add(createCheckUserInfoDialogItem(context, account));
                // 发起竞拍
//                if (isTypeAuction && !isInAuctionNow) {
//                    buttonItems.add(createStartAuctionItem(context, JavaUtil.str2long(roomId), account));
//                }
                // 抱Ta上麦（无法选择麦位，暂时去掉）
//                buttonItems.add(createInviteOnMicItem(nick, account));
            } else if (isTargetGuess) {
                //送礼物
                buttonItems.add(createSendGiftItem(context, account, nick, avatar));
                // 查看资料
                buttonItems.add(createCheckUserInfoDialogItem(context, account));
                // 发起竞拍
                if (isTypeAuction && !isInAuctionNow) {
                    buttonItems.add(createStartAuctionItem(context, JavaUtil.str2long(roomId), account));
                }
                // 抱Ta上麦（无法选择麦位，暂时去掉）
//                if (!isTargetOnMic) {
//                    buttonItems.add(createInviteOnMicItem(nick, account));
//                }
                // 踢出房间
                buttonItems.add(createKickOutRoomItem(roomId, context, account, nick));
                // 加入黑名单
                buttonItems.add(createMarkBlackListItem(roomId, context, account, nick));
            }
        } else if (AvRoomDataManager.get().isGuess()) {
//            if ((CoreManager.getCore(IAuthCore.class).getCurrentUid() + "").equals(account)) {
                showUserDialog(context, account);
//            } else {
//                showGiftDialog(context, account, nick, avatar);
//            }
            return null;
        }
        return buttonItems;
    }


    private static void showUserDialog(Context context, String account) {
        NewUserInfoDialog.showUserDialog(context, JavaUtil.str2long(account));
    }

    /**
     * 出现礼物弹框
     */
    private static void showGiftDialog(Context context, String account, String nick, String avatar) {
        GiftDialog giftDialog = new GiftDialog(context, JavaUtil.str2long(account), nick, avatar);
        giftDialog.setGiftDialogBtnClickListener(new GiftDialog.OnGiftDialogBtnClickListener() {
            @Override
            public void onRechargeBtnClick() {
                WalletActivity.start(context);
            }

            @Override
            public void onSendGiftBtnClick(GiftInfo giftInfo, long uid, int number, int currentP) {
                RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (currentRoomInfo == null) {
                    return;
                }
                CoreManager.getCore(IGiftCore.class).sendRoomGift(giftInfo.getGiftId(), uid, currentRoomInfo.getUid(), number, giftInfo.getGoldPrice(), currentP);
            }

            @Override
            public void onSendGiftBtnClick(GiftInfo giftInfo, List<MicMemberInfo> micMemberInfos, int number, int currentP) {
                RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (currentRoomInfo == null) {
                    return;
                }
                List<Long> targetUids = new ArrayList<>();
                for (int i = 0; i < micMemberInfos.size(); i++) {
                    targetUids.add(micMemberInfos.get(i).getUid());
                }
                CoreManager.getCore(IGiftCore.class).sendRoomMultiGift(giftInfo.getGiftId(), targetUids, currentRoomInfo.getUid(), number, giftInfo.getGoldPrice(), currentP);
            }
        });
        giftDialog.show();
    }

    /**
     * 发起竞拍
     */
    private static ButtonItem createStartAuctionItem(final Context context, final long roomUid, final String account) {
        return new ButtonItem("发起竞拍", () -> {
            AuctionDialog dialog = new AuctionDialog(context, JavaUtil.str2long(account));
            dialog.setOnClickItemListener(new AuctionDialog.OnClickItemListener() {
                @Override
                public void onClickHead() {
                    UIHelper.showUserInfoAct(context, JavaUtil.str2long(account));
                }

                @Override
                public void onClickBegin(int price) {
                    AuctionModel.get().startAuction(roomUid, JavaUtil.str2long(account), price,
                            30, 10, "暂无竞拍描述");
                }
            });
            dialog.show();
        });
    }


    public interface OnItemClick {
        void itemClick();
    }

    public static ButtonItem createMsgBlackListItem(String s, OnItemClick onItemClick) {
        return new ButtonItem(s, () -> onItemClick.itemClick());
    }


    /**
     * 踢Ta下麦
     */
    public static ButtonItem createKickDownMicItem(final String nickName, final String account) {
        return new ButtonItem(BasicConfig.INSTANCE.getAppContext().getString(R.string.embrace_down_mic), () -> {
            if (AvRoomDataManager.get().isOnMic(JavaUtil.str2long(account))) {
                int micPosition = AvRoomDataManager.get().getMicPosition(JavaUtil.str2long(account));
                IMNetEaseManager.get().downMicroPhoneBySdk(micPosition, null);

                RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (roomInfo != null) {
                    IMNetEaseManager.get().kickMicroPhoneBySdk(nickName, JavaUtil.str2long(account), roomInfo.getRoomId());
                }
            }
        });
    }

    /**
     * 拉他上麦
     */
    public static ButtonItem createInviteOnMicItem(final String nickName, final String account) {
        return new ButtonItem(BasicConfig.INSTANCE.getAppContext().getString(R.string.embrace_up_mic), () -> {
            int freePosition = AvRoomDataManager.get().findFreePosition();
            if (freePosition == Integer.MIN_VALUE) {
                SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext().getString(R.string.no_empty_mic));
                return;
            }
            IMNetEaseManager.get().inviteMicroPhoneBySdk(nickName, JavaUtil.str2long(account),
                    freePosition);
        });
    }

    /**
     * 踢出房间:  先强制下麦，再踢出房间
     */
    public static ButtonItem createKickOutRoomItem(final String roomId, final Context context, final String account, final String nick) {
        return new ButtonItem("踢出房间", () -> {
            ((BaseMvpActivity) context).getDialogManager().showOkCancelDialog(
                    "是否将" + nick + "踢出房间？", true, new DialogManager.AbsOkDialogListener() {
                        @Override
                        public void onOk() {
                            IMNetEaseManager.get().kickMember(account, new OkHttpManager.MyCallBack<Json>() {
                                @Override
                                public void onError(Exception e) {
                                    SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), "网络异常");
                                }

                                @Override
                                public void onResponse(Json response) {
                                    JSONObject jsonObject = JSON.parseObject(response.toString());
                                    int errno = jsonObject.getIntValue("errno");
                                    if (errno != 0) {
//                                        SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), imReportBean.getReportData().errmsg + "");
                                    } else {
//                                        RoomQueueMsgAttachment queueMsgAttachment = new RoomQueueMsgAttachment(CUSTOM_MSG_HEADER_TYPE_QUEUE,
//                                                CUSTOM_MSG_LET_SOMEONE_OUT_ROOM);
//                                        //queueMsgAttachment.uid = String.valueOf(micUid);
//                                        queueMsgAttachment.adminOrManagerUid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";//群主或者管理员uid
//                                        queueMsgAttachment.micName = nick;
//                                        ChatRoomMessage message = new ChatRoomMessage();
//                                        message.setRoom_id(String.valueOf(roomId));
//                                        message.setAttachment(queueMsgAttachment);
//                                        ReUsedSocketManager.get().sendCustomMessage(roomId + "", message, new IMSendCallBack() {
//                                            @Override
//                                            public void onSuccess(String data) {
//
//                                            }
//
//                                            @Override
//                                            public void onError(int errorCode, String errorMsg) {
//
//                                            }
//                                        });
                                    }
                                }
                            });
                        }
                    });
        });
    }

    /**
     * 查看资料弹窗
     */
    public static ButtonItem createCheckUserInfoDialogItem(final Context context, final String account) {
        return new ButtonItem("查看资料", () -> showUserDialog(context, account));
    }

    /**
     * 查看资料页面
     */
    public static ButtonItem createCheckUserInfoActivityItem(final Context context, final String account) {
        return new ButtonItem("查看资料", () -> UserInfoActivity.start(context, JavaUtil.str2long(account)));
    }

    /**
     * 下麦
     */
    public static ButtonItem createDownMicItem() {
        return new ButtonItem(BasicConfig.INSTANCE.getAppContext().getString(R.string.down_mic_text), () -> {
            long currentUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
            IMNetEaseManager.get().downMicroPhoneBySdk(AvRoomDataManager.get().getMicPosition(currentUid), null);
        });
    }


    //设置管理员
    public static ButtonItem createMarkManagerListItem(final String nickName, final String roomId, final String account, final boolean mark) {
        String title = BasicConfig.INSTANCE.getAppContext().getString(mark ? R.string.set_manager : R.string.remove_manager);
        return new ButtonItem(title, () -> IMNetEaseManager.get().markManager(account, mark, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                Log.e("createMarkManager-e",JSON.toJSONString(e));

            }

            @Override
            public void onResponse(Json response) {
                Log.e("createMarkManager",response.toString());
//                int second = "设置管理员".equals(title) ? CUSTOM_MSG_SET_MANAGER_OPEN : CUSTOM_MSG_SET_MANAGER_CLOSE;
//                RoomQueueMsgAttachment queueMsgAttachment = new RoomQueueMsgAttachment(CUSTOM_MSG_HEADER_TYPE_QUEUE,
//                        second);
////                queueMsgAttachment.uid = String.valueOf(micUid);
//                queueMsgAttachment.adminOrManagerUid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";//群主或者管理员uid
//                queueMsgAttachment.micName = nickName;
//                ChatRoomMessage message = new ChatRoomMessage();
//                message.setRoom_id(String.valueOf(roomId));
//                message.setAttachment(queueMsgAttachment);
//                ReUsedSocketManager.get().sendCustomMessage(roomId + "", message, new IMSendCallBack() {
//                    @Override
//                    public void onSuccess(String data) {
//
//                    }
//
//                    @Override
//                    public void onError(int errorCode, String errorMsg) {
//
//                    }
//                });
            }
        }));
    }

    //加入黑名单
    public static ButtonItem createMarkBlackListItem(final String roomId, final Context context, String account, String nick) {
        return new ButtonItem("加入房间黑名单", () -> {
            ((BaseMvpActivity) context).getDialogManager().showOkCancelDialog("是否将" + nick + "加入黑名单？加入后他将无法进入此房间", true, new DialogManager.AbsOkDialogListener() {

                @Override
                public void onOk() {
                    //现在拉黑，后端会把用户从队列移除
                    IMNetEaseManager.get().markBlackList(account, true, new OkHttpManager.MyCallBack<Json>() {

                        @Override
                        public void onError(Exception e) {

                        }

                        @Override
                        public void onResponse(Json response) {
//                            RoomQueueMsgAttachment queueMsgAttachment = new RoomQueueMsgAttachment(CUSTOM_MSG_HEADER_TYPE_QUEUE,
//                                    CUSTOM_MSG_ADD_ROOM_BLACK);
//                            //queueMsgAttachment.uid = String.valueOf(micUid);
//                            queueMsgAttachment.adminOrManagerUid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";//群主或者管理员uid
//                            queueMsgAttachment.micName = nick;
//                            ChatRoomMessage message = new ChatRoomMessage();
//                            message.setRoom_id(String.valueOf(roomId));
//                            message.setAttachment(queueMsgAttachment);
//                            ReUsedSocketManager.get().sendCustomMessage(roomId + "", message, new IMSendCallBack() {
//                                @Override
//                                public void onSuccess(String data) {
//
//                                }
//
//                                @Override
//                                public void onError(int errorCode, String errorMsg) {
//
//                                }
//                            });

                        }
                    });
                }
            });
        });
    }

    //发送礼物

    public static ButtonItem createSendGiftItem(final Context context, final IMChatRoomMember chatRoomMember, final GiftDialog.OnGiftDialogBtnClickListener giftDialogBtnClickListener) {
        ButtonItem buttonItem = new ButtonItem("送礼物", () -> {
            GiftDialog dialog = new GiftDialog(context, Long.valueOf(chatRoomMember.getAccount()), chatRoomMember.getNick(), chatRoomMember.getAvatar(), false);
            if (giftDialogBtnClickListener != null) {
                dialog.setGiftDialogBtnClickListener(giftDialogBtnClickListener);
            }
            dialog.show();
        });
        return buttonItem;
    }

    public static ButtonItem createSendGiftItem(final Context context, String uid, String nick, String avatar) {
        ButtonItem buttonItem = new ButtonItem("送礼物", () -> showGiftDialog(context, uid, nick, avatar));
        return buttonItem;
    }

    /**
     * 禁麦
     */
    public static ButtonItem createLockMicItem(final int position, ButtonItem.OnClickListener onClickListener) {
        return new ButtonItem(BasicConfig.INSTANCE.getAppContext().getString(R.string.forbid_mic), onClickListener);
    }

    /**
     * 取消禁麦
     */
    public static ButtonItem createFreeMicItem(final int position, ButtonItem.OnClickListener onClickListener) {
        return new ButtonItem(BasicConfig.INSTANCE.getAppContext().getString(R.string.no_forbid_mic), onClickListener);
    }

    public static ButtonItem createManagerListItem(final Context context) {
        ButtonItem buttonItem = new ButtonItem("管理员", () -> RoomManagerListActivity.start(context));
        return buttonItem;
    }

    public static ButtonItem createBlackListItem(final Context context) {
        ButtonItem buttonItem = new ButtonItem("黑名单", () -> RoomBlackListActivity.start(context));
        return buttonItem;
    }

    public static ButtonItem createPublicSwitch(int publicChatSwitch) {
        ButtonItem buttonItem = new ButtonItem(publicChatSwitch == 0 ? "关闭房间内聊天" : "开启房间内聊天", () -> {
            IAuthCore iAuthCore = CoreManager.getCore(IAuthCore.class);
            IAuthCore core = CoreManager.getCore(IAuthCore.class);
            if (core == null || iAuthCore == null) {
                return;
            }
            String ticket = iAuthCore.getTicket();
            long currentUid = core.getCurrentUid();
            CoreManager.getCore(IAVRoomCore.class).changeRoomMsgFilter(AvRoomDataManager.get().isRoomOwner(), publicChatSwitch == 0 ? 1 : 0, ticket, currentUid);
        });
        return buttonItem;
    }

    /**
     * 举报功能按钮
     *
     * @param context
     * @param title   房间内为举报房间  个人为举报
     * @param type    个人 和 房间举报
     * @return
     */
    public static ButtonItem createReportItem(Context context, String title, int type, long uid, String avatar, String album) {
        ButtonItem buttonItem = new ButtonItem(title, () -> {
            if (context == null) {
                return;
            }
            List<ButtonItem> buttons = new ArrayList<>();
            ButtonItem button5 = new ButtonItem("举报头像", () -> reportAvatar(context, type, 5, uid, avatar));
            ButtonItem button6 = new ButtonItem("举报昵称", () -> reportCommit(context, type, 6));
            ButtonItem button7 = new ButtonItem("举报相册", () -> reportAlbum(context, type, 7, uid, album));

            ButtonItem button1 = new ButtonItem("政治敏感", () -> reportCommit(context, type, 1));
            ButtonItem button2 = new ButtonItem("色情低俗", () -> reportCommit(context, type, 2));
            ButtonItem button3 = new ButtonItem("广告骚扰", () -> reportCommit(context, type, 3));
            ButtonItem button4 = new ButtonItem("人身攻击", () -> reportCommit(context, type, 4));

            buttons.add(button5);
            buttons.add(button6);
            buttons.add(button7);

            buttons.add(button1);
            buttons.add(button2);
            buttons.add(button3);
            buttons.add(button4);
            DialogManager dialogManager = null;
            if (context instanceof BaseMvpActivity) {
                dialogManager = ((BaseMvpActivity) context).getDialogManager();
            } else if (context instanceof BaseActivity) {
                dialogManager = ((BaseActivity) context).getDialogManager();
            }
            if (dialogManager != null) {
                dialogManager.showCommonPopupDialog(buttons, "取消");
            }
        });
        return buttonItem;
    }

    /**
     * uid '被举报的用户',
     * report_uid  '举报的用户',
     * report_type '举报类型 1,政治敏感 2,色情低俗 3,广告骚扰 4,人身攻击 5,举报头像 6,举报昵称 7,举报相册 8,举报房名 ',
     * type  		'类型 1. 用户 2. 房间',
     *
     * @param reportType
     */
    public static void reportCommit(Context context, int type, int reportType) {
        if (context != null) {
            if (context instanceof BaseMvpActivity) {
                ((BaseMvpActivity) context).toast("举报成功，我们会尽快为您处理");
            } else if (context instanceof BaseActivity) {
                ((BaseActivity) context).toast("举报成功，我们会尽快为您处理");
            }
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("reportType", reportType + "");
        params.put("type", type + "");
        params.put("phoneNo", CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo() == null ? "" : CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo().getPhone());
        params.put("reportUid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("uid", AvRoomDataManager.get().mCurrentRoomInfo == null ? "0" : (AvRoomDataManager.get().mCurrentRoomInfo.getUid() + ""));

        OkHttpManager.getInstance().doPostRequest(UriProvider.reportUrl(), params, null);
    }

    /**
     * uid '被举报的用户',
     * report_uid  '举报的用户',
     * report_type '举报类型 1,政治敏感 2,色情低俗 3,广告骚扰 4,人身攻击 5,举报头像 6,举报昵称 7,举报相册 8,举报房名 ',
     * type  		'类型 1. 用户 2. 房间',
     *
     * @param reportType
     */
    public static void reportCommit(Context context, int type, int reportType, long uid) {
        if (context != null) {
            if (context instanceof BaseMvpActivity) {
                ((BaseMvpActivity) context).toast("举报成功，我们会尽快为您处理");
            } else if (context instanceof BaseActivity) {
                ((BaseActivity) context).toast("举报成功，我们会尽快为您处理");
            }
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("reportType", reportType + "");
        params.put("type", type + "");
        params.put("phoneNo", CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo() == null ? "" : CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo().getPhone());
        params.put("reportUid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("uid", String.valueOf(uid));

        OkHttpManager.getInstance().doPostRequest(UriProvider.reportUrl(), params, null);
    }

    /**
     * 举报头像
     *
     * @param uid        '被举报的用户',
     * @param uid        '举报的用户',
     * @param reportType '举报类型 1,政治敏感 2,色情低俗 3,广告骚扰 4,人身攻击 5,举报头像 6,举报昵称 7,举报相册 8,举报房名',
     * @param type       '类型 1. 用户 2. 房间',
     * @param extend     被举报的用户头像 extend
     */
    public static void reportAvatar(Context context, int type, int reportType, long uid, String extend) {
        if (context != null) {
            if (context instanceof BaseMvpActivity) {
                ((BaseMvpActivity) context).toast("举报成功，我们会尽快为您处理");
            } else if (context instanceof BaseActivity) {
                ((BaseActivity) context).toast("举报成功，我们会尽快为您处理");
            }
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("reportType", reportType + "");
        params.put("type", type + "");
        params.put("phoneNo", CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo() == null ? "" : CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo().getPhone());
        params.put("reportUid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("uid", String.valueOf(uid));
        params.put("extend", extend);

        OkHttpManager.getInstance().doPostRequest(UriProvider.reportUrl(), params, null);
    }

    /**
     * 举报相册
     * uid '被举报的用户',
     * report_uid  '举报的用户',
     * report_type '举报类型 1,政治敏感 2,色情低俗 3,广告骚扰 4,人身攻击 5,举报头像 6,举报昵称 7,举报相册 8,举报房名 ',
     * type  		'类型 1. 用户 2. 房间',
     *
     * @param reportType
     */
    public static void reportAlbum(Context context, int type, int reportType, long uid, String extend) {
        if (context != null) {
            if (context instanceof BaseMvpActivity) {
                ((BaseMvpActivity) context).toast("举报成功，我们会尽快为您处理");
            } else if (context instanceof BaseActivity) {
                ((BaseActivity) context).toast("举报成功，我们会尽快为您处理");
            }
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("reportType", reportType + "");
        params.put("type", type + "");
        params.put("phoneNo", CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo() == null ? "" : CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo().getPhone());
        params.put("reportUid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("uid", String.valueOf(uid));
        params.put("extend", extend);

        OkHttpManager.getInstance().doPostRequest(UriProvider.reportUrl(), params, null);
    }

}