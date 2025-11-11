package com.erban.main.service.record;

import com.erban.main.model.BillRecord;
import com.erban.main.model.BillRecordExample;
import com.erban.main.mybatismapper.BillRecordMapper;
import com.erban.main.mybatismapper.BillRecordMapperExpand;
import com.google.common.collect.Lists;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.utils.GetTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by liuguofu on 2017/7/3.
 */
@Service
public class BillRecordService {
    @Autowired
    private BillRecordMapper billRecordMapper;
    @Autowired
    private BillRecordMapperExpand billRecordMapperExpand;

    /**
     * @param uid        账单主体UID
     * @param targetUid  目标用户UID
     * @param objId      账单对象id
     * @param objType    账单对象类型
     * @param diamondNum 消费钻石数量，正数表示收入，负数表示支出
     * @param goldNum    消费金币数量   正数表示收入，负数表示支出
     * @param money      消费人民币数量  正数表示提现，负数表示充值
     */
//    @Transactional(rollbackFor = Exception.class)
    public void insertBillRecord(Long uid, Long targetUid, String objId, Byte objType, Double diamondNum, Long goldNum, Long money) {
        Date date = new Date();
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setTargetUid(targetUid);
        billRecord.setObjId(objId);
        billRecord.setObjType(objType);
        billRecord.setDiamondNum(diamondNum);
        billRecord.setGoldNum(goldNum);
        billRecord.setMoney(money);
        billRecord.setCreateTime(date);
        billRecordMapper.insertSelective(billRecord);
    }

    public int insertBillRecord(BillRecord billRecord) {
        return billRecordMapper.insertSelective(billRecord);
    }

//    @Transactional(rollbackFor = Exception.class)
    public void insertBillRecordBySendGift(Long uid, Long targetUid, Long roomUid,String objId, Byte objType, Double diamondNum, Long goldNum, Long money, Integer giftNum, Integer giftId) {
        Date date = new Date();
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setTargetUid(targetUid);
        billRecord.setRoomUid(roomUid);
        billRecord.setObjId(objId);
        billRecord.setObjType(objType);
        billRecord.setDiamondNum(diamondNum);
        billRecord.setGoldNum(goldNum);
        billRecord.setMoney(money);
        billRecord.setGiftNum(giftNum);
        billRecord.setCreateTime(date);
        billRecord.setGiftId(giftId);
        billRecordMapper.insertSelective(billRecord);
    }


    @Transactional(rollbackFor = Exception.class)
    public void insertBillRecordByWithDraw(Long uid, Long targetUid, Byte billStatus, String objId, Byte objType, Double diamondNum, Long goldNum, Long money) {
        Date date = new Date();
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setTargetUid(targetUid);
        billRecord.setObjId(objId);
        billRecord.setObjType(objType);
        billRecord.setBillStatus(billStatus);
        billRecord.setDiamondNum(diamondNum);
        billRecord.setGoldNum(goldNum);
        billRecord.setMoney(money);
        billRecord.setCreateTime(date);
        billRecordMapper.insertSelective(billRecord);
    }

    public List<BillRecord> selectBillRecord(Long uid) {
        BillRecordExample example = new BillRecordExample();
        example.createCriteria().andObjTypeEqualTo(Constant.BillType.getCash).andCreateTimeBetween(GetTimeUtils.getTimesWeekmorning(), GetTimeUtils.getTimesnights(GetTimeUtils.getTimesWeeknight(),24)).andUidEqualTo(uid);
        List<BillRecord> billRecordList = billRecordMapper.selectByExample(example);
        return billRecordList;
    }

    public BillRecord selectBillRecordByBillId(String billId) {
        BillRecord billRecord = billRecordMapper.selectByPrimaryKey(billId);
        if (billRecord == null) {
            return null;
        }
        return billRecord;
    }

    public void updateBillRecordByWithDraw(BillRecord billRecord) {
        if (billRecord == null) {
            return;
        }
        billRecordMapper.updateByPrimaryKeySelective(billRecord);
    }

