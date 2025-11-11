package com.juxiao.xchat.manager.external.joinpay.ret;

import lombok.Data;

@Data
public class AppJoinpayRet {
    private int code;
    private String appId;
    private String partnerId;
    private String prepayId;
    private String timeStamp;
    private String nonceStr;
    private String paySign;
    private String hmac;
}
