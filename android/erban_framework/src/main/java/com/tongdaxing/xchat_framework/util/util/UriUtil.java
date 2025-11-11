package com.tongdaxing.xchat_framework.util.util;

import android.net.Uri;

/**
 * Created by lijun on 2015/6/18.
 */
public class UriUtil {

    public static final String SCHEME_HTTP_TAG = "http";
    public static final String SCHEME_HTTPS_TAG = "https";
    public static final String SCHEME_FILE_TAG = "file";

    /**
     * NOTICE you may need URLUtil.isHttpUrl(String url)
     * @param url
     * @return
     */
    public static boolean isHttpUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }

        return isHttpUri(Uri.parse(url));
    }

    public static boolean isHttpUri(Uri uri) {
        if (null == uri) {
            return false;
        }

        String scheme = uri.getScheme();
        if (StringUtils.isEmpty(scheme)) {
            return false;
        }
        return SCHEME_HTTP_TAG.equals(scheme) || SCHEME_HTTPS_TAG.equals(scheme);
    }

    public static boolean isFileUri(Uri uri) {
        if (null == uri) {
            return false;
        }
        String scheme = uri.getScheme();
        if (StringUtils.isEmpty(scheme)) {
            return false;
        }
        return SCHEME_FILE_TAG.equals(scheme);
    }

    public static Uri generateHttpUri(String url) {
        if (isHttpUrl(url)) {
            return Uri.parse(url);
        } else if (StringUtils.isNotEmpty(url)) {
            return Uri.parse(SCHEME_HTTP_TAG + "://" + url);
        }
        return null;
    }

    /**
     * 从http url获取参数
     * @param url
     * @param key
     * @return
     */
    public static String getParamFromUrl(String url, String key) {
        if (StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(key)) {
            int queryIndex = url.indexOf('?');
            if (queryIndex > 0) {
                String[] paramMap = url.substring(queryIndex + 1).split("&");
                if (paramMap != null) {
                    for (String map : paramMap) {
                        String[] paramKv = map.split("=");
                        if (paramKv.length == 2 && key.equals(paramKv[0])) {
                            return paramKv[1];
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getParamFromUrl("adfasdf?777=99&ss=11", "77"));
    }
}
