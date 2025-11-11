package com.erban.main.model;

import java.util.Date;

public class PacketWithDrawRecord {
    private String recordId;

    private Long uid;

    private Integer packetProdCashId;

    private Double packetNum;

    private Byte recordStatus;

    private Date createTime;

    private String bankCard;

    private String bankCardName;

    private String openBankCode;

    private Byte realTranType;

    private Date updateTime;

    private Byte tranType;

    private String merchantOrderNo;
    private Integer adminId;
    private String pingxxId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId == null ? null : recordId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getPacketProdCashId() {
        return packetProdCashId;
    }

    public void setPacketProdCashId(Integer packetProdCashId) {
        this.packetProdCashId = packetProdCashId;
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

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard == null ? null : bankCard.trim();
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName == null ? null : bankCardName.trim();
    }

    public String getOpenBankCode() {
        return openBankCode;
    }

    public void setOpenBankCode(String openBankCode) {
        this.openBankCode = openBankCode == null ? null : openBankCode.trim();
    }

    public Byte getRealTranType() {
        return realTranType;
    }

    public void setRealTranType(Byte realTranType) {
        this.realTranType = realTranType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
}
