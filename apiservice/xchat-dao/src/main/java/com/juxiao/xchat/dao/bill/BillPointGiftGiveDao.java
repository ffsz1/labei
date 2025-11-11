package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillPointGiftGiveDO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;

/**
 * 赠送送礼物记录操作
 *
 * @class: BillGiftGiveDao.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public interface BillPointGiftGiveDao {

    /**
     * 保存赠送礼物账单记录
     * @param giveDo giveDo
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_point_gift_give` (`room_uid`, `giver_uid`, `receiver_uid`, `record_id`, `gift_id`, `gift_num`, `gold_cost`, `create_time`) VALUES (#{roomUid}, #{giverUid} ,#{receiverUid}, #{recordId}, #{giftId}, #{giftNum}, #{goldCost}, #{createTime})")
    void save(BillPointGiftGiveDO giveDo);
}
