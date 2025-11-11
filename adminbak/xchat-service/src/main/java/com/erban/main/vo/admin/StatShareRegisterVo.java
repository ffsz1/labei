package com.erban.main.vo.admin;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台管理--充值记录
 */
public class StatShareRegisterVo {
    //==========share register================
    private Integer registerNum;
    private Integer lowerRegisterNum;
    private Long uid;
    private Double totalBouns;
    //==============user=================
    private Long erbanNo;
    private String nick;
    private String phone;
    private String alipayAccount;
    private String alipayAccountName;
    private Date createTime;
    //=================================
    private Integer sumSharer;
    private Integer sumRegister;

    public Integer getLowerRegisterNum() {
        return lowerRegisterNum;
    }

    public void setLowerRegisterNum(Integer lowerRegisterNum) {
        this.lowerRegisterNum = lowerRegisterNum;
    }

    public Integer getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(Integer registerNum) {
        this.registerNum = registerNum;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getTotalBouns() {
        return totalBouns;
    }

    public void setTotalBouns(Double totalBouns) {
        this.totalBouns = totalBouns;
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

    public Integer getSumSharer() {
        return sumSharer;
    }

    public void setSumSharer(Integer sumSharer) {
        this.sumSharer = sumSharer;
    }

    public Integer getSumRegister() {
        return sumRegister;
    }

    public void setSumRegister(Integer sumRegister) {
        this.sumRegister = sumRegister;
    }
}
