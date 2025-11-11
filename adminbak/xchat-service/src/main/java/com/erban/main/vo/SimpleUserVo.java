package com.erban.main.vo;

import java.util.Date;

public class SimpleUserVo {

    private Long uid;
    private Long erbanNo;
    private String phone;
    private Date birth;
    private Byte star;
    private String nick;
    private String email;
    private String signture;
    private String userVoice;
    private Integer followNum;
    private Integer fansNum;
    private Long fortune;
    private Byte gender;
    private String avatar; // 头像
    private Integer experLevel; //等级值
    private Integer charmLevel; //魅力值
    private Integer age;

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
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Long getFortune() {
        return fortune;
    }

    public void setFortune(Long fortune) {
        this.fortune = fortune;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(Integer experLevel) {
        this.experLevel = experLevel;
    }

    public Integer getCharmLevel() {
        return charmLevel;
    }

    public void setCharmLevel(Integer charmLevel) {
        this.charmLevel = charmLevel;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "SimpleUserVo{" +
                "uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", phone='" + phone + '\'' +
                ", birth=" + birth +
                ", star=" + star +
                ", nick='" + nick + '\'' +
                ", email='" + email + '\'' +
                ", signture='" + signture + '\'' +
                ", userVoice='" + userVoice + '\'' +
                ", followNum=" + followNum +
                ", fansNum=" + fansNum +
                ", fortune=" + fortune +
                ", gender=" + gender +
                ", avatar='" + avatar + '\'' +
                ", experLevel=" + experLevel +
                ", charmLevel=" + charmLevel +
                ", age=" + age +
                '}';
    }
}
