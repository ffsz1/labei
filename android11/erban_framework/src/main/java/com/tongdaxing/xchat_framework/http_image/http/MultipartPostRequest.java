package com.tongdaxing.xchat_framework.http_image.http;


import com.tongdaxing.xchat_framework.http_image.http.form.HttpMultipartMode;
import com.tongdaxing.xchat_framework.http_image.http.form.MultipartEntity;
import com.tongdaxing.xchat_framework.http_image.http.form.ProgressFileBody;
import com.tongdaxing.xchat_framework.http_image.http.form.content.FileDataBody;
import com.tongdaxing.xchat_framework.http_image.http.form.content.StringBody;
import com.tongdaxing.xchat_framework.util.util.json.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * http post 可支持string, file, string array数据上传
 *
 * @author zhongyongsheng
 */
public class MultipartPostRequest<T extends Serializable> extends BaseRequest<T> {

    private static final int TIMEOUT_MS = 6000;
    private static final int MAX_RETRIES = 3;
    private static final float BACKOFF_MULT = 0.4f;

    protected ProgressListener mProgressListener;
    protected RequestParam mRequestParam;

    private Class<T> cls;

    /**
     * @param url
     * @param param
     * @param successListener
     * @param errorListener
     */
    public MultipartPostRequest(String url,
                                RequestParam param,
                                Class clz,
                                ResponseListener<String> successListener,
                                ResponseErrorListener errorListener) {
        this(url, param, clz, successListener, errorListener, null);
    }

    /**
     * @param url
     * @param param
     * @param successListener
     * @param errorListener
     * @param progressListener
     */
    public MultipartPostRequest(String url,
                                RequestParam param,
                                Class clz,
                                ResponseListener<String> successListener,
                                ResponseErrorListener errorListener,
                                ProgressListener progressListener) {
        super(new NoCache(), url, successListener, errorListener);
        this.cls = clz;
        mMethod = Method.POST;
        mProgressListener = progressListener;
        if (mRequestParam == null) {
            mRequestParam = new DefaultRequestParam();
        }
        mRequestParam = param;
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
    }

    @Override
    public void parseDataToResponse(ResponseData responseData) {
        HttpLog.v("MultipartPostRequest parse network response");
        String parsed;
        try {
            parsed = new String(responseData.data, HttpHeaderParser.parseCharset(responseData.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(responseData.data);
        }

        if (cls != null && cls != String.class) {
            T result = JsonParser.parseJsonObject(parsed, cls);
            mResponse = Response.success(result, HttpHeaderParser.parseCacheHeaders(responseData));
        } else {
            mResponse = (Response<T>) Response.success(parsed, HttpHeaderParser.parseCacheHeaders(responseData));
        }
    }

    public void pushProgress(long progress, long total) {
        if (mRequestProcessor != null) {
            mRequestProcessor.getHandler().post(new ProgressDeliveryRunnable(this, mProgressListener,
                    new ProgressInfo(progress, total)));
        }
    }

    @Override
    public HttpEntity getPostEntity() {
        HttpEntity entity = null;

        if (!mRequestParam.getFileParams().isEmpty() || !mRequestParam.getFileDataParams().isEmpty()) {
            MultipartEntity multipartEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);

            for (Map.Entry<String, String> entry : mRequestParam.getUrlParams().entrySet()) {
                try {
                    StringBody stringBody = new StringBody(entry.getValue(), Charset.forName(mRequestParam.getParamsEncoding()));
                    multipartEntity.addPart(entry.getKey(), stringBody);
                } catch (UnsupportedEncodingException e) {
                    HttpLog.e(e, "get post entity error");
                }
            }

            for (Map.Entry<String, List<String>> entry : mRequestParam.getUrlParamsWithArray().entrySet()) {
                List<String> values = entry.getValue();
                for (String value : values) {
                    try {
                        StringBody stringBody = new StringBody(value, Charset.forName(mRequestParam.getParamsEncoding()));
                        multipartEntity.addPart(entry.getKey(), stringBody);
                    } catch (UnsupportedEncodingException e) {
                        HttpLog.e(e, "get post entity error");
                    }
                }
            }

            for (Map.Entry<String, RequestParam.FileWrapper> entry : mRequestParam.getFileParams().entrySet()) {
                RequestParam.FileWrapper file = entry.getValue();
                if (file.getFile() != null) {
                    if (file.getContentType() != null) {
                        ProgressFileBody fileBody = new ProgressFileBody(file.getFile(),
                                file.getFileName(), file.getContentType(), file.getEncoding(), this);
                        multipartEntity.addPart(entry.getKey(), fileBody);
                    } else {
                        ProgressFileBody fileBody = new ProgressFileBody(file.getFile(),
                                file.getFileName(), "application/octet-stream",
                                file.getEncoding(), this);
                        multipartEntity.addPart(entry.getKey(), fileBody);
                    }
                }
            }

            for (Map.Entry<String, RequestParam.FileData> entry : mRequestParam.getFileDataParams().entrySet()) {
                RequestParam.FileData file = entry.getValue();
                if (file.getFileData() != null) {
                    if (file.getContentType() != null) {
                        FileDataBody fileBody = new FileDataBody(file.getFileData(),
                                file.getFileName(), file.getContentType(),
                                file.getEncoding());
                        multipartEntity.addPart(entry.getKey(), fileBody);
                    } else {
                        FileDataBody fileBody = new FileDataBody(file.getFileData(),
                                file.getFileName(), "application/octet-stream",
                                file.getEncoding());
                        multipartEntity.addPart(entry.getKey(), fileBody);
                    }
                }
            }

            entity = multipartEntity;
        } else {
            try {
                entity = new UrlEncodedFormEntity(getParamsList(), mRequestParam.getParamsEncoding());
            } catch (UnsupportedEncodingException e) {
                HttpLog.e(e, "get post entity error");
            }
        }
        return entity;
    }

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

        for (Map.Entry<String, String> entry : mRequestParam.getUrlParams().entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        for (Map.Entry<String, List<String>> entry : mRequestParam.getUrlParamsWithArray().entrySet()) {
            List<String> values = entry.getValue();
            for (String value : values) {
                params.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }

        return params;
    }
}