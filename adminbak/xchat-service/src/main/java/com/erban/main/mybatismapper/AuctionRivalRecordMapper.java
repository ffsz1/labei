package com.erban.main.mybatismapper;

import com.erban.main.model.AuctionRivalRecord;
import com.erban.main.model.AuctionRivalRecordExample;

import java.util.List;

public interface AuctionRivalRecordMapper {
    int deleteByPrimaryKey(String auctId);

    int insert(AuctionRivalRecord record);

    int insertSelective(AuctionRivalRecord record);

    List<AuctionRivalRecord> selectByExample(AuctionRivalRecordExample example);

    AuctionRivalRecord selectByPrimaryKey(String auctId);

    int updateByPrimaryKeySelective(AuctionRivalRecord record);

    int updateByPrimaryKey(AuctionRivalRecord record);

/*
    Long getSum ();

    Long getNum();
*/


}
