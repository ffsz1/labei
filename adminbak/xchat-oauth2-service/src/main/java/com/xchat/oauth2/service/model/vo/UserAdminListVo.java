package com.xchat.oauth2.service.model.vo;

import com.xchat.common.utils.DateUtil;

import java.util.Date;

/**
 * @class: UserAdminListVo.java
 * @author: chenjunsheng
 * @date 2018/5/31
 */
public class UserAdminListVo {
    private Long erbanNo;
    private String avatar;
    private Long uid;
    private String nick;
    private Integer diamondNum;
    private Integer goldNum;
    private String phone;
    private Date birth;
    private Integer gender;
    private String alipayAccount;
    private String alipayAccountName;
    private Integer defUser;
    private Boolean isFirstCharge;
    private Date createTime;
    private String appVersion;
    private String levelExperience;
    private String levelCharm;
    private Date lastLoginTime;

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(Integer diamondNum) {
        this.diamondNum = diamondNum;
    }

    public Integer getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(Integer goldNum) {
        this.goldNum = goldNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
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

    public Integer getDefUser() {
        return defUser;
    }

    public void setDefUser(Integer defUser) {
        this.defUser = defUser;
    }

    public Boolean getFirstCharge() {
        return isFirstCharge;
    }

    public void setFirstCharge(Boolean firstCharge) {
        isFirstCharge = firstCharge;
    }

    public String getCreateTime() {
        return createTime != null ? DateUtil.date2Str(createTime, DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS) : null;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getLevelExperience() {
        return levelExperience;
    }

    public void setLevelExperience(String levelExperience) {
        this.levelExperience = levelExperience;
    }

    public String getLevelCharm() {
        return levelCharm;
    }

    public void setLevelCharm(String levelCharm) {
        this.levelCharm = levelCharm;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "UserAdminListVo{" +
                "erbanNo=" + erbanNo +
                ", avatar='" + avatar + '\'' +
                ", uid=" + uid +
                ", nick='" + nick + '\'' +
                ", diamondNum=" + diamondNum +
                ", goldNum=" + goldNum +
                ", phone='" + phone + '\'' +
                ", birth=" + birth +
                ", gender=" + gender +
                ", alipayAccount='" + alipayAccount + '\'' +
                ", alipayAccountName='" + alipayAccountName + '\'' +
                ", defUser=" + defUser +
                ", isFirstCharge=" + isFirstCharge +
                ", createTime=" + createTime +
                ", appVersion='" + appVersion + '\'' +
                ", levelExperience='" + levelExperience + '\'' +
                ", levelCharm='" + levelCharm + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                '}';
    }
}
