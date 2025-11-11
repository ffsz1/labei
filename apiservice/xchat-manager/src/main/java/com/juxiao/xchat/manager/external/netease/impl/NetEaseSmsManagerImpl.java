package com.juxiao.xchat.manager.external.netease.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.external.netease.NetEaseSmsManager;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseConf;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseSmsRet;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 网易短信服务
 *
 * @class: NetEaseSmsManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */

@Service("NetEaseSmsManager")
public class NetEaseSmsManagerImpl implements NetEaseSmsManager {
    public static final int SMS_CODE_LENGTH = 5;
    private static final String SEND_SMS_URL = "https://api.netease.im/sms/sendcode.action";
    private static final String VERIFY_SMS_CODE_URL = "https://api.netease.im/sms/verifycode.action";
    private static final String SEND_TEMPLATE_SMS_URL = "https://api.netease.im/sms/sendtemplate.action";
    //    private static final String SEND_BATCH_MSG = "https://api.netease.im/nimserver/msg/sendBatchMsg.action";
    private final List<String> MOBILES = Arrays.asList("13119531240", "13232951695", "18520124838", "18011710466");
    private final Logger logger = LoggerFactory.getLogger(NetEaseSmsManager.class);

    @Autowired
    private Gson gson;
    @Autowired
    private NetEaseConf neteaseConf;
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private RedisManager redisManager;

    /**
     * @see com.juxiao.xchat.manager.external.netease.NetEaseSmsManager#sendSms(String, String, String, String)
     */
    @Override
    public NetEaseSmsRet sendSms(String mobile, String deviceId, String smsTemplateid, String code) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(SEND_SMS_URL, neteaseConf.getSmsAppKey(), neteaseConf.getSmsAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("mobile", mobile);
        param.put("deviceId", deviceId);
        param.put("templateid", StringUtils.isBlank(smsTemplateid) ? neteaseConf.getSmsTemplateid() : smsTemplateid);
        if (StringUtils.isBlank(code)) {
            param.put("codeLen", SMS_CODE_LENGTH);
        } else {
            param.put("authCode", code);
        }
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 网易短信接口 ]请求:>{}，返回:>{}，耗时:>{}", SEND_SMS_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseSmsRet.class);
    }

    @Override
    public NetEaseSmsRet sendTemplateSms(List<String> mobiles, String message, String smsTemplateid) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(SEND_TEMPLATE_SMS_URL, neteaseConf.getSmsAppKey(), neteaseConf.getSmsAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("mobiles", mobiles == null ? gson.toJson(MOBILES) : gson.toJson(mobiles));
        param.put("templateid", smsTemplateid == null ? neteaseConf.getAlarmSmsTemplateid() : smsTemplateid);
        List<String> params = Lists.newArrayList();
        params.add(message);
        param.put("params", gson.toJson(params));
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 网易模板短信接口 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", SEND_TEMPLATE_SMS_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseSmsRet.class);
    }

    /**
     * @see com.juxiao.xchat.manager.external.netease.NetEaseSmsManager#verifySmsCode(String, String)
     */
    @Override
    public boolean verifySmsCode(String mobile, String code) throws WebServiceException {
        long startTime = System.currentTimeMillis();
        long verifyCount = redisManager.incrByTime(RedisKey.sms_code_number_verify.getKey(mobile), 120);
        if (verifyCount > 10) {
            logger.info("[ 网易短信接口 ] 短信验证过于频繁 ，耗时:>{}", (System.currentTimeMillis() - startTime));
            throw new WebServiceException("短信验证过于频繁!");
        }

        NetEaseClient client = new NetEaseClient(VERIFY_SMS_CODE_URL, neteaseConf.getSmsAppKey(), neteaseConf.getSmsAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("mobile", mobile);
        param.put("code", code);
        String result;
        try {
            result = client.buildHttpPostParam(param).executePost();
        } catch (Exception e) {
            logger.error("[ 网易短信接口 ] 验证短信码请求异常，请求:{}，错误信息：", param, e);
            return false;
        }
        NetEaseRet ret = gson.fromJson(result, NetEaseRet.class);
        logger.info("[ 网易短信接口 ]请求:>{}，返回:>{}，耗时:>{}", VERIFY_SMS_CODE_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return ret != null && ret.getCode() == 200;
    }

    @Override
    public NetEaseSmsRet sendAlarmSms(String message) throws Exception {
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return null;
        }

        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(SEND_TEMPLATE_SMS_URL, neteaseConf.getSmsAppKey(), neteaseConf.getSmsAppSecret());
        List<String> params = Lists.newArrayList(message);
        Map<String, Object> param = Maps.newHashMap();
        param.put("mobiles", gson.toJson(MOBILES));
        param.put("templateid", neteaseConf.getAlarmSmsTemplateid());
        param.put("params", gson.toJson(params));

        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 网易模板短信接口 ]请求:>{}，返回:>{}，耗时:>{}", SEND_TEMPLATE_SMS_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseSmsRet.class);
    }


}
