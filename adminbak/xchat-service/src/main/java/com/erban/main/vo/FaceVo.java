package com.erban.main.vo;

import java.util.List;

/**
 * Created by liuguofu on 2017/9/11.
 */
public class FaceVo {
    private Integer faceId;

    private String faceName;

    private Integer faceParentId;

    private String facePicUrl;

    private Boolean hasGifUrl;

    private String faceGifUrl;


    private Boolean isShow;

    private Integer faceValue;
    private List<FaceVo> children;
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
        this.faceName = faceName;
    }

    public Integer getFaceParentId() {
        return faceParentId;
    }

    public void setFaceParentId(Integer faceParentId) {
        this.faceParentId = faceParentId;
    }

    public Integer getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;
    }

    public String getFacePicUrl() {
        return facePicUrl;
    }

    public void setFacePicUrl(String facePicUrl) {
        this.facePicUrl = facePicUrl;
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
        this.faceGifUrl = faceGifUrl;
    }

    public List<FaceVo> getChildren() {
        return children;
    }

    public void setChildren(List<FaceVo> children) {
        this.children = children;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }
}
