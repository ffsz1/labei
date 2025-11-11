package com.erban.main.service;

import com.erban.main.mybatismapper.BillRecordMapperExpand;
import com.erban.main.mybatismapper.UserPurseMapperExpand;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 北岭山下 on 2017/8/5.
 */
@Service
public class ConsumeCountService {

        private static final Logger LOGGER = Logger.getLogger ( ConsumeCountService.class );

        @Autowired
        private BillRecordMapperExpand billRecordMapperExpand;
        @Autowired
        private UserPurseMapperExpand userPurseMapperExpand;
        public Long getAllUserNum(){
                Long userNum = userPurseMapperExpand.getAllUserNum ();
                LOGGER.info ( "======获取用户总数："+userNum );
                return userNum;

        }
        public Long getConsumeUserNum(){
                Long consumeNum = userPurseMapperExpand.getConsumeUserNum ();
                LOGGER.info ( "======获取用户消费总数："+consumeNum );
                return consumeNum;
        }
        public Integer getGiveGiftUserNum(){
                List<Long> giveGiftNum = billRecordMapperExpand.getUserNum();
                return giveGiftNum.size ();

        }


}
