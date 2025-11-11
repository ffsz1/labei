package com.tongdaxing.xchat_core.im.login;

import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by chenran on 2017/2/16.
 */

public interface IIMLoginClient extends ICoreClient {
    public static final String METHOD_ON_IM_LOGIN_SUCCESS = "onImLoginSuccess" ;
    public static final String METHOD_ON_IM_SYNC_SUCCESS = "onImSyncSuccess" ;
    public static final String METHOD_ON_IM_LOGIN_FAITH = "onImLoginFaith" ;
    public static final String METHOD_ON_KICK_OTHER = "onKickOther" ;
    public static final String METHOD_ON_KICKED_OUT = "onKickedOut";

    void onImLoginSuccess(LoginInfo loginInfo);

    void onImLoginFaith(String error);

    void onImSyncSuccess();

    /**
     * 踢出其他端
     */
    void onKickOther(OnlineClient onlineClient);

    /**
     * 被踢出
     */
    void onKickedOut(StatusCode code);
}
