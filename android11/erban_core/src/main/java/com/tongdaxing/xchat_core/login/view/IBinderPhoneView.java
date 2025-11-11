package com.tongdaxing.xchat_core.login.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;

/**
 * Function:
 * Author: Edward on 2019/6/5
 */
public interface IBinderPhoneView extends IMvpBaseView {

    /**
     * 获取更改绑定手机验证码
     */
    void getModifyPhoneSMSCodeSuccess();

    void getModifyPhoneSMSCodeFail(String msg);

    /**
     * 绑定手机
     */
    void onBinderPhone();

    void onBinderPhoneFail(String msg);
}
