package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.sysconf.domain.HomeChannelDO;
import org.apache.ibatis.annotations.Select;

/**
 * @Auther: alwyn
 * @Description: 渠道管理
 * @Date: 2018/11/7 15:55
 */
public interface HomeChannelDAO {

    @Select("SELECT * FROM home_channel WHERE channel = #{channel}")
    HomeChannelDO getByChannel(String channel);
}
