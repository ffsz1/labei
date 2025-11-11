/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tongdaxing.xchat_framework.http_image.http;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.protocol.HTTP;

import java.util.Map;

/**
 * @author zhongyongsheng
 */
public class HttpHeaderParser {

    private static final String[] MY_DEFAULT_DATE_PATTERNS = new String[]{
            "EEE, dd MMM yyyy HH:mm:ss"
    };

    public static Cache.Entry parseCacheHeaders(ResponseData response) {
        return parseCacheHeaders(response, false);
    }

    /**
     * @param response
     * @param noExpire true 强制缓存时间为最长
     * @return
     */
    public static Cache.Entry parseCacheHeaders(ResponseData response, boolean noExpire) {
        long now = System.currentTimeMillis();

        Map<String, String> headers = response.headers;

        long serverDate = 0;
        long serverExpires = 0;
        long softExpire = 0;
        long maxAge = 0;
        boolean hasCacheControl = false;

        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = parseDateAsEpoch(headerValue);
        }

        headerValue = headers.get("Cache-Control");
        if (headerValue != null) {
            hasCacheControl = true;
            String[] tokens = headerValue.split(",");
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i].trim();
                if (token.equals("no-cache") || token.equals("no-store")) {
                    return null;
                } else if (token.startsWith("max-age=")) {
                    try {
                        maxAge = Long.parseLong(token.substring(8));
                    } catch (Exception e) {
                    }
                } else if (token.equals("must-revalidate") || token.equals("proxy-revalidate")) {
                    maxAge = 0;
                }
            }
        }

        headerValue = headers.get("Expires");
        if (headerValue != null) {
            serverExpires = parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        // 是否有Cache-Control
        if (hasCacheControl) {
            softExpire = now + maxAge * 1000;
        } else if (serverDate > 0 && serverExpires >= serverDate) {
            // 按Expires计算有效时间
            softExpire = now + (serverExpires - serverDate);
        }

        if (noExpire) {
            softExpire = Long.MAX_VALUE;
        }

        Cache.Entry entry = new Cache.Entry();
        entry.setData(response.data);
        entry.setEtag(serverEtag);
        entry.setSoftTtl(softExpire);
        entry.setTtl(entry.getSoftTtl());
        entry.setServerDate(serverDate);
        entry.setResponseHeaders(headers);

        return entry;
    }

    public static long parseDateAsEpoch(String dateStr) {
        try {
            return DateUtils.parseDate(dateStr).getTime();
        } catch (DateParseException e) {
            try {
                return DateUtils.parseDate(dateStr, MY_DEFAULT_DATE_PATTERNS).getTime();
            } catch (DateParseException e1) {
                HttpLog.e(e1, "Parse server date error");
                return 0;
            }
        }
    }

    public static String parseCharset(Map<String, String> headers) {
        String contentType = headers.get(HTTP.CONTENT_TYPE);
        if (contentType != null) {
            String[] params = contentType.split(";");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }

        return HTTP.UTF_8;
    }
}
