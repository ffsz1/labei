package com.erban.main.service.duty.dto;

/**
 * 用户任务
 *
 * @class: UserDutyDTO.java
 * @author: chenjunsheng
 * @date 2018/8/9
 */
public class UserDutyDTO {
    private Integer dutyId;

    private String dutyName;

    private Integer goldAmount;

    private Byte udStatus;

    private Byte dutyType;

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

    public Byte getDutyType() {
        return dutyType;
    }

    public void setDutyType(Byte dutyType) {
        this.dutyType = dutyType;
    }
}
