package com.juxiao.xchat.record.controller.packet;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.dto.PacketRecordDTO;
import com.juxiao.xchat.service.record.packet.PacketRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @class: PacketRecordController.java
 * @author: chenjunsheng
 * @date 2018/6/4
 */
@RestController
@RequestMapping("/packetrecord")
public class PacketRecordController {
    @Autowired
    private PacketRecordService packetService;

    /**
     * 获取红包账单记录（提现红包类型除外）
     *
     * @param uid      账号
     * @param date     查询日期
     * @param pageNo   当前页码
     * @param pageSize 每页数据
     * @return
     * @author: chenjunsheng
     * @date 2018/6/4
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "get")
    public WebServiceMessage listPacketRecord(@RequestParam(value = "uid") long uid,
                                              @RequestParam(value = "date") long date,
                                              @RequestParam(value = "pageNo") int pageNo,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        try {
            Map<String, List<Map<Long, List<PacketRecordDTO>>>> record = packetService.listPacketRecord(uid, date, pageNo, pageSize);
            return WebServiceMessage.success(record);
        } catch (WebServiceException e) {
            return WebServiceMessage.failure(e);
        }
    }


    /**
     * 获取红包提现记录
     *
     * @param uid
     * @param date
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "deposit")
    public WebServiceMessage getPacketDepositRecord(@RequestParam(value = "uid") long uid,
                                                    @RequestParam(value = "date") long date,
                                                    @RequestParam(value = "pageNo") int pageNo,
                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        try {
            Map<String, List<Map<Long, List<PacketRecordDTO>>>> record = packetService.listPacketDeposiRecord(uid, date, pageNo, pageSize);
            return WebServiceMessage.success(record);
        } catch (WebServiceException e) {
            return WebServiceMessage.failure(e);
        }
    }

}
