package com.juxiao.xchat.manager.external.netease.impl;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NetEaseClient {
    private static final Logger logger = LoggerFactory.getLogger(NetEaseClient.class);

    private HttpPost httpPost;
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    public NetEaseClient(String url, String appKey, String appSecret) {
        httpPost = new HttpPost(url);
        String nonce = "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    }

    public String executePost() throws Exception {
        HttpResponse response = httpClient.execute(httpPost);

        StringBuilder sb = new StringBuilder();
        String line;
        try (InputStream is = response.getEntity().getContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw e;
        }

    }

    public NetEaseClient buildHttpPostParam(Map<String, Object> param) throws Exception {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        return this;
    }
}
