package com.tongdaxing.xchat_core.im.message;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by chenran on 2017/8/15.
 */

public interface IIMMessageCoreClient extends ICoreClient {
    public static final String METHOD_ON_RECEIVE_PERSONAL_MESSAGES= "onReceivePersonalMessages";
    public static final String METHOD_ON_RECEIVE_CONTACT_CHANGED= "onReceiveRecentContactChanged";

    void onReceivePersonalMessages(List<IMMessage> imMessages);

    void onReceiveRecentContactChanged(List<RecentContact> imMessages);
}
