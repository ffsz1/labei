package com.tongdaxing.xchat_core.user.bean;


import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.utils.StarUtils;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by chenran on 2017/3/8.
 */
public class UserInfo extends RealmObject implements Serializable {

    public static final int USER_TYPE_COMMON = 1;
    public static final int USER_TYPE_OFFICIAL = 2;

    @PrimaryKey
    private long uid;
    //耳伴号
    private long erbanNo;
    // 昵称
    private String nick;
    //头像
    private String avatar;
    //性别 1:男 2：女 0 ：未知
    private int gender;
    //生日
    private long birth;
    //生日日期格式不存数据库
    private String birthStr;
    //签名
    private String signture;
    //声音展示文件
    private String userVoice;
    //声音时间
    private int voiceDura;
    //关注数
    private long followNum;
    //粉丝数
    private long fansNum;
    //人气值
    private long fortune;
    //1普通账号，2官方账号，3机器账号
    private int defUser;
    //地区
    private String region;
    //个人简介
    private String userDesc;
    //个人相册
    private RealmList<UserPhoto> privatePhoto;
    //财富等级
    private int experLevel;
    //魅力等级
    private int charmLevel;

    private String phone;

    private String carUrl;

    private String carName;

    private String headwearUrl;

    private long createTime;

    private long tol;

    //显示萌新模块权限开关 1 有 0无
    private int findNewUsers;
    //是否有绑定微信
    private boolean hasWx;
    //是否有绑定QQ
    private boolean hasQq;
    //金币
    private double goldNum;
    //萌币
    private int mcoinNum;

    public double getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(double goldNum) {
        this.goldNum = goldNum;
    }

    public int getMcoinNum() {
        return mcoinNum;
    }

    public void setMcoinNum(int mcoinNum) {
        this.mcoinNum = mcoinNum;
    }

    public boolean isHasWx() {
        return hasWx;
    }

    public void setHasWx(boolean hasWx) {
        this.hasWx = hasWx;
    }

    public boolean isHasQq() {
        return hasQq;
    }

    public void setHasQq(boolean hasQq) {
        this.hasQq = hasQq;
    }

    /**
     * 用户活跃值
     */
    private int liveness;

    private String shareCode;

    public long getTol() {
        return tol;
    }

    public void setTol(long tol) {
        this.tol = tol;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getHeadwearUrl() {
        return headwearUrl;
    }

    public void setHeadwearUrl(String headwearUrl) {
        this.headwearUrl = headwearUrl;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarUrl() {
        return carUrl;
    }

    public void setCarUrl(String carUrl) {
        this.carUrl = carUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(int experLevel) {
        this.experLevel = experLevel;
    }

    public int getCharmLevel() {
        return charmLevel;
    }

    public void setCharmLevel(int charmLevel) {
        this.charmLevel = charmLevel;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getBirth() {
        return birth;
    }

    public String getBirthStr() {
        return birthStr;
    }

    public void setBirthStr(String birthStr) {
        this.birthStr = birthStr;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public String getStarStr() {
        return StarUtils.getConstellation(new Date(Long.valueOf(birth) / 1000));
//        return StarUtils.getConstellation(new Date(TimeUtil.getDateTimeString(Long.valueOf(birth),"yyyy-MM-dd")));
    }


    public String getSignture() {
        return signture;
    }

    public void setSignture(String signture) {
        this.signture = signture;
    }

    public String getUserVoice() {
        return userVoice;
    }

    public void setUserVoice(String userVoice) {
        this.userVoice = userVoice;
    }

    public int getVoiceDura() {
        return voiceDura;
    }

    public void setVoiceDura(int voiceDura) {
        this.voiceDura = voiceDura;
    }

    public long getFollowNum() {
        return followNum;
    }

    public void setFollowNum(long followNum) {
        this.followNum = followNum;
    }

    public long getFansNum() {
        return fansNum;
    }

    public void setFansNum(long fansNum) {
        this.fansNum = fansNum;
    }

    public long getFortune() {
        return fortune;
    }

    public void setFortune(long fortune) {
        this.fortune = fortune;
    }

    public int getDefUser() {
        return defUser;
    }

    public void setDefUser(int defUser) {
        this.defUser = defUser;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public RealmList<UserPhoto> getPrivatePhoto() {
        return privatePhoto;
    }

    public void setPrivatePhoto(RealmList<UserPhoto> privatePhoto) {
        this.privatePhoto = privatePhoto;
    }

    public int getFindNewUsers() {
        return findNewUsers;
    }

    public void setFindNewUsers(int findNewUsers) {
        this.findNewUsers = findNewUsers;
    }

    public int getLiveness() {
        return liveness;
    }

    public void setLiveness(int liveness) {
        this.liveness = liveness;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", birth=" + birth +
                ", birthStr='" + birthStr + '\'' +
                ", signture='" + signture + '\'' +
                ", userVoice='" + userVoice + '\'' +
                ", voiceDura=" + voiceDura +
                ", followNum=" + followNum +
                ", fansNum=" + fansNum +
                ", fortune=" + fortune +
                ", defUser=" + defUser +
                ", region='" + region + '\'' +
                ", userDesc='" + userDesc + '\'' +
                ", privatePhoto=" + privatePhoto +
                ", experLevel=" + experLevel +
                ", charmLevel=" + charmLevel +
                ", phone='" + phone + '\'' +
                ", carUrl='" + carUrl + '\'' +
                ", carName='" + carName + '\'' +
                ", headwearUrl='" + headwearUrl + '\'' +
                ", createTime=" + createTime +
                ", tol=" + tol +
                ", findNewUsers=" + findNewUsers +
                ", hasWx=" + hasWx +
                ", hasQq=" + hasQq +
                ", goldNum=" + goldNum +
                ", mcoinNum=" + mcoinNum +
                ", liveness=" + liveness +
                ", shareCode='" + shareCode + '\'' +
                '}';
    }
}

