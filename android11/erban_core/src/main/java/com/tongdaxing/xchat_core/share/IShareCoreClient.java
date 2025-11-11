package com.tongdaxing.xchat_core.share;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by chenran on 2017/8/14.
 */

public interface IShareCoreClient extends ICoreClient{

    String SHARE_SHORT_URL = "/front/share/share.html?shareUid=";
    String SHARE_SHORT_SHARE_UID = "?shareUid=";
    String SHARE_SHORT_UID = "&uid=";


    public static final String METHOD_ON_SHARE_ROOM = "onShareRoom";
    public static final String METHOD_ON_SHARE_ROOM_FAIL = "onShareRoomFail";
    public static final String METHOD_ON_SHARE_ROOM_CANCEL = "onShareRoomCancel";
    public static final String METHOD_ON_SHARE_H5 = "onShareH5";
    public static final String METHOD_ON_HSARE_H5_FAIL = "onShareH5Fail";
    public static final String METHOD_ON_HSARE_H5_CANCEL = "onShareH5Cancel";
    public static final String METHOD_ON_SHARE_REPORT = "onShareReport";
    void onShareRoom();
    void onShareRoomFail();
    void onShareRoomCancel();
    void onShareH5(String url);
    void onShareH5Fail();
    void onShareH5Cancel();
    void onShareReport();
}
