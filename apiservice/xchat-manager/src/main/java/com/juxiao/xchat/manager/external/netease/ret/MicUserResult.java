package com.juxiao.xchat.manager.external.netease.ret;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MicUserResult {

    private Integer code;
    private String message;
    private QueueUser desc;

    public MicUserResult() {
    }

    public MicUserResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
