package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.StatPacketActivityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statbouns")
@Api(tags = "用户信息接口",description = "用户信息接口")
public class StatPacketBounsController {
    @Autowired
    private StatPacketActivityService packetActivityService;

    /**
     * 获取用户分成详情
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public WebServiceMessage getStatBounsDetail(@RequestParam("uid") Long uid) throws WebServiceException {
        return WebServiceMessage.success(packetActivityService.getBounsDetail(uid));
    }
}
