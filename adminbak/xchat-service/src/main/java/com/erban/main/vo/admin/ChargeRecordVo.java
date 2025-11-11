package com.erban.main.vo.admin;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台管理--充值记录
 */
public class ChargeRecordVo {
    //========charge record===========
    private String chargeRecordId;

    private String pingxxChargeId;

    private String channel;

    private Byte bussType;

    private Byte chargeStatus;

    private Long amount;

    private Long totalGold;

    private String clientIp;

    private String subject;

    private String chargeDesc;

    private Date createTime;

    private Date updateTime;

    //========users==================
    private Long uid;

    private Long erbanNo;

    private String phone;

    private String nick;

    private String os;

    private Byte gender;

    private Integer num;

    //================================
    private Integer userNum;//充值人数
    private BigDecimal totalAmount;//充值总金额
    private Integer male;
    private Integer female;
    private Integer other;//未知性别

    public String getChargeRecordId() {
        return chargeRecordId;
    }

    public void setChargeRecordId(String chargeRecordId) {
        this.chargeRecordId = chargeRecordId;
    }

    public String getPingxxChargeId() {
        return pingxxChargeId;
    }

    public void setPingxxChargeId(String pingxxChargeId) {
        this.pingxxChargeId = pingxxChargeId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Byte getBussType() {
        return bussType;
    }

    public void setBussType(Byte bussType) {
        this.bussType = bussType;
    }

    public Byte getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Byte chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Long totalGold) {
        this.totalGold = totalGold;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getChargeDesc() {
        return chargeDesc;
    }

    public void setChargeDesc(String chargeDesc) {
        this.chargeDesc = chargeDesc;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Integer getMale() {
        return male;
    }

    public void setMale(Integer male) {
        this.male = male;
    }

    public Integer getFemale() {
        return female;
    }

    public void setFemale(Integer female) {
        this.female = female;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }
}
