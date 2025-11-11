package com.erban.web.controller.activity;

import com.erban.main.service.activity.ActivityHtmlService;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/activity/html")
public class ActivityHtmlController {
    @Autowired
    private ActivityHtmlService activityHtmlService;

    @ResponseBody
    @RequestMapping(value = "rank",method = RequestMethod.GET)
    public BusiResult queryActivityHomeList(Integer type){
        if(type==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return activityHtmlService.queryList(type);
    }

}
