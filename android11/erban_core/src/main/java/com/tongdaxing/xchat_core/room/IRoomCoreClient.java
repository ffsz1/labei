package com.tongdaxing.xchat_core.room;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/5/28.
 */

public interface IRoomCoreClient extends ICoreClient {
    public static final String METHOD_ON_OPEN_ROOM = "onOpenRoom";
    public static final String METHOD_ON_OPEN_ROOM_FAIL = "onOpenRoomFail";
    public static final String METHOD_ON_ALREADY_OPENED_ROOM= "onAlreadyOpenedRoom";

    public static final String METHOD_ON_ENTER= "onEnter";
    public static final String METHOD_ON_ENTER_FAIL = "onEnterFail";

    public static final String METHOD_ON_QUIT_ROOM = "onQuitRoom";

    public static final String METHOD_ON_BE_KICK_OUT= "onBeKickOut";

    public static final String METHOD_ON_GET_ROOM_INFO = "onGetRoomInfo";
    public static final String METHOD_ON_GET_ROOM_INFO_FAIL = "onGetRoomInfoFail";

    public static final String METHOD_ON_UPDATE_ROOM_INFO = "onUpdateRoomInfo";
    public static final String METHOD_ON_UPDATE_ROOM_INFO_FAIL = "onUpdateRoomInfoFail";

    public static final String METHOD_ON_CURRENT_ROOM_INFO_UPDATE = "onCurrentRoomInfoUpdate";

    public static final String METHOD_ON_CLOSE_ROOM_INFO = "onCloseRoomInfo";
    public static final String METHOD_ON_CLOSE_ROOM_INFO_FAIL = "onCloseRoomInfoFail";

    public static final String METHOD_ON_GET_ROOM_CONSUME_LIST = "onGetRoomConsumeList";
    public static final String METHOD_ON_GET_ROOM_CONSUME_LIST_FAIL = "onGetRoomConsumeListFail";

    public static final String METHOD_ON_SEARCH_ROOM = "onSearchRoom";
    public static final String METHOD_ON_SEARCH_ROOM_FAIL = "onSearchRoomFail";

    public static final String METHOD_ON_CURRENT_ROOM_RECEIVE_NEW_MSG = "onCurrentRoomReceiveNewMsg";

    public static final String METHOD_ON_GET_USER_ROOM = "onGetUserRoom";
    public static final String METHOD_ON_GET_USER_ROOM_FAIL = "onGetUserRoomFail";
    public static final String METHOD_ON_USER_ROOM_IN = "onUserRoomIn";
    public static final String METHOD_ON_USER_ROOM_OUT = "onUserRoomOut";

    public static final String METHOD_ON_SHOW_HEAD_WEAR = "showHeadWear";
    public static final String METHOD_ON_SHOW_CAR_ANIM = "showCarAnim";

    String METHOD_ROOM_GET_TAG = "onRoomGetTag";
    String METHOD_ROOM_GET_TAG_ERROR = "onRoomGetTagFail";

    void onOpenRoom(RoomInfo roomInfo);
    void onOpenRoomFail(String error);
    void onAlreadyOpenedRoom();

    void onEnter(RoomInfo roomInfo);
    void onEnterFail(int code, String error);

    void onQuitRoom(RoomInfo roomInfo);

    void onBeKickOut(ChatRoomKickOutEvent.ChatRoomKickOutReason reason);

    void onUpdateRoomInfo(RoomInfo roomInfo);
    void onUpdateRoomInfoFail(String error);

    void onCurrentRoomInfoUpdate(RoomInfo roomInfo);

    void onGetRoomInfo(RoomInfo roomInfo);
    void onGetRoomInfoFail(String error);

    void onRequestCurrentRoomInfo(RoomInfo roomInfo);
    void onRequestCurrentRoomInfoFail(String error);

    void onCloseRoomInfo();
    void onCloseRoomInfoFail(String error);

    void onGetRoomConsumeList(List<RoomConsumeInfo> roomConsumeInfos);
    void onGetRoomConsumeListFail(String msg);

    void onSearchRoom(List<HomeRoom> homeRooms);
    void onSearchRoomFail(String msg);

    void onCurrentRoomRecieveNewMsg(ChatRoomMessage chatRoomMessage, boolean needClear);

    void onGetUserRoom(RoomInfo roomInfo);
    void onGetUserRoomFail(String msg);

    void onUserRoomIn();
    void onUserRoomOut();

    void showHeadWear(String headWear);
    void showCarAnim(String carUrl);
    /**
     * 首页热门分类
     * @param tabs
     */
    void onRoomGetTag(List<TabInfo> tabs);
    void onRoomGetTagFail(String msg);

}
