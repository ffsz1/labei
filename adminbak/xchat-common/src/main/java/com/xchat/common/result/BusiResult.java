package com.xchat.common.result;

import com.xchat.common.status.BusiStatus;

/**
 * Created by liuguofu on 2017/4/30.
 */
public class BusiResult<T> {
    private int code;
    private String message;
    private T data;

    public BusiResult(){}

    public BusiResult(BusiStatus status){
        this(status, status.getReasonPhrase(),null);
    }

    public BusiResult(BusiStatus status,T data){
        this.code = status.value();
        this.message = status.getReasonPhrase();
        this.data = data;
    }

    public BusiResult(BusiStatus status, String message, T data){
        this.code = status.value();
        this.message = message;
        this.data = data;
    }

    public BusiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static BusiResult SUCCESS(Object obj) {
        return new BusiResult(200, "success", obj);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }
}
