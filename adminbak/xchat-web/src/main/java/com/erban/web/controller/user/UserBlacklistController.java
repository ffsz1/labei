package com.erban.web.controller.user;

import com.erban.main.service.user.UserBlacklistService;
import com.erban.main.util.StringUtils;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/blacklist")
public class UserBlacklistController extends BaseController {
    @Autowired
    private UserBlacklistService userBlacklistService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getBlacklist(Long uid, Integer pageNum, Integer pageSize) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        return new BusiResult(BusiStatus.SUCCESS, userBlacklistService.toVo(userBlacklistService.getBlacklist(uid, pageNum, pageSize)));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult add(Long uid, Long blacklistUid) {
        if (uid == null||blacklistUid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        return userBlacklistService.add(uid, blacklistUid);
    }

    @RequestMapping(value = "del", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult del(Long uid, String blacklistIds) {
        if (uid == null|| StringUtils.isBlank(blacklistIds)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        return userBlacklistService.del(uid, blacklistIds);
    }

}
