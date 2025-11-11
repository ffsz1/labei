package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserDrawGiftDayDO;
import com.juxiao.xchat.dao.user.dto.UserDrawGiftDayDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-06-05
 * @time 09:56
 */
public interface UserDrawGiftDayDao {

    /**
     * 保存房间捡海螺日流水
     *
     * @param  userDrawGiftDayDO userDrawGiftDayDO
     */
    @TargetDataSource
    @Insert("INSERT INTO `user_draw_gift_day`(`uid`, `create_date`, `input_num`, `out_num`) VALUES (#{uid}, #{createDate}, #{inputNum}, #{outNum}) ON DUPLICATE KEY UPDATE `input_num`=#{inputNum}, `out_num`= #{outNum}")
    void saveOrUpdate(UserDrawGiftDayDO userDrawGiftDayDO);


    /**
     * 按时间段查询房间捡海螺流水
     *
     * @param startDate startDate
     * @param endDate endDate
     * @return List<UserDrawGiftDayDTO>
     */
    @TargetDataSource(name = "ds2")
    @Select("select bgd.uid,sum(bgd.gold_cost) as inputNum ,sum(g.gold_price * bgd.gift_num) as outputNum from bill_gift_draw bgd inner join gift g on g.gift_id = bgd.gift_id WHERE bgd.`create_time` >= #{startDate} AND bgd.create_time <= #{endDate} group by bgd.uid")
    List<UserDrawGiftDayDTO> listDayUsersDrawGift(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


}
