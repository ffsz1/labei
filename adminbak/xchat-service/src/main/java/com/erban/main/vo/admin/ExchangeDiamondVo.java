package com.erban.main.vo.admin;

import java.util.Date;

/**
 * 红包提现Vo
 */
public class ExchangeDiamondVo {
    private String recordId;

    private Double exDiamondNum;

    private Long exGoldNum;

    private Date createTime;

    //====================================

    private Long uid;

    private Long erbanNo;

    private String phone;

    private String nick;

    private String alipayAccount;

    private String alipayAccountName;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Double getExDiamondNum() {
        return exDiamondNum;
    }

    public void setExDiamondNum(Double exDiamondNum) {
        this.exDiamondNum = exDiamondNum;
    }

    public Long getExGoldNum() {
        return exGoldNum;
    }

    public void setExGoldNum(Long exGoldNum) {
        this.exGoldNum = exGoldNum;
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
}
