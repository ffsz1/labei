package com.erban.main.model;

import java.util.Date;

public class WXPush {
    private String wxPushId;

    private String articleCount;

    private Boolean isPush;

    private Date createTime;

    private Date pubTime;

    private String msgId;

    private Boolean isFocusPush;

    public String getWxPushId() {
        return wxPushId;
    }

    public void setWxPushId(String wxPushId) {
        this.wxPushId = wxPushId == null ? null : wxPushId.trim();
    }

    public String getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(String articleCount) {
        this.articleCount = articleCount == null ? null : articleCount.trim();
    }

    public Boolean getIsPush() {
        return isPush;
    }

    public void setIsPush(Boolean isPush) {
        this.isPush = isPush;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPubTime() {
        return pubTime;
    }

    public void setPubTime(Date pubTime) {
        this.pubTime = pubTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId == null ? null : msgId.trim();
    }

    public Boolean getIsFocusPush() {
        return isFocusPush;
    }

    public void setIsFocusPush(Boolean isFocusPush) {
        this.isFocusPush = isFocusPush;
    }
}
