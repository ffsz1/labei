package com.tongdaxing.xchat_core.auth;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by chenran on 2017/2/16.
 */

public interface IAuthClient extends ICoreClient {
    public static final String METHOD_ON_WX_AUTH_SUCCEED = "onWxAuthSucceed";
    public static final String METHOD_ON_WX_AUTH_FAILURE = "onWxAuthFailure";

    public static final String METHOD_ON_NEED_LOGIN = "onNeedLogin";
    public static final String METHOD_ON_LOGIN = "onLogin";
    public static final String METHOD_ON_LOGOUT = "onLogout";
    public static final String METHOD_ON_REQUEST_TICKET_FAIL = "onRequestTicketFail";
    public static final String METHOD_ON_LOGOUT_FAITH = "onLogoutFaith";
    public static final String METHOD_ON_REGISTER = "onRegister";
    public static final String METHOD_ON_SMS_FAIL = "onSmsFail";
    public static final String METHOD_ON_SMS_SUCCESS = "onSmsSuccess";
    public static final String METHOD_ON_MODIFY_PSW = "onModifyPsw";
    public static final String METHOD_ON_MODIFY_PSW_FAIL = "onModifyPswFail";
    public static final String METHOD_ON_LOGIN_FAIL = "onLoginFail";
    public static final String METHOD_ON_REGISTER_FAIL = "onRegisterFail";
    //    public static final String METHOD_ON_IDQUERYUSERINFO = "onIdQueryUserInfo";
//    public static final String METHOD_ON_IDQUERYUSERINFO_FAIL = "onIdQueryUserInfoFail";

    String METHOD_ON_SET_PWD = "onSetPassWord";
    String METHOD_ON_SET_PWD_FAIL = "onSetPassWordFail";

    public static final String METHOD_ON_IS_PHONE = "onIsPhone";
    public static final String METHOD_ON_IS_PHONE_FAIL = "onIsphoneFail";

    public static final String METHOD_ON_BINDER = "onBinderPhone";
    public static final String METHOD_ON_BINDER_FAIL = "onBinderPhoneFail";

    public static final String METHOD_ON_GET_SMS_CODE = "onGetSMSCode";
    public static final String METHOD_ON_GET_SMS_CODE_FAIL = "onGetSMSCodeFail";
    String METHOD_MOIDFY_ON_BINDER = "onMoidfyOnBiner";
    String METHOD_MOIDFY_ON_BINDER_FAIL = "onMoidfyOnBinerFail";

    void onSmsSuccess();

    void onSetPassWord(boolean isSetPassWord);

    void onSetPassWordFail(String msg);

    void onWxAuthSucceed(String openid, String unionid, String accessToken, String nickName);

    void onWxAuthFailure(String errorStr);

    void onMoidfyOnBiner();

    void onMoidfyOnBinerFail(String msg);

    void onNeedLogin();

    void onRegister();

    void onRegisterFail(String error);

    void onLogin(AccountInfo accountInfo);

    void onRequestTicketFail(String error);

    void onLoginFail(String error);

    void onLogout();

    void onLogoutFaith(String error);

    void onModifyPsw();

    void onModifyPswFail(String error);

    void onSmsFail(String error);

    void onWxLogin();

    void onIsPhone();

    void onIsphoneFail(String error);

    void onBinderPhone();

    void onBinderPhoneFail(String error);

    void onGetSMSCode();

    void onGetSMSCodeFail(String error);
}
