package com.erban.admin.main.vo;

import com.erban.main.model.Users;

import java.util.Date;

public class TreasureBoxVo extends Users {
    private Long erbanNo;
    private String nick;
    private Integer inputNum;
    private Integer outputNum;
    private Integer fullNum = 0;
    private Long totalInputNum;
    private Date createTime;
    private Long totalOutputNum;
    private String averageRate;
    private Integer userNum;
    private String rate;

    private Integer num;
    private Long uid;

    @Override
    public Long getErbanNo() {
        return erbanNo;
    }

    @Override
    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    @Override
    public String getNick() {
        return nick;
    }

    @Override
    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getInputNum() {
        return inputNum;
    }

    public void setInputNum(Integer inputNum) {
        this.inputNum = inputNum;
    }

    public Integer getOutputNum() {
        return outputNum;
    }

    public void setOutputNum(Integer outputNum) {
        this.outputNum = outputNum;
    }

    public Integer getFullNum() {
        return fullNum;
    }

    public void setFullNum(Integer fullNum) {
        this.fullNum = fullNum;
    }

    public Long getTotalInputNum() {
        return totalInputNum;
    }

    public void setTotalInputNum(Long totalInputNum) {
        this.totalInputNum = totalInputNum;
    }

    public Long getTotalOutputNum() {
        return totalOutputNum;
    }

    public void setTotalOutputNum(Long totalOutputNum) {
        this.totalOutputNum = totalOutputNum;
    }

    public String getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(String averageRate) {
        this.averageRate = averageRate;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public Long getUid() {
        return uid;
    }

    @Override
    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
