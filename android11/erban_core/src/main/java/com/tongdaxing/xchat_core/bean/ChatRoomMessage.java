package com.tongdaxing.xchat_core.bean;

import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;

/**
 * 新的消息实体
 */
public class ChatRoomMessage{
    private String room_id;
    private String content;
    private String route;//路由协议替换msgType
    private IMChatRoomMember imChatRoomMember;
    private IMCustomAttachment attachment;

    public IMCustomAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(IMCustomAttachment attachment) {
        this.attachment = attachment;
    }

    public ChatRoomMessage() {
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    // 新版IM 所有发送消息的入口都需要处理
    public ChatRoomMessage(String room_id, String content) {
        this.room_id = room_id;
        this.content = content;
    }

    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public IMChatRoomMember getImChatRoomMember() {
        return imChatRoomMember;
    }

    public void setImChatRoomMember(IMChatRoomMember imChatRoomMember) {
        this.imChatRoomMember = imChatRoomMember;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    @Override
    public String toString() {
        return "ChatRoomMessage{" +
                "room_id='" + room_id + '\'' +
                ", content='" + content + '\'' +
                ", route='" + route + '\'' +
                ", imChatRoomMember=" + imChatRoomMember +
                ", attachment=" + attachment +
                '}';
    }

    //
//    public int getMsgType() {
//        return msgType;
//    }
//
//    public void setMsgType(int msgType) {
//        this.msgType = msgType;
//    }


//    public void setJson(Json json) {
//        this.json = json;
//    }
//
//    public Json getJson() {
//        return json;
//    }

//    public String getImMsgType() {
//        return json == null ? "" : json.str(IMKey.route);
//    }

}
