package com.tongdaxing.xchat_core.linked;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by chenran on 2017/8/14.
 */

public interface ILinkedCoreClient extends ICoreClient {
    public static final String METHOD_ON_LINKED_INFO_UPDATE = "onLinkedInfoUpdate";
    public static final String METHOD_ON_LINKED_INFO_UPDATE_NOT_LOGIN = "onLinkedInfoUpdateNotLogin";

    void onLinkedInfoUpdate(LinkedInfo linkedInfo);

    void onLinkedInfoUpdateNotLogin();
}
