package com.erban.admin.main.dto;

import java.util.Date;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.dto
 * @date 2018/9/3
 * @time 20:31
 */
public class FamilyTeamDTO {

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

    private String nike;

    private Long erbanNo;

    /***
     * 禁言类型 0:解除禁言，1:禁言普通成员 3:禁言整个群(包括群主)
     */
    private Integer muteType;

    /**
     * 被邀请人同意方式，0-需要同意(默认),1-不需要同意
     */
    private Integer beinvitemode;

    /**
     * 谁可以邀请他人入群，0-管理员(默认),1-所有人
     */
    private Integer invitemode;


    /**
     * 谁可以修改群资料，0-管理员(默认),1-所有人 备注:只有群主才能修改
     */
    private Integer uptinfomode;






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

    public String getNike() {
        return nike;
    }

    public void setNike(String nike) {
        this.nike = nike;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Integer getMuteType() {
        return muteType;
    }

    public void setMuteType(Integer muteType) {
        this.muteType = muteType;
    }

    public Integer getBeinvitemode() {
        return beinvitemode;
    }

    public void setBeinvitemode(Integer beinvitemode) {
        this.beinvitemode = beinvitemode;
    }

    public Integer getInvitemode() {
        return invitemode;
    }

    public void setInvitemode(Integer invitemode) {
        this.invitemode = invitemode;
    }

    public Integer getUptinfomode() {
        return uptinfomode;
    }

    public void setUptinfomode(Integer uptinfomode) {
        this.uptinfomode = uptinfomode;
    }
}
