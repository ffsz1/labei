package com.tongdaxing.xchat_core.im.room;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by chenran on 2017/2/16.
 */

public interface IIMRoomCore extends IBaseCore {



    /**
     * 发送房间消息
     */
    void sendMessage(com.tongdaxing.xchat_core.bean.ChatRoomMessage message);

    /**
     * 踢出聊天室
     * 踢出成员。仅管理员可以踢；如目标是管理员仅创建者可以踢。
     * 可以添加被踢通知扩展字段，这个字段会放到被踢通知的扩展字段中。通知扩展字段最长1K；扩展字段需要传入 Map， SDK 会负责转成Json String。
     * 当有人被踢出聊天室时，会收到类型为 ChatRoomMemberKicked 的聊天室通知消息。
     */

}
