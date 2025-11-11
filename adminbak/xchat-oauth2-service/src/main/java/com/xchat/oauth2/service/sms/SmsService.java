package com.xchat.oauth2.service.sms;

//import com.yy.ent.mobile.pool.thrift.ThriftClientManager;
//import com.xchat.oauth2.service.sms.thrift.MsgOper_Service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author sakyone
 *         on 2015-03-20.
 */
@Service
public class SmsService {

//    private static final Logger LOG = LoggerFactory.getLogger(SmsService.class);
//
//    private static final int APPID = 87;
//    private static final String APPKEY = "whistle-10086";
//
////    @Autowired
////    @Qualifier("smsThriftClientManager")
//    private ThriftClientManager<MsgOper_Service.Client> smsThriftClientPoolManager;
//
//    private String createMuid(String phone) {
//        return APPID + "_" + phone + "_" + System.currentTimeMillis()/1000;
//    }
//
//
//    public boolean sendMsg(String phone,String content){
//        LOG.debug("sendMsg invoked,param phone:{},content:{}",phone,content);
//        MsgOper_Service.Client client = null;
//        boolean flag = false;
//        try {
//            client = smsThriftClientPoolManager.borrowClient();
//            String muid = createMuid(phone);
//            LOG.info("client.send_msg param, appid:{} appkey:{} muid:{} phone:{} content:{}",APPID,APPKEY,muid,phone,content);
//            int resCode = client.send_msg(APPID,APPKEY,muid,phone,content);
//            LOG.info("client.send_msg resCode:{}",resCode);
//            flag = (resCode == 0);
//        } catch (Exception e) {
//            LOG.error("sendMsg error. phone:{},content:{},msg:{}", phone,content,e);
//        } finally {
//            smsThriftClientPoolManager.returnClient(client);
//        }
//        return flag;
//    }
}
