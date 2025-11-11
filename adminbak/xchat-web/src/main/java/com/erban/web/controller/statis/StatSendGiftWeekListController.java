package com.erban.web.controller.statis;

import com.erban.main.service.statis.StatSendGiftWeekListService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/giftweeklist")
public class StatSendGiftWeekListController {
    private static final Logger logger = LoggerFactory.getLogger(StatSendGiftWeekListController.class);
    @Autowired
    private StatSendGiftWeekListService statSendGiftWeekListService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult queryGiftList(Long roomUid) {
        BusiResult busiResult = null;
        try {
            busiResult = statSendGiftWeekListService.queryGiftLists(roomUid);
        } catch (Exception e) {
            logger.error("query error..roomUid=" + roomUid, e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }
}
