package com.tongdaxing.xchat_core.im.notification;

import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by zhouxiangfeng on 2017/6/19.
 */

public interface INotificationCoreClient extends ICoreClient {
    public static final String METHOD_ON_RECEIVED_CUSTOM_NOTIFICATION = "onReceivedCustomNotification";

    void onReceivedCustomNotification(JSONObject attachment);
}
