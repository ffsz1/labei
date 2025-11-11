package com.xchat.common.netease.neteaseacc.result;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/11/28 12:44
 */
public class InitQueueRet {

    private Object desc;
    private int code;

    public Object getDesc() {
        return desc;
    }

    public void setDesc(Object desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "InitQueueRet{" +
                "desc=" + desc +
                ", code=" + code +
                '}';
    }
}
