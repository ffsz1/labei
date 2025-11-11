package com.tongdaxing.xchat_core.im.room;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMemberUpdate;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomQueueChangeAttachment;
import com.netease.nimlib.sdk.util.Entry;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/5/20.
 */

public interface IIMRoomCoreClient extends ICoreClient {

    public static final String METHOD_ON_ENTER_ROOM = "onEnterRoom";
    public static final String METHOD_ON_ENTER_ROOM_FAITH = "onEnterRoomFail";
    public static final String METHOD_ON_QUITE_ROOM = "onQuiteRoom";
    public static final String METHOD_ON_ROOM_ONLINE_MEMBER_UPDATE = "onRoomOnlineMemberUpdate";
    public static final String METHOD_ON_ROOM_GUEST_MEMBER_UPDATE = "onRoomGuestMemberUpdate";
    public static final String METHOD_ON_MEMBER_IN = "onMemberIn";
    public static final String METHOD_ON_MEMBER_EXIT = "onMemberExit";
    public static final String METHOD_ON_MEMBER_UPDATE = "onMemberUpdate";

    public static final String METHOD_ON_KICK_OUT = "onKickOut";
    public static final String METHOD_ON_NEED_OUT = "onNeedOut";
    public static final String METHOD_ON_EXCEPTION = "onException";
    public static final String METHOD_ON_ONLINE_STATE_CHANGED = "onOnlineStateChanged";
    public static final String METHOD_ON_RECEIVE_CHATROOM_MESSAGES = "onReceiveChatRoomMessages";
    public static final String METHOD_ON_RECEIVE_CHATROOM_QUEUE_CHANGED = "onReceiveChatRoomQueueChanged";
    public static final String METHOD_ON_ROOM_INFO_UPDATE = "onRoomInfoUpdate";
    public static final String METHOD_ON_RECEIVE_CHATROOM_MY_ROOM_ROLE_UPDATE = "onReceiveChatRoomMyRoomRoleUpdate";
    public static final String METHOD_ON_QUERY_CHATROOM_QUEUE_SUCCESS = "onQueryChatRoomQueueSuccess";
    public static final String METHOD_ON_QUERY_CHATROOM_QUEUE_FAIL = "onQueryChatRoomQueueFail";
    public static final String METHOD_ON_UPDATE_CHATROOM_QUEUE_SUCCESS = "onUpdateChatRoomQueueSuccess";
    public static final String METHOD_ON_UPDATE_CHATROOM_QUEUE_FAIL = "onUpdateChatRoomQueueFail";
    public static final String METHOD_ON_UPDATE_MY_ROOM_ROLE_SUCCESS = "onUpdateMyRoomRoleSuccess";
    public static final String METHOD_ON_UPDATE_MY_ROOM_ROLE_FAIL = "onUpdateMyRoomRoleFail";
    public static final String METHOD_ON_SEND_ROOM_MESSAGE_SUCCESS = "onSendRoomMessageSuccess";
    public static final String METHOD_ON_QUERY_MANAGER_LIST = "onQueryManagerList";
    public static final String METHOD_ON_QUERY_MANAGER_LIST_FAIL = "onQueryManagerListFail";
    public static final String METHOD_ON_MARK_MANAGER_LIST = "onMarkManagerList";
    public static final String METHOD_ON_MARK_MANAGER_LIST_FAIL = "onMarkManagerListFail";
    public static final String METHOD_ON_QUERY_BLACK_LIST = "onQueryBlackList";
    public static final String METHOD_ON_QUERY_BLACK_LIST_FAIL = "onQueryBlackListFail";
    public static final String METHOD_ON_MARK_BLACK_LIST = "onMarkBlackList";
    public static final String METHOD_ON_MARK_BLACK_LIST_FAIL = "onMarkBlackListFail";
    public static final String METHOD_ON_MEMBER_BE_MANAGER = "onMemberBeManager";
    public static final String METHOD_ON_MEMBER_BE_REMOVE_MANAGER = "onMemberBeRemoveManager";
    String enterError = "enterError";

    void enterError();

    void onEnterRoom();

    void onEnterRoomFail(int code, String error);

    void onGetRoomInfo();

    void onQuiteRoom();

    void onRoomGuestMemberUpdate(List<IMChatRoomMember> guestMembers);

    void onRoomOnlineMemberUpdate(List<IMChatRoomMember> onlineMembers, List<IMChatRoomMember> normalMembers);

    void onMemberIn(IMChatRoomMember chatRoomMember);

    void onMemberUpdate(IMChatRoomMember chatRoomMember);

    void onMemberExit(String account);

    void onSendRoomMessageSuccess(ChatRoomMessage message);

    void onReceiveMessage();

    void onRegisterRoomOnlineStatus();

    void onKickMember();

    void onKickOut(ChatRoomKickOutEvent.ChatRoomKickOutReason reason);

    void onReceiveChatRoomMessages(List<ChatRoomMessage> chatRoomMessageList);

    void onReceiveChatRoomQueueChanged(ChatRoomQueueChangeAttachment attachment);

    void onRoomInfoUpdate();

    void onReceiveChatRoomMyRoomRoleUpdate(ChatRoomMessage chatRoomMessage);

    void onQueryChatRoomQueueSuccess(List<Entry<String, String>> entries);

    void onQueryChatRoomQueueFail();

    void onUpdateChatRoomQueueSuccess();

    void onUpdateChatRoomQueueFail();

    void onUpdateMyRoomRoleSuccess(ChatRoomMemberUpdate chatRoomMemberUpdate);

    void onUpdateMyRoomRoleFail();

    void onReceiveNoti(ChatRoomMessage msg);

    void onReceiveText(ChatRoomMessage msg);

    void onException();

    void onOnlineStateChanged(boolean isOnline);

    void onNeedOut();

    void onQueryManagerList(List<IMChatRoomMember> managerList);

    void onQueryManagerListFail();

    void onMarkManagerList(IMChatRoomMember chatRoomMember);

    void onMarkManagerListFail();

    void onQueryBlackList(List<IMChatRoomMember> blackList);

    void onQueryBlackListFail();

    void onMarkBlackList(IMChatRoomMember chatRoomMember);

    void onMarkBlackListFail();

    void onMemberBeManager(String account);

    void onMemberBeRemoveManager(String account);
}
