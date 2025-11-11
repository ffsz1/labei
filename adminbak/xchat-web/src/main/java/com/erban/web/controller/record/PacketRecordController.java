package com.erban.web.controller.record;


import com.erban.main.model.UserPacketRecord;
import com.erban.main.service.record.UserPacketRecord2Service;
import com.erban.main.vo.PacketRecordVo;
import com.google.common.collect.Maps;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/packetrecord")
public class PacketRecordController {

    private static final Logger logger = LoggerFactory.getLogger(PacketRecordController.class);

    @Autowired
    private UserPacketRecord2Service packetRecord2Service;

    /**
     * 获取红包账单记录（提现红包类型除外）
     *
     * @param uid  账号
     * @param date 查询日期
     * @param pageNo 当前页码
     * @return
     */
    @RequestMapping(value = "get")
    @ResponseBody
    @Authorization
    public BusiResult getPacketRecord(long uid, long date, int pageNo, Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            pageSize = 50;
        }
        List<UserPacketRecord> list = packetRecord2Service.getUserPacketRecord(uid, date, pageNo, pageSize);
        List<LinkedHashMap<Long, List<PacketRecordVo>>> reList = packetRecord2Service.transToSearchVo(list);
        LinkedHashMap<String, List<LinkedHashMap<Long, List<PacketRecordVo>>>> reMap = Maps.newLinkedHashMap();
        reMap.put("billList", reList);
        BusiResult<LinkedHashMap<String, List<LinkedHashMap<Long, List<PacketRecordVo>>>>> busiResult = new BusiResult<LinkedHashMap<String, List<LinkedHashMap<Long, List<PacketRecordVo>>>>>(BusiStatus.SUCCESS);
        busiResult.setData(reMap);
        return busiResult;
    }


    /**
     *  红包提现
     * @param uid
     * @param date
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "deposit")
    @ResponseBody
    @Authorization
    public BusiResult getPacketDepositRecord(long uid, long date, int pageNo, Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            pageSize = 50;
        }
        List<UserPacketRecord> list = packetRecord2Service.getPacketDeposiRecord(uid, date, pageNo, pageSize);
        List<LinkedHashMap<Long, List<PacketRecordVo>>> reList = packetRecord2Service.transToSearchVo(list);
        LinkedHashMap<String, List<LinkedHashMap<Long, List<PacketRecordVo>>>> reMap = Maps.newLinkedHashMap();
        reMap.put("billList", reList);
        BusiResult<LinkedHashMap<String, List<LinkedHashMap<Long, List<PacketRecordVo>>>>> busiResult = new BusiResult<LinkedHashMap<String, List<LinkedHashMap<Long, List<PacketRecordVo>>>>>(BusiStatus.SUCCESS);
        busiResult.setData(reMap);
        return busiResult;
    }

}
