package com.erban.main.vo;

import java.util.Date;

public class ExpendRecordVo implements Comparable<ExpendRecordVo> {
    private String userAvatar;

    private String userNick;

    private Long money;

    private String targetAvatar;

    private String giftName;

    private String targetNick;

    private String giftPic;

    private Integer giftNum;

    private Date recordTime;

    private Long goldNum;

    private Byte expendType;

    private Double diamoundNum;

    public void setDiamoundNum(Double diamoundNum) { this.diamoundNum = diamoundNum; }

    public Double getDiamoundNum() { return diamoundNum; }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getMoney() { return money; }

    public void setGiftPic(String giftPic) {
        this.giftPic = giftPic;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftName() {
        return giftName;
    }

    public String getGiftPic() { return giftPic; }

    public Integer getGiftNum() {
        return giftNum;
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

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public void setGoldNum(Long goldNum) {
        this.goldNum = goldNum;
    }

    public void setExpendType(Byte expendType) {
        this.expendType = expendType;
    }

    public Byte getExpendType() { return expendType; }

    public String getUserAvatar() { return userAvatar; }

    public String getUserNick() {
        return userNick;
    }

    public String getTargetAvatar() {
        return targetAvatar;
    }

    public String getTargetNick() {
        return targetNick;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public Long getGoldNum() {
        return goldNum;
    }

    @Override
    public int compareTo(ExpendRecordVo expendRecordVo) {
        Date recordTimeVo = expendRecordVo.recordTime;
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
