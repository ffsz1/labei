package com.tongdaxing.xchat_core.activity;

import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tongdaxing.xchat_core.common.ICommonClient;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.LotteryAttachment;
import com.tongdaxing.xchat_core.im.message.IIMMessageCoreClient;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

/**
 * Created by chenran on 2017/12/26.
 */

public class ActivityCoreImpl extends AbstractBaseCore implements IActivityCore {

    public ActivityCoreImpl() {
        CoreManager.addClient(this);
    }

    @Override
    public void requestLotteryActivity() {

    }

    @CoreEvent(coreClientClass = IIMMessageCoreClient.class)
    public void onReceivePersonalMessages(List<IMMessage> imMessages) {
        if (imMessages != null && imMessages.size() > 0) {
            for (IMMessage msg : imMessages) {
                if (msg.getMsgType() == MsgTypeEnum.custom) {
                    CustomAttachment attachment = (CustomAttachment) msg.getAttachment();
                    if (attachment.getFirst() == CustomAttachment.CUSTOM_MSG_HEADER_TYPE_LOTTERY) {
                        LotteryAttachment lotteryAttachment = (LotteryAttachment) msg.getAttachment();
                        notifyClients(IActivityCoreClient.class, IActivityCoreClient.METHOD_ON_RECEIVE_LOTTERY_ACTIVITY, lotteryAttachment.getLotteryInfo());
                        notifyClients(ICommonClient.class, ICommonClient.METHOD_ON_RECIEVE_NEED_REFRESH_WEBVIEW);
                    }
                }
            }
        }
    }
}
