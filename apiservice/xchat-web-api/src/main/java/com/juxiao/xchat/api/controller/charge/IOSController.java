package com.juxiao.xchat.api.controller.charge;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.utils.MD5Utils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.charge.IOSChargeService;
import com.juxiao.xchat.service.api.charge.ret.ReceiptRet;
import com.juxiao.xchat.service.api.charge.vo.ReceiptVO;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/verify")
@Api(description = "充值接口", tags = "充值接口")
public class IOSController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ActiveMqManager activeMqManager;
    @Autowired
    private IOSChargeService chargeService;

    @ApiOperation(value = "接收iOS端发过来的购买凭证", notes = "com.juxiao.xchat.api.controller.charge.IOSController")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "chargeRecordId", value = "订单号", dataType = "string", required = true),
            @ApiImplicitParam(name = "chooseEnv", value = "充值环境：true，沙箱；false，生产", dataType = "string", required = true),
            @ApiImplicitParam(name = "receipt", value = "苹果回调购买凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "isJailbroken", value = "客户端是否越狱：0，非越狱；1，越狱", dataType = "int", required = true),
            @ApiImplicitParam(name = "trancid", value = "购买成功之后苹果的订单ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = ReceiptVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/setiap", method = RequestMethod.POST)
    public WebServiceMessage verifyReceipt(HttpServletRequest request,
                                           @RequestParam(value = "uid") String uid,
                                           @RequestParam(value = "chargeRecordId") String chargeRecordId,
                                           @RequestParam(value = "chooseEnv") String chooseEnv,
                                           @RequestParam(value = "receipt") String receipt,
                                           @RequestParam(value = "isJailbroken", required = false) Integer isJailbroken,
                                           @RequestParam(value = "trancid", required = false) String trancid,
                                           @RequestParam(value = "os", required = false) String os,
                                           @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        ReceiptRet receiptRet = chargeService.verifyReceipt(uid, chooseEnv, receipt, isJailbroken, trancid, os, appVersion);
        String ip = HttpServletUtils.getRealIp(request);
        ReceiptVO receiptVo = chargeService.updateChargeRecord(ip, Long.valueOf(uid), chargeRecordId, receiptRet, MD5Utils.encode(receipt), trancid);
        return WebServiceMessage.success(receiptVo);
    }
}
