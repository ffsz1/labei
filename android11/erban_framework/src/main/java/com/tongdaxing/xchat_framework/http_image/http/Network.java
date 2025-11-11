package com.tongdaxing.xchat_framework.http_image.http;

/**
 * Created by zhongyongsheng on 14-4-4.
 */
public interface Network {

    public ResponseData performRequest(Request<?> request) throws RequestError;
}
