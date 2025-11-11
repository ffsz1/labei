package com.xchat.oauth2.service.infrastructure.mybatis;

import com.xchat.oauth2.service.vo.SysConf;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysConfMapperMybatis {

    @Select(" select config_id, config_name, config_value, name_space, config_status from sys_conf where config_id = #{configId,jdbcType=VARCHAR}")
    SysConf selectByPrimaryKey(@Param("configId") String configId);
}
