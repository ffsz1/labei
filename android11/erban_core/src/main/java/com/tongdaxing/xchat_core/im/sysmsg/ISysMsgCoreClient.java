package com.tongdaxing.xchat_core.im.sysmsg;

import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by zhouxiangfeng on 2017/5/19.
 */

public interface ISysMsgCoreClient extends ICoreClient {

    public static final String METHOD_ON_UNREAD_COUNT_CHANGE = "onUnreadCountChange";
    public static final String METHOD_ON_RECEIVE_SYSTEM_MSG = "onReceiveSystemMsg";


    void onUnreadCountChange(Integer integer);

    void onReceiveSystemMsg(SystemMessage message);

}
