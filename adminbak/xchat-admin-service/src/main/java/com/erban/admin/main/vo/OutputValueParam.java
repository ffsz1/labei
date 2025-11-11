package com.erban.admin.main.vo;

public class OutputValueParam {
    private Integer pageNum;
    private Integer pageSize;
    private String os;//平台
    private Byte channel;
    private Byte gender;
    private String signBegin;
    private String signEnd;
    private Byte showType;
    private String uidList;
    private String app;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Byte getChannel() {
        return channel;
    }

    public void setChannel(Byte channel) {
        this.channel = channel;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getSignBegin() {
        return signBegin;
    }

    public void setSignBegin(String signBegin) {
        this.signBegin = signBegin;
    }

    public String getSignEnd() {
        return signEnd;
    }

    public void setSignEnd(String signEnd) {
        this.signEnd = signEnd;
    }

    public Byte getShowType() {
        return showType;
    }

    public void setShowType(Byte showType) {
        this.showType = showType;
    }

    public String getUidList() {
        return uidList;
    }

    public void setUidList(String uidList) {
        this.uidList = uidList;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "OutputValueParam{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", os='" + os + '\'' +
                ", channel=" + channel +
                ", gender=" + gender +
                ", signBegin='" + signBegin + '\'' +
                ", signEnd='" + signEnd + '\'' +
                ", showType=" + showType +
                ", uidList='" + uidList + '\'' +
                ", app='" + app + '\'' +
                '}';
    }
}
