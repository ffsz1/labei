package com.erban.main.mybatismapper;

import com.erban.main.model.AuctionDeal;
import com.erban.main.model.StatAkiraAuctVo;
import com.erban.main.model.StatRoomAuctVo;

import java.util.List;

public interface AuctionDealMapperExpand {

    List<AuctionDeal> getAuctList();

    List<StatAkiraAuctVo> getAkiraAucts();


    List<StatRoomAuctVo> getRoomAucts();
}
