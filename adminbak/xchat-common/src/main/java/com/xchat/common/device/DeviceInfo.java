package com.xchat.common.device;

/**
 * Created by liuguofu on 2017/10/21.
 */
public class DeviceInfo {
    private Long shareUid;
    private String os;
    private String osVersion;
    private String appid;
    private String ispType;
    private String netType;
    private String model;
    private String appVersion;
    private String imei;
    private String deviceId;
    private String channel;
    private String linkedmeChannel;
    private boolean isWxapp = false;

    public boolean isWxapp() {
        return isWxapp;
    }

    public void setWxapp(boolean wxapp) {
        isWxapp = wxapp;
    }

    public Long getShareUid() {
        return shareUid;
    }

    public void setShareUid(Long shareUid) {
        this.shareUid = shareUid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOs() {
        return os;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getIspType() {
        return ispType;
    }

    public void setIspType(String ispType) {
        this.ispType = ispType;
    }

    public String getNetType() {
        return netType;
    }

    public String getLinkedmeChannel() {
        return linkedmeChannel;
    }

    public void setLinkedmeChannel(String linkedmeChannel) {
        this.linkedmeChannel = linkedmeChannel;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "os='" + os + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", appid='" + appid + '\'' +
                ", ispType='" + ispType + '\'' +
                ", netType='" + netType + '\'' +
                ", model='" + model + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", imei='" + imei + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", channel='" + channel + '\'' +
                ", linkedmeChannel='" + linkedmeChannel + '\'' +
                '}';
    }
}
