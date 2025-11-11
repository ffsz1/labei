package com.juxiao.xchat.manager.external.netease.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.MD5Utils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.SmsRecordDao;
import com.juxiao.xchat.dao.sysconf.domain.SmsRecordDO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.external.netease.NetEaseSmsManager;
import com.juxiao.xchat.manager.external.netease.conf.CaihConf;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseConf;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseSmsRet;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 网易短信服务
 *
 * @class: CaihSmsManagerImpl.java
 * @author: feimat
 * @date 2018/6/11
 */

@Service("CaihSmsManager")
public class CaihSmsManagerImpl implements NetEaseSmsManager {
    @Resource(name = "jsonHeaders")
    private HttpHeaders jsonHeaders;
    @Autowired
    private CaihConf caihConf;
    @Autowired
    private SmsRecordDao recordDao;
    @Autowired
    private RedisManager redisManager;

    private final Logger logger = LoggerFactory.getLogger(NetEaseSmsManager.class);

    /**
     * @see com.juxiao.xchat.manager.external.netease.NetEaseSmsManager#sendSms(String, String, String, String)
     */
    @Override
    public NetEaseSmsRet sendSms(String mobile, String deviceId, String smsTemplateid, String code) throws Exception {
        String smsCode = code;
        redisManager.set(RedisKey.sms_mobile_code_string.getKey(mobile), smsCode, 2, TimeUnit.MINUTES);

        JSONObject object = new JSONObject();
        object.put("clientid", caihConf.getClientId());
        object.put("password", MD5Utils.encode(caihConf.getPassword()));
        object.put("mobile", mobile);
        object.put("smstype", "4");
        object.put("content", caihConf.content(smsCode));
        object.put("uid", mobile);

        RestTemplate restTemplate = new RestTemplate();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(object.toJSONString(), jsonHeaders);
        String result = restTemplate.postForObject(caihConf.getSmsSendUrl(), entity, String.class);
        long time = System.currentTimeMillis() - startTime;

        logger.info("[ 东信短信 ]发送短信成功，接口:>{},请求:>{},返回:>{},耗时:>{}", caihConf.getSmsSendUrl(), object, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("code") != 0) {
            throw new WebServiceException(ret.getIntValue("code") + ":" + ret.getString("msg"));
        }
        NetEaseSmsRet netRet = new NetEaseSmsRet();
        netRet.setCode(200);
        return netRet;
    }

    @Override
    public NetEaseSmsRet sendTemplateSms(List<String> mobiles, String message, String smsTemplateid) throws Exception {
        return null;
    }

    /**
     * @see com.juxiao.xchat.manager.external.netease.NetEaseSmsManager#verifySmsCode(String, String)
     */
    @Override
    public boolean verifySmsCode(String mobile, String code) throws WebServiceException {
        String redisKey = RedisKey.sms_mobile_code_string.getKey(mobile);
        String smsCode = redisManager.get(redisKey);
        boolean isVerify = StringUtils.isNotBlank(smsCode) && smsCode.equalsIgnoreCase(code);
        if (isVerify) {
            redisManager.del(redisKey);
        }
        return isVerify;
    }

    @Override
    public NetEaseSmsRet sendAlarmSms(String message) throws Exception {
        JSONObject object = new JSONObject();
        object.put("clientid", caihConf.getClientId());
        object.put("password", MD5Utils.encode(caihConf.getPassword()));
        object.put("mobile", "13682306032");
        object.put("smstype", "4");
        object.put("content", message);
        object.put("uid", "13682306032");

        RestTemplate restTemplate = new RestTemplate();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(object.toJSONString(), jsonHeaders);
        String result = restTemplate.postForObject(caihConf.getSmsSendUrl(), entity, String.class);
        long time = System.currentTimeMillis() - startTime;

        logger.info("[ 东信短信 ]发送短信成功，接口:>{},请求:>{},返回:>{},耗时:>{}", caihConf.getSmsSendUrl(), object, result, time);
        JSONObject ret = JSON.parseObject(result);
        if (ret.getIntValue("code") != 0) {
            throw new WebServiceException(ret.getIntValue("code") + ":" + ret.getString("msg"));
        }
        NetEaseSmsRet netRet = new NetEaseSmsRet();
        netRet.setCode(200);
        return netRet;
    }


}
