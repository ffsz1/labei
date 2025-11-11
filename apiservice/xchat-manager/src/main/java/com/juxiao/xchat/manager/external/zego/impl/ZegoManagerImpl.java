package com.juxiao.xchat.manager.external.zego.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.MD5Utils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.external.zego.ZegoManager;
import com.juxiao.xchat.manager.external.zego.conf.ZegoConf;
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
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ZegoManagerImpl implements ZegoManager {
    private final long expire = 3600;
    private final Logger logger = LoggerFactory.getLogger(ZegoManager.class);

    @Resource(name = "jsonHeaders")
    private HttpHeaders jsonHeaders;
    @Autowired
    private ZegoConf zegoConf;
    @Autowired
    private RedisManager redisManager;


    @Override
    public String generateThirdToken(Long roomUid, Long uid) throws Exception {
        //>3.生成要加密的third_token json字符串
        JSONObject object = new JSONObject();
        object.put("app_id", zegoConf.getAppId());
        object.put("timeout", System.currentTimeMillis() + expire);
        object.put("nonce", new Random().nextLong());
        object.put("id_name", uid);

        //>4.对json 进行 AES 加密，使用模式: CBC/PKCS5Padding
        Long timestamp = System.currentTimeMillis() / 1000 + expire;
        byte[] serverSecret = zegoConf.getSecret().getBytes(); // Secret联系zego技术支持

        JSONObject encryptResult = new JSONObject();
        encryptResult.put("app_id", zegoConf.getAppId()); // 数值型, appid联系zego技术支持
        encryptResult.put("timeout", timestamp); // 数值型, 注意必须是当前时间戳(秒)加超时时间(秒)
        encryptResult.put("nonce", RandomStringUtils.randomNumeric(8)); // 随机数,须为数值型
        encryptResult.put("id_name", "hsx2117");// 字符串,id_name必须跟setUser的userid相同
        byte[] encryptByte = ZegoThirdTokenUtils.encrypt(encryptResult.toString(), serverSecret);
        return "01" + new String(encryptByte, "utf-8");//最后结果须加version("01")为前缀
    }

    @Override
    public String getAccessToken() throws Exception {
        String accessToken = redisManager.get(RedisKey.zego_access_token.getKey());
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }

        String lockVal = redisManager.lock(RedisKey.zego_access_token.getKey("lock"), 10000);
        if (StringUtils.isBlank(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            long currentTime = System.currentTimeMillis(); //获取当前unix时间戳
            long expiredTime = currentTime + 7200; //过期unix时间戳
            long appId = Long.valueOf(zegoConf.getAppId());
            String nonce = RandomStringUtils.randomAlphanumeric(16);

            String hashString = new StringBuilder().append(appId).append(zegoConf.getSecret()).append(nonce).append(expiredTime).toString();
            JSONObject tokeninfo = new JSONObject();
            tokeninfo.put("ver", 1);
            tokeninfo.put("hash", MD5Utils.getMD5(hashString));
            tokeninfo.put("nonce", nonce);
            tokeninfo.put("expired", expiredTime);

            String token = Base64.getEncoder().encodeToString(tokeninfo.toString().getBytes("UTF-8"));
            RestTemplate restTemplate = new RestTemplate();
            long startTime = System.currentTimeMillis();
            JSONObject json = new JSONObject();
            json.put("version", 1);
            json.put("seq", currentTime / 1000);
            json.put("app_id", appId);
            json.put("biz_type", 0);
            json.put("token", token);

            HttpEntity<String> entity = new HttpEntity<>(json.toString(), jsonHeaders);
            String result = restTemplate.postForObject(zegoConf.getTokenUrl(), entity, String.class);
            long time = System.currentTimeMillis() - startTime;
            logger.info("[ 即构接口 ] 获取AccessToken，接口:>{},请求:>{},返回:>{},耗时:>{}", zegoConf.getTokenUrl(), json, result, time);

            JSONObject ret = JSON.parseObject(result);
            if (ret.getIntValue("code") != 0) {
                throw new WebServiceException(ret.getIntValue("code") + ":" + ret.getString("message"));
            }

            JSONObject data = ret.getJSONObject("data");
            redisManager.set(RedisKey.zego_access_token.getKey(), data.getString("access_token"), data.getIntValue("expires_in") - 10, TimeUnit.SECONDS);
            return data.getString("access_token");
        } finally {
            redisManager.unlock(RedisKey.zego_access_token.getKey("lock"), lockVal);
        }

    }


}