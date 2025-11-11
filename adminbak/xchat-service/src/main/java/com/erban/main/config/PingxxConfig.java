package com.erban.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by PaperCut on 2018/2/3.
 */
@Component
public class PingxxConfig {
    public String apiKey;
    public String appId;
    public String apiIdSecond;
    public String publicKey;
    public String privateKeyPath;
    public String apiIdThree;

    @Value("${pingxxApiKey}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Value("${pingxxApiIdSecond}")
    public void setApiIdSecond(String apiIdSecond) {
        this.apiIdSecond = apiIdSecond;
    }

    public String getApiIdSecond() {
        return apiIdSecond;
    }

    @Value("${pingxxApiId}")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    @Value("${pingxxPublicKey}")
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    @Value("${pingxxPrivateKeyPath}")
    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public String getApiIdThree() {
        return apiIdThree;
    }
    @Value("${pingxxApiIdThree}")
    public void setApiIdThree(String apiIdThree) {
        this.apiIdThree = apiIdThree;
    }
}
