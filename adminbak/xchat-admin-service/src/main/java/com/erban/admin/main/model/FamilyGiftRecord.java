package com.erban.admin.main.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class FamilyGiftRecord implements Serializable {
    /**
     * 编号
     */
    private Long id;

    /**
     * 礼物ID
     */
    private Integer giftId;

    /**
     * 家族ID
     */
    private Long teamId;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 威望数
     */
    private Long num;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
