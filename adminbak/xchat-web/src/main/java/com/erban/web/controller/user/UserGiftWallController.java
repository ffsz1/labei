package com.erban.web.controller.user;

import com.erban.main.service.user.UserGiftWallService;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuguofu on 2017/10/17.
 */
@Controller
@RequestMapping( "/giftwall" )
public class UserGiftWallController {
    private static final Logger logger = LoggerFactory.getLogger (UserGiftWallController.class);
    @Autowired
    private UserGiftWallService userGiftWallService;

    @RequestMapping ( value = "get", method = RequestMethod.GET )
    @ResponseBody
    @SignVerification
    public BusiResult getUserWallListByUid(Long uid, int orderType){
        BusiResult busiResult=null;
        try {
            busiResult=userGiftWallService.getUserWallListByUid(uid,orderType);
        } catch (Exception e) {
            logger.error("getUserWallListByUid error",e);
            busiResult=new BusiResult(BusiStatus.BUSIERROR);
            return busiResult;
        }
        return  busiResult;
    }

    @ResponseBody
    @RequestMapping ( value = "getFullUserList", method = RequestMethod.GET )
    public BusiResult getFullUserList(Integer pageSize){
        try {
            return userGiftWallService.getFullUserList(pageSize);
        } catch (Exception e) {
            logger.error("getFullUserList error",e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}
