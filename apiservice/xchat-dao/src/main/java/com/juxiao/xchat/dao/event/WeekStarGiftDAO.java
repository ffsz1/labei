package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.event.domain.WeekStarGiftDO;
import com.juxiao.xchat.dao.event.domain.WeekStarItemRewardDO;
import com.juxiao.xchat.dao.event.dto.WeekStarGiftDTO;
import com.juxiao.xchat.dao.event.dto.WeekStarGiftNoticeDTO;
import com.juxiao.xchat.dao.event.dto.WeekStarItemRewardDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-05-20
 * @time 11:36
 */
public interface WeekStarGiftDAO {


    @TargetDataSource(name = "ds2")
    @Select("select id,gift_id as giftId,status,create_time as createTime,admin_id as adminId,seq from week_star_gift_notice where status = 1")
    List<WeekStarGiftNoticeDTO> findWeekStarGiftNotice();

    @TargetDataSource(name = "ds2")
    @Select("select id,gift_id as giftId,status,create_time as createTime,admin_id as adminId,seq from week_star_gift where status = 1")
    List<WeekStarGiftDTO> findWeekStarGift();

    @TargetDataSource(name = "ds2")
    @Select("select id,status,create_time as createTime,gift_id as giftId,item_id as itemId,admin_id as adminId,type,days,content,seq from week_star_item_reward where status = 1")
    List<WeekStarItemRewardDTO> findWeekStarItemReward();

    @TargetDataSource(name = "ds2")
    @Select("select id,status,create_time as createTime,gift_id as giftId,item_id as itemId,admin_id as adminId,type,days,content,seq from week_star_item_notice_reward where status = 1")
    List<WeekStarItemRewardDTO> findWeekStarItemNoticeReward();

    /**
     * 保存
     * @param weekStarGiftDO weekStarGiftDO
     * @return int
     */
    @TargetDataSource
    int save(WeekStarGiftDO weekStarGiftDO);


    /**
     * 保存
     * @param weekStarItemRewardDO weekStarItemRewardDO
     * @return int
     */
    @TargetDataSource
    int insert(WeekStarItemRewardDO weekStarItemRewardDO);

    /**
     * 更新状态
     * @param id id
     * @return int
     */
    @TargetDataSource
    int updateByWeekStarGiftStatus(@Param("id") Long id);

    /**
     * 更新状态
     * @param id id
     * @return int
     */
    @TargetDataSource
    int updateByWeekStarGiftNoticeStatus(@Param("id")Integer id);

    /**
     * 更新状态
     * @param id id
     * @return int
     */
    @TargetDataSource
    int updateByWeekStarItemRewardStatus(@Param("id")Integer id);

    @TargetDataSource
    void updateByWeekStarItemNoticeRewardStatus(@Param("id")Integer id);
}
