package com.erban.main.service;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.AuctionDealMapper;
import com.erban.main.mybatismapper.AuctionDealMapperExpand;
import com.erban.main.param.neteasepush.Body;
import com.erban.main.param.neteasepush.NeteaseSendMsgBatchParam;
import com.erban.main.param.neteasepush.Payload;
import com.erban.main.service.record.AuctionRivalRecordService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.vo.OrderServVo;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguofu on 2017/5/25.
 */
@Service
public class AuctionDealService {
    private static final Logger logger = LoggerFactory.getLogger(AuctionDealService.class);

    @Autowired
    private AuctionDealMapper auctionDealMapper;

    @Autowired
    private OrderServService orderServService;

    @Autowired
    private AuctionRivalRecordService auctionRivalRecordService;

    @Autowired
    private UserPurseService userPurseService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private AuctionCurService auctionCurService;

    @Autowired
    private AuctionDealMapperExpand auctionDealMapperExpand;

    @Autowired
    private SendSysMsgService sendSysMsgService;

    private Gson gson = new Gson();

    public AuctionDeal saveAuctionDeal(AuctionCur auctionCur) throws Exception {
        List<AuctionRivalRecord> auctionRivalRecordList = auctionRivalRecordService.getRivalRecordList(auctionCur);
        AuctionRivalRecord dealAuctionRivalRecord = null;
        if (CollectionUtils.isEmpty(auctionRivalRecordList)) {
            logger.info("结束竞拍，生成订单，没有人参与竞拍，直接结束，uid=" + auctionCur.getUid());
            return null;
        } else {// 多人参与竞拍，退回除拍卖得主之外的人的保证金
            for (AuctionRivalRecord auctionRivalRecord : auctionRivalRecordList) {
                if (auctionCur.getCurMaxUid().equals(auctionRivalRecord.getUid())) {
                    dealAuctionRivalRecord = auctionRivalRecord;// 选出最高价者，即成交人
                    break;
                }
            }
            // 统一处理，退钱，删除保证金记录
            returnBackDeposit(auctionRivalRecordList);
        }
        Long maxDealGoldMoney = dealAuctionRivalRecord.getAuctMoney();
        Long uid = dealAuctionRivalRecord.getUid();
        // 更新价最高者钱包记录
        userPurseService.updateGoldByGenOrder(uid, maxDealGoldMoney);

        // 生成账单交易记录
        // Long uid,Long targetUid,String objId,Byte objType,Long
        // diamondNum,Long goldNum,Long money
        billRecordService.insertBillRecord(uid, auctionCur.getAuctUid(), auctionCur.getAuctId(),
                Constant.BillType.orderPay, null, -maxDealGoldMoney, null);

        // 生成成交记录，生成订单
        AuctionDeal auctionDeal = new AuctionDeal();
        auctionDeal.setAuctId(auctionCur.getAuctId());
        auctionDeal.setUid(auctionCur.getUid());
        auctionDeal.setAuctUid(auctionCur.getAuctUid());
        auctionDeal.setAuctMoney(auctionCur.getAuctMoney());
        auctionDeal.setServDura(auctionCur.getServDura());
        auctionDeal.setMinRaiseMoney(auctionCur.getMinRaiseMoney());
        auctionDeal.setDealMoney(auctionCur.getCurMaxMoney());
        auctionDeal.setDealUid(auctionCur.getCurMaxUid());
        auctionDeal.setAuctDesc(auctionCur.getAuctDesc());
        auctionDeal.setRoomId(auctionCur.getRoomId());
        auctionDeal.setCurStatus(Constant.AuctCurStatus.finish);
        Date date = new Date();
        auctionDeal.setCreateTime(date);
        auctionDeal.setDealTime(date);
        // 保存拍卖成交记录
        saveAuctionDeal(auctionDeal);
        // 生成订单
        OrderServ orderServ = orderServService.saveOrderServ(auctionDeal);
        OrderServVo orderServVo = orderServService.convertOrderServToVo(orderServ);
        Long remainDay = orderServVo.getCreateTime().getTime() + 86400000 - System.currentTimeMillis();
        if (remainDay > 0) {
            orderServVo.setRemainDay(remainDay);
        } else {
            orderServVo.setRemainDay(0L);
        }
        sendSysMsgByDealFinish(orderServVo, orderServ.getRoomOwnerUid());
        return auctionDeal;

    }

