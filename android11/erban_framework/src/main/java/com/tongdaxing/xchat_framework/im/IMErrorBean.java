package com.tongdaxing.xchat_framework.im;

/**
 * 文件描述：IM异常实体
 *
 * @auther：zwk
 * @data：2018/12/27
 */
public class IMErrorBean {
    private int socketId;
    private int code;
    private String reason;
    private boolean remote;
    private int closeReason;

    public int getSocketId() {
        return socketId;
    }

    public void setSocketId(int socketId) {
        this.socketId = socketId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public int getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(int closeReason) {
        this.closeReason = closeReason;
    }
}
