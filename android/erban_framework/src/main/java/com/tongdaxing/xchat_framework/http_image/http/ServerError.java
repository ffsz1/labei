package com.tongdaxing.xchat_framework.http_image.http;

/**
 * Created by zhongyongsheng on 14-6-10.
 */
public class ServerError extends RequestError {
    public ServerError(ResponseData responseData) {
        super(responseData);
    }

    public ServerError() {
        super();
    }
}
