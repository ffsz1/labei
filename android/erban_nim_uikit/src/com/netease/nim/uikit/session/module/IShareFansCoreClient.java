package com.netease.nim.uikit.session.module;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

public interface IShareFansCoreClient extends ICoreClient {
    public static final String onShareFansJoin = "onShareFansJoin";


    void onShareFansJoin(long uid);


}