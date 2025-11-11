package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.event.domain.UserCardChipRecordDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface UserCardChipRecordDAO {

    @Insert("INSERT INTO user_card_chip_record (" +
            "   uid, chip_source, num, send_uid, create_date, chip_id, operate_type " +
            ") VALUES (" +
            "   #{uid}, #{chipSource}, #{num}, #{sendUid}, #{createDate}, #{chipId}, #{operateType}" +
            ")")
    @Options(useGeneratedKeys = true)
    int insert(UserCardChipRecordDO recordDO);
}
