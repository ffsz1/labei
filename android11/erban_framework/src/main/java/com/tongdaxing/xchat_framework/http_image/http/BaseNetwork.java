package com.tongdaxing.xchat_framework.http_image.http;

import android.os.SystemClock;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongyongsheng on 14-6-9.
 */
public class BaseNetwork implements Network {

    protected static final long DEFAULT_PROGRESS_PERCENT = 100;
    protected static final int SLOW_REQUEST_THRESHOLD_MS = 3000;

    protected long progressPercent = DEFAULT_PROGRESS_PERCENT;
    protected long progressStep = 0;
    protected HttpUriRequest mHttpUriRequest;

    protected static void attemptRetryOnException(String logPrefix, Request<?> request,
                                                  RequestError exception) throws RequestError {
        RetryPolicy retryPolicy = request.getRetryPolicy();
        int oldTimeout = request.getTimeoutMs();

        try {
            retryPolicy.retry(exception);
        } catch (RequestError e) {
            HttpLog.e("%s timeout giveup,timeout=%s", logPrefix, oldTimeout);
            throw e;
        }
        HttpLog.v("%s retry,timeout=%s", logPrefix, oldTimeout);
    }

    protected static Map<String, String> convertHeaders(Header[] headers) {
        Map<String, String> result = new HashMap<String, String>();
        for (int i = 0, len = headers.length; i < len; i++) {
            result.put(headers[i].getName(), headers[i].getValue());
        }
        return result;
    }

