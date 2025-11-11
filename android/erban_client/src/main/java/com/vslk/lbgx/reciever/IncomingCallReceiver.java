package com.vslk.lbgx.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.tongdaxing.xchat_core.im.state.IPhoneCallStateCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * Created by zhouxiangfeng on 2017/5/31.
 */

public class IncomingCallReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
            final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            CoreManager.getCore(IPhoneCallStateCore.class).callStateChanged(state);
        }
    }
}
