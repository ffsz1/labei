package com.juxiao.xchat.manager.external.dingtalk.bo;

/**
 * @author chris
 * @Title:
 * @date 2018/10/8
 * @time 09:56
 */
public interface DingtalkMessageBO {

    /**
     * 返回消息的Json格式字符串
     *
     * @return 消息的Json格式字符串
     */
    String toJsonString();
}
