package com.xchat.oauth2.service.vo.admin;

import java.util.Date;

/**
 * 用户注册VO
 */
public class AccountVo {
    //==============account==============
    private Long uid;

    private String phone;

    private Long erbanNo;

    private Date lastLoginTime;

    private String registerIp;

    private String weixinOpenid;

    private String qqOpenid;

    private String os;

    private String osversion;

    private String channel;

    private String model;

    private String deviceId;

    private String appVersion;

    private Date signTime;

    //=================关联注册StatPacketRegister==================

    private Boolean hasPrettyErbanNo;

    private Date birth;

    private String nick;

    private Byte gender;

    private String avatar;

    private String alipayAccount;

    private String alipayAccountName;

    //===========================================
    private Integer maleNum;
    private Integer femaleNum;
    private Integer other;
    private Integer sumNum;
    //===========================================
    private Long chargeAmount;
    private Double exechangeDiamond;
    private Long diamondWithDraw;
    private Double redPackerWithDraw;
    private Long charmNum;
    private Long experNum;
    // 送出普通礼物总值
    private Long normalGiftNum;
    // 打call总值
    private Long doCallNum;
    // 送海螺礼物总值
    private Long drawNum;
    private Long charmChange;
    private Long experChange;
    private String levelCharm;
    private String levelExper;
    private Long roomFlow;

    public Long getCharmChange() {
        return charmChange;
    }

    public void setCharmChange(Long charmChange) {
        this.charmChange = charmChange;
    }

    public Long getExperChange() {
        return experChange;
    }

    public void setExperChange(Long experChange) {
        this.experChange = experChange;
    }

    public Long getCharmNum() {
        return charmNum;
    }

    public void setCharmNum(Long charmNum) {
        this.charmNum = charmNum;
    }

    public Long getExperNum() {
        return experNum;
    }

    public void setExperNum(Long experNum) {
        this.experNum = experNum;
    }

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
        this.phone = phone;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Boolean getHasPrettyErbanNo() {
        return hasPrettyErbanNo;
    }

    public void setHasPrettyErbanNo(Boolean hasPrettyErbanNo) {
        this.hasPrettyErbanNo = hasPrettyErbanNo;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
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
        this.avatar = avatar;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public Long getDiamondWithDraw() {
        return diamondWithDraw;
    }

    public void setDiamondWithDraw(Long diamondWithDraw) {
        this.diamondWithDraw = diamondWithDraw;
    }

    public Double getRedPackerWithDraw() {
        return redPackerWithDraw;
    }

    public void setRedPackerWithDraw(Double redPackerWithDraw) {
        this.redPackerWithDraw = redPackerWithDraw;
    }

    public String getLevelCharm() {
        return levelCharm;
    }

    public void setLevelCharm(String levelCharm) {
        this.levelCharm = levelCharm;
    }

    public String getLevelExper() {
        return levelExper;
    }

    public void setLevelExper(String levelExper) {
        this.levelExper = levelExper;
    }

    public Long getRoomFlow() {
        return roomFlow;
    }

    public void setRoomFlow(Long roomFlow) {
        this.roomFlow = roomFlow;
    }

    public Integer getSumNum() {
        return sumNum;
    }

    public void setSumNum(Integer sumNum) {
        this.sumNum = sumNum;
    }

    public Integer getMaleNum() {
        return maleNum;
    }

    public void setMaleNum(Integer maleNum) {
        this.maleNum = maleNum;
    }

    public Integer getFemaleNum() {
        return femaleNum;
    }

    public void setFemaleNum(Integer femaleNum) {
        this.femaleNum = femaleNum;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
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
}
