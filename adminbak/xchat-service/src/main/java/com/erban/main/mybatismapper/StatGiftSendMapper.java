package com.erban.main.mybatismapper;

import com.erban.main.model.StatGiftSend;
import com.erban.main.model.StatGiftSendExample;
import java.util.List;

public interface StatGiftSendMapper {
    int deleteByPrimaryKey ( Long giftSendId );

    int insert ( StatGiftSend record );

    int insertSelective ( StatGiftSend record );

    List<StatGiftSend> selectByExample ( StatGiftSendExample example );

    StatGiftSend selectByPrimaryKey ( Long giftSendId );

    int updateByPrimaryKeySelective ( StatGiftSend record );

    int updateByPrimaryKey ( StatGiftSend record );
}
