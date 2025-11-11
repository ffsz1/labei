package com.xchat.common.utils;

import com.google.gson.Gson;
import com.xchat.common.device.DeviceInfo;
import com.xchat.common.netease.neteaseacc.result.TokenRet;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by liuguofu on 2017/7/10.
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static String executeGet(String url) throws Exception{
        HttpPost httpPost=new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
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
            logger.error("executeGet error..."+httpPost.getURI());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("executeGet close is error..."+httpPost.getURI());
            }
        }
        return sb.toString();
    }

//    public DeviceInfo getDeviceInfoByHeader(HttpHeaders headers){
//        DeviceInfo deviceInfo=new DeviceInfo();
//        if(headers==null){
//            return deviceInfo;
//        }
//        headers.
//
//    }

    //{"code":200,"info":{"token":"c7f302b637099dc3039761ff3b45a21a","accid":"helloworld2","name":""}}
    //{"desc":"already register","code":414}
    public static void main(String args[]){
        String str="{\"code\":200,\"info\":{\"token\":\"c7f302b637099dc3039761ff3b45a21a\",\"accid\":\"helloworld2\",\"name\":\"\"}}";
        Gson gson=new Gson();
        TokenRet map= gson.fromJson(str,TokenRet.class);
        System.out.println();
    }

}
