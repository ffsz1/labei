package com.erban.main.model;

import java.util.Date;

public class UserDrawRecord {
    private Integer recordId;

    private Long uid;

    private Byte drawStatus;

    private Byte type;

    private String srcObjId;

    private String srcObjName;

    private Long srcObjAmount;

    private Integer drawPrizeId;

    private String drawPrizeName;

    private Byte drawPrizePutout;

    private String recordDesc;

    private Date createTime;

    private Date updateTime;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Byte getDrawStatus() {
        return drawStatus;
    }

    public void setDrawStatus(Byte drawStatus) {
        this.drawStatus = drawStatus;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getSrcObjId() {
        return srcObjId;
    }

    public void setSrcObjId(String srcObjId) {
        this.srcObjId = srcObjId == null ? null : srcObjId.trim();
    }

    public String getSrcObjName() {
        return srcObjName;
    }

    public void setSrcObjName(String srcObjName) {
        this.srcObjName = srcObjName == null ? null : srcObjName.trim();
    }

    public Long getSrcObjAmount() {
        return srcObjAmount;
    }

    public void setSrcObjAmount(Long srcObjAmount) {
        this.srcObjAmount = srcObjAmount;
    }

    public Integer getDrawPrizeId() {
        return drawPrizeId;
    }

    public void setDrawPrizeId(Integer drawPrizeId) {
        this.drawPrizeId = drawPrizeId;
    }

    public String getDrawPrizeName() {
        return drawPrizeName;
    }

    public void setDrawPrizeName(String drawPrizeName) {
        this.drawPrizeName = drawPrizeName == null ? null : drawPrizeName.trim();
    }

    public Byte getDrawPrizePutout() {
        return drawPrizePutout;
    }

    public void setDrawPrizePutout(Byte drawPrizePutout) {
        this.drawPrizePutout = drawPrizePutout;
    }

    public String getRecordDesc() {
        return recordDesc;
    }

    public void setRecordDesc(String recordDesc) {
        this.recordDesc = recordDesc == null ? null : recordDesc.trim();
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
}
