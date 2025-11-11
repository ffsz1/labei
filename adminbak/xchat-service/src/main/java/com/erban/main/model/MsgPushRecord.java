package com.erban.main.model;

import java.util.Date;

public class MsgPushRecord {
    private Integer recordId;

    private Long fromAccid;

    private String fromAccountNick;

    private Byte toObjType;

    private Byte msgType;

    private String toAccids;

    private String toAccountNick;

    private String toErbanNos;

    private String title;

    private String webUrl;

    private String picUrl;

    private Byte skipType;

    private String skipUri;

    private String msgDesc;

    private String adminId;

    private Date crateTime;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Long getFromAccid() {
        return fromAccid;
    }

    public void setFromAccid(Long fromAccid) {
        this.fromAccid = fromAccid;
    }

    public String getFromAccountNick() {
        return fromAccountNick;
    }

    public void setFromAccountNick(String fromAccountNick) {
        this.fromAccountNick = fromAccountNick;
    }

    public Byte getToObjType() {
        return toObjType;
    }

    public void setToObjType(Byte toObjType) {
        this.toObjType = toObjType;
    }

    public Byte getMsgType() {
        return msgType;
    }

    public void setMsgType(Byte msgType) {
        this.msgType = msgType;
    }

    public String getToAccids() {
        return toAccids;
    }

    public void setToAccids(String toAccids) {
        this.toAccids = toAccids;
    }

    public String getToAccountNick() {
        return toAccountNick;
    }

    public void setToAccountNick(String toAccountNick) {
        this.toAccountNick = toAccountNick;
    }

    public String getToErbanNos() {
        return toErbanNos;
    }

    public void setToErbanNos(String toErbanNos) {
        this.toErbanNos = toErbanNos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Byte getSkipType() {
        return skipType;
    }

    public void setSkipType(Byte skipType) {
        this.skipType = skipType;
    }

    public String getSkipUri() {
        return skipUri;
    }

    public void setSkipUri(String skipUri) {
        this.skipUri = skipUri;
    }

    public String getMsgDesc() {
        return msgDesc;
    }

    public void setMsgDesc(String msgDesc) {
        this.msgDesc = msgDesc;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public Date getCrateTime() {
        return crateTime;
    }

    public void setCrateTime(Date crateTime) {
        this.crateTime = crateTime;
    }

    @Override
    public String toString() {
        return "MsgPushRecord{" +
                "recordId=" + recordId +
                ", fromAccid=" + fromAccid +
                ", fromAccountNick='" + fromAccountNick + '\'' +
                ", toObjType=" + toObjType +
                ", msgType=" + msgType +
                ", toAccids='" + toAccids + '\'' +
                ", toAccountNick='" + toAccountNick + '\'' +
                ", toErbanNos='" + toErbanNos + '\'' +
                ", title='" + title + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", skipType=" + skipType +
                ", skipUri='" + skipUri + '\'' +
                ", msgDesc='" + msgDesc + '\'' +
                ", adminId='" + adminId + '\'' +
                ", crateTime=" + crateTime +
                '}';
    }
}
