package com.erban.main.vo;

import java.util.Date;

public class StatPacketInviteRegisterListVo {
    private Long uid;
    private String nick;
    private Date createTime;
    private Double packetNum;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }
}
