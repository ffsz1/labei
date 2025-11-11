package com.tongdaxing.xchat_core.im.notification;

import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by zhouxiangfeng on 2017/6/19.
 */

public interface INotificationCore extends IBaseCore {

    void sendCustomNotification(CustomNotification notification);

    void observeCustomNotification(boolean register);

}
