package com.tongdaxing.xchat_framework.http_image.http;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认http参数实现
 *
 * @author zhongyongsheng on 14-6-12.
 */
public class DefaultRequestParam implements RequestParam {

    protected Map<String, String> urlParams;
    protected Map<String, FileWrapper> fileParams;
    protected Map<String, List<String>> urlParamsWithArray;
    protected Map<String, FileData> fileDataParams;
    protected String mParamEncoding = DEFAULT_PARAMS_ENCODING;
    protected boolean mNoExpire;

    /**
     * 默认http参数实现
     */
    public DefaultRequestParam() {
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
        urlParamsWithArray = new ConcurrentHashMap<String, List<String>>();
        fileDataParams = new ConcurrentHashMap<String, FileData>();
    }

    @Override
    public Map<String, String> getUrlParams() {
        return urlParams;
    }

    @Override
    public Map<String, FileWrapper> getFileParams() {
        return fileParams;
    }

    @Override
    public Map<String, List<String>> getUrlParamsWithArray() {
        return urlParamsWithArray;
    }

    @Override
    public Map<String, FileData> getFileDataParams() {
        return fileDataParams;
    }

    @Override
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    @Override
    public void put(String key, FileWrapper file) {
        if (key != null && file != null) {
            fileParams.put(key, file);
        }
    }

    @Override
    public void put(String key, FileData fileData) {
        if (key != null && fileData != null) {
            fileDataParams.put(key, fileData);
        }
    }

    @Override
    public void put(String key, List<String> values) {
        if (key != null && values != null) {
            urlParamsWithArray.put(key, values);
        }
    }

    @Override
    public void add(String key, String value) {
        if (key != null && value != null) {
            List<String> paramArray = urlParamsWithArray.get(key);
            if (paramArray == null) {
                paramArray = new ArrayList<String>();
                this.put(key, paramArray);
            }
            paramArray.add(value);
        }
    }

    @Override
    public void remove(String key) {
        urlParams.remove(key);
        fileParams.remove(key);
        urlParamsWithArray.remove(key);
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
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for (Map.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }

        for (Map.Entry<String, FileData> entry : fileDataParams.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }

            result.append(entry.getKey());
            result.append("=");
            result.append("FILEDATA");
        }

        for (Map.Entry<String, List<String>> entry : urlParamsWithArray.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }

            List<String> values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                if (i != 0) {
                    result.append("&");
                }
                result.append(entry.getKey());
                result.append("=");
                result.append(values.get(i));
            }
        }

        return result.toString();
    }

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

        for (Map.Entry<String, String> entry : getUrlParams().entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        for (Map.Entry<String, List<String>> entry : getUrlParamsWithArray().entrySet()) {
            List<String> values = entry.getValue();
            for (String value : values) {
                params.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }

        return params;
    }

    public String getParamString() {
        return URLEncodedUtils.format(getParamsList(), getParamsEncoding());
    }

    public String getParamsEncoding() {
        return mParamEncoding;
    }

    public void setParamsEncoding(String encoding) {
        this.mParamEncoding = encoding;
    }

}
