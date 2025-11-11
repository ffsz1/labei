package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillItemHeadwearDO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

/**
 * 购买头饰账单操作
 *
 * @class: BillItemHeadwearDao.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public interface BillItemHeadwearDao {

    /**
     * 保存头饰购买账单
     *
     * @param headwearDo
     * @author: chenjunsheng
     * @date 2018/5/21
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_item_headwear` (`record_id`,`uid`,`gold_cost`,`create_time`) VALUES (#{recordId},#{uid},#{goldCost},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(BillItemHeadwearDO headwearDo);
}
