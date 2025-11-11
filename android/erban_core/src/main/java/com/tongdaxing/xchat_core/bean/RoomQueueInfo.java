package com.tongdaxing.xchat_core.bean;


/**
 * <p>  房间麦序单个坑位信息实体，包含麦序状态，成员信息</p>
 *
 * @author jiahui
 * @date 2017/12/13
 */
public class RoomQueueInfo {
    /**
     * 坑位信息（是否所坑，开麦等）
     */
    public RoomMicInfo mRoomMicInfo;
    /**
     * 坑上人员信息
     */
    public IMChatRoomMember mChatRoomMember;

    public RoomQueueInfo(RoomMicInfo roomMicInfo, IMChatRoomMember chatRoomMember) {
        mRoomMicInfo = roomMicInfo;
        mChatRoomMember = chatRoomMember;
    }

    @Override
    public String toString() {
        return "RoomQueueInfo{" +
                "mRoomMicInfo=" + mRoomMicInfo +
                ", mChatRoomMember=" + mChatRoomMember +
                '}';
    }
}
