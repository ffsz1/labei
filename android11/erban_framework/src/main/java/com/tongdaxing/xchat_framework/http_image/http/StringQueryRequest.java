package com.tongdaxing.xchat_framework.http_image.http;

import java.io.UnsupportedEncodingException;

/**
 * @author zhongyongsheng
 */
public class StringQueryRequest<String> extends BaseRequest {

    public StringQueryRequest(Cache cache,
                              java.lang.String url,
                              ResponseListener<String> successListener,
                              ResponseErrorListener errorListener) {
        super(cache, url, successListener, errorListener);
    }

    @Override
    public void parseDataToResponse(ResponseData responseData) {
        HttpLog.v(((Object) this).getClass().getName() + " parse network response");
        java.lang.String parsed;
        try {
            parsed = new java.lang.String(responseData.data, HttpHeaderParser.parseCharset(responseData.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new java.lang.String(responseData.data);
        }

        mResponse = Response.success(parsed, HttpHeaderParser.parseCacheHeaders(responseData));
    }
}
