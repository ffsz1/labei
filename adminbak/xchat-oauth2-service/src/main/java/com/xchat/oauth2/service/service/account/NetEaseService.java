package com.xchat.oauth2.service.service.account;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.google.gson.Gson;
import com.xchat.common.netease.neteaseacc.NetEaseBaseClient;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.netease.neteaseacc.result.SmsRet;
import com.xchat.common.netease.neteaseacc.result.TokenRet;
import com.xchat.common.netease.util.NetEaseConstant;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.core.encoder.MD5Utils;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuguofu on 2017/5/9.
 */
@Service
public class NetEaseService {
    private static final Logger logger = LoggerFactory.getLogger(NetEaseService.class);
    @Autowired
    private JedisService jedisService;

    @Resource(name = "jsonHeaders")
    private HttpHeaders jsonHeaders;
    @Autowired
    private CaihConf caihConf;

    /**
     * 创建网易云账号
     *
     * @param accid
     * @param token
     * @param name
     * @param props
     * @param icon
     * @return
     * @throws Exception
     */
    public TokenRet createNetEaseAcc(String accid, String token, String props, String name, String icon) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.AccUrl.create;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("token", token);
        param.put("name", name);
        param.put("props", props);
        param.put("icon", icon);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("注册网易云账号,accid=" + accid + "&token=" + token + "&props=" + props + "&name=" + name + "&icon=" + icon + "|&result=" + result);
        Gson gson = new Gson();
        TokenRet tokenRet = gson.fromJson(result, TokenRet.class);
        return tokenRet;
    }

    /**
     * 创建完网易云账号之后，更新
     * 特别注意ex是业务方uid，不能再进行修改
     *
     * @param accid
     * @param ex
     * @return
     * @throws Exception
     */
    public TokenRet updateNetEaseAcc(String accid, String ex) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.UserUrl.updateUinfo;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("ex", ex);
        logger.info("updateNetEaseAcc:accid=" + accid + "&ex=" + ex);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        Gson gson = new Gson();
        TokenRet tokenRet = gson.fromJson(result, TokenRet.class);
        return tokenRet;
    }

    public TokenRet createNetEaseAcc(String accid, String token, String props) throws Exception {
        TokenRet tokenRet = createNetEaseAcc(accid, token, props, "", "");
        return tokenRet;
    }

    public TokenRet getNeteaseUsers(String accids) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.AccUrl.get;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accids", accids);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        Gson gson = new Gson();
        TokenRet TokenRet = new TokenRet();
        return TokenRet;
    }

    public TokenRet refreshToken(String accid) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.AccUrl.refreshToken;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        Gson gson = new Gson();
        TokenRet tokenRet = gson.fromJson(result, TokenRet.class);
        logger.info("token:{}", result);
        return tokenRet;
    }


    public SmsRet sendSms(String mobile, String deviceId, int templateid) throws Exception {
        String smsCode = RandomStringUtils.randomNumeric(5);;
//        String smsCode = "12345";
        jedisService.set(RedisKey.sms_mobile_code_string.getKey(mobile), smsCode, caihConf.getCodeExpires());

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
        SmsRet smsRet = new SmsRet();
        if (ret.getIntValue("code") != 0) {
            smsRet.setCode(500);
            smsRet.setMsg(result);
        } else {
            smsRet.setCode(200);
        }
        return smsRet;

//        String url = NetEaseConstant.smsBasicUrl + NetEaseConstant.SmsUrl.sendSms;
//        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url,true);
//        Map<String, Object> param = Maps.newHashMap();
//        param.put("mobile", mobile);
//        param.put("deviceId", deviceId);
//        param.put("templateid", templateid);
//        param.put("codeLen", NetEaseConstant.smsCodeLen);
//        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
//        Gson gson = new Gson();
//        SmsRet smsRet = gson.fromJson(result, SmsRet.class);
//        return smsRet;
    }

    public BaseNetEaseRet smsVerify(String mobile, String code) throws Exception {
        String redisKey = RedisKey.sms_mobile_code_string.getKey(mobile);
        String smsCode = jedisService.get(redisKey);
        boolean isVerify = StringUtils.isNotBlank(smsCode) && smsCode.equalsIgnoreCase(code);
        BaseNetEaseRet baseNetEaseRet = new BaseNetEaseRet();
        if (isVerify) {
            jedisService.del(redisKey);
            baseNetEaseRet.setCode(200);
        } else {
            baseNetEaseRet.setCode(500);
        }
        return baseNetEaseRet;
//        long verifyCount = jedisService.incr(RedisKey.sms_code_number_verify.getKey(mobile), 120);
//        if (verifyCount > 10) {
//            BaseNetEaseRet baseNetEaseRet = new BaseNetEaseRet();
//            logger.info("[ 网易短信接口 ] 短信验证过于频繁 ，mobile:>{}", mobile);
//            throw new Exception("短信验证过于频繁!");
//        }
//
//        String url = NetEaseConstant.smsBasicUrl + NetEaseConstant.SmsUrl.smsVerify;
//        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url, true);
//        Map<String, Object> param = Maps.newHashMap();
//        param.put("mobile", mobile);
//        param.put("code", code);
//        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
//        Gson gson = new Gson();
//        BaseNetEaseRet baseNetEaseRet = gson.fromJson(result, BaseNetEaseRet.class);
//        return baseNetEaseRet;
    }

    public void block(String accid, String needkick) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.AccUrl.block;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("needkick", needkick);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("block accid=" + accid + "&result=" + result);
    }

    public void unblock(String accid) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.AccUrl.unblock;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("unblock accid=" + accid + "&result=" + result);
    }


    public BaseNetEaseRet updateUserInfo(String accid,String name,String icon)throws Exception{
        long startTime = System.currentTimeMillis();
        Gson gson = new Gson();
        String url= NetEaseConstant.basicUrl+ NetEaseConstant.UserUrl.updateUinfo;
        NetEaseBaseClient netEaseBaseClient=new NetEaseBaseClient(url);
        Map<String,Object> param= Maps.newHashMap();
        param.put("accid",accid);
        param.put("name",name);
        param.put("icon", icon);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("[请求云信 updateUinfo]接口:{},请求参数:{},返回响应数据:{},耗时:{}/ms",NetEaseConstant.UserUrl.updateUinfo,gson.toJson(param),result,(System.currentTimeMillis() - startTime));
        return gson.fromJson(result,BaseNetEaseRet.class);
    }



    public SmsRet sendSmsV2(String mobile,String redisKey, Integer expire, String deviceId, int templateid) throws Exception {
        String smsCode = RandomStringUtils.randomNumeric(5);
        jedisService.set(redisKey, smsCode, expire == null ? caihConf.getCodeExpires(): expire);

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
        SmsRet smsRet = new SmsRet();
        if (ret.getIntValue("code") != 0) {
            smsRet.setCode(500);
            smsRet.setMsg(result);
        } else {
            smsRet.setCode(200);
        }
        return smsRet;

    }
    public BaseNetEaseRet smsVerifyV2(String redisKey, String code) throws Exception {
        String smsCode = jedisService.get(redisKey);
        boolean isVerify = StringUtils.isNotBlank(smsCode) && smsCode.equalsIgnoreCase(code);
        BaseNetEaseRet baseNetEaseRet = new BaseNetEaseRet();
        if (isVerify) {
            jedisService.del(redisKey);
            baseNetEaseRet.setCode(200);
        } else {
            baseNetEaseRet.setCode(500);
        }
        return baseNetEaseRet;

    }
    
    public static void main(String args[]) throws Exception {

        NetEaseService NetEaseService = new NetEaseService();
//        List<String> accidsList = Lists.newArrayList();
//        accidsList.add("90024171");
//        Gson gson = new Gson();
//        NetEaseService.getNeteaseUsers(gson.toJson(accidsList));
//        String uid="{'uid':'12345679'}";
//        NetEaseService.createNetEaseAcc("tttt12345","","");
//        NetEaseService.updateNetEaseAcc("tttt12345","123456790");
//        String url= NetEaseConstant.basicUrl+"/user/create.action";
//        NetEaseBaseClient netEaseBaseClient=new NetEaseBaseClient(url);
//        Map<String,Object> param= Maps.newHashMap();
//        param.put("accids","[900089]");
//
//        String url= NetEaseConstant.basicUrl+"/user/getUinfos.action";
//        NetEaseBaseClient netEaseBaseClient=new NetEaseBaseClient(url);
//        String result=netEaseBaseClient.buildHttpPostParam(param).executePost();
//        System.out.println(result);
        NetEaseService.sendSms("13682306032", "", NetEaseConstant.smsTemplateid);
//        NetEaseService.smsVerify("11933986969","90416");

    }


}
