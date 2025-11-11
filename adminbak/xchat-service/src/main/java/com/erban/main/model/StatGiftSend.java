package com.erban.main.model;

public class StatGiftSend {
    private Long giftSendId;

    private Integer giftId;

    private Integer sendCount;

    private java.sql.Date statDate;

    public Long getGiftSendId() {
        return giftSendId;
    }

    public void setGiftSendId(Long giftSendId) {
        this.giftSendId = giftSendId;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public  java.sql.Date getStatDate() {
        return statDate;
    }

    public void setStatDate( java.sql.Date statDate) {
        this.statDate = statDate;
    }
}
