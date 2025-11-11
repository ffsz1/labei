package com.erban.web.controller;

import com.erban.main.service.record.PersonBillRecordService;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/personbill")
public class PersonBillRecordController {

    private static final Logger logger = LoggerFactory.getLogger(PersonBillRecordController.class);

    @Autowired
    private PersonBillRecordService personBillRecordService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
//    @Authorization
    public BusiResult getExpendRecordListPage(Long uid, Integer pageSize, Integer pageNo, Long time, Byte type) {
        BusiResult busiResult = null;
        if (uid == null) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        try {
            busiResult = personBillRecordService.getBillRecordAllList(uid, pageSize, pageNo, time, type);
        } catch (Exception e) {
            logger.error("getExpendRecordListPage error, uid: " + uid + ", time: " + time + ", type: " + type, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @RequestMapping(value = "/redpacket", method = RequestMethod.GET)
    @ResponseBody
    @Authorization
    public BusiResult getRedPacketRecord(Long uid, Integer pageNo, Integer pageSize, Long time) {
        BusiResult busiResult = null;
        if (uid == null) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        try {
            busiResult = personBillRecordService.getRedPacketRecord(uid, pageNo, pageSize, time);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

}
