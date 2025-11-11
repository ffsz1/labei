package com.tongdaxing.xchat_core.manager;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.im.custom.bean.RoomCharmAttachment;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelInfo;

import java.util.List;

/**
 * 定义房间的操作
 *
 * @author xiaoyu
 * @date 2017/12/22
 */

public class RoomEvent {
    public static final int NONE = 0x00000000;
    public static final int ENTER_ROOM = 1;
    public static final int KICK_OUT_ROOM = 2;
    public static final int RECEIVE_MSG = 3;
    public static final int KICK_DOWN_MIC = 4;
    public static final int INVITE_UP_MIC = 5;
    public static final int DOWN_MIC = 6;
    /**
     * 坑的状态：1--锁，0--不锁
     */
    public static final int MIC_QUEUE_STATE_CHANGE = 7;
    public static final int ADD_BLACK_LIST = 8;
    public static final int UP_MIC = 9;
    public static final int MIC_IN_LIST_UPDATE = 32;
    public static final int ROOM_INFO_UPDATE = 10;
    public static final int ROOM_MANAGER_ADD = 11;
    public static final int ROOM_MANAGER_REMOVE = 12;
    public static final int SOCKET_IM_RECONNECT_LOGIN_SUCCESS = 0x00000030;
    /**
     * 即构声浪状态
     */
    public static final int SPEAK_ZEGO_STATE_CHANGE = 33;
    public static final int CURRENT_SPEAK_STATE_CHANGE = 30;
    public static final int ZEGO_RESTART_CONNECTION_EVENT = 31;
    public static final int ZEGO_AUDIO_DEVICE_ERROR = 32;
    /**
     * 房间魅力值
     */
    public static final int ROOM_CHARM = 0x00000031;
    public static final int SPEAK_STATE_CHANGE = 13;//声网的声浪
    /**
     * 推拉流失败次数超过10次数据
     */
    public static final int PLAY_OR_PUBLISH_NETWORK_ERROR = 23;
    public static final int FOLLOW = 14;
    public static final int UNFOLLOW = 15;
    public static final int RECHARGE = 16;
    public static final int AUCTION_START = 17;
    public static final int AUCTION_FINISH = 18;
    public static final int AUCTION_UPDATE = 19;
    public static final int ROOM_EXIT = 20;
    public static final int AUCTION_UPDATE_FAIL = 21;
    //房间成员进出
    public static final int ROOM_MEMBER_IN = 0x00000022;
    public static final int ROOM_MEMBER_EXIT = 0x00000023;
    /**
     * 房间断网重连
     */
    public static final int ROOM_CHAT_RECONNECTION = 0x00000024;
    /**
     * 用户被挤下麦
     */
    public static final int DOWN_CROWDED_MIC = 0x00000025;
    /**
     * 网络弱
     */
    public static final int RTC_ENGINE_NETWORK_BAD = 0x00000026;
    public static final int RTC_ENGINE_NETWORK_CLOSE = 0x00000027;
    public static final int METHOD_ON_AUDIO_MIXING_FINISHED = 0x00000028;
    //新的重连消息
    public static final int ROOM_RECONNECT = 0x00000029;
    private int event = NONE;
    private int micPosition = Integer.MIN_VALUE;
    private int posState = -1;
    private ChatRoomKickOutEvent reason;
    private String reason_msg;
    private int reason_no;
    private String account;
    private RoomInfo roomInfo;
    private boolean success;
    private AuctionInfo auctionInfo;
    public RoomQueueInfo roomQueueInfo;
    private int code;
    private ChatRoomMessage mChatRoomMessage;
    private RoomCharmAttachment roomCharmAttachment;
    private IMChatRoomMember chatRoomMember;

    /**
     * 当前用户的麦位
     */
    private int currentMicPosition = Integer.MIN_VALUE;

    private float currentMicStreamLevel = 0;

    private List<Integer> micPositionList;

    //即构的说话队列
    protected List<ZegoSoundLevelInfo> speakQueueMembersPosition;

    public int getEvent() {
        return event;
    }

    public RoomEvent setEvent(int event) {
        this.event = event;
        return this;
    }

    public ChatRoomKickOutEvent getReason() {
        return reason;
    }

    public RoomEvent setReason(ChatRoomKickOutEvent reason) {
        this.reason = reason;
        return this;
    }

    public RoomEvent setRoomQueueInfo(RoomQueueInfo roomQueueInfo) {
        this.roomQueueInfo = roomQueueInfo;
        return this;
    }

    public int getMicPosition() {
        return micPosition;
    }

    public RoomEvent setMicPosition(int micPosition) {
        this.micPosition = micPosition;
        return this;
    }

    public int getPosState() {
        return posState;
    }

    public RoomEvent setPosState(int posState) {
        this.posState = posState;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public RoomEvent setAccount(String account) {
        this.account = account;
        return this;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public RoomEvent setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public RoomEvent setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public AuctionInfo getAuctionInfo() {
        return auctionInfo;
    }

    public RoomEvent setAuctionInfo(AuctionInfo auctionInfo) {
        this.auctionInfo = auctionInfo;
        return this;
    }

    public ChatRoomMessage getChatRoomMessage() {
        return mChatRoomMessage;
    }

    public RoomEvent setChatRoomMessage(ChatRoomMessage chatRoomMessage) {
        mChatRoomMessage = chatRoomMessage;
        return this;
    }

    public IMChatRoomMember getChatRoomMember() {
        return chatRoomMember;
    }

    public RoomEvent setChatRoomMember(IMChatRoomMember chatRoomMember) {
        this.chatRoomMember = chatRoomMember;
        return this;
    }

    public String getReason_msg() {
        return reason_msg;
    }

    public RoomEvent setReason_msg(String reason_msg) {
        this.reason_msg = reason_msg;
        return this;
    }

    public int getReason_no() {
        return reason_no;
    }

    public RoomEvent setReason_no(int reason_no) {
        this.reason_no = reason_no;
        return this;
    }

    public int getCode() {
        return code;
    }

    public RoomEvent setCode(int code) {
        this.code = code;
        return this;
    }

    public List<Integer> getMicPositionList() {
        return micPositionList;
    }

    public RoomEvent setMicPositionList(List<Integer> micPositionList) {
        this.micPositionList = micPositionList;
        return this;
    }

    public int getCurrentMicPosition() {
        return currentMicPosition;
    }

    public RoomEvent setCurrentMicPosition(int currentMicPosition) {
        this.currentMicPosition = currentMicPosition;
        return this;
    }

    public float getCurrentMicStreamLevel() {
        return currentMicStreamLevel;
    }

    public RoomEvent setCurrentMicStreamLevel(float currentMicStreamLevel) {
        this.currentMicStreamLevel = currentMicStreamLevel;
        return this;
    }

    public List<ZegoSoundLevelInfo> getSpeakQueueMembersPosition() {
        return speakQueueMembersPosition;
    }

    public RoomEvent setSpeakQueueMembersPosition(List<ZegoSoundLevelInfo> speakQueueMembersPosition) {
        this.speakQueueMembersPosition = speakQueueMembersPosition;
        return this;
    }

    public RoomCharmAttachment getRoomCharmAttachment() {
        return roomCharmAttachment;
    }

    public RoomEvent setRoomCharmAttachment(RoomCharmAttachment roomCharmAttachment) {
        this.roomCharmAttachment = roomCharmAttachment;
        return this;
    }
}
