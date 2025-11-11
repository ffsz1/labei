package com.juxiao.xchat.service.api.netease;

import com.juxiao.xchat.service.api.room.bo.LogoutMsgBO;

/**
 * @Auther: alwyn
 * @Description: 接收云信回调消息
 * @Date: 2018/9/5 9:42
 */
public interface NetEaseLogoutMsgService {

    /**
     * 云信掉线, 退出 处理
     * @param messageBO
     */
    void logout(LogoutMsgBO messageBO);
}
