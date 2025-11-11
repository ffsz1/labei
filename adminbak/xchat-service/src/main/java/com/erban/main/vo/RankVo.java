package com.erban.main.vo;

public class RankVo {
    private Long uid;
    private Long erbanNo;
    private String avatar;
    private String nick;
    private Byte gender;
    private double totalNum;
    private Integer nobleId;
    private String nobleName;
    private Byte rankHide;
    private Boolean hasPrettyNo;
    private Integer experLevel; //等级值
    private Integer charmLevel; //魅力值

    public Boolean getHasPrettyNo() {
        return hasPrettyNo;
    }

    public void setHasPrettyNo(Boolean hasPrettyNo) {
        this.hasPrettyNo = hasPrettyNo;
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
        this.nobleName = nobleName;
    }

    public Byte getRankHide() {
        return rankHide;
    }

    public void setRankHide(Byte rankHide) {
        this.rankHide = rankHide;
    }

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public double getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(double totalNum) {
        this.totalNum = totalNum;
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
