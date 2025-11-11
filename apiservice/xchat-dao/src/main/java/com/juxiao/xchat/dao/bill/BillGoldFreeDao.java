package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillGoldFreeDO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;

/**
 * 免费送金币记录
 *
 * @class: BillGoldFreeDao.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public interface BillGoldFreeDao {
    /**
     * 保存免费送金币记录账单
     *
     * @param freeDo
     * @author: chenjunsheng
     * @date 2018/5/21
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_gold_free` (`uid`,`free_source`,`gold_amount`,`op_uid`,`create_time`) VALUES (#{uid},#{freeSource},#{goldAmount},#{opUid},#{createTime})")
    void save(BillGoldFreeDO freeDo);
}
