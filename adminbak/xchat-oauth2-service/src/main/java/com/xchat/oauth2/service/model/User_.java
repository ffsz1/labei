package com.xchat.oauth2.service.model;

import java.util.Date;

public class User_ {
    private Long uid;

    private Long xChatId;

    private String nick;

    private String charTag;

    private Date birth;

    private Integer voicePrice;

    private Integer videoPrice;

    private String gender;

    private String avatar;

    private String introVoiceUrl;

    private Long fansNum;

    private Long visitorNum;

    private Long upNum;

    private Long followNum;

    private String region;

    private String signature;

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
        this.nick = nick == null ? null : nick.trim();
    }

    public String getCharTag() {
        return charTag;
    }

    public void setCharTag(String charTag) {
        this.charTag = charTag == null ? null : charTag.trim();
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getVoicePrice() {
        return voicePrice;
    }

    public void setVoicePrice(Integer voicePrice) {
        this.voicePrice = voicePrice;
    }

    public Integer getVideoPrice() {
        return videoPrice;
    }

    public void setVideoPrice(Integer videoPrice) {
        this.videoPrice = videoPrice;
    }

    public Long getxChatId() {
        return xChatId;
    }

    public void setxChatId(Long xChatId) {
        this.xChatId = xChatId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getIntroVoiceUrl() {
        return introVoiceUrl;
    }

    public void setIntroVoiceUrl(String introVoiceUrl) {
        this.introVoiceUrl = introVoiceUrl == null ? null : introVoiceUrl.trim();
    }

    public Long getFansNum() {
        return fansNum;
    }

    public void setFansNum(Long fansNum) {
        this.fansNum = fansNum;
    }

    public Long getVisitorNum() {
        return visitorNum;
    }

    public void setVisitorNum(Long visitorNum) {
        this.visitorNum = visitorNum;
    }

    public Long getUpNum() {
        return upNum;
    }

    public void setUpNum(Long upNum) {
        this.upNum = upNum;
    }

    public Long getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Long followNum) {
        this.followNum = followNum;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }
}
