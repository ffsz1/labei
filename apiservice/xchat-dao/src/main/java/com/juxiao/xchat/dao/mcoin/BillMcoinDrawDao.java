package com.juxiao.xchat.dao.mcoin;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mcoin.domain.BillMcoinDrawDO;
import com.juxiao.xchat.dao.mcoin.dto.BillMcoinDrawDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawInvolvedUserDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinUserDrawRecordDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinUserHistoryRecordDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 */
public interface BillMcoinDrawDao {
    /**
     * 保存账单
     *
     * @param drawDo
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_mcoin_draw`(`uid`, `issue_id`, `draw_count`, `mcoin_cost`, `create_time`) VALUES (#{uid}, #{issueId}, #{drawCount}, #{mcoinCost}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(BillMcoinDrawDO drawDo);

    /**
     * 根据主键查询该条记录
     *
     * @param recordId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT * FROM `bill_mcoin_draw` WHERE `id` = #{recordId}")
    BillMcoinDrawDTO getBillMcoinDraw(@Param("recordId") Long recordId);

    /**
     * 查询参与的人数
     *
     * @param issueId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT uid AS uid, draw_count AS drawCount, create_time AS createTime FROM `bill_mcoin_draw` WHERE issue_id = #{issueId} LIMIT #{start}, #{limit}")
    List<McoinDrawInvolvedUserDTO> listDrawUsers(@Param("issueId") Long issueId, @Param("start") Integer start, @Param("limit") Integer limit);

    /**
     * 查询用户进行中参与记录
     *
     * @param uid
     * @param start
     * @param limit
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<McoinUserDrawRecordDTO> listUserDrawingRecords(@Param("uid") Long uid, @Param("start") Integer start, @Param("limit") Integer limit);

    /**
     * 查询用户参与已开奖参与记录
     *
     * @param uid
     * @param start
     * @param limit
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<McoinUserDrawRecordDTO> listUserDrawedRecords(@Param("uid") Long uid, @Param("start") Integer start, @Param("limit") Integer limit);

    /**
     * 查询用户的中奖纪录
     *
     * @param uid
     * @param start
     * @param limit
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<McoinUserDrawRecordDTO> listUserPrizeRecords(@Param("uid") Long uid, @Param("start") Integer start, @Param("limit") Integer limit);

    /**
     * 查询往期用户中奖记录
     *
     * @param start
     * @param limit
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<McoinUserHistoryRecordDTO> listPrizeHistroyRecords(@Param("start") Integer start, @Param("limit") Integer limit);
}
