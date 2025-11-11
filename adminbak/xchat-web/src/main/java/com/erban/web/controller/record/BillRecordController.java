package com.erban.web.controller.record;


import com.erban.main.service.record.BillRecord2Service;
import com.erban.main.vo.BillRecordExpandVo;
import com.erban.main.vo.BillSearchVo;
import com.google.common.collect.Maps;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/billrecord")
public class BillRecordController {

    private static final Logger logger = LoggerFactory.getLogger(BillRecordController.class);

    @Autowired
    private BillRecord2Service billRecord2Service;


    /**
     * 获取指定类型的账单记录（除红包外）
     *
     * @param uid    账号
     * @param type   记录类型
     * @param date   查询日期
     * @param pageNo 当前页码
     * @return
     */
    @RequestMapping(value = "get")
    @ResponseBody
    @Authorization
    public BusiResult getBillRecord(long uid, byte type, long date, int pageNo, Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            pageSize = 50;
        }
        List<BillRecordExpandVo> list = billRecord2Service.getBillRecord(uid, type, date, pageNo, pageSize);
        List<LinkedHashMap<Long, List<BillSearchVo>>> reList = billRecord2Service.transToSearchVo(list, type);
        LinkedHashMap<String, List<LinkedHashMap<Long, List<BillSearchVo>>>> reMap = Maps.newLinkedHashMap();
        reMap.put("billList", reList);
        BusiResult<LinkedHashMap<String, List<LinkedHashMap<Long, List<BillSearchVo>>>>> busiResult =
                new BusiResult<LinkedHashMap<String, List<LinkedHashMap<Long, List<BillSearchVo>>>>>(BusiStatus.SUCCESS);
        busiResult.setData(reMap);
        return busiResult;
    }


    public static void main(String[] args) {
        Date date =  DateTimeUtil.convertStrToDate("2018-01-09 00:00:00");
        System.out.println(date.getTime());
        System.out.println(new Date().getTime());
    }
}
