package com.erban.main.service.gift;

import com.erban.main.mybatismapper.BillRecordMapperExpand;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 北岭山下 on 2017/8/5.
 */
@Service
public class GiftCountService {

        private static final Logger LOGGER = Logger.getLogger ( GiftCountService.class );

        @Autowired
        private BillRecordMapperExpand billRecordMapperExpand;

        public Long getGiftSumByGift ( ) {
                Long giftSum = billRecordMapperExpand.getGiftSumByGift ( );
                return giftSum;
        }

        public Long getGiftTypeSumByGift ( ) {
                return null;

        }

        public Long getGiftSumByRoom ( ) {
                Long sumByRoom = billRecordMapperExpand.getGiftTypeSumByRoom ( );
                return sumByRoom;

        }


}
