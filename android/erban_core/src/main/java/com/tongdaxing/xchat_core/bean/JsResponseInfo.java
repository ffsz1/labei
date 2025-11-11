package com.tongdaxing.xchat_core.bean;

public class JsResponseInfo {
    private String urlController;
    private boolean isRequestError;
    private String bodyString;
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUrlController() {
        return urlController;
    }

    public void setUrlController(String urlController) {
        this.urlController = urlController;
    }

    public boolean isRequestError() {
        return isRequestError;
    }

    public void setRequestError(boolean requestError) {
        isRequestError = requestError;
    }

    public String getBodyString() {
        return bodyString;
    }

    public void setBodyString(String bodyString) {
        this.bodyString = bodyString;
    }
}
