package com.tongdaxing.erban.libcommon.net.rxnet;

import android.content.Context;

import com.tongdaxing.xchat_framework.http_image.http.Cache;
import com.tongdaxing.xchat_framework.http_image.http.CacheCleanRequest;
import com.tongdaxing.xchat_framework.http_image.http.CacheShrinkRequest;
import com.tongdaxing.xchat_framework.http_image.http.DefaultRequestProcessor;
import com.tongdaxing.xchat_framework.http_image.http.DiskCache;
import com.tongdaxing.xchat_framework.http_image.http.DownloadRequest;
import com.tongdaxing.xchat_framework.http_image.http.MultipartPostRequest;
import com.tongdaxing.xchat_framework.http_image.http.ProgressListener;
import com.tongdaxing.xchat_framework.http_image.http.RequestParam;
import com.tongdaxing.xchat_framework.http_image.http.RequestProcessor;
import com.tongdaxing.xchat_framework.http_image.http.Response;
import com.tongdaxing.xchat_framework.http_image.http.ResponseErrorListener;
import com.tongdaxing.xchat_framework.http_image.http.ResponseListener;
import com.tongdaxing.xchat_framework.http_image.http.SameThreadRequestProcessor;
import com.tongdaxing.xchat_framework.http_image.http.StringQueryRequest;

import java.util.Map;

/**
 * Http管理器
 *
 * @author zhongyongsheng on 14-6-11.
 */
public class RequestManager {

    private static RequestManager mFactory;
    private RequestProcessor mCommonProcessor;
    private RequestProcessor mSameThreadProcessor;
    private Cache mCache;

    private RequestManager() {
    }

    public static synchronized RequestManager instance() {
        if (mFactory == null) {
            mFactory = new RequestManager();
        }
        return mFactory;
    }

    public static String getUrlWithQueryString(String url, RequestParam params) {
        if (params != null) {
            String paramString = params.getParamString();
            if (paramString != null && paramString.length() > 0) {
                if (url.indexOf("?") == -1) {
                    url += "?" + paramString;
                } else {
                    url += "&" + paramString;
                }
            }
        }
        return url;
    }

    public synchronized void init(Context context, String cacheDir) {
        mCache = new DiskCache(DiskCache.getCacheDir(context, cacheDir), 5 * 1024 * 1024, 0.2f);
        mCache.initialize();

        mCommonProcessor = new DefaultRequestProcessor(2, "Http_");
        mCommonProcessor.start();

        mSameThreadProcessor = new SameThreadRequestProcessor();
    }

    public RequestProcessor getCommonProcessor() {
        return mCommonProcessor;
    }

    public Cache getCache() {
        return mCache;
    }

    /**
     * 创建一个返回String的http请求,并执行
     *
     * @param url             url
     * @param param           url参数
     * @param successListener 成功回调
     * @param errorListener   失败回调
     * @return
     */
    public StringQueryRequest submitStringQueryRequest(String url,
                                                       Map<String,String> headers,
                                                       RequestParam param,
                                                       ResponseListener<String> successListener,
                                                       ResponseErrorListener errorListener) {
        if (url == null || successListener == null || errorListener == null) {
            return null;
        }

        url = getUrlWithQueryString(url, param);
        StringQueryRequest req = new StringQueryRequest(mCache, url, successListener, errorListener);
        req.getHeaders().putAll(headers);

        mCommonProcessor.add(req);
        return req;
    }

    /**
     * 创建一个返回String的http post请求,并执行
     *
     * @param url
     * @param param
     * @param successListener
     * @param errorListener
     * @param progressListener
     * @return
     */
    public MultipartPostRequest submitMultipartPostRequest(String url,
                                                           Map<String,String> headers,
                                                           RequestParam param,
                                                           Class clz,
                                                           ResponseListener<String> successListener,
                                                           ResponseErrorListener errorListener,
                                                           ProgressListener progressListener) {
        if (url == null || param == null || successListener == null || errorListener == null) {
            return null;
        }
        MultipartPostRequest req = new MultipartPostRequest(url, param, clz, successListener, errorListener,
                progressListener);
        req.getHeaders().putAll(headers);
        mCommonProcessor.add(req);
        return req;
    }

