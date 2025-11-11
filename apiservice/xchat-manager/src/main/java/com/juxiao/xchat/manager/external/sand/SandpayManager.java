package com.juxiao.xchat.manager.external.sand;


import com.juxiao.xchat.manager.external.joinpay.ret.JoinpayRet;

import java.util.Map;

public interface  SandpayManager {

    String createCharge(String chargeRecordId, int amount, String subject, String clientIp, Long uid) throws Exception;

}
