package com.erban.main.vo.admin;

import java.util.Date;

/**
 * 后台管理--钻石提现VO
 */
public class DiamondWithDrawVo {
    //========bill record============
    private String billId;

    private Byte billStatus;

    private Double diamondNum;

    private Long goldNum;

    private Long money;

    private Date createTime;

    private Date updateTime;

    //========users==================
    private Long uid;

    private Long erbanNo;

    private String phone;

    private String nick;

    private String alipayAccount;

    private String alipayAccountName;

    private String gender;

    private Byte withdrawStatus;
    //==================================
    private Integer male;
    private Integer female;
    private Integer other;
    private Integer userNum;
    private Long sumMoney;//提现金额

    private String weixinOpenid;

    private Integer giftId;

    private String applyWithdrawalAccount;
    private String applyWithdrawalName;
    private String realTransferAccount;
    private String realTransferName;


    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Byte getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    public Double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(Double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public Long getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(Long goldNum) {
        this.goldNum = goldNum;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(Byte withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
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

    public Long getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(Long sumMoney) {
        this.sumMoney = sumMoney;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public String getWeixinOpenid() {
        return weixinOpenid;
    }

    public void setWeixinOpenid(String weixinOpenid) {
        this.weixinOpenid = weixinOpenid;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
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
}
