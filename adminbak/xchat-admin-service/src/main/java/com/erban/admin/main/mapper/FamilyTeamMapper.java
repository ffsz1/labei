package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.FamilyTeamDTO;
import com.erban.admin.main.model.FamilyTeam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.mapper
 * @date 2018/9/3
 * @time 22:55
 */
public interface FamilyTeamMapper {

    List<FamilyTeamDTO> selectByList(@Param("searchText") String searchText, @Param("type") Integer type, @Param("startDateStr") String startDateStr, @Param("endDateStr") String endDateStr);


    FamilyTeam selectById(@Param("teamId") Long teamId);

    int updateByDisplay(@Param("id") Long id, @Param("status") Integer status);


    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int update(FamilyTeam familyTeam);

    FamilyTeam getFamilyId(@Param("familyId") Long familyId);

    int updateStatusAndRoomId(@Param("id") Long id, @Param("status") Integer status, @Param("roomId") Long roomId);

    /**
     * 根据ID删除
     * @param teamId
     * @return
     */
    int deleteByTeamId(@Param("teamId") Long teamId);

}
