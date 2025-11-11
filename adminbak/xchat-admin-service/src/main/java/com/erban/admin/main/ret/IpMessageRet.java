package com.erban.admin.main.ret;

import java.util.Map;

public class IpMessageRet {

    private int ret;
    private DataMessage  data;
    private String msg;
    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataMessage  getData() {
        return data;
    }

    public void setData(DataMessage  data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
