package com.erban.main.mybatismapper;

import com.erban.main.model.AutoGenRobot;
import com.erban.main.model.AutoGenRobotExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoGenRobotMapper {
    int deleteByExample(AutoGenRobotExample example);

    int deleteByPrimaryKey(String nick);

    int insert(AutoGenRobot record);

    int insertSelective(AutoGenRobot record);

    List<AutoGenRobot> selectByExample(AutoGenRobotExample example);

    AutoGenRobot selectByPrimaryKey(String nick);

    AutoGenRobot selectById(Integer id);

    int updateByPrimaryKeySelective(AutoGenRobot record);

    int updateByPrimaryKey(AutoGenRobot record);

    int modifiedRobotPassword(@Param("robotIds") List<Integer> robotIds, @Param("password") String password);
}
