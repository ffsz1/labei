package com.juxiao.xchat.manager.external.weixin.ret;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 通过code换取网页授权返回对象
 *
 * @class: SnsapiBaseinfoRet.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
@Getter
@Setter
@ToString
public class SnsapiBaseinfoRet {
    /**
     * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     */
    @JSONField(name = "access_token")
    private String accessToken;
    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    @JSONField(name = "expires_in")
    private Integer expiresIn;
    /**
     * 用户刷新access_token
     */
    @JSONField(name = "refresh_token")
    private String refreshToken;
    /**
     * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
     */
    @JSONField(name = "openid")
    private String openid;
    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    @JSONField(name = "scope")
    private String scope;
}
