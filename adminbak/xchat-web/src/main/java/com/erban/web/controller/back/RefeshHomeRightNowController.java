package com.erban.web.controller.back;

import com.erban.main.service.CleanExceptionRoomService;
import com.erban.main.service.HomeService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuguofu on 2017/10/6.
 */
@Controller
@RequestMapping("/refreshhome")
public class RefeshHomeRightNowController {

    @Autowired
    private HomeService homeService;
    @RequestMapping(value = "refresh",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult cleanExceptionRoom(){
        BusiResult busiResult= new BusiResult(BusiStatus.SUCCESS);
        try {
            homeService.refreshHomData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return busiResult;
    }

}
