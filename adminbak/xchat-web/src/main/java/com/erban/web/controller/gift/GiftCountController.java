package com.erban.web.controller.gift;

import com.erban.main.service.gift.GiftCountService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 北岭山下 on 2017/8/5.
 */
/*
                礼物统计
                1. 礼物维度
                        统计所有订单中所有礼物送出的总数
                        统计所有订单中各种礼物送的的数量（-）
               2. 直播间维度
                       统计某直播间中送出的礼物总数
                       在账单中统计是送礼物获得的（obj_type = 6）的数量


 */
@Controller
@RequestMapping ( "/giftCount" )
public class GiftCountController {

        private static final Logger LOGGER = Logger.getLogger ( GiftCountController.class );

        @Autowired
        GiftCountService giftCountService;

        /*
                以礼物为单位，统计所有订单中送出的礼物总数
         */
        @RequestMapping ( "/byGift/giftSum" )
        @ResponseBody
        public BusiResult getGiftSumByGift ( ) {
                BusiResult busiResult = new BusiResult ( BusiStatus.SUCCESS );
                try {
                        Long giftSum = giftCountService.getGiftSumByGift ( );
                        LOGGER.info ( "============统计所用订单中送出礼物的总数："+giftSum );
                        busiResult.setData ( giftSum );
                } catch ( Exception e ) {
                        LOGGER.info ( "=============统计订单中各种礼物总数失败==========" );
                        e.printStackTrace ( );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
                return busiResult;
        }
        /*
               以礼物为单位， 统计所有订单中各种礼物送的的数量
        */
        /*@RequestMapping ( "/byGift/giftType" )
        @ResponseBody
        public BusiResult getGiftTySumByGift ( ) {
              BusiResult busiResult = new BusiResult ( BusiStatus.SUCCESS );
                try {
                        List<Map<String,String >> mapList= giftCountService.getGiftTypeSumByRoom ();
                        busiResult.setData ( mapList );
                } catch ( Exception e ) {
                        LOGGER.info ( "=============统计订单中礼物总数失败==========" );
                        e.printStackTrace ( );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
                return null;
        }*/

        /**
         *
         * @return
         */
        @RequestMapping ( "/byRoom" )
        @ResponseBody
        public BusiResult getGiftSumByRoom ( ) {
                BusiResult busiResult = new BusiResult ( BusiStatus.SUCCESS );
                try{
                        Long sum = giftCountService.getGiftSumByRoom();
                        busiResult.setData ( sum );
                        LOGGER.info ( "=============统计直播间中发送的礼物数："+sum );
                }catch ( Exception e ){
                        LOGGER.error ( "=============获取统计直播间中发送的礼物数失败==========" );
                        e.printStackTrace ( );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
                return busiResult;
        }

}
