package com.juxiao.xchat.dao.family.dao;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.family.domain.FamilyGiftRecordDO;
import com.juxiao.xchat.dao.family.dto.FamilyTeamRecordDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/26
 * @time 10:02
 */
public interface FamilyGiftRecordDAO {


    /**
     * 统计用户威望
     * @param teamId
     * @return
     */
    @TargetDataSource(name = "ds2")
    int countPrestige(@Param("teamId") Long teamId);

    /**
     *
     * @param familyGiftRecordDO
     */
    @TargetDataSource
    int save(FamilyGiftRecordDO familyGiftRecordDO);

    /**
     * 统计用户威望
     * @param teamId
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    int countPrestigeByUidAndTeamId(@Param("teamId")Long teamId, @Param("uid")Long uid);

    /**
     * 踢出用户之后把用户威望设为0
     * @param teamId
     * @param userId
     * @return
     */
    @TargetDataSource
    int removeUserGiftRecord(@Param("teamId") Long teamId, @Param("userId") Long userId);

    /**
     * 查询家族送礼记录
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<FamilyTeamRecordDTO> selectByTeamGiftRecord();

    /**
     * 删除家族送礼记录
     */
    @Update("truncate table family_gift_record")
    void cleanTeamGiftRecord();
}
