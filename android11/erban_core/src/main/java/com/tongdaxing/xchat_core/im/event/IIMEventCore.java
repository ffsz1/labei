package com.tongdaxing.xchat_core.im.event;

import com.netease.nimlib.sdk.event.model.Event;
import com.netease.nimlib.sdk.event.model.EventSubscribeRequest;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by zhouxiangfeng on 2017/5/21.
 */

public interface IIMEventCore extends IBaseCore {
    /**
     * 发布事件
     * @param var1
     * @return
     */
    void publishEvent(Event var1);

    /**
     * 订阅事件
     * @param var1
     * @return
     */
    void subscribeEvent(EventSubscribeRequest var1);

    /**
     * 取消订阅
     * @param var1
     * @return
     */
    void unSubscribeEvent(EventSubscribeRequest var1);


    void batchUnSubscribeEvent(EventSubscribeRequest var1);

    /**
     * 查询订阅事件
     * @param var1
     * @return
     */
    void querySubscribeEvent(EventSubscribeRequest var1);

    /**
     * 监听事件变化
     */
    void registerEventChanged();
}
