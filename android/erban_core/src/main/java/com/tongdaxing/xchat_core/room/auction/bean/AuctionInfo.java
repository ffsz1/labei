package com.tongdaxing.xchat_core.room.auction.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/5/28.
 */

public class AuctionInfo implements Serializable {

//     "auctId":"f3c1cf4da8f048e6865429217756271e",
//             "uid":900170,
//             "auctUid":900177,
//             "auctMoney":4,
//             "servDura":30,
//             "minRaiseMoney":10,
//             "curMaxMoney":4,
//             "auctDesc":"暂无竞拍描述",
//             "curStatus":1,
//             "createTime":"Jun 21, 2017 9:52:57 PM"


    private String auctId;

    private long uid;

    private long auctUid;

    private int auctMoney;

    private int servDura;

    private int minRaiseMoney;

    private long curMaxUid;

    private int curMaxMoney;

    private String auctDesc;

    private long createTime;

    private int curStatus;

    private List<AuctionUser> rivals;

    public long getCurMaxUid() {
        return curMaxUid;
    }

    public void setCurMaxUid(long curMaxUid) {
        this.curMaxUid = curMaxUid;
    }

    public int getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(int curStatus) {
        this.curStatus = curStatus;
    }

    public List<AuctionUser> getRivals() {
        return rivals;
    }

    public void setRivals(List<AuctionUser> rivals) {
        this.rivals = rivals;
    }

    public String getAuctId() {
        return auctId;
    }

    public void setAuctId(String auctId) {
        this.auctId = auctId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getAuctUid() {
        return auctUid;
    }

    public void setAuctUid(long auctUid) {
        this.auctUid = auctUid;
    }

    public int getAuctMoney() {
        return auctMoney;
    }

    public void setAuctMoney(int auctMoney) {
        this.auctMoney = auctMoney;
    }

    public int getServDura() {
        return servDura;
    }

    public void setServDura(int servDura) {
        this.servDura = servDura;
    }

    public int getMinRaiseMoney() {
        return minRaiseMoney;
    }

    public void setMinRaiseMoney(int minRaiseMoney) {
        this.minRaiseMoney = minRaiseMoney;
    }

    public int getCurMaxMoney() {
        return curMaxMoney;
    }

    public void setCurMaxMoney(int curMaxMoney) {
        this.curMaxMoney = curMaxMoney;
    }

    public String getAuctDesc() {
        return auctDesc;
    }

    public void setAuctDesc(String auctDesc) {
        this.auctDesc = auctDesc;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
