package com.tongdaxing.xchat_core.im.state;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by zhouxiangfeng on 2017/6/18.
 */

public interface IPhoneCallStateClient extends ICoreClient{


    public static final String METHOD_ON_PHONE_STATE_CHANGED = "onPhoneStateChanged";

    void onPhoneStateChanged(PhoneCallStateCoreImpl.PhoneCallStateEnum phoneCallStateEnum);
}
