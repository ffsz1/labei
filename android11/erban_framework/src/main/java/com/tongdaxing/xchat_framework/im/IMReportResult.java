package com.tongdaxing.xchat_framework.im;

import java.io.Serializable;

public class IMReportResult<T> implements Serializable{
    public static final int SC_SUCCESS = 0;

    private int errno = -1;

    private String errmsg = "";

    private T data;

    public IMReportResult() {
    }

    public  boolean isSuccess() {
        return errno == SC_SUCCESS;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
