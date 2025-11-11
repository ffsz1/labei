package com.tongdaxing.xchat_core.linked;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by chenran on 2017/8/11.
 */

public interface ILinkedCore extends IBaseCore {
    void setLinkedInfo(LinkedInfo linkedInfo);
    LinkedInfo getLinkedInfo();
}
