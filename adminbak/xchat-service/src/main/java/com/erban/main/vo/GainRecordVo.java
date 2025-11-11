package com.erban.main.vo;

import java.util.Date;

public class GainRecordVo implements Comparable<GainRecordVo> {
    private String userAvatar;

    private String userNick;

    private String targetAvatar;

    private Long money;

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getMoney() {

        return money;
    }

    private String giftName;

    private Long goldNum;

    public void setGoldNum(Long goldNum) {
        this.goldNum = goldNum;
    }

    public Long getGoldNum() {

        return goldNum;
    }

    private String targetNick;

    private Double diamondNum;

    private String giftPic;

    private Integer giftNum;

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftName() {

        return giftName;
    }

    public void setGiftPic(String giftPic) {
        this.giftPic = giftPic;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public String getGiftPic() {

        return giftPic;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    private Date recordTime;

    private Byte gainType;

    public void setGainType(Byte gainType) {
        this.gainType = gainType;
    }

    public Byte getGainType() {

        return gainType;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public void setTargetAvatar(String targetAvatar) {
        this.targetAvatar = targetAvatar;
    }

    public void setTargetNick(String targetNick) {
        this.targetNick = targetNick;
    }

    public void setDiamondNum(Double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getUserAvatar() {

        return userAvatar;
    }

    public String getUserNick() {
        return userNick;
    }

    public String getTargetAvatar() {
        return targetAvatar;
    }

    public String getTargetNick() {
        return targetNick;
    }

    public Double getDiamondNum() {
        return diamondNum;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    @Override
    public int compareTo(GainRecordVo gainRecordVo) {
        Date recordTimeVo = gainRecordVo.recordTime;
        Date recordTimeThis = this.recordTime;
        if (recordTimeThis.getTime() > recordTimeVo.getTime()) {
            return -1;
        } else if (recordTimeThis.getTime() < recordTimeVo.getTime()) {
            return 1;
        } else {
            return 0;
        }
    }
}
