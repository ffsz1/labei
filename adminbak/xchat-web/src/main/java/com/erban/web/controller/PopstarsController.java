package com.erban.web.controller;

import com.erban.main.service.PopstarsService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/popstars")
public class PopstarsController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PopstarsController.class);
    @Autowired
    private PopstarsService popstarsService;

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult followSendGold(String user, String openId, Byte wxGender) {
        BusiResult busiResult = null;
        logger.info("进入关注公众号送金币接口.user:" + user);
        if (user == null || openId == null || wxGender == null) {
            logger.info("进入关注公众号送金币接口参数错误.");
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        try {
            busiResult = popstarsService.followSendGold(user, openId, wxGender);
        } catch (Exception e) {
            busiResult = new BusiResult(BusiStatus.BUSIERROR);
            logger.error("followSendGold error..user="+user+"&openId="+openId+"&wxGender="+wxGender);
        }
        return busiResult;
    }

    @RequestMapping(value = "/geterban", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getErbanUser(String user) {
        BusiResult busiResult = null;
        if (user == null) {
            logger.info("关注公众号模块，查询用户参数错误。");
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        busiResult = popstarsService.getErBanUser(user);
        return busiResult;
    }

}
