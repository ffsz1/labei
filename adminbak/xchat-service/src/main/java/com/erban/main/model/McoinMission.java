package com.erban.main.model;

public class McoinMission {
    private Integer id;

    private String missionName;

    private Integer mcoinAmount;

    private Integer freebiesId;

    private Byte freebiesType;

    private String completePicUrl;

    private String incompletePicUrl;

    private Byte seq;

    private String androidScheme;

    private String iosScheme;

    private Byte missionType;

    private Byte missionScope;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName == null ? null : missionName.trim();
    }

    public Integer getMcoinAmount() {
        return mcoinAmount;
    }

    public void setMcoinAmount(Integer mcoinAmount) {
        this.mcoinAmount = mcoinAmount;
    }

    public Integer getFreebiesId() {
        return freebiesId;
    }

    public void setFreebiesId(Integer freebiesId) {
        this.freebiesId = freebiesId;
    }

    public Byte getFreebiesType() {
        return freebiesType;
    }

    public void setFreebiesType(Byte freebiesType) {
        this.freebiesType = freebiesType;
    }

    public String getCompletePicUrl() {
        return completePicUrl;
    }

    public void setCompletePicUrl(String completePicUrl) {
        this.completePicUrl = completePicUrl == null ? null : completePicUrl.trim();
    }

    public String getIncompletePicUrl() {
        return incompletePicUrl;
    }

    public void setIncompletePicUrl(String incompletePicUrl) {
        this.incompletePicUrl = incompletePicUrl == null ? null : incompletePicUrl.trim();
    }

    public Byte getSeq() {
        return seq;
    }

    public void setSeq(Byte seq) {
        this.seq = seq;
    }

    public String getAndroidScheme() {
        return androidScheme;
    }

    public void setAndroidScheme(String androidScheme) {
        this.androidScheme = androidScheme == null ? null : androidScheme.trim();
    }

    public String getIosScheme() {
        return iosScheme;
    }

    public void setIosScheme(String iosScheme) {
        this.iosScheme = iosScheme == null ? null : iosScheme.trim();
    }

    public Byte getMissionType() {
        return missionType;
    }

    public void setMissionType(Byte missionType) {
        this.missionType = missionType;
    }

    public Byte getMissionScope() {
        return missionScope;
    }

    public void setMissionScope(Byte missionScope) {
        this.missionScope = missionScope;
    }
}
