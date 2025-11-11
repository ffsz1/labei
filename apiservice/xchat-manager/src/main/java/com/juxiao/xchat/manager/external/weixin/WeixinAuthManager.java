package com.juxiao.xchat.manager.external.weixin;

import com.juxiao.xchat.manager.common.user.vo.WXUserInfoVO;
import com.juxiao.xchat.manager.external.weixin.ret.SnsapiBaseinfoRet;

import java.io.IOException;

public interface WeixinAuthManager {

    /**
     * 获取微信用户对应的openid
     *
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    SnsapiBaseinfoRet getSnsapiBaseinfo(String appid, String secret, String code) throws IOException;

    /**
     * 获取微信accessToken
     *
     * @return
     */
    String getAccessToken() throws IOException;

    String getOpenId(String appid, String secret, String code) throws IOException;

    WXUserInfoVO getUserInfo(String openid, String accessToken);

}
