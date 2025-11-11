package com.juxiao.xchat.manager.external.joinpay;

import com.juxiao.xchat.manager.external.joinpay.ret.JoinpayReciverRet;
import com.juxiao.xchat.manager.external.joinpay.ret.JoinpayRet;

public interface JoinpayManager {

    JoinpayRet createCharge(String chargeRecordId, int amount, String subject, String body, String clientIp, String successUrl, String payChannel, Long uid,String openId) throws Exception;

    JoinpayReciverRet retrieve(String merchantNo, String orderNo, String hmac) throws Exception;

}
