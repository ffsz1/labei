package com.tongdaxing.xchat_framework.util.cache;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;
import com.tongdaxing.xchat_framework.util.util.asynctask.AsyncTask;
import com.tongdaxing.xchat_framework.util.util.json.JsonParser;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 缓存客户端
 */
@SuppressLint("HandlerLeak")
public class CacheClient implements Cache {

    private long defaultExpire;

    private AsyncTask asyncTask = new AsyncTask("CacheClient");

    private Map<String, BlockingQueue<CallbackWrapper>> manager = new ConcurrentHashMap<String, BlockingQueue<CallbackWrapper>>();

    private CacheManager cacheManager;

    /**
     * 保证资源唯一
     */
    private String uri;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            CallbackWrapper wrapper = (CallbackWrapper) msg.obj;
            ReturnCallback returnCallback = wrapper.getReturnCallback();
            if (returnCallback != null) {
                try {
                    wrapper.getReturnCallback().onReturn(wrapper.getData());
                } catch (Exception e) {
                    MLog.error(this, e);
                }
            }
            ErrorCallback errorCallback = wrapper.getErrorCallback();
            if (errorCallback != null) {
                try {
                    wrapper.getErrorCallback().onError(wrapper.getError());
                } catch (Exception e) {
                    MLog.error(this, e);
                }
            }
        }
    };

    protected CacheClient(String uri) {
        this(uri, TimeUtils.MINUTES_OF_HOUR * TimeUtils.SECONDS_OF_MINUTE * TimeUtils.MILLIS_OF_SECOND);
    }

    protected CacheClient(String uri, long defaultExpire) {
        this.defaultExpire = defaultExpire;
        this.uri = uri;
        this.cacheManager = new CacheManager(uri);
    }

    @Override
    public Object getSync(String key) throws CacheException {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        final String mKey = key;
        Object data = "";
        CacheException error = null;
        try {
            // read json
            String json = cacheManager.getCache(mKey);

            CachePacket packet = JsonParser.parseJsonObject(json, CachePacket.class);
            data = packet.getContent();
        } catch (NoSuchKeyException e) {
            error = e;
            MLog.error(this, e);
        } catch (Exception e) {
            error = new CacheException(mKey, "Wrap otherwise exceptions", e);
            MLog.error(this, error);
        }

        if (null != error) {
            throw error;
        }

        return data;
    }

    @Override
    public void get(String key, ReturnCallback returncallback) {
        get(key, returncallback, null);
    }

    @Override
    public void get(String key, ReturnCallback returncallback, ErrorCallback errorCallback) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        final String mKey = key;
        BlockingQueue<CallbackWrapper> handlers = manager.get(mKey);
        if (handlers == null) {
            handlers = new LinkedBlockingQueue<CallbackWrapper>();
        }
        CallbackWrapper wrapper = new CallbackWrapper();
        wrapper.setReturnCallback(returncallback);
        wrapper.setErrorCallback(errorCallback);
        handlers.add(wrapper);
        manager.put(mKey, handlers);

        asyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Object data = "";
                CacheException error = null;
                BlockingQueue<CallbackWrapper> handlers = manager.get(mKey);
                if (handlers.isEmpty()) {
                    return;
                }
                try {
                    // read json
                    String json = cacheManager.getCache(mKey);

                    CachePacket packet = JsonParser.parseJsonObject(json, CachePacket.class);
                    data = packet.getContent();
                } catch (NoSuchKeyException e) {
                    error = e;
                    MLog.error(this, e);
                } catch (Exception e) {
                    error = new CacheException(mKey, "Wrap otherwise exceptions", e);
                    MLog.error(this, error);
                }

                for (; ; ) {
                    CallbackWrapper wrapper = handlers.poll();
                    if (wrapper == null) {
                        break;
                    }
                    wrapper.setData(data);
                    wrapper.setError(error);
                    Message msg = Message.obtain();
                    msg.obj = wrapper;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, defaultExpire);
    }

    @Override
    public void put(String key, Object value, long expire) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        final String mKey = key;
        final long mexpire = expire;
        CacheHeader header = new CacheHeader(key, expire, System.currentTimeMillis());
        CachePacket packet = new CachePacket(header, value);
        final String json = JsonParser.toJson(packet);
        asyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Save json
                cacheManager.putCache(mKey, json, mexpire);
            }
        });
    }

    public void remove(String key) {
        // Auto-generated method stub
        cacheManager.remove(key);
    }

    public void clear() {
        // Auto-generated method stub
        cacheManager.clear();
    }

    public String getUri() {
        return uri;
    }

    public class CallbackWrapper {

        private Object data;

        private CacheException error;

        private ReturnCallback returnCallback;

        private ErrorCallback errorCallback;

        public CallbackWrapper() {
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public ReturnCallback getReturnCallback() {
            return returnCallback;
        }

        public void setReturnCallback(ReturnCallback returnCallback) {
            this.returnCallback = returnCallback;
        }

        public ErrorCallback getErrorCallback() {
            return errorCallback;
        }

        public void setErrorCallback(ErrorCallback errorCallback) {
            this.errorCallback = errorCallback;
        }

        public CacheException getError() {
            return error;
        }

        public void setError(CacheException error) {
            this.error = error;
        }


    }

    /**
     * 缓存协议头
     *
     */
    public class CacheHeader implements Serializable {

        private String key;

        private long expired;

        private long createTime;

        public CacheHeader(String key, long expired, long createTime) {
            super();
            this.key = key;
            this.expired = expired;
            this.createTime = createTime;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public long getExpired() {
            return expired;
        }

        public void setExpired(long expired) {
            this.expired = expired;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }


    }

    /**
     * 缓存协议包
     *
     */
    public class CachePacket implements Serializable {

        private CacheHeader header;

        private Object content;

        public CachePacket(CacheHeader header, Object content) {
            this.header = header;
            this.content = content;
        }

        public CacheHeader getHeader() {
            return header;
        }

        public void setHeader(CacheHeader header) {
            this.header = header;
        }

        public Object getContent() {
            return content;
        }

        public void setContents(Object content) {
            this.content = content;
        }


    }
}
