package com.erban.main.util;

import com.xchat.oauth2.service.common.util.DESUtils;
import org.apache.http.util.TextUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2018/3/27.
 */

public class SignUtils {
    private static final String signKey = "1ea53d260ecf11e7b56e00163e046a26";

    public static String getSign(String url, Map<String, String> params, String key, String t) {

        Map<String, String> urlParamsMap = url2Map(url);
        StringBuffer signStringBuffer = new StringBuffer();
        if (urlParamsMap != null && urlParamsMap.size() > 0) {
            for (Map.Entry<String, String> entry : urlParamsMap.entrySet()) {
                signStringBuffer.append(entry.getKey() + entry.getValue());
            }
        }
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                signStringBuffer.append(entry.getKey() + entry.getValue());
            }
        }

        if (!TextUtils.isEmpty(t)) {
            signStringBuffer.append("t" + t);
        }
        String sign = "";
        try {
            sign = DESUtils.DESAndBase64Encrypt(signStringBuffer.toString(), key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sign.length() > 6) {
            sign = sign.substring(sign.length() - 6, sign.length());
        }
        return sign;
    }

    public static String getSign(String url, String key, String t) {
        return getSign(url, null, key, t);

    }

    public static String getSign(String url, Map<String, String> params, String t) {
        return getSign(url, params, signKey, t);

    }

    public static String getSign(String url, String t) {
        return getSign(url, null, signKey, t);

    }


    public static Map<String, String> url2Map(String param) {


        Map<String, String> map = new TreeMap<String, String>();
        if (TextUtils.isEmpty(param)) {
            return map;
        }
        String[] urlparams = param.split("\\?");
        if (urlparams != null && urlparams.length == 2) {
            param = urlparams[1];
        } else {
            return map;
        }


        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);

            }
        }
        return map;
    }


}
