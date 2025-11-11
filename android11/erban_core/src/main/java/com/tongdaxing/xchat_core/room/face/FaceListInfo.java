package com.tongdaxing.xchat_core.room.face;


import java.io.Serializable;
import java.util.List;

/**
 * @author xiaoyu
 * @date 2017/12/8
 */

public class FaceListInfo implements Serializable {
    private List<FaceInfo> faces;
    private int version;
    private String zipMd5;
    private String zipUrl;


    public List<FaceInfo> getFaces() {
        return faces;
    }

    public void setFaces(List<FaceInfo> faces) {
        this.faces = faces;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getZipMd5() {
        return zipMd5.toLowerCase();
    }

    public void setZipMd5(String zipMd5) {
        this.zipMd5 = zipMd5;
    }

    public String getZipUrl() {
        return zipUrl;
    }

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    @Override
    public String toString() {
        return "FaceListInfo{" +
                "faces=" + faces +
                ", version=" + version +
                ", zipMd5='" + zipMd5 + '\'' +
                ", zipUrl='" + zipUrl + '\'' +
                '}';
    }
}
