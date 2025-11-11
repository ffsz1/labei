package com.tongdaxing.xchat_core.utils;

import android.os.Handler;
import android.os.Looper;

import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;
import com.tongdaxing.xchat_framework.util.util.SafeDispatchHandler;


/**
 * @author zhongyongsheng on 2015/5/27.
 */
public class HandlerCoreImpl extends AbstractBaseCore implements IHandlerCore {

    protected final Handler mHandler = new SafeDispatchHandler(Looper.getMainLooper());

    protected static class HandlerCoreRunnable implements Runnable {
        Class<? extends ICoreClient> mClientClass;
        String mMethodName;
        Object[] mArgs;


        protected HandlerCoreRunnable(final Class<? extends ICoreClient> clientClass,
                                  final String methodName,
                                  final Object... args){
            mClientClass = clientClass;
            mMethodName = methodName;
            mArgs = args;
        }

        @Override
        public void run() {
            CoreManager.notifyClients(mClientClass, mMethodName, mArgs);
        }
    }

    @Override
    public void notifyClientsInMainThread(final Class<? extends ICoreClient> clientClass,
                                          final String methodName,
                                          final Object... args) {
        //MLog.verbose(this, "NotifyClientsInMainThread c=%s m=%s", clientClass, methodName);
        mHandler.post(new HandlerCoreRunnable(clientClass, methodName, args));
    }

    @Override
    public void notifyClientsInMainThreadDelayed(int delay, Class<? extends ICoreClient> clientClass, String methodName, Object... args) {
        mHandler.postDelayed(new HandlerCoreRunnable(clientClass, methodName, args), delay);
    }

    @Override
    public void performInMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    @Override
    public void performInMainThread(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }

    public void removeCallbacks(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }
}
