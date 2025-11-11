package com.tongdaxing.xchat_core.utils;


import com.tongdaxing.xchat_framework.coremanager.IBaseCore;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Handler相关逻辑处理
 * @author zhongyongsheng on 2015/5/27.
 */
public interface IHandlerCore extends IBaseCore {

    /**
     * 在主线程里发送广播
     * @param clientClass
     * @param methodName
     * @param args
     */
    void notifyClientsInMainThread(Class<? extends ICoreClient> clientClass, String methodName, Object... args);

    void notifyClientsInMainThreadDelayed(int delay, Class<? extends ICoreClient> clientClass, String methodName, Object... args);

    void performInMainThread(Runnable runnable);

    /**
     * 延迟 秒 发送
     * @param runnable
     * @param delay
     */
    void performInMainThread(Runnable runnable, long delay);

    /**
     * 移除任务
     * @param runnable
     */
    void removeCallbacks(Runnable runnable);
}
