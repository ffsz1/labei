package com.xchat.oauth2.service.service.dingtalk.bo;

/**
 * @author chris
 * @Title:
 * @date 2018/10/8
 * @time 09:56
 */
public interface Oauth2DingtalkMessageBO {

    /**
     * 返回消息的Json格式字符串
     *
     * @return 消息的Json格式字符串
     */
    String toJsonString();
}
