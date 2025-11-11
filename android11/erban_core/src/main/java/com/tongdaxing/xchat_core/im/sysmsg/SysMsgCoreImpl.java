package com.tongdaxing.xchat_core.im.sysmsg;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageStatus;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

import java.util.List;


/**
 * Created by zhouxiangfeng on 2017/5/19.
 */

public class SysMsgCoreImpl extends AbstractBaseCore implements ISysMsgCore {
    public String TAG = "SysMsgCoreImpl";


    @Override
    public void registSystemMessageObserver(boolean isRegist) {

        NIMClient.getService(SystemMessageObserver.class)
                .observeReceiveSystemMsg(new Observer<SystemMessage>() {
                    @Override
                    public void onEvent(SystemMessage message) {
                        notifyClients(ISysMsgCoreClient.class, ISysMsgCoreClient.METHOD_ON_RECEIVE_SYSTEM_MSG, message);
                    }
                }, isRegist);

        NIMClient.getService(SystemMessageObserver.class)
                .observeUnreadCountChange(new Observer<Integer>() {
                    @Override
                    public void onEvent(Integer integer) {
                        notifyClients(ISysMsgCoreClient.class, ISysMsgCoreClient.METHOD_ON_UNREAD_COUNT_CHANGE,
                                integer);
                    }
                }, isRegist);

    }

    @Override
    public List<SystemMessage> querySystemMessageList(int offset, int limit) {
        // 参数offset为当前已经查了offset条，limit为要继续查询limit条。
        List<SystemMessage> temps = NIMClient.getService(SystemMessageService.class)
                .querySystemMessagesBlock(offset, limit);
        return temps;
    }


    @Override
    public List<SystemMessage> querySystemMessageByType(List<SystemMessageType> types, int loadOffset, int count) {
        // 只查询“添加好友”类型的系统通知
        List<SystemMessage> temps = (List<SystemMessage>) NIMClient.getService(SystemMessageService.class)
                .querySystemMessageByType(types, loadOffset, count);
        return temps;
    }

    @Override
    public void setSystemMessageStatus(SystemMessage message, SystemMessageStatus status) {
        NIMClient.getService(SystemMessageService.class).setSystemMessageStatus(message.getMessageId(), status);
    }

    @Override
    public void deleteSystemMessage(SystemMessage message) {
        NIMClient.getService(SystemMessageService.class)
                .deleteSystemMessage(message.getMessageId());
    }

    @Override
    public void clearAllSystemMessage() {
        NIMClient.getService(SystemMessageService.class).clearSystemMessages();
    }

    @Override
    public int querySystemMessageCount() {
        int unread = NIMClient.getService(SystemMessageService.class)
                .querySystemMessageUnreadCountBlock();
        return unread;
    }

    @Override
    public int querySystemMessageCountByType(List<SystemMessageType> types) {
        // 查询“添加好友”类型的系统通知未读数总和
        int unread = NIMClient.getService(SystemMessageService.class)
                .querySystemMessageUnreadCountByType(types);
        return unread;
    }

    @Override
    public void setSystemMessageRead(long sysMsgId) {
        NIMClient.getService(SystemMessageService.class).setSystemMessageRead(sysMsgId);
    }

    @Override
    public void setAllSystemMessageRead() {
        // 进入过系统通知列表后，可调用此函数将未读数值为0
        NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
    }

    @Override
    public void setSystemMessageReadByType(List<SystemMessageType> types) {
        // 将“添加好友”类型的系统通知设为已读
        NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
    }


}
