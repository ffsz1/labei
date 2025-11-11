package com.erban.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AgoraConfig {
    private String appID;
    private String appCertificate;

    public String getAppID() {
        return appID;
    }

    @Value("${agora.app_iD}")
    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppCertificate() {
        return appCertificate;
    }

    @Value("${agora.app_certificate}")
    public void setAppCertificate(String appCertificate) {
        this.appCertificate = appCertificate;
    }
}
