package com.juxiao.xchat.base.annotation;

import com.juxiao.xchat.base.utils.DataUtils;
import org.apache.http.util.TextUtils;
import org.unbescape.html.HtmlEscape;

import java.util.Map;
import java.util.TreeMap;

public enum Client {
    /**
     * 应用客户端
     */
    APP {
        @Override
        public String sign(String url, Map<String, String> params, String key) {
            Map<String, String> paramsMap = this.url2Map(url);
            if (params != null) {
                paramsMap.putAll(params);
            }

            StringBuffer preSign = new StringBuffer();
            if (paramsMap != null && paramsMap.size() > 0) {
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    preSign.append(entry.getKey()).append("=").append(DataUtils.unescapeHtml(entry.getValue()));
                }
            }

            preSign.append(key);
            return preSign.toString();
        }
    },

    /**
     * 小程序客户端
     */
    WXAPP {
        @Override
        public String sign(String url, Map<String, String> params, String key) {
            Map<String, String> paramsMap = this.url2Map(url);
            if (params != null) {
                paramsMap.putAll(params);
            }

            StringBuilder preSign = new StringBuilder(url).append("?");
            if (paramsMap != null && paramsMap.size() > 0) {
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    preSign.append(entry.getKey()).append("=").append(DataUtils.unescapeHtml(entry.getValue())).append("&");
                }
            }

            preSign.append(key);
            return preSign.toString();
        }
    };

    public abstract String sign(String url, Map<String, String> params, String key);

    protected Map<String, String> url2Map(String param) {
        Map<String, String> map = new TreeMap<>(String::compareTo);
        if (TextUtils.isEmpty(param)) {
            return map;
        }

        String[] urlparams = param.split("\\?");
        if (urlparams.length == 2) {
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
