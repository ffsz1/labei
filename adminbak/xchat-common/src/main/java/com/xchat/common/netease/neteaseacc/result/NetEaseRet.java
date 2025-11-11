package com.xchat.common.netease.neteaseacc.result;

import java.util.Map;

/**
 * Created by liuguofu on 2017/4/29.
 */
public class NetEaseRet {
    //{"code":200,"info":{"token":"c7f302b637099dc3039761ff3b45a21a","accid":"helloworld2","name":""}}
//        {"desc":"already register","code":414}
    private String desc;
    private int code;
    private Map<String,Object> info;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }
}
