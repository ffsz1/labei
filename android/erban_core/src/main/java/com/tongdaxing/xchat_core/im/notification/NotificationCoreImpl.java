package com.tongdaxing.xchat_core.im.notification;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;


/**
 * Created by zhouxiangfeng on 2017/6/19.
 */

public class NotificationCoreImpl extends AbstractBaseCore implements INotificationCore {
    @Override
    public void sendCustomNotification(CustomNotification notification) {
        NIMClient.getService(MsgService.class).sendCustomNotification(notification);
    }

    @Override
    public void observeCustomNotification(boolean register) {
        // 如果有自定义通知是作用于全局的，不依赖某个特定的 Activity，那么这段代码应该在 Application 的 onCreate 中就调用
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(new Observer<CustomNotification>() {
            @Override
            public void onEvent(CustomNotification notification) {




                String content = notification.getContent();
                JSONObject object = JSONObject.parseObject(notification.getContent());
                notifyClients(INotificationCoreClient.class, INotificationCoreClient.METHOD_ON_RECEIVED_CUSTOM_NOTIFICATION, object);
            }
        }, register);
    }
}
