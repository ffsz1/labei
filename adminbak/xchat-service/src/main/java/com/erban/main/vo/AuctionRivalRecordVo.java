package com.erban.main.vo;

/**
 * Created by liuguofu on 2017/5/27.
 */
public class AuctionRivalRecordVo {
    private String rivalId;

    private String auctId;

    private Long uid;

    private UserVo userVo;

    private Long auctMoney;

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public String getRivalId() {
        return rivalId;
    }

    public void setRivalId(String rivalId) {
        this.rivalId = rivalId;
    }

    public String getAuctId() {
        return auctId;
    }

    public void setAuctId(String auctId) {
        this.auctId = auctId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getAuctMoney() {
        return auctMoney;
    }

    public void setAuctMoney(Long auctMoney) {
        this.auctMoney = auctMoney;
    }
}
