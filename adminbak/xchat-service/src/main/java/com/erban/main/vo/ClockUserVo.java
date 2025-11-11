package com.erban.main.vo;

public class ClockUserVo {
    private Long uid;
    private Long userNo;
    private String avatar;
    private String clockDesc;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getClockDesc() {
        return clockDesc;
    }

    public void setClockDesc(String clockDesc) {
        this.clockDesc = clockDesc;
    }
}
