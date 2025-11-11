package com.erban.main.service;

import com.erban.main.model.ChargeProd;
import com.erban.main.mybatismapper.ChargeRecordMapper;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.RecordIdVo;
import com.erban.main.vo.UserVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by 北岭山下 on 2017/7/14.
 */
@Service
public class IOSPayPlaService {

        private static final Logger logger = LoggerFactory.getLogger(IOSPayPlaService.class);

        @Autowired
        private ChargeProdService chargeProdService;

        @Autowired
        private ChargeRecordMapper chargeRecordMapper;


        @Autowired
        private UsersService usersService;

        public BusiResult<RecordIdVo> placeOrder(Long uid, String chargeProdId,String clientIp)
        {


                //==============判断用户是否存在======================//
                UserVo userVo = usersService.getUserByUid ( uid ).getData ();
                if(userVo == null){
                        logger.debug ( "===========用户不存在==================" );
                        return new BusiResult(BusiStatus.USERNOTEXISTS);
                }
                /*
                Users是否存在未支付订单
                 */
                ChargeProd chargeProd = chargeProdService.getChargeProdById ( chargeProdId );
                if(chargeProd == null){
                        logger.debug ( "===========订单类型不存在==================" );
                        return new BusiResult(BusiStatus.BUSIERROR);
                }
                //保存充值记录
                //1.创建订单号
                String chargeRecordId = UUIDUitl.get ( );   //UUID不会重复，所以不需要判断是否生成重复的订单号
                com.erban.main.model.ChargeRecord chargeRecord = new com.erban.main.model.ChargeRecord ( );
                chargeRecord.setChargeRecordId ( chargeRecordId );
                chargeRecord.setChargeProdId ( chargeProdId );
                chargeRecord.setUid ( uid );
                chargeRecord.setChannel ( Constant.ChargeChannel.ios_pay );
                chargeRecord.setChargeStatus ( Constant.ChargeRecordStatus.create );
                Long money = chargeProd.getMoney ( );
                Long amount = money * 100;
                chargeRecord.setAmount ( amount );
                String body = chargeProd.getProdName ( ) + "金币充值";
                String subject = chargeProd.getProdName ( ) + "金币充值";
                chargeRecord.setSubject ( subject );
                chargeRecord.setBody ( body );
                chargeRecord.setClientIp ( clientIp );

                BusiResult <RecordIdVo> busiResult = new BusiResult ( BusiStatus.SUCCESS );
                try {
                        //写入数据库
                        insertChargeRecord ( chargeRecord );
                        //订单创建成功返回订单号
                        busiResult.setMessage ( "success" );
                        RecordIdVo recordIdVo = new RecordIdVo ( );
                        recordIdVo.setRecordId ( chargeRecordId );
                        busiResult.setData ( recordIdVo );


                } catch ( Exception e ) {

                        logger.error ( "applyCharge Exception:" + e.getMessage ( ) + "  uid=" + uid + ",chargeProdId=" + chargeProdId );
                        busiResult.setCode ( BusiStatus.BUSIERROR.value ( ) );
                        busiResult.setMessage ( "BusiError" );
                        return busiResult;
                }
                return busiResult;
        }




        private void insertChargeRecord( com.erban.main.model.ChargeRecord chargeRecord){
                chargeRecord.setCreateTime(new Date ());
                chargeRecordMapper.insertSelective(chargeRecord);
        }
}
