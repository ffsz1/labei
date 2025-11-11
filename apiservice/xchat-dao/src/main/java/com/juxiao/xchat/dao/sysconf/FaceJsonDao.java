package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.dto.FaceJsonDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FaceJsonDao {

    /**
     * 查询可用列表
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT `id` AS id,`version` AS version,`status` AS `status`,`create_time` AS createTime,`json` AS json FROM face_json WHERE status = 1")
    List<FaceJsonDTO> list();
}
