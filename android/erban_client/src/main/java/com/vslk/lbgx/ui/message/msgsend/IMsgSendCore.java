package com.vslk.lbgx.ui.message.msgsend;

import com.netease.nimlib.sdk.msg.model.IMMessage;

public interface IMsgSendCore {
    void onSendMsg(IMMessage message, MsgSendCoreImpl.IMsgCallBackListener<IMMessage> listener);
}
