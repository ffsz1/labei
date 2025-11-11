package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.StatPacketRegisterDO;
import com.juxiao.xchat.dao.user.dto.StatPacketRegisterDTO;
import com.juxiao.xchat.dao.user.dto.UserPacketRegTeamDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @class: StatPacketRegisterDao.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
public interface StatPacketRegisterDao {

    /**
     * 保存注册
     *
     * @param registerDo
     */
    @TargetDataSource
    void save(StatPacketRegisterDO registerDo);

    /**
     * @param uid
     * @param registerUid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT COUNT(8) FROM stat_packet_register WHERE uid=#{uid} AND register_uid=#{registerUid}")
    int countUserRegisterUid(@Param("uid") Long uid, @Param("registerUid") Long registerUid);

    /**
     * 查询下级人数
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT COUNT(DISTINCT spr.uid) as lowerNum FROM (SELECT register_uid FROM stat_packet_register WHERE uid = #{uid}) lowereg LEFT JOIN stat_packet_register spr ON lowereg.register_uid = spr.uid")
    int countRegisterLowerNum(@Param("uid") Long uid);

    @TargetDataSource(name = "ds2")
    StatPacketRegisterDTO getShareRegister(@Param("registerUid") Long registerUid);

    /**
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("select u.uid, u.nick, u.avatar, date_format(a.create_time,'%Y-%m-%d') as createTime, (select COUNT(1) from stat_packet_register b where b.uid = a.register_uid) as invitationNum from stat_packet_register a INNER JOIN users u on a.register_uid = u.uid where a.uid = #{uid}")
    List<UserPacketRegTeamDTO> listUserTeam(@Param("uid") Long uid);
}
