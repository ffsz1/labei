package com.erban.main.model;

import java.util.Date;

public class Gift {
    private Integer giftId;

    private String giftName;

    private Long goldPrice;

    private Integer seqNo;

    private Integer nobleId;

    private String nobleName;

    private Boolean isNobleGift;

    private Byte giftType;

    private Byte giftStatus;

    private String picUrl;

    private Boolean hasGifPic;

    private String gifUrl;

    private Boolean hasVggPic;

    private String vggUrl;

    private Boolean isLatest;

    private Boolean isTimeLimit;

    private Boolean hasEffect;

    private Date startValidTime;

    private Date endValidTime;

    private Date createTime;

    private Boolean isExpressGift;

    private Boolean isCanGive;

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName == null ? null : giftName.trim();
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

    public Integer getNobleId() {
        return nobleId;
    }

    public void setNobleId(Integer nobleId) {
        this.nobleId = nobleId;
    }

    public String getNobleName() {
        return nobleName;
    }

    public void setNobleName(String nobleName) {
        this.nobleName = nobleName == null ? null : nobleName.trim();
    }

    public Boolean getIsNobleGift() {
        return isNobleGift;
    }

    public void setIsNobleGift(Boolean isNobleGift) {
        this.isNobleGift = isNobleGift;
    }

    public Byte getGiftType() {
        return giftType;
    }

    public void setGiftType(Byte giftType) {
        this.giftType = giftType;
    }

    public Byte getGiftStatus() {
        return giftStatus;
    }

    public void setGiftStatus(Byte giftStatus) {
        this.giftStatus = giftStatus;
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

    public Boolean getIsLatest() {
        return isLatest;
    }

    public void setIsLatest(Boolean isLatest) {
        this.isLatest = isLatest;
    }

    public Boolean getIsTimeLimit() {
        return isTimeLimit;
    }

    public void setIsTimeLimit(Boolean isTimeLimit) {
        this.isTimeLimit = isTimeLimit;
    }

    public Boolean getHasEffect() {
        return hasEffect;
    }

    public void setHasEffect(Boolean hasEffect) {
        this.hasEffect = hasEffect;
    }

    public Date getStartValidTime() {
        return startValidTime;
    }

    public void setStartValidTime(Date startValidTime) {
        this.startValidTime = startValidTime;
    }

    public Date getEndValidTime() {
        return endValidTime;
    }

    public void setEndValidTime(Date endValidTime) {
        this.endValidTime = endValidTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getIsExpressGift() {
        return isExpressGift;
    }

    public void setIsExpressGift(Boolean isExpressGift) {
        this.isExpressGift = isExpressGift;
    }

    public Boolean getIsCanGive() {
        return isCanGive;
    }

    public void setIsCanGive(Boolean isCanGive) {
        this.isCanGive = isCanGive;
    }
}
