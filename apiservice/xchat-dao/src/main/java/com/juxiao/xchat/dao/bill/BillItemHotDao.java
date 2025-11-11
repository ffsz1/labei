package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillItemHotDO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;

/**
 * 购买推荐位账单记录
 *
 * @class: BillItemHotDao.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public interface BillItemHotDao {

    /**
     * 保存推荐位购买账单
     *
     * @param hotDo
     * @author: chenjunsheng
     * @date 2018/5/21
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_item_hot` (`record_id`,`uid`,`gold_cost`,`create_time`,`end_time`) VALUES (#{recordId},#{uid},#{goldCost},#{createTime},#{endTime})")
    void save(BillItemHotDO hotDo);
}