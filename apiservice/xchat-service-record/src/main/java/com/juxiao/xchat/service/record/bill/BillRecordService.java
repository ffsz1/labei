package com.juxiao.xchat.service.record.bill;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.web.WebServiceException;

import java.util.List;

/**
 * 账单记录查询
 *
 * @class: BillRecordService.java
 * @author: chenjunsheng
 * @date 2018/5/16
 */
public interface BillRecordService {
    /**
     * @param uid
     * @param type
     * @param date
     * @param pageNo
     * @param pageSize
     * @return
     * @author: chenjunsheng
     * @date 2018/5/16
     * 账单记录查询
     */
    List<JSONObject> listBillRecord(Long uid, Byte type, Long date, Integer pageNo, Integer pageSize) throws WebServiceException;
}
