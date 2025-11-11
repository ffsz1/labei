package com.tongdaxing.xchat_framework.http_image.http;

import android.os.Handler;

import java.io.Serializable;

/**
 * 清除缓存
 *
 * @author zhongyongsheng on 2014/7/24.
 */
public class CacheCleanRequest<T extends Serializable> extends BaseRequest<T> {

    public CacheCleanRequest(Cache cache, ResponseListener successListener, ResponseErrorListener errorListener) {
        super(cache, null, successListener, errorListener);
    }

    @Override
    public Priority getPriority() {
        return Priority.IMMEDIATE;
    }

    @Override
    public boolean isCanceled() {
        mCache.clear();
        Handler handler = mRequestProcessor.getHandler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getSuccessListener() != null) {
                    getSuccessListener().onResponse(new Object());
                }
            }
        });
        return true;
    }

    @Override
    public void parseDataToResponse(ResponseData responseData) {
        // empty
    }
}
