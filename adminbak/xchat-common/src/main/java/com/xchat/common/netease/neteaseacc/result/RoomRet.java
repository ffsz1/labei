package com.xchat.common.netease.neteaseacc.result;

import java.util.Map;

/**
 * Created by liuguofu on 2017/5/21.
 */
public class RoomRet extends BaseNetEaseRet {
    /**
     *  "chatroom": {
     "roomid": 66,
     "valid": true,
     "announcement": null,
     "name": "mychatroom",
     "broadcasturl": "xxxxxx",
     "ext": "",
     "creator": "zhangsan"
     */
    private Map<String,Object> chatroom;

    public Map<String, Object> getChatroom() {
        return chatroom;
    }

    public void setChatroom(Map<String, Object> chatroom) {
        this.chatroom = chatroom;
    }
}
