package com.erban.main.model;

public class NeteaseChatroom {
    private Integer id;

    private String attach;

    private String ext;

    private String fromAccount;

    private String fromAvator;

    private String fromClientType;

    private String fromExt;

    private String fromNick;

    private String msgTimestamp;

    private String msgType;

    private String msgidClient;

    private String resendFlag;

    private String roleInfoTimetag;

    private String roomId;

    private String antispam;

    private String yidunRes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach == null ? null : attach.trim();
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext == null ? null : ext.trim();
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount == null ? null : fromAccount.trim();
    }

    public String getFromAvator() {
        return fromAvator;
    }

    public void setFromAvator(String fromAvator) {
        this.fromAvator = fromAvator == null ? null : fromAvator.trim();
    }

    public String getFromClientType() {
        return fromClientType;
    }

    public void setFromClientType(String fromClientType) {
        this.fromClientType = fromClientType == null ? null : fromClientType.trim();
    }

    public String getFromExt() {
        return fromExt;
    }

    public void setFromExt(String fromExt) {
        this.fromExt = fromExt == null ? null : fromExt.trim();
    }

    public String getFromNick() {
        return fromNick;
    }

    public void setFromNick(String fromNick) {
        this.fromNick = fromNick == null ? null : fromNick.trim();
    }

    public String getMsgTimestamp() {
        return msgTimestamp;
    }

    public void setMsgTimestamp(String msgTimestamp) {
        this.msgTimestamp = msgTimestamp == null ? null : msgTimestamp.trim();
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType == null ? null : msgType.trim();
    }

    public String getMsgidClient() {
        return msgidClient;
    }

    public void setMsgidClient(String msgidClient) {
        this.msgidClient = msgidClient == null ? null : msgidClient.trim();
    }

    public String getResendFlag() {
        return resendFlag;
    }

    public void setResendFlag(String resendFlag) {
        this.resendFlag = resendFlag == null ? null : resendFlag.trim();
    }

    public String getRoleInfoTimetag() {
        return roleInfoTimetag;
    }

    public void setRoleInfoTimetag(String roleInfoTimetag) {
        this.roleInfoTimetag = roleInfoTimetag == null ? null : roleInfoTimetag.trim();
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }

    public String getAntispam() {
        return antispam;
    }

    public void setAntispam(String antispam) {
        this.antispam = antispam == null ? null : antispam.trim();
    }

    public String getYidunRes() {
        return yidunRes;
    }

    public void setYidunRes(String yidunRes) {
        this.yidunRes = yidunRes == null ? null : yidunRes.trim();
    }
}
