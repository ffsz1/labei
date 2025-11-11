package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillGiftBonusDO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;

/**
 * 钻石回馈账单
 *
 * @class: BillGiftBonusDao.java
 * @author: chenjunsheng
 * @date 2018/5/22
 */
public interface BillGiftBonusDao {

    /**
     * 保存钻石回馈账单
     *
     * @param bonusDo
     * @author: chenjunsheng
     * @date 2018/5/22
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_gift_bonus` (`bonus_id`,`uid`,`diamond_amount`,`create_time`) VALUES (#{bonusId},#{uid},#{diamondAmount},#{createTime})")
    void save(BillGiftBonusDO bonusDo);
}
