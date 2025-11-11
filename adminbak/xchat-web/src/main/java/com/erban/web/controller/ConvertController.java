package com.erban.web.controller;

import com.erban.main.service.ConverService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/convertweek")
@Controller
public class ConvertController {

    @Autowired
    private ConverService converService;

    @RequestMapping("/weeklist")
    @ResponseBody
    public BusiResult converWeek() {
        BusiResult busiResult = null;
        try {
            busiResult = converService.converWeek();
        } catch (Exception e) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }
}
