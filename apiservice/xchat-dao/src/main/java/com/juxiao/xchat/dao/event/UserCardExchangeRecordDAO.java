package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.event.domain.UserCardExchangeRecordDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface UserCardExchangeRecordDAO {

    @Insert("INSERT INTO user_card_exchange_record (" +
            "   uid, card_id, gift_id, type, gold_num, days, create_date" +
            ") VALUES (" +
            "   #{uid}, #{cardId}, #{giftId}, #{type}, #{goldNum}, #{days}, #{createDate}" +
            ")")
    @Options(useGeneratedKeys = true)
    int insert(UserCardExchangeRecordDO recordDO);
}
