package com.erban.main.service.agora;

import com.erban.main.config.AgoraConfig;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class AgoraService {
    @Autowired
    private AgoraConfig agoraConfig;

    public BusiResult getChannelKey(String roomId, Long uid) {
        try {
            int ts = (int)(new Date().getTime()/1000);
            int r = new Random().nextInt();
            int expiredTs = 0;
            return new BusiResult(BusiStatus.SUCCESS, DynamicKey5.generateMediaChannelKey(agoraConfig.getAppID(), agoraConfig.getAppCertificate(), roomId, ts, r, uid, expiredTs));
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

}
