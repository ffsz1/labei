package com.tongdaxing.xchat_core.im.message;

import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/5/20.
 */

public class IMMessageCoreImpl extends AbstractBaseCore implements IIMMessageCore {

    public IMMessageCoreImpl() {
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);

        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(recentContactObserver, true);

        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(
                msgStatuObserver, true);


    }


    Observer<List<RecentContact>> recentContactObserver = new Observer<List<RecentContact>>() {


        @Override
        public void onEvent(List<RecentContact> recentContacts) {


            // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
            if (recentContacts != null && recentContacts.size() > 0) {
                notifyClients(IIMMessageCoreClient.class, IIMMessageCoreClient.METHOD_ON_RECEIVE_CONTACT_CHANGED, recentContacts);
            }
        }
    };

    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {





            // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
            if (messages != null && messages.size() > 0) {
                notifyClients(IIMMessageCoreClient.class, IIMMessageCoreClient.METHOD_ON_RECEIVE_PERSONAL_MESSAGES, messages);
            }
        }
    };

    Observer<IMMessage> msgStatuObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage imMessages) {
            if (imMessages != null) {

            }
        }
    };

    @Override
    public void clearAllUnreadMsg() {
        NIMClient.getService(MsgService.class).clearAllUnreadCount();
    }

    @Override
    public int queryUnreadMsg() {
        return NIMClient.getService(MsgService.class).getTotalUnreadCount();
    }

    @Override
    public void deleteRecentContact(String account) {
        NIMClient.getService(MsgService.class).deleteRecentContact2(account, SessionTypeEnum.P2P);
    }

    @Override
    public void updateMessageNotiConfig(StatusBarNotificationConfig config) {
// 更新消息提醒配置 StatusBarNotificationConfig
        NIMClient.updateStatusBarNotificationConfig(config);
    }

    @Override
    public void setMessageNoti(String account, boolean checkState) {
        NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {


            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    @Override
    public void setTeamMessageNoti(String teamId, boolean mute) {
//        NIMClient.getService(TeamService.class).muteTeam(teamId, mute).setCallback(new RequestCallback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//            }
//
//            @Override
//            public void onFailed(int i) {
//
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//
//            }
//        });
    }

    @Override
    public InvocationFuture<List<IMMessage>> pullMessageHistoryEx(IMMessage anchor, long toTime, int limit, QueryDirectionEnum direction, boolean persist) {
        return NIMClient.getService(MsgService.class).pullMessageHistoryEx(anchor, toTime, limit, direction, persist);
    }

    @Override
    public InvocationFuture<List<IMMessage>> pullMessageHistory(IMMessage anchor, int limit, boolean persist) {
        return NIMClient.getService(MsgService.class).pullMessageHistory(anchor, limit, persist);
    }

    @Override
    public InvocationFuture<List<IMMessage>> queryMessageListEx(IMMessage anchor, QueryDirectionEnum directionEnum, int limit, boolean asc) {
        return NIMClient.getService(MsgService.class).queryMessageListEx(anchor, directionEnum, limit, asc);
    }

    @Override
    public InvocationFuture<List<IMMessage>> queryMessageListExTime(IMMessage anchor, long toTime, QueryDirectionEnum direction, int limit) {
        return NIMClient.getService(MsgService.class).queryMessageListExTime(anchor, toTime, direction, limit);
    }

    @Override
    public List<IMMessage> queryMessageListByUuidBlock(List<String> uuids) {
        return NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
    }

    @Override
    public InvocationFuture<List<IMMessage>> queryMessageListByUuid(List<String> uuids) {
//        List<String> uuids = new ArrayList<>();
//        uuids.add(message.getUuid());
        return NIMClient.getService(MsgService.class).queryMessageListByUuid(uuids);
    }

    @Override
    public InvocationFuture<List<IMMessage>> queryMessageListByType(MsgTypeEnum msgTypeEnum, IMMessage anchor, int limit) {
        return NIMClient.getService(MsgService.class).queryMessageListByType(msgTypeEnum, anchor, limit);
    }

    @Override
    public void searchMessageHistory(String keyword, List<String> fromAccounts, IMMessage anchor, int limit) {
        NIMClient.getService(MsgService.class).searchMessageHistory(keyword, fromAccounts, anchor, limit)
                .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                    @Override
                    public void onResult(int i, List<IMMessage> imMessages, Throwable throwable) {

                    }
                });
    }

    @Override
    public void searchAllMessageHistory(String keyword, List<String> fromAccounts, long time, int limit) {
        NIMClient.getService(MsgService.class).searchAllMessageHistory(keyword, fromAccounts, time, limit)
                .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                    @Override
                    public void onResult(int i, List<IMMessage> imMessages, Throwable throwable) {

                    }
                });
    }

    @Override
    public void deleteChattingHistory(IMMessage message) {
        NIMClient.getService(MsgService.class).deleteChattingHistory(message);
    }

    @Override
    public void clearChattingHistory(String account, SessionTypeEnum sessionTypeEnum) {
        NIMClient.getService(MsgService.class).clearChattingHistory(account, sessionTypeEnum);
    }

    @Override
    public void setChattingAccount(String account, SessionTypeEnum sessionType) {
        // 进入聊天界面，建议放在onResume中
        NIMClient.getService(MsgService.class).setChattingAccount(account, sessionType);
//         进入最近联系人列表界面，建议放在onResume中
//        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        // 退出聊天界面或离开最近联系人列表界面，建议放在onPause中
//        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);


    }

    //------------------------消息提醒-----------------------
    @Override
    public IMMessage sendGiftMsg(int giftId, long uid, int num) {
//        long myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
//        UserInfo myUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(myUid);
//        GiftAttachment giftAttachment = new GiftAttachment(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT, IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_SEND_GIFT);
//        giftAttachment.setUid(myUid + "");
//        GiftReceiveInfo giftRecieveInfo = new GiftReceiveInfo();
//        giftRecieveInfo.setNick(myUserInfo.getNick());
//        giftRecieveInfo.setTargetUid(uid);
//        giftRecieveInfo.setAvatar(myUserInfo.getAvatar());
//        giftRecieveInfo.setGiftId(giftId);
//        giftRecieveInfo.setUid(myUid);
//        giftRecieveInfo.setGiftNum(num);
//        giftAttachment.setGiftRecieveInfo(giftRecieveInfo);
//        CustomMessageConfig customMessageConfig = new CustomMessageConfig();
//        customMessageConfig.enablePush = false;
//        IMMessage imMessage = MessageBuilder.createCustomMessage(uid + "", SessionTypeEnum.P2P, "", giftAttachment, customMessageConfig);
//        NIMClient.getService(MsgService.class).sendMessage(imMessage, false);
//        return imMessage;
        return null;
    }

    //    public IMMessage sendCallMsg(long uid) {
//        long myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
//        UserInfo myUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(myUid);
//        CallInfo callInfo = new CallInfo();
//        callInfo.setUid(myUid);
//        callInfo.setActivity(myUserInfo.getActivity());
//        CallAttachment callAttachment = new CallAttachment(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_CALL,IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_CALL_RECEIVE);
//        callAttachment.setCallInfo(callInfo);
//        CustomMessageConfig customMessageConfig = new CustomMessageConfig();
//        IMMessage imMessage = MessageBuilder.createCustomMessage(uid+"", SessionTypeEnum.P2P, "", callAttachment, customMessageConfig);
//        NIMClient.getService(MsgService.class).sendMessage(imMessage, false);
//        return imMessage;
//    }
}
