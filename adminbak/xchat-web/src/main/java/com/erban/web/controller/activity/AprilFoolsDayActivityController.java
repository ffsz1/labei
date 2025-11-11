package com.erban.web.controller.activity;


import com.erban.main.service.activity.AprilFoolsDayActivityService;
import com.erban.web.common.BaseController;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/aprilFoolsDay")
public class AprilFoolsDayActivityController extends BaseController {
    @Autowired
    private AprilFoolsDayActivityService aprilFoolsDayActivityService;

    @RequestMapping(value = "rank",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult queryActivityHomeList(Integer type){
        if(type==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return aprilFoolsDayActivityService.queryList(type);
    }

}
