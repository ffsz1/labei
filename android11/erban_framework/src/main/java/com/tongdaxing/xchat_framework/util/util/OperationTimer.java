package com.tongdaxing.xchat_framework.util.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by lijun on 2015/1/29.
 */
public class OperationTimer extends Handler {

    private static final long SHORT_TIME = 600;
    public static final int CLICK_MESSAGE = 100;
    private Callback mCallback;

    private long mShortTime = SHORT_TIME;

    public OperationTimer(long mShortTime, Callback callback) {
        this(callback);
        this.mShortTime = mShortTime;
    }

    public OperationTimer(Callback callback) {
        this.mCallback = callback;
    }

    public void setShortTime(long shortTime) {
        this.mShortTime = shortTime;
    }

    public void notifyClick() {
        removeMessages(CLICK_MESSAGE);
        sendEmptyMessageDelayed(CLICK_MESSAGE, mShortTime);
    }

    public void stopNotifyClick() {
        removeMessages(CLICK_MESSAGE);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case CLICK_MESSAGE:
                if (null != mCallback) {
                    mCallback.onRealClick();
                }
                break;
            default:
                break;
        }
    }

    public interface Callback {
        public void onRealClick();
    }
}
