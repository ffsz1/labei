package com.erban.main.vo.admin;

import java.util.Date;

/**
 * 新的靓号管理Vo
 */
public class UserPrettyNoVo {
    //==============UserDrawPrettyErbanNo============
    private Long prettyErbanNo;

    private Byte type;

    private Byte useStatus;

    private Date createTime;
    //================Users=======================
    private Long uid;

    private Long erbanNo;

    private Boolean hasPrettyErbanNo;

    private String phone;

    private String nick;

    private Byte seq;

    public Byte getSeq() {
        return seq;
    }

    public void setSeq(Byte seq) {
        this.seq = seq;
    }

    public Long getPrettyErbanNo() {
        return prettyErbanNo;
    }

    public void setPrettyErbanNo(Long prettyErbanNo) {
        this.prettyErbanNo = prettyErbanNo;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Byte useStatus) {
        this.useStatus = useStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public Boolean getHasPrettyErbanNo() {
        return hasPrettyErbanNo;
    }

    public void setHasPrettyErbanNo(Boolean hasPrettyErbanNo) {
        this.hasPrettyErbanNo = hasPrettyErbanNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
