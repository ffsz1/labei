package com.erban.web.controller.statis;

import com.erban.main.service.statis.StatBasicUsersService;
import com.erban.main.service.statis.StatUserStandTimeService;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/basicusers")
@Controller
public class StatBasicUsersController {
    private static final Logger logger = LoggerFactory.getLogger(StatBasicUsersController.class);

    @Autowired
    private StatBasicUsersService statBasicUsersService;

    @Autowired
    private StatUserStandTimeService statUserStandTimeService;

    @RequestMapping(value = "/record", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult setBasicUser(Long uid, Long roomUid,String channel,String ispType,String
            model,String netType,String os,String osVersion) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (uid == null || roomUid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (uid.equals(roomUid)) {
            return busiResult;
        }
        busiResult = statBasicUsersService.addBasicUser(uid, roomUid);
        return busiResult;
    }

}

