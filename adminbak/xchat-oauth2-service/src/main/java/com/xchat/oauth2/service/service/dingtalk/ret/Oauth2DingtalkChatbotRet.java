package com.xchat.oauth2.service.service.dingtalk.ret;

/**
 * @author chris
 * @Title:
 * @date 2018/10/8
 * @time 10:00
 */

public class Oauth2DingtalkChatbotRet {
    private boolean isSuccess;
    private Integer errorCode;
    private String errorMsg;

    public Oauth2DingtalkChatbotRet() {
        this.isSuccess = false;
        this.errorCode = 0;
    }

    public Oauth2DingtalkChatbotRet(boolean isSuccess, Integer errorCode, String errorMsg) {
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
