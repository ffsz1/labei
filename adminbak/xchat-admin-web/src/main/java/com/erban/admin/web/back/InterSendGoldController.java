package com.erban.admin.web.back;

import com.erban.admin.main.service.InterSendGoldService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/intergold")
public class InterSendGoldController {

    @Autowired
    private InterSendGoldService interSendGoldService;

    @RequestMapping("/charge")
    @ResponseBody
    public BusiResult interSendGold(Long erbanNo, Long goldNum) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (erbanNo == null || goldNum == null) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        try {
            busiResult = interSendGoldService.intelSendGold(erbanNo, goldNum);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @RequestMapping("/chargeds")
    @ResponseBody
    public BusiResult interSendGoldList(Long[] uids, Long goldNum) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (uids == null || uids.length == 0 || goldNum == null) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        try {
            busiResult = interSendGoldService.intelSendGoldList(uids, goldNum);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

}
