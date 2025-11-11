package com.juxiao.xchat.api.controller.charge;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.charge.ChargeProdService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 充值产品请求接口类
 */
@RestController
@RequestMapping("/chargeprod")
@Api(description = "充值接口", tags = "充值接口")
public class ChargeProdController {
    @Autowired
    private ChargeProdService chargeProdService;

    /**
     * 获取产品充值列表
     *
     * @param channelType
     * @param os
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public WebServiceMessage listChargeProd(@RequestParam("channelType") Integer channelType,
                                            @RequestParam(value = "os", required = false) String os,
                                            @RequestParam(value = "app", required = false) String app,
                                            @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        return WebServiceMessage.success(chargeProdService.listChargeProd(channelType, os, app, appVersion));
    }
}