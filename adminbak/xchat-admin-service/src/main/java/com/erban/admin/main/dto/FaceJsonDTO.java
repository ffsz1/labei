package com.erban.admin.main.dto;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-05-14
 * @time 17:12
 */
public class FaceJsonDTO {

    private List<FacesDTO> faces;

    private Integer version;

    private String zipMd5;

    private String zipUrl;

    public List<FacesDTO> getFaces() {
        return faces;
    }

    public void setFaces(List<FacesDTO> faces) {
        this.faces = faces;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getZipMd5() {
        return zipMd5;
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
}
