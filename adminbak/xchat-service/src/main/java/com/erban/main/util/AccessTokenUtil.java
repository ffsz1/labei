package com.erban.main.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AccessTokenUtil {

    public static String oauth2GetAccessToken(String classify) {
        String appid = "";
        String appsecret = "";
        switch (classify) {
            case "1":
                //自己的配置appid
                appid = "wx534aae2afa0bcc0d";
                //自己的配置APPSECRET;
                appsecret = "d941a1b705d4611001a4d991de176066";
                break;
            case "2":
                appid = "**********";
                appsecret = "**********";
                break;
            case "3":
                appid = "**********";
                appsecret = "************";
                break;
            case "4":
                appid = "**********";
                appsecret = "************";
                break;
            case "5":
                appid = "**********";
                appsecret = "************";
        }
        //授权（必填）
        String grant_type = "client_credential";
        //URL
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token";
        //请求参数
        String params = "appid=" + appid + "&secret=" + appsecret + "&grant_type=" + grant_type;
        //发送请求
        String data = HttpUtil.get(requestUrl, params);
        Map<String, Object> map = new HashMap<>();
        //解析相应内容（转换成json对象
        Gson gson = new Gson();
        JSONObject json = new JSONObject(gson.fromJson(data, map.getClass()));
        return String.valueOf(json.get("access_token"));
    }

}
