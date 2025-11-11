package com.juxiao.xchat.manager.external.agora;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class AgoraService {
    @Autowired
    private AgoraConfig agoraConfig;

    public WebServiceMessage getChannelKey(String roomId, Long uid) {
        try {
            int ts = (int)(new Date().getTime()/1000);
            int r = new Random().nextInt();
            int expiredTs = 0;
            String result = DynamicKey5.generateMediaChannelKey(agoraConfig.getAppId(), agoraConfig.getAppCertificate(), roomId, ts, r, uid, expiredTs);
            return WebServiceMessage.success(result);
        } catch (Exception e) {
        }
        return WebServiceMessage.failure(WebServiceCode.SERVER_ERROR);
    }

}
