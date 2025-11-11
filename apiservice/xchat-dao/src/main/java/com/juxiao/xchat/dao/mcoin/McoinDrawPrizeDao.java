package com.juxiao.xchat.dao.mcoin;


import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mcoin.domain.McoinDrawPrizeDO;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawPrizeDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface McoinDrawPrizeDao {

    /**
     * 保存用户中奖结果
     *
     * @param prizeDo
     */
    @TargetDataSource
    @Select("INSERT INTO `mcoin_draw_prize`(`issue_id`, `uid`, `ticket_id`, `create_time`) VALUES (#{issueId}, #{uid}, #{ticketId}, #{createTime})")
    void save(McoinDrawPrizeDO prizeDo);

    /**
     * 获取某一期的中间记录
     *
     * @param issueId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT * FROM `mcoin_draw_prize` WHERE issue_id = #{issueId}")
    McoinDrawPrizeDTO getMcoinDrawPrize(@Param("issueId") Long issueId);
}
