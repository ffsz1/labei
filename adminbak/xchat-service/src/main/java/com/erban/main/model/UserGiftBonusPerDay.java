package com.erban.main.model;

import java.util.Date;

public class UserGiftBonusPerDay {
    private Integer bonusId;

    private Long uid;

    private Double curDiamondNum;

    private Double forecastDiamondNum;

    private Double bonusDiamondNum;

    private Boolean todayHasFinishBonus;

    private Date statDate;

    private Date bonusFinishDate;

    public Integer getBonusId() {
        return bonusId;
    }

    public void setBonusId(Integer bonusId) {
        this.bonusId = bonusId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Double getCurDiamondNum() {
        return curDiamondNum;
    }

    public void setCurDiamondNum(Double curDiamondNum) {
        this.curDiamondNum = curDiamondNum;
    }

    public Double getForecastDiamondNum() {
        return forecastDiamondNum;
    }

    public void setForecastDiamondNum(Double forecastDiamondNum) {
        this.forecastDiamondNum = forecastDiamondNum;
    }

    public Double getBonusDiamondNum() {
        return bonusDiamondNum;
    }

    public void setBonusDiamondNum(Double bonusDiamondNum) {
        this.bonusDiamondNum = bonusDiamondNum;
    }

    public Boolean getTodayHasFinishBonus() {
        return todayHasFinishBonus;
    }

    public void setTodayHasFinishBonus(Boolean todayHasFinishBonus) {
        this.todayHasFinishBonus = todayHasFinishBonus;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Date getBonusFinishDate() {
        return bonusFinishDate;
    }

    public void setBonusFinishDate(Date bonusFinishDate) {
        this.bonusFinishDate = bonusFinishDate;
    }
}
