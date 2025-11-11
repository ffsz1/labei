package com.erban.main.vo;

import java.util.Date;

public class UserGiftWallVo {
    private Long giftWallId;

    private Long uid;

    private Integer giftId;

    private Integer reciveCount;


    public Long getGiftWallId() {
        return giftWallId;
    }

    public void setGiftWallId(Long giftWallId) {
        this.giftWallId = giftWallId;
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

    public Integer getReciveCount() {
        return reciveCount;
    }

    public void setReciveCount(Integer reciveCount) {
        this.reciveCount = reciveCount;
    }

}
