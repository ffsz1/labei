package com.xchat.common.netease.neteaseacc.result;

import java.util.Map;

/**
 * Created by liuguofu on 2017/5/21.
 */
public class CloseRoomRet{
    /**
     *{
     "desc": {
     "roomid": 13,
     "valid": true,
     "announcement": "这是聊天室",
     "name": "myChatroom",
     "broadcasturl": "http://www.xxxx.com/xxxxxx",
     "ext": "",
     "creator": "zhangsan"
     },
     "code": 200
     }
     */
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
