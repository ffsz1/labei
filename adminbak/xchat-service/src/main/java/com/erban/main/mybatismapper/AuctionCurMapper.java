package com.erban.main.mybatismapper;

import com.erban.main.model.AuctionCur;
import com.erban.main.model.AuctionCurExample;
import java.util.List;

public interface AuctionCurMapper {
    int deleteByPrimaryKey(String auctId);

    int insert(AuctionCur record);

    int insertSelective(AuctionCur record);

    List<AuctionCur> selectByExample(AuctionCurExample example);

    AuctionCur selectByPrimaryKey(String auctId);

    int updateByPrimaryKeySelective(AuctionCur record);

    int updateByPrimaryKey(AuctionCur record);
}
