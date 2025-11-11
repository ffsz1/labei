package com.erban.main.model;

import java.util.Date;

public class Users {
    private Long uid;

    private Long erbanNo;

    private Boolean hasPrettyErbanNo;

    private String phone;

    private Date birth;

    private Byte star;

    private String nick;

    private String email;

    private String signture;

    private String userVoice;

    private Integer voiceDura;

    private Integer followNum;

    private Integer fansNum;

    private Byte defUser;

    private Long fortune;

    private Byte channelType;

    private Date lastLoginTime;

    private String lastLoginIp;

    private Byte gender;

    private String avatar;

    private String region;

    private String userDesc;

    private String alipayAccount;

    private String alipayAccountName;

    private Date createTime;

    private Date updateTime;

    private String wxPubFansOpenid;

    private Byte wxPubFansGender;

    private Long roomUid;

    private Long shareUid;

    private Byte shareChannel;

    private String wxOpenid;

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

    private Integer nobleId;

    private String nobleName;

    private Byte withdrawStatus;

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

    public Boolean getHasPrettyErbanNo() {
        return hasPrettyErbanNo;
    }

    public void setHasPrettyErbanNo(Boolean hasPrettyErbanNo) {
        this.hasPrettyErbanNo = hasPrettyErbanNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Byte getStar() {
        return star;
    }

    public void setStar(Byte star) {
        this.star = star;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getSignture() {
        return signture;
    }

    public void setSignture(String signture) {
        this.signture = signture == null ? null : signture.trim();
    }

    public String getUserVoice() {
        return userVoice;
    }

    public void setUserVoice(String userVoice) {
        this.userVoice = userVoice == null ? null : userVoice.trim();
    }

    public Integer getVoiceDura() {
        return voiceDura;
    }

    public void setVoiceDura(Integer voiceDura) {
        this.voiceDura = voiceDura;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Byte getDefUser() {
        return defUser;
    }

    public void setDefUser(Byte defUser) {
        this.defUser = defUser;
    }

    public Long getFortune() {
        return fortune;
    }

    public void setFortune(Long fortune) {
        this.fortune = fortune;
    }

    public Byte getChannelType() {
        return channelType;
    }

    public void setChannelType(Byte channelType) {
        this.channelType = channelType;
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

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc == null ? null : userDesc.trim();
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount == null ? null : alipayAccount.trim();
    }

    public String getAlipayAccountName() {
        return alipayAccountName;
    }

    public void setAlipayAccountName(String alipayAccountName) {
        this.alipayAccountName = alipayAccountName == null ? null : alipayAccountName.trim();
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

    public String getWxPubFansOpenid() {
        return wxPubFansOpenid;
    }

    public void setWxPubFansOpenid(String wxPubFansOpenid) {
        this.wxPubFansOpenid = wxPubFansOpenid == null ? null : wxPubFansOpenid.trim();
    }

    public Byte getWxPubFansGender() {
        return wxPubFansGender;
    }

    public void setWxPubFansGender(Byte wxPubFansGender) {
        this.wxPubFansGender = wxPubFansGender;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Long getShareUid() {
        return shareUid;
    }

    public void setShareUid(Long shareUid) {
        this.shareUid = shareUid;
    }

    public Byte getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(Byte shareChannel) {
        this.shareChannel = shareChannel;
    }

    public String getWxOpenid() {
        return wxOpenid;
    }

    public void setWxOpenid(String wxOpenid) {
        this.wxOpenid = wxOpenid == null ? null : wxOpenid.trim();
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

    public Integer getNobleId() {
        return nobleId;
    }

    public void setNobleId(Integer nobleId) {
        this.nobleId = nobleId;
    }

    public String getNobleName() {
        return nobleName;
    }

    public void setNobleName(String nobleName) {
        this.nobleName = nobleName == null ? null : nobleName.trim();
    }

    public Byte getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(Byte withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }
}
