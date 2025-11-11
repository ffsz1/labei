package com.tongdaxing.xchat_core.msg;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by zhouxiangfeng on 2017/3/11.
 */

public interface ImsgClient extends ICoreClient{
    public static final String METHOD_ON_GET_MSG = "onGetMsg";

    void onGetMsg();
}
