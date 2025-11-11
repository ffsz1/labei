package com.erban.main.vo;

public class AppVersionVo {

    private String os;

    private String version;

    private Byte status;

    private String versionDesc;

    private String updateVersion;

    private String updateVersionDesc;

    private String downloadUrl;

    private Integer kickWaiting;

    public Integer getKickWaiting() {
        return kickWaiting;
    }

    public void setKickWaiting(Integer kickWaiting) {
        this.kickWaiting = kickWaiting;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(String updateVersion) {
        this.updateVersion = updateVersion;
    }


    public String getUpdateVersionDesc() {
        return updateVersionDesc;
    }

    public void setUpdateVersionDesc(String updateVersionDesc) {
        this.updateVersionDesc = updateVersionDesc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
