package com.tongdaxing.xchat_core.common;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by chenran on 2017/8/9.
 */

public interface ICommonClient extends ICoreClient{
    public static final String METHOD_ON_RECIEVE_NEED_RECHARGE = "onRecieveNeedRecharge";
    public static final String METHOD_ON_RECIEVE_NEED_REFRESH_WEBVIEW = "onRecieveNeedRefreshWebView";

    void onRecieveNeedRecharge();
    void onRecieveNeedRefreshWebView();
}
