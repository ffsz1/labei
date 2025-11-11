package com.juxiao.xchat.base.web;

/**
 * web service 接口服务异常
 *
 * @class: WebServiceException.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
public class WebServiceException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private WebServiceMessage code;

    public WebServiceException(WebServiceCode code) {
        this.code = new WebServiceMessage(code.getValue(), code.getMessage());
    }

    public WebServiceException(String message) {
        super(message);
    }

    public WebServiceException(Throwable cause) {
        super(cause);
    }

    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebServiceException(WebServiceCode code, String... args) {
        this.code = new WebServiceMessage(code.getValue(), String.format(code.getMessage(), args));
    }

    public WebServiceException(WebServiceCode code, boolean noCode) {
        this.code = new WebServiceMessage(code.getValue(),code.getMessage(), noCode);
    }

    public WebServiceMessage getCode() {
        return code;
    }
}
