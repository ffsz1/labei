package com.juxiao.xchat.dao.family.dao;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.family.domain.FamilyApplyJoinRecordDO;
import com.juxiao.xchat.dao.family.dto.FamilyRecordDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chris
 * @Title: 申请加入家族DAO操作数据库接口
 * @date 2018/11/26
 * @time 10:00
 */
public interface FamilyApplyJoinRecordDAO {

    /**
     *  保存数据
     * @param familyApplyJoinRecordDO
     * @return
     */
    @TargetDataSource
    int insert(FamilyApplyJoinRecordDO familyApplyJoinRecordDO);

    /**
     * 根据uid及家族ID查询未审核加入记录
     * @param teamId
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyApplyJoinRecordDO selectByUidAndTeamId(@Param("teamId") Long teamId, @Param("uid") Long uid);

    /**
     * 更新状态
     * @param teamId 家族ID
     * @param uid uid
     * @param status
     * @return
     */
    @TargetDataSource
    int updateByStatus(@Param("teamId") Long teamId, @Param("uid") Long uid, @Param("status") int status);

    /**
     * 根据familyId查询记录
     * @param familyId
     * @return
     */
    @TargetDataSource(name= "ds2")
    List<FamilyRecordDTO> selectByFamilyId(@Param("familyId") Long familyId,@Param("current")Integer current, @Param("pageSize")Integer pageSize);
}
