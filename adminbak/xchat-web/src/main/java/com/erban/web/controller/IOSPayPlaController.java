package com.erban.web.controller;

import com.erban.main.service.IOSPayPlaService;
import com.erban.main.vo.RecordIdVo;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 北岭山下 on 2017/7/12.
 */

@Controller
@RequestMapping("/order")
public class IOSPayPlaController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeController.class);


    //==========
    @Autowired
    private IOSPayPlaService iosPayPlaService;


    @ResponseBody
    @Authorization
    @RequestMapping("place")
    public BusiResult<RecordIdVo> placeOrder(Long uid, String chargeProdId, String clientIp) {

        logger.info("发起支付order/place..uid=" + uid + "&chargeProdId=" + chargeProdId + "&clientIp=" + clientIp);
        BusiResult<RecordIdVo> busiResult = iosPayPlaService.placeOrder(uid, chargeProdId, clientIp);
        return busiResult;
    }


}
