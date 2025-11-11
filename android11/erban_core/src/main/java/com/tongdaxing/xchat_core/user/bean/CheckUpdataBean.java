package com.tongdaxing.xchat_core.user.bean;

/**
 * Created by Administrator on 2018/3/5.
 */

public class CheckUpdataBean {
    /**
     * os : iOS
     * version : 2.3.2
     * status : 4
     * versionDesc : 更新测试
     * updateVersion : 2.4.0
     * updateVersionDesc : 更新测试
     */

    private String os;
    private String version;
    private int status;
    private String versionDesc;
    private String updateVersion;
    private String updateVersionDesc;
    private String downloadUrl;
    private int kickWaiting;

    public int isKickWaiting() {
        return kickWaiting;
    }

    public void setKickWaiting(int kickWaiting) {
        this.kickWaiting = kickWaiting;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getOs() {
        return os;
    }

    @Override
    public String toString() {
        return "CheckUpdataBean{" +
                "os='" + os + '\'' +
                ", version='" + version + '\'' +
                ", status=" + status +
                ", versionDesc='" + versionDesc + '\'' +
                ", updateVersion='" + updateVersion + '\'' +
                ", updateVersionDesc='" + updateVersionDesc + '\'' +
                '}';
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
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
}
