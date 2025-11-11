package com.tongdaxing.xchat_core.count;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by seven on 2017/8/8.
 */

public interface ICountCoreClient extends ICoreClient {
    public  static  final String METHOD_GET_ON_AVGCHATTIME ="onAvgChattime";
    public  static  final String METHOD_GET_ON_AVGCHATTIME_FAIL ="onAvgChattimeFail";


    void onAvChattime();
    void onAvChattimeFail();


}
