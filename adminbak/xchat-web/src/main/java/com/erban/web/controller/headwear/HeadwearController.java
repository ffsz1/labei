package com.erban.web.controller.headwear;

import com.erban.main.service.headwear.HeadwearService;
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
@RequestMapping("/headwear")
public class HeadwearController extends BaseController {
    @Autowired
    private HeadwearService headwearService;

    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BusiResult list(Long uid, Integer pageNum, Integer pageSize) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return new BusiResult(BusiStatus.SUCCESS, headwearService.getHeadwear(uid, pageNum, pageSize));
    }

    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/purse", method = RequestMethod.POST)
    public BusiResult purse(Long uid, Integer headwearId, Integer type) {
        if (uid == null || headwearId == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return headwearService.purse(uid, headwearId, type);
    }

    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/use", method = RequestMethod.POST)
    public BusiResult use(Long uid, Integer headwearId) {
        if (uid == null || headwearId == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return headwearService.use(uid, headwearId);
    }

    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/give", method = RequestMethod.POST)
    public BusiResult give(Long uid, Integer headwearId, Long targetUid) {
        if (uid == null || headwearId == null || targetUid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return headwearService.give(uid, headwearId, targetUid);
    }

}
