package com.erban.admin.web.controller.room;

import com.erban.main.service.CleanExceptionRoomService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuguofu on 2017/10/5.
 */
@Controller
@RequestMapping("/cleanroom")
public class CleanExceptionRoomController {

    @Autowired
    private CleanExceptionRoomService cleanExceptionRoomService;

    @RequestMapping(value = "clean", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult cleanExceptionRoom() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            cleanExceptionRoomService.cleanExceptionRoom();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return busiResult;
    }

}