    protected static void addHeaders(HttpUriRequest httpRequest, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpRequest.setHeader(entry.getKey(), entry.getValue());
        }
    }

    protected static HttpUriRequest createHttpRequest(Request<?> request,
                                                      Map<String, String> additionalHeaders) throws AuthFailureError {
        switch (request.getMethod()) {
            case Request.Method.GET:
                return new HttpGet(request.getUrl());
            case Request.Method.POST:
                HttpPost postRequest = new HttpPost(request.getUrl());
                postRequest.setEntity(request.getPostEntity());
                return postRequest;
            default:
                HttpLog.e("Unknown request method.");
                return new HttpGet(request.getUrl());
        }
    }

    @Override
    public ResponseData performRequest(Request<?> request) throws RequestError {
        long requestStart = SystemClock.elapsedRealtime();
        while (true) {
            HttpResponse httpResponse = null;
            byte[] responseContents = null;
            Map<String, String> responseHeaders = new HashMap<String, String>();
            try {
                Map<String, String> headers = new HashMap<String, String>();
                addCacheHeaders(headers, request.getCacheEntry());

                httpResponse = executeRequest(request, headers);

                StatusLine statusLine = httpResponse.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                HttpLog.v("Network status code is %d", statusCode);

                responseHeaders = convertHeaders(httpResponse.getAllHeaders());
                if (statusCode == HttpStatus.SC_NOT_MODIFIED) {
                    return new ResponseData(HttpStatus.SC_NOT_MODIFIED,
                            request.getCacheEntry().getData(), responseHeaders, true);
                }

                if (httpResponse.getEntity() != null) {
                    responseContents = entityToBytes(request, httpResponse);
                } else {
                    responseContents = new byte[0];
                }

                long requestLifetime = SystemClock.elapsedRealtime() - requestStart;
                logSlowRequests(requestLifetime, request, statusLine);

                if (statusCode < 200 || statusCode > 299) {
                    HttpLog.e(new String(responseContents));
                    throw new IOException();
                }
                return new ResponseData(statusCode, responseContents, responseHeaders, false);
            } catch (SocketTimeoutException e) {
                if (request.getMethod() == Request.Method.GET) { // modify by lijun, 2015/8/3
                    attemptRetryOnException("Socket", request, new TimeoutError());
                }
            } catch (ConnectTimeoutException e) {
                if (request.getMethod() == Request.Method.GET) { // modify by lijun, 2015/8/3
                    attemptRetryOnException("Connection", request, new TimeoutError());
                }
            } catch (MalformedURLException e) {
                HttpLog.e(e, "Bad URL " + request.getUrl());
                throw new RuntimeException("Bad URL " + request.getUrl(), e);
            } catch (IOException e) {
                int statusCode = 0;
                ResponseData responseData = null;
                if (httpResponse != null) {
                    statusCode = httpResponse.getStatusLine().getStatusCode();
                } else {
                    HttpLog.e("no connection error");
                    throw new NoConnectionError(e);
                }
                HttpLog.e(e, "Unexpected response code %d for %s", statusCode, request.getUrl());
                if (responseContents != null) {
                    responseData = new ResponseData(statusCode, responseContents,
                            responseHeaders, false);

                    // comment by lijun, 2015/8/3
                    // do not retry for these cases
//                    if (statusCode == HttpStatus.SC_UNAUTHORIZED ||
//                            statusCode == HttpStatus.SC_FORBIDDEN) {
//                        attemptRetryOnException("auth",
//                                request, new AuthFailureError(responseData));
//                    } else {
                        throw new ServerError(responseData);
//                    }
                } else {
                    throw new NetworkError(responseData);
                }
            }
        }
    }

    protected void logSlowRequests(long requestLifetime, Request<?> request,
                                   StatusLine statusLine) {
        if (requestLifetime > SLOW_REQUEST_THRESHOLD_MS) {
            HttpLog.d("Slow request lifetime=%d, " +
                            "sc=%d, retryCount=%s, request=%s ", requestLifetime,
                    statusLine.getStatusCode(), request.getRetryPolicy().getCurrentRetryCount(),
                    request);
        }
    }

    protected void addCacheHeaders(Map<String, String> headers, Cache.Entry entry) {
        if (entry == null) {
            return;
        }

        if (entry.getEtag() != null) {
            headers.put("If-None-Match", entry.getEtag());
        }

        if (entry.getServerDate() > 0) {
            Date refTime = new Date(entry.getServerDate());
            headers.put("If-Modified-Since", DateUtils.formatDate(refTime));
        }
    }

    public byte[] entityToBytes(Request<?> request, HttpResponse httpResponse) throws IOException, ServerError {
        HttpEntity entity = httpResponse.getEntity();
        ByteArrayPool pool = request.getRequestProcessor().getByteArrayPool();
        PoolingByteArrayOutputStream bytes =
                new PoolingByteArrayOutputStream(pool, (int) entity.getContentLength());
        byte[] buffer = null;
        try {
            InputStream in = entity.getContent();
            if (in == null) {
                throw new ServerError();
            }
            buffer = pool.getBuf(1024);
            int count;
            long progress = 0;
            long total = entity.getContentLength();

            while ((count = in.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
                progress += count;
                if (needProgress(count, total, request)) {
                    ProgressInfo progressInfo = new ProgressInfo(progress, total);
                    request.postProgress(progressInfo);
                }
            }
            return bytes.toByteArray();
        } catch (OutOfMemoryError oom) {
            HttpLog.e(oom, "Bytes.toByteArray from network oom.");
            System.gc();
            return new byte[0];
        } finally {
            try {
                entity.consumeContent();
            } catch (IOException e) {
                HttpLog.v("entity to bytes consumingContent error");
            }
            pool.returnBuf(buffer);
            bytes.close();
        }
    }

    protected boolean needProgress(long count, long total, Request<?> request) {
        if (request.getProgressListener() == null) {
            return false;//没有进度listener,不广播进度
        }
        if (total < 0) {
            return false; //没有取到文件长度,不广播进度
        }
        this.progressStep += count;
        if (this.progressStep > total / progressPercent || count >= total) {
            this.progressStep = 0;
            return true;
        }
        return false;
    }

    public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {
        mHttpUriRequest = createHttpRequest(request, additionalHeaders);
        addHeaders(mHttpUriRequest, additionalHeaders);
        addHeaders(mHttpUriRequest, request.getHeaders());
        HttpParams httpParams = mHttpUriRequest.getParams();
        int timeoutMs = request.getTimeoutMs();
        // wifi 3g 区分

        HttpConnectionParams.setConnectionTimeout(httpParams, BaseHttpClient.SOCKET_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
        return BaseHttpClient.getHttpClient().execute(mHttpUriRequest);
    }

    public void abort() {
        mHttpUriRequest.abort();
    }
}
