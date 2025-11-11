package com.tongdaxing.xchat_framework.http_image.http;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.tongdaxing.xchat_framework.util.util.SafeDispatchHandler;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zhongyongsheng on 14-6-13.
 */
public class SameThreadRequestProcessor implements RequestProcessor {
    private static int DEFAULT_POOL_SIZE = 4096;
    private final ByteArrayPool mPool;
    private Handler mHandler;

    public SameThreadRequestProcessor() {
        mPool = new ByteArrayPool(DEFAULT_POOL_SIZE);
        mHandler = new SafeDispatchHandler(Looper.getMainLooper());
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public Request add(final Request request) {
        //避免4.0以后系统不能在mainthread使用http
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        } // comment by lijun 2015.4.21

        if (request == null) return null;

        request.setRequestProcessor(this);

        //: 线程安全？？？多线程访问是什么情况？

        try {
            HttpLog.v("SameThreadRequestProcessor start");

            if (request.isCanceled()) {
                request.finish("Network discard cancelled");
                return request;
            }

            request.getCache().initialize();

            Cache.Entry entry = request.getCache().get(request.getKey());
            if (entry != null && !entry.isExpired()) {
                request.parseDataToResponse(new ResponseData(entry.getData(), entry.getResponseHeaders()));
                if (!entry.refreshNeeded()) {
                    request.postResponse();
                    request.markDelivered();
                } else {
                    HttpLog.v("Cache refresh needed");
                    request.setCacheEntry(entry);

                    request.getResponse().intermediate = true;

                    //返回成功结果,再去网络请求更新缓存
                    request.postResponse(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                performRequest(request);
                            } catch (RequestError error) {
                                request.postError(error);
                            } catch (Exception e) {
                                HttpLog.e(e, "Unhandled exception %s", e.toString());
                                request.postError(new RequestError(e));
                            }
                        }
                    });
                }

            } else {
                performRequest(request);
            }

        } catch (RequestError error) {
            request.postError(error);
        } catch (Exception e) {
            HttpLog.e(e, "Unhandled exception %s", e.toString());
            request.postError(new RequestError(e));
        }

        return request;
    }

    private void performRequest(Request request) throws RequestError {
        ResponseData responseData = request.getNetwork().performRequest(request);
        HttpLog.v("Network http complete");

        if (responseData.notModified && request.hasHadResponseDelivered()) {
            request.finish("Network not modified");
            return;
        }

        request.parseDataToResponse(responseData);
        HttpLog.v("Network parse complete");

        if (request.shouldCache() && request.getResponse().cacheEntry != null) {
            request.getCache().put(request.getKey(), request.getResponse().cacheEntry);
            HttpLog.v("Network cache written");
        }

        request.markDelivered();
        request.postResponse();
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public ByteArrayPool getByteArrayPool() {
        return mPool;
    }

    @Override
    public void cancelAll(RequestFilter filter) {

    }

    @Override
    public void cancelAll(Object tag) {

    }

    @Override
    public void finish(Request request) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public AtomicBoolean getPause() {
        return null;
    }
}
