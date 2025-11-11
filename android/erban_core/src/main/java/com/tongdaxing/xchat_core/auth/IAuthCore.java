package com.tongdaxing.xchat_core.auth;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by chenran on 2017/2/13.
 */

public interface IAuthCore extends IBaseCore {
    void getWxAuth();

    AccountInfo getCurrentAccount();

    ThirdUserInfo getThirdUserInfo();

    void setThirdUserInfo(ThirdUserInfo thirdUserInfo);

    long getCurrentUid();

    String getTicket();

    boolean isLogin();

    void register(String phone, String sms_code, String password);

    void login(String account, String password);

    void autoLogin();

    void logout();

    void requestTicket();

    void requestSMSCode(String phone, int type);// Type取值：1注册短信；2登录短信；3找回或者修改密码短信

    void requestResetPsw(String phone, String sms_code, String newPsw);

    void wxLogin();

    void ThirdLogin(String openid, String unionid,String access_token, int type);

    void qqLogin();

    void isPhone(long uid);

    void binderPhone(String phone, String code);

    void ModifyBinderPhone(String phone, String code, String url);
    /**
     * 检测是否设置过密码
     */
    void checkSetPwd();

    void getSMSCode(String phone);

    void getModifyPhoneSMSCode(String phone, String type);
}
