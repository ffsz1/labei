package com.erban.main.service;

/**
 * Created by 北岭山下 on 2017/8/1.
 */

import com.erban.main.model.WXMsgPicInfo;
import com.erban.main.model.WXMsgPicInfoExample;
import com.erban.main.model.WXPush;
import com.erban.main.model.WXPushExample;
import com.erban.main.mybatismapper.WXMsgPicInfoMapper;
import com.erban.main.mybatismapper.WXPushMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *      微信公众号推送
 */
@Service
public class WXPushService {

        @Autowired
        WXPushMapper wxPushMapper;
        @Autowired
        WXMsgPicInfoMapper wxMsgPicInfoMapper;

        /*
                图文
         */
        //创建一条图文
        public void insertPicInfoMsg ( WXMsgPicInfo wxMsgPicInfo ) {

                wxMsgPicInfo.setCreateTime ( new Date ( ) );
                wxMsgPicInfoMapper.insert ( wxMsgPicInfo );

        }

        public WXMsgPicInfo selectPicInfoMsgById ( String id ) {
                return wxMsgPicInfoMapper.selectByPrimaryKey ( id );
        }

        //更改一条图文
        public void updatePicInfoMsg ( WXMsgPicInfo wxMsgPicInfo ) {
                wxMsgPicInfo.setUpdateTime ( new Date ( ) );
                wxMsgPicInfoMapper.updateByPrimaryKey ( wxMsgPicInfo );
        }

        public void deletePicInfoMsgById ( String msgId ) {
                wxMsgPicInfoMapper.deleteByPrimaryKey ( msgId );
        }

        /**
         *
         * @param ids id串 001&002&003
         * @return
         */
        public List<WXMsgPicInfo> getPicInfoMsgByIds(String ids){

                String [] idList = ids.split ( "&" );
                WXMsgPicInfoExample wxMsgPicInfoExample = new WXMsgPicInfoExample ();
                List<String > list = Lists.newArrayList () ;
                for ( int i = 0;i<idList.length;i++ ){
                        list.add ( idList[i] );
                }
                wxMsgPicInfoExample.createCriteria ().andWxMsgIdIn ( list );
                List<WXMsgPicInfo> wxMsgPicInfoList = wxMsgPicInfoMapper.selectByExample ( wxMsgPicInfoExample );
                return wxMsgPicInfoList;
        }
        /*
                        推送
        */
        //创建一条推送
        public void insertPubMsg ( WXPush wxPush ) {

                wxPush.setCreateTime ( new Date ( ) );
                wxPushMapper.insert ( wxPush );
        }


        //更改一条推送
        public void updatePubMsg ( WXPush wxPush ) {
                wxPushMapper.updateByPrimaryKey ( wxPush );
        }

        public void deletePubMsg ( String id ) {
                wxPushMapper.deleteByPrimaryKey ( id );
        }

        public WXPush selectPubMsgById ( String id ) {
                return wxPushMapper.selectByPrimaryKey ( id );
        }


        /*public WXPush pushOneFirstMsg ( ) {

                WXPush wxPush = selectPubMsgById ( "1" );
                return wxPush;
        }*/
        //查找订阅推送
        public WXPush getSubMsg ( ) {

                /*

                List<ChargeProd> chargeProdList = Lists.newArrayList();
		if (chargeProdKeyMap == null || chargeProdKeyMap.size() == 0) {
			ChargeProdExample chargeProdExample = new ChargeProdExample();
			chargeProdExample.setOrderByClause(" seq_no asc");
			chargeProdList = chargeProdMapper.selectByExample(chargeProdExample);
			batchSaveChargeProdCache(chargeProdList);
                 */
                WXPushExample wxPushExample = new WXPushExample ( );
                List <WXPush> list = Lists.newArrayList ( );
                wxPushExample.createCriteria ( ).andIsFocusPushEqualTo ( true);
                list = wxPushMapper.selectByExample ( wxPushExample );
                return list.get ( 0 );


        }
}
