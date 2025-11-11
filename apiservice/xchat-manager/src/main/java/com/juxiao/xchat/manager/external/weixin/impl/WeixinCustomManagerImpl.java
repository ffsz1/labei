package com.juxiao.xchat.manager.external.weixin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.HttpUtils;
import com.juxiao.xchat.base.utils.PojoUtils;
import com.juxiao.xchat.base.utils.SHA1Utils;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.external.weixin.WeixinAuthManager;
import com.juxiao.xchat.manager.external.weixin.WeixinCustomManager;
import com.juxiao.xchat.manager.external.weixin.bo.WeixinCustomReceiverBO;
import com.juxiao.xchat.manager.external.weixin.conf.WeixinConf;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息服务实现类
 *
 * @class: MessageServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/5/8
 */
@Service
public class WeixinCustomManagerImpl implements WeixinCustomManager {
    private final String token = "hjPALHtBbaWjEghGebYknXIJEuoWAgUN";
    /**
     * 发送客服消息接口
     */
    private final String customUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private WXBizMsgCrypt crypt;
    @Autowired
    private WeixinConf weixinConf;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private WeixinAuthManager authManager;


    @Override
    public String checkWeixinSignature(String signature, String timestamp, String nonce, String echostr) {
        Map<String, String> param = new HashMap<>();
        param.put("timestamp", timestamp);
        param.put("nonce", nonce);
        param.put("token", token);
        String presign = PojoUtils.keyValuePair(param);
        String sign = SHA1Utils.encode(presign);
        if (sign == null || !sign.equalsIgnoreCase(signature)) {
            logger.warn("[ 微信客服消息 ] 签名验证失败，本地字符串:>{}, 生成签名:>{}, 接收签名:>{}", presign, sign, signature);
            return echostr;
        }
        return echostr;
    }

    @Override
    public void receiveWeixinMessage(String encrypt) {
        if (StringUtils.isEmpty(encrypt)) {
            return;
        }

        WXBizMsgCrypt crypt = this.getMsgCrypt();
        if (crypt == null) {
            return;
        }
        String json;
        try {
            json = crypt.decrypt(encrypt);
        } catch (AesException e) {
            return;
        }


        WeixinCustomReceiverBO receiverBo = JSON.parseObject(json, WeixinCustomReceiverBO.class);
        if (StringUtils.isBlank(receiverBo.getFromUserName()) || StringUtils.isBlank(receiverBo.getContent())) {
            return;
        }

//        String msgType = "link";
//        Map<String, Object> data = new HashMap<>();
//        if (receiverBo.getContent().contains("充值")) {
//            data.put("title", "");
//            data.put("description", "点击这里充值，首充有豪礼哦~");
//            data.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0bd2624f8c87128f&redirect_uri=https%3A%2F%2Fwww.pinjin12.com%2Fwx%2Fsnsapi%2Fbaseinfo%2Fget&response_type=code&scope=snsapi_base&state=%2Fmm%2Fwxpay%2Findex.html&connect_redirect=1#wechat_redirect");
//        } else if (receiverBo.getContent().contains("送礼") || receiverBo.getContent().contains("礼物")) {
//            msgType = "text";
//            data.put("content", "");
//        } else if (receiverBo.getContent().contains("") || receiverBo.getContent().contains("下载") || receiverBo.getContent().contains("app")) {
//            data.put("title", "");
//            data.put("description", "，");
//            data.put("url", "https://www.pinjin12.com/mm/download/download.html");
//        } else {
//            return;
//        }
//
//        try {
//            this.sendCustom(receiverBo.getFromUserName(), msgType, data);
//        } catch (Exception e) {
//            logger.error("[ 微信客服消息 ]接收信息错，接收:>{}，异常:", receiverBo.getFromUserName(), e);
//        }
    }


    private void sendCustom(String openid, String msgType, Map<String, Object> content) throws Exception {
        String accessToken = authManager.getAccessToken();
        JSONObject object = new JSONObject();
        object.put("touser", openid);
        object.put("msgtype", msgType);
        object.put(msgType, content);

        long startTime = System.currentTimeMillis();
        String result = HttpUtils.post(customUrl + accessToken, object.toJSONString());
        logger.info("[ 微信客服 ] 发送消息，请求:>{},响应:>{},耗时:>{}", object, result, System.currentTimeMillis() - startTime);
        JSONObject rjson = JSON.parseObject(result);
        if (rjson.containsKey("errcode") && rjson.getIntValue("errcode") != 0) {
            redisManager.del(RedisKey.wx_access_token.getKey());
        }
    }

    private WXBizMsgCrypt getMsgCrypt() {
        if (crypt == null) {
            try {
                crypt = new WXBizMsgCrypt(token, weixinConf.getEncodingAesKey(), weixinConf.getWxappId());
            } catch (AesException e) {
                crypt = null;
            }
        }
        return crypt;
    }
}
