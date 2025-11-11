package com.erban.main.mybatismapper;

import com.erban.main.model.StatRoomAuct;
import com.erban.main.model.StatRoomAuctExample;
import java.util.List;

public interface StatRoomAuctMapper {
    int deleteByPrimaryKey ( Long roomAuctId );

    int insert ( StatRoomAuct record );

    int insertSelective ( StatRoomAuct record );

    List<StatRoomAuct> selectByExample ( StatRoomAuctExample example );

    StatRoomAuct selectByPrimaryKey ( Long roomAuctId );

    int updateByPrimaryKeySelective ( StatRoomAuct record );

    int updateByPrimaryKey ( StatRoomAuct record );
}
