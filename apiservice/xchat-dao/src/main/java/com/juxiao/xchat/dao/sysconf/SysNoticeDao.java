package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.dto.SysNoticeDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysNoticeDao {

    @TargetDataSource(name = "ds2")
    @Select("select * from sys_notice order by seq asc")
    List<SysNoticeDTO> findAll();


}
