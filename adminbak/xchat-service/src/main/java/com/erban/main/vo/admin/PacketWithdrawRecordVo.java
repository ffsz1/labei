package com.erban.main.vo.admin;

import java.util.Date;

/**
 * 红包提现Vo
 */
public class PacketWithdrawRecordVo {
    //=========PacketWithdrawRecord===========
    private String recordId;

    private Double packetNum;

    private Byte recordStatus;

    private Date createTime;

    //====================================

    private Long uid;

    private Long erbanNo;

    private String phone;

    private String nick;

    private String alipayAccount;

    private String alipayAccountName;

    private String wxOpenId;

    private String cardNumber;

    private String cardName;

    private String openBankCode;

    private Byte withdrawStatus;

    private Byte realTranType;

    private Byte tranType;

    private String merchantOrderNo;

    private Integer userNum;

    private Long money;

    private Integer adminId;
    private String pingxxId;

    private String adminName;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }

    public Byte getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Byte recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getAlipayAccountName() {
        return alipayAccountName;
    }

    public void setAlipayAccountName(String alipayAccountName) {
        this.alipayAccountName = alipayAccountName;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getOpenBankCode() {
        return openBankCode;
    }

    public void setOpenBankCode(String openBankCode) {
        this.openBankCode = openBankCode;
    }

    public Byte getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(Byte withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public Byte getRealTranType() {
        return realTranType;
    }

    public void setRealTranType(Byte realTranType) {
        this.realTranType = realTranType;
    }

    public Byte getTranType() {
        return tranType;
    }

    public void setTranType(Byte tranType) {
        this.tranType = tranType;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getPingxxId() {
        return pingxxId;
    }

    public void setPingxxId(String pingxxId) {
        this.pingxxId = pingxxId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
