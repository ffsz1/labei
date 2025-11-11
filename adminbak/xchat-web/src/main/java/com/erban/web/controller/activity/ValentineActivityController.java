package com.erban.web.controller.activity;

import com.erban.main.service.activity.ValentineActivityService;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/activity/valentine")
public class ValentineActivityController {
    @Autowired
    private ValentineActivityService valentineActivityService;

    @RequestMapping(value = "get",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult get(Long uid){
        if(uid==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return valentineActivityService.get(uid);
    }

    @RequestMapping(value = "build",method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult build(Long uid, Long userNo){
        if(uid==null||userNo==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return valentineActivityService.build(uid, userNo);
    }

    @RequestMapping(value = "remove",method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult remove(Long uid){
        if(uid==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return valentineActivityService.remove(uid);
    }

    @RequestMapping(value = "rank",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult rank(){
        return valentineActivityService.getRank();
    }

}
