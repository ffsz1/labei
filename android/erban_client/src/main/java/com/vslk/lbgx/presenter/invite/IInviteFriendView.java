package com.vslk.lbgx.presenter.invite;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfo;

/**
 * 创建者      Created by dell
 * 创建时间    2019/1/4
 * 描述
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public interface IInviteFriendView extends IMvpBaseView {

    void checkSucceed();

    void checkFailure(String errorStr);

    void onGetRedPacketSuccessView(RedPacketInfo info);

    void onGetRedPacketFailView(String msg);

    void onSaveInviteCodeSuccessView(String inviteCode);

    void onSaveInviteCodeFailView(String msg);

    void onRemindToastSuc();

    void onRemindToastError(String error);
}
