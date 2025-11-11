package com.erban.main.model;

import java.util.Date;

public class Face {
    private Integer faceId;

    private String faceName;

    private Integer faceParentId;

    private String facePicUrl;

    private Boolean hasGifUrl;

    private String faceGifUrl;

    private Integer seqNo;

    private Byte faceStatus;

    private Boolean isShow;

    private Integer faceValue;

    private Date createTime;

    public Integer getFaceId() {
        return faceId;
    }

    public void setFaceId(Integer faceId) {
        this.faceId = faceId;
    }

    public String getFaceName() {
        return faceName;
    }

    public void setFaceName(String faceName) {
        this.faceName = faceName == null ? null : faceName.trim();
    }

    public Integer getFaceParentId() {
        return faceParentId;
    }

    public void setFaceParentId(Integer faceParentId) {
        this.faceParentId = faceParentId;
    }

    public String getFacePicUrl() {
        return facePicUrl;
    }

    public void setFacePicUrl(String facePicUrl) {
        this.facePicUrl = facePicUrl == null ? null : facePicUrl.trim();
    }

    public Boolean getHasGifUrl() {
        return hasGifUrl;
    }

    public void setHasGifUrl(Boolean hasGifUrl) {
        this.hasGifUrl = hasGifUrl;
    }

    public String getFaceGifUrl() {
        return faceGifUrl;
    }

    public void setFaceGifUrl(String faceGifUrl) {
        this.faceGifUrl = faceGifUrl == null ? null : faceGifUrl.trim();
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Byte getFaceStatus() {
        return faceStatus;
    }

    public void setFaceStatus(Byte faceStatus) {
        this.faceStatus = faceStatus;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Integer getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
