package com.juxiao.xchat.service.api.topic.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.juxiao.xchat.dao.topic.dto.TrendTopicDTO;

import java.util.Date;

public class TrendTopicVo {
    private Long id;

    private Long uid;

    private String name;

    private Long themeid;

    private String themeName;//主题名称

    private Long erbanNo;

    private String nick;

    private Byte gender;

    private Integer commentnum;

    private Integer visitnum;

    private String state;

    private String remarks;

    private Integer praisenum;
    private Integer audioDura;//声音时长
    private String voiceUrl;
    private String pictureUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private Byte permissionType;//权限类型

    private String avatar;//头像



    private Integer canComment;
    private Integer isFans;
    private Integer isPraise;

    public TrendTopicVo() {
    }
    public TrendTopicVo(TrendTopicDTO trendTopicDTO) {
        this.id = trendTopicDTO.getId();
        this.uid = trendTopicDTO.getUid();
        this.name = trendTopicDTO.getName();
        this.themeid = trendTopicDTO.getThemeid();
        this.themeName = trendTopicDTO.getThemeName();
        this.erbanNo = trendTopicDTO.getErbanNo();
        this.nick = trendTopicDTO.getNick();
        this.gender = trendTopicDTO.getGender();
        this.commentnum = trendTopicDTO.getCommentnum();
        this.visitnum = trendTopicDTO.getVisitnum();
        this.state = trendTopicDTO.getState();
        this.remarks = trendTopicDTO.getRemarks();
        this.praisenum = trendTopicDTO.getPraisenum();
        this.voiceUrl = trendTopicDTO.getVoiceUrl();
        this.pictureUrl = trendTopicDTO.getPictureUrl();
        this.createTime = trendTopicDTO.getCreateTime();
        this.updateTime = trendTopicDTO.getUpdateTime();
        this.permissionType = trendTopicDTO.getPermissionType();
        this.avatar = trendTopicDTO.getAvatar();
        this.audioDura =trendTopicDTO.getAudioDura();
        //this.isCanComment = CanComment;
        //his.isCanFans = isCanFans;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getThemeid() {
        return themeid;
    }

    public void setThemeid(Long themeid) {
        this.themeid = themeid;
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

    public Integer getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(Integer commentnum) {
        this.commentnum = commentnum;
    }

    public Integer getVisitnum() {
        return visitnum;
    }

    public void setVisitnum(Integer visitnum) {
        this.visitnum = visitnum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(Integer praisenum) {
        this.praisenum = praisenum;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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

    public Byte getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Byte permissionType) {
        this.permissionType = permissionType;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getCanComment() {
        return canComment;
    }

    public void setCanComment(Integer canComment) {
        this.canComment = canComment;
    }

    public Integer getIsFans() {
        return isFans;
    }

    public void setIsFans(Integer isFans) {
        this.isFans = isFans;
    }

    public Integer getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(Integer isPraise) {
        this.isPraise = isPraise;
    }

    public Integer getAudioDura() {
        return audioDura;
    }

    public void setAudioDura(Integer audioDura) {
        this.audioDura = audioDura;
    }
}
