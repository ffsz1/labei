package com.juxiao.xchat.dao.family.dao;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.family.domain.FamilyExitRecordDO;
import com.juxiao.xchat.dao.family.dto.FamilyRecordDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chris
 * @Title: 家族退出记录
 * @date 2018/11/26
 * @time 10:01
 */
public interface FamilyExitRecordDAO {

    /**
     * 保存数据
     * @param familyExitRecordDO
     * @return
     */
    @TargetDataSource
    int save(FamilyExitRecordDO familyExitRecordDO);

    /**
     * 根据uid及家族ID查找
     * @param teamId
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyExitRecordDO selectByTeamIdAndUid(@Param("teamId") Long teamId, @Param("uid") Long uid);

    /**
     * 更新状态
     * @param familyExitRecordDO
     * @return
     */
    @TargetDataSource
    int updateByStatus(FamilyExitRecordDO familyExitRecordDO);

    /**
     * 获取申请退出记录
     * @param teamId
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<FamilyRecordDTO> selectByFamilyId(@Param("teamId") Long teamId,@Param("current")Integer current, @Param("pageSize")Integer pageSize);

    /**
     * 更新状态
     * @param teamId
     * @param uid
     * @return
     */
    @TargetDataSource
    int updateStatusByTeamIdAndUid(@Param("teamId") Long teamId,@Param("uid") Long uid);
}
