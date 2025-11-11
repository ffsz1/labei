package com.vslk.lbgx.room.egg.call;

import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.vslk.lbgx.room.egg.bean.CallBean;

public interface ICallCore {
    void sendCall(MicMemberInfo user, GiftInfo info, CallCoreImpl.ICallBackListener<CallBean> listener);

    void getGold(CallCoreImpl.ICallBackListener<WalletInfo> listener);
}
