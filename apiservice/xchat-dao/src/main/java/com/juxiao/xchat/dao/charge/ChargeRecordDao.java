package com.juxiao.xchat.dao.charge;

import com.juxiao.xchat.dao.charge.domain.ChargeRecordDO;
import com.juxiao.xchat.dao.charge.dto.ChargeRecordDTO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 充值记录数据处理接口
 *
 * @class: ChargeRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface ChargeRecordDao {

    /**
     * 保存充值记录
     *
     * @param recordDo
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    @TargetDataSource
    void save(ChargeRecordDO recordDo);

    /**
     * 更新订单状态
     *
     * @param recordDo
     */
    @TargetDataSource
    void update(ChargeRecordDO recordDo);

    /**
     * 获取充值记录
     *
     * @param chargeRecordId
     * @return
     */
    @TargetDataSource(name = "ds2")
    ChargeRecordDTO getChargeRecord(@Param("chargeRecordId") String chargeRecordId);

    @TargetDataSource(name = "ds2")
    @Select("SELECT count(1) FROM `charge_record` WHERE `uid`= #{uid} AND `charge_status`= 2 AND `channel`<> 'exchange'")
    int countUserChargeSuccess(@Param("uid") Long uid);

    @TargetDataSource(name = "ds2")
    @Select("SELECT SUM(c.amount) FROM charge_record c WHERE c.uid = #{uid} AND c.charge_status = '2' AND NOT c.charge_prod_id IN ('exchange', 'company')")
    int sumUserChargeSuccess(@Param("uid") Long uid);
}
