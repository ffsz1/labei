package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.dto.AppVersionDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AppVersionDao {

//    @TargetDataSource(name = "ds2")
//    @Select("SELECT `version_id` AS versionId,`os` AS os,`version` AS version,`platform` AS platform,`status` AS status,`version_desc` AS versionDesc,`create_time` AS createTime,`publish_time` AS publishTime FROM app_version WHERE version = #{version} AND os = #{os} LIMIT 1")
//    AppVersionDTO listAppVersions(@Param("version") String version, @Param("os") String os);

    @TargetDataSource(name = "ds2")
    AppVersionDTO listAppVersions(@Param("version") String version, @Param("os") String os,@Param("appId") String appId);
}
