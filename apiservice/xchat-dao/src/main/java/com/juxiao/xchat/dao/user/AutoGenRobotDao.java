package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.domain.AutoGenRobot;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AutoGenRobotDao {

    @TargetDataSource(name = "ds2")
    @Select("SELECT * from auto_gen_robot where avatar is not null and is_used = 1")
    List<AutoGenRobot> list();

}