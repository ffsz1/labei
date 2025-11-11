package com.erban.main.mybatismapper.dao;

import com.erban.main.model.domain.SensitiveWordsDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SensitiveWordDAO {

    @Select("SELECT " +
            "   a.id as id, " +
            "   a.sensitive_words as sensitiveWords, " +
            "   a.admin_id as adminId, " +
            "   a.create_time as createTime " +
            "from " +
            "   sensitive_words a ")
    List<SensitiveWordsDO> list();
}
