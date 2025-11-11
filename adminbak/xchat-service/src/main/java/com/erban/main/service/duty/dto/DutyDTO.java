package com.erban.main.service.duty.dto;

/**
 * @class: DutyDTO.java
 * @author: chenjunsheng
 * @date 2018/8/9
 */
public class DutyDTO {
    private Integer id;
    private String dutyName;
    private Integer goldAmount;
    private Byte dutyType;
    private Integer dutyStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Byte getDutyType() {
        return dutyType;
    }

    public void setDutyType(Byte dutyType) {
        this.dutyType = dutyType;
    }

    public Integer getDutyStatus() {
        return dutyStatus;
    }

    public void setDutyStatus(Integer dutyStatus) {
        this.dutyStatus = dutyStatus;
    }
}
