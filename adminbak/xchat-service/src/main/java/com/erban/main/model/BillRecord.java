package com.erban.main.model;

import java.util.Date;

public class BillRecord  implements Comparable<BillRecord> {
    private String billId;

    private Long uid;

    private Long targetUid;

    private Long roomUid;

    private Byte billStatus;

    private String objId;

    private Byte objType;

    private Integer giftId;

    private Integer giftNum;

    private Double diamondNum;

    private Long goldNum;

    private Long money;

    private Date createTime;

    private Date updateTime;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId == null ? null : billId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Byte getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId == null ? null : objId.trim();
    }

    public Byte getObjType() {
        return objType;
    }

    public void setObjType(Byte objType) {
        this.objType = objType;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public Double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(Double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public Long getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(Long goldNum) {
        this.goldNum = goldNum;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int compareTo(BillRecord billRecord) {
        Date createTimeVo = billRecord.createTime;
        Date createTimeThis = this.createTime;
        if (createTimeThis.getTime() > createTimeVo.getTime()) {
            return -1;
        } else if (createTimeThis.getTime() < createTimeVo.getTime()) {
            return 1;
        } else {
            return 0;
        }
    }
}
