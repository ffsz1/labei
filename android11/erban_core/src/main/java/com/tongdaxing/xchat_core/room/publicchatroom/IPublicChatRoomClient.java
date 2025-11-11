package com.tongdaxing.xchat_core.room.publicchatroom;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by Administrator on 2018/3/20.
 */

public interface IPublicChatRoomClient extends ICoreClient {
    String onMsg = "onMsg";

    void onMsg(String content);
}
