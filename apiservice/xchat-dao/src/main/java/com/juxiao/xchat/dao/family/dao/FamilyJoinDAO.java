package com.juxiao.xchat.dao.family.dao;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.family.domain.FamilyJoinDO;
import com.juxiao.xchat.dao.family.dto.FamilyJoinDTO;
import com.juxiao.xchat.dao.family.dto.FamilyTeamJoinDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/26
 * @time 10:04
 */
public interface FamilyJoinDAO {


    /**
     * 根据用户ID及家族ID查询角色信息
     * @param uid
     * @param teamId
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyJoinDTO selectByFamilyJoin(@Param("uid") Long uid, @Param("teamId") Long teamId);

    /**
     * 根据uid移除成员
     * @param uid
     * @return
     */
    @TargetDataSource
    int deleteByUid(@Param("uid") Long uid);

    /**
     * 设置管理员
     * @param teamId
     * @param uid
     * @return
     */
    @TargetDataSource
    int setupAdministrator(@Param("teamId")Long teamId, @Param("uid")Long uid);

    /**
     * 保存
     * @param familyJoinDO
     * @return
     */
    @TargetDataSource
    int save(FamilyJoinDO familyJoinDO);

    /**
     * 统计家族成员人数
     * @param teamId
     * @return
     */
    @TargetDataSource(name = "ds2")
    int countMember(@Param("teamId") Long teamId);

    /**
     *  根据uid查询
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    FamilyJoinDO selectFamilyJoinByUid(@Param("uid") Long uid);

    /**
     * 根据家族ID查询成员信息
     * @param teamId
     * @param current
     * @param pageSize
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<FamilyTeamJoinDTO> selectFamilyTeamJoinByTeamId(@Param("teamId")Long teamId, @Param("current")Integer current, @Param("pageSize")Integer pageSize);

    /**
     * 从家族中移除
     * @param uid
     * @param teamId
     * @return
     */
    @TargetDataSource
    int deleteByUidAndTeamId(@Param("uid") Long uid, @Param("teamId") Long teamId);

    /**
     * 移除管理员
     * @param id
     * @param uid
     * @return
     */
    @TargetDataSource
    int removeAdmin(@Param("teamId") Long id,@Param("uid") Long uid);

    /**
     * 获取家族角色数
     * @param teamId
     * @return
     */
    @TargetDataSource(name = "ds")
    int getRoleStatusCount(@Param("teamId")Long teamId);

    /**
     * 根据uid查询是否加入家族
     * @param userId
     * @return
     */
    @TargetDataSource(name = "ds")
    FamilyJoinDO selectByUserId(@Param("userId") Long userId);

    /**
     * 设置消息提醒
     * @param id
     * @param ope
     * @return
     */
    int updateFamilyJoinByOpe(@Param("id") Long id,@Param("ope") Integer ope);

    /**
     * 设置禁言解禁
     * @param id
     * @param mute
     * @return
     */
    int updateFamilyJoinByMute(@Param("id") Long id,@Param("mute") Integer mute);

    /**
     * 统计家族人数
     * @param familyId
     * @return
     */
    @TargetDataSource(name = "ds")
    int countMemberByFamilyId(@Param("familyId") Long familyId);

    /**
     *根据UID获取家族角色
     * @param uid
     * @return
     */
    Integer selectRoleStatusByUid(@Param("uid")Long uid);
}
