package com.tongdaxing.xchat_framework.http_image.http;

import android.os.Handler;

import java.io.Serializable;

/**
 * 压缩缓存空间
 *
 * @author zhongyongsheng on 2014/7/24.
 */
public class CacheShrinkRequest<T extends Serializable> extends BaseRequest<T> {

    public CacheShrinkRequest(Cache cache, ResponseListener successListener, ResponseErrorListener errorListener) {
        super(cache, null, successListener, errorListener);
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    @Override
    public boolean isCanceled() {
        mCache.shrink();
        Handler handler = mRequestProcessor.getHandler();
        final ResponseListener listener = getSuccessListener();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onResponse(new Object());
                }
            }
        });
        return true;
    }

    @Override
    public void parseDataToResponse(ResponseData responseData) {
    }
}
