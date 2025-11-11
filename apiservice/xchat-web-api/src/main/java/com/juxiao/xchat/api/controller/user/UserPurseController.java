package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.UserPurseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purse")
@Api(tags = "用户信息接口",description = "用户信息接口")
public class UserPurseController {
    @Autowired
    private UserPurseService userPurseService;

    /**
     * 查询用户钱包
     *
     * @param uid
     * @return
     */
    @Authorization
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public WebServiceMessage getUserPurse(@RequestParam("uid") Long uid) throws WebServiceException {
        return WebServiceMessage.success(userPurseService.getUserPurse(uid));
    }

    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v2/query", method = RequestMethod.GET)
    public WebServiceMessage getUserPurseV2(@RequestParam("uid") Long uid) throws WebServiceException {
        return WebServiceMessage.success(userPurseService.getUserPurse(uid));
    }

    @RequestMapping(value = "queryFirst", method = RequestMethod.GET)
    public WebServiceMessage queryFirst(Long uid) throws WebServiceException {
        return WebServiceMessage.success(userPurseService.queryFirst(uid));
    }

    /**
     * 房主15%分成发放
     * @param date 时间 yyyy-MM-dd
     * @return
     * @throws WebServiceException
     */
//    @RequestMapping(value = "houseOwnerShare", method = RequestMethod.POST)
//    public WebServiceMessage houseOwnerShare(String date, Long uid) throws WebServiceException {
//        userPurseService.houseOwnerShare(date, uid);
//        return WebServiceMessage.success("操作成功");
//    }

}
