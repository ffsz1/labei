package com.erban.web.controller;

import com.erban.main.model.UserPurse;
import com.erban.main.model.Users;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.user.UserPurseService;
import com.xchat.common.UUIDUitl;
import com.xchat.oauth2.service.service.account.NetEaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by PaperCut on 2018/2/2.
 */
@Controller
@RequestMapping("/test/netease")
public class NeteaseTestController {
    @Autowired
    ErBanNetEaseService erBanNetEaseService;
    @Autowired
    UserPurseService userPurseService;
    @Autowired
    NetEaseService netEaseService;

    @RequestMapping("get")
    public String test(Long uid){
        UserPurse userPurse = userPurseService.getPurseByUid(uid);
        try {
            userPurseService.sendSysMsgByModifyGold(userPurse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "test";
    }

    @RequestMapping("register")
    public void register(String uid) throws Exception {
        netEaseService.createNetEaseAcc(uid, UUIDUitl.get(), "");
    }
}
