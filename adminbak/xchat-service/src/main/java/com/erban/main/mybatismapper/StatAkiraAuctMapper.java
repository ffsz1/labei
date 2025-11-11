package com.erban.main.mybatismapper;

import com.erban.main.model.StatAkiraAuct;
import com.erban.main.model.StatAkiraAuctExample;
import java.util.List;

public interface StatAkiraAuctMapper {
    int deleteByPrimaryKey ( Long akiraAuctId );

    int insert ( StatAkiraAuct record );

    int insertSelective ( StatAkiraAuct record );

    List<StatAkiraAuct> selectByExample ( StatAkiraAuctExample example );

    StatAkiraAuct selectByPrimaryKey ( Long akiraAuctId );

    int updateByPrimaryKeySelective ( StatAkiraAuct record );

    int updateByPrimaryKey ( StatAkiraAuct record );
}
