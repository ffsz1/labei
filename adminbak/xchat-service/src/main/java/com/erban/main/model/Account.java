package com.erban.main.model;

import java.util.Date;

public class Account {
    private Long uid;

    private String phone;

    private Long erbanNo;

    private String password;

    private String neteaseToken;

    private String state;

    private Date lastLoginTime;

    private String lastLoginIp;

    private String weixinOpenid;

    private String weixinUnionid;

    private String qqOpenid;

    private String qqUnionid;

    private String os;

    private String osversion;

    private String app;

    private String imei;

    private String channel;

    private String linkedmeChannel;

    private String ispType;

    private String netType;

    private String model;

    private String deviceId;

    private String appVersion;

    private Date accBlockStartTime;

    private Date accBlockEndTime;

    private Date deviceBlockStartTime;

    private Date deviceBlockEndTime;

    private Date signTime;

    private Date updateTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNeteaseToken() {
        return neteaseToken;
    }

    public void setNeteaseToken(String neteaseToken) {
        this.neteaseToken = neteaseToken == null ? null : neteaseToken.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp == null ? null : lastLoginIp.trim();
    }

    public String getWeixinOpenid() {
        return weixinOpenid;
    }

    public void setWeixinOpenid(String weixinOpenid) {
        this.weixinOpenid = weixinOpenid == null ? null : weixinOpenid.trim();
    }

    public String getWeixinUnionid() {
        return weixinUnionid;
    }

    public void setWeixinUnionid(String weixinUnionid) {
        this.weixinUnionid = weixinUnionid == null ? null : weixinUnionid.trim();
    }

    public String getQqOpenid() {
        return qqOpenid;
    }

    public void setQqOpenid(String qqOpenid) {
        this.qqOpenid = qqOpenid == null ? null : qqOpenid.trim();
    }

    public String getQqUnionid() {
        return qqUnionid;
    }

    public void setQqUnionid(String qqUnionid) {
        this.qqUnionid = qqUnionid == null ? null : qqUnionid.trim();
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os == null ? null : os.trim();
    }

    public String getOsversion() {
        return osversion;
    }

    public void setOsversion(String osversion) {
        this.osversion = osversion == null ? null : osversion.trim();
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app == null ? null : app.trim();
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei == null ? null : imei.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public String getLinkedmeChannel() {
        return linkedmeChannel;
    }

    public void setLinkedmeChannel(String linkedmeChannel) {
        this.linkedmeChannel = linkedmeChannel == null ? null : linkedmeChannel.trim();
    }

    public String getIspType() {
        return ispType;
    }

    public void setIspType(String ispType) {
        this.ispType = ispType == null ? null : ispType.trim();
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType == null ? null : netType.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion == null ? null : appVersion.trim();
    }

    public Date getAccBlockStartTime() {
        return accBlockStartTime;
    }

    public void setAccBlockStartTime(Date accBlockStartTime) {
        this.accBlockStartTime = accBlockStartTime;
    }

    public Date getAccBlockEndTime() {
        return accBlockEndTime;
    }

    public void setAccBlockEndTime(Date accBlockEndTime) {
        this.accBlockEndTime = accBlockEndTime;
    }

    public Date getDeviceBlockStartTime() {
        return deviceBlockStartTime;
    }

    public void setDeviceBlockStartTime(Date deviceBlockStartTime) {
        this.deviceBlockStartTime = deviceBlockStartTime;
    }

    public Date getDeviceBlockEndTime() {
        return deviceBlockEndTime;
    }

    public void setDeviceBlockEndTime(Date deviceBlockEndTime) {
        this.deviceBlockEndTime = deviceBlockEndTime;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
