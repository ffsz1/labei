package com.juxiao.xchat.manager.external.weixin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.HttpUtils;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.vo.WXUserInfoVO;
import com.juxiao.xchat.manager.external.weixin.WeixinAuthManager;
import com.juxiao.xchat.manager.external.weixin.conf.WeixinConf;
import com.juxiao.xchat.manager.external.weixin.ret.SnsapiBaseinfoRet;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 微信信息验证接口对接
 *
 * @class: WeixinAuthManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
@Service
public class WeixinAuthManagerImpl implements WeixinAuthManager {

    private final String SNSAPI_BASEINFO_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /**
     * 获取全局accessToken
     */
    private final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";


    private final Logger logger = LoggerFactory.getLogger(WeixinAuthManager.class);
    @Autowired
    private Gson gson;
    @Autowired
    private WeixinConf weixinConf;
    @Autowired
    private RedisManager redisManager;


    /**
     * 获取微信用户对应的openid
     *
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    @Override
    public SnsapiBaseinfoRet getSnsapiBaseinfo(String appid, String secret, String code) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("appid=").append(appid).append("&");
        builder.append("secret=").append(secret).append("&");
        builder.append("code=").append(code).append("&");
        builder.append("&grant_type=authorization_code");
        long starTime = System.currentTimeMillis();
        String data = HttpUtils.get(SNSAPI_BASEINFO_URL, builder.toString());
        long time = System.currentTimeMillis() - starTime;
        logger.info("[ 获取微信openid ] 请求:{}?{},返回:>{},耗时:>{}", SNSAPI_BASEINFO_URL, builder.toString(), data.replaceAll("\\n", ""), time);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create().fromJson(data, SnsapiBaseinfoRet.class);
    }

    @Override
    public String getAccessToken() throws IOException {
        String accessToken = redisManager.get(RedisKey.wx_access_token.getKey());
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        logger.info("获取accessToken-> url->" + ACCESS_TOKEN_URL);
        String param = String.format("appid=%s&secret=%s&grant_type=client_credential", weixinConf.getAppid(), weixinConf.getSecret());
        logger.info("获取accessToken-> param->" + param);
        String data = HttpUtils.get(ACCESS_TOKEN_URL,param);
        logger.info("获取accessToken->" + data);
        JSONObject object = JSON.parseObject(data);
        if (!object.containsKey("access_token")) {
            return null;
        }
        accessToken = object.getString("access_token");
        int expires = object.getIntValue("expires_in");
        redisManager.set(RedisKey.wx_access_token.getKey(), accessToken, expires - 10, TimeUnit.SECONDS);
        return accessToken;
    }

    @Override
    public String getOpenId(String appid, String secret, String code) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("appid=").append(appid).append("&");
        builder.append("secret=").append(secret).append("&");
        builder.append("code=").append(code).append("&");
        builder.append("&grant_type=authorization_code");
        long starTime = System.currentTimeMillis();
        String data = HttpUtils.get(SNSAPI_BASEINFO_URL, builder.toString());
        long time = System.currentTimeMillis() - starTime;
        logger.info("[ 获取微信getOpenId ] 请求:{}?{},返回:>{},耗时:>{}", SNSAPI_BASEINFO_URL, builder.toString(), data.replaceAll("\\n", ""), time);
        SnsapiBaseinfoRet baseinfoRet = JSON.parseObject(data,SnsapiBaseinfoRet.class);
        return baseinfoRet.getOpenid();
    }

    @Override
    public WXUserInfoVO getUserInfo(String openid, String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        StringBuilder builder = new StringBuilder();
        builder.append("access_token=").append(accessToken).append("&");
        builder.append("openid=").append(openid).append("&");
        builder.append("lang=").append("zh_CN");
        String result = null;
        logger.info ( "==>获取微信用户参数：" + builder.toString() );
        try {
            result = HttpUtils.get(url, builder.toString());
        } catch (IOException e) {
            logger.error("获取微信用户信息失败,异常信息:{}",e);
        }
        logger.info ( "==>获取微信用户返回结果：" + result );
        WXUserInfoVO userInfo = new Gson ().fromJson ( result,WXUserInfoVO.class );
        return userInfo;
    }

}
