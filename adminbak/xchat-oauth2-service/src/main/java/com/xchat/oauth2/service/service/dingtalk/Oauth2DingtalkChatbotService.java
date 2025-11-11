package com.xchat.oauth2.service.service.dingtalk;

import com.alibaba.fastjson.JSONObject;
import com.xchat.oauth2.service.service.dingtalk.bo.Oauth2DingtalkMessageBO;
import com.xchat.oauth2.service.service.dingtalk.ret.Oauth2DingtalkChatbotRet;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author chris
 * @Title: 钉钉机器人发送消息接口实现
 * @date 2018/10/8
 * @time 10:03
 */

@Service
public class Oauth2DingtalkChatbotService {
    private final String webhook = "https://oapi.dingtalk.com/robot/send?access_token=9601c29aa3173d28571abc2cf29cbda91d4fa9046057d3f032054cbb325de072";
    private final Logger logger = LoggerFactory.getLogger(Oauth2DingtalkChatbotService.class);

//    @Value("${admin.env}")
//    private String env;
    /**
     * 获取httpclient
     *
     * @return
     */
    private HttpClient getHttpclient() {
        return HttpClients.createDefault();
    }

    /**
     * @param message 发送的消息
     * @return
     */
    public Oauth2DingtalkChatbotRet send(Oauth2DingtalkMessageBO message) {
//        if (!"prod".equalsIgnoreCase(env)) {
//            return new DingtalkChatbotRet();
//        }

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
                return new Oauth2DingtalkChatbotRet();
            }

            String result = EntityUtils.toString(response.getEntity());
            logger.info("[ 发送预警信息 ] 发送成功，webhook:>{},请求内容:>{},返回:>{}", webhook, body, result);


            JSONObject obj = JSONObject.parseObject(result);
            int errcode = obj.getIntValue("errcode");
            return new Oauth2DingtalkChatbotRet(errcode == 0, errcode, obj.getString("errmsg"));
        } catch (Exception e) {
            logger.error("[ 发送预警信息 ] 发送失败：", e);
            return new Oauth2DingtalkChatbotRet();
        }
    }
}
