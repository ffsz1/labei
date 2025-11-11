package com.erban.web.controller.gift;

import com.erban.main.service.gift.GiftCarService;
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
@RequestMapping("/giftCar")
public class GiftCarController extends BaseController {
    @Autowired
    private GiftCarService giftCarService;

    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BusiResult list(Long uid, Integer pageNum, Integer pageSize) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return new BusiResult(BusiStatus.SUCCESS, giftCarService.getCat(uid, pageNum, pageSize));
    }

    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/purse", method = RequestMethod.POST)
    public BusiResult purse(Long uid, Integer carId, Integer type) {
        if (uid == null || carId == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return giftCarService.purse(uid, carId, type);
    }

    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/use", method = RequestMethod.POST)
    public BusiResult use(Long uid, Integer carId) {
        if (uid == null || carId == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return giftCarService.use(uid, carId);
    }

    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/give", method = RequestMethod.POST)
    public BusiResult give(Long uid, Integer carId, Long targetUid) {
        if (uid == null || carId == null || targetUid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return giftCarService.give(uid, carId, targetUid);
    }

}
