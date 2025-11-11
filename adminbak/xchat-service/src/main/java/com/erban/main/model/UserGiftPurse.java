package com.erban.main.model;

import java.util.Date;

public class UserGiftPurse {
    private Integer giftPurseId;

    private Long uid;

    private Integer giftId;

    private Integer countNum;

    private Date createTime;

    public Integer getGiftPurseId() {
        return giftPurseId;
    }

    public void setGiftPurseId(Integer giftPurseId) {
        this.giftPurseId = giftPurseId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
