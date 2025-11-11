package com.erban.main.model;

import java.util.Date;

public class GiftCar {
    private Integer carId;

    private String carName;

    private Long goldPrice;

    private Integer seqNo;

    private Byte carStatus;

    private String picUrl;

    private Boolean hasGifPic;

    private String gifUrl;

    private Boolean hasVggPic;

    private String vggUrl;

    private Boolean isTimeLimit;

    private Long effectiveTime;

    private Date createTime;

    private Boolean allowPurse;

    private Integer leftLevel;

    private String markPic;

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName == null ? null : carName.trim();
    }

    public Long getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(Long goldPrice) {
        this.goldPrice = goldPrice;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Byte getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Byte carStatus) {
        this.carStatus = carStatus;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Boolean getHasGifPic() {
        return hasGifPic;
    }

    public void setHasGifPic(Boolean hasGifPic) {
        this.hasGifPic = hasGifPic;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl == null ? null : gifUrl.trim();
    }

    public Boolean getHasVggPic() {
        return hasVggPic;
    }

    public void setHasVggPic(Boolean hasVggPic) {
        this.hasVggPic = hasVggPic;
    }

    public String getVggUrl() {
        return vggUrl;
    }

    public void setVggUrl(String vggUrl) {
        this.vggUrl = vggUrl == null ? null : vggUrl.trim();
    }

    public Boolean getIsTimeLimit() {
        return isTimeLimit;
    }

    public void setIsTimeLimit(Boolean isTimeLimit) {
        this.isTimeLimit = isTimeLimit;
    }

    public Long getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Long effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getAllowPurse() {
        return allowPurse;
    }

    public void setAllowPurse(Boolean allowPurse) {
        this.allowPurse = allowPurse;
    }

    public Integer getLeftLevel() {
        return leftLevel;
    }

    public void setLeftLevel(Integer leftLevel) {
        this.leftLevel = leftLevel;
    }

    public String getMarkPic() {
        return markPic;
    }

    public void setMarkPic(String markPic) {
        this.markPic = markPic == null ? null : markPic.trim();
    }
}
