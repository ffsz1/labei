package com.tongdaxing.xchat_core.im.state;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by chenran on 2017/8/23.
 */

public interface IPhoneCallStateCore extends IBaseCore{
    void callStateChanged(String state);
    PhoneCallStateCoreImpl.PhoneCallStateEnum getPhoneCallState();
}
