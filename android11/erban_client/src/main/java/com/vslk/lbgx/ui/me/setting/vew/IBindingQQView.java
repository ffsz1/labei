package com.vslk.lbgx.ui.me.setting.vew;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;

/**
 * Function:
 * Author: Edward on 2019/4/11
 */
public interface IBindingQQView extends IMvpBaseView {
    void sendSmsSucceed();

    void sendSmsFailure();

    void bindingSucceed(int type);

    void bindingFailure(String errorStr);
}
