package com.juxiao.xchat.api.controller.charge;

import com.juxiao.xchat.manager.external.ecpss.bo.OrderCallBackBO;
import com.juxiao.xchat.service.api.charge.ChargeService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chris
 * @date 2019-07-09
 */
@RestController
@RequestMapping("charge/ecpss")
public class EcpssReciverController {

    public final Logger logger = LoggerFactory.getLogger(JoinpayReciverController.class);
    @Autowired
    private ChargeService chargeService;

    @ApiOperation(value = "汇潮支付回调 Controller", hidden = true)
    @RequestMapping(value = "/callback", method = {RequestMethod.POST, RequestMethod.GET})
    public String pingxxCallBack(OrderCallBackBO callBackBO) throws Exception {
        logger.info("[ 汇潮支付回调 ] 请求:>{}，返回:>{}", callBackBO);
        return chargeService.reciveEcpssCharge(callBackBO);
    }
}
