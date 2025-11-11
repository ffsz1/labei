package com.tongdaxing.xchat_core.room.auction;

import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionListUserInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/5/28.
 */

public interface IAuctionCoreClient extends  ICoreClient {

    public static final String METHOD_ON_CURRENT_AUCTION_INFO_UPDATE = "onCurrentAuctionInfoUpdate";
    public static final String METHOD_ON_AUCTION_START = "onAuctionStart";
    public static final String METHOD_ON_AUCTION_FINISH = "onAuctionFinish";
    public static final String METHOD_ON_AUCTION_UPDATE = "onAuctionUpdate";
    public static final String METHOD_ON_AUCTION_UP = "onAuctionUp";
    public static final String METHOD_ON_AUCTION_UP_FAIL = "onAuctionUpFail";
    public static final String METHOD_ON_REQUEST_WEEK_AUCTION_LIST = "onRequestWeekAuctionList";
    public static final String METHOD_ON_REQUEST_WEEK_AUCTION_LIST_FAIL = "onRequestWeekAuctionListFail";
    public static final String METHOD_ON_REQUEST_TOTAL_AUCTION_LIST = "onRequestTotalAuctionList";
    public static final String METHOD_ON_REQUEST_TOTAL_AUCTION_LIST_FAIL = "onRequestTotalAuctionListFail";

    void onAuctionUp();
    void onAuctionUpFail(int code);
    void onCurrentAuctionInfoUpdate(AuctionInfo auctionInfo);
    void onAuctionStart(AuctionInfo auctionInfo);
    void onAuctionFinish(AuctionInfo auctionInfo);
    void onAuctionUpdate(AuctionInfo auctionInfo);
    void onRequestWeekAuctionList(List<AuctionListUserInfo> auctionListUserInfos);
    void onRequestWeekAuctionListFail(String msg);
    void onRequestTotalAuctionList(List<AuctionListUserInfo> auctionListUserInfos);
    void onRequestTotalAuctionListFail(String msg);
}
