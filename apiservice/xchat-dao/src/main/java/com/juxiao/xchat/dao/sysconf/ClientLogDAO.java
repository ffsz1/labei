package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.sysconf.domain.ClientLogDO;
import org.apache.ibatis.annotations.Insert;

/**
 * @author chris
 * @Title:
 * @date 2019-05-08
 * @time 11:23
 */
public interface ClientLogDAO {

    /**
     * 保存日志信息
     *
     * @param logDO
     * @return
     */
    @Insert("INSERT INTO `client_log` " +
            "   ( `uid`, `url`, `os`, `os_version`, `model`, `device_id`, `app`, `app_version`, `create_time`, `ip`, `erban_no` ) " +
            "VALUES " +
            "   ( #{uid}, #{url}, #{os}, #{osVersion}, #{model}, #{deviceId}, #{app}, #{appVersion}, #{createTime}, #{ip}, #{erbanNo} )")
    int save(ClientLogDO logDO);
}
