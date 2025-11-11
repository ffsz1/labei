package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.StatPacketActivityDO;
import com.juxiao.xchat.dao.user.dto.StatPacketActivityDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @class: StatPacketActivityDao.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
public interface StatPacketActivityDao {
    /**
     * @param activityDo
     */
    @TargetDataSource
    void save(StatPacketActivityDO activityDo);

    /**
     * 更新信息
     *
     * @param activityDo
     */
    @TargetDataSource
    void update(StatPacketActivityDO activityDo);

    /**
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    StatPacketActivityDTO getUserPacketActivity(@Param("uid") Long uid);

    /**
     * @param uids
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<StatPacketActivityDTO> listUsersPacketActivity(@Param("uids") List<Long> uids);
}
