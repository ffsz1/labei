package com.tongdaxing.xchat_framework.http_image.http;

import android.os.Handler;
import android.os.Looper;

import com.tongdaxing.xchat_framework.util.util.SafeDispatchHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhongyongsheng on 14-4-4.
 */
public class DefaultRequestProcessor implements RequestProcessor {

    private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 4;
    private static final int DEFAULT_POOL_SIZE = 4096;

    //相同的请求
    private final Map<String, Queue<Request>> mWaitingRequests = new HashMap<String, Queue<Request>>();
    private final Set<Request> mCurrentRequests = new HashSet<Request>();
    private final PriorityBlockingQueue<Request> mCacheQueue = new PriorityBlockingQueue<Request>();
    private final PriorityBlockingQueue<Request> mNetworkQueue = new PriorityBlockingQueue<Request>();

    private final ByteArrayPool mPool;
    private final String mName;
    private final AtomicBoolean paused = new AtomicBoolean(false);

    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private NetworkDispatcher[] mDispatchers;
    private CacheDispatcher mCacheDispatcher;
    private Handler mHandler;

    public DefaultRequestProcessor(int threadPoolSize, Handler handler, String name) {
        mDispatchers = new NetworkDispatcher[threadPoolSize];
        mHandler = handler;
        mPool = new ByteArrayPool(DEFAULT_POOL_SIZE);
        mName = name;
    }

    public DefaultRequestProcessor(int threadPoolSize, String name) {
        this(threadPoolSize, new SafeDispatchHandler(Looper.getMainLooper()), name);
    }

    public DefaultRequestProcessor() {
        this(DEFAULT_NETWORK_THREAD_POOL_SIZE, "");
    }

    public void start() {
        stop();
        mCacheDispatcher = new CacheDispatcher(mCacheQueue, mNetworkQueue, mName, this);
        mCacheDispatcher.start();

        for (int i = 0, len = mDispatchers.length; i < len; i++) {
            NetworkDispatcher networkDispatcher = new NetworkDispatcher(mNetworkQueue, mName, this);
            mDispatchers[i] = networkDispatcher;
            networkDispatcher.start();
        }
    }

    public void stop() {
        if (mCacheDispatcher != null) {
            mCacheDispatcher.quit();
        }
        for (int i = 0; i < mDispatchers.length; i++) {
            if (mDispatchers[i] != null) {
                mDispatchers[i].quit();
            }
        }
    }

    public int getSequenceNumber() {
        return mSequenceGenerator.incrementAndGet();
    }

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public ByteArrayPool getByteArrayPool() {
        return mPool;
    }

    public void cancelAll(RequestFilter filter) {
        if (filter == null) {
            return;
        }
        synchronized (mCurrentRequests) {
            for (Request<?> request : mCurrentRequests) {
                if (filter.apply(request)) {
                    request.cancel();
                }
            }
        }
    }

    public void cancelAll(final Object tag) {
        if (tag == null) {
            return;
        }
        cancelAll(new RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return request.getTag() == tag;
            }
        });
    }

    public Request add(Request request) {
        if (request == null) {
            return null;
        }

        request.setRequestProcessor(this);
        synchronized (mCurrentRequests) {
            mCurrentRequests.add(request);
        }

        request.setSequence(getSequenceNumber());
        HttpLog.v("Add to queue");

        if (!request.shouldCache()) {
            HttpLog.v("add to network queue");
            mNetworkQueue.add(request);
            return request;
        }

        synchronized (mWaitingRequests) {
            String cacheKey = request.getKey();
            if (mWaitingRequests.containsKey(cacheKey)) {
                Queue<Request> stagedRequests = mWaitingRequests.get(cacheKey);
                if (stagedRequests == null) {
                    stagedRequests = new LinkedList<Request>();
                }
                stagedRequests.add(request);
                mWaitingRequests.put(cacheKey, stagedRequests);
                HttpLog.v("Should cache, Request is processing, cacheKey=%s", cacheKey);
            } else {
                HttpLog.v("Should cache, add to cache queue");
                mWaitingRequests.put(cacheKey, null);
                mCacheQueue.add(request);
            }
            return request;
        }
    }

    public void finish(Request request) {
        if (request == null) {
            return;
        }
        synchronized (mCurrentRequests) {
            mCurrentRequests.remove(request);
        }

        if (request.shouldCache()) {
            synchronized (mWaitingRequests) {
                String cacheKey = request.getKey();
                Queue<Request> waitingRequests = mWaitingRequests.remove(cacheKey);
                if (waitingRequests != null) {
                    HttpLog.v("Releasing %d waiting requests for cacheKey=%s.", waitingRequests.size(), cacheKey);
                    mCacheQueue.addAll(waitingRequests);
                }
            }
        }
    }

    @Override
    public void pause() {
        paused.set(true);
    }

    @Override
    public void resume() {
        synchronized (paused) {
            paused.set(false);
            paused.notifyAll();
        }
    }

    public AtomicBoolean getPause() {
        return paused;
    }
}
