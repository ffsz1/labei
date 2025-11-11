package com.erban.web.controller;

import com.erban.main.service.ConsumeCountService;
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
        用于消费统计
               1. 消费率统计
                         统计用户钱包中user_purse所有已经消费(充值)过的用户次数占总数的百分比
                         (统计is_first_charge字段是否为一，并查询该总数占总用户数的比例)
                         select COUNT(is_first_charge) FROM user_purse where is_first_charge=1;
               2. 送礼物率统计
                        统计用户钱包中bill_record所有的用户ID占user_purse中用户总数的百分比
                        （统计bill_record中所有的用户（不重复），并查询user_purse中所有的用户，计算两者的比值）


 */
@Controller
@RequestMapping ( "/consumeCount" )
public class ConsumeCountController {

        private static final Logger LOGGER = Logger.getLogger ( ConsumeCountController.class );
        @Autowired
        private ConsumeCountService consumeCountService;

        /*
                消费率统计
         */
        @RequestMapping ( "/consume" )
        @ResponseBody
        public BusiResult getConsumePropor ( ) {

                BusiResult busiResult = new BusiResult ( BusiStatus.SUCCESS );
                try {
                        Long allNum = consumeCountService.getAllUserNum ( );
                        Long consumeNum = consumeCountService.getConsumeUserNum ( );
                        //保留两位小数
                        Double propor = ( int ) ( consumeNum / ( allNum + 0.0 ) * 100 ) / 100.0;
                        busiResult.setData ( propor );
                        LOGGER.info ( "======获取消费率统计成功：" + propor );
                } catch ( Exception e ) {
                        LOGGER.info ( "======获取消费率统计失败=========" );
                        e.printStackTrace ( );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
                return busiResult;


        }

        public static void main ( String[] args ) {
                Long allNum = 182L;
                Long consumeNum = 64L;
                //保留两位小数
                Double reuslt = ( int ) ( ( consumeNum / ( allNum + 0.0 ) * 100 ) ) / 100.0;
                System.out.println ( reuslt );
        }

        /*
                送礼物率统计
         */
        @RequestMapping ( "/gift" )
        @ResponseBody
        public BusiResult getGiftPropor ( ) {
                BusiResult busiResult = new BusiResult ( BusiStatus.SUCCESS );
                try {
                        Long allNum = consumeCountService.getAllUserNum ( );
                        Integer giveGifecNum = consumeCountService.getGiveGiftUserNum ( );
                        Double propor = ( int ) ( giveGifecNum / ( allNum + 0.0 ) * 100 ) / 100.0;
                        LOGGER.info ( "======送礼物率为：" + propor );
                        busiResult.setData ( propor );
                } catch ( Exception e ) {
                        LOGGER.info ( "======送礼物率获取失败===========" );
                        e.printStackTrace ( );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
                return busiResult;
        }


}
