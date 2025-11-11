package com.juxiao.xchat.record.controller.bill;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.record.bill.BillRecordService;
import io.swagger.annotations.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @class: BillRecordController.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
@RestController
@RequestMapping(value = "billrecord")
@Api(value="账单记录接口",tags={"账单记录接口"})
public class BillRecordController {
    @Autowired
    private BillRecordService recordService;

    /**
     * 获取指定类型的账单记录
     *
     * @param uid    账号
     * @param type   记录类型：1，礼物支出记录；2，礼物收入记录；5，提现记录 6,赠送金币记录
     * @param date   查询日期
     * @param pageNo 当前页码
     * @return
     * @author: chenjunsheng
     * @date 2018/6/5
     */
    @ApiOperation(value = "账单记录", notes = "需要加密、需要ticket")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "type", value = "记录类型：1，礼物支出记录；2，礼物收入记录；5，提现记录 6, 赠送金币记录", dataType = "long", required = true),
            @ApiImplicitParam(name = "date", value = "查询日期", dataType = "long", required = true),
            @ApiImplicitParam(name = "pageNo", value = "当前页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "单页长度", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public WebServiceMessage listBillRecord(@RequestParam(value = "uid") Long uid,
                                            @RequestParam("type") Byte type,
                                            @RequestParam("date") Long date,
                                            @RequestParam("pageNo") int pageNo,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        try {
            List<JSONObject> list = recordService.listBillRecord(uid, type, date, pageNo, pageSize);
            Map<String, Object> result = new HashMap<>();
            result.put("billList", list);
            return WebServiceMessage.success(result);
        } catch (WebServiceException e) {
            return WebServiceMessage.failure(e);
        }
    }
}
