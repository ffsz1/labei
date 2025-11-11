package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;

import java.util.Map;

public interface AccountsService {

    /**
     * 获取短信验证码
     * @param ip
     * @param uid
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    WebServiceMessage getSmsCode(String ip, Long uid, String deviceId, String imei, String os, String osversion, String channel, String appVersion, String model) throws Exception;


    /**
     * 校验手机验证码
     * @param code
     * @param uid
     * @return
     * @throws WebServiceException
     */
    void validateCode(Long uid,String code) throws WebServiceException;

    /**
     * 解绑第三方
     * @param uid
     * @param type
     * @return
     * @throws WebServiceException
     */
    WebServiceMessage untiedThird(Long uid,int type) throws WebServiceException;

    /**
     * 绑定第三方
     * @param uid
     * @param openId
     * @param unionId
     * @param accessToken
     * @param type
     * @param app
     * @return
     * @throws WebServiceException
     */
    WebServiceMessage bindThird(Long uid, String openId, String unionId, String accessToken, int type, String app,String os)throws WebServiceException;

    WebServiceMessage getWxUserInfo(Long uid, String openId, String unionId, String accessToken, String appid, String os) throws WebServiceException;

    WebServiceMessage checkWxInfo(Long uid, String openId, String unionId, String accessToken, String appid, String os)throws WebServiceException;


    WebServiceMessage getAccountSmsCode(Long uid, String phone) throws Exception;

    WebServiceMessage getSmsByCode(String phone)throws Exception;

    /**
     * 获取第三方绑定的昵称
     * @param uid
     * @return
     */
    Map<String, String> getBindNick(Long uid);
}
