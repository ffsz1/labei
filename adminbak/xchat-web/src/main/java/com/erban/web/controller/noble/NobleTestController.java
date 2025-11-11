package com.erban.web.controller.noble;

import com.erban.main.model.NobleRight;
import com.erban.main.model.NobleUsers;
import com.erban.main.model.Users;
import com.erban.main.service.noble.NobleHelperService;
import com.erban.main.service.noble.NobleMessageService;
import com.erban.main.service.noble.NobleRightService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.service.user.UsersService;
import com.erban.web.common.BaseController;
import com.xchat.common.netease.util.NetEaseConstant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/noble/test")
public class NobleTestController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(NobleTestController.class);

    @Autowired
    private NobleUsersService nobleUsersService;
    @Autowired
    private NobleHelperService nobleHelperService;
    @Autowired
    private NobleMessageService nobleMessageService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private NobleRightService nobleRightService;


    /**
     * 贵族快过期，发送小秘书提醒
     *
     * @param dd
     * @return
     */
    @RequestMapping(value = "/sendnotice")
    @ResponseBody
    public BusiResult sendNotice(int dd) {
        nobleUsersService.sendWillExpireNotice();
        return new BusiResult(BusiStatus.SUCCESS);
    }

    @RequestMapping(value = "/release")
    @ResponseBody
    public BusiResult releaseExpireNoble(int dd) {
        nobleUsersService.releaseAllExpireNoble();
        return new BusiResult(BusiStatus.SUCCESS);
    }


    @RequestMapping(value = "/sendmsg")
    @ResponseBody
    public BusiResult sendErbanNotice(long uid, String nobleName) {
        nobleHelperService.sendNobleHadExpireMess(uid, nobleName);
        nobleHelperService.sendNobleOpenMess(uid, nobleName);
        nobleHelperService.sendNobleGoodNumFailMess(uid);
        nobleHelperService.sendNobleWillExpireMess(uid, nobleName);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    @RequestMapping(value = "/sendbroadcast")
    @ResponseBody
    public BusiResult sendBroadCastMsg(long uid, byte optType, Long roomUid) {
        NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
        if (nobleUsers == null) {
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        Users users = usersService.getUsersByUid(uid);
        NobleRight nobleRight = nobleRightService.getNobleRight(nobleUsers.getNobleId());
        nobleMessageService.sendBroadCastMessage(users, nobleRight, optType, roomUid);
        return new BusiResult(BusiStatus.SUCCESS);
    }


    @RequestMapping(value = "/del")
    @ResponseBody
    public BusiResult delNobleUser(long uid) {
        logger.info("delNobleUser key:{}, secret:{}", NetEaseConstant.appKey, NetEaseConstant.appSecret);
        NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
        if (nobleUsers == null) {
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        nobleUsersService.releaseExpireNoble(nobleUsers);
        return new BusiResult(BusiStatus.SUCCESS);
    }
}
