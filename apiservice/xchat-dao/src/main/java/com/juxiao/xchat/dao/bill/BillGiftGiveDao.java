package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillGiftGiveDO;
import com.juxiao.xchat.dao.bill.dto.BillUserGiftDTO;
import com.juxiao.xchat.dao.bill.query.BillUserQuery;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

/**
 * 赠送送礼物记录操作
 *
 * @class: BillGiftGiveDao.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public interface BillGiftGiveDao {

    /**
     * 保存赠送礼物账单记录
     *
     * @param giveDo
     * @author: chenjunsheng
     * @date 2018/5/21
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_gift_give` (`room_uid`, `giver_uid`, `receiver_uid`, `record_id`, `gift_id`, `gift_num`, `gold_cost`, `diamond_amount`, `create_time`) VALUES (#{roomUid}, #{giverUid} ,#{receiverUid}, #{recordId}, #{giftId}, #{giftNum}, #{goldCost}, #{diamondAmount}, #{createTime})")
    void save(BillGiftGiveDO giveDo);

    /**
     * 保存打call账单记录
     *
     * @param giveDo
     * @author: chenjunsheng
     * @date 2018/5/21
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_gift_call` (`room_uid`, `giver_uid`, `receiver_uid`, `record_id`, `gift_id`, `gift_num`, `gold_cost`, `diamond_amount`, `create_time`) VALUES (#{roomUid}, #{giverUid} ,#{receiverUid}, #{recordId}, #{giftId}, #{giftNum}, #{goldCost}, #{diamondAmount}, #{createTime})")
    void saveCallBill(BillGiftGiveDO giveDo);


    /**
     * 查询用户赠送礼物
     *
     * @param query
     * @return
     * @author: chenjunsheng
     * @date 2018/5/16
     */
    @TargetDataSource(name = "ds2")
    List<BillUserGiftDTO> listUserGiftPay(BillUserQuery query);

    /**
     * 查询用户礼物收入
     *
     * @param query
     * @return
     * @author: chenjunsheng
     * @date 2018/5/16
     */
    @TargetDataSource(name = "ds2")
    List<BillUserGiftDTO> listUserGiftIncome(BillUserQuery query);
}
