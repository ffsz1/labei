package com.xchat.common.netease.neteaseacc;

import com.google.gson.Gson;
import com.xchat.common.netease.neteaseacc.result.TokenRet;
import com.xchat.common.netease.util.CheckSumBuilder;
import com.xchat.common.netease.util.NetEaseConstant;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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

/**
 * Created by liuguofu on 2017/5/6.
 */
public class NetEaseBaseClient {
    private static final Logger logger = LoggerFactory.getLogger(NetEaseBaseClient.class);

    protected HttpPost httpPost;
    CloseableHttpClient httpClient = HttpClients.createDefault();

    public NetEaseBaseClient (String url, Boolean isSms) {
        httpPost=new HttpPost(url);
        String appKey = NetEaseConstant.smsAppKey;
        String appSecret = NetEaseConstant.smsAppSecret;
        String nonce =  "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    }

    public NetEaseBaseClient (String url){
        httpPost=new HttpPost(url);
        String appKey = NetEaseConstant.appKey;
        String appSecret = NetEaseConstant.appSecret;
        String nonce =  "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    }

    public String executePost() throws Exception{
        HttpResponse response = httpClient.execute(httpPost);
        InputStream is=response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.error("executePost error..."+httpPost.getURI());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("executePost close is error..."+httpPost.getURI());
            }
        }
        return sb.toString();
    }

    public String executePost2() throws IOException {
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String respContent = EntityUtils.toString(entity , "GBK").trim();
        return respContent;
    }

    public NetEaseBaseClient buildHttpPostParam(Map<String,Object> param) throws Exception{
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            Object value=entry.getValue();
            if(value==null){
                continue;
            }
            nvps.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        return this;
    }
    //{"code":200,"info":{"token":"c7f302b637099dc3039761ff3b45a21a","accid":"helloworld2","name":""}}
    //{"desc":"already register","code":414}
    public static void main(String args[]){
        String str="{\"code\":200,\"info\":{\"token\":\"c7f302b637099dc3039761ff3b45a21a\",\"accid\":\"helloworld2\",\"name\":\"\"}}";
        Gson gson=new Gson();
        TokenRet map= gson.fromJson(str,TokenRet.class);
        System.out.println();
    }



}
