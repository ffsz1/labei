package com.erban.admin.main.mapper;

import com.erban.admin.main.model.FamilyJoin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.mapper
 * @date 2018/9/3
 * @time 22:55
 */
public interface FamilyJoinMapper {

    int save(FamilyJoin familyJoin);

    void updateByFamilyApplyJoinRecord(@Param("teamId") Long teamId, @Param("uid") Long uid, @Param("checkStatus") int checkStatus);

    FamilyJoin selectByUid(@Param("uid") Long uid);

    int deleteByTeamId(@Param("teamId") Long teamId);

    List<Long> selectTeamIdByFamilyJoinList(@Param("teamId") Long teamId);
}
