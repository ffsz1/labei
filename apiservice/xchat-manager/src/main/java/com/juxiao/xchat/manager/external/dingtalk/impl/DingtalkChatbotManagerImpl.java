package com.juxiao.xchat.manager.external.dingtalk.impl;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.external.dingtalk.DingtalkChatbotManager;
import com.juxiao.xchat.manager.external.dingtalk.bo.DingtalkMessageBO;
import com.juxiao.xchat.manager.external.dingtalk.ret.DingtalkChatbotRet;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chris
 * @Title: 钉钉机器人发送消息接口实现
 * @date 2018/10/8
 * @time 10:03
 */
@Service
public class DingtalkChatbotManagerImpl implements DingtalkChatbotManager {

    private final Logger logger = LoggerFactory.getLogger(DingtalkChatbotManager.class);
    @Autowired
    private SystemConf systemConf;

    /**
     * 获取httpclient
     *
     * @return
     */
    private HttpClient getHttpclient() {
        return HttpClients.createDefault();
    }

    /**
     * @param webhook 机器人url
     * @param message 发送的消息
     * @return
     */
    @Override
    public DingtalkChatbotRet send(String webhook, DingtalkMessageBO message) {
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return new DingtalkChatbotRet();
        }

        String body = message.toJsonString();
        try {
            HttpClient httpclient = getHttpclient();
            HttpPost httppost = new HttpPost(webhook);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = new StringEntity(body, "utf-8");
            httppost.setEntity(se);
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("[ 发送预警信息 ] 发送失败，url:>{}，请求内容:>{},响应码:>{}", webhook, body, response.getStatusLine().getStatusCode());
                return new DingtalkChatbotRet();
            }

            String result = EntityUtils.toString(response.getEntity());
            logger.info("[ 发送预警信息 ] 发送成功，webhook:>{},请求内容:>{},返回:>{}", webhook, body, result);


            JSONObject obj = JSONObject.parseObject(result);
            Integer errcode = obj.getInteger("errcode");
            return new DingtalkChatbotRet(errcode.equals(0), errcode, obj.getString("errmsg"));
        } catch (Exception e) {
            logger.error("[ 发送预警信息 ] 发送失败：", e);
            return new DingtalkChatbotRet();
        }


    }
}
