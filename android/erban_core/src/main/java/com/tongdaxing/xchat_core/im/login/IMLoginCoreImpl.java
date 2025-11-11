package com.tongdaxing.xchat_core.im.login;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.DataCacheManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.tongdaxing.xchat_core.DemoCache;
import com.tongdaxing.xchat_core.auth.AccountInfo;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachParser;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.util.List;

import static com.netease.nimlib.sdk.StatusCode.LOGINED;

/**
 * Created by chenran on 2017/2/16.
 */

public class IMLoginCoreImpl extends AbstractBaseCore implements IIMLoginCore {

    private static final String TAG = "IMLoginCoreImpl";
    private LoginInfo loginInfo;
    private int retryCount;
    private StatusCode statuCode;

    public IMLoginCoreImpl() {
        CoreManager.addClient(this);
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser()); //注册自定义Im消息解析器
    }


    @Override
    public boolean isImLogin() {
        return loginInfo != null && statuCode == LOGINED;
    }

    @Override
    public void registerOtherClientsObserver(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(new Observer<List<OnlineClient>>() {
            @Override
            public void onEvent(List<OnlineClient> onlineClients) {
                if (null != onlineClients && onlineClients.size() > 0) {
                    for (OnlineClient client : onlineClients) {
                        kickOtherClient(client);
                        switch (client.getClientType()) {
                            case ClientType.Windows:
                                break;
                            case ClientType.Web:
                                break;
                            case ClientType.Android:
                            case ClientType.iOS:
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }, register);
    }

    @Override
    public void registAuthServiceObserver(boolean isRegister) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {

            @Override
            public void onEvent(StatusCode status) {
                statuCode = status;
                if (status.wontAutoLogin()) {
                    Log.d("mynetease",statuCode.toString());
                    notifyClients(IIMLoginClient.class, IIMLoginClient.METHOD_ON_KICKED_OUT, status);
                } else if (status.shouldReLogin()) {
                    if (loginInfo != null) {
                        login(loginInfo.getAccount(), loginInfo.getToken());
                    }
                }
            }
            }, isRegister);
    }

    @Override
    public void login(final String account, final String token) {
        MLog.info(TAG, "account:" + account + " token:" + token);
        final LoginInfo info = new LoginInfo(account, token);
        NIMClient.getService(AuthService.class).login(info).setCallback(new RequestCallback<LoginInfo>() {

            @Override
            public void onSuccess(LoginInfo loginInfo) {
                if (loginInfo == null) {
                    return;
                }
                retryCount = 0;
                DemoCache.saveLoginInfo(loginInfo);
                IMLoginCoreImpl.this.loginInfo = loginInfo;
                NimUIKit.setAccount(loginInfo.getAccount());
                DataCacheManager.buildDataCacheAsync();
                NimUIKit.getImageLoaderKit().buildImageCache();
                //更新消息提醒配置
                initNotificationConfig();
                notifyClients(IIMLoginClient.class, IIMLoginClient.METHOD_ON_IM_LOGIN_SUCCESS, loginInfo);
            }

            @Override
            public void onFailed(int i) {
                MLog.error(TAG, "失败错误码：" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                MLog.error(TAG, "失败异常信息：" + throwable.toString());
            }
        });
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(true);
    }

    @Override
    public void logout() {
        loginInfo = null;
        NIMClient.getService(AuthService.class).logout();
    }

    @Override
    public void kickOtherClient(final OnlineClient onlineClient) {
        NIMClient.getService(AuthService.class).kickOtherClient(onlineClient).setCallback(new RequestCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLogin(AccountInfo accountInfo) {
        login(String.valueOf(accountInfo.getUid()), accountInfo.getNetEaseToken());
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLogout() {
        logout();
    }
}
