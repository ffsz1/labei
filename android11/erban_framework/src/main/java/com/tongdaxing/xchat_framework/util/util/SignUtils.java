package com.tongdaxing.xchat_framework.util.util;

import android.text.TextUtils;

import com.tongdaxing.xchat_framework.util.util.codec.MD5Utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by polo on 2018/6/27.
 */

public class SignUtils {

    public static String getSign(String url, Map<String, String> params, String key, String t) {
        LogUtils.d("sign", url);
        Map<String, String> paramsMap = url2Map(url);
        if (params != null) {
            paramsMap.putAll(params);
        }
        if (t != null) {
            paramsMap.put("t", t);
        }

        StringBuffer signStringBuffer = new StringBuffer();
        if (paramsMap != null && paramsMap.size() > 0) {
            //请求参数是在url地址中的如果encode后发送，需要decode解码在加密，和后台一致（最好应该是直接对参数加密后再在发送因为地址中取不到问题所以不能这样）
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                String str = entry.getKey() + "=" + entry.getValue();
                signStringBuffer.append(str);
                LogUtils.d("sign", str);
            }
        }

        LogUtils.d("appendSnSign", key);
        signStringBuffer.append(key);
        LogUtils.d("getSign  params", signStringBuffer.toString());

        String sign = "";
        try {
            sign = MD5Utils.getMD5String(signStringBuffer.toString());
            LogUtils.d("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.d("getSign  sign", sign);
        if (sign.length() > 7) {
            sign = sign.substring(0, 7);
        }
        LogUtils.d("sign", "sub --- " + sign);
        return sign;
    }

    public static String getSign(String url, String t, String k) {
        return getSign(url, null, k, t);

    }

    public static String getSign(String k, String url, Map<String, String> params, String t) {
        return getSign(url, params, k, t);

    }


    public static Map<String, String> url2Map(String param) {
        Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }

        });

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
            } else if (p.length == 1) {
                map.put(p[0], "");
            }
        }
        return map;
    }


}
