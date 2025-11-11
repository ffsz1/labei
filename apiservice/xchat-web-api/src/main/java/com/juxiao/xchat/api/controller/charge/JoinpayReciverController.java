package com.juxiao.xchat.api.controller.charge;

import com.juxiao.xchat.manager.external.joinpay.vo.JoinpayReciver;
import com.juxiao.xchat.service.api.charge.ChargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/joinpay")
@Api(description = "充值接口", tags = "充值接口")
public class JoinpayReciverController {
    public static final Logger logger = LoggerFactory.getLogger(JoinpayReciverController.class);
    @Autowired
    private ChargeService chargeService;

    @ApiOperation(value = "汇聚支付回调", hidden = true)
    @RequestMapping(value = "/callback", method = {RequestMethod.POST, RequestMethod.GET})
    public String pingxxCallBack(JoinpayReciver joinpayReciver) throws Exception {
        logger.info("[ 汇聚回调 ] 请求:>{}，返回:>{}", joinpayReciver);
        chargeService.reciveJoinpayCharge(joinpayReciver);
        return "success";
    }

}
