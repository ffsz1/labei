package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillGiftUseDO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;

/**
 * 使用抽奖礼物记录
 *
 * @class: BillGiftUseDao.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public interface BillGiftUseDao {
    /**
     * 保存使用抽奖礼物记录
     *
     * @param giftDo
     * @author: chenjunsheng
     * @date 2018/5/21
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_gift_use` (`uid`,`gift_id`,`gift_num`,`create_time`) VALUES (#{uid}, #{giftId}, #{giftNum}, #{createTime})")
    void save(BillGiftUseDO giftDo);
}
