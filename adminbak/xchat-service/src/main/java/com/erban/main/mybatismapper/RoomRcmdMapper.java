package com.erban.main.mybatismapper;

import com.erban.main.model.RoomRcmd;
import com.erban.main.model.RoomRcmdExample;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RoomRcmdMapper {
    int countByExample(RoomRcmdExample example);

    int deleteByExample(RoomRcmdExample example);

    int deleteByPrimaryKey(Integer rcmdId);

    int insert(RoomRcmd record);

    int insertSelective(RoomRcmd record);

    List<RoomRcmd> selectByExample(RoomRcmdExample example);

    RoomRcmd selectByPrimaryKey(Integer rcmdId);

    int updateByExampleSelective(@Param("record") RoomRcmd record, @Param("example") RoomRcmdExample example);

    int updateByExample(@Param("record") RoomRcmd record, @Param("example") RoomRcmdExample example);

    int updateByPrimaryKeySelective(RoomRcmd record);

    int updateByPrimaryKey(RoomRcmd record);

    /**
     * 查询时间段冲突的推荐信息
     *
     * @param startDate
     * @param endDate
     * @return
     */
    int countConflictRcmd(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("rcmdId") Integer rcmdId);
}
