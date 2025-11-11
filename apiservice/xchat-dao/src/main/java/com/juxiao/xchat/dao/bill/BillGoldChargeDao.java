package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillGoldChargeDO;
import com.juxiao.xchat.dao.bill.dto.BillGoldUserChargeDTO;
import com.juxiao.xchat.dao.bill.query.BillUserQuery;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

/**
 * 充值金币账单
 *
 * @class: BillGoldChargeDao.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public interface BillGoldChargeDao {
    /**
     * 保存充值金币账单
     *
     * @param chargeDo
     * @author: chenjunsheng
     * @date 2018/5/21
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_gold_charge` (`uid`,`gold_amount`,`money`,`charge_id`,`create_time`) VALUES (#{uid},#{goldAmount},#{money},#{chargeId},#{createTime})")
    void save(BillGoldChargeDO chargeDo);

    /**
     * 查询用户金币充值信息列表
     *
     * @param query
     * @return
     * @author: chenjunsheng
     * @date 2018/5/17
     */
    @TargetDataSource(name = "ds2")
    List<BillGoldUserChargeDTO> listUserCharge(BillUserQuery query);
}