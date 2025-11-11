package com.juxiao.xchat.service.api.charge.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeixinUnifiedOrderVO {
    private String outTradeNo;
    private String appid;
    private String sign_type;
    private String nonce_str;
    private String prepay_id;
    private String timestamp;
    private String sign;
    private String nick;
    private Long erban_no;

    public String takeOutTradeNo() {
        return outTradeNo;
    }

    public String getOutTradeNo() {
        return null;
    }
}

