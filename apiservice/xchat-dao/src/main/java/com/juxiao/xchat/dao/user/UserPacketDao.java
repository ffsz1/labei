package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserPacketDO;
import com.juxiao.xchat.dao.user.dto.StatPacketActRankDTO;
import com.juxiao.xchat.dao.user.dto.StatPacketBounsDTO;
import com.juxiao.xchat.dao.user.dto.UserPacketDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @class: UserPacketDao.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
public interface UserPacketDao {

    /**
     * 保存用户红包
     *
     * @param packetDo
     */
    @TargetDataSource
    void save(UserPacketDO packetDo);

    /**
     * 更新用户红包
     *
     * @param packetDo
     */
    @TargetDataSource
    void update(UserPacketDO packetDo);

    /**
     * 获取用户红包
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    UserPacketDTO getUserPacket(@Param("uid") Long uid);

    /**
     * @param histPacketNum
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT COUNT(*) FROM `user_packet` WHERE `hist_packet_num`>= #{histPacketNum,jdbcType=BIGINT}")
    int countStatPacketRankSeqNo(@Param("histPacketNum") Double histPacketNum);

    /**
     * 查询房间排行榜
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<StatPacketActRankDTO> listPacketActivityRank();

    /**
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<StatPacketBounsDTO> listInviteBonus(@Param("uid") Long uid);
}
