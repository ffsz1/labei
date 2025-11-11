package com.tongdaxing.xchat_framework.http_image.http;

import org.apache.http.HttpEntity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by zhongyongsheng on 14-4-4.
 */
public interface Request<T extends Serializable> extends Comparable<Request<T>> {

    static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    public int getMethod();

    public Map<String, String> getHeaders();

    public Object getTag();

    public void setTag(Object tag);

    void finish(final String tag);

    public RequestProcessor getRequestProcessor();

    public void setRequestProcessor(RequestProcessor requestProcessor);

    public int getSequence();

    public void setSequence(int sequence);

    public String getUrl();

    public String getKey();

    public void cancel();

    public boolean isCanceled();

    public HttpEntity getPostEntity();

    public String getParamsEncoding();

    public void setShouldCache(boolean shouldCache);

    public boolean shouldCache();

    public Priority getPriority();

    public int getTimeoutMs();

    public RetryPolicy getRetryPolicy();

    public void setRetryPolicy(RetryPolicy retryPolicy);

    public Response<T> getResponse();

    /**
     * 将data数据转换成回调对象
     *
     * @param responseData
     */
    public void parseDataToResponse(ResponseData responseData);

    /**
     * 发送对象给回调listener
     */
    public void postResponse();

    /**
     * 发送对象给回调listener
     *
     * @param runnable
     */
    public void postResponse(Runnable runnable);

    /**
     * 发送异常给回调listener
     *
     * @param error
     */
    public void postError(RequestError error);

    /**
     * 发送进度给回调listener
     *
     * @param progressInfo
     */
    public void postProgress(ProgressInfo progressInfo);

    public Network getNetwork();

    public Cache getCache();

    public void markDelivered();

    public boolean hasHadResponseDelivered();

    public Cache.Entry getCacheEntry();

    public void setCacheEntry(Cache.Entry entry);

    public ResponseListener getSuccessListener();

    public ResponseErrorListener getErrorListener();

    public ProgressListener getProgressListener();

    /**
     * 是否缓存永远有效
     *
     * @return
     */
    public boolean getNoExpire();

    /**
     * 设置是否缓存永远有效
     *
     * @param noExpire true 缓存永远有效
     */
    public void setNoExpire(boolean noExpire);

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    public interface Method {
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
    }
}
