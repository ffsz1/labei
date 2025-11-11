package com.erban.main.service.mq;

import com.erban.main.model.GiftSendRecord;
import com.erban.main.model.UserPurse;
import com.erban.main.service.*;
import com.erban.main.service.gift.GiftSendRecordService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.statis.StatRoomCtrbSumService;
import com.erban.main.service.statis.StatRoomCtrbSumTotalService;
import com.erban.main.service.statis.StatSendGiftSumListService;
import com.erban.main.service.statis.StatSendGiftWeekListService;
import com.erban.main.service.user.UserGiftWallService;
import com.erban.main.service.user.UserPurseService;
import com.xchat.common.constant.Constant;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;

public class MyMessageListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(MyMessageListener.class);

    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private SumListService sumListService;
    @Autowired
    private WeekListService weekListService;
    @Autowired
    private GiftSendRecordService giftSendRecordService;
    @Autowired
    private StatSendGiftWeekListService statSendGiftWeekListService;
    @Autowired
    private StatSendGiftSumListService statSendGiftSumListService;
    @Autowired
    private StatRoomCtrbSumService statRoomCtrbSumService;
    @Autowired
    private StatRoomCtrbSumTotalService statRoomCtrbSumTotalService;
    @Autowired
    private UserGiftWallService userGiftWallService;

    @Override
    public void onMessage(Message message) {
        /** 判断消息类型 */
        if (message instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) message;
            try {
                String title = (String) mapMessage.getObject("title");
                if (title.equals(Constant.ActiveMq.sum_list)) {
                    Long roomUid = (Long) mapMessage.getObject("roomUid");
                    String auctId = (String) mapMessage.getObject("auctId");
                    SumListAndWeekList(roomUid, auctId);
                } else if (title.equals(Constant.ActiveMq.send_gift)) {
                    Long uid = (Long) mapMessage.getObject("uid");
                    Long targetUid = (Long) mapMessage.getObject("targetUid");
                    Long roomUid=mapMessage.getObject("roomUid")==null?null:(Long) mapMessage.getObject("roomUid");
                    Byte roomType=(Byte) mapMessage.getObject("roomType")==null?null:(Byte) mapMessage.getObject("roomType");
                    Long goldNum = (Long) mapMessage.getObject("goldNum");
                    int type = (int) mapMessage.getObject("type");
                    Integer giftId = (Integer) mapMessage.getObject("giftId");
                    Integer giftNum = (Integer) mapMessage.getObject("giftNum");
                    Double diamondNum = (Double) mapMessage.getObject("diamondNum");
                    logger.info("uid:" + uid + "targetUid:" + targetUid + "giftId:" + giftId + "diamondNum:"
                            + diamondNum + "type" + type);
                    Long totalGoldNum=giftNum*goldNum;
                    if(totalGoldNum<0||diamondNum<0){
                        logger.error("刷礼物异常，金币或者钻石<0,uid=" +uid+"&goldNum="+goldNum+"&diamondNum="+diamondNum);
                        return;
                    }
                    if(!uid.equals(targetUid)){//一堆一刷礼物已经前置处理，不在此处处理
//                        userPurseService.updateMinusGoldNum(uid,totalGoldNum);//刷礼物人
                        userPurseService.updateDiamondByOrderIncome(targetUid, diamondNum);

                        GiftSendRecord giftSendRecord =giftSendRecordService.insertGiftInfo(uid, targetUid,roomUid,roomType, type, giftId, giftNum,totalGoldNum,diamondNum);
                        String objId=giftSendRecord.getSendRecordId().toString();
                        billRecordService.insertBillRecordBySendGift(uid, targetUid, roomUid,objId, Constant.BillType.giftPay, null, -goldNum, null, giftNum, giftId);
                        billRecordService.insertBillRecordBySendGift(targetUid, uid, roomUid,objId, Constant.BillType.giftIncome, diamondNum, null, null, giftNum, giftId);
                    }
                    if (type == Constant.SendGiftType.room||type == Constant.SendGiftType.roomToperson) {
                        statRoomCtrbSumService.addAndUpdateStatRoomCtrbSumList(roomUid,uid,totalGoldNum);
                        statRoomCtrbSumTotalService.addAndUpdateStatRoomCtrbSumTotalList(roomUid,totalGoldNum);
                    }
                    //更新个人礼物墙
                    userGiftWallService.updateGiftWallCount(targetUid,giftId,giftNum);
                }
            } catch (JMSException e) {
                logger.error("消息队列出现异常:" + e.getMessage(),e);
            } catch (Exception e) {
                logger.error("消息队列出现异常:" + e.getMessage(),e);
            }
        }
    }

    public static void main(String args[]){
        Integer  giftNum=1314;
        Long goldNum=66L;
        Long num=giftNum*goldNum;
        System.out.println(num);
        System.out.println(giftNum*goldNum);
    }

    private void SumListAndWeekList(Long roomUid, String auctId) throws Exception {
        sumListService.updateSumList(auctId, roomUid);
        weekListService.updateWeekList(roomUid, auctId);
    }
}
