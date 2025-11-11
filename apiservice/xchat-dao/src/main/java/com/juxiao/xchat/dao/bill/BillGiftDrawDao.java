package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillGiftDrawDO;
import com.juxiao.xchat.dao.bill.dto.BillGiftDrawDTO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户礼物抽奖账单记录
 *
 * @class: BillGiftDrawDao.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
public interface BillGiftDrawDao {

    /**
     * 查询用户抽奖记录
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<BillGiftDrawDTO> listByUid(@Param("uid") Long uid, @Param("giftType") Integer giftType, @Param("begin") Integer start, @Param("size") Integer size);

    /**
     * 保存礼物抽奖账单记录
     *
     * @param drawDo
     * @author: chenjunsheng
     * @date 2018/5/15
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_gift_draw` (`uid`,`gift_id`,`gift_num`,`gold_cost`,`create_time`) VALUES (#{uid},#{giftId},#{giftNum},#{goldCost},#{createTime})")
    void save(BillGiftDrawDO drawDo);

    /**
     * 查询用户抽奖次数
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("select COUNT(1) from `bill_gift_draw` where uid = #{uid}")
    int countUserDraw(@Param("uid") Long uid);

    /**
     * 批量保存捡海螺
     *
     * @param list
     * @return
     */
    @TargetDataSource
    int saveMany(List<BillGiftDrawDO> list);


}
