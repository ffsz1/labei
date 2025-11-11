package com.tongdaxing.xchat_core.login.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;

/**
 * Function:
 * Author: Edward on 2019/6/4
 */
public interface ILoginView extends IMvpBaseView {
    void openPhoneLoginPage();

    void openRegisterPage();

    void openLoginPage();

    void openResetPwPage();

    void openBinderPhonePage();

    void onQQLogin(boolean agreeUserProtocol);

    void onWechatLogin(boolean agreeUserProtocol);

    void openProtocolPage();

    void openSelfProtocolPage();

    void openLiveProtocolPage();

    void onPhoneLogin(String account, String pw, boolean agreeUserProtocol);

    boolean isAgreeCb();
}
