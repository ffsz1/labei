package com.juxiao.xchat.manager.external.netease.ret;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 用于封装云信开通房间的结果
 */
@Getter
@Setter
public class RoomResult {
    private String desc;
    private int code;
    private Map<String, Object> chatroom;

    public RoomResult(){
    }

    /**
     * 除200是成功状态,其他的都是异常状态
     * @param code
     */
    public RoomResult(int code) {
        this();
        this.code = code;
    }

    public RoomResult(int code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
