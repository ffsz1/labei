package com.tongdaxing.xchat_framework.im;

public interface ICommonListener {

    void onDisconnectCallBack(IMErrorBean error);

    void onNoticeMessage(String message);
}
