package com.xchat.common.netease.neteaseacc.result;


import java.util.Map;

/**
 * Created by liuguofu on 2017/5/9.
 */
public class TokenRet extends BaseNetEaseRet{
    private Map<String,Object> info;

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }
}
