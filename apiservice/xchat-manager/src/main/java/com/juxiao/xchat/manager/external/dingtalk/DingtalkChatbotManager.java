package com.juxiao.xchat.manager.external.dingtalk;

import com.juxiao.xchat.manager.external.dingtalk.bo.DingtalkMessageBO;
import com.juxiao.xchat.manager.external.dingtalk.ret.DingtalkChatbotRet;

/**
 * @author chris
 * @Title: 钉钉机器人发送消息接口
 * @date 2018/10/8
 * @time 10:02
 */
public interface DingtalkChatbotManager {

    /**
     * 发送消息
     *
     * @param webhook 机器人url
     * @param message 发送的消息
     * @return
     */
    DingtalkChatbotRet send(String webhook, DingtalkMessageBO message);
}
