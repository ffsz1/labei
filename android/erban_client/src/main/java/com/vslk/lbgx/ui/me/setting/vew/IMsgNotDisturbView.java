package com.vslk.lbgx.ui.me.setting.vew;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;

public interface IMsgNotDisturbView extends IMvpBaseView {
    //获取免打扰状态成功
    void getDisturbStateSuccess(int state);

    //获取免打扰状态失败
    void getDisturbStateFail(String message);

    //修改免打扰状态成功
    void saveDisturbStateSuccess(int state);

    //修改免打扰状态失败
    void saveDisturbStateFail(String message);



}
