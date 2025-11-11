package com.erban.admin.main.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class FamilyTeam implements Serializable {
    /**
     * 编号
     */
    private Long id;

    /**
     * 家族ID
     */
    private Long familyId;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * logo
     */
    private String familyLogo;

    /**
     * 名称
     */
    private String familyName;

    /**
     * 公告
     */
    private String familyNotice;

    /**
     * 手上厅,多个用逗号隔开
     */
    private String hall;

    /**
     *  审核(0.未审核,1.审核通过,2.审核失败)
     */
    private Integer status;

    /**
     * 威望
     */
    private Integer prestige;

    /**
     * 背景图
     */
    private String bgimg;

    /**
     * 申请加入验证(0.否1.是)
     */
    private Integer verification;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 显示/隐藏
     */
    private Integer display;

    private Long roomId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getFamilyLogo() {
        return familyLogo;
    }

    public void setFamilyLogo(String familyLogo) {
        this.familyLogo = familyLogo;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyNotice() {
        return familyNotice;
    }

    public void setFamilyNotice(String familyNotice) {
        this.familyNotice = familyNotice;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPrestige() {
        return prestige;
    }

    public void setPrestige(Integer prestige) {
        this.prestige = prestige;
    }

    public String getBgimg() {
        return bgimg;
    }

    public void setBgimg(String bgimg) {
        this.bgimg = bgimg;
    }

    public Integer getVerification() {
        return verification;
    }

    public void setVerification(Integer verification) {
        this.verification = verification;
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

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
