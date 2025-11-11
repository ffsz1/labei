package com.tongdaxing.xchat_core.room.auction;

import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by zhouxiangfeng on 2017/5/28.
 */

public interface IAuctionCore extends IBaseCore {
    AuctionInfo getCurrentAuctionInfo();

    /**
     * 房主开始竞拍
     * uid: 必填房主uid
     * ticket：必填
     * auctUid:被拍卖声优UID，必填
     * auctMoney：拍卖起拍价，必填
     * servDura：服务时长，本期需求直接写死30（分钟）
     * minRaiseMoney：最低竞拍报价
     * auctDesc:竞拍描述，选填
     */
    void auctionStart(long uid, long auctUid, int auctMoney, int servDura, int minRaiseMoney, String auctDesc);

    /**
     * 用户参与竞拍报价
     * uid: 必填房主uid
     * ticket：必填
     * auctUid:参与人id
     * auctId:拍卖单ID
     * type:用户竞拍动作，1加价，2出价（自行填写价格）
     * money：金额
     *
     * @return
     */
    void auctionUp(long uid, long auctUid, String auctId, int type, int money);

    /**
     * 房主结束竞拍
     * uid: 必填房主uid
     * ticket：必填
     * auctId:当前拍卖单ID
     */
    void finishAuction(long uid, String auctId);

    /**
     * 获取周竞拍榜
     *
     * @param roomUid
     */
    void requestWeekAuctionList(long roomUid);
    /**
     * 获取总竞拍榜
     *
     * @param roomUid
     */
    void requestTotalAuctionList(long roomUid);
}
