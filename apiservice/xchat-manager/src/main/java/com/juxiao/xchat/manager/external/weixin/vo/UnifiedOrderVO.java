package com.juxiao.xchat.manager.external.weixin.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnifiedOrderVO {
//    private String appid;
    private String sign_type;
    private String nonce_str;
    private String prepay_id;
    private String timestamp;
    private String sign;
}

