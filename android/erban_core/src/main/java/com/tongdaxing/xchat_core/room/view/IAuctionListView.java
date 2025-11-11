package com.tongdaxing.xchat_core.room.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionListUserInfo;

import java.util.List;

public interface IAuctionListView extends IMvpBaseView {
    void onRequestWeekAuctionList(List<AuctionListUserInfo> auctionListUserInfos);

    void onRequestTotalAuctionList(List<AuctionListUserInfo> auctionListUserInfos);
}

