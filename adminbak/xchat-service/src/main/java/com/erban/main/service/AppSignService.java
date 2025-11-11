//package com.erban.main.service;
//
//import com.erban.main.mybatismapper.AppSignMapper;
//import com.xchat.common.redis.RedisKey;
//import com.xchat.oauth2.service.service.JedisService;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.util.TextUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.TreeMap;
//
//@Service
//public class AppSignService {
//    @Autowired
//    private AppSignMapper signMapper;
//    @Autowired
//    private JedisService jedisService;
//
//    public String getPreSign(String appVersion, String url, Map<String, String> params) {
//        String signKey = this.getAppSignKey(appVersion);
//        Map<String, String> paramsMap = url2Map(url);
//        if (params != null) {
//            paramsMap.putAll(params);
//        }
//
//        StringBuffer preSign = new StringBuffer();
//        if (paramsMap != null && paramsMap.size() > 0) {
//            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
//                String str = entry.getKey() + "=" + entry.getValue();
//                preSign.append(str);
//            }
//        }
//
//        preSign.append(signKey);
//        return preSign.toString();
//    }
//
//    private String getAppSignKey(String appVersion) {
//        String signKey = jedisService.hget(RedisKey.app_sign_key.getKey(), appVersion);
//        if (StringUtils.isNotBlank(signKey)) {
//            return signKey;
//        }
//
//        signKey = signMapper.getAppSignKey(appVersion);
//        if (StringUtils.isNotBlank(signKey)) {
//            jedisService.hset(RedisKey.app_sign_key.getKey(), appVersion, signKey);
//        }
//        return signKey;
//    }
//
//    private Map<String, String> url2Map(String param) {
//        Map<String, String> map = new TreeMap<>(String::compareTo);
//        if (TextUtils.isEmpty(param)) {
//            return map;
//        }
//
//        String[] urlparams = param.split("\\?");
//        if (urlparams != null && urlparams.length == 2) {
//            param = urlparams[1];
//        } else {
//            return map;
//        }
//
//        String[] params = param.split("&");
//        for (int i = 0; i < params.length; i++) {
//            String[] p = params[i].split("=");
//            if (p.length == 2) {
//                map.put(p[0], p[1]);
//            }
//        }
//        return map;
//    }
//}
