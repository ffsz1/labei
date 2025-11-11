package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillGoldDrawDO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;

/**
 * 金币抽奖账单
 *
 * @class: BillGoldDrawDao.java
 * @author: chenjunsheng
 * @date 2018/5/23
 */
public interface BillGoldDrawDao {

    /**
     * 保存抽奖得金币账单记录
     *
     * @param drawDo
     * @author: chenjunsheng
     * @date 2018/5/23
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_gold_draw` (`record_id`,`uid`,`gold_amount`,`create_time`) VALUES (#{recordId},#{uid},#{goldAmount},#{createTime})")
    void save(BillGoldDrawDO drawDo);
}
