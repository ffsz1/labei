package com.juxiao.xchat.service.record.bill.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.bill.BillGiftGiveDao;
import com.juxiao.xchat.dao.bill.BillGoldChargeDao;
import com.juxiao.xchat.dao.bill.BillTransferDao;
import com.juxiao.xchat.dao.bill.dto.*;
import com.juxiao.xchat.dao.bill.query.BillUserQuery;
import com.juxiao.xchat.dao.bill.BillUserGiveRecordDao;
import com.juxiao.xchat.service.record.bill.BillRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 账单记录查询
 *
 * @class: BillRecordService.java
 * @author: chenjunsheng
 * @date 2018/5/16
 */
@Service
@Slf4j
public class BillRecordServiceImpl implements BillRecordService {
    @Autowired
    private BillGiftGiveDao giftGiveDao;
    @Autowired
    private BillGoldChargeDao goldChargeDao;
    @Autowired
    private BillTransferDao transferDao;
    @Autowired
    private BillUserGiveRecordDao billUserGiveRecordDao;

    /**
     * @see BillRecordService#listBillRecord(Long, Byte, Long, Integer, Integer)
     */
    @Override
    public List<JSONObject> listBillRecord(Long uid, Byte type, Long date, Integer pageNo, Integer pageSize) throws WebServiceException {
        if (uid == null || type == null || date == null || pageNo == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        if (pageSize == null || pageSize == 0)
            pageSize = 20;
        List<JSONObject> list;
        Date qdate = DateTimeUtils.addDay(new Date(date), 1);
        BillUserQuery query = new BillUserQuery(uid, qdate, (pageNo - 1) * pageSize, pageSize);
        log.info("[ 账单记录 ] uid:>{} , type:{} , date:>{} , startRecord:>{} , pageSize:>{}", uid, type, qdate, (pageNo - 1) * pageSize, pageSize);
        switch (type) {
            case 1:
                List<BillUserGiftDTO> payGifts = giftGiveDao.listUserGiftPay(query);
                list = this.transfer(payGifts);
                break;
            case 2:
                List<BillUserGiftDTO> incomeGifts = giftGiveDao.listUserGiftIncome(query);
                list = this.transfer(incomeGifts);
                break;
            case 4:
                List<BillGoldUserChargeDTO> goldCharges = goldChargeDao.listUserCharge(query);
                list = this.transfer(goldCharges);
                break;
            case 5:
                List<BillUserTransferDTO> transfers = transferDao.listUserTransfer(query);
                list = this.transfer(transfers);
                break;
            case 6:
                List<BillUserGiveGoldDTO> giveGoldS = billUserGiveRecordDao.listUserGiveGold(query);
                list = this.transfer(giveGoldS);
                break;
            default:
                throw new WebServiceException("不支持的记录类型");
        }

        return list;
    }

    /**
     * 转化成客户端需要的结果
     *
     * @param list
     * @return
     * @author: chenjunsheng
     * @date 2018/5/16
     */
    private List<JSONObject> transfer(List<? extends BaseUserBillDTO> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        Map<Long, JSONArray> map = new LinkedHashMap<>();
        for (BaseUserBillDTO payDto : list) {
            JSONArray array = map.get(payDto.getDate());
            if (array == null) {
                array = new JSONArray();
            }
            array.add(payDto);
            map.put(payDto.getDate(), array);
        }

        Set<Map.Entry<Long, JSONArray>> set = map.entrySet();
        Iterator<Map.Entry<Long, JSONArray>> iterator = set.iterator();
        List<JSONObject> result = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Long, JSONArray> entry = iterator.next();
            JSONObject unit = new JSONObject();
            unit.put(entry.getKey() + "", entry.getValue());
            result.add(unit);
        }
        return result;
    }


}
