package com.tongdaxing.xchat_core.linked;

import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tongdaxing.xchat_core.im.login.IIMLoginClient;
import com.tongdaxing.xchat_core.im.login.IIMLoginCore;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * Created by chenran on 2017/8/11.
 */

public class LinkedCoreImpl extends AbstractBaseCore implements ILinkedCore {
    private LinkedInfo linkedInfo;

    @Override
    public void setLinkedInfo(LinkedInfo linkedInfo) {
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (linkedInfo != null && CoreManager.getCore(IIMLoginCore.class).isImLogin() && userInfo != null && !StringUtil.isEmpty(userInfo.getNick())) {
            notifyClients(ILinkedCoreClient.class, ILinkedCoreClient.METHOD_ON_LINKED_INFO_UPDATE, linkedInfo);
        } else {
            notifyClients(ILinkedCoreClient.class, ILinkedCoreClient.METHOD_ON_LINKED_INFO_UPDATE_NOT_LOGIN);
            this.linkedInfo = linkedInfo;
        }
    }

    @Override
    public LinkedInfo getLinkedInfo() {
        return linkedInfo;
    }

    @CoreEvent(coreClientClass = IIMLoginClient.class)
    public void onImLoginSuccess(LoginInfo loginInfo) {
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (linkedInfo != null && userInfo != null && !StringUtil.isEmpty(userInfo.getNick())) {
            notifyClients(ILinkedCoreClient.class, ILinkedCoreClient.METHOD_ON_LINKED_INFO_UPDATE, linkedInfo);
            linkedInfo = null;
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoComplete(UserInfo userInfo) {
        if (linkedInfo != null) {
            notifyClients(ILinkedCoreClient.class, ILinkedCoreClient.METHOD_ON_LINKED_INFO_UPDATE, linkedInfo);
            linkedInfo = null;
        }
    }
}
