package com.vslk.lbgx.presenter.invite;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;

/**
 * 创建者      Created by dell
 * 创建时间    2019/07/12
 * 描述
 * <p>
 * 更新者      wm
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public interface IInviteRedPacketDrawView extends IMvpBaseView {

    void checkSucceed();

    void checkFailure(String errorStr);

    void onRemindToastSuc();

    void onRemindToastError(String error);
}
