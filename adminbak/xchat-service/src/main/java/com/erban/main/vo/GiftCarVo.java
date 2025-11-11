package com.erban.main.vo;

public class GiftCarVo {
    private Integer carId;

    private String carName;

    private Long goldPrice;

    private String picUrl;

    private Boolean hasGifPic;

    private String gifUrl;

    private Boolean hasVggPic;

    private String vggUrl;

    private Boolean isTimeLimit;

    private Long effectiveTime;

    private Integer isPurse;

    private Integer daysRemaining;

    private Integer days;

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
        this.carName = carName;
    }

    public Long getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(Long goldPrice) {
        this.goldPrice = goldPrice;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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
        this.gifUrl = gifUrl;
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
        this.vggUrl = vggUrl;
    }

    public Boolean getTimeLimit() {
        return isTimeLimit;
    }

    public void setTimeLimit(Boolean timeLimit) {
        isTimeLimit = timeLimit;
    }

    public Long getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Long effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Integer getIsPurse() {
        return isPurse;
    }

    public void setIsPurse(Integer isPurse) {
        this.isPurse = isPurse;
    }

    public Integer getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(Integer daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
