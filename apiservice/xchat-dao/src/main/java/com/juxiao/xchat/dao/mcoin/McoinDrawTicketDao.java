package com.juxiao.xchat.dao.mcoin;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mcoin.domain.McoinDrawTicketDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface McoinDrawTicketDao {

    /**
     * 保存记录
     *
     * @param ticketDo
     */
    @TargetDataSource
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO `mcoin_draw_ticket`(`issue_id`, `record_id`, `uid`, `create_time`, `update_time`) VALUES (#{issueId}, #{recordId}, #{uid}, #{createTime}, #{updateTime});")
    void save(McoinDrawTicketDO ticketDo);

    /**
     * 更新该条记录的号码
     *
     * @param ticketDo
     */
    @TargetDataSource
    @Update("UPDATE `mcoin_draw_ticket` SET `ticket_no` = #{ticketNo} WHERE id = #{id}")
    void updateTicketNo(McoinDrawTicketDO ticketDo);

    /**
     * 统计某一期分配的号码数量
     *
     * @param issueId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT COUNT(*) FROM `mcoin_draw_ticket` WHERE issue_id = #{issueId}")
    int countIssueTickets(@Param("issueId") Long issueId);

    /**
     * 查询号码
     *
     * @param ticketId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT ticket_no FROM `mcoin_draw_ticket` WHERE id=#{ticketId}")
    String getTicketNo(@Param("ticketId") Long ticketId);

    /**
     * 查询指定用户下某个记录的号码
     *
     * @param uid
     * @param recordId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT ticket_no FROM `mcoin_draw_ticket` WHERE uid = #{uid} AND record_id = #{recordId}")
    List<String> listUserRecordTickets(@Param("uid") Long uid, @Param("recordId") Long recordId);

    /**
     * 查询参与竞拍用户
     *
     * @param issueId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT DISTINCT uid FROM mcoin_draw_ticket WHERE issue_id = #{issueId};")
    List<Long> listIssueUsers(@Param("issueId") Long issueId);
}
