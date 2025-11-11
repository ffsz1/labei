package com.erban.main.vo;

import java.util.Date;
import java.util.List;

/**
 * Created by liuguofu on 2017/5/26.
 */
public class AuctionCurVo {
    private String auctId;

    private Long uid;

    private UserVo userVo;

    private Long auctUid;

    private UserVo auctUserVo;

    private Long auctMoney;

    private Integer servDura;

    private Long minRaiseMoney;

    private Long curMaxUid;

    private UserVo curMaxUserVo;

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public UserVo getCurMaxUserVo() {
        return curMaxUserVo;
    }

    public void setCurMaxUserVo(UserVo curMaxUserVo) {
        this.curMaxUserVo = curMaxUserVo;
    }

    private Long curMaxMoney;

    public UserVo getAuctUserVo() {
        return auctUserVo;
    }

    public void setAuctUserVo(UserVo auctUserVo) {
        this.auctUserVo = auctUserVo;
    }

    private String auctDesc;

    private Byte curStatus;

    private Date createTime;

    private Date dealTime;

    private List<AuctionRivalRecordVo> rivals;

    public List<AuctionRivalRecordVo> getRivals() {
        return rivals;
    }

    public void setRivals(List<AuctionRivalRecordVo> rivals) {
        this.rivals = rivals;
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

    public Long getAuctUid() {
        return auctUid;
    }

    public void setAuctUid(Long auctUid) {
        this.auctUid = auctUid;
    }

    public Long getAuctMoney() {
        return auctMoney;
    }

    public void setAuctMoney(Long auctMoney) {
        this.auctMoney = auctMoney;
    }

    public Integer getServDura() {
        return servDura;
    }

    public void setServDura(Integer servDura) {
        this.servDura = servDura;
    }

    public Long getMinRaiseMoney() {
        return minRaiseMoney;
    }

    public void setMinRaiseMoney(Long minRaiseMoney) {
        this.minRaiseMoney = minRaiseMoney;
    }

    public Long getCurMaxUid() {
        return curMaxUid;
    }

    public void setCurMaxUid(Long curMaxUid) {
        this.curMaxUid = curMaxUid;
    }

    public Long getCurMaxMoney() {
        return curMaxMoney;
    }

    public void setCurMaxMoney(Long curMaxMoney) {
        this.curMaxMoney = curMaxMoney;
    }

    public String getAuctDesc() {
        return auctDesc;
    }

    public void setAuctDesc(String auctDesc) {
        this.auctDesc = auctDesc;
    }

    public Byte getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(Byte curStatus) {
        this.curStatus = curStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }
}
