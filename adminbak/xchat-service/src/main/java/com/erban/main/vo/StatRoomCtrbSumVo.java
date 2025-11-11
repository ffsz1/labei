package com.erban.main.vo;


import com.erban.main.model.NobleUsers;

public class StatRoomCtrbSumVo {
    private Long uid;//房主UID
    private String nick;
    private String avatar;
    private Byte gender;
    private Long ctrbUid;
    private Long sumGold;

    private Integer experLevel; //等级值
    private Integer charmLevel; //魅力值

    private NobleUsers nobleUsers;

    public NobleUsers getNobleUsers() {
        return nobleUsers;
    }

    public void setNobleUsers(NobleUsers nobleUsers) {
        this.nobleUsers = nobleUsers;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getCtrbUid() {
        return ctrbUid;
    }

    public void setCtrbUid(Long ctrbUid) {
        this.ctrbUid = ctrbUid;
    }

    public Long getSumGold() {
        return sumGold;
    }

    public void setSumGold(Long sumGold) {
        this.sumGold = sumGold;
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
}
