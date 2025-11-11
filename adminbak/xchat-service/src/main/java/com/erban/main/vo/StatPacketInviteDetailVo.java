package com.erban.main.vo;

public class StatPacketInviteDetailVo {
    private Long uid;
    private Integer todayRegisterCount;
    private Integer totalRegisterCount;
    private Double packetNum;
    private Byte superiorBouns;
    private Integer directlyNum;
    private Integer lowerNum;

    public Byte getSuperiorBouns() {
        return superiorBouns;
    }

    public void setSuperiorBouns(Byte superiorBouns) {
        this.superiorBouns = superiorBouns;
    }

    public Integer getDirectlyNum() {
        return directlyNum;
    }

    public void setDirectlyNum(Integer directlyNum) {
        this.directlyNum = directlyNum;
    }

    public Integer getLowerNum() {
        return lowerNum;
    }

    public void setLowerNum(Integer lowerNum) {
        this.lowerNum = lowerNum;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getTodayRegisterCount() {
        return todayRegisterCount;
    }

    public void setTodayRegisterCount(Integer todayRegisterCount) {
        this.todayRegisterCount = todayRegisterCount;
    }

    public Integer getTotalRegisterCount() {
        return totalRegisterCount;
    }

    public void setTotalRegisterCount(Integer totalRegisterCount) {
        this.totalRegisterCount = totalRegisterCount;
    }

    public Double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }
}
