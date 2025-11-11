package com.tongdaxing.xchat_core.im.event;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.event.EventSubscribeService;
import com.netease.nimlib.sdk.event.EventSubscribeServiceObserver;
import com.netease.nimlib.sdk.event.model.Event;
import com.netease.nimlib.sdk.event.model.EventSubscribeRequest;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/5/21.
 */

public class IMEventCoreImpl extends AbstractBaseCore implements IIMEventCore {

    @Override
    public void publishEvent(Event event) {
        NIMClient.getService(EventSubscribeService.class).publishEvent(event);
    }

    @Override
    public void subscribeEvent(EventSubscribeRequest eventSubscribeRequest) {
        NIMClient.getService(EventSubscribeService.class).subscribeEvent(eventSubscribeRequest).setCallback(new RequestCallbackWrapper<List<String>>() {
            @Override
            public void onResult(int code, List<String> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS) {
                    if (result != null) {
                        // 部分订阅失败的账号。。。
                        //
                        //
                    }
                } else {

                }
            }
        });
    }

    @Override
    public void unSubscribeEvent(EventSubscribeRequest eventSubscribeRequest) {
        NIMClient.getService(EventSubscribeService.class).unSubscribeEvent(eventSubscribeRequest);
    }

    /**
     * 批量取消订阅 文档暂时没有
     * @param var1
     */
    @Override
    public void batchUnSubscribeEvent(EventSubscribeRequest var1) {

    }

    @Override
    public void querySubscribeEvent(EventSubscribeRequest request) {
        NIMClient.getService(EventSubscribeService.class).querySubscribeEvent(request);
    }

    @Override
    public void registerEventChanged() {
        NIMClient.getService(EventSubscribeServiceObserver.class).observeEventChanged(new Observer<List<Event>>() {
            @Override
            public void onEvent(List<Event> events) {
                // 处理

            }
        }, true);
    }
}
