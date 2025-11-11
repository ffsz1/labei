package com.erban.main.model;

import com.erban.main.vo.WeekListVo;

import java.util.Date;

public class StatWeekLists implements Comparable<StatWeekLists> {
    private Long id;

    private Long roomUid;

    private Long dealUid;

    private String avatar;

    private Byte gender;

    private Long price;

    private String nick;

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

    public Long getDealUid() {
        return dealUid;
    }

    public void setDealUid(Long dealUid) {
        this.dealUid = dealUid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
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
    public int compareTo(StatWeekLists statWeekLists) {
        long priceVo = statWeekLists.price;
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
