package com.erban.admin.web.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class StatReportBO {
    @Excel(name = "拉贝号")
    private Long erbanNo;
    @Excel(name = "用户昵称")
    private String nick;
    @Excel(name = "手机号码")
    private String phone;
    @Excel(name = "性别")
    private Byte gender;
    @Excel(name = "平台")
    private String os;
    @Excel(name = "充值金额")
    private Long chargeAmount;
    @Excel(name = "兑换金币")
    private Double exechangeDiamond;
    @Excel(name = "财富变化")
    private Long experChange;
    @Excel(name = "魅力变化")
    private Long charmChange;
    @Excel(name = "总财富值")
    private Long experNum;
    @Excel(name = "总魅力值")
    private Long charmNum;
    @Excel(name = "送出普通礼物总值")
    private Long normalGiftNum;
    @Excel(name = "打Call总值")
    private Long doCallNum;
    @Excel(name = "送海螺礼物总值")
    private Long drawNum;
    @Excel(name = "微信注册")
    private String weixinOpenid;
    @Excel(name = "QQ注册")
    private String qqOpenid;
    @Excel(name = "注册时间", format = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date signTime;
    @Excel(name = "最近登录时间", format = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date lastLoginTime;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Long getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(Long chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public Double getExechangeDiamond() {
        return exechangeDiamond;
    }

    public void setExechangeDiamond(Double exechangeDiamond) {
        this.exechangeDiamond = exechangeDiamond;
    }

    public Long getExperChange() {
        return experChange;
    }

    public void setExperChange(Long experChange) {
        this.experChange = experChange;
    }

    public Long getCharmChange() {
        return charmChange;
    }

    public void setCharmChange(Long charmChange) {
        this.charmChange = charmChange;
    }

    public Long getExperNum() {
        return experNum;
    }

    public void setExperNum(Long experNum) {
        this.experNum = experNum;
    }

    public Long getCharmNum() {
        return charmNum;
    }

    public void setCharmNum(Long charmNum) {
        this.charmNum = charmNum;
    }

    public Long getNormalGiftNum() {
        return normalGiftNum;
    }

    public void setNormalGiftNum(Long normalGiftNum) {
        this.normalGiftNum = normalGiftNum;
    }

    public Long getDoCallNum() {
        return doCallNum;
    }

    public void setDoCallNum(Long doCallNum) {
        this.doCallNum = doCallNum;
    }

    public Long getDrawNum() {
        return drawNum;
    }

    public void setDrawNum(Long drawNum) {
        this.drawNum = drawNum;
    }

    public String getWeixinOpenid() {
        return weixinOpenid;
    }

    public void setWeixinOpenid(String weixinOpenid) {
        this.weixinOpenid = weixinOpenid;
    }

    public String getQqOpenid() {
        return qqOpenid;
    }

    public void setQqOpenid(String qqOpenid) {
        this.qqOpenid = qqOpenid;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "StatReportBO{" +
                "erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", os='" + os + '\'' +
                ", chargeAmount=" + chargeAmount +
                ", exechangeDiamond=" + exechangeDiamond +
                ", experChange=" + experChange +
                ", charmChange=" + charmChange +
                ", experNum=" + experNum +
                ", charmNum=" + charmNum +
                ", normalGiftNum=" + normalGiftNum +
                ", doCallNum=" + doCallNum +
                ", drawNum=" + drawNum +
                ", weixinOpenid='" + weixinOpenid + '\'' +
                ", qqOpenid='" + qqOpenid + '\'' +
                ", signTime=" + signTime +
                ", lastLoginTime=" + lastLoginTime +
                '}';
    }
}
