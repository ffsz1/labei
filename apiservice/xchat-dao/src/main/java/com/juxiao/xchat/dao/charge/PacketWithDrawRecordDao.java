package com.juxiao.xchat.dao.charge;

import com.juxiao.xchat.dao.charge.domain.PacketWithDrawRecordDO;
import com.juxiao.xchat.dao.charge.dto.UserPacketRecordDTO;
import com.juxiao.xchat.dao.charge.dto.WithdrawRedPacketRecordDTO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PacketWithDrawRecordDao {

    /**
     * 保存记录
     *
     * @param recordDo
     */
    @TargetDataSource
    @Insert("INSERT INTO `packet_withdraw_record` (`record_id`,`uid`,`packet_prod_cash_id`,`packet_num`,`record_status`,`create_time`,`tran_type`)VALUES(#{recordId,jdbcType=VARCHAR},#{uid,jdbcType=BIGINT},#{packetProdCashId,jdbcType=INTEGER},#{packetNum,jdbcType=DOUBLE},#{recordStatus,jdbcType=TINYINT},#{createTime,jdbcType=TIMESTAMP},#{tranType})")
    void save(PacketWithDrawRecordDO recordDo);

    /**
     * 查询用户成功提现记录
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<UserPacketRecordDTO> listSuccessPacketRecord(byte status);

    @TargetDataSource(name = "ds2")
    List<WithdrawRedPacketRecordDTO> selectWithdrawRedPacketRecordByList(@Param("uid") Long uid);

    /**
     * 保存
     * @param withdrawRecordDo withdrawRecordDo
     */
    @TargetDataSource
    @Insert("INSERT INTO `packet_withdraw_record` (`record_id`,`uid`,`packet_prod_cash_id`,`packet_num`,`record_status`,`create_time`,`tran_type`,`wx_open_id`)VALUES(#{recordId,jdbcType=VARCHAR},#{uid,jdbcType=BIGINT},#{packetProdCashId,jdbcType=INTEGER},#{packetNum,jdbcType=DOUBLE},#{recordStatus,jdbcType=TINYINT},#{createTime,jdbcType=TIMESTAMP},#{tranType},#{wxOpenId,jdbcType=VARCHAR})")
    void insert(PacketWithDrawRecordDO withdrawRecordDo);
}
