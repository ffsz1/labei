package com.vslk.lbgx.im.transfer;

public interface INimCore {
//    void sendTransfer(TransferBean it);

//    void sendMsgVerify(MsgBean bean);

    void givegoldcheck(NimCoreImpl.INimCallbackListener<Boolean> listener);
}
