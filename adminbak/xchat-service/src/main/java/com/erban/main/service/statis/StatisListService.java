package com.erban.main.service.statis;

import com.erban.main.model.*;
import com.erban.main.mybatismapper.StatAkiraAuctMapper;
import com.erban.main.mybatismapper.StatGiftSendMapper;
import com.erban.main.mybatismapper.StatRoomAuctMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 北岭山下 on 2017/8/8.
 */
@Service
public class StatisListService {

        @Autowired
        StatRoomAuctMapper statRoomAuctMapper;
        @Autowired
        StatAkiraAuctMapper statAkiraAuctMapper;
        @Autowired
        StatGiftSendMapper statGiftSendMapper;

        public List<StatRoomAuct>  getStatRoomAuctList(){

                //获取房间拍卖成交统计表
                StatRoomAuctExample roomExample = new StatRoomAuctExample ();
                roomExample.createCriteria ().andRoomAuctIdIsNotNull ();
                List<StatRoomAuct> roomList = statRoomAuctMapper.selectByExample ( roomExample );
                return roomList;

        }
        public List<StatAkiraAuct>  getStatAkiraAuctList(){

                //获取房间拍卖成交统计表
                StatAkiraAuctExample statAkiraAuctExample = new StatAkiraAuctExample ();
                statAkiraAuctExample.createCriteria ().andAkiraAuctIdIsNotNull ();
                List<StatAkiraAuct> statAkiraAucts = statAkiraAuctMapper.selectByExample ( statAkiraAuctExample );
                return statAkiraAucts;

        }
        //stat_gift_send礼物发送统计表
        public List getStatGiftSendList(){

                //获取房间拍卖成交统计表
                StatGiftSendExample statGiftSendExample= new StatGiftSendExample ();
                statGiftSendExample.createCriteria ().andGiftIdIsNotNull ();
                List<StatGiftSend> statGiftSends = statGiftSendMapper.selectByExample ( statGiftSendExample );
                return statGiftSends;
        }


}
