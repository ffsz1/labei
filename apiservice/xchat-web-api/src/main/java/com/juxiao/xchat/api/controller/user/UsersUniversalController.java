package com.juxiao.xchat.api.controller.user;


import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.UsersUniversalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户提现白名单
 * @author BigCat
 */
@RestController
@RequestMapping("/user/universal")
public class UsersUniversalController {

    @Autowired
    private UsersUniversalService usersUniversalService;


    /**
     * 检测用户提现开关
     * @param uid uid
     * @return WebServiceMessage
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "checkWithdrawWhitelist", method = RequestMethod.GET)
    public WebServiceMessage checkWithdrawWhitelist(@RequestParam("uid") Long uid) {
        return WebServiceMessage.success(usersUniversalService.checkUsersWithdrawWhitelist(uid));
    }
}
