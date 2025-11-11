package com.tongdaxing.xchat_core.msg;

import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

/**
 * Created by zhouxiangfeng on 2017/3/11.
 */

public class MsgCoreImpl extends AbstractBaseCore implements ImsgCore{
    @Override
    public void getMsgList() {
        notifyClients(ImsgClient.class, ImsgClient.METHOD_ON_GET_MSG);
    }
}
