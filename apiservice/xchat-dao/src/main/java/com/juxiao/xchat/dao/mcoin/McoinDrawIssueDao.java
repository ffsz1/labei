package com.juxiao.xchat.dao.mcoin;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawIssueDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawIssuesDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface McoinDrawIssueDao {

    /**
     * 更新当前拍卖状态
     *
     * @param issueId
     * @param issueStatus
     */
    @TargetDataSource
    @Update("UPDATE `mcoin_draw_issue` SET `issue_status` = #{issueStatus}, update_time = NOW() WHERE `id` = #{issueId}")
    void updateIssueStatus(@Param("issueId") Long issueId, @Param("issueStatus") Byte issueStatus);

    /**
     * 查询该期号
     *
     * @param issueId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT * FROM `mcoin_draw_issue` WHERE `id` = #{issueId}")
    McoinDrawIssueDTO getMcoinDrawIssue(@Param("issueId") Long issueId);

    /**
     * 查询当前有效的竞拍列表
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT id AS issueId, issue_url AS issueUrl, item_name AS itemName, item_type AS itemType,draw_num AS drawNum,price AS price FROM `mcoin_draw_issue` WHERE issue_status = 2 GROUP BY item_type ORDER BY id")
    List<McoinDrawIssuesDTO> listIssues();

    /**
     * 根据类型查询最近的有效期的列表（用于新一期活动开始的秘书推送）
     *
     * @param itemType
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT id AS issueId, issue_url AS issueUrl, item_name AS itemName, item_type AS itemType,draw_num AS drawNum,price AS price,push_msg_status AS pushMsgStatus FROM `mcoin_draw_issue` WHERE item_type = #{itemType} AND issue_status = 2 ORDER BY id ASC LIMIT 1")
    McoinDrawIssuesDTO findNewIssues(Byte itemType);

    @TargetDataSource
    @Update("UPDATE `mcoin_draw_issue` SET `push_msg_status` = #{pushMsgStatus}, update_time = NOW() WHERE `id` = #{issueId}")
    void updatePushMsgStatus(@Param("issueId") Long issueId, @Param("pushMsgStatus") Byte pushMsgStatus);
}
