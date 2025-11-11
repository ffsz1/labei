package com.tongdaxing.xchat_core.auth;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mob.MobSDK;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.DemoCache;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.linked.ILinkedCore;
import com.tongdaxing.xchat_core.linked.LinkedInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.utils.Logger;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.http_image.util.DeviceUuidFactory;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.DESUtils;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.VersionUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by chenran on 2017/2/16.
 */

public class AuthCoreImpl extends AbstractBaseCore implements IAuthCore {
    private static final String TAG = "AuthCoreImpl";
    private AccountInfo currentAccountInfo;
    private TicketInfo ticketInfo;
    private boolean isRequestTicket;
    private ThirdUserInfo thirdUserInfo;
    private Platform wechat;
    private Platform qq;
    private AuthCoreImplHander handler = new AuthCoreImplHander(this);

    public AuthCoreImpl() {
        MobSDK.init(getContext());
        currentAccountInfo = DemoCache.readCurrentAccountInfo();
        ticketInfo = DemoCache.readTicketInfo();
        if (currentAccountInfo == null) {
            currentAccountInfo = new AccountInfo();
        }
        if (ticketInfo == null) {
            ticketInfo = new TicketInfo();
        }
    }

    private String DESAndBase64(String psw) {
        String pwd = "";
        try {
            pwd = DESUtils.DESAndBase64Encrypt(psw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pwd;
    }

    private void reset() {
        currentAccountInfo = new AccountInfo();
        ticketInfo = new TicketInfo();
        DemoCache.saveCurrentAccountInfo(new AccountInfo());
        DemoCache.saveTicketInfo(new TicketInfo());
    }

    @Override
    public boolean isLogin() {
        if (currentAccountInfo == null || StringUtil.isEmpty(currentAccountInfo.getAccess_token()) || TextUtils.isEmpty(currentAccountInfo.getUid() + "")) {
            return false;
        }
        return currentAccountInfo.getAccess_token() != null ? true : false && !isRequestTicket;
    }

    @Override
    public long getCurrentUid() {
        return currentAccountInfo == null ? 0 : currentAccountInfo.getUid();
    }

    @Override
    public String getTicket() {
        if (ticketInfo != null && ticketInfo.getTickets() != null && ticketInfo.getTickets().size() > 0) {
            return ticketInfo.getTickets().get(0).getTicket();
        }
        return "";
    }

    /**
     * 获取微信授权
     */
    @Override
    public void getWxAuth() {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (!wechat.isClientValid()) {
            notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_WX_AUTH_FAILURE, "未安装微信");
            return;
        }
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }

        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
                if (i == Platform.ACTION_USER_INFOR) {
                    String openid = platform.getDb().getUserId();
                    String unionid = platform.getDb().get("unionid");
                    String accessToken = platform.getDb().getToken();
                    String nickName = platform.getDb().getUserName();
                    notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_WX_AUTH_SUCCEED, openid, unionid, accessToken, nickName);
                } else {
                    notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_WX_AUTH_FAILURE, "微信登录失败，错误码：" + i);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_WX_AUTH_FAILURE, "微信授权错误");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_WX_AUTH_FAILURE, "取消微信授权");
            }
        });
        wechat.SSOSetting(false);
        wechat.showUser(null);
    }

    @Override
    public AccountInfo getCurrentAccount() {
        return currentAccountInfo;
    }

    @Override
    public ThirdUserInfo getThirdUserInfo() {
        return thirdUserInfo;
    }


    @Override
    public void setThirdUserInfo(ThirdUserInfo thirdUserInfo) {
        this.thirdUserInfo = thirdUserInfo;
    }

    @Override
    public void autoLogin() {
        if (!isLogin()) {
            notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_NEED_LOGIN);
            return;
        }

        isRequestTicket = true;
        requestTicket();
    }

    @Override
    public void login(final String account, String password) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("phone", account);
        params.put("version", VersionUtil.getLocalName(getContext()));
        params.put("client_id", "erban-client");
        params.put("username", account);
        params.put("password", DESAndBase64(password));
        params.put("grant_type", "password");
        params.put("client_secret", "uyzjdhds");
        params.put("IMEI", DeviceUuidFactory.getPhoneIMEI(BasicConfig.INSTANCE.getAppContext()));

        OkHttpManager.getInstance().doPostRequest(UriProvider.getLoginResourceUrl(), params, new OkHttpManager.MyCallBack<ServiceResult<AccountInfo>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGIN_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<AccountInfo> response) {
                if (response.isSuccess()) {
                    currentAccountInfo = response.getData();
                    DemoCache.saveCurrentAccountInfo(currentAccountInfo);
                    requestTicket();
                } else {
                    logout();
                    notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGIN_FAIL, response.getMessage());
                }
            }
        });
    }

    @Override
    public void requestTicket() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("issue_type", "multi");
        params.put("access_token", currentAccountInfo.getAccess_token());

        OkHttpManager.getInstance().getRequest(UriProvider.getAuthTicket(), params, new OkHttpManager.MyCallBack<ServiceResult<TicketInfo>>() {

            @Override
            public void onError(Exception e) {
                if (e != null) {
                    isRequestTicket = false;
                    reset();
                    notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGOUT);
                    notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_REQUEST_TICKET_FAIL, e.getMessage());
                } else {
                    handler.sendEmptyMessageDelayed(0, 1000);
                }
            }

            @Override
            public void onResponse(ServiceResult<TicketInfo> response) {
                isRequestTicket = false;
                if (response.isSuccess()) {
                    ticketInfo = response.getData();
                    DemoCache.saveTicketInfo(ticketInfo);
                    notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGIN, currentAccountInfo);
                    //获取到用户信息之后加载用户抽奖礼物
                    CoreManager.getCore(IGiftCore.class).requestGiftInfos();
                } else {
                    reset();
                    notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGOUT);
                }
            }
        });
    }

    @Override
    public void requestSMSCode(String phone, int type) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("phone", phone);
        params.put("type", String.valueOf(type));

        OkHttpManager.getInstance().getRequest(UriProvider.getSMSCode(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_SMS_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_SMS_SUCCESS);
                    } else {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_SMS_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void requestResetPsw(String phone, String smsCode, String newPsw) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("phone", phone);
        params.put("smsCode", smsCode);
        params.put("newPwd", DESAndBase64(newPsw));

        OkHttpManager.getInstance().doPostRequest(UriProvider.modifyPsw(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_MODIFY_PSW_FAIL, e.getMessage());
                String e2 = e.getMessage();
                System.out.println("e2" + e2);
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_MODIFY_PSW);
                    } else {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_MODIFY_PSW_FAIL, response.getMessage());
                        String e1 = response.getErrorMessage().toString();
                        System.out.println("e1" + e1);
                    }
                }
            }
        });
    }

    @Override
    public void wxLogin() {
        wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (!wechat.isClientValid()) {
            notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGIN_FAIL, "未安装微信");
            return;
        }
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }

        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
                if (i == Platform.ACTION_USER_INFOR) {
                    String openid = platform.getDb().getUserId();
                    String unionid = platform.getDb().get("unionid");
                    String access_token = platform.getDb().getToken();
                    thirdUserInfo = new ThirdUserInfo();
                    thirdUserInfo.setUserName(platform.getDb().getUserName());
                    thirdUserInfo.setUserGender(platform.getDb().getUserGender());
                    thirdUserInfo.setUserIcon(platform.getDb().getUserIcon());
                    ThirdLogin(openid, unionid, access_token, 1);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGIN_FAIL, "第三方登录失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGIN_FAIL, "第三方登录取消");
            }
        });
        wechat.SSOSetting(false);
        wechat.showUser(null);
    }

    @Override
    public void ThirdLogin(String openid, String unionid, String access_token, int type) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("openid", openid);
        params.put("unionid", unionid);
        params.put("access_token", access_token);
        params.put("type", String.valueOf(type));
        params.put("IMEI", DeviceUuidFactory.getPhoneIMEI(BasicConfig.INSTANCE.getAppContext()));
        LinkedInfo linkedInfo = CoreManager.getCore(ILinkedCore.class).getLinkedInfo();
        if (linkedInfo != null && !StringUtil.isEmpty(linkedInfo.getChannel())) {
            params.put("linkedmeChannel", linkedInfo.getChannel());
        }

        OkHttpManager.getInstance().doPostRequest(UriProvider.requestWXLogin(), params, new OkHttpManager.MyCallBack<ServiceResult<AccountInfo>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGIN_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<AccountInfo> response) {
                if (response.isSuccess()) {
                    currentAccountInfo = response.getData();
                    DemoCache.saveCurrentAccountInfo(currentAccountInfo);
                    requestTicket();
                } else {
                    notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGIN_FAIL, response.getCode() + "错误," + response.getMessage());
                    logout();
                }
            }
        });
    }

    @Override
    public void qqLogin() {
//        qq.authorize();\
        qq = ShareSDK.getPlatform(QQ.NAME);
        if (qq.isAuthValid()) {
            qq.removeAccount(true);
        }
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if (i == Platform.ACTION_USER_INFOR) {
                    String openid = platform.getDb().getUserId();
                    String unionid = platform.getDb().get("unionid");
                    String access_token = platform.getDb().getToken();
                    thirdUserInfo = new ThirdUserInfo();
                    thirdUserInfo.setUserName(platform.getDb().getUserName());
                    thirdUserInfo.setUserGender(platform.getDb().getUserGender());
                    thirdUserInfo.setUserIcon(platform.getDb().getUserIcon());
                    ThirdLogin(openid, unionid, access_token, 2);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGIN_FAIL, "第三方登录取消");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGIN_FAIL, "第三方登录取消");
            }
        });
        qq.SSOSetting(false);
        qq.showUser(null);
    }

    @Override
    public void isPhone(long uid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));

        OkHttpManager.getInstance().getRequest(UriProvider.isPhones(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_IS_PHONE_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_IS_PHONE);
                    } else {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_IS_PHONE_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void ModifyBinderPhone(String phone, String code, String url) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("phone", phone);
        params.put("smsCode", code);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(url, params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_MOIDFY_ON_BINDER_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_MOIDFY_ON_BINDER);
                        IUserCore core = CoreManager.getCore(IUserCore.class);
                        if (core != null && core.getCacheLoginUserInfo() != null) {
                            core.requestUserInfo(core.getCacheLoginUserInfo().getUid());
                        }
                    } else {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_MOIDFY_ON_BINDER_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void checkSetPwd() {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().doPostRequest(UriProvider.checkSetPwd(), param, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_SET_PWD_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_SET_PWD, response.boo("data"));
                } else {
                    notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_SET_PWD_FAIL, response.str("message"));
                }
            }
        });
    }

    @Override
    public void binderPhone(String phone, String code) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("phone", phone);
        params.put("code", code);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.binderPhone(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_BINDER_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_BINDER);
                        IUserCore core = CoreManager.getCore(IUserCore.class);
                        if (core != null && core.getCacheLoginUserInfo() != null) {
                            core.requestUserInfo(core.getCacheLoginUserInfo().getUid());
                        }
                    } else {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_BINDER_FAIL, response.getMessage());
                    }
                }
            }
        });
    }


    @Override
    public void getSMSCode(String phone) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("phone", phone);

        OkHttpManager.getInstance().getRequest(UriProvider.getSmS(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_GET_SMS_CODE_FAIL, "绑定手机失败!");
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                    } else {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_GET_SMS_CODE_FAIL, response.getMessage() + "");
                    }
                }
            }
        });
    }


    @Override
    public void getModifyPhoneSMSCode(String phone, String type) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("phone", phone);
        params.put("type", type);

        OkHttpManager.getInstance().getRequest(UriProvider.getModifyPhoneSMS(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_GET_SMS_CODE_FAIL, "换绑手机失败!");
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                    } else {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_GET_SMS_CODE_FAIL, response.getMessage() + "");
                    }
                }
            }
        });
    }


    @Override
    public void logout() {
        reset();
        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_LOGOUT);
    }

    @Override
    public void register(String phone, String smsCode, String password) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("phone", phone);
        params.put("smsCode", smsCode);
        params.put("password", DESAndBase64(password));
        params.put("os", "android");
        LinkedInfo linkedInfo = CoreManager.getCore(ILinkedCore.class).getLinkedInfo();
        if (linkedInfo != null && !StringUtil.isEmpty(linkedInfo.getChannel())) {
            params.put("linkedmeChannel", linkedInfo.getChannel());
        }

        OkHttpManager.getInstance().doPostRequest(UriProvider.getRegisterResourceUrl(), params, new OkHttpManager.MyCallBack<ServiceResult<TicketInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_REGISTER_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<TicketInfo> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_REGISTER);
                    } else {
                        notifyClients(IAuthClient.class, IAuthClient.METHOD_ON_REGISTER_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    static class AuthCoreImplHander extends Handler {
        WeakReference<AuthCoreImpl> authCoreImpl;

        public AuthCoreImplHander(AuthCoreImpl authCoreImpl) {
            this.authCoreImpl = new WeakReference<>(authCoreImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (authCoreImpl == null || authCoreImpl.get() == null) {
                return;
            }
            authCoreImpl.get().requestTicket();
        }
    }

}
