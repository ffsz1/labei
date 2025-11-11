package com.tongdaxing.xchat_framework.im;

import com.tongdaxing.xchat_framework.util.util.LogUtils;

abstract public class IMProCallBack extends IMCallBack {
    @Override
    public void onSuccess(String data) {
        IMReportBean imReportBean = new IMReportBean(data);
        LogUtils.d("request_info_im_onSuccess", data);
        onSuccessPro(imReportBean);
    }

    public abstract void onSuccessPro(IMReportBean imReportBean);
}
