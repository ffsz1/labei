package com.erban.main.model;

import java.util.Date;

public class StatSumList implements Comparable<StatSumList> {
    private Long id;

    private Long roomUid;

    private Long price;

    private Long uid;

    private Long prodId;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public int compareTo(StatSumList sumList) {
        long priceVo = sumList.price;
        long priceThis = this.price;
        if (priceVo > priceThis) {
            return 1;
        } else if (priceVo < priceThis) {
            return -1;
        } else {
            return 0;
        }
    }
}
