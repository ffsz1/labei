package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillGoldExchangeDO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;

/**
 * 钻石兑换金币记录操作
 *
 * @class: BillGoldExchangeDao.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public interface BillGoldExchangeDao {

    /**
     * 保存钻石对话金币账单
     *
     * @param exchangeDo
     * @author: chenjunsheng
     * @date 2018/5/21
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_gold_exchange` (`record_id`,`uid`,`diamond_cost`,`gold_amount`,`create_time`) VALUES (#{recordId},#{uid},#{diamondCost},#{goldAmount},#{createTime})")
    void save(BillGoldExchangeDO exchangeDo);
}
