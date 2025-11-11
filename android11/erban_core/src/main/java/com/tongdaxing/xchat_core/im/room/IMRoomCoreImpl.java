package com.tongdaxing.xchat_core.im.room;

import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

/**
 * Created by zhouxiangfeng on 2017/5/20.
 */

public class IMRoomCoreImpl extends AbstractBaseCore implements IIMRoomCore {

    private static final String TAG = "IMRoomCoreImpl";

    public EnterChatRoomResultData imRoomInfo;

    public IMRoomCoreImpl() {

    }


    @Override
    public void sendMessage(final ChatRoomMessage message) {
        ReUsedSocketManager.get().sendCustomMessage(message.getRoom_id() + "", message, new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean != null && imReportBean.getReportData() != null&& imReportBean.getReportData().errno == 0){
                    notifyClients(IIMRoomCoreClient.class, IIMRoomCoreClient.METHOD_ON_SEND_ROOM_MESSAGE_SUCCESS, message);
//                    IMCustomAttachment attachment = (IMCustomAttachment) message.getAttachment();
//                    if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT ||
//                            attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP ||
//                            attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT ||
//                            attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_LOTTERY_BOX
//                            || attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_MIC_IN_LIST) {
//                        LogUtil.e("message", "sendMessage");
////                        IMNetEaseManager.get().addMessagesImmediately(message);
//                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
            }
        });
    }




}
