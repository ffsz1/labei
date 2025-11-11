package com.tongdaxing.xchat_framework.http_image.http;

import android.os.Process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zhongyongsheng
 */
@SuppressWarnings("rawtypes")
public class CacheDispatcher extends Thread {

    private final BlockingQueue<Request> mCacheQueue;

    private final BlockingQueue<Request> mNetworkQueue;

    private volatile boolean mQuit = false;

    private RequestProcessor mRequestProcessor;

    public CacheDispatcher(
            BlockingQueue<Request> cacheQueue, BlockingQueue<Request> networkQueue, String name,
            RequestProcessor requestProcessor) {
        super(name + "CacheThread");
        mCacheQueue = cacheQueue;
        mNetworkQueue = networkQueue;
        mRequestProcessor = requestProcessor;
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        while (true) {
            try {

                final Request request = mCacheQueue.take();

                waitIfPaused();

                if (request.isCanceled()) {
                    request.finish("Cache discard canceled");
                    continue;
                }

                request.getCache().initialize();

                Cache.Entry entry = request.getCache().get(request.getKey());
                if (entry == null) {
                    HttpLog.v("Cache miss");
                    mNetworkQueue.put(request);
                    continue;
                }

                if (entry.isExpired()) {
                    //缓存过期, 去网络请求
                    HttpLog.v("Cache expired");
                    request.setCacheEntry(entry);
                    mNetworkQueue.put(request);
                    continue;
                }

                HttpLog.v("Cache hit");
                request.parseDataToResponse(new ResponseData(entry.getData(), entry.getResponseHeaders()));
                HttpLog.v("Cache parsed");

                if (!entry.refreshNeeded()) {
                    request.postResponse();
                } else {
                    HttpLog.v("Cache refresh needed");
                    request.setCacheEntry(entry);

                    request.getResponse().intermediate = true;

                    //返回成功结果,再去网络请求更新缓存
                    request.postResponse(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mNetworkQueue.put(request);
                            } catch (InterruptedException e) {
                            }
                        }
                    });
                }

            } catch (InterruptedException e) {
                if (mQuit) {
                    return;
                }
                continue;
            } catch (Exception ee) {
                HttpLog.e(ee, "Uncatch error.");
                continue;
            } catch (Error e) {
                HttpLog.e(e, "Unhandled error " + e.toString());
            }
        }
    }

    private void waitIfPaused() {
        AtomicBoolean pause = mRequestProcessor.getPause();
        synchronized (pause) {
            if (pause.get()) {
                HttpLog.v("Cache Wait for pause");
                try {
                    pause.wait();
                } catch (InterruptedException e) {
                    HttpLog.e(e, "Cache Wait for pause interrupted");
                    return;
                }
                HttpLog.v("Cache Resume pause");
            }
        }
    }
}
