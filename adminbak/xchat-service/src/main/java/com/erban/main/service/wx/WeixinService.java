//package com.erban.main.service.wx;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.erban.main.config.WxConfig;
//import com.xchat.common.result.BusiResult;
//import com.xchat.common.status.BusiStatus;
//import com.xchat.common.utils.HttpUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
///**
// * @class: WeixinAuthService.java
// * @author: chenjunsheng
// * @date 2018/6/29
// */
//@Service
//public class WeixinService {
//
//    private static final String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
//
//    private static final String WX_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";
//
//    private final Logger logger = LoggerFactory.getLogger(WeixinService.class);
//
//    public BusiResult<JSONObject> wxpubLogin(String code, String state) {
//        if (StringUtils.isBlank(code) || StringUtils.isBlank(state)) {
//            return new BusiResult<>(BusiStatus.PARAMETERILLEGAL);
//        }
//
//        JSONObject userinfo;
//        try {
//            JSONObject accessToken = this.getAccessToken(code);
//            userinfo = this.getUserinfo(accessToken.getString("access_token"), accessToken.getString("openid"));
//            if (userinfo.containsKey("errcode")) {
//                return new BusiResult<>(BusiStatus.SERVERERROR, "微信返回：" + userinfo.getString("errcode") + "，" + userinfo.getString("errmsg"), null);
//            }
//            return new BusiResult<>(BusiStatus.SUCCESS, userinfo);
//        } catch (Exception e) {
//            return new BusiResult<>(BusiStatus.SERVERERROR, "网络繁忙，请稍后重试", null);
//        }
//    }
//
//    public JSONObject getAccessToken(String code) throws Exception {
//        StringBuilder params = new StringBuilder(WX_ACCESS_TOKEN_URL);
//        params.append("?appid=").append(WxConfig.appId);
//        params.append("&secret=").append(WxConfig.appSecret);
//        params.append("&code=").append(code);
//        params.append("&grant_type=authorization_code");
//        long startTime = System.currentTimeMillis();
//        String data = HttpUtils.executeGet(params.toString());
//        logger.info("[ 微信AccessToken ] 请求:{},返回:>{},耗时:>{}", params, data, (System.currentTimeMillis() - startTime));
//        return JSON.parseObject(data);
//    }
//
//    public JSONObject getUserinfo(String accessToken, String openId) throws Exception {
//        StringBuilder params = new StringBuilder(WX_USERINFO_URL);
//        params.append("?access_token=").append(accessToken);
//        params.append("&openid=").append(openId);
//        params.append("&lang=zh_CN");
//        long startTime = System.currentTimeMillis();
//        String data = HttpUtils.executeGet(params.toString());
//        logger.info("[ 微信获取用户信息 ] 请求:{},返回:>{},耗时:>{}", params, data, (System.currentTimeMillis() - startTime));
//        return JSON.parseObject(data);
//    }
//}
