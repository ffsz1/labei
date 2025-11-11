package com.erban.main.mybatismapper;

import com.erban.main.model.AuctionDeal;
import com.erban.main.model.AuctionDealExample;
import java.util.List;

public interface AuctionDealMapper {
    int deleteByPrimaryKey(String auctId);

    int insert(AuctionDeal record);

    int insertSelective(AuctionDeal record);

    List<AuctionDeal> selectByExample(AuctionDealExample example);

    AuctionDeal selectByPrimaryKey(String auctId);

    int updateByPrimaryKeySelective(AuctionDeal record);

    int updateByPrimaryKey(AuctionDeal record);
}
