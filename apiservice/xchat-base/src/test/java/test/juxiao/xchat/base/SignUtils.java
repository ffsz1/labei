package test.juxiao.xchat.base;

import com.juxiao.xchat.base.utils.MD5Utils;
import org.apache.http.util.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by polo on 2018/6/27.
 */

public class SignUtils {
    private static final String signKey = "1ea53d260ecf11e7b56e00163e046a26";

    public static String getSign(String url, Map<String, String> params, String key, String t) {
        Map<String, String> paramsMap = url2Map(url);
        System.out.println(paramsMap);
        if (params != null)
            paramsMap.putAll(params);
        if (t != null) {
            paramsMap.put("t", t);
        }


        StringBuffer signStringBuffer = new StringBuffer();
        if (paramsMap != null && paramsMap.size() > 0) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                String str = entry.getKey() + "=" + entry.getValue();
                signStringBuffer.append(str);

            }
        }


        signStringBuffer.append(key);
        String sign = "";
        System.out.println(signStringBuffer.toString());
        try {
            sign = MD5Utils.encode(signStringBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sign.length() > 7) {
            sign = sign.substring(0, 7);
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


        Map<String, String> map = new TreeMap<String, String>(String::compareTo);

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

    public static void main(String[] args) {
        String url = "/purse/query";
        Map<String, String> map = new HashMap<>();
        String t = String.valueOf(System.currentTimeMillis());

        map.put("app", "xchat");
        map.put("appVersion", "2.5.3");
        map.put("os", "android");
        map.put("ticket", "eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjo5MDAwMDc2NywidGlja2V0X2lkIjoiYWFjNDkwMGYtZTIxMy00MTRmLTg0MTgtZjNmOWEzYWVjZjA0IiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.6N7Uj99XquNZimW0UIHl6b13Dp08f3_J7riy3Kjj82o");
        map.put("netType", "1");
        map.put("channel", "tt");
        map.put("appCode", "59");
        map.put("deviceId", "f1d834b3-5bd0-37de-8437-a77bdcfffb87");
        map.put("uid", "90000767");
        map.put("osVersion", "8.1.0");
        map.put("model", "EML-AL00");
        map.put("ispType", "4");

        String sign = getSign(url, map, signKey, t);


    }

}
