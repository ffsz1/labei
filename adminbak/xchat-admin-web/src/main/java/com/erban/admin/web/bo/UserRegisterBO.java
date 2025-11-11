package com.erban.admin.web.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class UserRegisterBO {
    @Excel(name = "拉贝号")
    private Long erbanNo;
    @Excel(name = "用户昵称")
    private String nick;
    @Excel(name = "是否为靓号")
    private Boolean hasPrettyErbanNo;
    @Excel(name = "性别")
    private String gender;
    @Excel(name = "生日", format = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date birth;
    @Excel(name = "手机号码")
    private String phone;
    @Excel(name = "支付宝账号")
    private String alipayAccount;
    @Excel(name = "支付宝账号姓名")
    private String alipayAccountName;
    @Excel(name = "注册IP")
    private String registerIp;
    @Excel(name = "注册时间", format = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date signTime;
    @Excel(name = "微信注册")
    private String weixinOpenid;
    @Excel(name = "QQ注册")
    private String qqOpenid;
    @Excel(name = "下载渠道")
    private String channel;
    @Excel(name = "APP版本")
    private String appVersion;
    @Excel(name = "设备")
    private String model;
    @Excel(name = "设备ID")
    private String deviceId;
    @Excel(name = "系统")
    private String os;
    @Excel(name = "系统版本")
    private String osversion;
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

    public Boolean getHasPrettyErbanNo() {
        return hasPrettyErbanNo;
    }

    public void setHasPrettyErbanNo(Boolean hasPrettyErbanNo) {
        this.hasPrettyErbanNo = hasPrettyErbanNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsversion() {
        return osversion;
    }

    public void setOsversion(String osversion) {
        this.osversion = osversion;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "UserRegisterBO{" +
                "erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", hasPrettyErbanNo=" + hasPrettyErbanNo +
                ", gender='" + gender + '\'' +
                ", birth=" + birth +
                ", phone='" + phone + '\'' +
                ", alipayAccount='" + alipayAccount + '\'' +
                ", alipayAccountName='" + alipayAccountName + '\'' +
                ", registerIp='" + registerIp + '\'' +
                ", signTime=" + signTime +
                ", weixinOpenid='" + weixinOpenid + '\'' +
                ", qqOpenid='" + qqOpenid + '\'' +
                ", channel='" + channel + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", model='" + model + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", os='" + os + '\'' +
                ", osversion='" + osversion + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                '}';
    }
}
