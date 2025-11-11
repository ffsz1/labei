package com.juxiao.xchat.service.api.wish.bo;

import com.juxiao.xchat.dao.wish.domain.Wish;

import java.util.Date;
import java.util.List;

public class WishBo {

    private Long uid;
    private String remarks;
    private String viceUrl;
    private Integer audioDura;
    private List<Integer> personalLabels;
    private List<Integer> meetLabels;

    public Wish getWish(){
        Wish wish=new Wish();
        wish.setUid(this.getUid());
        wish.setRemarks(this.getRemarks());
        wish.setViceUrl(this.getViceUrl());
        wish.setAudioDura(this.getAudioDura());
        return wish;
    }

    public WishBo() {
    }

    public WishBo(Long uid, String remarks, String viceUrl, Integer audioDura, List<Integer> personalLabels, List<Integer> meetLabels) {
        this.uid = uid;
        this.remarks = remarks;
        this.viceUrl = viceUrl;
        this.audioDura = audioDura;
        this.personalLabels = personalLabels;
        this.meetLabels = meetLabels;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public List<Integer> getPersonalLabels() {
        return personalLabels;
    }

    public void setPersonalLabels(List<Integer> personalLabels) {
        this.personalLabels = personalLabels;
    }

    public List<Integer> getMeetLabels() {
        return meetLabels;
    }

    public void setMeetLabels(List<Integer> meetLabels) {
        this.meetLabels = meetLabels;
    }

    public Integer getAudioDura() {
        return audioDura;
    }

    public void setAudioDura(Integer audioDura) {
        this.audioDura = audioDura;
    }
}