    public List<BillRecord> getGiftSendRecordToTable(Long uid) {
        BillRecordExample example = new BillRecordExample();
        example.createCriteria().andObjTypeEqualTo(Constant.BillType.giftIncome).andUidEqualTo(uid).andTargetUidEqualTo(uid);
        List<BillRecord> billRecordList = billRecordMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(billRecordList)) {
            return Lists.newArrayList();
        }
        return billRecordList;
    }

    public List<BillRecord> selectBillRecordList() {
        BillRecordExample example = new BillRecordExample();
        example.createCriteria().andObjTypeEqualTo(Constant.BillType.getCash).andBillStatusEqualTo(Constant.WithDraw.ing);
        List<BillRecord> billRecordList = billRecordMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(billRecordList)) {
            return null;
        }
        return billRecordList;
    }

    public List<BillRecord> selectBillRecordByErrorTargetId() {
        BillRecordExample example = new BillRecordExample();
        example.createCriteria().andObjTypeEqualTo(Constant.BillType.giftIncome);
        example.or().andObjTypeEqualTo(Constant.BillType.orderIncome);
        List<BillRecord> billRecordList = billRecordMapper.selectByExample(example);
        return billRecordList;
    }

    public void updatebillRecordByErrorTarget(BillRecord billRecord) {
        billRecordMapper.updateByPrimaryKeySelective(billRecord);
    }

    public List<BillRecord> getGainRecordList(Long uid, Date date) {
        List<Byte> list = Lists.newArrayList();
        list.add(Constant.BillType.giftIncome);
        list.add(Constant.BillType.orderIncome);
        BillRecordExample example = new BillRecordExample();
        example.createCriteria().andUidEqualTo(uid).andObjTypeIn(list).andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnights(date, 24));
        List<BillRecord> billRecordList = billRecordMapper.selectByExample(example);
        return billRecordList;
    }

    public List<BillRecord> getExpendRecordList(Long uid, Date date) {
        List<Byte> list = Lists.newArrayList();
        list.add(Constant.BillType.orderPay);
        list.add(Constant.BillType.giftPay);
        BillRecordExample example = new BillRecordExample();
        example.createCriteria().andUidEqualTo(uid).andObjTypeIn(list).andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnights(date, 24));
        List<BillRecord> billRecordList = billRecordMapper.selectByExample(example);
        return billRecordList;
    }

    public List<BillRecord> getShareChargeByMoreThanThree(Long uid) {
        Date date = new Date(System.currentTimeMillis() - 87400000);
        BillRecordExample example = new BillRecordExample();
        example.createCriteria().andUidEqualTo(uid).andObjTypeEqualTo(Constant.BillType.charge).andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnight(0));
        List<BillRecord> billRecordList = billRecordMapper.selectByExample(example);
        return billRecordList;
    }

    public List<BillRecord> getRecordAllList(Long uid, Integer pageNo, Integer pageSize, Byte type) {
        List<BillRecord> billRecordList = null;
        if (type.equals(Constant.BillType.orderPay)) {
            billRecordList = billRecordMapperExpand.getRecordAllListByOrder(uid, pageNo, pageSize, Constant.BillType.orderPay, Constant.BillType.orderIncome);
        } else {
            billRecordList = billRecordMapperExpand.getRecordAllList(uid, pageNo, pageSize, type);
        }
        return billRecordList;
    }

    public Integer getRecordCount(Long uid, Byte type) {
        Integer pageCount = null;
        if (type.equals(Constant.BillType.orderPay)) {
            pageCount = billRecordMapperExpand.getRecordCountByOrder(uid, Constant.BillType.orderPay, Constant.BillType.orderIncome);
        } else {
            pageCount = billRecordMapperExpand.getRecordCount(uid, type);
        }
        return pageCount;
    }

    public Integer getRecordCountByDate(Long uid, Date timesnights, Date timesnights1, Byte type) {
        Integer pageCountByDate = null;
        if (type.equals(Constant.BillType.orderPay)) {
            pageCountByDate = billRecordMapperExpand.getRecordCountByOrderDate(uid, timesnights, timesnights1, Constant.BillType.orderPay, Constant.BillType.orderIncome);
        } else {
            pageCountByDate = billRecordMapperExpand.getRecordCountByDate(uid, timesnights, timesnights1, type);
        }
        return pageCountByDate;
    }

    public List<BillRecord> getRecordByDateList(Long uid, Date timesnights, Date timesnights1, Integer pageNo, Integer pageSize, Byte type) {
        List<BillRecord> billRecordList = null;
        if (type.equals(Constant.BillType.orderPay)) {
            billRecordList = billRecordMapperExpand.getRecordByDateListByOrder(uid, timesnights, timesnights1, pageNo, pageSize, Constant.BillType.orderPay, Constant.BillType.orderIncome);
        } else {
            billRecordList = billRecordMapperExpand.getRecordByDateList(uid, timesnights, timesnights1, pageNo, pageSize, type);
        }
        return billRecordList;
    }

    public List<BillRecord> getGainRecordListBy() {
        BillRecordExample example = new BillRecordExample();
        example.createCriteria().andObjTypeEqualTo(Constant.BillType.giftIncome).andGiftIdIsNull();
        List<BillRecord> billRecordList = billRecordMapper.selectByExample(example);
        return billRecordList;
    }

    public List<BillRecord> getExpendRecordListBy() {
        BillRecordExample example = new BillRecordExample();
        example.createCriteria().andObjTypeEqualTo(Constant.BillType.giftPay).andGiftIdIsNull();
        List<BillRecord> billRecordList = billRecordMapper.selectByExample(example);
        return billRecordList;
    }

    public void updatebillRecordByGift(BillRecord billRecord) {
        billRecordMapper.updateByPrimaryKeySelective(billRecord);
    }
}
