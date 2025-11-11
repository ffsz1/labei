package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillItemCarDO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;

/**
 * 购买座驾账单操作
 *
 * @class: BillItemCarDao.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public interface BillItemCarDao {

    /**
     * 保存购买座驾账单
     *
     * @param carDo
     * @author: chenjunsheng
     * @date 2018/5/21
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_item_car` (`record_id`,`uid`,`gold_cost`,`create_time`) VALUES (#{recordId},#{uid},#{goldCost},#{createTime})")
    void save(BillItemCarDO carDo);
}
