package com.tongdaxing.xchat_core.im.friend;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/5/19.
 */

public interface IIMFriendCoreClient extends ICoreClient {

    String METHOD_ON_FRIEND_LIST_UPDATE = "onFriendListUpdate";

    String METHOD_ON_REQUEST_FRIEND = "onRequestFriend";

    String METHOD_ON_REQUEST_FRIEND_FAITH = "onRequestFriendFaith";

    String addBlackListSuccess = "addBlackListSuccess";

    String removeBlackListSuccess = "removeBlackListSuccess";

    void onFriendListUpdate(List<NimUserInfo> userInfos);

    void onRequestFriend(List<NimUserInfo> userInfos);

    void onRequestFriendFaith();

    void addBlackListSuccess();

    void removeBlackListSuccess();
}
