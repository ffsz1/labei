package com.erban.main.service.record;

import com.erban.main.mybatismapper.*;
import com.erban.main.vo.BillRecordExpandVo;
import com.erban.main.vo.BillSearchVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.constant.Constant;
import com.xchat.common.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BillRecord2Service {

    public static final Logger logger = LoggerFactory.getLogger(BillRecord2Service.class);

    @Autowired
    private BillRecordMapperExpand billRecordMapperExpand;

    public List<BillRecordExpandVo> getBillRecord(long uid, byte type, long date, int page, int size) {
        Date nextDate = DateTimeUtil.getNextDay(new Date(date), 1);
        List<Byte> status = Lists.newArrayList();
        Map<String, Object> param = Maps.newHashMap();
        param.put("uid", uid);
        param.put("status", status);
        param.put("date", nextDate);
        param.put("skip", (page - 1) * size);
        param.put("size", size);
        switch (type) {
            case 1:     //礼物支出记录
                status.add(Constant.BillType.giftPay);
                // 返回结果带有礼物信息
                return billRecordMapperExpand.getRecordByStatusAndDate2(param);
            case 2:     //礼物收入记录
                status.add(Constant.BillType.giftIncome);
                status.add(Constant.BillType.bonusPerDayRecv);
                // 返回结果带有礼物信息
                return converBonusPerDay(billRecordMapperExpand.getRecordByStatusAndDate2(param));
            case 3:     //密聊记录
                status.add(Constant.BillType.orderPay);
                status.add(Constant.BillType.orderIncome);
                status.add(Constant.BillType.roomOwnerIncome);
                return billRecordMapperExpand.getRecordByStatusAndDate(param);
            case 4:     //充值记录
                status.add(Constant.BillType.charge);
                status.add(Constant.BillType.redeemCode);
                status.add(Constant.BillType.clockResult);
                status.add(Constant.BillType.draw);
                status.add(Constant.BillType.exchangeDimondToGoldIncome);
                status.add(Constant.BillType.roomNoble);
                status.add(Constant.BillType.questionnaireDraw);
                return billRecordMapperExpand.getRecordByStatusAndDate(param);
            case 5:     //提现记录
                status.add(Constant.BillType.getCash);
                return billRecordMapperExpand.getRecordByStatusAndDate(param);
        }
        return Lists.newArrayList();
    }

    public List<LinkedHashMap<Long, List<BillSearchVo>>> transToSearchVo(List<BillRecordExpandVo> list, byte type) {
        LinkedHashMap<Long, List<BillSearchVo>> voMap = Maps.newLinkedHashMap();
        for (BillRecordExpandVo record : list) {
            try {
                Date date1 = DateTimeUtil.getDate(record.getCreateTime());
                List<BillSearchVo> voList = voMap.get(date1.getTime());
                if (voList == null) {
                    voList = Lists.newArrayList();
                    voMap.put(date1.getTime(), voList);
                }
                BillSearchVo searchVo = new BillSearchVo();
                searchVo.setDate(date1.getTime());
                searchVo.setRecordTime(record.getCreateTime().getTime());
                searchVo.setDiamondNum(record.getDiamondNum());
                searchVo.setGiftNum(record.getGiftNum());
                searchVo.setMoney(record.getMoney());
                searchVo.setGoldNum(record.getGoldNum());
                if(type == 4){
                    // 历史原因，多个渠道充值，某些渠道没有把金额写到money字段
                    // 金币与金钱的比例是10：1，但某些渠道充值后写入时比例为100：1
                    if (record.getMoney() == null || record.getMoney() == 0) {
                        record.setMoney(record.getGoldNum()/10);
                    } else if(record.getMoney() * 100 == record.getGoldNum()){
                        searchVo.setGoldNum(record.getGoldNum()/10);
                    }

                    if (record.getObjType() == Constant.BillType.charge) {
                        searchVo.setShowStr("-"+ record.getMoney() + "元");
                    } else if (record.getObjType() == Constant.BillType.redeemCode) {
                        searchVo.setShowStr("兑换码");
                    } else if (record.getObjType() == Constant.BillType.draw) {
                        searchVo.setShowStr("中奖");
                    } else if (record.getObjType() == Constant.BillType.clockResult) {
                        searchVo.setShowStr("打卡结算");
                    } else if (record.getObjType() == Constant.BillType.exchangeDimondToGoldIncome) {
                        searchVo.setShowStr("钻石兑换");
                    } else if (record.getObjType() == Constant.BillType.roomNoble) {
                        searchVo.setShowStr("贵族分成");
                    } else if (record.getObjType() == Constant.BillType.questionnaireDraw) {
                        searchVo.setShowStr("问卷礼包");
                    }
                }
                searchVo.setSrcAvatar(record.getSrcAvatar());
                searchVo.setSrcNick(record.getSrcNick());
                searchVo.setTargetAvatar(record.getTargetAvatar());
                searchVo.setTargetNick(record.getTargetNick());
                searchVo.setGiftPict(record.getGiftPict());
                searchVo.setGiftName(record.getGiftName());

                voList.add(searchVo);
            } catch (Exception e) {
                logger.error("transToSearchVo error", e);
            }
        }

        List<LinkedHashMap<Long, List<BillSearchVo>>> reList = Lists.newArrayList();
        Set<Long> keySet = voMap.keySet();
        for (Long key : keySet) {
            LinkedHashMap<Long, List<BillSearchVo>> map = Maps.newLinkedHashMap();
            map.put(key, voMap.get(key));
            reList.add(map);
        }
        return reList;
    }


    private List<BillRecordExpandVo> converBonusPerDay(List<BillRecordExpandVo> list) {
        for (BillRecordExpandVo vo : list) {
            if(vo.getObjType().equals(Constant.BillType.bonusPerDayRecv)){
                vo.setTargetNick(GlobalConfig.appName + "官方");
                vo.setGiftName("回馈钻石");
                vo.setGiftPict("http://res.91fb.com/logo.png");
            }
        }
        return list;
    }
}
