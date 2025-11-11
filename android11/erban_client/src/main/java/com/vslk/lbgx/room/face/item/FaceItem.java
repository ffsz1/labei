package com.vslk.lbgx.room.face.item;

/**
 * Created by chenran on 2017/9/9.
 */

public class FaceItem {
    private int faceId;
    private String faceName;
    private String faceUrl;

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public FaceItem(int faceId) {
        this.faceId = faceId;
    }

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public String getFaceName() {
        return faceName;
    }

    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }
}
