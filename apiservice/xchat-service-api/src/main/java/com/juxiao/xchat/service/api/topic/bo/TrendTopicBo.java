package com.juxiao.xchat.service.api.topic.bo;

import com.juxiao.xchat.dao.topic.domain.TrendTopic;

public class TrendTopicBo {
    private Long id;

    private Long uid;

    private String name;

    private Long themeid;

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
    private Byte permissionType;//权限类型

    public TrendTopic getTopic(){
        TrendTopic trendTopic =new TrendTopic();
        trendTopic.setId(this.getId());
        trendTopic.setUid(this.getUid());
        trendTopic.setNick(this.getNick());
        trendTopic.setErbanNo(this.getErbanNo());
        trendTopic.setGender(this.getGender());
        trendTopic.setName(this.getName());
        trendTopic.setThemeid(this.getThemeid());
        trendTopic.setCommentnum(this.getCommentnum());
        trendTopic.setVisitnum(this.getVisitnum());
        trendTopic.setState(this.getState());
        trendTopic.setRemarks(this.getRemarks());
        trendTopic.setPraisenum(this.getPraisenum());
        trendTopic.setPictureUrl(this.getPictureUrl());
        trendTopic.setVoiceUrl(this.getVoiceUrl());
        trendTopic.setPermissionType(this.getPermissionType());
        trendTopic.setAudioDura(this.audioDura);
        return trendTopic;
    }

    public Integer getAudioDura() {
        return audioDura;
    }

    public void setAudioDura(Integer audioDura) {
        this.audioDura = audioDura;
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

    public Byte getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Byte permissionType) {
        this.permissionType = permissionType;
    }
}
