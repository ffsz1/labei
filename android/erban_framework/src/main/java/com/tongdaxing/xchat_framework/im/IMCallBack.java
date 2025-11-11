package com.tongdaxing.xchat_framework.im;

abstract public class IMCallBack {

    private final int callbackId;

    public static int callbackIndex = 0;

    public abstract void onSuccess(String data);


    public abstract void onError(int errorCode, String errorMsg);

    public IMCallBack() {
        callbackId = callbackIndex++;
        //应该用不上
        if (callbackIndex == Integer.MAX_VALUE) {
            callbackIndex = 0;
        }
    }


    public int getCallbackId() {
        return callbackId;
    }
}
