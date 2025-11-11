package com.tongdaxing.xchat_framework.http_image.http;


/**
 * 查询url数据,返回BytesWrapper
 *
 * @author zhongyongsheng
 */
public class BytesQueryRequest<BytesWrapper> extends BaseRequest {

    public BytesQueryRequest(Cache cache,
                             String url,
                             ResponseListener<String> successListener,
                             ResponseErrorListener errorListener,
                             ProgressListener progressListener) {
        super(cache, url, successListener, errorListener, progressListener);
    }

    @Override
    public void parseDataToResponse(ResponseData responseData) {
        HttpLog.v(((Object) this).getClass().getName() + " parse network response");

        BytesQueryRequest.BytesWrapper wrapper = new BytesQueryRequest.BytesWrapper();
        wrapper.data = responseData.data;

        mResponse = Response.success(wrapper, HttpHeaderParser.parseCacheHeaders(responseData, mNoExpire));
    }

    public static class BytesWrapper {
        public byte[] data;
    }
}
