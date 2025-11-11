package com.tongdaxing.xchat_core.im.state;

import android.telephony.TelephonyManager;
import android.util.Log;

import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

/**
 * Created by huangjun on 2015/5/13.
 */
public class PhoneCallStateCoreImpl extends AbstractBaseCore implements IPhoneCallStateCore {

    public enum PhoneCallStateEnum {
        IDLE,           // 空闲
        INCOMING_CALL,  // 有来电
        DIALING_OUT,    // 呼出电话已经接通
        DIALING_IN      // 来电已接通
    }

    private final String TAG = "PhoneCallStateCoreImpl";

    private int phoneState = TelephonyManager.CALL_STATE_IDLE;
    private PhoneCallStateEnum stateEnum = PhoneCallStateCoreImpl.PhoneCallStateEnum.IDLE;
    ;

    @Override
    public void callStateChanged(String state) {
        Log.i(TAG, "onCallStateChanged, now state =" + state);
        stateEnum = PhoneCallStateEnum.IDLE;
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            phoneState = TelephonyManager.CALL_STATE_IDLE;
            stateEnum = PhoneCallStateEnum.IDLE;
        } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            phoneState = TelephonyManager.CALL_STATE_RINGING;
            stateEnum = PhoneCallStateEnum.INCOMING_CALL;
        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
            int lastPhoneState = phoneState;
            phoneState = TelephonyManager.CALL_STATE_OFFHOOK;
            if (lastPhoneState == TelephonyManager.CALL_STATE_IDLE) {
                stateEnum = PhoneCallStateEnum.DIALING_OUT;
            } else if (lastPhoneState == TelephonyManager.CALL_STATE_RINGING) {
                stateEnum = PhoneCallStateEnum.DIALING_IN;
            }
        }

        notifyClients(IPhoneCallStateClient.class, IPhoneCallStateClient.METHOD_ON_PHONE_STATE_CHANGED, stateEnum);
    }

    @Override
    public PhoneCallStateEnum getPhoneCallState() {
        return stateEnum;
    }
}
