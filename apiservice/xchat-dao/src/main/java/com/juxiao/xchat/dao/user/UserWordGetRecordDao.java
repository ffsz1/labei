package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.user.domain.UserWordGetRecordDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface UserWordGetRecordDao {

    /**
     * 添加
     * @param data
     */
    @Insert("insert into user_word_get_recode (uid, word, activity_type, create_time) value(#{uid}, #{word}, #{activityType}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "recordId", keyColumn = "record_id")
    void insert(UserWordGetRecordDO data);


}
