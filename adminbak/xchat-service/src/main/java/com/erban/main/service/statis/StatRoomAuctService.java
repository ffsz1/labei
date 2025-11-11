package com.erban.main.service.statis;

import com.erban.main.model.StatRoomAuct;
import com.erban.main.mybatismapper.StatRoomAuctMapper;
import com.erban.main.mybatismapper.StatRoomAuctMapperExpand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 北岭山下 on 2017/8/7.
 */
@Service
public class StatRoomAuctService {


        @Autowired
        StatRoomAuctMapper statRoomAuctMapper;
        @Autowired
        StatRoomAuctMapperExpand statRoomAuctMapperExpand;
        //插入一条记录
        public void insert(StatRoomAuct statRoomAuct){
                Calendar calendar = Calendar.getInstance ();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                statRoomAuct.setCreateTime ( new Date (  ) );
                statRoomAuctMapper.insert ( statRoomAuct );
        }
        //批量插入
        public void insertList( List<StatRoomAuct> statRoomAuctList){

                java.sql.Date date =  new java.sql.Date ( System.currentTimeMillis () );
                for ( int i = 0;i<statRoomAuctList.size ();i++ ){
                        statRoomAuctList.get ( i).setStatDate (date);
                        statRoomAuctList.get ( i).setCreateTime ( new Date (  ) );
                }
                statRoomAuctMapperExpand.insertList ( statRoomAuctList );
        }


}
