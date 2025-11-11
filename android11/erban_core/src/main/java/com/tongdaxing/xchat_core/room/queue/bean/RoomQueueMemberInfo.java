package com.tongdaxing.xchat_core.room.queue.bean;

import com.tongdaxing.xchat_core.bean.IMChatRoomMember;

/**
 * 坑上单个队列信息
 * Created by chenran on 2017/9/8.
 */

public class RoomQueueMemberInfo {
    private RoomQueueInfo roomQueueInfo;
    public IMChatRoomMember chatRoomMember;

    public RoomQueueMemberInfo(RoomQueueInfo roomQueueInfo, IMChatRoomMember chatRoomMember) {
        this.roomQueueInfo = roomQueueInfo;
        this.chatRoomMember = chatRoomMember;
    }

    public RoomQueueInfo getRoomQueueInfo() {
        return roomQueueInfo;
    }

    public void setRoomQueueInfo(RoomQueueInfo roomQueueInfo) {
        this.roomQueueInfo = roomQueueInfo;
    }

    public IMChatRoomMember getChatRoomMember() {
        return chatRoomMember;
    }

    public void setChatRoomMember(IMChatRoomMember chatRoomMember) {
        this.chatRoomMember = chatRoomMember;
    }
}
