package com.juxiao.xchat.service.api.sysconf;

import com.juxiao.xchat.dao.sysconf.domain.HomeChannelDO;

/**
 * @Auther: alwyn
 * @Description: 渠道信息
 * @Date: 2018/11/7 15:54
 */
public interface HomeChannelService {

    HomeChannelDO getByChannel(String channel);
}
