package com.tongdaxing.xchat_core.room.model;


/**
 * <p> IM信息相关 </p>
 *
 * @author jiahui
 * @date 2017/12/20
 */
public class ImMessageModel {

//    /**
//     * 发送文本信息
//     *
//     * @param message
//     * @param callBack
//     */
//    public void sendTextMsg(String message, CallBack<ChatRoomMessage> callBack) {
//        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
//        if (roomInfo == null || TextUtils.isEmpty(message)) return;
//        ChatRoomMessage chatRoomMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(
//                String.valueOf(roomInfo.getRoomId()), message);
//        IMNetEaseManager.get().sendChatRoomMessage(chatRoomMessage, false);
//    }
//
//    /**
//     * 发送表情是否成功
//     *
//     * @return - 成功后返回表情ChatRoomMessage的observable
//     */
//    public Single<ChatRoomMessage> sendFaceMsgSuccessOrNot(ChatRoomMessage chatRoomMessage) {
//        return IMNetEaseManager.get().sendChatRoomMessage(chatRoomMessage, false);
//    }
//
//    /**
//     * 发送礼物是否成功
//     *
//     * @return - 成功后返回礼物ChatRoomMessage的observable
//     */
//    public Single<ChatRoomMessage> sendGiftMsgSuccessOrNot(ChatRoomMessage chatRoomMessage) {
//        return IMNetEaseManager.get().sendChatRoomMessage(chatRoomMessage, false);
//    }
}
