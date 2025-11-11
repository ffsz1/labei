package com.tongdaxing.xchat_core.im.login;

import com.netease.nimlib.sdk.auth.OnlineClient;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by chenran on 2017/2/16.
 */

public interface IIMLoginCore extends IBaseCore {

    /**
     * 登录Im
     * @param account
     * @param token
     */
    void login(String account, String token);

    void logout();

    boolean isImLogin();

    void kickOtherClient(OnlineClient onlineClient);

    /**
     * 注册其他端登录事件
     */
    void registerOtherClientsObserver(boolean isRegister);

    /**
     * 注册AuthService观察者，监听回调
     */
    void registAuthServiceObserver(boolean isRegister);
}
