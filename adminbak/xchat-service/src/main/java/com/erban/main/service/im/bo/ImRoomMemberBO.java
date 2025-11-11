package com.erban.main.service.im.bo;

import java.util.Date;

public class ImRoomMemberBO {
    
    private Long uid;

    private String nick;

    private String avatar;

    private Byte gender;

    private String headwearUrl;

    private String headwearName;

    private String carUrl;

    private String carName;

    private Integer experLevel;

    private Integer charmLevel;

    private Date createTime;

    public Long getCreateTime() {
        return createTime == null ? 0 : createTime.getTime();
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getHeadwearUrl() {
        return headwearUrl;
    }

    public void setHeadwearUrl(String headwearUrl) {
        this.headwearUrl = headwearUrl;
    }

    public String getHeadwearName() {
        return headwearName;
    }

    public void setHeadwearName(String headwearName) {
        this.headwearName = headwearName;
    }

    public String getCarUrl() {
        return carUrl;
    }

    public void setCarUrl(String carUrl) {
        this.carUrl = carUrl;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
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

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
