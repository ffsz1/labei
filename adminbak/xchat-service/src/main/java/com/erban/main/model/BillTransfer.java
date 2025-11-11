package com.erban.main.model;

import java.util.Date;

public class BillTransfer {
    private Integer id;

    private Integer uid;

    private Byte tranType;

    private Byte realTranType;

    private Integer cost;

    private Integer money;

    private Byte billStatus;

    private Date createTime;

    private Date updateTime;

    private String billId;

    private String bankCard;

    private String bankCardName;

    private String openBankCode;

    private String applyWithdrawalAccount;
    private String applyWithdrawalName;
    private String realTransferAccount;
    private String realTransferName;
    private Integer adminId;
    private String pingxxId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Byte getTranType() {
        return tranType;
    }

    public void setTranType(Byte tranType) {
        this.tranType = tranType;
    }

    public Byte getRealTranType() {
        return realTranType;
    }

    public void setRealTranType(Byte realTranType) {
        this.realTranType = realTranType;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Byte getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
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

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId == null ? null : billId.trim();
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getOpenBankCode() {
        return openBankCode;
    }

    public void setOpenBankCode(String openBankCode) {
        this.openBankCode = openBankCode;
    }

    public String getApplyWithdrawalAccount() {
        return applyWithdrawalAccount;
    }

    public void setApplyWithdrawalAccount(String applyWithdrawalAccount) {
        this.applyWithdrawalAccount = applyWithdrawalAccount;
    }

    public String getApplyWithdrawalName() {
        return applyWithdrawalName;
    }

    public void setApplyWithdrawalName(String applyWithdrawalName) {
        this.applyWithdrawalName = applyWithdrawalName;
    }

    public String getRealTransferAccount() {
        return realTransferAccount;
    }

    public void setRealTransferAccount(String realTransferAccount) {
        this.realTransferAccount = realTransferAccount;
    }

    public String getRealTransferName() {
        return realTransferName;
    }

    public void setRealTransferName(String realTransferName) {
        this.realTransferName = realTransferName;
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
