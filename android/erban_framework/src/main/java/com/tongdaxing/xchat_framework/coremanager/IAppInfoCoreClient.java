package com.tongdaxing.xchat_framework.coremanager;

import android.content.Intent;

public interface IAppInfoCoreClient extends ICoreClient{
    public static final String METHOD_ON_SEND_PIC = "sendPictureResult";
    void sendPictureResult(int requestCode, int resultCode, Intent data);
}
