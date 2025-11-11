package com.erban.web.controller.statis;

import com.erban.main.model.StatAkiraAuct;
import com.erban.main.model.StatGiftSend;
import com.erban.main.model.StatRoomAuct;
import com.erban.main.service.statis.StatisListService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by 北岭山下 on 2017/8/8.
 */
@Controller
@RequestMapping ( "/statList" )
public class StatisListController {


        private static final Logger LOGGER = Logger.getLogger ( StatisListController.class );

        @Autowired
        StatisListService statisListService;

        /**
         *      获取前一天的
         * @return
         */
        @RequestMapping ( "/room" )
        @ResponseBody
        public BusiResult getStatRoomAuct ( ) {

                try {
                        List <StatRoomAuct> statRoomAuctList = statisListService.getStatRoomAuctList ( );
                        BusiResult busiResult = new BusiResult ( BusiStatus.SUCCESS );
                        busiResult.setData ( statRoomAuctList );
                        return busiResult;
                } catch ( Exception e ) {
                        LOGGER.info ( "##########获取stat_auct_room表信息失败#########" );
                        e.printStackTrace ( );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
        }

        /**
         *      stat_akira_auct
         * @return
         */
        @RequestMapping ( "/akira" )
        @ResponseBody
        public BusiResult getStatAkiraAuct ( ) {
                try {
                        List <StatAkiraAuct> statAkiraAuctList = statisListService.getStatAkiraAuctList ( );
                        BusiResult busiResult = new BusiResult ( BusiStatus.SUCCESS );
                        busiResult.setData ( statAkiraAuctList );
                        return busiResult;
                } catch ( Exception e ) {
                        LOGGER.info ( "##########获取stat_akira_auct表信息失败#########" );
                        e.printStackTrace ( );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
        }
        /**
         *     stat_gift_send
         * @return
         */
        @RequestMapping ( "/giftSend" )
        @ResponseBody
        public BusiResult getGiftSendAuct ( ) {
                try {
                        List <StatGiftSend> statGiftSends = statisListService.getStatGiftSendList ( );
                        BusiResult busiResult = new BusiResult ( BusiStatus.SUCCESS );
                        busiResult.setData ( statGiftSends );
                        return busiResult;
                } catch ( Exception e ) {
                        LOGGER.info ( "##########获取stat_gift_send表信息失败#########" );
                        e.printStackTrace ( );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
        }


}
