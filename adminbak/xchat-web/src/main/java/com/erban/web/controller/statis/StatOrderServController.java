package com.erban.web.controller.statis;

import com.erban.main.service.statis.StatOrderServService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/basicorder")
public class StatOrderServController {

    @Autowired
    private StatOrderServService statOrderServService;

    @RequestMapping(value = "/avgchattime", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getAvgChatTime() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult = statOrderServService.getAvgChatTime();
        return busiResult;
    }

    @RequestMapping(value = "/chattime", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getChatTime(Long orderId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult = statOrderServService.getChatTime(orderId);
        return busiResult;
    }

    @RequestMapping(value = "/orderprop", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getChatProp() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult = statOrderServService.getOrderProp();
        return busiResult;
    }

}
