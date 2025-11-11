package com.erban.main.vo.activity;

public class RankActVo {
    private long uid;
    private long erbanNo;
    private String nick;
    private String avatar;
    private Integer experLevel; //等级值
    private Integer charmLevel; //魅力值
    private long total;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setErbanNo(long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public Long getErbanNo() {
        return erbanNo;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
