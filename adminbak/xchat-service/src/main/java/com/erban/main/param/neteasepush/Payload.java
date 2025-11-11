package com.erban.main.param.neteasepush;

/**
 * Created by liuguofu on 2017/7/13.
 */
public class Payload {
    private Object data;

    private int skiptype;//1跳app页面，2跳聊天室，3跳h5页面
    public int getSkiptype() {
        return skiptype;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setSkiptype(int skiptype) {
        this.skiptype = skiptype;
    }

}
