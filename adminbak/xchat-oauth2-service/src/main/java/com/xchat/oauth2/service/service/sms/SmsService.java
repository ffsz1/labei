package com.xchat.oauth2.service.service.sms;

import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.oauth2.service.service.account.NetEaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liuguofu on 2017/4/28.
 */
public class SmsService {
    @Autowired
    private NetEaseService netEaseService;

    public static boolean sendSms(String phone,String smsCode){
        return true;
    }

    public boolean verifySmsCodeByNetEase(String mobile, String code) throws Exception {
        BaseNetEaseRet baseNetEaseRet = netEaseService.smsVerify(mobile, code);
        if (baseNetEaseRet.getCode() == 200) {
            return true;
        } else {
            return false;
        }
    }
}
