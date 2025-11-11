package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.AccountBannedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banned")
@Api(tags = "用户配置接口", description = "用户配置接口")
public class AccountBannedController {

    @Autowired
    private AccountBannedService accountBannedService;

    /**
     * 检测禁言
     *
     * @param uid
     */
    @SignVerification
    @RequestMapping(value = "checkBanned", method = RequestMethod.GET)
    @ApiOperation(value = "检测禁言", notes = "根据uid是否被检测禁言")
    public WebServiceMessage checkBanned(@RequestParam("uid") Long uid) throws Exception {
        //
        return WebServiceMessage.success(accountBannedService.checkBanned(uid));
    }


}
