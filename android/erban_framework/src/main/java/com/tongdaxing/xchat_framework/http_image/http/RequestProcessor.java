package com.tongdaxing.xchat_framework.http_image.http;

import android.os.Handler;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Http 请求处理器
 * 图片和其他http请求分开不同的处理器
 *
 * @author zhongyongsheng
 */
public interface RequestProcessor {

    /**
     * 启动.必须先启动,才能add request
     */
    public void start();

    /**
     * 停止
     */
    public void stop();

    /**
     * 把request添加到处理器运行
     *
     * @param request
     * @return
     */
    public Request add(Request request);

    public Handler getHandler();

    public void setHandler(Handler handler);

    public ByteArrayPool getByteArrayPool();

    public void cancelAll(RequestFilter filter);

    public void cancelAll(final Object tag);

    public void finish(Request request);

    public void pause();

    public void resume();

    public AtomicBoolean getPause();

    public interface RequestFilter {
        public boolean apply(Request<?> request);
    }
}
