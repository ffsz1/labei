package com.erban.main.service.job;

import com.erban.main.model.*;
import com.erban.main.service.*;
import com.erban.main.service.gift.GiftSendRecordService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.statis.StatAkiraAuctService;
import com.erban.main.service.statis.StatGiftSendService;
import com.erban.main.service.statis.StatRoomAuctService;
import com.xchat.common.utils.CommonUtil;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 北岭山下 on 2017/8/7.
 */
/*
                更新统计数据操作
                        三个表
                                stat_gift_send礼物发送统计表
                                stat_room_auct房间拍卖成交统计表
                                stat_akira_auct声优拍卖成交统计表
 */
public class RefreshCountJob implements Job {


        private static final Logger LOGGER = Logger.getLogger ( RefreshCountJob.class );


        @Autowired
        AuctionDealService auctionDealService;
        @Autowired
        StatRoomAuctService statRoomAuctService;
        @Autowired
        StatAkiraAuctService statAkiraAuctService;
        @Autowired
        RoomService roomService;

        @Autowired
        GiftSendRecordService giftSendRecordService;
        @Autowired
        StatGiftSendService statGiftSendService;

        @Override
        public void execute ( JobExecutionContext jobExecutionContext ) throws JobExecutionException {

               /*
                        更新房间拍卖成交记录表auction_deal
                                room_auct_id - 房间拍卖单号(创建)
                                uid - 房间UID --竞拍房间房主UID,通过UID查找RoomId
                                total_auct_money - 每个房间拍卖成功的金额总数（deal_money）
                                total_auct_count-   每个房间拍卖的次数（每个UID只能创建一个房间）
                                stat_date -     天数（只有day）
                                create_time-    时间
                 */
                LOGGER.info ( "----------更新房间拍卖成交记录表----------" );
                //获取用户List
                List <StatRoomAuct> statRoomAuctList = new ArrayList <> ( );
                //auction_deal 中的拍卖次数以及拍卖总数
                List <StatRoomAuctVo> statRoomAuctVos = auctionDealService.getRoomAucts ( );
                LOGGER.info ( "==========获取房间拍卖成交记录 statRoomAuctList==========" );
                for ( int i = 0 ; i < statRoomAuctVos.size ( ) ; i++ ) {
                        LOGGER.info ( "==========UID:" + statRoomAuctVos.get ( i ).getUid ( ) +"==========");
                        LOGGER.info ( "==========Total_auct_count:" + statRoomAuctVos.get ( i ).getTotal_auct_count ( ) +"==========");
                        LOGGER.info ( "==========Total_auct_money:" + statRoomAuctVos.get ( i ).getTotal_auct_money ( )+"==========" );
                }
                //房主UIDList
                List <Long> uidArray = new ArrayList <> ( );
                for ( int i = 0 ; i < statRoomAuctVos.size ( ) ; i++ ) {
                        StatRoomAuct statRoomAuct = new StatRoomAuct ( );
                        Long roomAuctId = Long.parseLong ( CommonUtil.getRandomNumStr ( 10 ) );
                        statRoomAuct.setRoomAuctId ( roomAuctId );
                        uidArray.add ( i, statRoomAuctVos.get ( i ).getUid ( ) );
                        statRoomAuct.setTotalAuctCount ( statRoomAuctVos.get ( i ).getTotal_auct_count ( ) );
                        statRoomAuct.setTotalAuctMoney ( statRoomAuctVos.get ( i ).getTotal_auct_money ( ) );
                        statRoomAuctList.add ( statRoomAuct );
                }
                LOGGER.info ( "==========房主UIDList:" + uidArray.toString ( )+ "==========");
                //通过UID查找RoomId
                List <Room> roomList = roomService.getRoomListByUids ( uidArray );
                //将RoomId写入StatRoomAuct
                for ( int i = 0 ; i < roomList.size ( ) ; i++ ) {
                        //UID对应一个roomId
                        //通过UID和statRoomAuctList对应
                        for ( int j = 0 ; j < roomList.size ( ) ; j++ ) {
                                if ( uidArray.get ( i ).equals ( roomList.get ( j ).getUid ( ) ) ) {
                                        statRoomAuctList.get ( i ).setUid ( roomList.get ( j ).getRoomId ( ) );
                                }
                        }
                }
                //输出
                for ( int i = 0 ; i < statRoomAuctList.size ( ) ; i++ ) {
                        LOGGER.info ( "==========getRoomAuctId:" + statRoomAuctList.get ( i ).getRoomAuctId ( )+"==========" );
                        LOGGER.info ( "==========getUid:" + statRoomAuctList.get ( i ).getUid ( ) +"==========");
                        LOGGER.info ( "==========getTotalAuctCount:" + statRoomAuctList.get ( i ).getTotalAuctCount ( )+"==========" );
                        LOGGER.info ( "==========getTotalAuctMoney:" + statRoomAuctList.get ( i ).getTotalAuctMoney ( )+"==========" );
                }
                statRoomAuctService.insertList ( statRoomAuctList );

                /*
                        更新声优拍卖成交表
                                akira_auct_id
                                uid     - 声优UID （最终拍卖成交单中被拍卖的UID都被视为声优UID：auction_deal中的auct_uid）
                                total_auct_money        - 拍卖订单的总数
                                total_auct_count        - 该声优被拍拍卖的总次数
                                stat_date
                                create_time

                 */
                LOGGER.info ( "----------更新声优拍卖成交表----------" );
                List <StatAkiraAuctVo> statAkiraAuctVos = new ArrayList <> ( );
                List <StatAkiraAuct> statAkiraAucts = new ArrayList <> ( );
                //获取声优列表(包括total_auct_money total_auct_count )
                statAkiraAuctVos = auctionDealService.getAkiraAucts ( );
                LOGGER.info ( "==========获取声优列表 statAkiraAucts==========" );
                for ( int i = 0 ; i < statAkiraAuctVos.size ( ) ; i++ ) {
                        LOGGER.info ( statAkiraAuctVos.get ( i ).getAuct_uid ( ) );
                        LOGGER.info ( statAkiraAuctVos.get ( i ).getTotalAuctMoney ( ) );
                        LOGGER.info ( statAkiraAuctVos.get ( i ).getTotalAuctCount ( ) );
                }
                //遍历添加AkiraAuctId
                for ( int i = 0 ; i < statAkiraAuctVos.size ( ) ; i++ ) {
                        StatAkiraAuct statAkiraAuct = new StatAkiraAuct ( );
                        Long akiraAuctId = Long.parseLong ( CommonUtil.getRandomNumStr ( 10 ) );
                        statAkiraAuct.setAkiraAuctId ( akiraAuctId );
                        statAkiraAuct.setUid ( statAkiraAuctVos.get ( i ).getAuct_uid ( ) );
                        statAkiraAuct.setTotalAuctCount ( statAkiraAuctVos.get ( i ).getTotalAuctCount ( ) );
                        statAkiraAuct.setTotalAuctMoney ( statAkiraAuctVos.get ( i ).getTotalAuctMoney ( ) );
                        statAkiraAucts.add ( statAkiraAuct );
                }
                statAkiraAuctService.insertList ( statAkiraAucts );
                 /*
                         更新礼物发送统计表
                 */
                LOGGER.info ( "----------更新礼物发送统计表----------" );
                List<GiftSendRecordVo> giftSendRecordVos = giftSendRecordService.getGiftSendRecordVoList ();
                List<StatGiftSend> statGiftSends = new ArrayList <> (  );
                for( int i=0;i<giftSendRecordVos.size ();i++){
                        LOGGER.info ( "==========getGiftId :" +giftSendRecordVos.get ( i ).getGiftId ()+"==========");
                        LOGGER.info ( "==========getSendCount :" +giftSendRecordVos.get ( i ).getSendCount ()+"==========");
                }
                for ( int i = 0 ; i <giftSendRecordVos.size ()  ; i++ ) {
                        StatGiftSend statGiftSend = new StatGiftSend ( );
                        Long giftSendId = Long.parseLong ( CommonUtil.getRandomNumStr ( 10 ) );
                        statGiftSend.setGiftSendId (giftSendId);
                        statGiftSend.setGiftId ( giftSendRecordVos.get ( i ).getGiftId () );
                        statGiftSend.setSendCount ( giftSendRecordVos.get ( i ).getSendCount () );
                        statGiftSends.add ( statGiftSend );
                }
                statGiftSendService.insertList ( statGiftSends );
        }

        public static void main ( String[] args ) {

                String num = CommonUtil.getRandomNumStr ( 10 );
                System.out.println ( Long.parseLong ( num ) );

        }
}
