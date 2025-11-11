package com.juxiao.xchat.dao.family.dao;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.family.domain.FamilyTeamDO;
import com.juxiao.xchat.dao.family.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/26
 * @time 10:07
 */
public interface FamilyTeamDAO {

    /**
     * 保存
     * @param familyTeamDO
     * @return
     */
    @TargetDataSource
    int save(FamilyTeamDO familyTeamDO);

    /**
     * 根据家族ID查询家族信息
     * @param teamId
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyTeamDO selectByTeamId(@Param("familyId") Long teamId);

    /**
     * 更新家族信息资料
     * @param familyTeamDO
     * @return
     */
    @TargetDataSource
    int updateByLogoAndNotice(FamilyTeamDO familyTeamDO);

    /**
     * 设置申请加入方式
     * @param id
     * @param familyId
     * @return
     */
    @TargetDataSource
    int setApplyJoinMethod(@Param("id")Long id,@Param("familyId")Long familyId,@Param("verification")Integer verification);

    /**
     * 获取家族列表分页信息
     * @param familyId
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<FamilyDTO> selectByPage(@Param("familyId") Long familyId, @Param("current")Integer current, @Param("pageSize")Integer pageSize);

    /**
     * 获取家族列表信息
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<FamilyDTO> selectByList();

    /**
     * 根据uid查询
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyJoinsDTO selectByUid(@Param("uid") Long uid);

    /**
     * 获取所有列表
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<FamilyTeamDTO> selectByRankingList();

    /**
     * 根据家族编号查询家族成员信息
     * @param teamId
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyTeamUsersDTO selectTeamIdByUsers(@Param("teamId") Long teamId);

    /**
     * 根据家族ID跟uid查询
     * @param uid
     * @param familyId
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyJoinsDTO selectByUidAndFamilyId(@Param("uid") Long uid, @Param("familyId") Long familyId);

    /**
     * 根据当前UID查询
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyTeamDO selectByUserId(@Param("uid")Long uid);


    /**
     * 根据当前UID查询排名
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyJoinsDTO selectByUidRanking(@Param("uid")Long uid);

    /**
     * 设置邀请他人权限
     * @param id
     * @param invitemode
     * @return
     */
    int updateByInvoteMode(@Param("id") Long id, @Param("invitemode") Integer invitemode);

    /**
     * 根据家族编号查询家族信息
     * @param teamId
     * @return
     */

    @TargetDataSource(name = "ds2")
    FamilyTeamDO selectById(@Param("teamId") Long teamId);

    /**
     * 根据familyId查询家族信息
     * @param familyId
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyTeamDO selectByFamilyId(@Param("familyId") Long familyId);

    /**
     * 查询审核家族
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<FamilyDTO> selectAuditingVersion();

    /**
     * 查询马甲包审核家族
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<FamilyDTO> selectAuditingGGLVersion();

    /**
     * 获取家族成员数据
     * @param teamId
     * @param size
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<FamilyUsersDTO> selectFamilyMemberByTeamId(@Param("teamId")Long teamId,@Param("size")Integer size);

    /**
     * 根据UID获取家族信息
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyDTO selectFamilyByUid(@Param("uid") Long uid);

    /**
     * 根据家族Id获取家族信息
     * @param familyId
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyDTO selectFamilyById(@Param("familyId") Long familyId);

}
