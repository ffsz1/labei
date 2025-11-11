package com.tongdaxing.xchat_framework.http_image.http;

import android.os.Handler;

import org.apache.http.HttpEntity;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zhongyongsheng on 14-6-9.
 */
public abstract class BaseRequest<T extends Serializable> implements Request<T> {

    protected Network mNetwork;
    protected Cache mCache;
    protected Object mTag;
    public int mMethod;
    protected String mUrl;
    protected Integer mSequence;
    protected RequestProcessor mRequestProcessor;
    protected Response<T> mResponse;
    protected boolean mShouldCache = true;
    protected AtomicBoolean mCanceled = new AtomicBoolean(false);
    protected boolean mResponseDelivered = false;
    protected RetryPolicy mRetryPolicy;
    protected Cache.Entry mCacheEntry = null;
    protected ResponseListener mSuccessListener;
    protected ResponseErrorListener mErrorListener;
    protected ProgressListener mProgressListener;
    protected boolean mNoExpire;
    protected Map<String, String> mHeader;

    public BaseRequest(Cache cache,
                       String url,
                       ResponseListener successListener,
                       ResponseErrorListener errorListener) {
        this(cache, url, successListener, errorListener, null);
    }

    public BaseRequest(Cache cache,
                       String url,
                       ResponseListener successListener,
                       ResponseErrorListener errorListener,
                       ProgressListener progressListener) {
        mNetwork = new BaseNetwork();
        mMethod = Method.GET;
        mCache = cache;
        mUrl = url;
        mSuccessListener = successListener;
        mErrorListener = errorListener;
        mProgressListener = progressListener;
        mRetryPolicy = new DefaultRetryPolicy();
        mHeader = new ConcurrentHashMap<String, String>();
    }

    @Override
    public int getMethod() {
        return mMethod;
    }

    @Override
    public Map<String, String> getHeaders() {
        return mHeader;
    }

    @Override
    public Object getTag() {
        return mTag;
    }

    @Override
    public void setTag(Object tag) {
        mTag = tag;
    }

    @Override
    public void finish(String tag) {
        HttpLog.v(tag);
        if (mRequestProcessor != null) {
            mRequestProcessor.finish(this);
        }
    }

    @Override
    public RequestProcessor getRequestProcessor() {
        return mRequestProcessor;
    }

    @Override
    public void setRequestProcessor(RequestProcessor requestProcessor) {
        mRequestProcessor = requestProcessor;
    }

    @Override
    public int getSequence() {
        return mSequence;
    }

    @Override
    public void setSequence(int sequence) {
        mSequence = sequence;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public String getKey() {
        return getUrl();
    }

    @Override
    public void cancel() {
        mCanceled.set(true);
    }

    @Override
    public boolean isCanceled() {
        return mCanceled.get();
    }

    @Override
    public HttpEntity getPostEntity() {
        return null;
    }

    @Override
    public String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    @Override
    public void setShouldCache(boolean shouldCache) {
        mShouldCache = shouldCache;
    }

    @Override
    public boolean shouldCache() {
        return mShouldCache;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public int getTimeoutMs() {
        return mRetryPolicy.getCurrentTimeout();
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return mRetryPolicy;
    }

    @Override
    public void setRetryPolicy(RetryPolicy retryPolicy) {
        mRetryPolicy = retryPolicy;
    }

    @Override
    public Response<T> getResponse() {
        return mResponse;
    }

    @Override
    public abstract void parseDataToResponse(ResponseData responseData);

    @Override
    public void postResponse() {
        postResponse(null);
    }

    @Override
    public void postResponse(Runnable runnable) {
        if (mRequestProcessor != null) {
            Handler handler = mRequestProcessor.getHandler();
            if (handler == null) {
                new ResponseDeliveryRunnable(this,
                        this.getResponse(), runnable).run();
            } else {
                handler.post(new ResponseDeliveryRunnable(this,
                        this.getResponse(), runnable));
            }
        }
    }

    @Override
    public void postError(RequestError error) {
        mResponse = Response.error(error);
        postResponse();
    }

    @Override
    public void postProgress(ProgressInfo progressInfo) {
        if (mRequestProcessor != null) {
            Handler handler = mRequestProcessor.getHandler();
            if (handler == null) {
                new ProgressDeliveryRunnable(this,
                        mProgressListener, progressInfo).run();
            } else {
                handler.post(new ProgressDeliveryRunnable(this,
                        mProgressListener, progressInfo));
            }
        }
    }

    @Override
    public Network getNetwork() {
        return mNetwork;
    }

    @Override
    public Cache getCache() {
        return mCache;
    }

    @Override
    public void markDelivered() {
        mResponseDelivered = true;
    }

    @Override
    public boolean hasHadResponseDelivered() {
        return mResponseDelivered;
    }

    @Override
    public Cache.Entry getCacheEntry() {
        return mCacheEntry;
    }

    @Override
    public void setCacheEntry(Cache.Entry entry) {
        mCacheEntry = entry;
    }

    @Override
    public ResponseListener getSuccessListener() {
        return mSuccessListener;
    }

    @Override
    public ResponseErrorListener getErrorListener() {
        return mErrorListener;
    }

    @Override
    public ProgressListener getProgressListener() {
        return mProgressListener;
    }

    @Override
    public boolean getNoExpire() {
        return mNoExpire;
    }

    @Override
    public void setNoExpire(boolean noExpire) {
        mNoExpire = noExpire;
    }

    @Override
    public int compareTo(Request<T> other) {
        Priority left = this.getPriority();
        Priority right = other.getPriority();

        return left == right ?
                this.getSequence() - other.getSequence() :
                right.ordinal() - left.ordinal();
    }

    @Override
    public String toString() {
        return ((Object) this).getClass().getName() +
                "mUrl='" + mUrl + '\'' +
                '}';
    }

    private class ResponseDeliveryRunnable implements Runnable {
        private final Request mRequest;
        private final Response mResponse;
        private final Runnable mRunnable;

        public ResponseDeliveryRunnable(Request request, Response response, Runnable runnable) {
            mRequest = request;
            mRunnable = runnable;
            mResponse = response;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {

            if (mRequest.isCanceled()) {
                mRequest.finish("canceled-at-delivery");
                return;
            }

            if (mResponse.isSuccess()) {
                if (mRequest.getSuccessListener() != null) {
                    mRequest.getSuccessListener().onResponse(mResponse.result);
                }
            } else {
                if (mRequest.getErrorListener() != null) {
                    mRequest.getErrorListener().onErrorResponse(mResponse.error);
                }
            }

            if (mResponse.intermediate) {
                HttpLog.v("intermediate-response");
            } else {
                mRequest.finish("done");
            }

            if (mRunnable != null) {
                mRunnable.run();
            }
        }
    }

    protected class ProgressDeliveryRunnable implements Runnable {
        private final Request mRequest;
        private final ProgressInfo mInfo;
        private final ProgressListener mListener;

        public ProgressDeliveryRunnable(Request request, ProgressListener progressListener, ProgressInfo progressInfo) {
            mRequest = request;
            mListener = progressListener;
            mInfo = progressInfo;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {

            if (mRequest.isCanceled()) {
                mRequest.finish("Canceled in delivery runnable");
                return;
            }

            if (mListener != null) {
                HttpLog.d("On progress delivery " + mInfo);
                mListener.onProgress(mInfo);
            }

        }
    }
}
