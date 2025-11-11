package com.erban.web.controller;

import com.erban.main.service.AuctionCountService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by 北岭山下 on 2017/8/4.
 */
/*
                        拍卖数据统计
                        1. 拍卖金额统计
                                (弃用)统计一次拍卖中所有的叫价金额总数（统计acution_rival_record表中所有auct_money中总数）
                                统计该平台中所有拍卖金额（拍卖成交金额）总数
                        2. 拍卖次数统计
                                统计所有拍卖中所有的叫价次数（统计acution_rival_record表中所有订单总数）

 */



@Controller
@RequestMapping ( "/auctionCount" )
public class AuctionCountController {

        private static final Logger LOGGER = Logger.getLogger ( AuctionCountController.class );

        @Autowired
        AuctionCountService auctionCountService;

        /**
         *
         * @return 金额总数(统计该平台中所有拍卖金额（拍卖成交金额）总数)
         */
        @RequestMapping ( value = "/sum" )
        @ResponseBody
        public BusiResult getSum ( ) {
                try {
                        BusiResult busiResult = auctionCountService.getSumOrNumByAucId ("sum" );
                        LOGGER.info ( "==========sum:" + busiResult.getData ( ) +"==========");
                        return busiResult;
                } catch ( Exception e ) {
                        e.printStackTrace ( );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }

        }
        /**
         *
         * @return 拍卖次数（统计所有拍卖中所有的叫价次数）
         */
        @RequestMapping ( value = "/num" )
        @ResponseBody
        public BusiResult getNum ( ) {

                try {
                        BusiResult busiResult = auctionCountService.getSumOrNumByAucId ( "num" );
                        LOGGER.info ( "==========num:" + busiResult.getData ( )+"==========" );
                        return busiResult;
                } catch ( Exception e ) {
                        e.printStackTrace ( );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
        }





}


