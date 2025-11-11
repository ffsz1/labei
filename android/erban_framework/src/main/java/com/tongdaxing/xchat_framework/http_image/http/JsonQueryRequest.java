package com.tongdaxing.xchat_framework.http_image.http;

import com.tongdaxing.xchat_framework.util.util.json.JsonParser;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Creator: 舒强睿
 * Date:2014/11/21
 * Time:10:15
 * <p/>
 * Description：
 */
public class JsonQueryRequest<T extends Serializable> extends BaseRequest<T> {

    Class<T> cls;

    public JsonQueryRequest(Cache cache, String url, ResponseListener successListener,
                            ResponseErrorListener errorListener, Class cls) {
        super(cache, url, successListener, errorListener);
        this.cls = cls;
    }

    @Override
    public void parseDataToResponse(ResponseData responseData) {

        HttpLog.v(((Object) this).getClass().getName() + " parse network response");
        String parsed;
        T result;
        try {
            parsed = new String(responseData.data, HttpHeaderParser.parseCharset(responseData.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(responseData.data);
        }

        MLog.debug(this, "JSON:" + parsed);
        result = JsonParser.parseJsonObject(parsed, cls);

        mResponse = Response.success(result, HttpHeaderParser.parseCacheHeaders(responseData));
    }
}
