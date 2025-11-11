package com.erban.admin.main.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class TreasureBoxReportDTO {
    @Excel(name = "拉贝号", orderNum = "0")
    private Long erBanNo;

    @Excel(name = "昵称", orderNum = "1")
    private String nick;

    @Excel(name = "日期", exportFormat = "yyyy-MM-dd", orderNum = "5")
    private Date time;

    @Excel(name = "投入金币", orderNum = "2")
    private Integer inputGold;

    @Excel(name = "产出金币", orderNum = "3")
    private Integer outPutGold;

    @Excel(name = "投入产出比", orderNum = "4")
    private String rate;

    public Long getErBanNo() {
        return erBanNo;
    }

    public void setErBanNo(Long erBanNo) {
        this.erBanNo = erBanNo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getInputGold() {
        return inputGold;
    }

    public void setInputGold(Integer inputGold) {
        this.inputGold = inputGold;
    }

    public Integer getOutPutGold() {
        return outPutGold;
    }

    public void setOutPutGold(Integer outPutGold) {
        this.outPutGold = outPutGold;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
