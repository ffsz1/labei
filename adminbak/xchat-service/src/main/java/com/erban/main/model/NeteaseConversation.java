package com.erban.main.model;

public class NeteaseConversation {
    private Integer id;

    private String convType;

    private String toStr;

    private String fromAccount;

    private String fromClientType;

    private String fromDeviceId;

    private String fromNick;

    private String msgTimestamp;

    private String msgType;

    private String body;

    private String attach;

    private String msgidClient;

    private String msgidServer;

    private String resendFlag;

    private String customSafeFlag;

    private String customApnsText;

    private String tMembers;

    private String ext;

    private String antispam;

    private String yidunRes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConvType() {
        return convType;
    }

    public void setConvType(String convType) {
        this.convType = convType == null ? null : convType.trim();
    }

    public String getToStr() {
        return toStr;
    }

    public void setToStr(String toStr) {
        this.toStr = toStr == null ? null : toStr.trim();
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount == null ? null : fromAccount.trim();
    }

    public String getFromClientType() {
        return fromClientType;
    }

    public void setFromClientType(String fromClientType) {
        this.fromClientType = fromClientType == null ? null : fromClientType.trim();
    }

    public String getFromDeviceId() {
        return fromDeviceId;
    }

    public void setFromDeviceId(String fromDeviceId) {
        this.fromDeviceId = fromDeviceId == null ? null : fromDeviceId.trim();
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach == null ? null : attach.trim();
    }

    public String getMsgidClient() {
        return msgidClient;
    }

    public void setMsgidClient(String msgidClient) {
        this.msgidClient = msgidClient == null ? null : msgidClient.trim();
    }

    public String getMsgidServer() {
        return msgidServer;
    }

    public void setMsgidServer(String msgidServer) {
        this.msgidServer = msgidServer == null ? null : msgidServer.trim();
    }

    public String getResendFlag() {
        return resendFlag;
    }

    public void setResendFlag(String resendFlag) {
        this.resendFlag = resendFlag == null ? null : resendFlag.trim();
    }

    public String getCustomSafeFlag() {
        return customSafeFlag;
    }

    public void setCustomSafeFlag(String customSafeFlag) {
        this.customSafeFlag = customSafeFlag == null ? null : customSafeFlag.trim();
    }

    public String getCustomApnsText() {
        return customApnsText;
    }

    public void setCustomApnsText(String customApnsText) {
        this.customApnsText = customApnsText == null ? null : customApnsText.trim();
    }

    public String gettMembers() {
        return tMembers;
    }

    public void settMembers(String tMembers) {
        this.tMembers = tMembers == null ? null : tMembers.trim();
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext == null ? null : ext.trim();
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
