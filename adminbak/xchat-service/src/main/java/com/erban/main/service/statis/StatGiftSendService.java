package com.erban.main.service.statis;

import com.erban.main.model.StatGiftSend;
import com.erban.main.mybatismapper.StatGiftSendMapperExpand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 北岭山下 on 2017/8/8.
 */
@Service
public class StatGiftSendService {



        @Autowired
        StatGiftSendMapperExpand statGiftSendMapperExpand;

        public void insertList( List<StatGiftSend> statGiftSends ){

                java.sql.Date date = new java.sql.Date ( System.currentTimeMillis () );
                for(int i=0;i < statGiftSends.size ();i++){
                        statGiftSends.get ( i ).setStatDate ( date );
                }
                statGiftSendMapperExpand.insertList ( statGiftSends );
        }

}
