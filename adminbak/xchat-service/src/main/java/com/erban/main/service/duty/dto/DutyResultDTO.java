package com.erban.main.service.duty.dto;

public class DutyResultDTO {

    private Integer dutyId;

    private String dutyName;

    private Integer goldAmount;

    private Byte udStatus;

    public Integer getDutyId() {
        return dutyId;
    }

    public void setDutyId(Integer dutyId) {
        this.dutyId = dutyId;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public Integer getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(Integer goldAmount) {
        this.goldAmount = goldAmount;
    }

    public Byte getUdStatus() {
        return udStatus;
    }

    public void setUdStatus(Byte udStatus) {
        this.udStatus = udStatus;
    }
}
