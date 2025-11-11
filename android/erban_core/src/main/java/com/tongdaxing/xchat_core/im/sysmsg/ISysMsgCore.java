package com.tongdaxing.xchat_core.im.sysmsg;

import com.netease.nimlib.sdk.msg.constant.SystemMessageStatus;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/5/19.
 */

public interface ISysMsgCore extends IBaseCore {



    /**
     * 注册SystemMessage观察者，监听回调
     * 监听系统通知的到达事件和监听未读数变化
     */
    void registSystemMessageObserver(boolean isRegister);


    /**
     * 查询系统消息列表
     * @param offset
     * @param limit
     * @return
     */
    List<SystemMessage> querySystemMessageList(int offset, int limit);

    /**
     * 只查询“添加好友”类型的系统通知
     */
    List<SystemMessage> querySystemMessageByType(List<SystemMessageType> types, int loadOffset, int count);

    /**
     * 设置系统通知状态
     */
    void setSystemMessageStatus(SystemMessage msg,SystemMessageStatus status);

    /**
     * 删除系统通知
     */
    void deleteSystemMessage(SystemMessage msg);

    /**
     * 清除所有系统消息
     */
    void clearAllSystemMessage();

    /**
     * 查询系统通知未读数总和
     */
    int querySystemMessageCount();

    /**
     * 查询指定类型的系统通知未读数总和
     * @param types
     * @return
     */
    int querySystemMessageCountByType(List<SystemMessageType> types);

    /**
     * 设置单条系统通知为已读
     */
    void setSystemMessageRead(long sysMsgId);

    /**
     * 将所有系统通知设为已读
     */
    void setAllSystemMessageRead();

    /**
     * 将指定类型的系统通知设为已读接口
     */
    void setSystemMessageReadByType(List<SystemMessageType> types);

}
