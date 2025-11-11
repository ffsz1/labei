package com.juxiao.xchat.manager.external.netease.ret;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NeteaseReqAddrRet {
    private int code;
    private List<String> addr;
}
