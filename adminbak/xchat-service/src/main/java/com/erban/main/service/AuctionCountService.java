package com.erban.main.service;

import com.erban.main.mybatismapper.AuctionRivalRecordMapperExpand;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 北岭山下 on 2017/8/4.
 */
@Service
public class AuctionCountService {

        //SQL语句拓展
        @Autowired
        private AuctionRivalRecordMapperExpand auctionRivalRecordMapperExpand;

        /*
                通过拍卖单获取该次拍卖的金额总数com/erban/main/service/AuctionCountService.java
         */
        public BusiResult getSumOrNumByAucId (String type ) {

                BusiResult busiResult = new BusiResult ( BusiStatus.SUCCESS );
                Integer sumOrNum;
                if ( type.equals ( "sum" ) ) {
                        //查询金额总数
                        sumOrNum = auctionRivalRecordMapperExpand.getSum (  );
                } else if ( type.equals ( "num" ) ) {
                        //查询叫价总数
                        sumOrNum = auctionRivalRecordMapperExpand.getNum (  );
                } else {
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
                busiResult.setData ( sumOrNum );
                return busiResult;

        }
        //总金额
        public Integer getTotalAuctMoney(){
                return auctionRivalRecordMapperExpand.getSum (  );
        }
        //总次数
        public Integer  getTotalAuctCount(){
                return auctionRivalRecordMapperExpand.getNum ();
        }



}
