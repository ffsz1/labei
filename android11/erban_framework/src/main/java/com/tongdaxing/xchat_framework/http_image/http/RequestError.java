package com.tongdaxing.xchat_framework.http_image.http;

/**
 * Created by zhongyongsheng on 14-6-6.
 */
public class RequestError extends Exception {

    public final ResponseData responseData;

    public RequestError() {
        responseData = null;
    }

    public RequestError(ResponseData response) {
        responseData = response;
    }

    public RequestError(String exceptionMessage) {
        super(exceptionMessage);
        responseData = null;
    }

    public RequestError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
        responseData = null;
    }

    public RequestError(Throwable cause) {
        super(cause);
        responseData = null;
    }

    public String getErrorStr(){
        if(null == responseData){
            return "网络不稳定，请稍后再试...";
        }else{
            return responseData.getErrorStr();
        }
    }
}
