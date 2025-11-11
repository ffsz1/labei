package com.juxiao.xchat.base.web;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * web service接口返回对象
 *
 * @class: WebServiceMessage.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebServiceMessage {
    private static final Logger logger = LoggerFactory.getLogger(WebServiceMessage.class);
    /**
     * 返回代码
     */
    private int code;
    /**
     * 返回数据
     */
    private Object data;
    /**
     * 返回代码描述信息
     */
    private String message;

    public WebServiceMessage(int code, String message) {
        this.code = code;
        if (message == null) {
            return;
        }

        if (message.contains(":")) {
            this.message = message;
            return;
        }

        this.message = code + ":" + message;
    }

    public WebServiceMessage(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        if (message == null) {
            return;
        }
        if (message.contains(":")) {
            this.message = message;
            return;
        }
        this.message = code + ":" + message;
    }

    public WebServiceMessage(int code, String message,boolean noCode) {
        this.code = code;
        if (message == null) {
            return;
        }
        if (message.contains(":") || noCode == true) {
            this.message = message;
            return;
        }
        this.message = code + ":" + message;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * 返回成功数据
     *
     * @param data
     * @return
     * @author: chenjunsheng
     * @date 2018/5/16
     */
    public static WebServiceMessage success(Object data) {
        return new WebServiceMessage(WebServiceCode.SUCCESS.getValue(), data, WebServiceCode.SUCCESS.getMessage());
    }

    public static WebServiceMessage success(Object data, WebServiceCode code) {
        return new WebServiceMessage(code.getValue(), data, code.getMessage());
    }

    /**
     * 返回失败消息
     *
     * @param
     * @return
     */
    public static WebServiceMessage failure(WebServiceCode code, Integer state) {
        return new WebServiceMessage(code.getValue(), state + "-" + code.getMessage());
    }

    /**
     * 返回失败消息
     *
     * @param code
     * @return
     * @author: chenjunsheng
     * @date 2018/5/16
     */
    public static WebServiceMessage failure(WebServiceCode code) {
        return new WebServiceMessage(code.getValue(), code.getMessage());
    }

    /**
     * 返回失败消息
     *
     * @param msg
     * @return
     * @author: chenjunsheng
     * @date 2018/5/16
     */
    public static WebServiceMessage failure(String msg) {
        return new WebServiceMessage(500,msg);
    }

    /**
     * 返回失败消息
     *
     * @param e
     * @return
     * @author: chenjunsheng
     * @date 2018/5/16
     */
    public static WebServiceMessage failure(WebServiceException e) {
        logger.info("[ WebServiceMessage ]", e);
        if (e != null && e.getCode() != null) {
            return e.getCode();
        }

        return new WebServiceMessage(500, e.getMessage());
    }



}
