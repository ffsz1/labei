package com.juxiao.xchat.service.api.netease.impl;

import com.juxiao.xchat.service.api.netease.NetEaseLogoutMsgService;
import com.juxiao.xchat.service.api.room.bo.LogoutMsgBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Auther: alwyn
 * @Description: 用户掉线业务相关
 * @Date: 2018/9/5 10:26
 */
@Service
public class NetEaseLogoutMsgServiceImpl implements NetEaseLogoutMsgService {

    private final Logger logger = LoggerFactory.getLogger(NetEaseLogoutMsgService.class);

    @Override
    public void logout(LogoutMsgBO messageBO) {
        //
        logger.info("[用户退出/掉线] message:{}", messageBO);
    }
}
