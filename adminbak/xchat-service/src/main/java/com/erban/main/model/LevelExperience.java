package com.erban.main.model;

import java.util.Date;

public class LevelExperience {
    private Integer id;

    private Integer levelSeq;

    private String levelName;

    private String levelGrp;

    private Long amount;

    private Long needMin;

    private Byte status;

    private Byte broadcast;

    private Byte interaction;

    private Date createTime;

    private Long needMax;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevelSeq() {
        return levelSeq;
    }

    public void setLevelSeq(Integer levelSeq) {
        this.levelSeq = levelSeq;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName == null ? null : levelName.trim();
    }

    public String getLevelGrp() {
        return levelGrp;
    }

    public void setLevelGrp(String levelGrp) {
        this.levelGrp = levelGrp == null ? null : levelGrp.trim();
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getNeedMin() {
        return needMin;
    }

    public void setNeedMin(Long needMin) {
        this.needMin = needMin;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Byte broadcast) {
        this.broadcast = broadcast;
    }

    public Byte getInteraction() {
        return interaction;
    }

    public void setInteraction(Byte interaction) {
        this.interaction = interaction;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getNeedMax() {
        return needMax;
    }

    public void setNeedMax(Long needMax) {
        this.needMax = needMax;
    }
}
