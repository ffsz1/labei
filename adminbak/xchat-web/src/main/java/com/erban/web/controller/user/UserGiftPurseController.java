package com.erban.web.controller.user;

import com.erban.main.service.user.UserGiftPurseService;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/giftPurse")
public class UserGiftPurseController extends BaseController {
    @Autowired
    private UserGiftPurseService userGiftPurseService;

    /**
     * 发起一次抽奖
     *
     **/
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult doDraw(Long uid, Integer type, Long roomId) {
        if(uid==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return userGiftPurseService.doDraw(uid, type, roomId);
        } catch (Exception e) {
            logger.error("/user/giftPurse/draw error " ,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "/getRank", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getRank(Integer type) {
        try {
            return userGiftPurseService.getRank(type);
        } catch (Exception e) {
            logger.error("/user/giftPurse/getRank error " ,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}
