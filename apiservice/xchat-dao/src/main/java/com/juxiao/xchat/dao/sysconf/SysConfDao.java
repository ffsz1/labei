package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.dto.SplashScreenDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.dto.TopTabDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统配置
 *
 * @class: SysConfDO.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface SysConfDao {
    @TargetDataSource
    // @Update("UPDATE sys_conf SET config_value = #{value} WHERE config_id = #{configId}")
    void updateConfValue(@Param("value") String value, @Param("configId") String configId);

    /**
     * 获取系统配置
     *
     * @param configId
     * @return
     */
    @TargetDataSource(name = "ds2")
    SysConfDTO getSysConf(@Param("configId") String configId);

    @TargetDataSource(name = "ds2")
    SplashScreenDTO getSplashScreen(@Param("userType") Integer userType);

    @TargetDataSource(name = "ds2")
    @Select("select config_name as tabName,config_value as tabValue,name_space nameSpace from sys_conf where name_space=#{nameSpace} and config_status=#{status} order by config_value")
    List<TopTabDTO> getSysConfigByNameSpace(@Param("nameSpace")String nameSpace,@Param("status") Integer status);
}