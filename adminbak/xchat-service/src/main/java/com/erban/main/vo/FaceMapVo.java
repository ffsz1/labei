package com.erban.main.vo;

import java.util.List;

/**
 * Created by liuguofu on 2017/9/12.
 */
public class FaceMapVo {
    private List<FaceVo> faces;
    private String version;

    public String getVersion() {
        return version;
    }

    public List<FaceVo> getFaces() {
        return faces;
    }

    public void setFaces(List<FaceVo> faces) {
        this.faces = faces;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
