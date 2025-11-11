package com.tongdaxing.xchat_core.room.bean;

public class TaskInfo {
    private int dutyId;
    private String dutyName;
    private int goldAmount;
    private int udStatus;

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public int getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(int goldAmount) {
        this.goldAmount = goldAmount;
    }

    public int getUdStatus() {
        return udStatus;
    }

    public void setUdStatus(int udStatus) {
        this.udStatus = udStatus;
    }
}