    private void sendSysMsgByDealFinish(OrderServVo orderServVo, Long roomUid) {
        List<String> list = Lists.newArrayList();
        list.add(orderServVo.getProdUid().toString());
        list.add(orderServVo.getUid().toString());
        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
        neteaseSendMsgBatchParam.setFromAccid(roomUid.toString());
        neteaseSendMsgBatchParam.setToAccids(list);
        neteaseSendMsgBatchParam.setType(100);
        neteaseSendMsgBatchParam.setPushcontent("你已成功拍下宝贝，请速速密聊去吧~~");
        Body body = new Body();
        body.setFirst(Constant.DefMsgType.DealFinish);
        body.setSecond(Constant.DefMsgType.DealFinishNotice);
        Map<String, String> data = Maps.newHashMap();
        String orderSerStr = gson.toJson(orderServVo);
        data.put("orderserv", orderSerStr);
        body.setData(data);
        neteaseSendMsgBatchParam.setBody(body);
        data.put("skiproute", String.valueOf(Constant.PayloadRoute.order));
        Payload payload = new Payload();
        payload.setSkiptype(Constant.PayloadSkiptype.apppage);
        payload.setData(data);
        neteaseSendMsgBatchParam.setPayload(payload);
        logger.info("发送订单拍卖成功通知：fromAccid:" + roomUid + ",toaccids:" + orderServVo.getUid() + "," + orderServVo.getProdUid());
        sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
    }

    /* 异常关闭房间后返回抵押金 */
    public boolean closeRoomReturnBackDeposit(Long uid) throws Exception {
        AuctionCur auctionCur = auctionCurService.getAuctionCurByUid(uid);
        logger.info("是否有未完成拍卖：" + auctionCur);
        if (auctionCur != null) {
            Byte status = auctionCur.getCurStatus();
            if (status == Constant.AuctCurStatus.ing || status == Constant.AuctCurStatus.error) {
                List<AuctionRivalRecord> auctionRivalRecordList = auctionRivalRecordService
                        .getRivalRecordList(auctionCur);
                if (CollectionUtils.isEmpty(auctionRivalRecordList)) {
                    logger.info("结束竞拍，生成订单，没有人参与竞拍，直接结束，uid=" + auctionCur.getUid());
                    return false;
                } else {
                    returnBackDeposit(auctionRivalRecordList);
                    return true;
                }
            }
        }
        return false;
    }

    private void returnBackDeposit(List<AuctionRivalRecord> auctionRivalRecordList) throws Exception {
        for (AuctionRivalRecord auctionRivalRecord : auctionRivalRecordList) {
            String objId = auctionRivalRecord.getAuctId();
            userPurseService.updateGoldByReturnBackDeposit(auctionRivalRecord.getUid(),
                    auctionRivalRecord.getAuctMoney());
            depositService.deleteDepositByObjId(objId, auctionRivalRecord.getUid());
        }
    }

    public AuctionDeal getAuctionDealById(String auctId) {
        AuctionDeal auctionDeal = auctionDealMapper.selectByPrimaryKey(auctId);
        return auctionDeal;
    }

    private void saveAuctionDeal(AuctionDeal auctionDeal) {
        auctionDealMapper.insert(auctionDeal);
    }

    public AuctionDeal getWeekListData(Long roomUid) {
        if (roomUid == null) {
            return null;
        }
        AuctionDealExample example = new AuctionDealExample();
        example.createCriteria().andDealUidEqualTo(roomUid);
        example.setOrderByClause("create_time desc");
        List<AuctionDeal> auctionDeals = auctionDealMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(auctionDeals)) {
            return null;
        }
        return auctionDeals.get(0);
    }

    //获取所有拍卖订单号列表
    public List<AuctionDeal> getAuctList() {
        return auctionDealMapperExpand.getAuctList();

    }

    //获取声优UID以及被拍卖次数和被拍卖总金额
    public List<StatAkiraAuctVo> getAkiraAucts() {
        return auctionDealMapperExpand.getAkiraAucts();
    }

    //获取房间房主的UID以及发起拍卖的次数和成交的金额
    public List<StatRoomAuctVo> getRoomAucts() {

        return auctionDealMapperExpand.getRoomAucts();
    }
}
