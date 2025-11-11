package com.erban.web.controller.statis;

import com.erban.main.service.statis.StatSendGiftSumListService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/giftsumlist")
@Controller
public class StatSendGiftSumListController {

    @Autowired
    private StatSendGiftSumListService statSendGiftSumListService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult queryGiftList(Long roomUid) {
        BusiResult busiResult = null;
        try {
            busiResult = statSendGiftSumListService.queryGiftLists(roomUid);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }
}
