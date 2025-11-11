package com.erban.admin.main.dto;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/10/15
 * @time 15:30
 */
public class UsersMiningMustDTO {
    private Long id;

    private Long uid;

    private Long erbanNo;

    private String nick;

    private Integer status;

    private Date createTime;

    private Long giftId;

    private String giftName;

    private Long inputGold;

    private Long outputGold;

    private Double rate;

    private Integer adminId;

    private String adminName;

    public Long getGiftId() {
        return giftId;
    }

    public void setGiftId(Long giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getInputGold() {
        return inputGold;
    }

    public void setInputGold(Long inputGold) {
        this.inputGold = inputGold;
    }

    public Long getOutputGold() {
        return outputGold;
    }

    public void setOutputGold(Long outputGold) {
        this.outputGold = outputGold;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "UsersMiningMustDTO{" +
                "id=" + id +
                ", uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", giftId=" + giftId +
                ", giftName='" + giftName + '\'' +
                ", inputGold=" + inputGold +
                ", outputGold=" + outputGold +
                ", rate=" + rate +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                '}';
    }
}
