package com.erban.main.vo;

import java.util.Date;

public class ValentineVo {
    private Integer id;
    private Integer maleUid;
    private Long maleNo;
    private String maleNick;
    private String maleAvatar;
    private Integer femaleUid;
    private Long femaleNo;
    private String femaleNick;
    private String femaleAvatar;
    private Byte valentineStatus;
    private Date createTime;
    private Date updateTime;
    private Integer rankNo;
    private Long total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaleUid() {
        return maleUid;
    }

    public void setMaleUid(Integer maleUid) {
        this.maleUid = maleUid;
    }

    public Long getMaleNo() {
        return maleNo;
    }

    public void setMaleNo(Long maleNo) {
        this.maleNo = maleNo;
    }

    public String getMaleNick() {
        return maleNick;
    }

    public void setMaleNick(String maleNick) {
        this.maleNick = maleNick;
    }

    public String getMaleAvatar() {
        return maleAvatar;
    }

    public void setMaleAvatar(String maleAvatar) {
        this.maleAvatar = maleAvatar;
    }

    public Integer getFemaleUid() {
        return femaleUid;
    }

    public void setFemaleUid(Integer femaleUid) {
        this.femaleUid = femaleUid;
    }

    public Long getFemaleNo() {
        return femaleNo;
    }

    public void setFemaleNo(Long femaleNo) {
        this.femaleNo = femaleNo;
    }

    public String getFemaleNick() {
        return femaleNick;
    }

    public void setFemaleNick(String femaleNick) {
        this.femaleNick = femaleNick;
    }

    public String getFemaleAvatar() {
        return femaleAvatar;
    }

    public void setFemaleAvatar(String femaleAvatar) {
        this.femaleAvatar = femaleAvatar;
    }

    public Byte getValentineStatus() {
        return valentineStatus;
    }

    public void setValentineStatus(Byte valentineStatus) {
        this.valentineStatus = valentineStatus;
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

    public Integer getRankNo() {
        return rankNo;
    }

    public void setRankNo(Integer rankNo) {
        this.rankNo = rankNo;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
