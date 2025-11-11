package com.tongdaxing.xchat_framework.util.util.asynctask;

import android.os.Build;
import android.os.HandlerThread;
import android.os.Looper;

import com.tongdaxing.xchat_framework.util.util.SafeDispatchHandler;

/**
 * 异步任务工具。
 */
public final class AsyncTask {
    private Looper mTaskLooper;
    private SafeDispatchHandler mTaskHandler;
    private HandlerThread mThread;

    public AsyncTask() {
        this("AsyncTask");
    }

    public boolean quit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return mThread.quitSafely();
        } else {
            return mThread.quit();
        }
    }

    public AsyncTask(String name) {
        mThread = new HandlerThread(name);
        mThread.start();

        mTaskLooper = mThread.getLooper();
        mTaskHandler = new SafeDispatchHandler(mTaskLooper);

    }

    /**
     * 执行任务，单位milliseconds
     */
    public void execute(Runnable command) {
        mTaskHandler.removeCallbacks(command);
        mTaskHandler.post(command);
    }
}
