package com.xchat.oauth2.service.service.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CaihConf {
    private  String clientId;

    private  String smsSendUrl;

    private  String password;

    private  String smsTemplate;

    private  int deviceIdDayLimit;

    private  int uidDayLimit;

    private  int codeExpires;

    public String content(String smsCode) {
        return String.format(smsTemplate, smsCode);
    }

    public String getClientId() {
        return clientId;
    }

    @Value("${sms.caih.clientId}")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSmsSendUrl() {
        return smsSendUrl;
    }

    @Value("${sms.caih.smsSendUrl}")
    public void setSmsSendUrl(String smsSendUrl) {
        this.smsSendUrl = smsSendUrl;
    }

    public String getPassword() {
        return password;
    }

    @Value("${sms.caih.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }

    @Value("${sms.caih.smsTemplate}")
    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate;
    }

    public int getDeviceIdDayLimit() {
        return deviceIdDayLimit;
    }

    @Value("${sms.caih.deviceIdDayLimit}")
    public void setDeviceIdDayLimit(int deviceIdDayLimit) {
        this.deviceIdDayLimit = deviceIdDayLimit;
    }

    public int getUidDayLimit() {
        return uidDayLimit;
    }

    @Value("${sms.caih.uidDayLimit}")
    public void setUidDayLimit(int uidDayLimit) {
        this.uidDayLimit = uidDayLimit;
    }

    public int getCodeExpires() {
        return codeExpires;
    }

    @Value("${sms.caih.codeExpires}")
    public void setCodeExpires(int codeExpires) {
        this.codeExpires = codeExpires;
    }
}
