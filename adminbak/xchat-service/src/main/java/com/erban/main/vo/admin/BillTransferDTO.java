package com.erban.main.vo.admin;

import java.util.Date;

/**
 * 钻石提现 - news
 */
public class BillTransferDTO {

    private Long id;

    private Long uid;

    private Byte tranType;

    private Byte realTranType;

    private Integer cost;

    private Integer money;

    private Byte billStatus;

    private Date createTime;

    private Date updateTime;

    private String billId;

    private Long erbanNo;

    private String nick;

    private Byte gender;

    private String phone;

    private String wxOpenId;

    private String cardNumber;

    private String cardName;

    private String openBankCode;

    private String alipayAccount;

    private String alipayAccountName;

    private Byte withdrawStatus;


    private Integer male;
    private Integer female;
    private Integer other;
    private Integer userNum;
    private Long sumMoney;//提现金额

    private String applyWithdrawalAccount;
    private String applyWithdrawalName;
    private String realTransferAccount;
    private String realTransferName;
    private Integer adminId;
    private String pingxxId;
    private String adminName;

    public Byte getRealTranType() {
        return realTranType;
    }

    public void setRealTranType(Byte realTranType) {
        this.realTranType = realTranType;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Byte getTranType() {
        return tranType;
    }

    public void setTranType(Byte tranType) {
        this.tranType = tranType;
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
        this.billId = billId;
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

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
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

    public Byte getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(Byte withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
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

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public Long getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(Long sumMoney) {
        this.sumMoney = sumMoney;
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

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
