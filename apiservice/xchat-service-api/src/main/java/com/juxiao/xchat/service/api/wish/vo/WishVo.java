package com.juxiao.xchat.service.api.wish.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.juxiao.xchat.dao.wish.domain.Wish;
import com.juxiao.xchat.dao.wish.dto.WishDTO;

import java.util.Date;
import java.util.List;

public class WishVo {
    private Long uid;
    private Long erbanNo;
    private String nick;
    private String avatar;//头像
    private Byte gender;
    private String remarks; //补充说明
    private String viceUrl; //声音url
    private Integer audioAura;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime; //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime; //修改时间

    private List<String> personalLabels;
    private List<String> meetLabels;

    public WishVo() {
    }
    public WishVo(WishDTO wish) {
        this.uid=wish.getUid();
        this.erbanNo=wish.getErbanNo();
        this.nick=wish.getNick();
        this.gender=wish.getGender();
        this.remarks=wish.getRemarks();
        this.viceUrl=wish.getViceUrl();
        this.audioAura=wish.getAudioDura();
        this.createTime=wish.getCreateTime();
        this.updateTime=wish.getUpdateTime();
        this.avatar=wish.getAvatar();
    }
    public WishVo(Wish wish) {
        this.uid=wish.getUid();
        this.remarks=wish.getRemarks();
        this.viceUrl=wish.getViceUrl();
        this.audioAura=wish.getAudioDura();
        this.createTime=wish.getCreateTime();
        this.updateTime=wish.getUpdateTime();
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getViceUrl() {
        return viceUrl;
    }

    public void setViceUrl(String viceUrl) {
        this.viceUrl = viceUrl;
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

    public List<String> getPersonalLabels() {
        return personalLabels;
    }

    public void setPersonalLabels(List<String> personalLabels) {
        this.personalLabels = personalLabels;
    }

    public List<String> getMeetLabels() {
        return meetLabels;
    }

    public void setMeetLabels(List<String> meetLabels) {
        this.meetLabels = meetLabels;
    }

    public Integer getAudioAura() {
        return audioAura;
    }

    public void setAudioAura(Integer audioAura) {
        this.audioAura = audioAura;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
