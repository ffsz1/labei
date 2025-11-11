package com.xchat.oauth2.service.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author liuguofu
 */
public class ServiceResult<T> implements Serializable {

    private static final long serialVersionUID = -1954065564856833013L;

    public static final int SC_SUCCESS = 0;
    public static final int SC_NON_AUTH = 203;

    public static final int SC_BAD_REQUEST = 400;
    public static final int SC_FORBIDDEN = 403;
    public static final int SC_TIMEOUT = 408;
    public static final int SC_GONE = 410;

    public static final int SC_SERVER_ERROR = 500;
    public static final int SC_DATA_ERROR = 501;

    private int code = -1;

    private String message = "";

    @JsonInclude(Include.NON_NULL)
    private T data;

    public ServiceResult() {
    }

    public static <T> ServiceResult<T> success(T data) {
        return success(data, StringUtils.EMPTY);
    }

    public static <T> ServiceResult<T> success(T data, String message) {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCodeSuccess().setMessage(message).setData(data);
        return result;
    }

    public static <T> ServiceResult<T> getInstance() {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCodeSuccess();
        return result;
    }

    public static <T> ServiceResult<T> authFailed(String message) {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCode(SC_NON_AUTH).setMessage(message);
        return result;
    }

    public static <T> ServiceResult<T> badRequest(String message) {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCode(SC_BAD_REQUEST).setMessage(message);
        return result;
    }

    public static <T> ServiceResult<T> forbidden(String message) {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCodeForbidden().setMessage(message);
        return result;
    }

    public static <T> ServiceResult<T> timeout(String message) {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCodeTimeout().setMessage(message);
        return result;
    }

    public static <T> ServiceResult<T> serverError(String message) {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCode(SC_SERVER_ERROR).setMessage(message);
        return result;
    }

    public static <T> ServiceResult<T> dataError(String message) {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCode(SC_DATA_ERROR).setMessage(message);
        return result;
    }

    public static <T> ServiceResult<T> gone(String message) {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCode(SC_GONE).setMessage(message);
        return result;
    }

    public int getCode() {
        return code;
    }

    public ServiceResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public ServiceResult<T> setCodeSuccess() {
        this.code = SC_SUCCESS;
        return this;
    }

    public ServiceResult<T> setCodeForbidden() {
        this.code = SC_FORBIDDEN;
        return this;
    }

    public ServiceResult<T> setCodeServerError() {
        this.code = SC_SERVER_ERROR;
        return this;
    }

    public ServiceResult<T> setCodeTimeout() {
        this.code = SC_TIMEOUT;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ServiceResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ServiceResult<T> setData(T data) {
        this.data = data;
        return this;
    }

}
