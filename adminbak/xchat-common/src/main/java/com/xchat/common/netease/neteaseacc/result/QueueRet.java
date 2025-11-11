package com.xchat.common.netease.neteaseacc.result;


import com.xchat.common.netease.neteaseacc.model.QueueItem;

import java.util.List;
import java.util.Map;

public class QueueRet {

    private int code;
    private Map<String, List<Map<String,String>>> desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, List<Map<String, String>>> getDesc() {
        return desc;
    }

    public void setDesc(Map<String, List<Map<String, String>>> desc) {
        this.desc = desc;
    }
}