    /**
     * 创建一个返回String的http post请求,同步执行
     *
     * @param url
     * @param param
     * @param progressListener
     * @return
     */
    public Response submitMultipartPostRequestSync(String url,
                                                   Map<String,String> headers,
                                                   RequestParam param,
                                                   Class clz,
                                                   ProgressListener progressListener) {
        if (url == null || param == null) {
            return null;
        }
        MultipartPostRequest req = new MultipartPostRequest(url, param, clz, null, null, progressListener);
        req.getHeaders().putAll(headers);
        mSameThreadProcessor.add(req);
        return req.getResponse();
    }

    /**
     * 创建一个返回String的http post请求,并执行
     *
     * @param url
     * @param param
     * @param successListener
     * @param errorListener
     * @return
     */
    public MultipartPostRequest submitMultipartPostRequest(String url,
                                                           RequestParam param,
                                                           Class clz,
                                                           ResponseListener<String> successListener,
                                                           ResponseErrorListener errorListener) {
        if (url == null || param == null || successListener == null || errorListener == null) {
            return null;
        }
        MultipartPostRequest req = new MultipartPostRequest(url, param, clz, successListener, errorListener);
        mCommonProcessor.add(req);
        return req;
    }

    /**
     * 上报崩溃日志
     *
     * @param url
     * @param param
     * @param successListener
     * @param errorListener
     * @param onStartSubmit
     * @return
     */
    public MultipartPostRequest submitCrashReportRequest(String url,
                                                         RequestParam param,
                                                         ResponseListener<String> successListener,
                                                         ResponseErrorListener errorListener,
                                                         boolean onStartSubmit) {
        if (url == null || param == null || successListener == null || errorListener == null) {
            return null;
        }
        MultipartPostRequest req = new MultipartPostRequest(url, param, String.class, successListener, errorListener);
        if (onStartSubmit) {
            mCommonProcessor.add(req);
        } else {
            mCommonProcessor.setHandler(null);
            mCommonProcessor.add(req);
        }
        return req;
    }

    /**
     * 下载文件请求,调用者保证下载到本地文件的目录已经创建好
     *
     * @param url              下载服务器路径
     * @param downloadFilePath 下载本地路径
     * @param successListener  成功回调
     * @param errorListener    失败回调
     * @param progressListener 进度回调
     * @return
     */
    public DownloadRequest submitDownloadRequest(String url,
                                                 Map<String,String> headers,
                                                 String downloadFilePath,
                                                 ResponseListener<String> successListener,
                                                 ResponseErrorListener errorListener,
                                                 ProgressListener progressListener) {
        return submitDownloadRequest(url, headers, downloadFilePath, successListener,
                errorListener, progressListener, false);
    }

    /**
     * 下载文件请求,调用者保证下载到本地文件的目录已经创建好
     *
     * @param url                 下载服务器路径
     * @param downloadFilePath    下载本地路径
     * @param successListener     成功回调
     * @param errorListener       失败回调
     * @param progressListener    进度回调
     * @param useContinueDownload 是否使用断点续传
     * @return
     */
    public DownloadRequest submitDownloadRequest(String url,
                                                 Map<String,String> headers,
                                                 String downloadFilePath,
                                                 ResponseListener<String> successListener,
                                                 ResponseErrorListener errorListener,
                                                 ProgressListener progressListener,
                                                 boolean useContinueDownload) {
        if (url == null
                || downloadFilePath == null
                || successListener == null
                || errorListener == null
                || progressListener == null) {
            return null;
        }
        DownloadRequest req = new DownloadRequest(url, downloadFilePath, successListener,
                errorListener, progressListener, useContinueDownload);
        req.getHeaders().putAll(headers);
        mCommonProcessor.add(req);
        return req;
    }

    /**
     * 清除缓存
     *
     * @param successListener
     * @param errorListener
     * @return
     */
    public CacheCleanRequest submitCacheCleanRequest(ResponseListener<Object> successListener,
                                                     ResponseErrorListener errorListener) {
        CacheCleanRequest req = new CacheCleanRequest(mCache, successListener, errorListener);
        mCommonProcessor.add(req);
        return req;
    }

    /**
     * 压缩缓存空间
     *
     * @param successListener
     * @param errorListener
     * @return
     */
    public CacheShrinkRequest submitCacheShrinkRequest(ResponseListener<Object> successListener,
                                                       ResponseErrorListener errorListener) {
        CacheShrinkRequest req = new CacheShrinkRequest(mCache, successListener, errorListener);
        mCommonProcessor.add(req);
        return req;
    }

}
