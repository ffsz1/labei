package com.tongdaxing.xchat_framework.http_image.http;

/**
 * Http 请求错误的监听器
 *
 * @author zhongyongsheng
 */
public interface ResponseErrorListener {

    /**
     * 返回错误结果,异常类型如下:
     * <p/>
     * {@link com.yy.android.yymusic.http.AuthFailureError}
     * {@link com.yy.android.yymusic.http.NoConnectionError}
     * {@link com.yy.android.yymusic.http.TimeoutError}
     *
     * @param error 错误结果
     */
    public void onErrorResponse(RequestError error);
}
