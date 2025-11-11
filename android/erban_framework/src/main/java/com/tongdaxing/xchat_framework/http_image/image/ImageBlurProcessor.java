package com.tongdaxing.xchat_framework.http_image.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.tongdaxing.xchat_framework.http_image.http.ByteArrayPool;
import com.tongdaxing.xchat_framework.http_image.http.Cache;
import com.tongdaxing.xchat_framework.http_image.http.HttpLog;
import com.tongdaxing.xchat_framework.http_image.http.Request;
import com.tongdaxing.xchat_framework.http_image.http.RequestError;
import com.tongdaxing.xchat_framework.http_image.http.RequestProcessor;
import com.tongdaxing.xchat_framework.http_image.http.ResponseData;
import com.tongdaxing.xchat_framework.http_image.http.ResponseErrorListener;
import com.tongdaxing.xchat_framework.http_image.http.ResponseListener;
import com.tongdaxing.xchat_framework.util.util.SafeDispatchHandler;
import com.tongdaxing.xchat_framework.util.util.image.Blur;
import com.tongdaxing.xchat_framework.util.util.image.JXImageUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lijun on 2015/4/24.
 */
public class ImageBlurProcessor implements RequestProcessor {

    private Handler mHandler;

    private ExecutorService mService = Executors.newSingleThreadExecutor();

    private final Map<String, Queue<Request>> mWaitingRequests = new HashMap<String, Queue<Request>>();
    private final Set<Request> mCurrentRequests = new HashSet<Request>();

    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    /**
     * 播放页背景图片模糊半径
     */
    public static final int DEFAULT_BLUR_RADIO = 25;

    public ImageBlurProcessor(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public ImageBlurProcessor() {
        this(new SafeDispatchHandler(Looper.getMainLooper()));
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        mService.shutdown();
    }


    @Override
    public Request add(final Request request) {

        if (!(request instanceof ImageBlurRequest)) {
            return request;
        }

        request.setRequestProcessor(ImageBlurProcessor.this);

        synchronized (mCurrentRequests) {
            mCurrentRequests.add(request);
        }

        request.setSequence(getSequenceNumber());

        synchronized (mWaitingRequests) {
            final String cacheKey = request.getKey();
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

                if (!loadCache((ImageRequest) request)) {
                    loadAlbumImg(request.getUrl(), ((ImageRequest)request).mContext, ((ImageRequest) request).mImageConfig,
                       new ResponseListener<ImageResponse>() {
                        @Override
                        public void onResponse(final ImageResponse response) {
                            // main thread
                            executeFlur(response, request);
                        }
                    }, new ResponseErrorListener() {
                        @Override
                        public void onErrorResponse(RequestError error) {
                            request.postError(error);
                        }
                    });
                }
            }
        }

        return request;
    }

    private void executeFlur(final ImageResponse response, final Request request) {
        mService.submit(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                try {
                    Bitmap old = response.bitmapDrawable.getBitmap();
                    if (null != old && !old.isRecycled()) {

                        ImageRequest imageRequest = (ImageRequest) request;
                        final Bitmap blurBitmap = Blur.fastblur(imageRequest.mContext, old, DEFAULT_BLUR_RADIO);
                        ResponseData data = new ResponseData(JXImageUtils.compressToBytes(blurBitmap));
                        request.parseDataToResponse(data);

                        if (request.shouldCache() && request.getResponse().cacheEntry != null) {
                            request.getCache().put(request.getKey(), request.getResponse().cacheEntry);
                            HttpLog.v("Network cache written");
                        }

                        request.postResponse();
                        request.markDelivered();
                    }
                } catch (Exception e) {
                    HttpLog.e(e, "Unhandled exception " + e.toString());
                    request.postError(new RequestError(e));
                } catch (Error e) {
                    HttpLog.e(e, "Unhandled error " + e.toString());
                    request.postError(new RequestError(e));
                }
            }
        });
    }

    private void loadAlbumImg(final String albumCover, Context context, ImageConfig config,
                              ResponseListener listener, ResponseErrorListener errorListener) {
        MLog.debug(this, "loadAlbumImg album: %s", albumCover);
        final ImageRequest imageRequest = ImageManager.instance().newRequest(
                albumCover, config, context, true, listener, errorListener
        );

        ImageManager.instance().getProcessor().add(imageRequest);
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    @Override
    public ByteArrayPool getByteArrayPool() {
        return null;
    }

    @Override
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

    @Override
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
                final Queue<Request> waitingRequests = mWaitingRequests.remove(cacheKey);
                if (waitingRequests != null) {
                    HttpLog.v("Releasing %d waiting requests for cacheKey=%s.", waitingRequests.size(), cacheKey);

                    for (Request req : waitingRequests) {
                        loadCache((ImageRequest) req);
                    }
                }
            }
        }
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

    public int getSequenceNumber() {
        return mSequenceGenerator.incrementAndGet();
    }

    private boolean loadCache(ImageRequest request) {

        ResponseData data;
        BitmapDrawable bitmapDrawable = request.mMemCache.getBitmapFromMemCache(request.getKey());
        if (null != bitmapDrawable) {
            data = new ResponseData(JXImageUtils.compressToBytes(bitmapDrawable.getBitmap()));
        } else {
            request.getCache().initialize();
            Cache.Entry entry = request.getCache().get(request.getKey());
            if (entry == null) {
                HttpLog.v("Cache miss");
                return false;
            }

            data = new ResponseData(entry.getData(), entry.getResponseHeaders());
        }

        request.parseDataToResponse(data);
        request.postResponse();
        request.markDelivered();
        return true;
    }
}
