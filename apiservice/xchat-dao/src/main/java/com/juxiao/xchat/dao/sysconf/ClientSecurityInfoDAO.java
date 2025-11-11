package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.sysconf.domain.ClientSecurityInfoDO;
import org.apache.ibatis.annotations.Insert;

/**
 * @Auther: alwyn
 * @Description: 客户端安全上报DAO
 * @Date: 2018/10/30 16:19
 */
public interface ClientSecurityInfoDAO {

    /**
     * 保存安全上报信息, uid, msgId, deviceId 联合主键</br>
     * 如果信息已经存在则更新操作时间
     * @param infoDO
     * @return
     */
    @Insert("INSERT INTO client_security_info ( uid, msg_id, device_id, create_date, os, os_version, app, app_version, model, channel, content ) " +
            "VALUES " +
            "   ( #{uid}, #{msgId}, #{deviceId}, NOW(), #{os}, #{osVersion}, #{app}, #{appVersion}, #{model}, #{channel}, #{content} ) " +
            "   ON DUPLICATE KEY UPDATE total_num = (total_num + 1)")
    int save(ClientSecurityInfoDO infoDO);
}
