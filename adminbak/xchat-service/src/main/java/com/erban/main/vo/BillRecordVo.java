package com.erban.main.vo;

import java.util.Date;

public class BillRecordVo {

    private String billId;

    public void setBillId(String billlId) {
        this.billId = billlId;
    }

    public String getBillId() {

        return billId;
    }

    private long erbanNo;

    private String nick;

    private double diamondNum;

    private long money;

    public BillRecordVo() {
    }

    public void setErbanNo(long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setDiamondNum(double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getErbanNo() {
        return erbanNo;
    }

    public String getNick() {
        return nick;
    }

    public double getDiamondNum() {
        return diamondNum;
    }

    public long getMoney() {
        return money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    private Date createTime;
}
