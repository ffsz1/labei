package com.erban.web.controller.activity;

import com.erban.main.service.activity.ActivityService;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 查询活动
     * @param type  弹窗位置
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "query",method = RequestMethod.GET)
    public BusiResult queryActivityHomeList(Integer type, Long uid){
        if(type==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return activityService.queryActivityList(type, uid);
    }

}
