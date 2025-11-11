package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserPacketRecordDO;
import com.juxiao.xchat.dao.user.dto.PacketRecordDTO;
import com.juxiao.xchat.dao.user.dto.UserPacketInviteRecordDTO;
import com.juxiao.xchat.dao.user.query.PacketRecordQuery;
import com.juxiao.xchat.dao.user.query.UserPacketSumQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户红包记录
 *
 * @class: UserPacketRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
public interface UserPacketRecordDao {

    /**
     * @param recordDo
     */
    @TargetDataSource
    void save(UserPacketRecordDO recordDo);

    /**
     * 用户红包
     *
     * @param recordDo
     */
    @TargetDataSource
    void update(UserPacketRecordDO recordDo);

    /**
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT SUM(`packet_num`) FROM `user_packet_record` WHERE `uid` = #{uid} AND `type` in (1,3,4,6) AND `create_time` BETWEEN #{startTime} AND #{endTime}")
    Double sumUserPacketNum(UserPacketSumQuery query);

    /**
     * 查询用户红包记录
     *
     * @param query
     * @return
     * @author: chenjunsheng
     * @date 2018/6/5
     */
    @TargetDataSource(name = "ds2")
    List<PacketRecordDTO> listPacketRecord(PacketRecordQuery query);

    /**
     * 查询提现红包的记录
     *
     * @param query
     * @return
     * @author: chenjunsheng
     * @date 2018/6/5
     */
    @TargetDataSource(name = "ds2")
    List<PacketRecordDTO> listPacketDeposiRecord(PacketRecordQuery query);

    /**
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<UserPacketInviteRecordDTO> listPacketInviteRegister(@Param("uid") Long uid);
}
