package com.erban.main.service.statis;

import com.erban.main.model.StatAkiraAuct;
import com.erban.main.mybatismapper.StatAkiraAuctMapperExpand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by 北岭山下 on 2017/8/7.
 */
@Service
public class StatAkiraAuctService {


        @Autowired
        StatAkiraAuctMapperExpand statAkiraAuctMapperExpand;
        public void insertList( List<StatAkiraAuct> statAkiraAuctList ){

                java.sql.Date date = new java.sql.Date ( System.currentTimeMillis () );
                for(int i=0;i < statAkiraAuctList.size ();i++){
                        statAkiraAuctList.get ( i ).setStatDate ( date );
                        statAkiraAuctList.get ( i ).setCreateTime ( new Date (  ) );
                }
                statAkiraAuctMapperExpand.insertList ( statAkiraAuctList );

        }

}
